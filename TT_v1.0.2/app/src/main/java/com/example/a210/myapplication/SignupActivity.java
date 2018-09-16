package com.example.a210.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{


    String successFlag;
    EditText editTextSignup_ID;
    EditText editTextSignup_Password;
    EditText editTextSignup_NickName;
    EditText editTextSignup_Age;
    RadioButton radioGroupSignup_Man;
    RadioButton radioGroupSignup_Woman;
    Spinner spinnerSignup_Fun;
    Button btnAreaSearch;
    TextView tvArea;
    String sex = "남자";
    List<String> listLive = new ArrayList<String>();
    List<String> listFun = new ArrayList<String>();

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

    Intent it;
    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler,new IntentFilter("SIGNUP-MESSAGE"));
        listLive.add("서울");
        listLive.add("인천");
        listLive.add("부산");
        listLive.add("광주");
        listLive.add("대전");

        listFun.add("운동");
        listFun.add("음식");
        listFun.add("여행");

        editTextSignup_ID = (EditText)findViewById(R.id.id);
        editTextSignup_Password = (EditText)findViewById(R.id.pwd);
        editTextSignup_NickName = (EditText)findViewById(R.id.nick);
        editTextSignup_Age = (EditText)findViewById(R.id.age);
        spinnerSignup_Fun = (Spinner)findViewById(R.id.funSpinner) ;
        radioGroupSignup_Man = (RadioButton)findViewById(R.id.radioMan) ;
        radioGroupSignup_Woman = (RadioButton)findViewById(R.id.radioWoman) ;
        registerBtn = (Button)findViewById(R.id.registerBtn);
        tvArea = (TextView)findViewById(R.id.tvArea);
        btnAreaSearch=(Button)findViewById(R.id.btnCitySearch);


        final ArrayAdapter adapterFun = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listFun);
        spinnerSignup_Fun = (Spinner) findViewById(R.id.funSpinner);
        adapterFun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSignup_Fun.setPrompt("구분을 선택하세요.");
        spinnerSignup_Fun.setSelection(0);
        spinnerSignup_Fun.setAdapter(adapterFun);

        radioGroupSignup_Man.setOnClickListener(this);
        radioGroupSignup_Woman.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        btnAreaSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerBtn :

                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            String urlString = "http://lim7504.iptime.org:8080/TripTalkWebServer/SignupUser.jsp?";
                            urlString += "USER_ID=" + editTextSignup_ID.getText().toString();
                            urlString += "&USER_PASSWORD=" + editTextSignup_Password.getText().toString();
                            urlString += "&USER_NICKNAME=" + editTextSignup_NickName.getText().toString();
                            urlString += "&USER_AGE=" + editTextSignup_Age.getText().toString();
                            urlString += "&USER_SEX=" + sex.toString();
                            urlString += "&USER_FUN=" + spinnerSignup_Fun.getSelectedItem().toString();
                            urlString += "&USER_ADDRESS=" + tvArea.getText().toString();
                            urlString += "&USER_TOKEN=" +  FirebaseInstanceId.getInstance().getToken();

                            FirebaseInstanceId.getInstance().getToken();

                            successFlag = TomcatConnector(urlString);

                        }catch (Exception e) {
                            successFlag = "Fail";
                        }
                        handler.sendEmptyMessage(0);
                    }
                });
                th.start();
                finish();
                break;
            case R.id.radioMan :
                sex = "남자";
                break;
            case R.id.radioWoman :
                sex = "여자";
                break;
            case R.id.btnCitySearch :
                Intent intent = new Intent(getApplicationContext(),AreaActivity.class);
                startActivity(intent);
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

    private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String strArea = intent.getStringExtra("SIGNUP_AREA");
            tvArea.setText(strArea);
        }
    };
}
