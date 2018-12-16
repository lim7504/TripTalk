package com.example.a210.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    CheckBox rememberID;

    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

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
                if(prefs.getBoolean("auto", false) && prefs.getString("id","").equals("")
                        && !editTextLogin_ID.getText().toString().equals("")) {
                    edit.putString("id",editTextLogin_ID.getText().toString());
                    edit.putString("pass",editTextLogin_Password.getText().toString());
                    edit.commit();
                    Log.d("LOGIN1", ""+prefs.getBoolean("auto", false));
                    Log.d("LOGIN2", ""+prefs.getString("id", ""));
                    Log.d("LOGIN3", ""+prefs.getString("pass", ""));
                } else if(prefs.getBoolean("auto", false) && !prefs.getString("id","").equals("")) {
                    edit.putString("id",prefs.getString("id", ""));
                    edit.putString("pass",prefs.getString("pass", ""));
                    edit.commit();
                    Log.d("LOGIN1", ""+prefs.getBoolean("auto", false));
                    Log.d("LOGIN2", ""+prefs.getString("id", ""));
                    Log.d("LOGIN3", ""+prefs.getString("pass", ""));
                }
                startActivity(it);
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "Fail..!!", Toast.LENGTH_LONG).show();

        }
    };

    Button login,signUp;
    Button testLocation;
    Intent it;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longin);

        editTextLogin_ID = (EditText) findViewById(R.id.idGet);
        editTextLogin_Password = (EditText) findViewById(R.id.pwGet);
        rememberID = (CheckBox)findViewById(R.id.rememberID);

        login = (Button)findViewById(R.id.login);
        signUp = (Button)findViewById(R.id.signUp);


        login.setOnClickListener(this);
        signUp.setOnClickListener(this);


        prefs = getSharedPreferences("USER", MODE_PRIVATE);
        edit = prefs.edit();

        Log.d("sssss", ""+prefs.getBoolean("auto", false));
        Log.d("sssss", ""+prefs.getString("id", ""));
        Log.d("sssss", ""+prefs.getString("pass", ""));

        if (prefs.getBoolean("auto", false) && !prefs.getString("id", "").equals("")){
            rememberID.setChecked(true);
            attemptAutoLogin();
        }

        rememberID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rememberID.isChecked()){
                    edit.putBoolean("auto", true);
                    edit.commit();
                } else {
                    edit.putBoolean("auto", false);
                    edit.commit();
                }
            }
        });
    }

    private void attemptAutoLogin() {

        // pref에 저장된 암호 비번 가져오기

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        String str = FirebaseInstanceId.getInstance().getToken();
        Log.d("test token",str);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String jsonString;
                    String urlString = getString(R.string.url_Server)+"/Login.jsp?";
                    String id = prefs.getString("id", "");
                    String password = prefs.getString("pass", "");
                    //String urlString = "http://10.0.2.2:8080/TripTalkWebServer/Login.jsp?";
                    urlString += "USER_ID=" + id;
                    urlString += "&USER_PASSWORD=" + password;
                    urlString += "&USER_TOKEN=" +  FirebaseInstanceId.getInstance().getToken();

                    jsonString = TomcatConnector(urlString);
                    JSONArray arr = new JSONArray(jsonString);

                    Log.d("확인용 주소",urlString);

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
                            String urlString = getString(R.string.url_Server)+"/Login.jsp?";
                            //String urlString = "http://10.0.2.2:8080/TripTalkWebServer/Login.jsp?";
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
