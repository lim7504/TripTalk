package com.example.a210.myapplication;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*
ChattingAysnc 이 클래스의 용도는 채팅 메세지를 입력 했을 때 JSP로 보내는 용도임
appendMessage 메소드는 상대방의 메세지를 왼쪽 위치 표시해주는 용도임
 LocalBroadcastManager.getInstance(this).registerReceiver(mHandler,new IntentFilter("FCM-MESSAGE")); 을 통해 로컬 브로드캐스트 리시버를 해당 액티비티에 등록함(키 값은 FCM-MESSAGE)
 FirebaseMessagingService 클래스에서도 동일한 키 값을 사용함 여러 브로드 캐스트를 구별하기 위해서 키 값을 사용함


 실제 브로드캐스트 리시버가 동작하는 부분임
 데이터를 주고 받을 때 액티비티처럼 인텐트를 사용함
 *내부에서 UI처리 가능
     private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String strMsg = intent.getStringExtra("CHAT_MESSAGE");
            strMsg = strMsg.substring(13,strMsg.length());//통신 프로토콜 CHAT_MESSAGE:를 제거하기 위함
            appendMessage(strMsg);//S

        }
    };

 */
public class RoomActivity extends AppCompatActivity {
    private String[] chatProtocol = {"WAIT_ID", "MESSAGE_ID", "CHAT_SENDER_ID", "CHAT_MESSAGE", "CHAR_IMAGE", "CREATE_DATE", "MESSAGE_TYPE"};
    private String[] chooseProtocol = {"USER_ID", "ROOM_ID", "QUOSTION_TYPE", "QUOTATION_CONTENS", "CREATE_DATE"};
    //JS
    Intent itRoom;
    TextView setNick, setGrade;
    Button chattingBtn, exit;
    EditText chatting;
    ScrollView chatScroll;
    ChatView getIntentItem;
    String strSubTitle;
    public OutputStreamWriter osw;
    HttpURLConnection conn;
    private boolean bStopThread = false;
    Thread catchThread = null;
    ImageButton btnFinalSelect = null;
    String sCreate = "False";
    String sRequestUser = "";

    @Override
    protected void onStop() {
        bStopThread = true;
        super.onStop();
    }

    @Override
    protected void onStart() {
        if (catchThread == null) {
            //     catchThread = new Thread(new BackgroundThread());
            //   catchThread.start();
        }

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler, new IntentFilter("FCM-MESSAGE"));
        itRoom = getIntent();
        //String

        //   if (UserInfomation.User_NickName.isEmpty() || UserInfomation.User_NickName.equals("")) {
        //UserInfomation.User_NickName = itRoom.getStringExtra("nick");


        //    }
        /*
        getIntentItem.setNick(itRoom.getStringExtra("nick"));
        getIntentItem.setGrade(itRoom.getStringExtra("grade"));
        */
        setNick = (TextView) findViewById(R.id.setNick);
        setGrade = (TextView) findViewById(R.id.setGrade);
        chattingBtn = (Button) findViewById(R.id.chattingBtn);
        exit = (Button) findViewById(R.id.exit);
        chatting = (EditText) findViewById(R.id.chatting);
        chatScroll = (ScrollView) findViewById(R.id.chatScroll);
        btnFinalSelect = (ImageButton) findViewById(R.id.finalSelect);

        setNick.setText(UserInfomation.User_ID);

        sRequestUser = itRoom.getStringExtra("nick");
        setGrade.setText(sRequestUser+"("+itRoom.getStringExtra("subTitle")+")");
        strSubTitle = itRoom.getStringExtra("grade");
        sCreate = itRoom.getStringExtra("create");

