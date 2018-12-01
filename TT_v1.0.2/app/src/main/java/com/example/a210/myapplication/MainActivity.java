package com.example.a210.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity{
    ListView mainList;
    Intent it,itGet;
    String blogId;
    String index = "";
    String jsonStringForIntent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainList    = (ListView)findViewById(R.id.mainList);
        final mainViewAdapter adapter = new mainViewAdapter(this);

        mainList.setAdapter(adapter);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                blogId = adapter.getBlogId(i);

                if(blogId.contains("bigdata_") == true) {
                    index = blogId.replace("bigdata_", "");

                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                String urlString = getString(R.string.url_Server) + "/MongoDB_Select.jsp?";
                                urlString += "USER_ID=" + UserInfomation.User_ID;
                                urlString += "&SELECT_INDEX=" + index;
                                urlString += "&USER_AGE=" + UserInfomation.User_Age.toString();
                                urlString += "&USER_SEX=" + UserInfomation.User_Sex.toString();
                                urlString += "&USER_FUN=" + UserInfomation.User_Fun.toString();

                                jsonStringForIntent = TomcatConnector(urlString);
                            } catch (Exception e) {
                            }
                        }
                    });
                    th.start();

                    try {
                        th.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (blogId.equals("bigdata_0")
                            || blogId.equals("bigdata_1")
                            || blogId.equals("bigdata_2")
                            || blogId.equals("bigdata_3")) {
                        Intent it = new Intent(getApplicationContext(), BigdataActivityStatistics.class);
                        it.putExtra("str", jsonStringForIntent);
                        it.putExtra("sep", "quest");
                        startActivity(it);
                    } else if (blogId.equals("bigdata_4")
                            || blogId.equals("bigdata_6")) {
                        Intent it = new Intent(getApplicationContext(), BigdataActivityStatisticsForString.class);
                        it.putExtra("str", jsonStringForIntent);
                        it.putExtra("sep", "quest");
                        startActivity(it);
                    } else if (blogId.equals("bigdata_5")) {
                        Intent it = new Intent(getApplicationContext(), BlogDetailActivity.class);
                        it.putExtra("blogId", jsonStringForIntent.replace("\n", ""));
                        startActivity(it);
                    }
                }
                else {
                    Intent it = new Intent(getApplicationContext(), BlogDetailActivity.class);
                    it.putExtra("blogId", blogId);
                    startActivity(it);
                }
            }
        });
        /*
        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"빅데이터추천 TOP10\n조용한 여행지",
                "참여한 인원 - 836172","한달간 집계한 관광객이 가장 적은 여행지");
        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"빅데이터추천 TOP10\n많이가는 여행지",
                "참여한 인원 - 987981","한달간 집계한 관광객이 가장 많은 여행지");
        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"빅데이터추천\n클릭클릭",
                "참여한 인원 - 102909","1주간 클릭수가 가장 많은 지역");
        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"태양신이 간다\n울진-삼척 투어",
                "조회수 - 31287","태양신이 떴다 울진-삼척 숨은명소 탐방");
        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"태양신이 간다\n부산 내륙편",
                "조회수 - 8912","'부산은 해안가보다는 내륙이지!' 실전 루트");
        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"빅데이터추천\n이런날씨 이런장소",
                "참여한 인원 - 121787","오늘같이 비오는날 갔던 여행지");
                */

        callFragment(1);
        callFragment(2);
    }

    private void callFragment(int frament_no) {

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        itGet = getIntent();


        switch (frament_no) {
            case 1:
                // '프래그먼트1' 호출
                Log.i("WRYYYYYYYYYY", itGet.getStringExtra("sep"));
                Footer fragment1 = new Footer();
                Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
                bundle.putString("sep", itGet.getStringExtra("sep")); // key , value
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

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            if(conn != null)
            {
                conn.setConnectTimeout(3000);
                conn.setUseCaches(false);
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
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

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return html.toString();

    }

}
