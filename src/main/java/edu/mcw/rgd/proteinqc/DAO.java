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

            String sql = "select * from Transcripts t, genes g where t.gene_rgd_id= g.rgd_id and transcript_rgd_id = ?";
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
        String sql = "SELECT vt.* FROM variant_transcript vt, variant v WHERE v.variant_id = vt.variant_id and sample_id in (select sample_id from sample where map_key=?) and chromosome = ? and full_ref_aa is not null";
        JdbcTemplate jt = new JdbcTemplate(DataSourceFactory.getInstance().getCarpeNovoDataSource());
        List rows =  jt.query(sql, new Object[]{mapkey,chr}, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                VariantTranscript vt = new VariantTranscript();
                vt.setTranscript_rgd_id(rs.getInt("transcript_rgd_id"));
                vt.setAaSequence(rs.getString("full_ref_aa"));
                vt.setVariant_id(rs.getInt("variant_id"));
                vt.setVariant_transcript_id((rs.getInt("variant_transcript_id")));
                //vt.setNucSequence(rs.getString("full_ref_nuc"));
                return vt;
            }
        });
        return rows;
    }

    public int updateVariantTranscript(int variant_transcript_id) throws Exception{
        String sql="update variant_transcript set full_ref_aa=null, full_ref_nuc=null, location_name='Unknown', syn_status='Unknown', ref_aa=null, var_aa=null, full_ref_aa_pos=null,full_ref_nuc_pos=null, near_splice_site=null,frameshift=null where variant_transcript_id=?";
        SqlUpdate su = new SqlUpdate(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        su.declareParameter(new SqlParameter(4));
        su.compile();
        return su.update(new Object[]{variant_transcript_id});
    }

    public List<Sequence> getNcbiProteinSequences(int transcriptRgdId) throws Exception {
        return seqDAO.getObjectSequences(transcriptRgdId, "ncbi_protein");
    }
}

