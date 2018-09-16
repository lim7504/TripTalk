package com.example.a210.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BlogDetailActivity extends AppCompatActivity {

    Intent it, getIt;
    JSONArray jArray;
    TextView detailTitle, detailWriter, detailDate, detailContent;
    LinearLayout blogLin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        detailTitle = (TextView)findViewById(R.id.detailTitle);
        detailWriter = (TextView)findViewById(R.id.detailWriter);
        detailDate = (TextView)findViewById(R.id.detailDate);
        detailContent = (TextView)findViewById(R.id.detailContent);
        blogLin = (LinearLayout)findViewById(R.id.blogLin);

        getIt = getIntent();
        String blogId = getIt.getStringExtra("blogId");

        try {
            String result = new BlogDetailSearch().execute(blogId).get();
            Log.d("뤼절트", result);

            //Log.e("끼야아랑가아락", result.toString());
            JSONObject jObject = new JSONObject(result);  // JSONObject 추출
            String code = jObject.getString("code");
            Log.d("blgInfo", code);
            if(code.equals("succ")){
                jArray = jObject.getJSONArray("contents");
                JSONObject item = jArray.getJSONObject(0);
                detailTitle.setText(item.getString("title"));
                detailWriter.setText(item.getString("writer"));
                detailDate.setText(item.getString("date"));
                detailContent.setText(item.getString("content"));

                for(int i = 1; i < 6; i ++) {
                    if(!item.getString("img" + i).equals("none_image")) {
                        ImageView img = new ImageView(this);
                        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        img.setBackgroundResource(R.drawable.underline);
                        img.setLayoutParams(imgParams);
                        img.setPadding(10,10,10,10);
                        img.setAdjustViewBounds(true);
                        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        String imageurl = getString(R.string.url_Server) + item.getString("img"+i);
                        Glide.with(this)
                                .load(imageurl)
                                .into(img);
                        blogLin.addView(img);
                    }
                }
            }else{
                Log.d("USERINFO", "blogDetail Not Working");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    class BlogDetailSearch extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(getString(R.string.url_MphotoUpload));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "type=blogDetailSearch"
                        + "&blogId="      + strings[0];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }
}
