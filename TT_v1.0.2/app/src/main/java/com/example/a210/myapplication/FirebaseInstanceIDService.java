package com.example.a210.myapplication;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG = "FirebaseIIDService";

    String successFlag;
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(successFlag.contains("Success") == true)
                Toast.makeText(getApplicationContext(), "Success..!!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Fail..!!", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();

        // 각자 핸드폰 토큰값을 핸드폰으로 전송한다
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token)
    {
        new ProcessFacebookTask().execute(null,null,null);
    }

    private class ProcessFacebookTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        String urlString = "http://lim7504.iptime.org:8080/TripTalkWebServer/register.jsp?";
                        urlString += "Token=" + FirebaseInstanceId.getInstance().getToken();

                        successFlag = TomcatConnector(urlString);

                    }catch (Exception e) {
                        successFlag = "Fail";
                    }
                    handler.sendEmptyMessage(0);
                }
            });
            th.start();

            return null;
        }
    }

    public String TomcatConnector(String urlString) {

        StringBuilder html = new StringBuilder();
        try {
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                conn.setConnectTimeout(3000);
                conn.setUseCaches(false);

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br =  new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));

                    while (true)
                    {
                        String line = br.readLine();
                        if(line == null) break;
                        html.append(line+"\n");

                    }
                    br.close();
                }
                conn.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return html.toString();

    }
}