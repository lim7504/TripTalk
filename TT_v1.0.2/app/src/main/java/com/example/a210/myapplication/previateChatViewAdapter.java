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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class previateChatViewAdapter extends BaseAdapter {
    private ArrayList<ChatView> chatViewItem = new ArrayList<ChatView>();
    JSONObject jObject;
    JSONArray jArray;
    Context context;
    public previateChatViewAdapter(Context context) {

        try {
            this.context = context;
            String result = new previateChatViewAdapter.PreivateChatRoomSearch().execute().get();

            //jObject = new JSONObject(result);  // JSONObject 추출

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
        return chatViewItem.get(position);
    }

    public ChatView getChatViewItem(int position) {
        return chatViewItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        Log.e("test1234",String.valueOf(pos));
        View v;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            v = inflater.inflate(R.layout.customlayout, parent, false);
        } else {
            v = (View) convertView;
        }

        ImageView chatImage = (ImageView) v.findViewById(R.id.chatImageView);
        ImageView chatIcon = (ImageView) v.findViewById(R.id.chatIcon);
        TextView nick = (TextView) v.findViewById(R.id.chatNick);
        TextView grade = (TextView) v.findViewById(R.id.chatGrade);
        TextView subTitle = (TextView) v.findViewById(R.id.chatSubTitle);

        chatIcon.setVisibility(View.INVISIBLE);

        ChatView chatItem = chatViewItem.get(position);

       // chatImage.setImageDrawable(chatItem.getImage());
      //  nick.setText(chatItem.getNick());
      //  grade.setText(chatItem.getGrade());
      //  subTitle.setText(chatItem.getSubTitle());
       // chatIcon.setImageDrawable(chatItem.getIcon());

        try {

            JSONObject jObject = jArray.getJSONObject(pos);
            String ROOM_ID = jObject.getString("ROOM_ID");
            String QUOSTION_TYPE = jObject.getString("QUOSTION_TYPE");
            String QUOTATION_CONTENS = jObject.getString("QUOTATION_CONTENS");
            String REQUEST_USER_ID = jObject.getString("REQUEST_USER_ID");
            String RECEIVE_USER_ID = jObject.getString("RECEIVE_USER_ID");
            String WAIT_ID = jObject.getString("WAIT_ID");

            String log = ROOM_ID + "," + QUOSTION_TYPE + "," + QUOTATION_CONTENS + "," + REQUEST_USER_ID + "," + RECEIVE_USER_ID + "," + WAIT_ID;

            nick.setText(REQUEST_USER_ID);
            grade.setText(QUOSTION_TYPE);
            subTitle.setText(QUOTATION_CONTENS);
            chatItem.setRoomId(ROOM_ID);
            chatItem.setWaitID(WAIT_ID);
            chatItem.setNick(REQUEST_USER_ID);
            chatItem.setGrade(QUOSTION_TYPE);
            chatItem.setSubTitle(QUOTATION_CONTENS);

            Log.d("List-", log);
        }catch(Exception e)
        {
            e.printStackTrace();
        }


        return v;
    }

    public void addItem(Drawable image, Drawable icon, String nick, String grade, String subtitle, String wait_id) {
        ChatView item = new ChatView();

        item.setImage(image);
        item.setNick(nick);
        item.setGrade(grade);
        item.setSubTitle(subtitle);
        item.setIcon(icon);
        item.setWaitID(wait_id);
        item.setRoomId("");

        chatViewItem.add(item);
    }

    public void addItem(Drawable image, Drawable icon, String nick, String grade, String subtitle, String wait_id, String roomId) {
        ChatView item = new ChatView();

        item.setImage(image);
        item.setNick(nick);
        item.setGrade(grade);
        item.setSubTitle(subtitle);
        item.setIcon(icon);
        item.setWaitID(wait_id);
        item.setRoomId(roomId);

        chatViewItem.add(item);
    }

    class PreivateChatRoomSearch extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String urlString =  context.getString(R.string.url_Server) +"/PrivateRoomList.jsp?";
                urlString += "USER_ID=" + UserInfomation.User_ID;
                URL url = new URL(urlString);
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

                    try
                    {
                        jArray = new JSONArray(receiveMsg);

                        for (int i = 0; i < jArray.length(); i++) {

                            ChatView chatView = new ChatView();
                            chatViewItem.add(chatView);
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
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
