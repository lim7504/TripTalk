<%@page import="java.io.PrintWriter"%>
<%@page import="java.sql.DriverManager"%>
<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%

/*
FCM을 통해 전달 받은 메세지 아이디를 통해 DB로부터 채팅 메세세지를 가져오는 JSP

FCM틍 통해 전달 받은 채팅 메세지 아이디를 이용하여 메세지 테이블에서 검색하여 해당 내용을 전송
추후 ROOM ID는 DB에서 검색을 빠르게 하기 위해 추가(현재는 미사용)
*/
   	request.setCharacterEncoding("UTF-8");

	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	String[] chatProtocol= {"ROOM_ID","MESSAGE_ID"};
   	
	String ROOM_ID = null;
	String GET_MESSAGE_ID = null;
	String CHAT_MESSAGE = null;
	int MESSAGE_TYPE = 0;
	Statement stmt = null;
	ResultSet rs = null;
	
	//ROOM_ID = request.getParameter("ROOM_ID");
	GET_MESSAGE_ID = request.getParameter("GET_MESSAGE_ID");
	
 	if(GET_MESSAGE_ID == null || GET_MESSAGE_ID.isEmpty()|| GET_MESSAGE_ID.equals(""))
 	{
 		return ;
 	}else
 	{
		try {
			String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
			//String url = "jdbc:sqlserver://localhost:1433;databaseName=TEST_DB;user=sa;password=1;";
			conn = DriverManager.getConnection(url);
			
		} catch (Exception e) {
			out.println("Can not Conection MSSQL");
			e.printStackTrace();
		}

		if(conn != null)
		{
			stmt = conn.createStatement(); 
			System.out.println("Start Search Form DB for the Message");
			//String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    //메시지 고유 ID
			String sql = "SELECT [ROOM_ID],[CHAT_MESSAGE],[IS_CHOOSE] FROM [TRIPTALK_MESSEGE] WHERE MESSEGE_ID = '{0}' ";
		    //sql = sql.replace("{0}", ROOM_ID);
		    sql = sql.replace("{0}", GET_MESSAGE_ID);
		    
		    rs = stmt.executeQuery(sql);
		    
			while(rs.next())
			{
				//ROOM_ID = rs.getString("ROOM_ID");
				CHAT_MESSAGE = rs.getString("CHAT_MESSAGE");
				MESSAGE_TYPE = rs.getInt("IS_CHOOSE");
			}
		
		
			//if(ROOM_ID.isEmpty() || ROOM_ID.equals(""))
			//{
			//	System.out.println("Can not found Room ID");
			//	out.println("NACK");
			//	return ;
			//}
			
			if(CHAT_MESSAGE.isEmpty() || CHAT_MESSAGE.equals(""))
			{
				System.out.println("Can not found Chat Message");
				out.println("NACK");
				return ;
			}
			
			stmt.clearBatch();
			System.out.println("MESSAGE_TYPE:"+MESSAGE_TYPE);
			System.out.println("CHAT_MESSAGE:"+CHAT_MESSAGE);
			out.println("MESSAGE_TYPE:"+ MESSAGE_TYPE +",CHAT_MESSAGE:"+CHAT_MESSAGE );
			
	 	}else
	 	{

			System.out.println("Please Check....Connection is NULL");
	 	}
	}

%>