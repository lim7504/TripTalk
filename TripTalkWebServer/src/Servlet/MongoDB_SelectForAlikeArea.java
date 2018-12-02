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

public class MongoDB_SelectForAlikeArea
{	
	public MongoDB_SelectForAlikeArea()
	{		
	}
	
	public String Select(String user_id)
	{
		Data data = new Data(user_id);
		return data.GetList();
	}
	
	public class Model implements Comparable<Model> 
	{
		private String question_area;
		private String question_area_detail;	
		
		private Integer count = 1;
		
		public Model(String question_area, String question_area_detail)
		{
			this.question_area = question_area;		
			this.question_area_detail = question_area_detail;
		}
		
		public String GetQuestionArea()
		{
			return this.question_area;
		}
		
		public String GetQuestionAreaDetail()
		{
			return this.question_area_detail;
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
		List<Document> resultAreaDetailTotal;	
		List<Document> tendencyCollectionOfTemp;
		
		String userTendency1 = "";
		String userTendency2 = "";
		String userTendency3 = "";
		
		String areaTendency1 = "";
		String areaTendency2 = "";
		String areaTendency3 = "";
		
		String str = "";
		List<Model> listModel = new ArrayList<Model>();
		String user_id;
		String lastQuestionArea = "";
		String lastQuestionAreaDetail = "";
		String resultArea = "";
		String resultAreaDetail = "";
		
		
		public Data(String user_id)
		{
			this.user_id = user_id;
			
			this.MSSQLConnecting();
			this.MongoDBConnecting();
			this.SearchMajorTendercy();
			this.SearchAlikeArea();
			this.SearchAlikeAreaAroundDetail();
			this.SetlistModelParsingNDistinct();
			this.SetListModelSort();
		}
		
		private void MSSQLConnecting()
		{
			
			try {
				url = "jdbc:sqlserver://lim7504.iptime.org:1433;databaseName=TEST_DB;user=guest;password=1234;";
				conn = DriverManager.getConnection(url);
				stmt = conn.createStatement(); 
				sql = "SELECT [LAST_QUOTAION_AREA],[LAST_QUOTAION_AREA_DETAIL], [TENDENCY1], [TENDENCY2], [TENDENCY3] FROM [TRIPTALK_USER] WHERE [USER_ID] = '{0}'";
				sql = sql.replace("{0}", this.user_id.toString());
				
				rs = stmt.executeQuery(sql);
				
				if(rs.next())
				{	    	  	
					this.lastQuestionArea = rs.getString("LAST_QUOTAION_AREA");
					this.lastQuestionAreaDetail = rs.getString("LAST_QUOTAION_AREA_DETAIL");
					this.userTendency1 = rs.getString("TENDENCY1");
					this.userTendency2 = rs.getString("TENDENCY2");
					this.userTendency3 = rs.getString("TENDENCY3");
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
					new Document("$match", new Document("LOG_CODE", "AREA")
												.append("QUESTION_AREA", this.lastQuestionArea)
												.append("QUESTION_AREA_DETAIL", this.lastQuestionAreaDetail)
												.append("TENDENCY1", new Document("$ne","보통")))
					,     	        
	                new Document("$group",new Document("_id", "$TENDENCY1")
	                						   .append("COUNT", new Document("$sum", 1)))
	                ,
	                new Document("$sort", new Document("COUNT", -1))
	                ,
	                new Document("$limit", 1)
	                )).into(new ArrayList<Document>());;
	                
	        Document tendencyDocument1 = tendencyCollectionOfTemp.get(0);
	                
	        this.areaTendency1 = tendencyDocument1.getString("_id");
	        
	        
	        
	        
			//tendercy2
			this.tendencyCollectionOfTemp = (List<Document>)collFinger.aggregate(Arrays.asList(
					new Document("$match", new Document("LOG_CODE", "AREA")
												.append("QUESTION_AREA", this.lastQuestionArea)
												.append("QUESTION_AREA_DETAIL", this.lastQuestionAreaDetail)
												.append("TENDENCY2", new Document("$ne","보통")))
					,  	        
	                new Document("$group",new Document("_id", "$TENDENCY2")
	                						   .append("COUNT", new Document("$sum", 1)))
	                ,
	                new Document("$sort", new Document("COUNT", -1))
	                ,
	                new Document("$limit", 1)
	                )).into(new ArrayList<Document>());;
	                
	        Document tendencyDocument2 = tendencyCollectionOfTemp.get(0);
	                
	        this.areaTendency2 = tendencyDocument2.getString("_id");
	        
	        
	        
	        
			//tendercy3
			this.tendencyCollectionOfTemp = (List<Document>)collFinger.aggregate(Arrays.asList(
					new Document("$match", new Document("LOG_CODE", "AREA")
												.append("QUESTION_AREA", this.lastQuestionArea)
												.append("QUESTION_AREA_DETAIL", this.lastQuestionAreaDetail)
												.append("TENDENCY3", new Document("$ne","보통")))
					,  	        
	                new Document("$group",new Document("_id", "$TENDENCY3")
	                						   .append("COUNT", new Document("$sum", 1)))
	                ,
	                new Document("$sort", new Document("COUNT", -1))
	                ,
	                new Document("$limit", 1)
	                )).into(new ArrayList<Document>());;
	                
	        Document tendencyDocument3 = tendencyCollectionOfTemp.get(0);
	                
	        this.areaTendency3 = tendencyDocument3.getString("_id");
		}
		
			
		public void SearchAlikeArea()
		{
			
  
            if(this.userTendency1 == "보통"
    	        	&& this.userTendency2 == "보통"
    	        	&& this.userTendency3 == "보통")
                {               
    				this.fingerTotal = (List<Document>)collFinger.aggregate(Arrays.asList(
    						new Document("$match", new Document("LOG_CODE", "AREA").append("QUESTION_AREA", new Document("$ne",this.lastQuestionArea))),     	        
    		                new Document("$group",new Document("_id",new Document("QUESTION_AREA","$QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$QUESTION_AREA_DETAIL")).append("COUNT", new Document("$sum", 1))),
    		                new Document("$project", new Document("QUESTION_AREA", "$_id.QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$_id.QUESTION_AREA_DETAIL").append("COUNT", "$COUNT").append("_id", 0)),
    		                new Document("$sort", new Document("COUNT", -1)),
    		                new Document("$limit", 1)
    		                )).into(new ArrayList<Document>());;   	                
                }       
	                
            else if(this.userTendency1 != "보통"
    	        	&& this.userTendency2 == "보통"
    	        	&& this.userTendency3 == "보통")
                {               
    				this.fingerTotal = (List<Document>)collFinger.aggregate(Arrays.asList(
    						new Document("$match", new Document("LOG_CODE", "AREA").append("TENDENCY1", this.areaTendency1).append("QUESTION_AREA", new Document("$ne",this.lastQuestionArea))),     	        
    		                new Document("$group",new Document("_id",new Document("QUESTION_AREA","$QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$QUESTION_AREA_DETAIL")).append("COUNT", new Document("$sum", 1))),
    		                new Document("$project", new Document("QUESTION_AREA", "$_id.QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$_id.QUESTION_AREA_DETAIL").append("COUNT", "$COUNT").append("_id", 0)),
    		                new Document("$sort", new Document("COUNT", -1)),
    		                new Document("$limit", 1)
    		                )).into(new ArrayList<Document>());;   	                
                }
            
            else if(this.userTendency1 == "보통"
    	        	&& this.userTendency2 != "보통"
    	        	&& this.userTendency3 == "보통")
                {               
    				this.fingerTotal = (List<Document>)collFinger.aggregate(Arrays.asList(
    						new Document("$match", new Document("LOG_CODE", "AREA").append("TENDENCY2", this.areaTendency2).append("QUESTION_AREA", new Document("$ne",this.lastQuestionArea))),     	        
    		                new Document("$group",new Document("_id",new Document("QUESTION_AREA","$QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$QUESTION_AREA_DETAIL")).append("COUNT", new Document("$sum", 1))),
    		                new Document("$project", new Document("QUESTION_AREA", "$_id.QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$_id.QUESTION_AREA_DETAIL").append("COUNT", "$COUNT").append("_id", 0)),
    		                new Document("$sort", new Document("COUNT", -1)),
    		                new Document("$limit", 1)
    		                )).into(new ArrayList<Document>());;   	                
                }
	                
            else if(this.userTendency1 == "보통"
    	        	&& this.userTendency2 == "보통"
    	        	&& this.userTendency3 != "보통")
                {               
    				this.fingerTotal = (List<Document>)collFinger.aggregate(Arrays.asList(
    						new Document("$match", new Document("LOG_CODE", "AREA").append("TENDENCY3", this.areaTendency3).append("QUESTION_AREA", new Document("$ne",this.lastQuestionArea))),     	        
    		                new Document("$group",new Document("_id",new Document("QUESTION_AREA","$QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$QUESTION_AREA_DETAIL")).append("COUNT", new Document("$sum", 1))),
    		                new Document("$project", new Document("QUESTION_AREA", "$_id.QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$_id.QUESTION_AREA_DETAIL").append("COUNT", "$COUNT").append("_id", 0)),
    		                new Document("$sort", new Document("COUNT", -1)),
    		                new Document("$limit", 1)
    		                )).into(new ArrayList<Document>());;   	                
                }
            else if(this.userTendency1 != "보통"
    	        	&& this.userTendency2 != "보통"
    	        	&& this.userTendency3 == "보통")
                {               
    				this.fingerTotal = (List<Document>)collFinger.aggregate(Arrays.asList(
    						new Document("$match", new Document("LOG_CODE", "AREA").append("TENDENCY1", this.areaTendency1).append("TENDENCY2", this.areaTendency2).append("QUESTION_AREA", new Document("$ne",this.lastQuestionArea))),     	        
    		                new Document("$group",new Document("_id",new Document("QUESTION_AREA","$QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$QUESTION_AREA_DETAIL")).append("COUNT", new Document("$sum", 1))),
    		                new Document("$project", new Document("QUESTION_AREA", "$_id.QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$_id.QUESTION_AREA_DETAIL").append("COUNT", "$COUNT").append("_id", 0)),
    		                new Document("$sort", new Document("COUNT", -1)),
    		                new Document("$limit", 1)
    		                )).into(new ArrayList<Document>());;   	                
                }
            else if(this.userTendency1 != "보통"
    	        	&& this.userTendency2 == "보통"
    	        	&& this.userTendency3 != "보통")
                {               
    				this.fingerTotal = (List<Document>)collFinger.aggregate(Arrays.asList(
    						new Document("$match", new Document("LOG_CODE", "AREA").append("TENDENCY1", this.areaTendency1).append("TENDENCY3", this.areaTendency3).append("QUESTION_AREA", new Document("$ne",this.lastQuestionArea))),     	        
    		                new Document("$group",new Document("_id",new Document("QUESTION_AREA","$QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$QUESTION_AREA_DETAIL")).append("COUNT", new Document("$sum", 1))),
    		                new Document("$project", new Document("QUESTION_AREA", "$_id.QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$_id.QUESTION_AREA_DETAIL").append("COUNT", "$COUNT").append("_id", 0)),
    		                new Document("$sort", new Document("COUNT", -1)),
    		                new Document("$limit", 1)
    		                )).into(new ArrayList<Document>());;   	                
                }
            else if(this.userTendency1 == "보통"
    	        	&& this.userTendency2 != "보통"
    	        	&& this.userTendency3 != "보통")
                {               
    				this.fingerTotal = (List<Document>)collFinger.aggregate(Arrays.asList(
    						new Document("$match", new Document("LOG_CODE", "AREA").append("TENDENCY2", this.areaTendency2).append("TENDENCY3", this.areaTendency3).append("QUESTION_AREA", new Document("$ne",this.lastQuestionArea))),     	        
    		                new Document("$group",new Document("_id",new Document("QUESTION_AREA","$QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$QUESTION_AREA_DETAIL")).append("COUNT", new Document("$sum", 1))),
    		                new Document("$project", new Document("QUESTION_AREA", "$_id.QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$_id.QUESTION_AREA_DETAIL").append("COUNT", "$COUNT").append("_id", 0)),
    		                new Document("$sort", new Document("COUNT", -1)),
    		                new Document("$limit", 1)
    		                )).into(new ArrayList<Document>());;   	                
                }
            else if(this.userTendency1 != "보통"
    	        	&& this.userTendency2 != "보통"
    	        	&& this.userTendency3 != "보통")
                {               
    				this.fingerTotal = (List<Document>)collFinger.aggregate(Arrays.asList(
    						new Document("$match", new Document("LOG_CODE", "AREA").append("TENDENCY1", this.areaTendency1).append("TENDENCY2", this.areaTendency2).append("TENDENCY3", this.areaTendency3).append("QUESTION_AREA", new Document("$ne",this.lastQuestionArea))),     	        
    		                new Document("$group",new Document("_id",new Document("QUESTION_AREA","$QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$QUESTION_AREA_DETAIL")).append("COUNT", new Document("$sum", 1))),
    		                new Document("$project", new Document("QUESTION_AREA", "$_id.QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$_id.QUESTION_AREA_DETAIL").append("COUNT", "$COUNT").append("_id", 0)),
    		                new Document("$sort", new Document("COUNT", -1)),
    		                new Document("$limit", 1)
    		                )).into(new ArrayList<Document>());;   	                
                }

	                
	                
            this.resultArea = fingerTotal.get(0).getString("QUESTION_AREA");
            this.resultAreaDetail = fingerTotal.get(0).getString("QUESTION_AREA_DETAIL");
	                
		}
		
		public void SearchAlikeAreaAroundDetail()
		{
			
			this.resultAreaDetailTotal = (List<Document>)collFinger.aggregate(Arrays.asList(
					new Document("$match", new Document("LOG_CODE", "AREA").append("QUESTION_AREA", this.resultArea).append("QUESTION_AREA_DETAIL", new Document("$ne",this.resultAreaDetail))),     	        
	                new Document("$group",new Document("_id",new Document("QUESTION_AREA","$QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$QUESTION_AREA_DETAIL")).append("COUNT", new Document("$sum", 1))),
	                new Document("$project", new Document("QUESTION_AREA", "$_id.QUESTION_AREA").append("QUESTION_AREA_DETAIL", "$_id.QUESTION_AREA_DETAIL").append("COUNT", "$COUNT").append("_id", 0)),
	                new Document("$sort", new Document("COUNT", -1))
	                )).into(new ArrayList<Document>());;
	              
		}
		
		
		private void SetlistModelParsingNDistinct()
		{
		    for (Document detailDocument : resultAreaDetailTotal) 
		    {   
		    	System.out.println(detailDocument.toJson());
		    	boolean existFlag = false;
				String question_area = detailDocument.getString("QUESTION_AREA");
				String question_area_detail = detailDocument.getString("QUESTION_AREA_DETAIL");
		    	
				for(Model model : listModel)
				{		
					
					if(model != null 
							&& model.GetQuestionArea().equals(question_area) == true
							&& model.GetQuestionArea().equals(question_area_detail) == true)
					{						
						model.SetCount(model.GetCount() + 1 );
						existFlag = true;
					}										
				}				
				
				if(existFlag == false)
				{
					listModel.add(new Model(question_area, question_area_detail));						
				}
				
		    }		    
		}
		
		
		public void SetListModelSort()
		{
			Collections.sort(listModel);			
		}
		
		
		public String GetList()
		{
		
			String str = "최근 질문한 \"" + this.lastQuestionArea + " " + this.lastQuestionAreaDetail + "\" 지역과";
				   str += "\n유사한 지역는"; 
				   str += "\n\n\"" + this.resultArea +  " " + this.resultAreaDetail + "\" 입니다.";
				   
				   str += "\n\n\n\"" + this.resultArea + " " + this.resultAreaDetail + "\" 주위 관광지로";
				   str += "\n";
				   
				   for (Model model : listModel) {
			        	
					   int index = listModel.indexOf(model) + 1;
//					   str += "\n    " +  index  + ". " + model.GetQuestionAreaDetail();
					   str += "\n   [" + model.GetQuestionAreaDetail() + "]";
			        }
				   
				   str += "\n\n가 있습니다.";
			
	        return str;	
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

