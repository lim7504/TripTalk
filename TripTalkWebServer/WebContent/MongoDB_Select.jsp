<%@page import="Servlet.MongoDB_SelectForAgeNFun"%>
<%@page import="Servlet.MongoDB_SelectForAgeNSex"%>
<%@page import="Servlet.MongoDB_SelectForAreaSmallCount"%>
<%@page import="Servlet.MongoDB_SelectForAreaBigCount"%>
<%@page import="Servlet.MongoDB_SelectForAllCondition"%>
<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

	//조회 Index 1.가장 질문이 많은 지역    2. 가장 질문이 적은 지역    3. 같은나이대 같은 성별   4. 같은 나이대 같은취미 

	Integer SELECT_INDEX = null;
	Integer USER_AGE = null;
	String USER_SEX = null;
	String USER_FUN = null;
	
	try 
	{
		SELECT_INDEX = Integer.valueOf(request.getParameter("SELECT_INDEX"));
		USER_AGE = Integer.valueOf(request.getParameter("USER_AGE"));
		USER_SEX = request.getParameter("USER_SEX");
		USER_FUN = request.getParameter("USER_FUN");

		switch(SELECT_INDEX)
		{
			case 0:
				MongoDB_SelectForAreaBigCount db1 = new MongoDB_SelectForAreaBigCount();
				out.println(db1.Select());			
				break;
			
			case 1:
				MongoDB_SelectForAreaSmallCount db2 = new MongoDB_SelectForAreaSmallCount();
				out.println(db2.Select());			
				break;
		
			case 2:
				MongoDB_SelectForAgeNSex db3 = new MongoDB_SelectForAgeNSex();
				out.println(db3.Select(USER_AGE,USER_SEX));				
				break;
		
			case 3:
				MongoDB_SelectForAgeNFun db4 = new MongoDB_SelectForAgeNFun();
				out.println(db4.Select(USER_AGE,USER_FUN));			
				break;
			
			default :
				break;
		}
	}catch(Exception e)
	{
		
		
	}
	
	

%>