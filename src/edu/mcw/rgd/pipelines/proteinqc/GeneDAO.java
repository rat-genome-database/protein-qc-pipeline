package edu.mcw.rgd.pipelines.proteinqc;

import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.datamodel.Gene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by jthota on 12/3/2015.
 */
public class GeneDAO {

  public Genes getGeneSymbols(int transcript_rgd_id) throws Exception {
      Connection conn = null;
      ResultSet rs = null;
      PreparedStatement pstmt = null;
      Genes genes=new Genes();

      try {

          conn = DataSourceFactory.getInstance().getDataSource().getConnection();
          String sql = "select * from Transcripts t, genes g where t.gene_rgd_id= g.rgd_id and transcript_rgd_id = ?";
          pstmt = conn.prepareStatement(sql);
          pstmt.setInt(1, transcript_rgd_id);
          rs = pstmt.executeQuery();
          while(rs.next()){
              genes.setGeneSymbol(rs.getString("gene_symbol"));
              genes.setProteinAccId(rs.getString("protein_acc_id"));

          }
      } catch (Exception e) {

      }finally{

          rs.close();
          pstmt.close();
          conn.close();

      }
      return genes;

    }









}