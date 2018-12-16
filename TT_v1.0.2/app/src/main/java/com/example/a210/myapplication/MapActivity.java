package com.example.a210.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.maps.NMapView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapActivity extends FragmentActivity {

    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "K5vbJxcWC35Rdakrwifz";// 애플리케이션 클라이언트 아이디 값
    public static TextView myLocation;
    Button selectButton;
    Intent it,itGet;
    EditText etcEditText;
    Spinner subjectSpinner;
    ArrayAdapter subjectAdapter;
    public static TextView itGetTextView;
    String successFlag;

    Button btn1;
    int flag = 1;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(successFlag.contains("ACK") == true) {
                Toast.makeText(getApplicationContext(), "매칭을 시작했습니다. 대기해주세요.", Toast.LENGTH_LONG).show();
                Intent it = new Intent(getApplicationContext(), MainActivity.class);
                it.putExtra("sep","quest");
                startActivity(it);
                finish();
/*
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            String jsonString;
                            String urlString = getResources().getString(R.string.url_Server)+"/QuestionSelectForMy.jsp?";
                            urlString += "USER_ID=" + UserInfomation.User_ID;
                            urlString += "&SERACH_AREA=" + UserInfomation.SearchArea;
                            urlString += "&SEARCH_SUBJECT=" + UserInfomation.SearchSubJect;
                            jsonString = TomcatConnector(urlString);

                            JSONArray arr = new JSONArray(jsonString);

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);

                                UserInfomation.Wait_ID = obj.get("WAIT_ID").toString();
                            }

                            Intent it = new Intent(getApplicationContext(), RoomActivity.class);

                            it.putExtra("nick", UserInfomation.User_ID);
                            //it.putExtra("grade", UserInfomation.User_ID);
                            it.putExtra("subTitle", etcEditText.getText().toString());
                            it.putExtra("create", "True");
                            startActivity(it);
                        }
                        catch (Exception e)
                        {
                        }
                    }
                });
                th.start();
                */

                //if(bResult) {
            }
            else
                Toast.makeText(getApplicationContext(), "Fail..!!", Toast.LENGTH_LONG).show();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        selectButton = (Button)findViewById(R.id.selectButton);
        subjectSpinner = (Spinner)findViewById(R.id.subjectSpinner);
        etcEditText = (EditText)findViewById(R.id.etcEditText);
        itGetTextView = (TextView)findViewById(R.id.itGetTextView);
        itGet = getIntent();

        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if(permissionCheck1 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET},1);

        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissionCheck2 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},1);

        int permissionCheck3 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck3 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);

        btn1 = (Button)findViewById(R.id.btn1);
        myLocation = (TextView)findViewById(R.id.myLocation);


        subjectAdapter = ArrayAdapter.createFromResource(this, R.array.subject, android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myLocation.getText().equals("주소를 찾는 중...") && flag != 1){
                    Toast.makeText(getApplicationContext(),"현재 주소를 찾는 중 입니다.\n잠시 기다려주세요",Toast.LENGTH_LONG).show();
                    return;
                }
                myLocation.setText("주소를 찾는 중...");
                NaverFrag naverFrag = new NaverFrag();
                naverFrag.setArguments(new Bundle());
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                if(flag == 0) {
                    fragmentTransaction.replace(R.id.fragmentHere,naverFrag);
                } else if(flag == 1) {
                    fragmentTransaction.add(R.id.fragmentHere, naverFrag);
                    flag = 0;
                }
                fragmentTransaction.commit();
            }
        });

        callFragment(1);
        callFragment(2);

        final View activityRootView = findViewById(R.id.MapContainer);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 100) {
                    Log.d("kth", "activityRootView on");
                } else {
                    Log.d("kth", "activityRootView off");
                }
            }
        });

       // myLocation.setText("인천광역시 부평구 산곡동");

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(subjectSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(),"항목 구분을 선택해주세요",Toast.LENGTH_LONG).show();
                    return;
                } else if(etcEditText.getText().toString().equals("") || etcEditText.getText() == null) {
                    Toast.makeText(getApplicationContext(),"기타 사항을 입력해주세요",Toast.LENGTH_LONG).show();
                    return;
                } else if(myLocation.getText().toString().equals("현 위치를 검색해주세요")) {
                    Toast.makeText(getApplicationContext(),"현 위치를 검색해주세요",Toast.LENGTH_LONG).show();
                    return;
                } else if(myLocation.getText().toString().equals("주소를 찾는 중...")) {
                    Toast.makeText(getApplicationContext(),"주소를 찾는 중 입니다.\n잠시만 기다려주세요.",Toast.LENGTH_LONG).show();
                    return;
                }

                UserInfomation.SearchArea = myLocation.getText().toString();

                UserInfomation.SearchSubJect = etcEditText.getText().toString();

                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            String urlString = getString(R.string.url_Server)+"/QuestionRegist.jsp?";
                            urlString += "QUESTION_USER_ID=" + UserInfomation.User_ID;
                            urlString += "&QUESTION_CONTENS=" + etcEditText.getText().toString();
                            urlString += "&QUESTION_AREA=" + myLocation.getText().toString();
                            urlString += "&QUESTION_SUBJECT=" + subjectSpinner.getSelectedItem().toString();
                            urlString += "&ISQUESTION=Y";

                            successFlag = TomcatConnector(urlString);

                        }catch (Exception e) {
                            successFlag = "Fail";
                        }
                        handler.sendEmptyMessage(0);
                    }
                });
                th.start();
           //     finish();


//                it = new Intent(getApplicationContext(),ChatActivity.class);
//                it.putExtra("Location",myLocation.getText());
//                if(itGetTextView.getText().toString().equals("dap"))
//                    it.putExtra("sep","dap");
//                else if(itGetTextView.getText().toString().equals("quest"))
//                    it.putExtra("sep","quest");
//                else if(itGetTextView.getText().toString().equals("none"))
//                    it.putExtra("sep",itGet.getStringExtra("sep"));
//                it.putExtra("Subject","asdf");
//                it.putExtra("Subtitle","알라아랄");
//                startActivity(it);
//                finish();
            }
        });
    }

    private void callFragment(int frament_no) {

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            case 1:
                // '프래그먼트1' 호출
                Footer fragment1 = new Footer();
                Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
                bundle.putString("sep", itGet.getStringExtra("sep").toString()); // key , value
                fragment1.setArguments(bundle);
                transaction.replace(R.id.footer, fragment1);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                Header fragment2 = new Header();
                transaction.replace(R.id.header, fragment2);
                transaction.commit();
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