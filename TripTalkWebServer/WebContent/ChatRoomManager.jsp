<%@page import="java.sql.DriverManager"%>
<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%

//아직 사용하지 않는 JSP
//추후 채팅 방 생성시 사용할 용도

   	request.setCharacterEncoding("UTF-8");

	Connection conn = null;
	PreparedStatement pstmt = null;
	String[] chatRoomProtocol= {"ROOM_ID","REQUEST_USER_ID","RECEIVE_USER_ID","ROOM_NAME","CREATE_DATE"};
   	
	String ROOM_ID = null;
	String REQUEST_USER_ID = null;
	String RECEIVE_USER_ID = null;
	String ROOM_NAME = null;
	String CREATE_DATE = null;
	
	ROOM_ID = request.getParameter("ROOM_ID");
	REQUEST_USER_ID = request.getParameter("REQUEST_USER_ID");
	REQUEST_USER_ID = request.getParameter("RECEIVE_USER_ID");
	ROOM_NAME = request.getParameter("ROOM_NAME");
	CREATE_DATE = request.getParameter("CREATE_DATE");
	
 	if(ROOM_ID == null || ROOM_ID.isEmpty()|| ROOM_ID.equals(""))
 	{
 		out.println("Watting");
 	}else
 	{
 		out.println(REQUEST_USER_ID);
   	
		try {
			String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
			conn = DriverManager.getConnection(url);
			out.println("MSSQL Success");
		} catch (Exception e) {
			out.println("MSSQL Fail");
			e.printStackTrace();
	}

		if(conn != null)
		{
			//String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    //메시지 고유 ID
		// 토큰값 전달시 쿼리문 입력할곳임
			String sql = "INSERT INTO [TRIPTALK_ROOM](ROOM_ID,REQUEST_USER_ID,RECEIVE_USER_ID,ROOM_NAME,CREATE_DATE) VALUES(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
		
			pstmt.setString(1, ROOM_ID);
			pstmt.setString(2, REQUEST_USER_ID);
			pstmt.setString(3, REQUEST_USER_ID);
			pstmt.setString(4, ROOM_NAME);
			pstmt.setString(5, CREATE_DATE);
			
			pstmt.executeUpdate();//쿼리를 실행 하라는 명령어 
	 	}
	}

%>