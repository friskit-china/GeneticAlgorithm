/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.friskit.hw.geneticalgorithm;

import java.util.Random;


/**
 *
 * @author shibotian
 */
public class GAUtil {
    
    /**
     * 适应度函数
     */
    public static int calcFitness(int geneCode[]){
        int t = GAUtil.getDecValue(geneCode);
        return t*t;
    }
    
    public static int getDecValue(int geneCode[]){
        int rank = 1;
        int value = 0;
        for(int i = geneCode.length-1;i>=0;i--){   //Bin2Dec
            value+=geneCode[i]*rank;
            rank*=2;
        }
        return value;
    }
    
    
    /**
     * 交叉算法
     * 此处将两个基因的后三位交叉
     */
//    public static void crossGene(Gene gene1, Gene gene2){
//        int position1 = (int)(new Random().nextDouble());
//        int position2 = (int)(new Random().nextDouble());
//        int position3 = (int)(new Random().nextDouble());
//        
//        int t1 = gene1.getBit(position1);
//        gene1.setBit(gene2.getBit(position1), position1);
//        
//        int t2 = gene1.getBit(position2);
//        gene1.setBit(gene2.getBit(position2), position2);
//        
//        int t3 = gene1.getBit(position3);
//        gene1.setBit(gene2.getBit(position3), position3);
//    }
//    
public static void crossGene(Gene gene1, Gene gene2){
        int t1 = gene1.getBit(2);
        int t2 = gene1.getBit(3);
        int t3 = gene1.getBit(4);
        
        gene1.setBit(gene2.getBit(2), 2);
        gene1.setBit(gene2.getBit(3), 3);
        gene1.setBit(gene2.getBit(4), 4);
        
        gene2.setBit(2, t1);
        gene2.setBit(3, t2);
        gene2.setBit(4, t3);
    }
    
    
    /**
     * 变异算法 
     * 此处随机产生一位变异
     */
    public static void aberrationGene(Gene gene){
        int position = (int)(new Random().nextDouble()*5);
        int number=gene.getBit(position)==1?0:1;
        gene.setBit(position, number);
    }
}
