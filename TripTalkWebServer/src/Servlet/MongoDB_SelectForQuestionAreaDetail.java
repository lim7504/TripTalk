package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;

public class MongoDB_SelectForQuestionAreaDetail
{	
	public MongoDB_SelectForQuestionAreaDetail()
	{		
	}
	
	public String Select(String question_area)
	{
		Data data = new Data(question_area);
		return data.GetList();
	}
	
	public class Model implements Comparable<Model> 
	{
		private String question_area_detail;	
		
		private Integer count = 1;
		
		public Model(String question_area_detail)
		{
			this.question_area_detail = question_area_detail;			
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
		MongoClient mongo;
		MongoDatabase db;
		MongoCollection<Document> collFinger;
		List<Document> fingerTotal;
		String str = "";
		List<Model> listModel = new ArrayList<Model>();
		
		public Data(String question_area)
		{
			this.MongoDBConnecting(question_area);
			this.SetlistModelParsingNDistinct();
			this.SetListModelSort();
		}
		
		private void MongoDBConnecting(String question_area)
		{
			this.mongo = new MongoClient("lim7504.iptime.org", 27017);
			this.db =  mongo.getDatabase("TRIPTALK");	
			this.collFinger = db.getCollection("FINGER");
			
			//quotationAreaDetail
			this.fingerTotal = (List<Document>)collFinger.aggregate(Arrays.asList(
					new Document("$match", new Document("LOG_CODE", "AREA")
												.append("QUESTION_AREA", question_area))
					,     	        
	                new Document("$group",new Document("_id", "$QUESTION_AREA_DETAIL")
	                						   .append("COUNT", new Document("$sum", 1)))
	                ,
	                
	                new Document("$project", new Document("QUESTION_AREA_DETAIL", "$_id")
	                							  .append("COUNT", "$COUNT")
	                							  .append("_id", 0))
	                ,
	                new Document("$sort", new Document("COUNT", -1))	           
	                )).into(new ArrayList<Document>());;
			
		}
		
		private void SetlistModelParsingNDistinct()
		{
		    for (Document fingerDocument : fingerTotal) 
		    {   
		    	System.out.println(fingerDocument.toJson());
		    	boolean existFlag = false;
				String question_area_detatil = fingerDocument.getString("QUESTION_AREA_DETAIL");
		    	
				for(Model model : listModel)
				{		
					
					if(model != null 
							&& model.GetQuestionAreaDetail().equals(question_area_detatil) == true)
					{						
						model.SetCount(model.GetCount() + 1 );
						existFlag = true;
					}										
				}				
				
				if(existFlag == false)
				{
					listModel.add(new Model(question_area_detatil));						
				}
				
		    }		    
		}
		
		
		public void SetListModelSort()
		{
			Collections.sort(listModel);			
		}
		
		public String GetList()
		{
			String str = "[";
			
	        for (Model model : listModel) {
	        	
	        	if(str != "[")
	        		str += ",";
	        	
	        	str += "{\"QUESTION_AREA_DETAIL\" : \"" + model.GetQuestionAreaDetail() + "\"}";

	        }
	        str += "]";
	        return str;
			
		}
		
		public void MongoDBClose()
		{
			mongo.close();			
		}
	}
	
	
	
}
