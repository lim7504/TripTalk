<%@page import="java.net.URLDecoder"%>
<%@page import="Servlet.MongoDB_Insert"%>
<%@page import="Servlet.MongoDB_Sample"%>
<%@page import="java.util.List"%>
<%@page import="com.google.android.gcm.server.MulticastResult"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.google.android.gcm.server.Message"%>
<%@page import="com.google.android.gcm.server.Sender"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@page import="java.net.URLEncoder"%>
<%@ page import="com.google.android.gcm.server.*"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@ page language="java" contentType="text/html;  charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Connection conn = null;
	PreparedStatement pstmt = null;
	Statement stmt = null; 
	String sql = null;
	String ISQUESTION = null;
	ResultSet rs = null;
    ArrayList<String> token = new ArrayList<String>();    //token값을 ArrayList에 저장	
	
    request.setCharacterEncoding("UTF-8");

    String msg = request.getParameter("message");;
    
    if(msg==null || msg.equals("")){
        msg="";
    }
    
	String QUESTION_USER_ID = null;
	String QUESTION_CONTENS = null;
	String QUESTION_AREA = null;
	String QUESTION_SUBJECT = null;
	String IS_QUESTION = null;

	request.setCharacterEncoding("utf-8");

	try {
	//String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
		String url = "jdbc:sqlserver://localhost:1433;databaseName=TEST_DB;user=sa;password=1;";
		conn = DriverManager.getConnection(url);
		out.println("MSSQL Success");
	} catch (Exception e) {
		out.println("MSSQL Fail");
		e.printStackTrace();
	}

	QUESTION_USER_ID = request.getParameter("QUESTION_USER_ID");
	QUESTION_CONTENS = request.getParameter("QUESTION_CONTENS");
	QUESTION_AREA = request.getParameter("QUESTION_AREA");
	QUESTION_SUBJECT = request.getParameter("QUESTION_SUBJECT");
	
	if(QUESTION_USER_ID ==null || QUESTION_CONTENS == null || QUESTION_AREA == null || QUESTION_SUBJECT == null)
	{
		return ;
	}
	//IS_QUESTION = request.getParameter("IS_QUESTION");
	
	// 토큰값 전달시 쿼리문 입력할곳임
	sql = "INSERT INTO [TRIPTALK_WATING](QUESTION_USER_ID,QUESTION_CONTENS,QUESTION_AREA,QUESTION_SUBJECT) VALUES(?,?,?,?)";
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, QUESTION_USER_ID);
	pstmt.setString(2, QUESTION_CONTENS);
	pstmt.setString(3, QUESTION_AREA);
	pstmt.setString(4, QUESTION_SUBJECT);
	/*
	TRIPTALK_WATING 테이블에 질문자 혹은 답변자 방 목록을 같이 저장 할 경우 대비
	sql = "INSERT INTO [TRIPTALK_WATING](QUESTION_USER_ID,QUESTION_CONTENS,QUESTION_AREA,QUESTION_SUBJECT,IS_QUESTION) VALUES(?,?,?,?,?)";
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, QUESTION_USER_ID);
	pstmt.setString(2, QUESTION_CONTENS);
	pstmt.setString(3, QUESTION_AREA);
	pstmt.setString(4, QUESTION_SUBJECT);


	//질문자/답변자 방 목록을 구별하기 위함
	if(IS_QUESTION.equals("Y"))
	{
		//질문자의 글
		pstmt.setInt(5,1);
	}else
	{
		//질문을 받으려는 답변자의 글
		pstmt.setInt(5,0);
	}
	*/
	pstmt.executeUpdate();
    
  	sql = "select [USER_AGE], [USER_SEX], [USER_FUN] from TRIPTALK_USER WHERE USER_ID = '{0}'";
   	sql = sql.replace("{0}", QUESTION_USER_ID);
		
 	stmt = conn.createStatement(); 
   	rs = stmt.executeQuery(sql);
	
/* 임시로 몽고디비 주석
    while(rs.next()){ 	
    	MongoDB_Insert.FingerInsert(rs.getInt("USER_AGE"),rs.getString("USER_SEX"),rs.getString("USER_FUN"), QUESTION_AREA);
    }
	
		 */
	
 	sql = "select [USER_ID], [USER_TOKEN] from TRIPTALK_USER WHERE USER_ADDRESS = '{0}'";
   	sql = sql.replace("{0}", QUESTION_AREA);
   	System.out.println(QUESTION_AREA); 
 	stmt = conn.createStatement(); 
   	rs = stmt.executeQuery(sql);
     
   	out.println("ACK"); 
   	msg = "질문이 왔습니다!! 질문주제 :" + QUESTION_SUBJECT + " 질문내용 : " + QUESTION_CONTENS;
  // 	msg = URLEncoder.encode(msg);    
   	msg = URLEncoder.encode(msg,"UTF-8");
    //모든 등록ID를 리스트로 묶음
    while(rs.next()){
    	response.sendRedirect("FCMManager.jsp?FCM_TYPE=QUESTION_MESSAGE" + "&" + "MESSAGE_ID=QUESTION_MESSAGE&" + "TARGET=" + rs.getString("USER_TOKEN")
		+ "&" + "MESSAGE=" + msg);
    	
        token.add(rs.getString("USER_TOKEN"));
       
        System.out.println(msg); 
    }
    conn.close();
%>