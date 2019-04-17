package edu.mcw.rgd.proteinqc;

/**
 * Created by jthota on 12/4/2015.
 */
public class VariantTranscript {
    private int transcript_rgd_id;
    private int variant_id;
    private int variant_transcript_id;
    private String aaSequence;
    private String nucSequence;

    public void setTranscript_rgd_id(int transcript_rgd_id){ this.transcript_rgd_id = transcript_rgd_id;}
    public int getTranscipt_rgd_id(){        return this.transcript_rgd_id;    }
    public void setVariant_id(int variant_id){ this.variant_id = variant_id;}
    public int getVariant_id(){        return this.variant_id;    }
    public void setVariant_transcript_id(int variant_transcript_id){this.variant_transcript_id=variant_transcript_id;}
    public int getVariant_transcript_id(){return this.variant_transcript_id;}
    public void setAaSequence(String aaSequence){ this.aaSequence = aaSequence;}
    public String getAaSequence(){return this.aaSequence;}
    public void setNucSequence(String nucSequence){this.nucSequence = nucSequence;}
    public String getNucSequence(){return this.nucSequence;}
}
