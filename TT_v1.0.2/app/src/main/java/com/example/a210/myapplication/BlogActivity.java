package com.example.a210.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a210.myapplication.utils.YPhotoPickerIntent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BlogActivity extends AppCompatActivity{
    public final static int REQUEST_CODE = 1;
    private Button button3,blogSendBtn;
    EditText blogTitle,blogContent;
    GridView blogGrid;
    Intent itGet;
    int blog_num = 0;
    Boolean alreadySend = false;
    Boolean imageSending = false;
    LinearLayout titleLin,gridLiner;
    ImageView gridImg1,gridImg2,gridImg3,gridImg4,gridImg5;
    ExampleThread thread;

    public static ArrayList<String> selectedPhotos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);


        //blogGrid = (GridView)findViewById(R.id.blogGrid);
        blogTitle = (EditText)findViewById(R.id.blogTitle);
        blogContent = (EditText)findViewById(R.id.blogContent);
        titleLin = (LinearLayout)findViewById(R.id.titleLin);
        gridLiner = (LinearLayout)findViewById(R.id.gridLiner);
        gridImg1 = (ImageView) findViewById(R.id.gridImg1);
        gridImg2 = (ImageView) findViewById(R.id.gridImg2);
        gridImg3 = (ImageView) findViewById(R.id.gridImg3);
        gridImg4 = (ImageView) findViewById(R.id.gridImg4);
        gridImg5 = (ImageView) findViewById(R.id.gridImg5);

        itGet = getIntent();
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if(permissionCheck1 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET},1);

        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        if(permissionCheck2 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_NETWORK_STATE},1);

        try {
            if(itGet.getStringExtra("flag") == null || !itGet.getStringExtra("flag").equals("true")) {
                String result = new BlogIn().execute().get();
                result = result.replace(" ", "");
                blog_num = Integer.parseInt(result);
            } else {
                blog_num = Integer.parseInt(itGet.getStringExtra("bNum"));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        callFragment(1);
        callFragment(2);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String resultSplit[] = {};
                try {
                    String result = new BlogSearch().execute(Integer.toString(blog_num)).get();
                    result = result.substring(0,result.length());
                    Log.e("ressssssssssssss", "왜 안돼 : " + result);
                    resultSplit = result.split("/123/");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (resultSplit[1].equals("none_image")) {
                    gridImg1.setImageResource(R.drawable.default_back);
                    gridImg2.setImageResource(R.drawable.default_back);
                    gridImg3.setImageResource(R.drawable.default_back);
                    gridImg4.setImageResource(R.drawable.default_back);
                    gridImg5.setImageResource(R.drawable.default_back);
                } else {
                    for (int i = 0; i < resultSplit.length-1; i++) {
                        String imageurl = "http://lim7504.iptime.org:8080/TripTalkWebServer" + resultSplit[i + 1];
                        //Log.e("쀍123", "***" + gridSplit[position + 1] + "***" + position);
                        if (resultSplit[i + 1].equals("none_image")) {
                            if(i == 1)
                                gridImg2.setImageResource(R.drawable.default_back);
                            else if(i == 2)
                                gridImg3.setImageResource(R.drawable.default_back);
                            else if(i == 3)
                                gridImg4.setImageResource(R.drawable.default_back);
                            else if(i == 4)
                                gridImg5.setImageResource(R.drawable.default_back);
                        } else {
                            Log.e("동작안해?",imageurl);

                            if(i == 0)
                                Glide.with(getApplicationContext())
                                        .load(imageurl)
                                        .into(gridImg1);
                            else if(i == 1)
                                Glide.with(getApplicationContext())
                                        .load(imageurl)
                                        .into(gridImg2);
                            else if(i == 2)
                                Glide.with(getApplicationContext())
                                        .load(imageurl)
                                        .into(gridImg3);
                            else if(i == 3)
                                Glide.with(getApplicationContext())
                                        .load(imageurl)
                                        .into(gridImg4);
                            else if(i == 4)
                                Glide.with(getApplicationContext())
                                        .load(imageurl)
                                        .into(gridImg5);
                        }
                    }
                }
            }
        }, 1000);



        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YPhotoPickerIntent intent = new YPhotoPickerIntent(BlogActivity.this);
                intent.setMaxSelectCount(6);
                intent.setShowCamera(false);
                intent.setShowGif(false);
                intent.setSelectCheckBox(true);
                intent.setMaxGrideItemCount(3);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        blogSendBtn = (Button)findViewById(R.id.blogSendBtn);

        blogSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(blogTitle.getText() == null || blogTitle.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"제목을 입력하세요",Toast.LENGTH_LONG).show();
                    return;
                }
                if(blogContent.getText() == null || blogContent.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"내용을 입력하세요",Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    Log.e("값 확인 : ",blogTitle.getText().toString() + "///" + blogContent.getText().toString() + "///" + blog_num);
                    String result = new BlogInsert().execute(Integer.toString(blog_num),blogTitle.getText().toString(),
                            blogContent.getText().toString(),UserInfomation.User_ID).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                alreadySend = true;
                Intent it = new Intent(getApplicationContext(),MainActivity.class);
                it.putExtra("sep","dap");
                startActivity(it);
                finish();
            }
        });

        final View activityRootView = findViewById(R.id.blogLayout);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                int height = activityRootView.getRootView().getHeight();
                int whatHeight = activityRootView.getHeight();
                int result = height - whatHeight;
                activityRootView.getWindowVisibleDisplayFrame(r);
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                if (result > dpToPx(200)) {
                    gridLiner.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                    param.addRule(RelativeLayout.BELOW,R.id.header);
                    titleLin.setLayoutParams(param);
                }else{
                    gridLiner.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    param.addRule(RelativeLayout.BELOW,R.id.button3);
                    titleLin.setLayoutParams(param);
                }
            }
        });
    }

    private class ExampleThread extends Thread {
        private int threadNum = 0;

        public ExampleThread() throws InterruptedException {
        // 초기화 작업
            this.threadNum = threadNum;
        }
        public void run() {
            Log.i("시작된 스레드", "우어어어엉");
            try {

                Thread.sleep(1000);
            // 1초간 Thread를 잠재운다
            } catch (InterruptedException e) {
            e.printStackTrace();
            } Log.e("종료된 스레드", "잉잉잉");
        }
    }

    public float dpToPx(float val) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, metrics);
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
    public void onDestroy() {
        super.onDestroy();
        Log.e("넌 뭐야","플래그 : " + itGet.getStringExtra("flag") + "////" + "얼래디 : " + alreadySend);
        if(imageSending) {

            Log.e("이미지 전송한다ㅏㅏㅏㅏㅏㅏ", "ㅎㅇㅎㅇ");
        } else if(itGet.getStringExtra("flag") == null) {
            Log.e("널이다아아아아", "ㅎㅇㅎㅇ");
            try {
                String result = new BlogOut().execute(Integer.toString(blog_num)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else if (itGet.getStringExtra("flag").equals("true") && alreadySend == true) {

            Log.e("안사라진다아아아아ㅏ", "ㅎㅇㅎㅇ");
        } else {
            Log.e("지운다아아아", "ㅎㅇㅎㅇ");
            try {
                String result = new BlogOut().execute(Integer.toString(blog_num)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }



            try {
                String a = new BlogImageReset().execute(Integer.toString(blog_num)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            for(int i = 0; i < selectedPhotos.size(); i++) {
                String split[] = selectedPhotos.get(i).toString().split("/");

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("blogId",Integer.toString(blog_num))
                        .addFormDataPart("imageNum", Integer.toString(i+1))
                        .addFormDataPart("image", split[split.length-1], RequestBody.create(MultipartBody.FORM, new File(selectedPhotos.get(i).toString())))
                        .build();

                Request request = new Request.Builder()
//                .url(getString(R.string.url_MphotoUpload))
                        .url(getString(R.string.url_MphotoUpload))
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        Log.d("ERROR", "err");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        Log.d("HERE", "onResponse: " + response.body().string());


                    }
                });
            }
            selectedPhotos.clear();
            // start image viewr
            Intent startActivity = new Intent(this , BlogActivity.class);
            startActivity.putStringArrayListExtra("photos" , selectedPhotos);
            startActivity.putExtra("sep","dap");
            startActivity.putExtra("bNum",Integer.toString(blog_num));
            startActivity.putExtra("flag","true");
            imageSending = true;
            startActivity(startActivity);
            finish();
        }
    }

    class BlogIn extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(getString(R.string.url_MphotoUpload));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "type=blogIn";
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }

    class BlogOut extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(getString(R.string.url_MphotoUpload));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "type=blogOut"
                        + "&blogId="      + strings[0];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }

    /*class BlogSearch extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(getString(R.string.url_MphotoUpload));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "type=BlogSearch"
                        + "&blogId="      + strings[0];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }*/

    class BlogInsert extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(getString(R.string.url_MphotoUpload));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "type=blogInsert"
                        + "&blogId="            + strings[0]
                        + "&blogTitle="         + strings[1]
                        + "&blogContent="       + strings[2]
                        + "&blogWriter="        + strings[3];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }

    class BlogSearch extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(getString(R.string.url_MphotoUpload));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "type=blogSearch"
                        + "&blogId="      + strings[0];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }

    class BlogImageReset extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(getString(R.string.url_MphotoUpload));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "type=blogImageReset"
                        + "&blogId="      + strings[0];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }
}
