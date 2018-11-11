package com.example.a210.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BigdataActivity extends AppCompatActivity{
    ListView bigdataList;
    Intent it,itGet;
    String jsonStringForIntent = "abcd";
    Integer index;
    List<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigdata);


        itGet = getIntent();

        bigdataList = (ListView)findViewById(R.id.bigdataList);
        BigdataViewAdapter adapter = new BigdataViewAdapter();

        bigdataList.setAdapter(adapter);

        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"빅데이터추천 TOP10\n많이가는 여행지",
                "참여한 인원 - 89","한달간 집계한 관광객이 가장 많은 여행지");
        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"빅데이터추천 TOP10\n조용한 여행지",
                "참여한 인원 - 57","한달간 집계한 관광객이 가장 적은 여행지");
        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"빅데이터추천\n" + (UserInfomation.User_Age/10) * 10 +"대의 " + UserInfomation.User_Sex + "모여라!",
                "참여한 인원 - 15","또래, 같은 성별이 많이 갔던 여행지");
        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"빅데이터추천\n" + (UserInfomation.User_Age/10) * 10 +"대의 " + UserInfomation.User_Fun + "이(가) 취미 인사람~",
                "참여한 인원 - 20","또래, 같은 취미의 사람들이 많이 갔던 여행지");
        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"빅데이터추천\n당신이 좋아할만한 여행지",
                "참여한 인원 - 430","최근 검색한 지역의 유사한 여행지");
        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"빅데이터추천\n당신이 좋아할만한 블로그",
                "참여한 인원 - 755","최근 방문한 블로그와 유사한 블로그");
        adapter.addItem(getResources().getDrawable(R.drawable.default_back),"빅데이터추천\n당신이 좋아할만한 여행지2",
                "참여한 인원 - 244","같은 지역을 질문했던 유저들의 선호 지역");

        bigdataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                index = i;
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {

                            String urlString = "http://lim7504.iptime.org:8080/TripTalkWebServer/MongoDB_Select.jsp?";
                            urlString += "USER_ID=" + UserInfomation.User_ID;
                            urlString += "&SELECT_INDEX=" + index.toString();
                            urlString += "&USER_AGE=" + UserInfomation.User_Age.toString();
                            urlString += "&USER_SEX=" + UserInfomation.User_Sex.toString();
                            urlString += "&USER_FUN=" + UserInfomation.User_Fun.toString();

                            jsonStringForIntent = TomcatConnector(urlString);
                        }
                        catch (Exception e)
                        {
                        }
                    }
                });
                th.start();

//                try {
//                    TimeUnit.MILLISECONDS.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                try {
                    th.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if( i< 4)
                {
                    Intent it = new Intent(getApplicationContext(), BigdataActivityStatistics.class);
                    it.putExtra("str",jsonStringForIntent);
                    it.putExtra("sep", "quest");
                    startActivity(it);
                }
                else if( i == 4
                        || i == 6)
                {
                    Intent it = new Intent(getApplicationContext(), BigdataActivityStatisticsForString.class);
                    it.putExtra("str",jsonStringForIntent);
                    it.putExtra("sep", "quest");
                    startActivity(it);
                }
                else if( i == 5 )
                {
                    Intent it = new Intent(getApplicationContext(),BlogDetailActivity.class);
                    it.putExtra("blogId",jsonStringForIntent.replace("\n",""));
                    startActivity(it);
                }
            }
        });

        callFragment(1);
        callFragment(2);
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
