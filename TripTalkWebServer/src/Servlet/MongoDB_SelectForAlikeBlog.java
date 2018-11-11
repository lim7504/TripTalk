package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Aggregates;

import static com.mongodb.client.model.Filters.eq;

public class MongoDB_SelectForAlikeBlog
{	
	public MongoDB_SelectForAlikeBlog()
	{		
	}
	
	public String Select(String user_id)
	{
		Data data = new Data(user_id);
		return data.GetList();
	}
	
	public class Model implements Comparable<Model> 
	{
		private String blog_id;	
		
		private Integer count = 1;
		
		public Model(String blog_id)
		{
			this.blog_id = blog_id;			
		}
		
		public String GetBlogId()
		{
			return this.blog_id;
		}
				
		public Integer GetCount()
		{
			return this.count;
		}
		
		public void SetCount(Integer count)
		{
			this.count  = count;
		}
		
	    @Override
	    public int compareTo(Model s) {
	        if (this.count > s.GetCount()) {
	            return -1;
	        } else if (this.count < s.GetCount()) {
	            return 1;
	        }
	        return 0;
	    }

	}
	
	
	
	public class Data
	{
		//MongoDB Connecting
		MongoClient mongo;
		MongoDatabase db;
		MongoCollection<Document> collFinger;
		
		//MSSQL Conneting
		Statement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		String url = "";
		String sql = "";
		
		//ETC
		List<Document> fingerTotal;	
		List<Document> tendencyCollectionOfTemp;
		
		String tendency1 = "";
		String tendency2 = "";
		String tendency3 = "";
		
		String str = "";
		List<Model> listModel = new ArrayList<Model>();
		String user_id = "";
		String blog_id = "";

		public Data(String user_id)
		{
			this.user_id = user_id;
			
			this.MSSQLConnecting();
			this.MongoDBConnecting();
			this.SearchMajorTendercy();
			this.SearchAlikeArea();
			this.SetlistModelParsingNDistinct();
			this.SetListModelSort();
		}
		
