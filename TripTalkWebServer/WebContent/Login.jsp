<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
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
	String USER_TOKEN = null;

	String requestMethod = null;

	request.setCharacterEncoding("utf-8");

	try 
	{
		String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
		conn = DriverManager.getConnection(url);

		USER_ID = request.getParameter("USER_ID");
		USER_PASSWORD = request.getParameter("USER_PASSWORD");
		USER_TOKEN = request.getParameter("USER_TOKEN");
	
	    sql = "SELECT [USER_ID], [USER_PASSWORD], [USER_TOKEN], [USER_NICKNAME], [USER_AGE], [USER_SEX], [USER_FUN] FROM [TRIPTALK_USER] WHERE [USER_ID] = '{0}' AND [USER_PASSWORD] = '{1}'";
	    
	    sql = sql.replace("{0}", USER_ID);
	    sql = sql.replace("{1}", USER_PASSWORD);
		
	    stmt = conn.createStatement(); 
	    	
	    rs = stmt.executeQuery(sql);
     	JSONArray jsonArry = new JSONArray();
		if(rs.next())
		{	    	  	
        	
        	
			System.out.println(USER_TOKEN); 
	    	if(USER_TOKEN != rs.getString("USER_TOKEN"))
	    	{
	    		sql = "UPDATE [TRIPTALK_USER] SET [USER_TOKEN] = '" + USER_TOKEN + "' WHERE [USER_ID] = '{0}' AND [USER_PASSWORD] = '{1}'";
	    		
	    	    sql = sql.replace("{0}", USER_ID);
	    	    sql = sql.replace("{1}", USER_PASSWORD);
	    	    
	    	    stmt = conn.createStatement(); 	    	
	    	    stmt.execute(sql);
	    	}
	    	
	    	
	    	JSONObject object = new JSONObject();
	    	object.put("USER_ID", rs.getString("USER_ID"));
	    	object.put("USER_NICKNAME", rs.getString("USER_NICKNAME"));
	        object.put("USER_AGE", rs.getString("USER_AGE"));
	        object.put("USER_SEX", rs.getString("USER_SEX"));
	        object.put("USER_FUN", rs.getString("USER_FUN"));
	        jsonArry.add(object);
	        
	        out.clear();
	        out.println(jsonArry);
	        out.flush();
		}
	    else
	    	out.println("Fail");
		
	    out.flush();
	    
	} catch (Exception e) {
		out.println("Fail");
		e.printStackTrace();
	}
%>