        final View activityRootView = findViewById(R.id.roomLayout);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(r);

                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 100) {
                    chatScroll.fullScroll(ScrollView.FOCUS_DOWN);
                } else {
                    //키보드 숨김
                }
            }
        });

        if (sCreate.compareTo("True") != 0) {
            tryGetHisotry();
        }
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExitChatRoomAsync exitChatRoomAsync = new ExitChatRoomAsync();
                try {
                    String strResult = exitChatRoomAsync.execute(UserInfomation.Wait_ID, UserInfomation.User_NickName, UserInfomation.User_ID).get();

                    if(strResult.equals("ACK")) {
                        Toast.makeText(getApplicationContext(), "채팅 방에서 나가셨습니다.", Toast.LENGTH_SHORT).show();

                        ChattingAysnc chattingAysnc = new ChattingAysnc();
                        String strMsg = UserInfomation.User_ID + " 님이 나가셨습니다.";
                        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);
                        Date currentTime = new Date();
                        String mTime = mSimpleDateFormat.format(currentTime);


                        strResult = chattingAysnc.execute(UserInfomation.Wait_ID, UserInfomation.User_ID, strMsg, mTime, "4").get();

                    }else
                    {
                        Toast.makeText(getApplicationContext(), "채팅 방에서 나가기 실패 했습니다!", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }catch(Exception e)
                {
                    finish();
                    e.printStackTrace();
                }

            }
        });
        chattingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mychat n_layout = new mychat(getApplicationContext());
                if (!chatting.getText().toString().equals("")) {
                    LinearLayout chattingView = (LinearLayout) findViewById(R.id.chattingView);
                    TextView topTV1 = new TextView(RoomActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(200, 10, 20, 10);
                    params.gravity = Gravity.RIGHT;
                    topTV1.setLayoutParams(params);
                    topTV1.setBackgroundColor(Color.parseColor("#F8EB04"));
                    topTV1.setPadding(12, 12, 12, 12);
                    topTV1.setTextColor(Color.parseColor("#000000"));
                    topTV1.setTextSize(20);

                    ChattingAysnc chattingAysnc = new ChattingAysnc();

                    try {
                        String strRoomID = UserInfomation.Wait_ID;
                        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);
                        Date currentTime = new Date();
                        String mTime = mSimpleDateFormat.format(currentTime);

                        String strMsg = chatting.getText().toString();
                        String strResult = chattingAysnc.execute(strRoomID, UserInfomation.User_ID, strMsg, mTime, "1").get();

                        topTV1.setTag(1);

                        if (strResult.equals("ACK")) {
                            topTV1.setText(strMsg);
                            chattingView.addView(topTV1);
                        } else {
                            topTV1.setText("전송 실패!");
                            chattingView.addView(topTV1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                chatting.setText("");
            }
        });

        btnFinalSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(setNick.getText().toString().compareTo(sRequestUser) == 0)
                {
                    //질문자는 제외 시키기
                    return ;
                }
                Intent it = new Intent(getApplicationContext(), MapsActivity.class);
                it.putExtra("search address","");
                startActivityForResult(it,0);
            }
        });

    }

    private void tryGetHisotry()
    {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    setNick.setText(UserInfomation.User_ID);
                 //   setGrade.setText(itRoom.getStringExtra("subTitle"));
                 //   strSubTitle = itRoom.getStringExtra("grade");

                    String jsonString;
                    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss", Locale.KOREA );
                    Date currentTime = new Date ();
                    String mTime = mSimpleDateFormat.format ( currentTime );

                    String urlString = getString(R.string.url_Server)+"/ChatRoomManager.jsp?";
                    urlString += "&ROOM_ID=" + UserInfomation.Wait_ID;
                    urlString += "&REQUEST_USER_ID=" + setNick.getText();
                    urlString += "&RECEIVE_USER_ID=" + UserInfomation.User_ID;
                    urlString += "&ROOM_NAME=" +  setGrade.getText();
                    //     urlString += "&QUOSTION_TYPE=" + setGrade.getText();

                    jsonString = TomcatConnector(urlString);
                    JSONArray arr = new JSONArray(jsonString);
                    Boolean bAlreadSelect = false;

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        String IS_CHOOSE = obj.getString("IS_CHOOSE");

                        if(IS_CHOOSE.compareTo("9") == 0)
                        {
                            bAlreadSelect = true;
                        }
                    }
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);

                        String ROOM_ID = obj.getString("ROOM_ID");
                        String MESSEGE_ID = obj.getString("MESSEGE_ID");
                        String CHAT_SENDER_ID = obj.getString("CHAT_SENDER_ID");
                        String CHAT_MESSAGE = obj.getString("CHAT_MESSAGE");
                        String IS_CHOOSE = obj.getString("IS_CHOOSE");

                        Log.e("jinsu111",obj.toString());

                        if(bAlreadSelect)
                        {
                            if(IS_CHOOSE.compareTo("3") == 0) {
                                IS_CHOOSE = "4";
                            }
                        }

                        InsertChatMessage(CHAT_SENDER_ID,CHAT_MESSAGE,IS_CHOOSE);
                    }
                }
                catch (Exception e)
                {
                }
            }
        });
        th.start();

    }
    @Override
    protected void onPause() {


        super.onPause();
    }
