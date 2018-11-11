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


	String REQUEST_USER_ID = null;
	String RECEIVE_USER_ID = null;
	String ROOM_NAME = null;
	String CREATE_DATE = null;
	ResultSet rs = null;
	Statement stmt = null;
	ROOM_NAME = request.getParameter("ROOM_NAME");


	if(ROOM_NAME == null )
	{
		return ;
	}
		out.println(REQUEST_USER_ID);

		try {
			//String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
			String url = "jdbc:sqlserver://localhost:1433;databaseName=TEST_DB;user=sa;password=1;";
			conn = DriverManager.getConnection(url);
			out.println("MSSQL Success");
		} catch (Exception e) {
			out.println("MSSQL Fail");
			e.printStackTrace();
		}

		if (conn != null) {
			
			String sql="";
			//String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    //메시지 고유 ID
			// 토큰값 전달시 쿼리문 입력할곳임
			sql = "DELETE FROM [TRIPTALK_ROOM] WHERE ROOM_NAME = '{0}' ";
			sql = sql.replace("{0}", ROOM_NAME);
			pstmt = conn.prepareStatement(sql);

			if(pstmt.executeUpdate() != 0)//쿼리를 실행 하라는 명령어 
			{
	    		sql = "UPDATE [TRIPTALK_WATING] SET [IS_MATCHING] = 0 WHERE [WATING_ID] = '{0}'";

				sql = sql.replace("{0}", ROOM_NAME);

				stmt = conn.createStatement();

				if(stmt.execute(sql) == false)
				{
		     		JSONArray jsonArry = new JSONArray();   	
		       	 	JSONObject object = new JSONObject();
			        	
			   	  object.put("RESULT", "ACK");
			  	  jsonArry.add(object);

		      	  out.clear();
		      	  out.println(jsonArry);
		     	   out.flush();
				}else
				{
			     	JSONArray jsonArry = new JSONArray();    	       	
		        	JSONObject object = new JSONObject();
			        object.put("RESULT", "NACK");
	
			        jsonArry.add(object);
			        System.out.println(jsonArry.toJSONString()); 
			        
		        	out.clear();
		       	 	out.println(jsonArry);
		       	 	out.flush();
	
				}
;
			}else
			{
		     	JSONArray jsonArry = new JSONArray();    	       	
		        JSONObject object = new JSONObject();
			    object.put("RESULT", "NACK");
	
			    jsonArry.add(object);
			    System.out.println(jsonArry.toJSONString()); 
			        
		        out.clear();
		        out.println(jsonArry);
		        out.flush();

			}
		}
	
%>