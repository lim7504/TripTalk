<%@page import="Servlet.MongoDB_SelectForQuestionAreaDetail"%>
<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

	String QUESTION_AREA = null;
	
	try 
	{
		QUESTION_AREA = request.getParameter("QUESTION_AREA");

	
		MongoDB_SelectForQuestionAreaDetail db0 = new MongoDB_SelectForQuestionAreaDetail();
		out.println(db0.Select(QUESTION_AREA));			
			
	}catch(Exception e)
	{
		
		out.println(e.getMessage());		
	}
	
	

%>