package edu.mcw.rgd.proteinqc;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;
import java.util.*;

/**
 * Created by jthota on 10/29/2015.
 */
public class Manager {

    private String version;
    Logger log = Logger.getLogger("main");

    public static void main(String[] args) throws Exception {
        String mode = null;
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        new XmlBeanDefinitionReader(bf).loadBeanDefinitions(new FileSystemResource("properties/AppConfigure.xml"));
        Manager manager = (Manager) (bf.getBean("manager"));
        System.out.println(manager.getVersion());

        // parse cmdline
        for( String arg: args ) {
            switch(arg.toLowerCase()) {
                case "-dryrun":
                    mode = "DRY RUN";
                    break;
                case "-normalrun":
                    mode = "NORMAL RUN";
                    break;
                default:
                    break;
            }
        }
        if( mode==null ) {
            manager.printUsageAndExit();
        }

        try { manager.run(mode); } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    void printUsageAndExit() {
        System.out.println("Usage:");
        System.out.println("  java -jar ProteinQcPipeline.jar -dryRun|-normalRun");
        System.out.println("    -dryRun    simulate the run, no changes are made to database (default mode)");
        System.out.println("    -normalRun normal run");
        System.exit(-1);
    }

    /**
     *
     * @param mode if true no changes will be made in the database, but you will still get a report
     *               with the changes that will be made in normal mode
     * @throws Exception *** EXTREMELY USEFUL TO FIND OUT WHAT WRONG HAPPENED TO YOUR PROGRAM *** DO NOT IGNORE THEM ***
     */
    public void run(String mode) throws Exception{

        if( mode==null ) {
            return;
        }
        boolean isNormalMode = mode.equals("NORMAL RUN");

        log.info("MODE: "+mode);
        log.info("=========================================================================================");
        log.info("**PROTEIN SEQUENCE ANALYSIS TO GENERATE MULTI STOP CODON SEQUENCE DATA FOR**");
        log.info("\t\t QUALITY CONTROL ON ALL EXISTING RGD ASSEMBLIES ****");
        log.info("==================================================================================================================================");

        DAO dao = new DAO();
        List<Integer> mapkeys = new ArrayList<>(Arrays.asList(60, 70, 360));
        for(int mapkey : mapkeys) {
            String mapKeyName = dao.getMapName(mapkey);
            log.info("GENERATING MULTI STOP CODON REPORT FOR  " + mapKeyName + "  CHROMOSOME WISE...");
            log.info("====================================================================================================");
            TranscriptAnalysisMethods transcriptAnalysisMethods = new TranscriptAnalysisMethods();
            // GET MULTI STOP CODON TRANSCRIPTS FOR ONE WHOLE ASSEMBLY AT A TIME
            List<TranscriptData> transcriptDataList= transcriptAnalysisMethods.getMultiStopCodonTranscripts(mapkey, dao);
            log.info("transcripts data loaded: "+transcriptDataList.size());
            for(TranscriptData trData: transcriptDataList) {
                String chr = trData.getChromosome();
                log.info("CHROMOSOME: " + chr);
                log.info("====================================================================");
                List<Integer> multiStopCodonVariantTranscriptIds = trData.getMultiStopCoodnVariantTranscriptIds();
                Set<Integer> missingRefSeqIds = trData.getMissingRefSeqIds();
                int missingRefSeqCount = trData.getMissingRefSeqCount();
                int multistopcodonVariantCount = multiStopCodonVariantTranscriptIds.size();
                int multistopcodonGeneCount = trData.getMultiStopCodonTranscriptRgdIds().size();
                log.info("TOTAL NUMBER OF MULTI STOP CODON SEQUENCES OF CHROMOSOME " + chr + " : " + multistopcodonVariantCount);
                log.info("Multi Stop Codon GENE COUNT" + multistopcodonGeneCount);
                log.info("Missing RefSeq Count: " + missingRefSeqCount);
                log.info("Please Update the Database for Missing RefSeq and Run this CODE again.");
                log.info("See the MAIN LOG FOR MORE INFO. ");
                log.info("**********MISSING REFSEQ TRANSCRIPT RGD IDS and SYMBOLS***********");
                for (int id : missingRefSeqIds) {
                    Genes genes = dao.getGeneSymbols(id);
                    String geneSymbol = genes.getGeneSymbol();
                    log.info("                " + id + "                 " + geneSymbol);
                }
                if (multistopcodonVariantCount == 0) {
                    continue;
                } else {
                    log.info("******MULTI STOP CODON TranscriptRGDIds and GENE Symbols on Chromosome: " + chr +"**********");
                    log.info("Transcript RGDID **** GENE SYMBOL ***** PROTEIN ACC ID");

                    Set<Integer> multistopcodonTranscritIds =trData.getMultiStopCodonTranscriptRgdIds();
                    for(int trId:multistopcodonTranscritIds){
                        Genes genes = dao.getGeneSymbols(trId);
                        String geneSymbol=genes.getGeneSymbol();
                        String proteinAccId= genes.getProteinAccId();
                        log.info("        " + trId + "        " + geneSymbol + "       " + proteinAccId);
                    }

                    if( isNormalMode ) {
                        log.debug("UPDATING THE VARIANT TRANSCRIPT DATA of CHROMOSOME: " + chr + ".....");
                        for (int variantTranscriptId : multiStopCodonVariantTranscriptIds) {
                            dao.updateVariantTranscript(variantTranscriptId);
                        }
                        log.info("UPDATE COMPLETED FOR CHROMOSOME: " + chr + "See the MAIN LOG FOR UPDATED GENEs LIST");
                    }
                }
            }
        }

        if( isNormalMode ) {
            log.info("DATABASE UPDATE COMPLETED ON ALL ASSEMBLIES FOR REPORTED LIST OF GENES OF MULTISTOP CODON TRANSCRIPTS");
        } else {
            log.info("REPORT GENERATION COMPLETED.");
            log.info(" dryRun completed Successfully");
        }
    }

    public void setVersion(String version) { this.version = version;    }
    public String getVersion() { return version; }
}
