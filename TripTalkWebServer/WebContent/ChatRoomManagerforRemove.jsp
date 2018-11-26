<%@page import="java.sql.DriverManager"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
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
	String[] chatRoomProtocol = { "ROOM_ID", "REQUEST_USER_ID", "RECEIVE_USER_ID", "ROOM_NAME", "CREATE_DATE" };


	String QUESTION_USER_ID = null;
	String WATING_ID = null;
	String CREATE_DATE = null;
	String USER_ID = null;
	ResultSet rs = null;
	Statement stmt = null;
	
	WATING_ID = request.getParameter("WATING_ID");
	QUESTION_USER_ID = request.getParameter("QUESTION_ID");
	USER_ID = request.getParameter("USER_ID");

	if(WATING_ID == null || QUESTION_USER_ID == null || USER_ID == null)
	{
		return ;
	}
	
	System.out.println(WATING_ID);
		try {
			String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
			//String url = "jdbc:sqlserver://localhost:1433;databaseName=TEST_DB;user=sa;password=1;";
			conn = DriverManager.getConnection(url);
			out.println("MSSQL Success");
		} catch (Exception e) {
			out.println("MSSQL Fail");
			e.printStackTrace();
		}

		if (conn != null) {
			
			String sql="";
			
			if(QUESTION_USER_ID.equals(USER_ID))
			{

				System.out.println(WATING_ID + "방에서 " + USER_ID + " 유저가 나가셨습니다.");
				
				sql = "DELETE FROM [TRIPTALK_WATING] WHERE WATING_ID = '{0}'";
				sql = sql.replace("{0}", WATING_ID);
			
				stmt = conn.createStatement();

				if(stmt.execute(sql) == false)
				{
					System.out.println("ChatRoomManagerforRemove.jsp ACK");
		      	  	out.clear();
		      	  	out.println("ACK");
		     	   	out.flush();
				}else
				{
					System.out.println("ChatRoomManagerforRemove.jsp NACK");
		        	out.clear();
		       	 	out.println("NACK");
		       	 	out.flush();
	
				}
				
			}else
			{

				System.out.println(WATING_ID + "방에서 " + USER_ID + " 유저가 나가셨습니다.");
				
	    		sql = "UPDATE [TRIPTALK_WATING] SET [IS_MATCHING] = 0 WHERE [WATING_ID] = '{0}'";

				sql = sql.replace("{0}", WATING_ID);
				
				stmt = conn.createStatement();

				if(stmt.execute(sql) == false)
				{
					System.out.println("ChatRoomManagerforRemove.jsp ACK");
		      	  	out.clear();
		      	  	out.println("ACK");
		     	   	out.flush();
				}else
				{
					System.out.println("ChatRoomManagerforRemove.jsp NACK");
		        	out.clear();
		       	 	out.println("NACK");
		       	 	out.flush();
				}
			}
		}
%>