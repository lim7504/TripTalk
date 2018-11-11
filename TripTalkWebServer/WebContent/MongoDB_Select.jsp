<%@page import="Servlet.MongoDB_SelectForAlikeUserArea"%>
<%@page import="Servlet.MongoDB_SelectForAgeNFun"%>
<%@page import="Servlet.MongoDB_SelectForAgeNSex"%>
<%@page import="Servlet.MongoDB_SelectForAreaSmallCount"%>
<%@page import="Servlet.MongoDB_SelectForAreaBigCount"%>
<%@page import="Servlet.MongoDB_SelectForAllCondition"%>
<%@page import="Servlet.MongoDB_SelectForAlikeArea"%>
<%@page import="Servlet.MongoDB_SelectForAlikeBlog"%>
<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

	//조회 Index 0.가장 질문이 많은 지역    1.가장 질문이 적은 지역    2.같은나이대 같은 성별   3.같은 나이대 같은취미   4.유사 성향의 지역   5.유사성향의 블로그   6. 같은 지역을 질문했던 유저들의 선호 지역 

	Integer SELECT_INDEX = null;
	Integer USER_AGE = null;
	String USER_SEX = null;
	String USER_FUN = null;
	String USER_ID = null;
	
	try 
	{
		USER_ID = request.getParameter("USER_ID");
		SELECT_INDEX = Integer.valueOf(request.getParameter("SELECT_INDEX"));
		USER_AGE = Integer.valueOf(request.getParameter("USER_AGE"));
		USER_SEX = request.getParameter("USER_SEX");
		USER_FUN = request.getParameter("USER_FUN");
		
		switch(SELECT_INDEX)
		{
			case 0:
				MongoDB_SelectForAreaBigCount db0 = new MongoDB_SelectForAreaBigCount();
				out.println(db0.Select());			
				break;
			
			case 1:
				MongoDB_SelectForAreaSmallCount db1 = new MongoDB_SelectForAreaSmallCount();
				out.println(db1.Select());			
				break;
		
			case 2:
				MongoDB_SelectForAgeNSex db2 = new MongoDB_SelectForAgeNSex();
				out.println(db2.Select(USER_AGE, USER_SEX));				
				break;
		
			case 3:
				MongoDB_SelectForAgeNFun db3 = new MongoDB_SelectForAgeNFun();
				out.println(db3.Select(USER_AGE, USER_FUN));			
				break;
			
			case 4:
				MongoDB_SelectForAlikeArea db4 = new MongoDB_SelectForAlikeArea();
				out.println(db4.Select(USER_ID));			
				break;
				
			case 5:
				MongoDB_SelectForAlikeBlog db5 = new MongoDB_SelectForAlikeBlog();
				out.println(db5.Select(USER_ID));			
				break;
				
			case 6:
				MongoDB_SelectForAlikeUserArea db6 = new MongoDB_SelectForAlikeUserArea();
				out.println(db6.Select(USER_ID));			
				break;
			
			default :
				break;
		}
	}catch(Exception e)
	{
		
		out.println(e.getMessage());		
	}
	
	

%>