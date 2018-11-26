package com.example.a210.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class ChatMsgFromDBAsync extends AsyncTask<String, String, String> {
    String chatFromDBProtocol[] ={"ROOM_ID","GET_MESSAGE_ID"};
    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;
            URL url = new URL( "http://lim7504.iptime.org:8080/TripTalkWebServer/ChatMsgFromDB.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

          //  String sendMsg = chatFromDBProtocol[0] + "=" + strings[0] + "&" + chatFromDBProtocol[1] + "=" + strings[1];
            String sendMsg = chatFromDBProtocol[1] + "=" + strings[0];

            osw.write(sendMsg);
            osw.flush();
            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                String receiveMsg = buffer.toString();

                return receiveMsg;
            } else {

                Log.i("통신 결과", conn.getResponseCode()+"에러");
                return "Fail";
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return "Fail";
    }

}

