package com.example.a210.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class privateRoomActivity extends AppCompatActivity{
    ListView chatList;
    Intent it,itGet;
    ChatView adapterItem = new ChatView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privateroom);

        chatList = (ListView)findViewById(R.id.privateRoomListView);
        itGet = getIntent();

        final previateChatViewAdapter adapter = new previateChatViewAdapter(this);

        chatList.setAdapter(adapter);

        /*
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String jsonString;
                        String urlString = "http://10.0.2.2:8080/TripTalkWebServer/PrivateRoomList.jsp?";
                        urlString += "USER_ID=9999";// + UserInfomation.User_ID;
                        jsonString = TomcatConnector(urlString);

                        JSONArray arr = new JSONArray(jsonString);
//arr.length();
                        for (int i = 0; i <5;  i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            String ROOM_ID = obj.getString("ROOM_ID");
                            String QUOSTION_TYPE = obj.getString("QUOSTION_TYPE");
                            String QUOTATION_CONTENS = obj.getString("QUOTATION_CONTENS");
                            String REQUEST_USER_ID = obj.getString("REQUEST_USER_ID");
                            String RECEIVE_USER_ID = obj.getString("RECEIVE_USER_ID");
                            String WAIT_ID = obj.getString("WAIT_ID");

                            String log = ROOM_ID + "," + QUOSTION_TYPE + "," + QUOTATION_CONTENS+ "," + REQUEST_USER_ID+ "," + RECEIVE_USER_ID+ "," + WAIT_ID;

                            Log.d("List-",log);

                            adapter.addItem(getResources().getDrawable(R.drawable.default_face), getResources().getDrawable(R.drawable.newicon),QUOTATION_CONTENS,
                                    QUOSTION_TYPE,  REQUEST_USER_ID, WAIT_ID, ROOM_ID);


                        }
                    } catch (Exception e) {
                    }
                }
            });

            th.start();
*/

        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Boolean bResult = false;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                UserInfomation.Wait_ID = adapter.getChatViewItem(i).getWaitID();

                //if(bResult) {
                Intent it = new Intent(getApplicationContext(), RoomActivity.class);
                Log.i("getItem", adapter.getChatViewItem(i).getNick());
                it.putExtra("nick", adapter.getChatViewItem(i).getNick().toString());
                it.putExtra("grade", adapter.getChatViewItem(i).getGrade().toString());//등급대신 subtitle로 사용중
                it.putExtra("subTitle", adapter.getChatViewItem(i).getSubTitle() .toString()); //subtitle 대신 title로 사용 중
                it.putExtra("create","false");
                startActivity(it);
             //   finish();
            }
        });

        callFragment(1);
        callFragment(2);
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
