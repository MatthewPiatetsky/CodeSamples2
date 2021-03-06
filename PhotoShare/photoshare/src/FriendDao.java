package photoshare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A data access object (DAO) to handle the Users table
 *
 */
public class FriendDao {
  private static final String CHECK_EMAIL_STMT = "SELECT " +
      "COUNT(*) FROM Users WHERE email = ?";

  private static final String NEW_FRIEND_STMT = "INSERT INTO " +
      "friends_with (email1, email2) VALUES (?,?)";
    
    private static final String ALL_FRIENDS_STMT = "SELECT email2 FROM friends_with WHERE email1= ?"; 

 private static final String MOST_POPULAR = "SELECT email1,COUNT(email1) as cnt FROM friends_with GROUP BY email1 ORDER BY cnt DESC";

    private static String VALID_USER = "SELECT COUNT(*) FROM Users WHERE email=?";
    public List<String>  mostPopular() {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;

        List<String> mostPopular = new ArrayList<String>();
        try {
            conn = DbConnection.getConnection();
            stmt = conn.prepareStatement(MOST_POPULAR);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String email=rs.getString(1);
                int count = rs.getInt(2);
                mostPopular.add(email + " " + count);
            }

            rs.close();
            rs = null;

            stmt.close();
            stmt = null;

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
		if (rs != null) {
		    try { rs.close(); } catch (SQLException e) { ; }
		    rs = null;
		}
		if (stmt != null) {
		    try { stmt.close(); } catch (SQLException e) {;}
		    stmt = null;
		}
		if (conn != null) {
		    try { conn.close(); } catch (SQLException e) {;}
		    conn = null;
		}
	    }

	    return mostPopular;
    }

    
    public String allFriends(String email1) throws SQLException{
	PreparedStatement stmt = null;
	Connection conn = null;
	ResultSet rs = null;
	try {
	    conn = DbConnection.getConnection();
	    stmt = conn.prepareStatement(ALL_FRIENDS_STMT);
	    stmt.setString(1,email1);
	    rs = stmt.executeQuery();
	    String total = "";
	    while(rs.next()){
		total+=rs.getString(1) + ", ";
	    }
	    return total.substring(0,total.length()-2);
	    } catch (SQLException e) {
		e.printStackTrace();
		throw new RuntimeException(e);
	    }
    }

public boolean create(String email1, String email2) throws SQLException {
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    try {
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(VALID_USER);
      stmt.setString(1,email2);
      rs=stmt.executeQuery();
      rs.next();
      if(rs.getInt(1)==0){
	  return false;
      }
      stmt.close();
      stmt = conn.prepareStatement(NEW_FRIEND_STMT);
      stmt.setString(1,email1);      
      stmt.setString(2,email2);
      stmt.executeUpdate();

      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      if (rs != null) {
        try { rs.close(); }
        catch (SQLException e) { ; }
        rs = null;
      }
      
      if (stmt != null) {
        try { stmt.close(); }
        catch (SQLException e) { ; }
        stmt = null;
      }
      
      if (conn != null) {
        try { conn.close(); }
        catch (SQLException e) { ; }
        conn = null;
      }
    }
  }
}
