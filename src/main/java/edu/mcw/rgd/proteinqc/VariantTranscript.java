package edu.mcw.rgd.proteinqc;

/**
 * Created by jthota on 12/4/2015.
 */
public class VariantTranscript {
    private int transcriptRgdId;
    private int variantRgdId;
    private String aaSequence;

    public int getTranscriptRgdId() {
        return transcriptRgdId;
    }

    public void setTranscriptRgdId(int transcriptRgdId) {
        this.transcriptRgdId = transcriptRgdId;
    }

    public int getVariantRgdId() {
        return variantRgdId;
    }

    public void setVariantRgdId(int variantRgdId) {
        this.variantRgdId = variantRgdId;
    }

    public void setAaSequence(String aaSequence){ this.aaSequence = aaSequence;}
    public String getAaSequence(){return this.aaSequence;}
}
