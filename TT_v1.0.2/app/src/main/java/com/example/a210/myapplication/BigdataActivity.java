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

public class BigdataActivity extends AppCompatActivity{
    ListView bigdataList;
    Intent it,itGet;

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


        bigdataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(getApplicationContext(),BigdataActivityStatistics.class);
                it.putExtra("SELECT_INDEX", i);
                it.putExtra("sep","quest");
                startActivity(it);
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
