<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Connection conn = null;
	PreparedStatement pstmt = null;
	String sql = null;
	
	String USER_ID = null;
	String TENDENCY1 = null;
	String TENDENCY2 = null;
	String TENDENCY3 = null;
	
	String requestMethod = null;

	request.setCharacterEncoding("utf-8");

	try {
		String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
		conn = DriverManager.getConnection(url);
		out.println("MSSQL Success");
	} catch (Exception e) {
		out.println("MSSQL Fail");
		e.printStackTrace();
	}

 	USER_ID = request.getParameter("USER_ID");
 	TENDENCY1 = request.getParameter("TENDENCY1");
 	TENDENCY2 = request.getParameter("TENDENCY2");
 	TENDENCY3 = request.getParameter("TENDENCY3");


    sql = "UPDATE"; 
    sql +="     [TRIPTALK_USER]									";
    sql +="		SET												";
    sql +="			 TENDENCY1 = ?								";
  	sql +="     	,TENDENCY2 = ?								";
  	sql +="     	,TENDENCY3 = ?								";
    sql +="		WHERE 											";
    sql +="			USER_ID = ?									";
	

	pstmt = conn.prepareStatement(sql);

	pstmt.setString(1, TENDENCY1);
	pstmt.setString(2, TENDENCY2);
	pstmt.setString(3, TENDENCY3);
	pstmt.setString(4, USER_ID);

	pstmt.executeUpdate();//쿼리를 실행 하라는 명령어 
	
%>