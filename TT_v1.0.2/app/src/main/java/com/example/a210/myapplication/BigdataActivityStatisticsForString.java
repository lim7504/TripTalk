package com.example.a210.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigdataActivityStatisticsForString extends AppCompatActivity{
    TextView statisticsForStringTextView;
    Intent it,itGet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigdatastatisticsforstring);

        statisticsForStringTextView = (TextView)findViewById(R.id.statisticsForStringTextView);
        itGet = getIntent();
        String  str = itGet.getStringExtra("str").toString();

        statisticsForStringTextView.setText(str);

        Linkify.TransformFilter mTransform = new Linkify.TransformFilter()
        {
            @Override public String transformUrl(Matcher match, String url)
            {
                return "";
            }
        };

        List<Pattern> patternList = new ArrayList<>();
        String patternString = "";
        boolean wordOpenFlag = false;
        for(int i = 0; i < str.length(); i ++)
        {
            if(str.charAt(i) == '[')
            {
                wordOpenFlag = true;
            }
            else if(str.charAt(i) == ']')
            {
                wordOpenFlag = false;

                patternString += ']';
                Pattern pattern = Pattern.compile(patternString);
                patternList.add(pattern);
                patternString = "";
            }

            if(wordOpenFlag == true)
            {
                patternString += str.charAt(i);

            }
        }

        for (Pattern pattern: patternList)
        {
            Linkify.addLinks(statisticsForStringTextView, pattern, "http://search.naver.com/search.naver?query=" + pattern.toString().replace("[","").replace("]",""),null,mTransform);
        }



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
