package edu.mcw.rgd.proteinqc;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.datamodel.Chromosome;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jthota on 11/30/2015.
 */

public class ChromosomeDAO extends AbstractDAO {
    public List<Chromosome> getChromosomes(int map_key) throws Exception
    {

        String sql = "select * from chromosomes where map_key = ?";
        ChromosomeMapper q = new ChromosomeMapper(getDataSource(), sql);
        List<Chromosome> chrList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn= DataSourceFactory.getInstance().getDataSource().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,map_key);
            rs = pstmt.executeQuery();
            while(rs.next()){
                Chromosome chr= (Chromosome) q.mapRow(rs,1);
                chrList.add(chr);
            }



        }catch (Exception e){} finally{
            if (pstmt != null) {
                pstmt.close();
            }
            if (rs != null) {
                rs.close();
            }

            if (conn != null) {
                conn.close();
            }
        }

        return chrList;
    }



}

