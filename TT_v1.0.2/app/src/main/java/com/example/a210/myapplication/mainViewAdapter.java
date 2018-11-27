package com.example.a210.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class mainViewAdapter extends BaseAdapter{

    Context context;
    static JSONArray jArray;
    private ArrayList<mainView> mainViewItem = new ArrayList<mainView>();

    public mainViewAdapter(Context context) {
            this.context = context;

            try {
                String result = new BlogAllSearch().execute().get();
                Log.d("뤼절트", result);

                //Log.e("끼야아랑가아락", result.toString());
                JSONObject jObject = new JSONObject(result);  // JSONObject 추출
                String code = jObject.getString("code");
                Log.d("blgInfo", code);
                if(code.equals("succ")){
                    jArray = jObject.getJSONArray("contents");
                }else{
                    Log.d("USERINFO", "blog Not Working");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
    }

    @Override
    public int getCount() {
        if(jArray.length() != 0){
            return jArray.length();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        //final Context context = parent.getContext();

        View v;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            v = inflater.inflate(R.layout.mainview, parent, false);
        } else {
            v = (View) convertView;
        }

        ImageView mainImage = (ImageView) v.findViewById(R.id.mainImageView);
        TextView title = (TextView)v.findViewById(R.id.mainTitle);
        TextView user = (TextView)v.findViewById(R.id.mainUser);
        TextView subTitle = (TextView)v.findViewById(R.id.mainSubTitle);

        /*mainImage.setPadding(10, 10, 10, 10);    //테두리 설정
        LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 370);

        mainImage.setLayoutParams(imgParam);*/
        try {
            //for(int i=0; i<jArray.length(); i++){
            JSONObject item = jArray.getJSONObject(pos);

            if (item.getString("img1").equals("none_image")) {
                mainImage.setImageResource(R.drawable.default_back);
            } else {
                String imageurl = "http://lim7504.iptime.org:8080/TripTalkWebServer" + item.getString("img1");
                Log.e("쀍123", "***" + item.getString("img1") + "***" + position);
                Glide.with(context)
                        .load(imageurl)
                        .into(mainImage);
            }
            title.setText(item.getString("title"));
            user.setText(item.getString("date"));
            subTitle.setText(item.getString("writer"));
            //}
        }catch(Exception e){
            e.printStackTrace();
        }
        return v;
    }

    public void addItem(Drawable icon, String title, String user, String subTitle) {
        mainView item = new mainView();

        item.setImage(icon);
        item.setTitle(title);
        item.setUser(user);
        item.setSubTitle(subTitle);

        mainViewItem.add(item);
    }

    public String getBlogId(int position) {
        String ret = "";
        try {
            JSONObject item = jArray.getJSONObject(position);

            ret =  item.getString("blog_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    class BlogAllSearch extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://lim7504.iptime.org:8080/TripTalkWebServer/ttBlogImageUpload.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "type=blogAllSearch";
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
