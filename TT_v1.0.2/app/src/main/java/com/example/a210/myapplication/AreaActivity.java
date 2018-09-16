package com.example.a210.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AreaActivity extends AppCompatActivity {

    ListView selectList;
    TextView resultText;
    Intent it,itGet;
    ArrayAdapter subjectAdapter;
    public static TextView itGetTextView;
    String key3="ED57FE12-D1BD-30C1-83B1-82C3FFB9AF92&domain=http://www.test.com&";
    String str = "first";

    String[] sido = {"LT_C_ADSIDO_INFO","ctp_kor_nm"};
    String[] sgg = {"LT_C_ADSIGG_INFO","sig_kor_nm"};
    String[] emd = {"LT_C_ADEMD_INFO","emd_kor_nm"};
    String filter_sido = "attrFilter=ctp_kor_nm:like:";
    String filter_etc = "attrFilter=full_nm:like:";

    ArrayList<String> arr = new ArrayList<>();

    int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        selectList = (ListView)findViewById(R.id.selectAreaList);
        resultText = (TextView)findViewById(R.id.resultAreaText);
        itGetTextView = (TextView)findViewById(R.id.itGetAreaTextView);
        itGet = getIntent();


        final LocalBroadcastManager localBroadcastManager =  LocalBroadcastManager.getInstance(this);



        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub

                getXmlData();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Log.e("ARR",arr.get(0));

                        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arr);
                        selectList.setAdapter(adapter);
                        result++;
                    }
                });
            }
        }).start();

        selectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!str.equals("second"))
                    str += " ";
                else if(str.equals("second"))
                    str = "";
                str += arr.get(i);

                arr.clear();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        getXmlData();
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arr);
                                selectList.setAdapter(adapter);
                                result++;
                                if(result >= 4) {
                                    Intent intent2 = new Intent("SIGNUP-MESSAGE");
                                    intent2.putExtra("SIGNUP_AREA",str);
                                    localBroadcastManager.sendBroadcast(intent2);
                                    Log.e("ARR1234",str);
                                    finish();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

    String getXmlData(){
        StringBuffer buffer=new StringBuffer();

        String location = URLEncoder.encode(str);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     //지역 검색 위한 변수

        String[] sep = str.split(" ");
        String queryUrl = "";

        if(str.equals("first")) {
            queryUrl = "http://api.vworld.kr/req/data?service=data&size=100&request=GetFeature&" +
                    "data=" + sido[0] + "&format=xml&key=" + key3 + filter_sido + "%25%25" +
                    "&geometry=false";
            str = "second";
        } else if(sep.length == 1) {
            queryUrl = "http://api.vworld.kr/req/data?service=data&size=100&request=GetFeature&" +
                    "data=" + sgg[0] + "&format=xml&key=" + key3 + filter_etc + str +
                    "&geometry=false";
        } else if(sep.length > 1) {
            if (sep[sep.length - 1].charAt(sep[sep.length - 1].length() - 1) == '시' ||
                    sep[sep.length - 1].charAt(sep[sep.length - 1].length() - 1) == '군' ||
                    sep[sep.length - 1].charAt(sep[sep.length - 1].length() - 1) == '구') {
                queryUrl = "http://api.vworld.kr/req/data?service=data&size=100&request=GetFeature&" +
                        "data=" + emd[0] + "&format=xml&key=" + key3 + filter_etc + str +
                        "&geometry=false";
            } else if (sep[sep.length - 1].charAt(sep[sep.length - 1].length() - 1) == '읍' ||
                    sep[sep.length - 1].charAt(sep[sep.length - 1].length() - 1) == '면' ||
                    sep[sep.length - 1].charAt(sep[sep.length - 1].length() - 1) == '동') {
                queryUrl = "http://api.vworld.kr/req/data?service=data&size=100&request=GetFeature&" +
                        "data=" + emd[0] + "&format=xml&key=" + key3 + filter_etc + str +
                        "&geometry=false";
            }
        }

            Log.e("queryUrl", queryUrl);
            try {
                URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
                InputStream is = url.openStream(); //url위치로 입력스트림 연결
                Log.e("test1", "test");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

                Log.e("test2", "test");
                String tag;

                Log.e("test3", factory.toString());
                xpp.next();
                Log.e("test4", "test");
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            buffer.append("파싱 시작...\n\n");
                            break;

                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();//태그 이름 얻어오기
                            if (tag.equals("item")) ;// 첫번째 검색결과
                            else if (tag.equals(sido[1])) {
                                xpp.next();
                                if (!xpp.getText().equals("세종특별자치시"))
                                    arr.add(xpp.getText());
                            } else if (tag.equals(sgg[1])) {
                                xpp.next();
                                arr.add(xpp.getText());
                            } else if (tag.equals(emd[1])) {
                                xpp.next();
                                arr.add(xpp.getText());
                            }
                            break;

                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            tag = xpp.getName(); //태그 이름 얻어오기

                            if (tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                            break;
                    }

                    eventType = xpp.next();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch blocke.printStackTrace();
                Log.e("exception", e.toString());
            }

            buffer.append("파싱 끝\n");

            return buffer.toString();//StringBuffer 문자열 객체 반환
    }
}
