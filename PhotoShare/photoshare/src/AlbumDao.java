package photoshare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A data access object (DAO) to handle AlbumObjects
 */
public class AlbumDao {

  private static final String NEW_ALBUM_STMT = "INSERT INTO " + "Albums (album_name,user_id,user_email,album_date) VALUES (?,?,?,?)";


 private static final String NEW_ALBUM_PHOTO_STMT = "INSERT INTO " +
     "album_contains (album_id, album_name, picture_id) VALUES (?,?,?)";


  private static final String LOAD_ALBUM_STMT = "SELECT " +
      "\"album_id\", \"user_id\", \"album_name\", \"user_email\" FROM Pictures WHERE \"album_id\" = ?";

  private static final String SAVE_ALBUM_STMT = "INSERT INTO " +
      "Albums (\"album_id\", \"album_name\",\"user_id\",\"user_email\") VALUES (?, ?, ?, ?)";

private static final String GET_USER_ID_STMT = "SELECT \"user_id\" FROM users WHERE \"email\"=?";

private static final String GET_ALBUM_ID_STMT = "SELECT \"album_id\" FROM albums WHERE \"album_name\"=?";

    private static final String DELETE_PHOTOS_STMT = "DELETE FROM pictures WHERE picture_id=(SELECT picture_id FROM album_contains WHERE album_name=?)";
private static final String DELETE_ALBUM_STMT = "DELETE FROM album_contains WHERE album_name=?";

private static final String DELETE_ALBUM_STMT2 = "DELETE FROM Albums WHERE album_name=?";

    private static final String GET_ALBUM_OWNER = "SELECT user_email FROM Albums WHERE album_name=?";
  private static final String ALL_PICTURE_IDS_STMT = "SELECT \"picture_id\" FROM album_contains WHERE \"album_name\"=?  ORDER BY \"picture_id\" DESC";

    public boolean create(String email,String album_name) throws SQLException{
        if(album_name.equals("YOU SHALL NOT PASS")){
            return true;
        }
	PreparedStatement stmt = null;
	Connection conn = null;

	conn = DbConnection.getConnection();
	stmt = conn.prepareStatement(NEW_ALBUM_STMT);
	stmt.setString(1,album_name);
	stmt.setInt(2,getUserId2(email));
	stmt.setString(3,email);
	java.util.Date today = new java.util.Date();
	java.sql.Date sqlToday = new java.sql.Date(today.getTime());
	stmt.setDate(4,sqlToday);
	stmt.executeUpdate();



	stmt.close();
	stmt = null;

	conn.close();
	conn = null;

	return true;

    }
    public String getAlbumOwner(String album_name) throws SQLException{
	PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet albumowner = null;
        conn = DbConnection.getConnection();
        stmt = conn.prepareStatement(GET_ALBUM_OWNER);
        stmt.setString(1,album_name);
        albumowner = stmt.executeQuery();
	String id = "";
        while(albumowner.next()){
            id = albumowner.getString(1);
        }
        albumowner.close();
        albumowner=null;

        stmt.close();
        stmt = null;

        conn.close();
        conn = null;
        return id;
    }
    public boolean addphoto(String album_name, int picture_id) throws SQLException{
	PreparedStatement stmt = null;
	Connection conn = null;
	ResultSet rs = null;

	    conn = DbConnection.getConnection();
	    stmt = conn.prepareStatement(NEW_ALBUM_PHOTO_STMT);
	    stmt.setInt(1,getAlbumId(album_name));
	    stmt.setString(2,album_name);
	    stmt.setInt(3,picture_id);
	    stmt.executeUpdate();
	    return true;
    }


    public int getAlbumId(String album_name) throws SQLException {

        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet useridrs = null;
        conn = DbConnection.getConnection();
        stmt = conn.prepareStatement(GET_ALBUM_ID_STMT);
        stmt.setString(1,album_name);
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



    public int getUserId2(String email) throws SQLException {

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


  public Album load(int album_id) {
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		Album album = null;
    try {
		conn = DbConnection.getConnection();
		stmt = conn.prepareStatement(LOAD_ALBUM_STMT);
                stmt.setInt(1, album_id);
			rs = stmt.executeQuery();
      if (rs.next()) {
        album = new Album();
        album.setAlbumId(album_id);
        album.setAlbumName(rs.getString(1));
        album.setUserId(rs.getInt(2));
        album.setUserEmail(rs.getString(3));
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
				try { stmt.close(); } catch (SQLException e) { ; }
				stmt = null;
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { ; }
				conn = null;
			}
		}

		return album;
	}

	public void save(Album album) {
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = DbConnection.getConnection();
			stmt = conn.prepareStatement(SAVE_ALBUM_STMT);
			stmt.setInt(1, album.getAlbumId());
			stmt.setString(2, album.getAlbumName());
			stmt.setInt(3, album.getUserId());
			stmt.setString(4, album.getUserEmail());
			stmt.executeUpdate();
			
			stmt.close();
			stmt = null;
			
			conn.close();
			conn = null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) { ; }
				stmt = null;
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { ; }
				conn = null;
			}
		}
	}

    public void delete(String album_name) {
	PreparedStatement stmt = null;
	Connection conn = null;
	try {
	    conn = DbConnection.getConnection();
	    stmt = conn.prepareStatement(DELETE_PHOTOS_STMT);
	    stmt.setString(1, album_name);
	    stmt.executeUpdate();
	    stmt = conn.prepareStatement(DELETE_ALBUM_STMT2);
            stmt.setString(1, album_name);
            stmt.executeUpdate();

	    stmt.close();
	    stmt = null;

	    conn.close();
	    conn = null;
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new RuntimeException("print" + album_name);
	} finally {
	    if (stmt != null) {
		try { stmt.close(); } catch (SQLException e) {;}
		stmt = null;
	    }
	    if (conn != null) {
		try { conn.close(); } catch (SQLException e) {;}
		conn = null;
	    }
	}
    }



	public List<Integer> allPicturesIds(String album_name) {
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		List<Integer> picturesIds = new ArrayList<Integer>();
		try {
			conn = DbConnection.getConnection();
			stmt = conn.prepareStatement(ALL_PICTURE_IDS_STMT);
			stmt.setString(1,album_name);
			rs = stmt.executeQuery();
			while (rs.next()) {
				picturesIds.add(rs.getInt(1));
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
				try { stmt.close(); } catch (SQLException e) { ; }
				stmt = null;
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { ; }
				conn = null;
			}
		}

		return picturesIds;
	}
}
