<%@page import="java.net.URLDecoder"%>
<%@page import="java.io.UTFDataFormatException"%>
<%@page import="org.omg.PortableInterceptor.USER_EXCEPTION"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.google.android.gcm.server.*"%>

<%@ page language="java" contentType="text/html;  charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>

<%

/*
	ChatMsgtoDB Jsp로부터 실행이 되는 jsp 
	fcm 전용으로 사용하기 위해 따로 jsp 파일을 생성함
	
	ChatMsgtoDB.jsp로부터 호출을 받을 때 채팅 메세지 아이디/ 채팅 메세지/ 타겟 유저를 함께 전달 해준다.
	타켓 유저 정보를 통해 유저 테이블로부터 해당 유정의 토큰 값을 가져 올 수가 있다.
	채팅 메세지는 카카오톡 처럼 새로운 메세지가 오면 그 메세지를 보여주기 위함. 
	fcm의 title키는  신규 메세지를 보여주는 용도로 사용하고 있으며 실제 안드로이드에서  fcm을 통해 화면에 보여주는 타이틀은 TripTal으로 보여줌 ( 강제) 
	fcm 알림 메뉴에 채팅 메세지 아이디 값은 보여주지 않음.
	
	가져온 토큰 값을 fcm을 통해 메세지 아이디 + 신규 채팅 메세지 내용을 전송함.
	"ACK"의 의미는 채팅 메세지를 보낸 사람에게 정상적으로 수신하는 유저에게 보내졌다고 알림과 동시에 본인 쓴 채팅을 채팅방에 보여주기 위함.
	
*/
	boolean SHOW_ON_IDLE = false;    //옙 활성화 상태일때 보여줄것인지
	int LIVE_TIME = 1;    //옙 비활성화 상태일때 FCM가 메시지를 유효화하는 시간
	int RETRY = 2;    //메시지 전송실패시 재시도 횟수
	Statement stmt = null;
	ResultSet rs = null;
	
	try{
		Connection conn = null;
		
		//simpleapikey 반드시 본인의 firebase key를 입력해야함.
		//String simpleApiKey = "AAAAX4kf_bU:APA91bHgjt4oHe643xsm-45lACsuYR9N7LAs-FfN9CYKqEUU8JwsLVX7cr-lvWfCrY8E9HV4JTHcKIljcBymSAhV4ygbPurLsn-K4k_JJKRMtu9hEH-nQmZy3FLQCaqu-2KXPGz6USCn";
		String simpleApiKey = "AIzaSyD_iEohE8kjPsPZ_NO-L-czARVuEiT3dFw";
		String gcmURL = "https://android.googleapis.com/fcm/send";  
		
		String MESSAGE_ID = null;
		String TARGET = null;
		String MESSAGE = null;
		String FCM_TYPE = null;
		
		//String token =request.getParameter("token"); 
		ArrayList<String> token = new ArrayList<String>();   
		
		request.setCharacterEncoding("UTF-8");


		FCM_TYPE  = request.getParameter("FCM_TYPE");//새롭게 발생한 채팅 메세지
		MESSAGE_ID = request.getParameter("MESSAGE_ID");//새롭게 발생한 채팅 메세지에 대한 id
		TARGET = request.getParameter("TARGET");//fcm을 받을 대상의 유저 아이디
		MESSAGE = request.getParameter("MESSAGE");
		System.out.println();
		System.out.println(MESSAGE);
		
		MESSAGE = URLEncoder.encode(MESSAGE,"UTF-8");

		if(FCM_TYPE == null || TARGET == null || MESSAGE_ID == null)
		{
			return ;
		}
		
		try {
			    String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
				//String url = "jdbc:sqlserver://localhost:1433;databaseName=TEST_DB;user=sa;password=1;";
			
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			out.println("MSSQL Fail");
			e.printStackTrace();
		}
		
		if(conn != null)
		{
			if(FCM_TYPE.contains("CHAT_MESSAGE"))
			{
				stmt = conn.createStatement(); 
			
				String sql = "SELECT * FROM [TRIPTALK_USER] WHERE USER_ID = '{0}' ";
				sql = sql.replace("{0}", TARGET);
	
				rs = stmt.executeQuery(sql);

		
				if(rs.next())
				{
					String USER_TOKEN = rs.getString("USER_TOKEN");
					token.add(USER_TOKEN);
				}
			
				System.out.println(TARGET);
				System.out.println(token);
				System.out.println(MESSAGE_ID);
			}else if(FCM_TYPE.contains("QUESTION_MESSAGE"))
			{
				token.add(TARGET);
				
			}


			Sender sender = new Sender(simpleApiKey);
			
			Message message = new Message.Builder().collapseKey(MESSAGE_ID).delayWhileIdle(SHOW_ON_IDLE).timeToLive(LIVE_TIME).addData("message",MESSAGE).addData("title", MESSAGE_ID).build();
	
			MulticastResult result1 = sender.send(message,token,RETRY);
			
			if (result1 != null) {
			
			    List<Result> resultList = result1.getResults();
			
			    for (Result result : resultList) {
			
			        System.out.println(result.getErrorCodeName()); 
			
			    }
			
			}
			System.out.println(sender.toString()); 
			
			out.println("ACK");
			
			MESSAGE = URLDecoder.decode(MESSAGE,"UTF-8");
			System.out.println(MESSAGE);
			
		}else
		{
			out.println("FCMManager Error");
		}
		
	}catch (Exception e) {
	
	e.printStackTrace();
	
	}
%>