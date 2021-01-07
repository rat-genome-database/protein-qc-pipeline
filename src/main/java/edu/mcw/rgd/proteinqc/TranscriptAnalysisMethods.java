package edu.mcw.rgd.proteinqc;

import edu.mcw.rgd.datamodel.Chromosome;
import edu.mcw.rgd.datamodel.Sequence;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by jthota on 1/13/2016.
 */
public class TranscriptAnalysisMethods {

    Logger log = Logger.getLogger("main");

    public List<TranscriptData> getMultiStopCodonTranscripts(int mapkey, DAO dao) throws Exception {
        List<TranscriptData> transcriptDatas = new ArrayList<>();
        List<VariantTranscript> vtList;
        List<Chromosome> chrList = dao.getChromosomes(mapkey);
        for (Chromosome chromosome : chrList) {
            log.debug("chr "+chromosome.getChromosome());
            int equal = 0;
            int notEqual = 0;
            int emptySeq = 0;
            int noProteinSeq = 0;
            int emptyVT = 0;
            int i = 0;
            int multipleStopCodonSeq = 0;
            Set<String> variantTranscriptIdSet = new HashSet<>(); // 'variantRgdId,transcriptRgdId'
            List<Integer> missingRefSeqIds = new ArrayList<>();
            String chr = chromosome.getChromosome();
            vtList = dao.getVariantTranscripts(chr, mapkey);
            log.debug("  vtList size "+vtList.size());

            for (VariantTranscript vt : vtList) {
                String aminoAcidSeq = vt.getAaSequence();
                String aaSeq;
                String lastCharacter = aminoAcidSeq.substring(aminoAcidSeq.length() - 1);
                if (lastCharacter.equals("*")) {
                    aaSeq = aminoAcidSeq.substring(0, aminoAcidSeq.length() - 1);
                } else aaSeq = aminoAcidSeq;
                int transcript_rgd_id = vt.getTranscriptRgdId();
                int variant_rgd_id = vt.getVariantRgdId();
                List<Sequence> sequences = dao.getNcbiProteinSequences(transcript_rgd_id);
                if (!sequences.isEmpty()) {
                    for (Sequence sequence : sequences) {
                        String RefSeq = sequence.getSeqData();
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
                                variantTranscriptIdSet.add(variant_rgd_id+","+transcript_rgd_id);
                            }
                        }
                    }
                } else {
                    emptySeq = emptySeq + 1;
                    missingRefSeqIds.add(transcript_rgd_id);
                }
                i = i + 1;
            }
            Set<Integer> missingRefSeqIdsSet = new HashSet<>(missingRefSeqIds);

            TranscriptData transcriptData = new TranscriptData();
            transcriptData.setMultiStopCodonVariantTranscriptIds(variantTranscriptIdSet);
            transcriptData.setChromosome(chr);
            transcriptData.setMissingRefSeqCount(emptySeq);
            transcriptData.setMissingRefSeqIds(missingRefSeqIdsSet);
            transcriptData.setNoProteinSeqCount(noProteinSeq);

            transcriptData.setVtEntriesAnalyzed(vtList.size());
            transcriptData.setEmptyVTCount(emptyVT);
            transcriptDatas.add(transcriptData);
        }

        return transcriptDatas;
    }
}
