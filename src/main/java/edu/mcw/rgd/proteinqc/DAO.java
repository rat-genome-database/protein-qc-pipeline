package edu.mcw.rgd.proteinqc;

import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.impl.MapDAO;
import edu.mcw.rgd.dao.impl.SequenceDAO;
import edu.mcw.rgd.datamodel.Chromosome;
import edu.mcw.rgd.datamodel.Sequence;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class DAO {

    private MapDAO mapDAO = new MapDAO();
    private SequenceDAO seqDAO = new SequenceDAO();

    public String getMapName(int mapKey) throws Exception {
        return mapDAO.getMap(mapKey).getName();
    }

    public List<Chromosome> getChromosomes(int map_key) throws Exception {
        return mapDAO.getChromosomes(map_key);
    }

    public Genes getGeneSymbols(int transcript_rgd_id) throws Exception {
        Genes genes=new Genes();

        try( Connection conn = mapDAO.getConnection() ){

            String sql = "SELECT gene_symbol,protein_acc_id FROM transcripts t, genes g WHERE t.gene_rgd_id=g.rgd_id AND transcript_rgd_id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, transcript_rgd_id);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                genes.setGeneSymbol(rs.getString("gene_symbol"));
                genes.setProteinAccId(rs.getString("protein_acc_id"));

            }
        }
        return genes;
    }

    public List getVariantTranscripts(String chr, int mapkey) throws Exception{
//        String sql = "SELECT vt.* FROM variant_transcript vt, variant v WHERE v.variant_id = vt.variant_id "+
//                "AND sample_id IN (select sample_id from sample where map_key=?) AND chromosome=? "+
//                "AND full_ref_aa is not null";

        String sql = "SELECT vt.variant_rgd_id,transcript_rgd_id,full_ref_aa_seq_key " +
                "FROM variant_transcript vt, variant_map_data m " +
                "WHERE m.map_key=? and chromosome=? " +
                "  AND m.rgd_id=vt.variant_rgd_id AND m.map_key=vt.map_key " +
                "  AND NVL(full_ref_aa_seq_key,0)<>0";
        JdbcTemplate jt = new JdbcTemplate(DataSourceFactory.getInstance().getCarpeNovoDataSource());
        List rows =  jt.query(sql, new Object[]{mapkey,chr}, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                VariantTranscript vt = new VariantTranscript();
                vt.setTranscriptRgdId(rs.getInt("transcript_rgd_id"));
                vt.setVariantRgdId(rs.getInt("variant_rgd_id"));
                int seqKey = rs.getInt("full_ref_aa_seq_key");
                vt.setAaSequence(getAaSequence(seqKey));
                return vt;
            }
        });
        return rows;
    }

    public int updateVariantTranscript(int variantRgdId, int transcriptRgdId, int mapKey) throws Exception{
//        String sql="UPDATE variant_transcript SET full_ref_aa=null, full_ref_nuc=null, location_name='Unknown', syn_status='Unknown', "+
//               "ref_aa=null, var_aa=null, full_ref_aa_pos=null,full_ref_nuc_pos=null, near_splice_site=null,frameshift=null "+
//                "WHERE variant_transcript_id=?";

        String sql="UPDATE variant_transcript SET full_ref_aa_seq_key=null, full_ref_nuc_seq_key=null, location_name='Unknown', syn_status='Unknown', "+
               "ref_aa=null, var_aa=null, full_ref_aa_pos=null,full_ref_nuc_pos=null, near_splice_site=null,frameshift=null "+
                "WHERE variant_rgd_id=? AND transcript_rgd_id=? AND map_key=?";
        int rowsUpdated = 0;
        try( Connection conn = DataSourceFactory.getInstance().getCarpeNovoDataSource().getConnection() ) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, variantRgdId);
            ps.setInt(2, transcriptRgdId);
            ps.setInt(3, mapKey);
            rowsUpdated = ps.executeUpdate();
        }
        return rowsUpdated;
    }

    public List<Sequence> getNcbiProteinSequences(int transcriptRgdId) throws Exception {
        return seqDAO.getObjectSequences(transcriptRgdId, "ncbi_protein");
    }

    public String getAaSequence(int seqKey) {

        List<Sequence> seqs;
        try {
            seqs = seqDAO.getObjectSequencesBySeqKey(seqKey);

            // filter out sequences of type other than 'full_ref_aa'
            seqs.removeIf(seq -> !seq.getSeqType().startsWith("full_ref_aa"));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        if( seqs.isEmpty() ) {
            return null;
        }
        return seqs.get(0).getSeqData();
    }
}

