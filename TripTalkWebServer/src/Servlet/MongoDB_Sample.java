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
		// Document�� Row�� ����.
		// "empno" �÷����̴�.
		// 666        ���̴�.
		// �߰��� �÷��� ���� �ִٸ� append�Լ��� �ڿ� �ٿ� ����ϸ�ȴ�.
		// MongoDB�� Table ��Ű���� ���� ������ 
		// �÷����� append("���ο��÷�", ��)�̿� ���� ���� �߰��Ͽ��� Insert�ȴ�.
		// ���ڿ��� �÷��� ���ڿ� �����͸� �־ �ȴ�. �÷� ������ Mixed�� �ڵ���ȯ�� �ȴ�.

//		coll.insertOne(doc);
		//Ư���� ������� �� �������� �ش� Document Insert�� �ȴ�.
		
		
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
