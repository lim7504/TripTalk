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
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@ page language="java" contentType="text/html;  charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

	String USER_ID = null;
	Connection conn = null;
	PreparedStatement pstmt = null;
	Statement stmt = null; 
	String sql = null;
	ResultSet rs = null;

	USER_ID = request.getParameter("USER_ID");
	
	if(USER_ID == null)
	{
		System.out.println("USER_ID is null");
		return ;
	}
	
	try {
		String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
		//String url = "jdbc:sqlserver://localhost:1433;databaseName=TEST_DB;user=sa;password=1;";

		conn = DriverManager.getConnection(url);
		out.println("MSSQL Success");
	} catch (Exception e) {
		out.println("MSSQL Fail");
		e.printStackTrace();
	}

	if(conn != null)
	{
		stmt = conn.createStatement(); 

		//String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    //메시지 고유 ID
		sql = "SELECT * FROM [TRIPTALK_ROOM] WHERE REQUEST_USER_ID = '{0}' or RECEIVE_USER_ID = '{1}' ";
		sql = sql.replace("{0}", USER_ID);
		sql = sql.replace("{1}", USER_ID);
	    
	    rs = stmt.executeQuery(sql);
	    
		JSONArray jsonArry = new JSONArray();
        //모든 등록ID를 리스트로 묶음
       	while(rs.next())
       	{        	       	
        	JSONObject object = new JSONObject();
	        	
        	object.put("ROOM_ID", rs.getString("ROOM_ID"));
        	object.put("QUOSTION_TYPE", rs.getString("QUOSTION_TYPE"));
        	object.put("QUOTATION_CONTENS", rs.getString("QUOTATION_CONTENS"));
	        object.put("REQUEST_USER_ID", rs.getString("REQUEST_USER_ID"));
	        object.put("RECEIVE_USER_ID", rs.getString("RECEIVE_USER_ID"));
	        object.put("WAIT_ID", rs.getString("WAIT_ID"));
	        jsonArry.add(object);
	    }
	        
        out.clear();
        out.println(jsonArry);
        out.flush();	
        System.out.println(jsonArry);
	}

%>