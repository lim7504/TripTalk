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
	String SEARCH_AREA = null;
	String SEARCH_SUBJECT = null;
//	String IS_QUESTION = null;
	
	request.setCharacterEncoding("utf-8");
	//IS_QUESTION = request.getParameter("ISQUESTION");
	
	try 
	{
		String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
		//String url = "jdbc:sqlserver://localhost:1433;databaseName=TEST_DB;user=sa;password=1;";
		conn = DriverManager.getConnection(url);

		USER_ID = request.getParameter("USER_ID");
		SEARCH_AREA=request.getParameter("SERACH_AREA");
		SEARCH_SUBJECT=request.getParameter("SEARCH_SUBJECT");
		//IS_QUESTION = request.getParameter("IS_QUESTION");
		
		 System.out.println("USER_ID:"+USER_ID+",SEARCH_AREA"+SEARCH_AREA+",SEARCH_SUBJECT"+SEARCH_SUBJECT); 
/*
		//해당 지역 사람만 방 목록을 보이게 하는 경우
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
		sql +="			WATING.[QUESTION_USER_ID] != '{0}' and WATING.[QUESTION_AREA] = '{1}' and WATING.[QUESTION_SUBJECT] = '{2}'";

*/	    

//해당 지역 이외의 사람도 방이 보이도록 하는 경우
sql = "SELECT"; 
sql +="      WATING.QUESTION_USER_ID AS NICK					";
sql +="		,WATING.QUESTION_SUBJECT AS GRADE				";
sql +="		,WATING.QUESTION_CONTENS AS SUBTITLE			";
sql +="		,WATING.WATING_ID AS WAIT_ID			";
sql +="     FROM 											";
sql +="			TRIPTALK_WATING WATING						";
sql +="		WHERE											";
sql +="			WATING.[QUESTION_USER_ID] = '{0}' and WATING.[QUESTION_AREA] like '{1}' and WATING.[QUESTION_CONTENS] = '{2}' and WATING.[IS_MATCHING]=0";
//sql +="			WATING.[QUESTION_USER_ID] = '{0}' and WATING.[QUESTION_AREA] = '{1}' and WATING.[QUESTION_CONTENS] = '{2}' and WATING.[IS_MATCHING]=0";
/*	
	if(IS_QUESTION.equals("Y"))
	{
		sql +="         AND WATING.[IS_QUESTION] = 1 	";	질문자들이 만듬 방들
	}else
	{
		sql +="         AND WATING.[IS_QUESTION] = 0 	";	답변자들이 올린 방들	
	}
*/    
		sql = sql.replace("{0}", USER_ID);
		
		SEARCH_AREA =  SEARCH_AREA + "%";
	    
		sql = sql.replace("{1}", SEARCH_AREA);
	    sql = sql.replace("{2}", SEARCH_SUBJECT);
	    System.out.println("my:" + sql); 	
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
	        object.put("WAIT_ID", rs.getString("WAIT_ID"));
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