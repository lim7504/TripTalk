package com.example.a210.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
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
        String  jsonString = itGet.getStringExtra("str").toString();

        try
        {
            JSONArray arr = new JSONArray(jsonString);

            for (int i = 0; i < arr.length(); i++)
            {
                if (i == 10)
                    break;

                JSONObject obj = arr.getJSONObject(i);

                data.add(data.size() + 1 + "위 [" + obj.get("QUESTION_AREA").toString() + "] " + obj.get("COUNT").toString() + "회");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
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
}