/*기존  기능 주석처리
    public void appendMessage(String Message)
    {
        LinearLayout chattingView = (LinearLayout) findViewById(R.id.chattingView);
        TextView topV2 = new TextView(RoomActivity.this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins(20, 10, 200, 10);
        params2.gravity = Gravity.LEFT;
        topV2.setLayoutParams(params2);
        topV2.setBackgroundColor(Color.parseColor("#FFFFFF"));
        topV2.setPadding(12, 12, 12, 12);
        topV2.setTextColor(Color.parseColor("#000000"));
        topV2.setTextSize(20);
        topV2.setText(Message);

        chattingView.addView(topV2);
    }
    */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String addressGet = "";

        switch (resultCode) {
            case 100:
                addressGet = data.getStringExtra("address");
                break;
            default:
                addressGet = "error";
                break;
        }
        Log.d("Final Select Log:", addressGet);
        if (addressGet.equals("error")) {
            return;
        }

        LinearLayout chattingView = (LinearLayout) findViewById(R.id.chattingView);
        TextView topTV1 = new TextView(RoomActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(200, 10, 20, 10);
        params.gravity = Gravity.RIGHT;
        topTV1.setLayoutParams(params);
        //topTV1.setBackgroundColor(Color.parseColor("#F8EB04"));
        topTV1.setBackgroundColor(Color.YELLOW);
        topTV1.setPadding(12, 12, 12, 12);
        topTV1.setTextColor(Color.parseColor("#000000"));
        topTV1.setTextSize(20);
        topTV1.setTag(1);
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);
        Date currentTime = new Date();
        String mTime = mSimpleDateFormat.format(currentTime);

        ChattingAysnc chattingAysnc = new ChattingAysnc();
        try {
            addressGet = "추천 장소(주소) : " + addressGet;
            String strResult = chattingAysnc.execute(UserInfomation.Wait_ID, UserInfomation.User_ID, addressGet, mTime, "3").get();

            if (strResult.equals("ACK")) {
                topTV1.setText(addressGet);
                chattingView.addView(topTV1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendMessage(String MessgaeType, String Message) {
        LinearLayout chattingView = (LinearLayout) findViewById(R.id.chattingView);
        final TextView topV2 = new TextView(RoomActivity.this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins(20, 10, 200, 10);
        params2.gravity = Gravity.LEFT;
        topV2.setLayoutParams(params2);
        topV2.setPadding(12, 12, 12, 12);
        topV2.setTextColor(Color.parseColor("#000000"));
        topV2.setTextSize(20);
        topV2.setTag(Integer.parseInt(MessgaeType));
        topV2.setText(Message);

        if((int)topV2.getTag() != 3)
        {
            if((int)topV2.getTag() != 9) {
                topV2.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            else {
                topV2.setBackgroundColor(Color.RED);
            }
        }else
        {
            topV2.setBackgroundColor(Color.GREEN);
        }
        appendEvent(topV2);
        chattingView.addView(topV2);
    }

    void appendEvent(final TextView topV2)
    {
        topV2.setOnClickListener(new View.OnClickListener() {
            //JS
            @Override
            public void onClick(View view) {
                final TextView tv = (TextView) view;

                if ((int) tv.getTag() != 3) {
                    if ((int) tv.getTag() == 9 || (int) tv.getTag() == 4) {
                        Toast.makeText(RoomActivity.this, "이미 채택 하셨습니다!", Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("tag is not 3");
                    return;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(RoomActivity.this);
                builder.setTitle("채택하시겠습니까?");

                List<String> SelectAnswer = new ArrayList<>();

                SelectAnswer.add("예");
                SelectAnswer.add("아니오");
                SelectAnswer.add("위치 보기");
                final CharSequence[] items = SelectAnswer.toArray(new String[SelectAnswer.size()]);


                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pos) {

                        String sTemp = topV2.getText().toString();

                        if (pos == 0) {
                            String strRoomID = "1";
                            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);
                            Date currentTime = new Date();
                            String mTime = mSimpleDateFormat.format(currentTime);

                            try {
                                String QUOSTION_TYPE = strSubTitle;
                                String QUOTATION_CONTENS = setGrade.getText().toString();

                                ChooseAysnc chooseAysnc = new ChooseAysnc();
                                String strResult = chooseAysnc.execute(UserInfomation.User_ID, UserInfomation.Wait_ID, QUOSTION_TYPE, QUOTATION_CONTENS, mTime).get();

                                if (strResult.equals("ACK")) {
                                 //   topV2.setBackgroundColor(Color.RED);

                                    ChattingAysnc chattingAysnc = new ChattingAysnc();

                                    try {

                                        sTemp = sTemp +" 채택!.";
                                        mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);
                                        currentTime = new Date();
                                        mTime = mSimpleDateFormat.format(currentTime);

                                        strResult = chattingAysnc.execute(UserInfomation.Wait_ID, UserInfomation.User_ID, sTemp, mTime, "9").get();
                                        if(strResult.compareTo("ACK") ==0)
                                        {
                                            InsertChatMessage(UserInfomation.User_ID,sTemp,"9");
                                        }
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }

                                    Toast.makeText(RoomActivity.this, "채택이 완료 되었습니다.", Toast.LENGTH_SHORT).show();

                                    tv.setTag(9);//재등록 방지용
                                } else if (strResult.equals("WNACK")) {
                                    Toast.makeText(RoomActivity.this, "답변자는 채택을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RoomActivity.this, "채택 등록이 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else if(pos == 2)
                        {
                            Intent intent2 = new Intent(getApplicationContext(),MapsActivity.class);
                            sTemp= sTemp.substring(12,sTemp.length());
                            intent2.putExtra("search address",sTemp);
                            startActivity(intent2);

                        }
                    }
                });

                builder.show();
            }
        });
    }
    public class ChattingAysnc extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://lim7504.iptime.org:8080/TripTalkWebServer/ChatMsgtoDB.jsp");

               // URL url = new URL("http://10.0.2.2:8080/TripTalkWebServer/ChatMsgtoDB.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                String sendMsg = chatProtocol[0] + "=" + strings[0] + "&" + chatProtocol[2] + "=" + strings[1] + "&" + chatProtocol[3] + "=" + strings[2]
                        + "&" + chatProtocol[5] + "=" + strings[3] + "&" + chatProtocol[6] + "=" + strings[4];

                Log.d("Protocol :",sendMsg);
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    String receiveMsg = buffer.toString();

                    return receiveMsg;
                } else {

                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                    return "Fail";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Fail";
        }
    }

    public class ChooseAysnc extends AsyncTask<String, String, String> {
        //JS
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://lim7504.iptime.org:8080/TripTalkWebServer/ChoosetoDB.jsp");
               // URL url = new URL("http://10.0.2.2:8080/TripTalkWebServer/ChoosetoDB.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                String sendMsg = chooseProtocol[0] + "=" + strings[0] + "&" + chooseProtocol[1] + "=" + strings[1] + "&" + chooseProtocol[2] + "=" + strings[2]
                        + "&" + chooseProtocol[3] + "=" + strings[3] + "&" + chooseProtocol[4] + "=" + strings[4];

                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    String receiveMsg = buffer.toString();

                    return receiveMsg;
                } else {

                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                    return "Fail";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Fail";
        }

    }


    private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //JS
            String strMsg = intent.getStringExtra("CHAT_MESSAGE");
            String strMsgType = strMsg.substring(13, 14);
            String strMsgText = strMsg.substring(28, strMsg.length());

            appendMessage(strMsgType, strMsgText);
            /*
            String strMsg = intent.getStringExtra("CHAT_MESSAGE");
            strMsg = strMsg.substring(13,strMsg.length());
            appendMessage(strMsg);
            */

        }
    };

    public String TomcatConnector(String urlString) {

        StringBuilder html = new StringBuilder();
        try {
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                conn.setConnectTimeout(3000);
                conn.setUseCaches(false);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

                    while (true) {
                        String line = br.readLine();
                        if (line == null) break;
                        html.append(line + "\n");

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

    public class ExitChatRoomAsync extends AsyncTask<String, String, String> {
        //JS
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://lim7504.iptime.org:8080/TripTalkWebServer/ChatRoomManagerforRemove.jsp");
                //URL url = new URL("http://10.0.2.2:8080/TripTalkWebServer/ChatRoomManagerforRemove.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                String sendMsg = "WATING_ID=" + strings[0] + "&" + "QUESTION_ID=" + strings[1] + "&" + "USER_ID=" + strings[2];

                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    String receiveMsg = buffer.toString();

                    return receiveMsg;
                } else {

                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                    return "Fail";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Fail";
        }

    }

    private void InsertChatMessage(String CHAT_SENDER_ID,String CHAT_MESSAGE,String IS_CHOOSE)
    {
        if(UserInfomation.User_ID.compareTo(CHAT_SENDER_ID) == 0)
        {
            LinearLayout chattingView = (LinearLayout) findViewById(R.id.chattingView);
            TextView topTV1 = new TextView(RoomActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(200, 10, 20, 10);
            params.gravity = Gravity.RIGHT;
            topTV1.setLayoutParams(params);
            topTV1.setPadding(12, 12, 12, 12);
            topTV1.setTextColor(Color.parseColor("#000000"));
            topTV1.setTextSize(20);

            if(IS_CHOOSE.equals("9"))
            {
                topTV1.setBackgroundColor(Color.RED);
                topTV1.setTag(9);
                topTV1.setText(CHAT_MESSAGE);
                chattingView.addView(topTV1);
            }else
            {

                topTV1.setBackgroundColor(Color.YELLOW);
                topTV1.setTag(1);
                topTV1.setText(CHAT_MESSAGE);
                chattingView.addView(topTV1);
            }
        }else
        {

            LinearLayout chattingView = (LinearLayout) findViewById(R.id.chattingView);
            final TextView topV2 = new TextView(RoomActivity.this);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params2.setMargins(20, 10, 200, 10);
            params2.gravity = Gravity.LEFT;
            topV2.setLayoutParams(params2);
            topV2.setPadding(12, 12, 12, 12);
            topV2.setTextSize(20);

            if(IS_CHOOSE.equals("9"))
            {
                topV2.setBackgroundColor(Color.RED);
                topV2.setTag(9);
                topV2.setText(CHAT_MESSAGE);
            }else
            {


                if(IS_CHOOSE.compareTo("3") == 0 || IS_CHOOSE.compareTo("4") == 0) {
                    topV2.setTag(Integer.parseInt(IS_CHOOSE));
                    topV2.setBackgroundColor(Color.GREEN);
                }else
                {
                    topV2.setTag(2);
                    topV2.setBackgroundColor(Color.WHITE);
                }
                appendEvent(topV2);
                topV2.setText(CHAT_MESSAGE);
            }

            chattingView.addView(topV2);
        }
    }
}