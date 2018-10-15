<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Connection conn = null;
	PreparedStatement pstmt = null;
	String sql = null;
	String token = null;
	String requestMethod = null;

	request.setCharacterEncoding("utf-8");

	try {
		
		//String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
		String url = "jdbc:sqlserver://localhost:1433;databaseName=TEST_DB;user=sa;password=1;";
		conn = DriverManager.getConnection(url);
		out.println("MSSQL Success");
	} catch (Exception e) {
		out.println("MSSQL Fail");
		e.printStackTrace();
	}

	token = request.getParameter("Token");

	if (token.equals("")) {
		out.println("Not Receive Token");
	} else {
		// 토큰값 전달시 쿼리문 입력할곳임
		sql = "INSERT INTO TRIPTALK_TOKEN(Token) VALUES(?)";
		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, token);

		pstmt.executeUpdate();//쿼리를 실행 하라는 명령어
	}
%>