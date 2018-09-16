package Servlet;

import org.bson.Document;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class InsertItem {

	public void Insert(MongoCollection<Document> coll, Document doc)
	{
		
//		MongoClient mongo = new MongoClient("lim7504.iptime.org", 27017);
//		MongoDatabase db =  mongo.getDatabase("test");	
//		MongoCollection<Document> coll = db.getCollection("emp");
//
//		
//		Document doc = new Document("empno", 666);
//		// Document는 Row와 같다.
//		// "empno" 컬럼명이다.
//		// 666        값이다.
//		// 추가할 컬럼과 값이 있다면 append함수를 뒤에 붙여 사용하면된다.
//		// MongoDB는 Table 스키마가 없기 때문에 
//		// 컬럼명을 append("새로운컬럼", 값)이와 같이 새로 추가하여도 Insert된다.
//		// 숫자였던 컬럼에 문자열 데이터를 넣어도 된다. 컬럼 형식은 Mixed로 자동변환이 된다.
//
		coll.insertOne(doc);
//
//		//특별한 문장없이 위 문장으로 해당 Document Insert가 된다.
//		
//		mongo.close();
	}
	
	
}
