<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Connection conn = null;
	PreparedStatement pstmt = null;
	String sql = null;
	
	String USER_ID = null;
	String USER_PASSWORD = null;
	String USER_NICKNAME = null;
	String USER_AGE = null;
	String USER_SEX = null;
	String USER_FUN = null;
	String USER_ADDRESS = null;
	String USER_TOKEN = null;
	
	String requestMethod = null;

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
	USER_PASSWORD = request.getParameter("USER_PASSWORD");
	USER_NICKNAME = request.getParameter("USER_NICKNAME");
	USER_AGE = request.getParameter("USER_AGE");
	USER_SEX = request.getParameter("USER_SEX");
	USER_FUN = request.getParameter("USER_FUN");
	USER_ADDRESS = request.getParameter("USER_ADDRESS");
	USER_TOKEN = request.getParameter("USER_TOKEN");


	// 토큰값 전달시 쿼리문 입력할곳임
	sql = "INSERT INTO [TRIPTALK_USER](USER_ID,USER_PASSWORD,USER_NICKNAME,USER_AGE,USER_SEX,USER_FUN,USER_ADDRESS,USER_TOKEN) VALUES(?,?,?,?,?,?,?,?)";
	pstmt = conn.prepareStatement(sql);

	pstmt.setString(1, USER_ID);
	pstmt.setString(2, USER_PASSWORD);
	pstmt.setString(3, USER_NICKNAME);
	pstmt.setString(4, USER_AGE);
	pstmt.setString(5, USER_SEX);
	pstmt.setString(6, USER_FUN);
	pstmt.setString(7, USER_ADDRESS);
	pstmt.setString(8, USER_TOKEN);

	pstmt.executeUpdate();//쿼리를 실행 하라는 명령어 
	
%>