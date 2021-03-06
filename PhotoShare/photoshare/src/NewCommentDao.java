package photoshare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Integer;
import java.util.Date;

/**
 * A data access object (DAO) to handle the Comments table
 */

public class NewCommentDao {
    
    private static final String NEW_COMMENT_STMT = "INSERT INTO " + "comments (comment_text,user_id,comment_date,picture_id) VALUES (?,?,?,?)";

 private static final String NEW_LIKE_STMT = "INSERT INTO " + "likes (user_email,picture_id) VALUES (?,?)";

    private static final String HAS_LIKED = "SELECT COUNT(*) FROM likes WHERE user_email=?";
    private static final String ALL_COMMENT_TEXT_STMT = "SELECT \"comment_text\",\"user_id\",\"comment_date\" FROM comments WHERE \"picture_id\"=? ORDER BY \"comment_id\" DESC";

private static final String ALL_LIKES_TEXT_STMT = "SELECT \"user_email\" FROM likes WHERE \"picture_id\"=?";

    private static final String NUM_LIKES_STMT = "SELECT COUNT(user_email) FROM likes WHERE picture_id=?";

private static final String GET_EMAIL_STMT = "SELECT \"email\" FROM users WHERE \"user_id\"=?";

private static final String GET_OWNER_STMT = "SELECT \"email\" FROM pictures WHERE \"picture_id\"=?";


private static final String GET_USER_ID_STMT = "SELECT \"user_id\" FROM users WHERE \"email\"=?";

private static final String GET_USER_NAME =  "SELECT \"first_name\",\"last_name\" FROM users WHERE user_id= ?";

    private static final String MOST_POPULAR = "SELECT picture_id,COUNT(picture_id) as cnt FROM comments GROUP BY picture_id ORDER BY cnt DESC"; 

public String getUserName(int id) throws SQLException {

    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet username = null;
    conn = DbConnection.getConnection();
    stmt = conn.prepareStatement(GET_USER_NAME);
    stmt.setInt(1,id);
    username  = stmt.executeQuery();
    String name = "";
    username.next();
	name += username.getString(1) + "  " + username.getString(2) + "  ";
    
    username.close();
    username=null;

    stmt.close();
    stmt = null;

    conn.close();
    conn = null;

    return name;
    

}

public boolean hasLiked(String email) throws SQLException {

    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet username = null;
    conn = DbConnection.getConnection();
    stmt = conn.prepareStatement(HAS_LIKED);
    stmt.setString(1,email);
    username = stmt.executeQuery();
    int count = 0;
    while(username.next()){
	count = username.getInt(1);
    }
    if(count==1){
	return true;
	    }
    username.close();
    username=null;

    stmt.close();
    stmt = null;

    conn.close();
    conn = null;

    return false;
    

}


    public String getEmail(int id) throws SQLException {

	PreparedStatement stmt = null;
	Connection conn = null;
	ResultSet username = null;
	conn = DbConnection.getConnection();
	stmt = conn.prepareStatement(GET_EMAIL_STMT);
	stmt.setInt(1,id);
	username  = stmt.executeQuery();
	String name = "";
	username.next();
        name += username.getString(1);

	username.close();
	username=null;

	stmt.close();
	stmt = null;

	conn.close();
	conn = null;

	return name;
    }

    public String getOwner(int id) throws SQLException {

        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet username = null;
        conn = DbConnection.getConnection();
        stmt = conn.prepareStatement(GET_OWNER_STMT);
        stmt.setInt(1,id);
        username  = stmt.executeQuery();
        String name = "";
        username.next();
        name += username.getString(1);

        username.close();
        username=null;

        stmt.close();
        stmt = null;

        conn.close();
        conn = null;

        return name;
    }


public int getUserId(String email) throws SQLException {

    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet useridrs = null;
    conn = DbConnection.getConnection();
    stmt = conn.prepareStatement(GET_USER_ID_STMT);
    stmt.setString(1,email);
    useridrs = stmt.executeQuery();
    int id = 16;
    while(useridrs.next()){
	id = useridrs.getInt(1);
    }
    useridrs.close();
    useridrs=null;    

    stmt.close();
    stmt = null;

    conn.close();
    conn = null;
    return id;

}
 

    public boolean create(String comment_text,int user_id, int picture_id) throws SQLException{
	if(comment_text.equals("YOU SHALL NOT PASS")){
	    return true;
	}
    PreparedStatement stmt = null;
    Connection conn = null;
    
    conn = DbConnection.getConnection();
    stmt = conn.prepareStatement(NEW_COMMENT_STMT);
    stmt.setString(1,comment_text);
    stmt.setInt(2,user_id);
    java.util.Date today = new java.util.Date();
    java.sql.Date sqlToday = new java.sql.Date(today.getTime());
    stmt.setDate(3,sqlToday);
    stmt.setInt(4,picture_id);
    stmt.executeUpdate();
    
    
    
    stmt.close();
    stmt = null;

    conn.close();
    conn = null;

    return true;

    }

 public boolean createLike(String user_email, int picture_id) throws SQLException{
     if(user_email.equals("YOU SHALL NOT PASS")){
	 return true;
	     }
    PreparedStatement stmt = null;
    Connection conn = null;
    
    conn = DbConnection.getConnection();
    stmt = conn.prepareStatement(NEW_LIKE_STMT);
    stmt.setString(1,user_email);
    stmt.setInt(2,picture_id);
    stmt.executeUpdate();
    
    
    stmt.close();
    stmt = null;

    conn.close();
    conn = null;

    return true;

    }

public List<String> allLikes(int picture_id) {
                PreparedStatement stmt = null;
                Connection conn = null;
                ResultSet rs = null;

                List<String> likeText = new ArrayList<String>(); 
                try {
                        conn = DbConnection.getConnection();
                        stmt = conn.prepareStatement(ALL_LIKES_TEXT_STMT);
                        stmt.setInt(1,picture_id);
			rs = stmt.executeQuery();
                        while (rs.next()) {
                            String email = rs.getString(1);
			    likeText.add(email);
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

                return likeText;
        }

public int numLikes(int picture_id) {
                PreparedStatement stmt = null;
                Connection conn = null;
                ResultSet rs = null;
		int numLikes = 0;
                try {
                        conn = DbConnection.getConnection();
                        stmt = conn.prepareStatement(NUM_LIKES_STMT);
                        stmt.setInt(1,picture_id);
			rs = stmt.executeQuery();
                        while (rs.next()) {
                            numLikes = rs.getInt(1);
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

                return numLikes;
        }


public List<String> allCommentText(int picture_id) {
                PreparedStatement stmt = null;
                Connection conn = null;
                ResultSet rs = null;

                List<String> commentText = new ArrayList<String>(); 
                try {
                        conn = DbConnection.getConnection();
                        stmt = conn.prepareStatement(ALL_COMMENT_TEXT_STMT);
                        stmt.setInt(1,picture_id);
			rs = stmt.executeQuery();
                        while (rs.next()) {
                            String text = rs.getString(1);
			    String name = getUserName(rs.getInt(2));
			    Date date = rs.getDate(3);
			    String full = date + ":  " + name + "wrote:" + "    " + text;
			    commentText.add(full);
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

                return commentText;
        }



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
                int pictureid=rs.getInt(1);
                int count = rs.getInt(2);
                mostPopular.add(pictureid + " " + count);
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


}

