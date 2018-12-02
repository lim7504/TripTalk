<%@page import="java.io.PrintWriter"%>
<%@page import="java.sql.DriverManager"%>
<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLEncoder"%>
<%
	/*
	채팅 방에 채팅 메세지를 받
	-클라이언트 즉, 안드로이드로부터 채팅 메세지를 전송 받아 db에 저장을 진행함
	-채팅 메세지를 전송 받을 때 함께 받은 채팅 방 아이디를 통해 질문자와 답변자에 대한 아이디를 db로부터 가져온다
	가져오는 이유는 채팅 메세지를 전송 받아야하는 사람의 아이디를 통해 토큰 값을 구해오기 위함.
	
	채팅 메세지를 전송한 사람의 아이디를 각각 채팅 방의 질문자, 답변자 정보와 비교하여 채팅 메세지를 수진 할 사람의 아이디를 가져온다.
	수신 할 사람의 아이디를 기본으로 유저 테이블에서 토큰 값을 뽑아온다.
	
	채팅 메세제지를 db 저장 및 유저 정보를 제대로 가져왔다면 FCM Manager.jsp에 채팅 메세지/채팅 메세지 아이디(db저장시 생성)/수신 받을 유저 아이디를 전달한다.
	*/

	request.setCharacterEncoding("UTF-8");

	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	String[] chatProtocol = { "ROOM_ID", "MESSAGE_ID", "CHAT_SENDER_ID", "CHAT_MESSAGE", "CHAR_IMAGE",
			"CREATE_DATE" };

	String WAIT_ID = null;
	String ROOM_ID = null;
	String USER_ID = null;
	String CHAT_MESSAGE = null;
	String CHAT_MESSAGE_ID = null;
	String CHAT_CREATE_DATE = null;
	String USER_TYPE = null;
	String TARGET_USER = null;
	String MESSAGE_TYPE = null;
	Statement stmt = null;

	ResultSet rs = null;

	WAIT_ID = request.getParameter("WAIT_ID");
	USER_ID = request.getParameter("CHAT_SENDER_ID");
	CHAT_MESSAGE = request.getParameter("CHAT_MESSAGE");
	//	CHAT_MESSAGE_ID = request.getParameter("MESSAGE_ID");
	CHAT_CREATE_DATE = request.getParameter("CREATE_DATE");
	//USER_TYPE = request.getParameter("USER_TYPE");
	MESSAGE_TYPE = request.getParameter("MESSAGE_TYPE");
	
	System.out.println("MESSAGE_TYPE:"+MESSAGE_TYPE);
	System.out.println(CHAT_MESSAGE);
	if (WAIT_ID == null ||USER_ID == null || CHAT_MESSAGE == null || CHAT_CREATE_DATE == null || MESSAGE_TYPE == null) {
		out.println("Watting");
	} else {
		
		if(MESSAGE_TYPE.equals("4"))
		{
			return ;
		}
		
		try {
			String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
			//String url = "jdbc:sqlserver://localhost:1433;databaseName=TEST_DB;user=sa;password=1;";
			conn = DriverManager.getConnection(url);

		} catch (Exception e) {
			out.println("Can not Conection MSSQL");
			e.printStackTrace();
		}

		if (conn != null) {
			stmt = conn.createStatement();

			
			String sql = "SELECT * FROM [TRIPTALK_ROOM] WHERE WAIT_ID = '{0}'";
			sql = sql.replace("{0}", WAIT_ID);

			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				ROOM_ID = rs.getString("ROOM_ID");
			}

			if(ROOM_ID == null)
			{
				System.out.println("Room ID is null");
				out.println("NACK");
				return;	
			}
			
			if (ROOM_ID.isEmpty() || ROOM_ID.equals("")) {
				System.out.println("Can not found Room ID");
				out.println("NACK");
				return;
			}

			sql = "SELECT [REQUEST_USER_ID], [RECEIVE_USER_ID] FROM [TRIPTALK_ROOM] WHERE ROOM_ID = '{0}' ";
			sql = sql.replace("{0}", ROOM_ID);
			rs = stmt.executeQuery(sql);

			if (rs.next()) {
				String REQUEST_USER_ID = rs.getString("REQUEST_USER_ID");
				String RECEIVE_USER_ID = rs.getString("RECEIVE_USER_ID");

				if (REQUEST_USER_ID.equals(USER_ID)) {
					//sql = "SELECT * FROM [TRIPTALK_USER] WHERE USER_ID = '{0}' ";
					//sql = sql.replace("{0}", RECEIVE_USER_ID);
					//rs = stmt.executeQuery(sql);

					///if(rs.next())
					//{
					if (!MESSAGE_TYPE.contains("3") && !MESSAGE_TYPE.contains("9")) {
						MESSAGE_TYPE = "1";
					}
					TARGET_USER = RECEIVE_USER_ID;//rs.getString("USER_TOKEN");
					//}

				}

				if (RECEIVE_USER_ID.equals(USER_ID)) {

					//sql = "SELECT * FROM [TRIPTALK_USER] WHERE USER_ID = '{0}' ";
					//sql = sql.replace("{0}", REQUEST_USER_ID);

					//rs = stmt.executeQuery(sql);

					//if(rs.next())
					//{
					if (!MESSAGE_TYPE.contains("3") && !MESSAGE_TYPE.contains("9")) {
						MESSAGE_TYPE = "2";
					}
					TARGET_USER = REQUEST_USER_ID;//rs.getString("USER_TOKEN");
					//}

				}
			}

			stmt.clearBatch();
			/*	
				//MESSAGE_ID = String.valueOf(Math.random() % 100 + 1); 
			// 토큰값 전달시 쿼리문 입력할곳임
				sql = "INSERT INTO [TRIPTALK_MESSEGE](ROOM_ID,CHAT_SENDER_ID,CHAT_MESSAGE,CREATE_DATE) VALUES(?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
			
				pstmt.setString(1, ROOM_ID);
				//pstmt.setString(2, MESSAGE_ID);
				pstmt.setString(2, USER_ID);
				pstmt.setString(3, CHAT_MESSAGE);
				pstmt.setString(4, CHAT_CREATE_DATE);
				*/
			String MESSAGE_ID = "";
			MESSAGE_ID= String.valueOf(Math.random() % 100 + 1); //메시지 고유 ID
			System.out.println(USER_ID);
			// 토큰값 전달시 쿼리문 입력할곳임
			sql = "INSERT INTO [TRIPTALK_MESSEGE](ROOM_ID,MESSEGE_ID,CHAT_SENDER_ID,CHAT_MESSAGE,IS_CHOOSE) VALUES(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, ROOM_ID);
			pstmt.setString(2, MESSAGE_ID);
			pstmt.setString(3, USER_ID);
			pstmt.setString(4, CHAT_MESSAGE);
			pstmt.setInt(5, Integer.parseInt(MESSAGE_TYPE));

			pstmt.executeUpdate();//쿼리를 실행 하라는 명령어 

			if (TARGET_USER == null || TARGET_USER.isEmpty() || TARGET_USER.equals("")) {
				System.out.println("Wrong TARGET_USER ID");
				out.print("NACK");
				return;
			}

			CHAT_MESSAGE = URLEncoder.encode(CHAT_MESSAGE,"UTF-8");
			
			response.sendRedirect("FCMManager.jsp?FCM_TYPE=CHAT_MESSAGE" + "&"+ "MESSAGE_ID=" + MESSAGE_ID + "&" + "TARGET=" + TARGET_USER
					+ "&" + "MESSAGE=" + CHAT_MESSAGE);

			out.println("ACK");

		} else {

			System.out.println("Please Check....Connection is NULL");
		}
	}
%>