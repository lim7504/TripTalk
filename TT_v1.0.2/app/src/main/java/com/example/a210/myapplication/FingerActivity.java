package com.example.a210.myapplication;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FingerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button,finalSelectBtn;
    private EditText editText;

    ListView selectList;
    TextView resultText,resultAddress;
    Button selectButton,btnSearch,btnFinger;
    Intent it,itGet;
    EditText etcEditText;
    Spinner subjectSpinner;
    ArrayAdapter subjectAdapter;
    RelativeLayout searchAddress;
    Fragment map;
    public static TextView itGetTextView;
    String key3="ED57FE12-D1BD-30C1-83B1-82C3FFB9AF92&domain=http://www.test.com&";
    String str = "first";
    String successFlag;
    String jsonString;
    String[] sido = {"LT_C_ADSIDO_INFO","ctp_kor_nm"};
    String[] sgg = {"LT_C_ADSIGG_INFO","sig_kor_nm"};
    String[] emd = {"LT_C_ADEMD_INFO","emd_kor_nm"};
    String filter_sido = "attrFilter=ctp_kor_nm:like:";
    String filter_etc = "attrFilter=full_nm:like:";

    ArrayList<String> arr = new ArrayList<>();

    int result = 0;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

//            if(successFlag.contains("Success") == true)
//                Toast.makeText(getApplicationContext(), "Success..!!", Toast.LENGTH_LONG).show();
//            else
//                Toast.makeText(getApplicationContext(), "Fail..!!", Toast.LENGTH_LONG).show();

            if(result >= 4)
                GetQuestionAreaDetail();

        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);

        editText = (EditText) findViewById(R.id.addressEdt);
        button=(Button)findViewById(R.id.addressBtn);
        finalSelectBtn = (Button)findViewById(R.id.finalSelectBtn);
        selectList = (ListView)findViewById(R.id.selectList);
        resultText = (TextView)findViewById(R.id.resultText);
        resultAddress = (TextView)findViewById(R.id.resultAddress);
        selectButton = (Button)findViewById(R.id.selectButton);
        subjectSpinner = (Spinner)findViewById(R.id.subjectSpinner);
        etcEditText = (EditText)findViewById(R.id.etcEditText);
        itGetTextView = (TextView)findViewById(R.id.itGetTextView);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnFinger = (Button)findViewById(R.id.btnFinger);
        searchAddress = (RelativeLayout)findViewById(R.id.searchAddress);
        itGet = getIntent();

        subjectAdapter = ArrayAdapter.createFromResource(this, R.array.subject, android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

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

        callFragment(1);
        callFragment(2);

        selectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!str.equals("second"))
                    str += " ";
                else if(str.equals("second"))
                    str = "";
                str += arr.get(i);

                arr.clear();

                Thread th2 = new Thread(new Runnable() {
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
                                    selectList.setVisibility(View.GONE);
                                    resultText.setText(str);
                                    resultText.setVisibility(View.VISIBLE);

                                    handler.sendEmptyMessage(0);


                                }
                            }
                        });
                    }
                });
                th2.start();

                try {
                    th2.join();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectList.setVisibility(View.GONE);
                resultText.setText("");
                searchAddress.setVisibility(View.VISIBLE);
            }
        });

        btnFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectList.setVisibility(View.VISIBLE);
                resultAddress.setText("");
                editText.setText("");
                resultText.setText("");
                searchAddress.setVisibility(View.GONE);
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String addressSplit = "";
                String addressArray[] = {};
                String dong = "";

                if(subjectSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(),"항목 구분을 선택해주세요",Toast.LENGTH_LONG).show();
                    return;
                } else if(etcEditText.getText().toString().equals("") || etcEditText.getText() == null) {
                    Toast.makeText(getApplicationContext(),"기타 사항을 입력해주세요",Toast.LENGTH_LONG).show();
                    return;
                } else if(resultText.getText().equals("") && resultAddress.getText().equals("")) {
                    Toast.makeText(getApplicationContext(),"지역을 선택해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }

                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            String urlString = "http://lim7504.iptime.org:8080/TripTalkWebServer/QuestionRegist.jsp?";
                            urlString += "QUESTION_USER_ID=" + UserInfomation.User_ID;
                            urlString += "&QUESTION_CONTENS=" + etcEditText.getText().toString();
                            urlString += "&QUESTION_AREA=" + resultText.getText().toString();
                            urlString += "&QUESTION_AREA_DETAIL=" + "TEST".toString();
                            urlString += "&QUESTION_SUBJECT=" + subjectSpinner.getSelectedItem().toString();
                            urlString += "&ISQUESTION=Y";

                            successFlag = TomcatConnector(urlString);

                        }catch (Exception e) {
                            successFlag = "Fail";
                        }

                    }
                });
                th.start();

                try {
                    th.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                UserInfomation.SearchSubJect =  subjectAdapter.getItem(subjectSpinner.getSelectedItemPosition()).toString();
                UserInfomation.SearchArea = resultText.getText().toString();

                it = new Intent(getApplicationContext(),ChatActivity.class);
                if(resultAddress.getText().equals("")) {
                    it.putExtra("Location",resultText.getText());
                } else if(resultText.getText().equals("")) {
                    addressSplit = resultAddress.getText().toString().substring(5,resultAddress.getText().toString().length());
                    addressArray = addressSplit.split(" ");
                    if(addressArray.length <= 2)
                        it.putExtra("Location",resultAddress.getText());
                    else if(addressArray.length > 2) {
                        for(int i = 2; i <= addressArray.length; i++) {
                            if(addressArray[i].substring(addressArray[i].length()).equals("동")) {
                                for(int j = 0; j <= i; j++) {
                                    dong = dong + addressArray[j] + " ";
                                }
                                dong = dong.substring(0,dong.length());
                            }
                        }
                    }
                }

                if(itGetTextView.getText().toString().equals("dap"))
                    it.putExtra("sep","dap");
                else if(itGetTextView.getText().toString().equals("quest"))
                    it.putExtra("sep","quest");
                else if(itGetTextView.getText().toString().equals("none"))
                    it.putExtra("sep",itGet.getStringExtra("sep"));
                it.putExtra("Subject","asdf");
                it.putExtra("Subtitle","알라아랄");
                startActivity(it);
                finish();



//                it = new Intent(getApplicationContext(),ChatActivity.class);
//                it.putExtra("Location",resultText.getText());
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

    public void GetQuestionAreaDetail()
    {
        Thread th3 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlString = "http://lim7504.iptime.org:8080/TripTalkWebServer/QuestionAreaDetail.jsp?";
                    urlString += "QUESTION_AREA=" + resultText.getText().toString();

                    jsonString = TomcatConnector(urlString);
                    JSONArray arr = new JSONArray(jsonString);

                    for (int i = 0; i < arr.length(); i++)
                    {
                        JSONObject obj = arr.getJSONObject(i);
                        Log.d("QUESTION_AREA_DETAIL", obj.get("QUESTION_AREA_DETAIL").toString());
                    }
                }catch (Exception e) {
                    successFlag = "Fail";
                }
            }
        });
        th3.start();

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
            if(sep[sep.length-1].charAt(sep[sep.length-1].length() - 1) == '시' ||
                    sep[sep.length-1].charAt(sep[sep.length-1].length() - 1) == '군' ||
                    sep[sep.length-1].charAt(sep[sep.length-1].length() - 1) == '구') {
                queryUrl = "http://api.vworld.kr/req/data?service=data&size=100&request=GetFeature&" +
                        "data=" + emd[0] + "&format=xml&key=" + key3 + filter_etc + str +
                        "&geometry=false";
            }}/* else if(sep[sep.length-1].charAt(sep[sep.length-1].length() - 1) == '읍' ||
                    sep[sep.length-1].charAt(sep[sep.length-1].length() - 1) == '면' ||
                    sep[sep.length-1].charAt(sep[sep.length-1].length() - 1) == '동') {
            queryUrl = "http://api.vworld.kr/req/data?service=data&size=100&request=GetFeature&" +
                    "data=" + emd[0] + "&format=xml&key=" + key3 + filter_etc + str +
                    "&geometry=false";
        }*/

        Log.e("queryUrl",queryUrl);
        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결
            Log.e("test1","test");
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            Log.e("test2","test");
            String tag;

            Log.e("test3",factory.toString());
            xpp.next();
            Log.e("test4","test");
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//태그 이름 얻어오기
                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals(sido[1])) {
                            xpp.next();
                            if(!xpp.getText().equals("세종특별자치시"))
                                arr.add(xpp.getText());
                        }
                        else if(tag.equals(sgg[1])) {
                            xpp.next();
                            arr.add(xpp.getText());
                        }
                        else if(tag.equals(emd[1])) {
                            xpp.next();
                            arr.add(xpp.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //태그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
            Log.e("exception",e.toString());
        }

        buffer.append("파싱 끝\n");

        return buffer.toString();//StringBuffer 문자열 객체 반환

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("마커 좌표");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
            }
        });
        ////////////////////

        // 버튼 이벤트
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String addressSplit = "";
                String addressArray[] = {};
                String dong = "";
                int length = 0;

                String str=editText.getText().toString();
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            20); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                if (addressList.size() == 0){
                    resultAddress.setText("검색 결과가 없습니다.");
                    return;
                }
                Log.d("로그그그그ㅡ그그그극",Integer.toString(addressList.size()));

                mMap.clear();
                for(int size = 0; size < addressList.size(); size++) {
                    System.out.println(addressList.get(size).toString());
                    // 콤마를 기준으로 split
                    String []splitStr = addressList.get(size).toString().split(",");
                    String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                    System.out.println(address);
                    addressSplit = address.substring(5,address.length());
                    addressArray = addressSplit.split(" ");
                    if(addressArray.length <= 2)
                        dong = addressSplit;
                    else if(addressArray.length > 2) {
                        Log.d("길이",Integer.toString(addressArray.length));
                        for(int i = 0; i < addressArray.length; i++) {
                            length = addressArray[i].length() - 1;
                            if(addressArray[i].charAt(length) == '동' || addressArray[i].charAt(length) == '읍' || addressArray[i].charAt(length) == '면'
                                    || addressArray[i].charAt(length) == '리') {
                                Log.d("동작동작","ㅁㄴㅇㄻㄴㅇㄹ");
                                for(int j = 0; j <= i; j++) {
                                    dong = dong + addressArray[j] + " ";
                                    Log.d("동이 동작하니",dong);
                                }
                                dong = dong.substring(0,dong.length() - 1);
                            }
                        }
                        Log.d("동",dong);
                    }
                    resultAddress.setText(dong);

                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                String urlString = "http://lim7504.iptime.org:8080/TripTalkWebServer/QuestionAreaDetail.jsp?";
                                urlString += "QUESTION_AREA=" + resultAddress.getText().toString();

                                jsonString = TomcatConnector(urlString);
                                JSONArray arr = new JSONArray(jsonString);

                                for (int i = 0; i < arr.length(); i++)
                                {
                                    JSONObject obj = arr.getJSONObject(i);
                                    Log.d("QUESTION_AREA_DETAIL", obj.get("QUESTION_AREA_DETAIL").toString());
                                }
                            }catch (Exception e) {
                                successFlag = "Fail";
                            }
                        }
                    });
                    th.start();

                    try {
                        th.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }



                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                    System.out.println(latitude);
                    System.out.println(longitude);

                    // 좌표(위도, 경도) 생성
                    LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    // 마커 생성
                    MarkerOptions mOptions2 = new MarkerOptions();
                    mOptions2.title("search result");
                    mOptions2.snippet(address);
                    mOptions2.position(point);
                    // 마커 추가
                    mMap.addMarker(mOptions2);
                    // 해당 좌표로 화면 줌
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
                }

            }
        });
        ////////////////////

        // Add a marker in Sydney and move the camera
        LatLng sichong = new LatLng(37.5662952, 126.9779451);
        mMap.addMarker(new MarkerOptions().position(sichong).title("서울특별시 시청"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sichong));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sichong,15));

    }
}
