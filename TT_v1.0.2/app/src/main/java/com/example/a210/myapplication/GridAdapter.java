package com.example.a210.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class GridAdapter extends BaseAdapter {

    Context context;
    int blog_num;
    String[] gridSplit = {};
    Boolean flag = false;
    Integer[] posterId = {
            R.drawable.default_back,R.drawable.default_back,R.drawable.default_back,R.drawable.default_back,R.drawable.default_back
    };

    public GridAdapter(Context context,int blog_num, String[] gridSpit) {
        this.context = context;
        this.blog_num = blog_num;
        this.gridSplit = gridSpit;
    }

    @Override
    public int getCount() {
        return posterId.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder {
        final ImageView img;

        public ViewHolder(ImageView bananaView) {
            this.img = bananaView;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //View v = convertView;
        ViewHolder holder;

        View v;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            v = inflater.inflate(R.layout.gridviewitem, viewGroup, false);
        } else {
            v = (View) convertView;
        }

        //ImageView img = new ImageView(context); //이미지 선언

        if(v != null) {
            ImageView img = (ImageView) v.findViewById(R.id.gridItem);

            Log.e("몇번?", "1");
            img.setPadding(10, 10, 10, 10);    //테두리 설정
            LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 370);

            img.setLayoutParams(imgParam);


            if (gridSplit[1].equals("none_image")) {
                img.setImageResource(posterId[position]);
            } else {
                String imageurl = "http://10.0.2.2:8090/GoEuro" + gridSplit[position + 1];
                Log.e("쀍123", "***" + gridSplit[position + 1] + "***" + position);
                if (gridSplit[position + 1].equals("none_image")) {
                    img.setImageResource(posterId[0]);
                } else {
                    Glide.with(context)
                            .load(imageurl)
                            .into(img);
                }
            }
        }
        return v; //중요.
    }
}
