package Servlet;

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

public class MongoDB_Insert
{	
	public MongoDB_Insert()
	{		
	}
	
	static public void FingerInsertForArea(String user_id, Integer user_age, String user_sex, String user_fun, String question_area, String question_area_detail, String tendency1, String tendency2, String tendency3)
	{
		MongoClient mongo = new MongoClient("lim7504.iptime.org", 27017);
		MongoDatabase db =  mongo.getDatabase("TRIPTALK");	
		MongoCollection<Document> collFinger = db.getCollection("FINGER");

		
		Document doc = new Document("LOG_CODE", "AREA")
							.append("USER_ID", user_id)
							.append("USER_AGE", user_age)
							.append("USER_SEX", user_sex)
							.append("USER_FUN", user_fun)
							.append("QUESTION_AREA", question_area)
							.append("QUESTION_AREA_DETAIL", question_area_detail)
							.append("TENDENCY1", tendency1)
							.append("TENDENCY2", tendency2)
							.append("TENDENCY3", tendency3);
		
			
		collFinger.insertOne(doc);
		
		mongo.close();
	}
	
	static public void FingerInsertForBlog(Integer user_age, String user_sex, String user_fun, String blog_id, String tendency1, String tendency2, String tendency3)
	{
		MongoClient mongo = new MongoClient("lim7504.iptime.org", 27017);
		MongoDatabase db =  mongo.getDatabase("TRIPTALK");	
		MongoCollection<Document> collFinger = db.getCollection("FINGER");

		
		Document doc = new Document("LOG_CODE", "BLOG")
							.append("USER_AGE", user_age)
							.append("USER_SEX", user_sex)
							.append("USER_FUN", user_fun)
							.append("BLOG_ID", blog_id)
							.append("TENDENCY1", tendency1)
							.append("TENDENCY2", tendency2)
							.append("TENDENCY3", tendency3);
		
			
		collFinger.insertOne(doc);
		
		mongo.close();
	}
}
