package edu.mcw.rgd.proteinqc;


import edu.mcw.rgd.dao.impl.*;
import edu.mcw.rgd.datamodel.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;
import java.util.*;

/**
 * Created by jthota on 10/29/2015.
 */
public class Manager {

    private String version;
    Log log = LogFactory.getLog("main");

    public static void main(String[] args) throws Exception {
        String mode = null;
        if( args.length==0 ) {
            printUsageAndExit();

        }
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        new XmlBeanDefinitionReader(bf).loadBeanDefinitions(new FileSystemResource("properties/AppConfigure.xml"));
        Manager manager = (Manager) (bf.getBean("manager"));
        System.out.println(manager.getVersion());
        // parse cmdline
        for( String arg: args ) {
            if(!arg.isEmpty()){
            switch(arg.toLowerCase()) {
                case "-dryrun":
                    mode = "dryRun";
                    break;
                case "-normalrun":
                    mode = "normalRun";
                    break;
                default:
                    mode="none";
                    break;
            }}
        }
        try { manager.run(mode); } catch(Exception e) {
            e.printStackTrace();
            throw e; }
    }
   static void printUsageAndExit() {
        System.out.println("ProteinQcPipeline ver 1.0, Jan 13, 2016");
        System.out.println("  Copyright Jyothi Xxx");
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
        VariantTranscriptDAO variantTranscriptDAO = new VariantTranscriptDAO();
        GeneDAO geneDAO = new GeneDAO();
        MapDAO mapDAO = new MapDAO();
        Genes genes = new Genes();
        String MODE = mode.toLowerCase();
        List<Integer> mapkeys = new ArrayList<>(Arrays.asList(60, 70, 360));
        if(MODE.equals("dryrun")){
             System.out.println("MODE: DRY RUN");
             log.info("MODE: DRY RUN \n=========================================================================================");
             System.out.println("**PROTEIN SEQUENCE ANALYSIS TO GENERATE MULTI STOP CODON SEQUENCE DATA FOR**" +" \n\t\t" + " QUALITY CONTROL ON ALL EXISTING RGD ASSEMBLIES ****");
             System.out.println("=========================================================================================");
             log.info("**PROTEIN SEQUENCE ANALYSIS TO GENERATE MULTI STOP CODON SEQUENCE DATA FOR**" +" \n\t\t" + " QUALITY CONTROL ON ALL EXISTING RGD ASSEMBLIES ****");
             log.info("==================================================================================================================================");
             for(int mapkey : mapkeys) {
                 String mapKeyName = mapDAO.getMap(mapkey).getName();
                 System.out.println("GENERATING MULTI STOP CODON REPORT FOR  " + mapKeyName + "  CHROMOSOME WISE...");
                 System.out.println("=========================================================================================");
                 log.info("GENERATING MULTI STOP CODON REPORT FOR  " + mapKeyName + "  CHROMOSOME WISE...");
                 log.info("====================================================================================================");

                 TranscriptAnalysisMethods transcriptAnalysisMethods = new TranscriptAnalysisMethods();
                 /*GET MULTI STOP CODON TRANSCRIPTS FOR ONE WHOLE ASSEMBLY AT A TIME*/
                 List<TranscriptData> transcriptDataList= transcriptAnalysisMethods.getMultiStopCodonTranscripts(mapkey);
                 for(TranscriptData trData: transcriptDataList){
                     Set<Integer> multistopcodonTranscritIds = new HashSet<>(trData.getMultiStopCodonTranscriptRgdIds());
                     List<Integer> multistopcodonVariantTranscriptIds = trData.getMultiStopCoodnVariantTranscriptIds();
                     int transcriptsSize = multistopcodonTranscritIds.size();
                     int variantsSize= multistopcodonVariantTranscriptIds.size();
                     int missingRefSeqCount = trData.getMissingRefSeqCount();
                     Set<Integer> missingRefSeqIds = trData.getMissingRefSeqIds();
                     String chr = trData.getChromosome();
                     System.out.println("CHROMOSOME: " + chr);
                     System.out.println("====================================================================");
                     log.info("CHROMOSOME: " + chr);
                     log.info("====================================================================");
                     System.out.println("Multi Stop Codon GENES COUNT : "+ transcriptsSize);
                     log.info("Multi Stop Codon GENES COUNT : "+ transcriptsSize);
                     System.out.println("Multi Stop Codon VARIANT TRANSCRIPTS COUNT  : " + variantsSize);
                     log.info("Multi Stop Codon VARIANT TRANSCRIPTS COUNT  : " + variantsSize);
                     System.out.println("Missing RefSeq Count: " + missingRefSeqCount + "\nPlease Update the Database for Missing RefSeq and Run this CODE again.\nSee the MAIN LOG FOR MORE INFO. ");
                     log.info("MISSING REFSEQ COUNT: " + missingRefSeqCount);
                     log.info("**********MISSING REFSEQ TRANSCRIPT RGD IDS and SYMBOLS***********");
                     for(int id: missingRefSeqIds){
                         genes = geneDAO.getGeneSymbols(id);
                         String geneSymbol= genes.getGeneSymbol();
                         log.info("                " + id + "                 " + geneSymbol);
                     }

                    if(multistopcodonTranscritIds.size()==0){
                        continue;
                    }else{
                        log.info("******MULTI STOP CODON TranscriptRGDIds and GENE Symbols on Chromosome: " + chr +"**********");
                        log.info("Transcript RGDID **** GENE SYMBOL ***** PROTEIN ACC ID");
                        for(int trId:multistopcodonTranscritIds){
                        genes = geneDAO.getGeneSymbols(trId);
                            String geneSymbol = genes.getGeneSymbol();
                            String proteinAccId=genes.getProteinAccId();
                        log.info("        " + trId + "        " + geneSymbol + "       " + proteinAccId);
                    }
                }}
          }
                  log.info("REPORT GENERATION COMPLETED.\n dryRun completed Successfully");
                  System.out.println("REPORT GENERATION COMPLETED.\n dryRun completed Successfully");

        }else{if(MODE.equals("normalrun")){
            System.out.println("MODE: NORMAL RUN\n====================================================================");
            log.info("MODE: NORMAL RUN\n====================================================================");
            System.out.println("**PROTEIN SEQUENCE ANALYSIS TO GENERATE MULTI STOP CODON SEQUENCE DATA FOR**" +" \n\t\t" +" QUALITY CONTROL ON ALL EXISTING RGD ASSEMBLIES ****");
            System.out.println("=================================================================================================================================");
            log.info("**PROTEIN SEQUENCE ANALYSIS TO GENERATE MULTI STOP CODON SEQUENCE DATA FOR**" + " \n\t\t" + " QUALITY CONTROL ON ALL EXISTING RGD ASSEMBLIES ****");
            log.info("=================================================================================================================================");
            for(int mapkey : mapkeys) {
                String mapKeyName=mapDAO.getMap(mapkey).getName();
            System.out.println("GENERATING MULTI STOP CODON REPORT FOR  " + mapKeyName + "  CHROMOSOME WISE...");
            System.out.println("=========================================================================================");
                log.info("GENERATING MULTI STOP CODON REPORT FOR  " + mapKeyName + "  CHROMOSOME WISE...");
                log.info("=========================================================================================");
            TranscriptAnalysisMethods transcriptAnalysisMethods = new TranscriptAnalysisMethods();
            List<TranscriptData> transcriptDataListAllChromosomes= transcriptAnalysisMethods.getMultiStopCodonTranscripts(mapkey);
                 for(TranscriptData trDataEachChromosome: transcriptDataListAllChromosomes){
                     String chr = trDataEachChromosome.getChromosome();
                     log.info("CHROMOSOME: "+ chr +"\n====================================================================");
                     List<Integer> multiStopCodonVariantTranscriptIds = trDataEachChromosome.getMultiStopCoodnVariantTranscriptIds();
                     Set<Integer> missingRefSeqIds = trDataEachChromosome.getMissingRefSeqIds();
                     int multistopcodonVariantCount = multiStopCodonVariantTranscriptIds.size();
                     int multistopcodonGeneCount = trDataEachChromosome.getMultiStopCodonTranscriptRgdIds().size();
                     int missingRefSeqCount= trDataEachChromosome.getMissingRefSeqCount();
                     System.out.println("TOTAL NUMBER OF MULTI STOP CODON SEQUENCES OF CHROMOSOME " + chr + " : "+ multistopcodonVariantCount);
                     log.info("Multi Stop Codon GENE COUNT" + multistopcodonGeneCount);
                     log.info("Multi Stop Codon VARIANT SEQ COUNT" + multistopcodonVariantCount);
                     log.info("TOTAL NUMBER OF MULTI STOP CODON SEQUENCES OF CHROMOSOME"+ chr + ": " + multistopcodonVariantCount);
                     System.out.println("Missing REFSEQ COUNT: " + missingRefSeqCount + "\nPlease Update the Database for Missing RefSeq and Run this CODE again.\nSee the MAIN LOG FOR MORE INFO. ");
                     log.info("Missing REFSEQ COUNT: " + missingRefSeqCount);
                     log.info("**********MISSING REFSEQ TRANSCRIPT RGD IDS and SYMBOLS***********");
                     for(int id: missingRefSeqIds){
                         genes = geneDAO.getGeneSymbols(id);
                         String geneSymbol=genes.getGeneSymbol();
                         log.info("                " + id + "                 " + geneSymbol);
                     }
                     if(multistopcodonVariantCount==0)
                     { continue;}
                     else{
                         log.info("******MULTI STOP CODON TranscriptRGDIds and GENE Symbols on Chromosome: " + chr +"**********");
                         log.info("Transcript RGDID **** GENE SYMBOL ***** PROTEIN ACC ID");
                        Set<Integer> multistopcodonTranscritIds =trDataEachChromosome.getMultiStopCodonTranscriptRgdIds();
                        for(int trId:multistopcodonTranscritIds){
                            genes = geneDAO.getGeneSymbols(trId);
                            String geneSymbol=genes.getGeneSymbol();
                            String proteinAccId= genes.getProteinAccId();
                            log.info("        " + trId + "        " + geneSymbol + "       " + proteinAccId);
                        }
                    System.out.println("UPDATING THE VARIANT TRANSCRIPT DATA of CHROMOSOME: " +chr + ".....");
                         for(int variantTranscriptId: multiStopCodonVariantTranscriptIds){
                            variantTranscriptDAO.updateVariantTranscript(variantTranscriptId);
                        }
                    System.out.println("UPDATE COMPLETED FOR CHROMOSOME: " + chr + "See the MAIN LOG FOR UPDATED GENEs LIST");
                         log.info("UPDATE COMPLETED FOR CHROMOSOME: " + chr);
                    }
            }
         }
                 log.info("DATABASE UPDATE COMPLETED ON ALL ASSEBLIES FOR REPORTED LIST OF GENES OF MULTISTOP CODON TRANSCRIPTS");
            System.out.println("DATABASE UPDATE COMPLETED ON ALL ASSEBLIES FOR REPORTED LIST OF GENES OF MULTISTOP CODON TRANSCRIPTS");
        } else {
            System.out.println("Please enter the MODE OPTIONS as below \n\t\t -dryrun \n\t\t -normalrun  \n as arguments");
        }
         }
    }

    public void setVersion(String version) { this.version = version;    }
    public String getVersion() { return version; }
}
