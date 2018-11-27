package com.example.a210.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TendencyActivity extends AppCompatActivity implements View.OnClickListener{


    String successFlag;
    RadioButton radioGroupTendency_rbDayTime;
    RadioButton radioGroupTendency_rbTendercy1Nomal;
    RadioButton radioGroupTendency_rbNight;
    RadioButton radioGroupTendency_rbIntroverted;
    RadioButton radioGroupTendency_rbTendercy2Nomal;
    RadioButton radioGroupTendency_rbExtroverted;
    RadioButton radioGroupTendency_rbVacationSpot;
    RadioButton radioGroupTendency_rbTendercy3Nomal;
    RadioButton radioGroupTendency_rbTourSpot;
    Intent it;
    Button successTendencyBtn;
    String tendency1;
    String tendency2;
    String tendency3;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(successFlag.contains("Success") == true) {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tendency);

        radioGroupTendency_rbDayTime = (RadioButton)findViewById(R.id.rbDayTime) ;
        radioGroupTendency_rbTendercy1Nomal = (RadioButton)findViewById(R.id.rbTendercy1Nomal) ;
        radioGroupTendency_rbNight = (RadioButton)findViewById(R.id.rbNight) ;
        radioGroupTendency_rbIntroverted = (RadioButton)findViewById(R.id.rbIntroverted) ;
        radioGroupTendency_rbTendercy2Nomal = (RadioButton)findViewById(R.id.rbTendercy2Nomal) ;
        radioGroupTendency_rbExtroverted = (RadioButton)findViewById(R.id.rbExtroverted) ;
        radioGroupTendency_rbVacationSpot = (RadioButton)findViewById(R.id.rbVacationSpot) ;
        radioGroupTendency_rbTendercy3Nomal = (RadioButton)findViewById(R.id.rbTendercy3Nomal) ;
        radioGroupTendency_rbTourSpot = (RadioButton)findViewById(R.id.rbTourSpot) ;
        successTendencyBtn = (Button)findViewById(R.id.successTendencyBtn) ;

        radioGroupTendency_rbDayTime.setOnClickListener(this);
        radioGroupTendency_rbTendercy1Nomal.setOnClickListener(this);
        radioGroupTendency_rbNight.setOnClickListener(this);
        radioGroupTendency_rbIntroverted.setOnClickListener(this);
        radioGroupTendency_rbTendercy2Nomal.setOnClickListener(this);
        radioGroupTendency_rbExtroverted.setOnClickListener(this);
        radioGroupTendency_rbVacationSpot.setOnClickListener(this);
        radioGroupTendency_rbTendercy3Nomal.setOnClickListener(this);
        radioGroupTendency_rbTourSpot.setOnClickListener(this);
        successTendencyBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.successTendencyBtn :

                if(    tendency1.toString() == ""
                    || tendency2.toString() == ""
                    || tendency3.toString() == "")
                {
                    Toast.makeText(getApplicationContext(), "성향을 선택해주세요..!!", Toast.LENGTH_LONG).show();
                    return;
                }

                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            String urlString = getString(R.string.url_Server)+"/SignupUserForTendency.jsp?";
                            urlString += "USER_ID=" + UserInfomation.User_ID.toString();
                            urlString += "&TENDENCY1=" + tendency1.toString();
                            urlString += "&TENDENCY2=" + tendency2.toString();
                            urlString += "&TENDENCY3=" + tendency3.toString();

                            FirebaseInstanceId.getInstance().getToken();

                            successFlag = TomcatConnector(urlString);


                            UserInfomation.Tendency1 = tendency1;
                            UserInfomation.Tendency2 = tendency2;
                            UserInfomation.Tendency3 = tendency3;


                        }catch (Exception e) {
                            successFlag = "Fail";
                        }
                        handler.sendEmptyMessage(0);
                    }
                });
                th.start();
                //finish();
                break;
            case R.id.rbDayTime :
                tendency1 = "낮";
                break;
            case R.id.rbTendercy1Nomal :
                tendency1 = "보통";
                break;
            case R.id.rbNight :
                tendency1 = "밤";
                break;
            case R.id.rbIntroverted :
                tendency2 = "내성적";
                break;
            case R.id.rbTendercy2Nomal :
                tendency2 = "보통";
                break;
            case R.id.rbExtroverted :
                tendency2 = "외향적";
                break;
            case R.id.rbVacationSpot :
                tendency3 = "휴양지";
                break;
            case R.id.rbTendercy3Nomal :
                tendency3 = "보통";
                break;
            case R.id.rbTourSpot :
                tendency3 = "관광지";
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
