<%@page import="java.io.PrintWriter"%>
<%@page import="java.sql.DriverManager"%>
<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%

/*
FCM을 통해 전달 받은 메세지 아이디를 통해 DB로부터 채팅 메세세지를 가져오는 JSP

FCM틍 통해 전달 받은 채팅 메세지 아이디를 이용하여 메세지 테이블에서 검색하여 해당 내용을 전송
추후 ROOM ID는 DB에서 검색을 빠르게 하기 위해 추가(현재는 미사용)
*/
   	request.setCharacterEncoding("UTF-8");

	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;

	String[] chooseProtocol = {"USER_ID","CHOOSE_ID","QUOSTION_TYPE","QUOTATION_CONTENS","CREATE_DATE"};
	
	String ROOM_ID = null;
	String USER_ID = null;
	String CHOOSE_ID = null;
	String QUOSTION_TYPE = null;
	String QUOTATION_CONTENS = null;
	String CREATE_DATE = null;
	String REQUEST_USER_ID = null;
	
	Statement stmt = null;
	ResultSet rs = null;
	
	USER_ID = request.getParameter("USER_ID");
	CHOOSE_ID = request.getParameter("CHOOSE_ID"); 
	QUOSTION_TYPE = request.getParameter("QUOSTION_TYPE");
	QUOTATION_CONTENS = request.getParameter("QUOTATION_CONTENS");
	CREATE_DATE = request.getParameter("CREATE_DATE");
	
 	if(USER_ID == null || USER_ID.isEmpty()|| USER_ID.equals(""))
 	{
 		return ;
 	}else
 	{
		try {
			String url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
			
			conn = DriverManager.getConnection(url);
			
		} catch (Exception e) {
			out.println("Can not Conection MSSQL");
			e.printStackTrace();
		}

		if(conn != null)
		{
			stmt = conn.createStatement(); 
	
			//String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    //메시지 고유 ID
			String sql = "SELECT * FROM [TRIPTALK_ROOM] WHERE REQUEST_USER_ID = '{0}'";
			sql = sql.replace("{0}", USER_ID);

		    
		    rs = stmt.executeQuery(sql);
		    
			while(rs.next())
			{
				ROOM_ID = rs.getString("ROOM_ID");
				REQUEST_USER_ID = rs.getString("REQUEST_USER_ID");
			}
		
		
			if(ROOM_ID == null || ROOM_ID.isEmpty() || ROOM_ID.equals(""))
			{
				System.out.println("Can not found Room ID");
				out.println("NACK");
				return ;
			}
			
			
			if(REQUEST_USER_ID == null || !USER_ID.contains(REQUEST_USER_ID))
			{
				System.out.println("diff USER_ID:"+USER_ID + "REQUEST_USER_ID:"+REQUEST_USER_ID);
				out.println("WNACK");
				return ;				
			}
			stmt.clearBatch();
		
			CHOOSE_ID = String.valueOf(Math.random() % 100 + 1);  
			
			// 토큰값 전달시 쿼리문 입력할곳임
			sql = "INSERT INTO [TRIPTALK_CHOOSE](ROOM_ID,CHOOSE_ID,QUOSTION_TYPE,QUOTATION_CONTENS,CREATE_DATE) VALUES(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
		
			/* 디버깅용
			System.out.println(ROOM_ID);
			System.out.println(CHOOSE_ID);
			System.out.println(QUOSTION_TYPE);
			System.out.println(QUOTATION_CONTENS);
			System.out.println(CREATE_DATE);
			*/
			pstmt.setString(1, ROOM_ID);
			pstmt.setString(2, CHOOSE_ID);
			pstmt.setString(3, QUOSTION_TYPE);
			pstmt.setString(4, QUOTATION_CONTENS);
			pstmt.setString(5, CREATE_DATE);
			
			pstmt.executeUpdate();//쿼리를 실행 하라는 명령어 

			out.println("ACK");
	 	}else
	 	{

			System.out.println("Please Check....Connection is NULL for ChoosetoDB");
			out.println("NACK");
	 	}
	}

%>