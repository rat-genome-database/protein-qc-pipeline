package edu.mcw.rgd.proteinqc;

import java.util.List;
import java.util.Set;

/**
 * Created by jthota on 1/13/2016.
 */
public class TranscriptData {
    private Set<Integer> multiStopCodonTranscriptRgdIds;
    private List<Integer> multiStopCoodnVariantTranscriptIds;
    private Set<Integer> multiStopCodonVariantIds;
    private String chromosome;
    private int missingRefSeqCount;
    private Set<Integer> missingRefSeqIds;
    private int noProteinSeqCount;
    private Set<Integer> noProteinSeqIds;
    private int missingVtranscriptCount;
    private int emptyVTCount;

    public void setMultiStopCodonTranscriptRgdIds(Set<Integer> multiStopCodonTranscriptRgdIds){this.multiStopCodonTranscriptRgdIds=multiStopCodonTranscriptRgdIds;}
    public Set<Integer> getMultiStopCodonTranscriptRgdIds(){return this.multiStopCodonTranscriptRgdIds;}

    public void setMultiStopCoodnVariantTranscriptIds(List<Integer> multiStopCoodnVariantTranscriptIds){this.multiStopCoodnVariantTranscriptIds=multiStopCoodnVariantTranscriptIds;}
    public void setMultiStopCodonVariantIds(Set<Integer> variantIds) {this.multiStopCodonVariantIds= variantIds;}

    public List<Integer> getMultiStopCoodnVariantTranscriptIds(){return this.multiStopCoodnVariantTranscriptIds;}
    public Set<Integer> getMultiStopCodonVariantIds(){return this.multiStopCodonVariantIds;}

    public void setChromosome(String chromosome){this.chromosome=chromosome;}
    public String getChromosome(){return this.chromosome;}
    public void setMissingRefSeqCount(int missingRefSeqCount){this.missingRefSeqCount = missingRefSeqCount;}
    public int getMissingRefSeqCount(){return this.missingRefSeqCount;}
    public void setMissingRefSeqIds(Set<Integer> missingRefSeqIds){this.missingRefSeqIds = missingRefSeqIds;}
    public Set<Integer> getMissingRefSeqIds(){ return this.missingRefSeqIds;}
    public void setNoProteinSeqCount(int noProteinSeqCount){this.noProteinSeqCount=noProteinSeqCount;}
    public int getNoProteinSeqCount(){return this.noProteinSeqCount;}
    public void setNoProteinSeqIds(Set<Integer> noProteinSeqIds){this.noProteinSeqIds = noProteinSeqIds;}
    public Set<Integer> getNoProteinSeqIds(){return this.noProteinSeqIds;}
    public void setMissingVtranscriptCount(int missingVtranscriptCount){this.missingVtranscriptCount= missingVtranscriptCount;}
    public int getMissingVtranscriptCount(){return this.missingVtranscriptCount;}
    public void setEmptyVTCount(int emptyVTCount){this.emptyVTCount=emptyVTCount;}
    public int getEmptyVTCount(){return this.emptyVTCount;}

}
