<%@page import="Servlet.MongoDB_Insert"%>
<%@page import="Servlet.MongoDB_Sample"%>
<%@page import="java.util.List"%>
<%@page import="com.google.android.gcm.server.MulticastResult"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.google.android.gcm.server.Message"%>
<%@page import="com.google.android.gcm.server.Sender"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.google.android.gcm.server.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Connection conn = null;
	PreparedStatement pstmt = null;
	Statement stmt = null; 
	String sql = null;
	ResultSet rs = null;
	  
    ArrayList<String> token = new ArrayList<String>();    //token값을 ArrayList에 저장
    String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    //메시지 고유 ID
    boolean SHOW_ON_IDLE = false;    //옙 활성화 상태일때 보여줄것인지
    int LIVE_TIME = 1;    //옙 비활성화 상태일때 FCM가 메시지를 유효화하는 시간
    int RETRY = 2;    //메시지 전송실패시 재시도 횟수
    String simpleApiKey = "AAAAYztpOJk:APA91bEGv_6PYWPGHUd6xXnUC6BueevtaOpj4TbYyhv--oodytsOcwR-5M6DZqNHXeKc0OTZScIQR5Pd-c_d7o1oehDJ3qsrgEnO66jlYM53V83a5bjNg4Bj3O7S7kLljJGmFicZ-eUC";
    String gcmURL = "https://android.googleapis.com/fcm/send";  
    
    String msg = request.getParameter("message");;
    
    if(msg==null || msg.equals("")){
        msg="";
    }
    
	String QUESTION_USER_ID = null;
	String QUESTION_CONTENS = null;
	String QUESTION_AREA = null;
	String QUESTION_SUBJECT = null;

	request.setCharacterEncoding("utf-8");

	try {
		String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
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



	// 토큰값 전달시 쿼리문 입력할곳임
	sql = "INSERT INTO [TRIPTALK_WATING](QUESTION_USER_ID,QUESTION_CONTENS,QUESTION_AREA,QUESTION_SUBJECT) VALUES(?,?,?,?)";
	pstmt = conn.prepareStatement(sql);

	pstmt.setString(1, QUESTION_USER_ID);
	pstmt.setString(2, QUESTION_CONTENS);
	pstmt.setString(3, QUESTION_AREA);
	pstmt.setString(4, QUESTION_SUBJECT);


	pstmt.executeUpdate();//쿼리를 실행 하라는 명령어 
	

	
	
	
  	sql = "select [USER_AGE], [USER_SEX], [USER_FUN], [TENDENCY1], [TENDENCY2], [TENDENCY3] from TRIPTALK_USER WHERE USER_ID = '{0}'";
   	sql = sql.replace("{0}", QUESTION_USER_ID);
		
 	stmt = conn.createStatement(); 
   	rs = stmt.executeQuery(sql);
	
	
    while(rs.next()){ 	
    	MongoDB_Insert.FingerInsert(rs.getInt("USER_AGE"),rs.getString("USER_SEX"),rs.getString("USER_FUN"),rs.getString("TENDENCY1"),rs.getString("TENDENCY2"),rs.getString("TENDENCY3"), QUESTION_AREA);
    }
	
		 
	
 	sql = "select [USER_ID], [USER_TOKEN] from TRIPTALK_USER WHERE USER_ADDRESS = '{0}'";
   	sql = sql.replace("{0}", QUESTION_AREA);
		
 	stmt = conn.createStatement(); 
   	rs = stmt.executeQuery(sql);
     
   	msg = "질문이 왔습니다!!\n질문주제 :" + QUESTION_SUBJECT + "\n질문내용 : " + QUESTION_CONTENS;
   	
    //모든 등록ID를 리스트로 묶음
    while(rs.next()){
        token.add(rs.getString("USER_TOKEN"));
    }
    conn.close();
    
    if(token.size() == 0)
    	return;
    
    Sender sender = new Sender(simpleApiKey);
    Message message = new Message.Builder()
    .collapseKey(MESSAGE_ID)
    .delayWhileIdle(SHOW_ON_IDLE)
    .timeToLive(LIVE_TIME)
    .addData("message",msg)
    .build();
    MulticastResult result1 = sender.send(message,token,RETRY);
    if (result1 != null) {
        List<Result> resultList = result1.getResults();
        for (Result result : resultList) {
            System.out.println(result.getErrorCodeName()); 
        }
    }
	
%>