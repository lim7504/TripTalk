package Servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import javamysql.Board;
//import javamysql.MemberBean;

public class ConnectDB {
	// �̱��� �������� ��� �ϱ��� �� �ڵ��
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
 	private final String dbId = "guest";
 	private final String dbPw = "1234";
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null;
	private String sql = "";
	private String sql2 = "";
	
	public ConnectDB() {
//	 	 try{
//	 	   Class.forName(JDBC_DRIVER);
//	 	   }catch(Exception e){
//	 	      System.out.println("Error : JDBC ");
//	 	   }
	     }

    public String tt_blog_search(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sent_info = "{\"code\":\"fail\"}";

        try {
            conn = DriverManager.getConnection(url);
            String sql = "select blog_id,img1,img2,img3,img4,img5 from TRIPTALK_BLOG where blog_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                sent_info = Integer.toString(rs.getInt("blog_id")) + "/123/" + rs.getString("img1") + "/123/"
                + rs.getString("img2") + "/123/" + rs.getString("img3") + "/123/" + rs.getString("img4") + "/123/"
                + rs.getString("img5");
            }
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        } finally {
        	if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
       		if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return sent_info;
    }
    
    public String tt_blog_DetailSearch(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sent_info = "{\"code\":\"succ\",\"contents\":[";

        try {
            conn = DriverManager.getConnection(url);
            String sql = "select * from TRIPTALK_BLOG where blog_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
            	sent_info = sent_info + "{" +
            	"\"blog_id\":\"" + rs.getInt("blog_id") + "\"," + 
            	"\"title\":\"" + rs.getString("title") + "\"," + 
            	"\"writer\":\"" + rs.getString("writer") + "\"," + 
            	"\"img1\":\"" + rs.getString("img1") + "\"," + 
            	"\"img2\":\"" + rs.getString("img2") + "\"," + 
            	"\"img3\":\"" + rs.getString("img3") + "\"," + 
            	"\"img4\":\"" + rs.getString("img4") + "\"," + 
            	"\"img5\":\"" + rs.getString("img5") + "\"," + 
            	"\"content\":\"" + rs.getString("content") + "\"," + 
            	"\"date\":\"" + rs.getString("date") + "\"},";
            	sent_info = sent_info.substring(0, sent_info.length()-1);
            	sent_info = sent_info + "],\"num\":\"" + "1" + "\"}";
            } else {
            	sent_info = "{\"code\":\"fail\"}";
            }
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        } finally {
        	if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
       		if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return sent_info;
    }
    
    public String tt_blog_AllSearch() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sent_info = "{\"code\":\"succ\",\"contents\":[";
        int count = 0;

        try {
            conn = DriverManager.getConnection(url);
            String sql = "select * from TRIPTALK_BLOG where title != '' and content != '' ORDER BY blog_id DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while(rs.next()) {
            	sent_info = sent_info + "{" +
            	"\"blog_id\":\"" + rs.getInt("blog_id") + "\"," + 
            	"\"title\":\"" + rs.getString("title") + "\"," + 
            	"\"writer\":\"" + rs.getString("writer") + "\"," + 
            	"\"img1\":\"" + rs.getString("img1") + "\"," + 
            	"\"date\":\"" + rs.getString("date") + "\"},";
            	count++;
			}
            if(count == 0)
            	sent_info = "{\"code\":\"fail\"}";
            else {
            	sent_info = sent_info.substring(0, sent_info.length()-1);
            	sent_info = sent_info + "],\"num\":\"" + count + "\"}";
            }
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        } finally {
        	if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
       		if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return sent_info;
    }
    
    public String tt_blog_image(int id, String imageurl, String imagenum) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sent_info =  "{\"code\":\"fail\"}";

        try {
            conn = DriverManager.getConnection(url);
        	sql = "update TRIPTALK_BLOG set img" + imagenum + "=? where blog_id = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, imageurl);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();

			sent_info = "{\"code\":\"succ\"}";
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        } finally {
        	if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
       		if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return sent_info;
    }
    
    public String tt_blog_reset(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sent_info =  "fail";
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(url);
        	sql = "select * from TRIPTALK_BLOG where blog_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
	        rs = pstmt.executeQuery();
	        
			if(rs.next()) {
				sent_info = rs.getString("img1") + "!@#" + rs.getString("img2") + "!@#" + rs.getString("img3") + "!@#" + 
						rs.getString("img4") + "!@#" + rs.getString("img5");
				System.out.println("3");
			}
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        } finally {
        	if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
       		if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        
        try {
            conn = DriverManager.getConnection(url);
        	sql = "update TRIPTALK_BLOG set img1='none_image',img2='none_image',img3='none_image',img4='none_image',img5='none_image' where blog_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        } finally {
        	if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
       		if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return sent_info;
    }
    
    public String tt_blog_insert(int id, String title, String content,String writer) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sent_info =  "{\"code\":\"fail\"}";

        try {
        	System.out.println("///" + id + "///" + title + "///" + content);
            conn = DriverManager.getConnection(url);
        	sql = "update TRIPTALK_BLOG set title=?, content = ?, writer= ? where blog_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, content);		
			pstmt.setString(3, writer);
			pstmt.setInt(4, id);
			pstmt.executeUpdate();

			sent_info = "{\"code\":\"succ\"}";
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        } finally {
        	if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
       		if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return sent_info;
    }
    
    public String tt_blogOut(int blog_id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sent_info =  "fail";

        try {
            conn = DriverManager.getConnection(url);
        	sql = "delete from TRIPTALK_BLOG where blog_id=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, blog_id);
			pstmt.executeUpdate();

			sent_info = "succ";
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        } finally {
        	if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
       		if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return sent_info;
    }
    
    public int tt_blogIn() {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sent_info =  "{\"code\":\"fail\"}";
        int id = 0;
        
        try {
        	
            conn = DriverManager.getConnection(url);
        	sql = "insert into TRIPTALK_BLOG (title,writer,img1,img2,img3,img4,img5,content,date) values(default,'12341234',default,default,default,default,default,default,getdate())";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

			sent_info = "{\"code\":\"succ\"}";
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        } finally {
        	if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
       		if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        
        try {
            conn = DriverManager.getConnection(url);
    		sql = "select * FROM TRIPTALK_BLOG order by blog_id DESC";
			pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
			
			if(rs.next()) {
            	id = rs.getInt("blog_id");
			}
    	} catch (Exception ex) {
            System.out.println("Exception" + ex);
        } finally {
        	if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
       		if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return id;
    }
}
