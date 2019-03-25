package com.tunestore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tunestore.beans.CD;

public class DBUtil {
  private static final Log log = LogFactory.getLog(DBUtil.class);

  public static String DB_URL;
  
  static {
    if (System.getProperty("tunestore.db.location") != null) {
      DB_URL = "jdbc:derby://localhost:1527/" + System.getProperty("tunestore.db.location");
    } else {
      DB_URL = "jdbc:derby://localhost:1527/" + System.getProperty("user.home") + "/.tunestore";
    }
    
    System.setProperty("jdbc.tunestore.url", DB_URL);
  }
  
  public static Connection getConnection() throws Exception {
    log.info("Opening database at " + DB_URL);
    Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
    Connection conn = DriverManager.getConnection(DB_URL);
    return conn;
  }

  public static double getBalance(String userid) {
    Connection conn = null;
    double retval = 0;

    try {
      conn = getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT BALANCE " + "FROM TUNEUSER "
          + "WHERE USERNAME = '" + userid + "'");
      rs.next();
      retval = rs.getDouble(1);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (Exception e) {
        }
      }
    }

    return retval;
  }

  public static List getCDsForUser(String myLinks, String user) throws Exception {
    Connection conn = null;
    List results = new ArrayList();

    try {
      conn = DBUtil.getConnection();
      Statement stmt = conn.createStatement();
      String sql = null;
      if (user == null) {
        // Show all the CD's
        if (myLinks != null) {
          sql = "SELECT ID, ARTIST, ALBUM, IMAGE, PRICE, BITS, TUNEUSER_CD.TUNEUSER "
              + "FROM CD " + "LEFT JOIN TUNEUSER_CD "
              + "ON TUNEUSER_CD.CD = CD.ID " + "AND TUNEUSER_CD.TUNEUSER = '"
              + myLinks + "'";
        } else {
          sql = "SELECT ID, ARTIST, ALBUM, IMAGE, PRICE, BITS, '' TUNEUSER "
              + "FROM CD";
        }
      } else {
        // Show CD's for a particular user
        if (myLinks != null) {
          sql = "SELECT ID, "
            + "ARTIST, "
            + "ALBUM, "
            + "IMAGE, "
            + "PRICE, "
            + "BITS, "
            + "MYLINK.TUNEUSER "
            + "FROM CD "
            + "INNER JOIN TUNEUSER_CD ON "
            + "TUNEUSER_CD.CD = CD.ID "
            + "LEFT JOIN TUNEUSER_CD MYLINK "
            + "ON MYLINK.CD = CD.ID "
            + "AND MYLINK.TUNEUSER = '"
            + myLinks
            + "' "
            + "WHERE TUNEUSER_CD.TUNEUSER = '"
            + user
            + "'";
        } else {
          sql = "SELECT ID, ARTIST, ALBUM, IMAGE, PRICE, BITS, '' TUNEUSER "
              + "FROM CD INNER JOIN TUNEUSER_CD ON TUNEUSER_CD.CD = CD.ID "
              + "AND TUNEUSER_CD.TUNEUSER = '" + user + "'";
        }
      }

      log.info(sql);
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        CD cd = new CD();
        cd.setId(new Long(rs.getLong(1)));
        cd.setArtist(rs.getString(2));
        cd.setAlbum(rs.getString(3));
        cd.setPrice(new Double(rs.getDouble(5)));
        cd.setImage(rs.getString(4));
        cd.setOwned(null != rs.getString(7)
            && !"".equals(rs.getString(7)));
        cd.setBits(rs.getString(6));
        results.add(cd);
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      throw(e);
    } finally {
      if (conn != null) {
        try {
          conn.close();
          log.info("Closed Connection");
        } catch (Exception e) {
        }
      }
    }

    return results;
  }
}
