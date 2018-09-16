<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Connection conn = null;
	Statement stmt = null;
    ResultSet rs = null;
	String sql = null;
	
	String USER_ID = null;
	String USER_PASSWORD = null;

	String requestMethod = null;

	request.setCharacterEncoding("utf-8");

	try 
	{
		String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
		conn = DriverManager.getConnection(url);

		USER_ID = request.getParameter("USER_ID");
	
	    sql = "SELECT"; 
	    sql +="      QUEST.USER_NICKNAME AS NICK					";
	    sql +="		,WATING.QUESTION_SUBJECT AS GRADE				";
	    sql +="		,WATING.QUESTION_CONTENS AS SUBTITLE			";
	    sql +="     FROM 											";
    	sql +="			TRIPTALK_USER DAP							";
	    sql +="		LEFT JOIN 										";
	    sql +="			TRIPTALK_WATING WATING						";
   		sql +="			ON											";
		sql +="			DAP.USER_ADDRESS = WATING.QUESTION_AREA		";  
		sql +="		LEFT JOIN										";
		sql +="			TRIPTALK_USER QUEST							";
		sql +="			ON											";
		sql +="			WATING.QUESTION_USER_ID = QUEST.[USER_ID]	";
		sql +="			AND IS_MATCHING = 0							";
		sql +="		WHERE											"; 
		sql +="			DAP.[USER_ID] = '{0}'						";
//		sql +="         AND DAP.[USER_ID] <> QUEST.[USER_ID]		";	

	    
	    sql = sql.replace("{0}", USER_ID);
		
	    stmt = conn.createStatement(); 
	    	
	    rs = stmt.executeQuery(sql);
			
     	JSONArray jsonArry = new JSONArray();
        //모든 등록ID를 리스트로 묶음
       	while(rs.next())
       	{        	       	
        	JSONObject object = new JSONObject();
	        	
	        object.put("NICK", rs.getString("NICK"));
	        object.put("GRADE", rs.getString("GRADE"));
	        object.put("SUBTITLE", rs.getString("SUBTITLE"));
	        jsonArry.add(object);
	        System.out.println(jsonArry.toJSONString()); 
	    }
	        
        out.clear();
        out.println(jsonArry);
        out.flush();
	    
	} catch (Exception e) {
		out.println("Fail");
		e.printStackTrace();
	}
%>