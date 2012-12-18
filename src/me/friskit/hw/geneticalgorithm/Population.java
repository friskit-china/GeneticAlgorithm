/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.friskit.hw.geneticalgorithm;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author shibotian
 */
public class Population extends ArrayList<Gene> {
    public Population(){
        super();  
    }
    
    /**
     * 获取总适应度 
     */
    public int getTotalFitness(){
        int totalFitness = 0;
        Iterator<Gene> it = this.iterator();
        while(it.hasNext()){
            totalFitness+=it.next().getFitness();
        }
        
        return totalFitness;
    }
    
    /**
     * 获取均值
     */
    public int getMean(){
        int mean = 0;
        Iterator<Gene> it = this.iterator();
        while(it.hasNext()){
            Gene g = it.next();
            mean+=g.getFitness();
        }
        mean/=this.size();
        return mean;
    }
    
    /**
     * 获取适应度最大的Gene 
     */
    public Gene getMostFitGene() throws Exception{   //unfinish
        int index = 0;
        int maxValue = 0;
        Gene maxGene = null;
        Iterator<Gene> it = this.iterator();
        
        if(it.hasNext()){
            maxGene = it.next();
        }else{
            throw new Exception("ERROR: Population is empty");
        }
        while(it.hasNext()){
            index++;
            Gene tGene = it.next();
            if(tGene.getFitness()>maxValue){
                maxValue = tGene.getFitness();
                maxGene = tGene;
            }
        }
        return maxGene;
    }
    
    /**
     * 插入时应更新均值和适应度
     */
    @Override
    public boolean add(Gene gene){
        //this.mean = (this.mean*this.size()+gene.getFitness())/(this.size()+1);
        return super.add(gene);
    }
}
