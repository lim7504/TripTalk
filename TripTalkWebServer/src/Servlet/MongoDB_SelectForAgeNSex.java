package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

public class MongoDB_SelectForAgeNSex
{	
	public MongoDB_SelectForAgeNSex()
	{		
	}
	
	public String Select(Integer user_age, String user_sex)
	{
		Data data = new Data(user_age, user_sex);
		return data.GetList();
	}
	
	public class Model implements Comparable<Model> 
	{
		private String question_area;	
		
		private Integer count = 1;
		
		public Model(String question_area)
		{
			this.question_area = question_area;			
		}
		

		public String GetQuestionArea()
		{
			return this.question_area;
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
		
		public Data(Integer user_age, String user_sex)
		{
			this.MongoDBConnecting(user_age, user_sex);
			this.SetlistModelParsingNDistinct();
			this.SetListModelSort();
		}
		
		private void MongoDBConnecting(Integer user_age, String user_sex)
		{
			this.mongo = new MongoClient("lim7504.iptime.org", 27017);
			this.db =  mongo.getDatabase("TRIPTALK");	
			this.collFinger = db.getCollection("FINGER");
			
			Integer ageFirst = (user_age / 10) * 10;
			Integer ageEnd = (user_age / 10) * 10 + 10;
			
			this.fingerTotal = (List<Document>) collFinger.find(and(and(and(gte("USER_AGE", ageFirst),lt("USER_AGE", ageEnd)),eq("USER_SEX", user_sex),eq("LOG_CODE", "AREA")))).into(new ArrayList<Document>());
		}
		
		private void SetlistModelParsingNDistinct()
		{
		    for (Document fingerDocument : fingerTotal) 
		    {   
		    	System.out.println(fingerDocument.toJson());
		    	boolean existFlag = false;
				String question_area = fingerDocument.getString("QUESTION_AREA");
		    	
				for(Model model : listModel)
				{		
					
					if(model != null 
							&& model.GetQuestionArea().equals(question_area) == true)
					{						
						model.SetCount(model.GetCount() + 1 );
						existFlag = true;
					}										
				}				
				
				if(existFlag == false)
				{
					listModel.add(new Model(question_area));						
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
	        	
	        	str += "{\"QUESTION_AREA\" : \"" + model.GetQuestionArea() + "\""
	            		+ ", \"COUNT\" : " + model.GetCount().toString() + "}";
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
