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
	Statement stmt = null;
	ResultSet rs = null;

	String[] chatRoomProtocol = { "ROOM_ID", "REQUEST_USER_ID", "RECEIVE_USER_ID", "ROOM_NAME", "CREATE_DATE" };

	String WAIT_ID = null;
	String REQUEST_USER_ID = null;
	String RECEIVE_USER_ID = null;
	String ROOM_NAME = null;
	String CREATE_DATE = null;
	String QUOSTION_TYPE = null;
	String QUOTATION_CONTENS = null;
	String sSearchRoomID = null;
	
	Boolean bExistRoom = false;

	WAIT_ID = request.getParameter("ROOM_ID");
	REQUEST_USER_ID = request.getParameter("REQUEST_USER_ID");
	RECEIVE_USER_ID = request.getParameter("RECEIVE_USER_ID");
	ROOM_NAME = request.getParameter("ROOM_NAME");
	CREATE_DATE = request.getParameter("CREATE_DATE");
	QUOSTION_TYPE = request.getParameter("QUOSTION_TYPE");

	if (REQUEST_USER_ID == null || REQUEST_USER_ID == null || ROOM_NAME == null || WAIT_ID == null) {
		System.out.println("parameter is null");
		return;
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

	if (conn != null) {
		stmt = conn.createStatement();

		String sql = "SELECT * FROM [TRIPTALK_ROOM] WHERE WAIT_ID = '{0}'";
		sql = sql.replace("{0}", WAIT_ID);

		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String sTempID = rs.getString("WAIT_ID");
		

			if(sTempID.isEmpty())
			{
				break;
			}

			if (sTempID.compareTo(WAIT_ID) == 0) {
				sSearchRoomID = rs.getString("ROOM_ID");
				System.out.println(sSearchRoomID);
				bExistRoom = true;
				break;
			}
		}

		
		stmt.clearBatch();

		if (bExistRoom) {		
			System.out.print("this room is exist!!");
			stmt = conn.createStatement(); 

			if(sSearchRoomID == null)
			{
				JSONArray jsonArry = new JSONArray();
				JSONObject object = new JSONObject();
				object.put("RESULT1", "NACK");

				jsonArry.add(object);
				System.out.println(jsonArry.toJSONString());

				out.clear();
				out.println(jsonArry);
				out.flush();			
				
				return ;
			}
			
			//String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    //메시지 고유 ID
			sql = "SELECT * FROM [TRIPTALK_MESSEGE] WHERE ROOM_ID = '{0}' ORDER BY CREATE_DATE";
			sql = sql.replace("{0}", sSearchRoomID);

		    rs = stmt.executeQuery(sql);
		    System.out.println("123");
			JSONArray jsonArry = new JSONArray();
	        //모든 등록ID를 리스트로 묶음
	       	while(rs.next())
	       	{        	       	
	        	JSONObject object = new JSONObject();
		        	
	        	object.put("ROOM_ID", rs.getString("ROOM_ID"));
	        	object.put("MESSEGE_ID", rs.getString("MESSEGE_ID"));
	        	object.put("CHAT_SENDER_ID", rs.getString("CHAT_SENDER_ID"));
		        object.put("CHAT_MESSAGE", rs.getString("CHAT_MESSAGE"));
		        object.put("IS_CHOOSE", rs.getString("IS_CHOOSE"));
		        jsonArry.add(object);
		    }
		        
	        out.clear();
	        out.println(jsonArry);
	        out.flush();	
	        System.out.println(jsonArry);
				
		}
		else
			{
			//String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    //메시지 고유 ID
			// 토큰값 전달시 쿼리문 입력할곳임
			sql = "INSERT INTO [TRIPTALK_ROOM](REQUEST_USER_ID,RECEIVE_USER_ID,ROOM_NAME,CREATE_DATE,WAIT_ID,QUOSTION_TYPE,QUOTATION_CONTENS) VALUES(?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, REQUEST_USER_ID);
			pstmt.setString(2, RECEIVE_USER_ID);
			pstmt.setString(3, ROOM_NAME);
			pstmt.setString(4, CREATE_DATE);
			pstmt.setString(5, WAIT_ID);
			pstmt.setString(6, QUOSTION_TYPE);
			pstmt.setString(7, ROOM_NAME);

			if (pstmt.executeUpdate() != 0)//쿼리를 실행 하라는 명령어 
			{
				sql = "UPDATE [TRIPTALK_WATING] SET [IS_MATCHING] = 1 WHERE [WATING_ID] = '{0}'";

				sql = sql.replace("{0}", WAIT_ID);

				stmt = conn.createStatement();

				if (stmt.execute(sql) == false) {
					JSONArray jsonArry = new JSONArray();
					JSONObject object = new JSONObject();

					object.put("RESULT", "ACK");
					jsonArry.add(object);

					out.clear();
					out.println(jsonArry);
					out.flush();
				} else {
					JSONArray jsonArry = new JSONArray();
					JSONObject object = new JSONObject();
					object.put("RESULT1", "NACK");

					jsonArry.add(object);
					System.out.println(jsonArry.toJSONString());

					out.clear();
					out.println(jsonArry);
					out.flush();
				}

			} else {
				JSONArray jsonArry = new JSONArray();
				JSONObject object = new JSONObject();
				object.put("RESULT2", "NACK");

				jsonArry.add(object);
				System.out.println(jsonArry.toJSONString());

				out.clear();
				out.println(jsonArry);
				out.flush();

			}
		}
	}
%>