package edu.mcw.rgd.proteinqc;

import java.util.Set;

/**
 * Created by jthota on 1/13/2016.
 */
public class TranscriptData {
    private Set<String> multiStopCodonVariantTranscriptIds; // 'variantRgdId,transcriptRgdId'
    private String chromosome;
    private int missingRefSeqCount;
    private Set<Integer> missingRefSeqIds;
    private int noProteinSeqCount;
    private int missingVtranscriptCount;
    private int emptyVTCount;

    public void setMultiStopCodonVariantTranscriptIds(Set<String> multiStopCodonVariantTranscriptIds){this.multiStopCodonVariantTranscriptIds=multiStopCodonVariantTranscriptIds;}
    public Set<String> getMultiStopCodonVariantTranscriptIds(){return this.multiStopCodonVariantTranscriptIds;}

    public void setChromosome(String chromosome){this.chromosome=chromosome;}
    public String getChromosome(){return this.chromosome;}
    public void setMissingRefSeqCount(int missingRefSeqCount){this.missingRefSeqCount = missingRefSeqCount;}
    public int getMissingRefSeqCount(){return this.missingRefSeqCount;}
    public void setMissingRefSeqIds(Set<Integer> missingRefSeqIds){this.missingRefSeqIds = missingRefSeqIds;}
    public Set<Integer> getMissingRefSeqIds(){ return this.missingRefSeqIds;}
    public void setNoProteinSeqCount(int noProteinSeqCount){this.noProteinSeqCount=noProteinSeqCount;}
    public int getNoProteinSeqCount(){return this.noProteinSeqCount;}
    public void setMissingVtranscriptCount(int missingVtranscriptCount){this.missingVtranscriptCount= missingVtranscriptCount;}
    public int getMissingVtranscriptCount(){return this.missingVtranscriptCount;}
    public void setEmptyVTCount(int emptyVTCount){this.emptyVTCount=emptyVTCount;}
    public int getEmptyVTCount(){return this.emptyVTCount;}

}
