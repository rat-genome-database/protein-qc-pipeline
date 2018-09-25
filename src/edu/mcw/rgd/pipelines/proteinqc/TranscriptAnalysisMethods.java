package edu.mcw.rgd.pipelines.proteinqc;

import edu.mcw.rgd.dao.impl.SequenceDAO;
import edu.mcw.rgd.datamodel.Chromosome;
import edu.mcw.rgd.datamodel.Sequence;

import java.util.*;

/**
 * Created by jthota on 1/13/2016.
 */
public class TranscriptAnalysisMethods {
    public TranscriptAnalysisMethods() {
    }

    public List<TranscriptData> getMultiStopCodonTranscripts(Integer mapkey) throws Exception {
        SequenceDAO sdao = new SequenceDAO();
        VariantTranscriptDAO variantTranscriptDAO = new VariantTranscriptDAO();
        ChromosomeDAO chrDAO = new ChromosomeDAO();
        List<TranscriptData> transcriptDatas = new ArrayList<>();
        List<VariantTranscript> vtList;
        List<Chromosome> chrList = chrDAO.getChromosomes(mapkey);
        for (Chromosome chromosome : chrList) {
            int equal = 0;
            int notEqual = 0;
            int emptySeq = 0;
            int transcripts = 0;
            int noProteinSeq = 0;
            int emptyVT = 0;
            int i = 0;
            int multipleStopCodonSeq = 0;
            List<Integer> transcriptRgdIdList = new ArrayList<>();
            List<Integer> variantTranscriptIdList = new ArrayList<>();
            List<Integer> variantIdList = new ArrayList<>();
            List<Integer> missingRefSeqIds = new ArrayList<>();
            String chr = chromosome.getChromosome();
            vtList = variantTranscriptDAO.getVariantTranscripts(chr, mapkey);

                for (VariantTranscript vt : vtList)
                {  if (vt != null) {
                        String aminoAcidSeq = vt.getAaSequence();
                        String aaSeq;
                        String lastCharacter = aminoAcidSeq.substring(aminoAcidSeq.length() - 1);
                        if (lastCharacter.equals("*")) {
                            aaSeq = aminoAcidSeq.substring(0, aminoAcidSeq.length() - 1);
                        } else aaSeq = aminoAcidSeq;
                        int transcript_rgd_id = vt.getTranscipt_rgd_id();
                        int variant_id = vt.getVariant_id();
                        int variant_transcript_id = vt.getVariant_transcript_id();
                        List<Sequence> sequences = sdao.getObjectSequences(transcript_rgd_id, 12);
                        if (!sequences.isEmpty()) {
                            for (Sequence sequence : sequences) {
                                if (sequence.getSeqTypeKey() == 12) {
                                    String RefSeq = sequence.getCloneSeq();
                                    if (RefSeq.equals(aaSeq)) {
                                        equal = equal + 1;
                                    } else {
                                        notEqual = notEqual + 1;
                                        int stopCodon = 0;
                                        for (int j = 0; j < aaSeq.length() && j < RefSeq.length(); j++) {
                                            if (aaSeq.charAt(j) != RefSeq.charAt(j) && aaSeq.charAt(j) == '*')
                                                stopCodon = stopCodon + 1;
                                        }
                                        if (stopCodon > 1) {
                                            multipleStopCodonSeq = multipleStopCodonSeq + 1;
                                            transcriptRgdIdList.add(transcript_rgd_id);
                                            variantIdList.add(variant_id);
                                            variantTranscriptIdList.add(variant_transcript_id);
                                        }
                                    }
                                } else {
                                    if (sequences.size() <= 1 && sequence.getSeqTypeKey() != 12)
                                        noProteinSeq = noProteinSeq + 1;
                                }
                            }
                        } else {
                            emptySeq = emptySeq + 1;
                            missingRefSeqIds.add(transcript_rgd_id);
                        }
                        i = i + 1;

                     } else {
                     transcripts = transcripts + 1; }
                     }
                Set<Integer> transcriptRgdIdSet = new HashSet<>(transcriptRgdIdList);
                Set<Integer> variantIdSet = new HashSet<>(variantIdList);
                Set<Integer> missingRefSeqIdsSet = new HashSet<>(missingRefSeqIds);

                TranscriptData transcriptData = new TranscriptData();
                transcriptData.setMultiStopCodonTranscriptRgdIds(transcriptRgdIdSet);
                transcriptData.setMultiStopCodonVariantIds(variantIdSet);
                transcriptData.setMultiStopCoodnVariantTranscriptIds(variantTranscriptIdList);
                transcriptData.setChromosome(chr);
                transcriptData.setMissingRefSeqCount(emptySeq);
                transcriptData.setMissingRefSeqIds(missingRefSeqIdsSet);
                transcriptData.setNoProteinSeqCount(noProteinSeq);

                transcriptData.setMissingVtranscriptCount(transcripts);
                transcriptData.setEmptyVTCount(emptyVT);
                transcriptDatas.add(transcriptData);
            }

            return transcriptDatas;
        }

}