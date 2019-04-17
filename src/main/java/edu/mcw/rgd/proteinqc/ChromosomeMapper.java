package edu.mcw.rgd.proteinqc;

import edu.mcw.rgd.datamodel.Chromosome;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 11/30/2015.
 */
public class ChromosomeMapper  extends MappingSqlQuery {

    public ChromosomeMapper(DataSource ds, String query) {
        super(ds, query);
    }

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Chromosome obj = new Chromosome();
        obj.setMapKey(rs.getInt("map_key"));
        obj.setChromosome(rs.getString("chromosome"));
        obj.setRefseqId(rs.getString("refseq_id"));
        obj.setGenbankId(rs.getString("genbank_id"));
        obj.setSeqLength(rs.getInt("seq_length"));
        obj.setGapLength(rs.getInt("gap_length"));
        obj.setGapCount(rs.getInt("gap_count"));
        obj.setContigCount(rs.getInt("contig_count"));
        return obj;
    }
}
