package Servlet;

import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDB_Sample extends HttpServlet{
	

	public MongoDB_Sample()
	{
		
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
//		out.println(234*324);
		
		
		MongoClient mongo = new MongoClient("localhost", 27017);
		
		MongoDatabase db =  mongo.getDatabase("TRIPTALK");	
		MongoCollection<Document> collFinger = db.getCollection("FINGER");


		List<Document> fingerTotal = (List<Document>) collFinger.find().into(new ArrayList<Document>());
		
		

		
//		Document myDoc = collFinger.find(eq("USER_AGE", 11)).first();
		
		
		
		
//		InsertItem a = new InsertItem();
//		
//		List<Document> adresslist = new ArrayList<>();
//		
//		Document adress = new Document("street","seoul").append("nation","korea");
//		Document adress2 = new Document("street","dddd").append("nation","korea2222");
//		adresslist.add(adress);
//		adresslist.add(adress2);
		
//		Document doc = new Document("empno", 666).append("adress",adresslist);
//		a.Insert(coll,doc);
		// Document는 Row와 같다.
		// "empno" 컬럼명이다.
		// 666        값이다.
		// 추가할 컬럼과 값이 있다면 append함수를 뒤에 붙여 사용하면된다.
		// MongoDB는 Table 스키마가 없기 때문에 
		// 컬럼명을 append("새로운컬럼", 값)이와 같이 새로 추가하여도 Insert된다.
		// 숫자였던 컬럼에 문자열 데이터를 넣어도 된다. 컬럼 형식은 Mixed로 자동변환이 된다.

//		coll.insertOne(doc);
		//특별한 문장없이 위 문장으로 해당 Document Insert가 된다.
		
		
//		Bson deleteColumnAndValue = new Document("empno", 999);		
//		coll.deleteMany(deleteColumnAndValue);
		
		
		
//		Document item = (Document)coll.find(new Document("empno",666)).first();
//		Bson changeColumnAndValue = new Document("empno", 999);
//		Bson updateQuory = new Document("$set", changeColumnAndValue);			
//		coll.updateOne(item,updateQuory);
//	
//		
//      try (MongoCursor<Document> cur = coll.find(new Document("empno",666)).iterator()) 
//      {
//          while (cur.hasNext()) 
//          {
//              Document item = cur.next();
//                             
//      		Bson changeDocument = new Document("empno", 999);
//    		Bson updateQuory = new Document("$set", changeDocument);			
//    		coll.updateOne(item,updateQuory);
//
//          }
//      }
//		List<Document> employees = (List<Document>) coll.find().into(
//				new ArrayList<Document>());
// 
//		for (Document employee : employees) {
// 
//			if(employee.containsKey("adress") == false)
//				continue;
//			
//			
//			if((employee.get("adress")).getClass() ==  Document.class)
//			{
//				Document adress= (Document) employee.get("adress");
//				out.println("street = " + adress.get("street"));
//				out.println("nation = " + adress.get("nation"));
//				continue;
//			}
//			
//			
//			List<Document> adressTotal= (List<Document>) employee.get("adress");
//			for (Document adress : adressTotal) {
//				out.println("street = " + adress.get("street"));
//				out.println("nation = " + adress.get("nation"));
//			}
// 
//		}
// 
//		
//		
		
		
		
		
		
//        try (MongoCursor<Document> cur = coll.find().iterator()) 
//        {
//            while (cur.hasNext()) 
//            {
//                Document doc = cur.next();
//                               
//                out.println(doc.toJson());
////                out.println("empno : ");
////                out.println(doc.get("empno"));                
//                
//                out.println("<BR>");
//
//            }
//        }

        mongo.close();
		
		
//		DBCollection collection = (DBCollection) mongo.getDatabase("test").getCollection("emp");
		
//		System.out.println(a.toString());

/*		BasicDBObject doc = new BasicDBObject();
        
        doc.put("id","lim4057@naver.com");
        collection.insert(doc);
    
        mongo.close();*/
        
		
//		
//		MongoCursor<Document> cursor = collection.find();
//		
//		int i =1;
//		
//		while(cursor.hasNext())
//		{
//			out.println("Document #" + i);
//			out.println(cursor.next());
//			i++;
//			
//		}
//
//		mongo.close();
	}
	



}
