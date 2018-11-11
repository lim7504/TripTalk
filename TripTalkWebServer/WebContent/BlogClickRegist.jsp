<%@page import="java.net.URLDecoder"%>
<%@page import="Servlet.MongoDB_Insert"%>
<%@page import="Servlet.MongoDB_Sample"%>
<%@page import="java.util.List"%>
<%@page import="com.google.android.gcm.server.MulticastResult"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.google.android.gcm.server.Message"%>
<%@page import="com.google.android.gcm.server.Sender"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@page import="java.net.URLEncoder"%>
<%@ page import="com.google.android.gcm.server.*"%>
<%@ page language="java" contentType="text/html;  charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Connection conn = null;
	PreparedStatement pstmt = null;
	Statement stmt = null; 
	String sql = null;
	String ISQUESTION = null;
	ResultSet rs = null;
    ArrayList<String> token = new ArrayList<String>();    //token값을 ArrayList에 저장	
	
    request.setCharacterEncoding("UTF-8");
    
	String USER_ID = null;
	String BLOG_ID = null;
	
	request.setCharacterEncoding("utf-8");

	try {
		String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
		//String url = "jdbc:sqlserver://localhost:1433;databaseName=TEST_DB;user=sa;password=1;";
		conn = DriverManager.getConnection(url);
		out.println("MSSQL Success");
	} catch (Exception e) {
		out.println("MSSQL Fail");
		e.printStackTrace();
	}

	USER_ID = request.getParameter("USER_ID");
	BLOG_ID = request.getParameter("BLOG_ID");
	
	if(USER_ID ==null || BLOG_ID == null)
	{
		return ;
	}
		
	
	
	

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//USER Table에 최근 선택 Blog를 저장
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
    sql = "UPDATE"; 
    sql +="     [TRIPTALK_USER]									";
    sql +="		SET												";
    sql +="			 LAST_BLOG_ID = ?							";
    sql +="		WHERE 											";
    sql +="			USER_ID = ?									";
	

	pstmt = conn.prepareStatement(sql);

	pstmt.setString(1, BLOG_ID);
	pstmt.setString(2, USER_ID);

	pstmt.executeUpdate();//쿼리를 실행 하라는 명령어 
	
	
	
	
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//MONGODB에 Blog Click Data 수집
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
  	sql = "select [USER_AGE], [USER_SEX], [USER_FUN], [TENDENCY1], [TENDENCY2], [TENDENCY3] from TRIPTALK_USER WHERE USER_ID = '{0}'";
   	sql = sql.replace("{0}", USER_ID);
		
 	stmt = conn.createStatement(); 
   	rs = stmt.executeQuery(sql);
	

    while(rs.next()){ 	
    	MongoDB_Insert.FingerInsertForBlog(rs.getInt("USER_AGE"),rs.getString("USER_SEX"),rs.getString("USER_FUN"), BLOG_ID, rs.getString("TENDENCY1"), rs.getString("TENDENCY2"), rs.getString("TENDENCY3"));
    }
			 
    conn.close();
%>