package edu.mcw.rgd.pipelines.proteinqc;

/**
 * Created by jthota on 12/4/2015.
 */
public class Genes {
    private String geneSymbol;
    private String proteinAccId;
    public void setGeneSymbol(String geneSymbol){
        this.geneSymbol = geneSymbol;
    }
    public String getGeneSymbol(){
        return this.geneSymbol;
    }
    public void setProteinAccId(String proteinAccId){
        this.proteinAccId = proteinAccId;
    }
    public String getProteinAccId(){
        return this.proteinAccId;
    }

}

