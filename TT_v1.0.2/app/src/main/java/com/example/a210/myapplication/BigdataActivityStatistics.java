package com.example.a210.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

public class BigdataActivityStatistics extends AppCompatActivity{
    ListView bigdataList;
    Intent it,itGet;
    List<String> data = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigdatastatistics);

        bigdataList = (ListView)findViewById(R.id.bigdataList);
        itGet = getIntent();
        Integer a= itGet.getIntExtra("SELECT_INDEX",0);

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    String jsonString;
                    String urlString = "http://lim7504.iptime.org:8080/TripTalkWebServer/MongoDB_Select.jsp?";
                    urlString += "SELECT_INDEX=" + itGet.getIntExtra("SELECT_INDEX",0);
                    urlString += "&USER_AGE=" + UserInfomation.User_Age.toString();
                    urlString += "&USER_SEX=" + UserInfomation.User_Sex.toString();
                    urlString += "&USER_FUN=" + UserInfomation.User_Fun.toString();
                    jsonString = TomcatConnector(urlString);

                    JSONArray arr = new JSONArray(jsonString);

                    for (int i = 0; i < arr.length(); i++) {

                        if(i == 10)
                            break;

                        JSONObject obj = arr.getJSONObject(i);

                        data.add(data.size() + 1 + "위 [" + obj.get("QUESTION_AREA").toString() + "] " + obj.get("COUNT").toString() + "회");
                    }
                }
                catch (Exception e)
                {
                }
            }
        });
        th.start();


        final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
        bigdataList.setAdapter(adapter);


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
