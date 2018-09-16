package com.example.a210.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LonginActivity extends AppCompatActivity implements View.OnClickListener{


    String successFlag = "Fail";
    EditText editTextLogin_ID;
    EditText editTextLogin_Password;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(successFlag.contains("Success") == true)
            {
                Toast.makeText(getApplicationContext(), "Success..!!", Toast.LENGTH_LONG).show();
                Intent it = new Intent(getApplicationContext(),MainActivity.class);
                it.putExtra("sep","quest");
                startActivity(it);
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "Fail..!!", Toast.LENGTH_LONG).show();

        }
    };

    Button login,signUp;
    Intent it;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longin);

        editTextLogin_ID = (EditText) findViewById(R.id.idGet);
        editTextLogin_Password = (EditText) findViewById(R.id.pwGet);

        login = (Button)findViewById(R.id.login);
        signUp = (Button)findViewById(R.id.signUp);

        login.setOnClickListener(this);
        signUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login :
                String str = FirebaseInstanceId.getInstance().getToken();
                Log.d("test token",str);
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            String jsonString;
                           // String urlString = "http://lim7504.iptime.org:8080/TripTalkWebServer/Login.jsp?";
                            String urlString = "http://lim7504.iptime.org:8080/TripTalkWebServer/Login.jsp?";
                            urlString += "USER_ID=" + editTextLogin_ID.getText().toString();
                            urlString += "&USER_PASSWORD=" + editTextLogin_Password.getText().toString();
                            urlString += "&USER_TOKEN=" +  FirebaseInstanceId.getInstance().getToken();

                            jsonString = TomcatConnector(urlString);
                            JSONArray arr = new JSONArray(jsonString);

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);

                                UserInfomation.User_ID = obj.get("USER_ID").toString();
                                UserInfomation.User_NickName = obj.getString("USER_NICKNAME");
                                UserInfomation.User_Age = obj.getInt("USER_AGE");
                                UserInfomation.User_Sex = obj.getString("USER_SEX").toString();
                                UserInfomation.User_Fun = obj.getString("USER_FUN").toString();
                                successFlag = "Success";
                            }


                        }catch (Exception e) {
                            successFlag = "Fail";
                            handler.sendEmptyMessage(0);
                        }
                        handler.sendEmptyMessage(0);
                    }
                });
                th.start();

                break;
            case R.id.signUp :
                it = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(it);
                break;
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
