/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.friskit.hw.geneticalgorithm;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shibotian
 */
public class GeneticAlgorithm {
    /**
     * 控制参数
     */
    private int maxIterateTimes = 100000;      //终止条件1：最大迭代次数
    private int distanceThreshold = 1;          //终止条件2：收敛阈值
    private double crossingOverRate = 0.8;      //交叉率
    private double aberrationRate = 0.5;       //变异率
    private int moustFitness = 0;
            
    private boolean debugMode = false;
    
    /**
     * 运行参数
     */
    private int subgenerationCount = 0;     //子代数目
    
    
    /**
     * 成员变量
     */
    private Population population = null;
    
    
    public GeneticAlgorithm(){
        
        //初始化群落
        this.population = new Population();
        
        //初代群落
        population.add(new Gene(new int[]{0,1,1,0,1}));
        population.add(new Gene(new int[]{1,1,0,0,0}));
        population.add(new Gene(new int[]{0,1,0,0,0}));
        population.add(new Gene(new int[]{1,0,0,1,1}));
        
        while(!checkDestination()){         //未达终点
            //产生子代群落
            Population tmpPopulation = new Population();
            this.subgenerationCount++;
            
            /*遗传操作*/
            
            //1.选择复制
            //初始化选择概率
            double selectiveProbability[] = new double[population.size()];
            
            //初始化累积概率
            double accumulateProbability[] = new double[population.size()];
            
            //计算选择概率密度
            for(int i=0;i<population.size();i++){
                selectiveProbability[i] = (double)population.get(i).getFitness()/(double)population.getTotalFitness();
            }
            
            //计算累积概率密度s
            accumulateProbability[0] = selectiveProbability[0];
            for(int i=1;i<population.size();i++){
                accumulateProbability[i] = accumulateProbability[i-1]+selectiveProbability[i];
            }
            
            if(debugMode){
                System.out.println("子代"+this.subgenerationCount+":");
                System.out.println("基因型：");
                for(int i=0;i<population.size();i++){
                    Gene out = this.population.get(i);
                    for(int j=0;j<5;j++){
                        System.out.print(out.getGeneCode()[j]);
                    }
                    System.out.print("\t");
                }
                System.out.println("");
                
                System.out.println("选择概率：");
                for(int i=0;i<population.size();i++){
                    System.out.println(i+":"+selectiveProbability[i]);
                }
                
                System.out.println("累积概率:");
                for(int i=0;i<population.size();i++){
                    System.out.println(i+":"+accumulateProbability[i]);
                }
                System.out.println("-------------------------");
            }
            
            
            //初始化随机数生成器
            Random rdGenerator = new Random();
            
            //复制 population.size() 次
            for(int i=0;i<population.size();i++){
                double rand = rdGenerator.nextDouble();
                
                int j = 0;
                while(accumulateProbability[j]<rand){
                    j++;
                }
                Gene tg = new Gene(population.get(j).getGeneCode());
                tmpPopulation.add(tg);

            }
            
            //2.交叉
            //计算交叉数量
            int crossCount = (int)(this.crossingOverRate*tmpPopulation.size());
            for(int i=0;i<crossCount;i++){  //进行crossCount次交叉操作
                //确定要交叉的元素
                int geneIndex1 = (int)(rdGenerator.nextDouble()*tmpPopulation.size());
                int geneIndex2 = (int)(rdGenerator.nextDouble()*tmpPopulation.size());
                
                //判断两基因是否是同一个
                if(geneIndex1==geneIndex2){
                    i--;
                    continue;
                }
                
                //进行交叉
                GAUtil.crossGene(tmpPopulation.get(geneIndex1), tmpPopulation.get(geneIndex2));
            }
            
            //3.变异
            
            //计算变异数量
            int aberrationCount = (int)(this.aberrationRate*tmpPopulation.size());
            for(int i=0;i<aberrationCount;i++){ //进行aberrationCount次变异操作
                //确定要变异的元素
                int geneIndex = (int)(rdGenerator.nextDouble()*tmpPopulation.size());
                GAUtil.aberrationGene(tmpPopulation.get(geneIndex));
            }
            
            /*遗传操作结束*/
            //子代代替父代
            this.population = tmpPopulation;
            
            //保留新生代的最高适应度
            try{
                int mf = population.getMostFitGene().getFitness();
                this.moustFitness = mf>this.moustFitness?mf:this.moustFitness;
            }catch (Exception ex){
                Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //垃圾回收，影响速度。但如果种群占内存超过总内存一半，必须打开
            //System.gc();
        } 
        
        //已达终点
        try {
            Gene mostGene;
            mostGene = population.getMostFitGene();
            System.out.println("最优解："+mostGene.getFitness());
            System.out.print("最优解基因型：");
            for(int i=0;i<5;i++){
                System.out.print(mostGene.getBit(i));
            }
            System.out.println("\n迭代次数"+this.subgenerationCount);
        } catch (Exception ex) {
            Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    /**
     *
     * 终止条件判断
     * 1，子代数目达到最大迭代次数
     * 2，最大适应度值与平均适应度值相差不大
     */
    private boolean checkDestination(){
        if(this.subgenerationCount<this.maxIterateTimes){
            try {   //迭代尚未结束
                Gene mostGene = population.getMostFitGene();
                
                /**
                 * conditions是一系列收敛判断条件
                 */
                boolean condition1 = mostGene.getFitness()==population.getMean();
                boolean condition2 = population.getMostFitGene().getFitness()==this.moustFitness;
                
                if(condition1&&condition2){     //未收敛    
                    return true;
                }
            } catch (Exception ex) {
                Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
        return true;
    }
   
}