		private void MSSQLConnecting()
		{
			
			try {
				url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
				conn = DriverManager.getConnection(url);
				stmt = conn.createStatement(); 
				sql = "SELECT [LAST_BLOG_ID] FROM [TRIPTALK_USER] WHERE [USER_ID] = '{0}'";
				//sql = "SELECT TOP 1 [QUESTION_AREA], [QUESTION_AREA_DETAIL]  FROM [TRIPTALK_WATING] WHERE [QUESTION_USER_ID] = '{0}' ORDER BY [CREATE_DATE] DESC";
				sql = sql.replace("{0}", this.user_id.toString());
				
				rs = stmt.executeQuery(sql);
				
				if(rs.next())
				{	    	  	
					this.blog_id = rs.getString("LAST_BLOG_ID");
					//this.QuestionAreaDetail = rs.getString("QUESTION_AREA_DETAIL");
				}
		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		private void MongoDBConnecting() 
		{
			this.mongo = new MongoClient("lim7504.iptime.org", 27017);
			this.db =  mongo.getDatabase("TRIPTALK");	
			this.collFinger = db.getCollection("FINGER");   
		}
		
		public void SearchMajorTendercy()
		{
			
			//tendercy1
			this.tendencyCollectionOfTemp = (List<Document>)collFinger.aggregate(Arrays.asList(
					new Document("$match", new Document("LOG_CODE", "BLOG").append("BLOG_ID", this.blog_id)),  	        
	                new Document("$group",new Document("_id", "$TENDENCY1").append("COUNT", new Document("$sum", 1))),
	                new Document("$sort", new Document("COUNT", -1)),
	                new Document("$limit", 1)
	                )).into(new ArrayList<Document>());;
	                
	        Document tendencyDocument1 = tendencyCollectionOfTemp.get(0);
	                
	        this.tendency1 = tendencyDocument1.getString("_id");
	        
	        
			//tendercy2
			this.tendencyCollectionOfTemp = (List<Document>)collFinger.aggregate(Arrays.asList(
					new Document("$match", new Document("LOG_CODE", "BLOG").append("BLOG_ID", this.blog_id)),  	         
	                new Document("$group",new Document("_id", "$TENDENCY2").append("COUNT", new Document("$sum", 1))),
	                new Document("$sort", new Document("COUNT", -1)),
	                new Document("$limit", 1)
	                )).into(new ArrayList<Document>());;
	                
	        Document tendencyDocument2 = tendencyCollectionOfTemp.get(0);
	                
	        this.tendency2 = tendencyDocument2.getString("_id");
	        
	        
			//tendercy3
			this.tendencyCollectionOfTemp = (List<Document>)collFinger.aggregate(Arrays.asList(
					new Document("$match", new Document("LOG_CODE", "BLOG").append("BLOG_ID", this.blog_id)),  	 	        
	                new Document("$group",new Document("_id", "$TENDENCY3").append("COUNT", new Document("$sum", 1))),
	                new Document("$sort", new Document("COUNT", -1)),
	                new Document("$limit", 1)
	                )).into(new ArrayList<Document>());;
	                
	        Document tendencyDocument3 = tendencyCollectionOfTemp.get(0);
	                
	        this.tendency3 = tendencyDocument3.getString("_id");
		}
		
			
		public void SearchAlikeArea()
		{
			
			this.fingerTotal = (List<Document>)collFinger.aggregate(Arrays.asList(
					new Document("$match", new Document("LOG_CODE", "BLOG").append("TENDENCY1", this.tendency1).append("TENDENCY2", this.tendency2).append("TENDENCY3", this.tendency3).append("BLOG_ID", new Document("$ne",this.blog_id))),     	        
	                new Document("$group",new Document("_id",new Document("BLOG_ID","$BLOG_ID")).append("COUNT", new Document("$sum", 1))),
	                new Document("$project", new Document("BLOG_ID", "$_id.BLOG_ID").append("COUNT", "$COUNT").append("_id", 0)),
	                new Document("$sort", new Document("COUNT", -1)),
	                new Document("$limit", 1)
	                )).into(new ArrayList<Document>());;
	              
		}
		
		
		private void SetlistModelParsingNDistinct()
		{
		    for (Document fingerDocument : fingerTotal) 
		    {   
		    	System.out.println(fingerDocument.toJson());
		    	boolean existFlag = false;
				String blog_id = fingerDocument.getString("BLOG_ID");
		    	
				for(Model model : listModel)
				{		
					
					if(model != null 
							&& model.GetBlogId().equals(blog_id) == true)
					{						
						model.SetCount(model.GetCount() + 1 );
						existFlag = true;
					}										
				}				
				
				if(existFlag == false)
				{
					listModel.add(new Model(blog_id));						
				}
				
		    }		    
		}
		
		
		public void SetListModelSort()
		{
			Collections.sort(listModel);			
		}
		
		public String GetList()
		{
//			String str = "[";
//			
//	        for (Model model : listModel) {
//	        	
//	        	if(str != "[")
//	        		str += ",";
//	        	
//	        	str += "{\"BLOG_ID\" : \"" + model.GetBlogId() + "\""
//	            		+ ", \"COUNT\" : " + model.GetCount().toString() + "}";
//	        }
//	        
//	        str += "]";
	        return listModel.get(0).GetBlogId();	
		}
		
		public void MongoDBClose()
		{
			mongo.close();			
		}
	}
	
	
	
}



//db.myCollection.aggregate(
//	    {$unwind : "$views"},
//	    {$match : {"views.isActive" : true}},
//	    {$sort : {"views.date" : 1}},
//	    {$limit : 200},
//	    {$project : {"_id" : 0, "url" : "$views.url", "date" : "$views.date"}}
//	)


//this.fingerTotal = (List<Document>)collFinger.aggregate(Arrays.asList(
//        new Document("$match", new Document("QUESTION_AREA", this.QuestionArea)),
//        new Document("$sort", new Document("TENDERCY1", -1)),
//        new Document("$limit", 1),
//       new Document("$project", new Document("_id", 0)
//                    				.append("url", "$TENDERCY1")
//               					.append("date", "$views.date"))
//        )).into(new ArrayList<Document>());

