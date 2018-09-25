package edu.mcw.rgd.pipelines.proteinqc;


import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
 import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

/**
 * Created by jthota on 11/4/2015.
 */


public class VariantTranscriptDAO extends AbstractDAO {
    public VariantTranscriptDAO() {    }
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

}
