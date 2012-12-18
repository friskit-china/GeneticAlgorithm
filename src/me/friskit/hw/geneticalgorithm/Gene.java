package me.friskit.hw.geneticalgorithm;

/**
 *
 * @author shibotian
 */
public class Gene {
    private int geneCode[];
    private int fitness;
    
    public Gene(int a[]){
        this.geneCode = new int[5];
        System.arraycopy(a, 0, this.geneCode, 0, 5);
        this.fitness = GAUtil.calcFitness(geneCode);        //it may not works!!!!!!!  (use this before constructor finish)
    }
    
    public int[] getGeneCode(){
        return this.geneCode;
    }
    
    public int getFitness(){
        return this.fitness;
    }
    
    public int getBit(int position){
        return geneCode[position];
    }
    
    public void setBit(int position,int number){
        if(number==1||number==0){
            geneCode[position]=number;
        }
        this.fitness = GAUtil.calcFitness(geneCode);
    }
}
