package com.example.a210.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BigdataViewAdapter extends BaseAdapter{
    private ArrayList<mainView> mainViewItem = new ArrayList<mainView>();

    public BigdataViewAdapter() {}

    @Override
    public int getCount() {
        return mainViewItem.size();
    }

    @Override
    public Object getItem(int position) {
        return mainViewItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bigdataview,parent, false);
        }

        ImageView Image = (ImageView) convertView.findViewById(R.id.bigdataImageView);
        TextView title = (TextView)convertView.findViewById(R.id.bigdataTitle);
        TextView user = (TextView)convertView.findViewById(R.id.bigdataUser);
        TextView subTitle = (TextView)convertView.findViewById(R.id.bigdataSubTitle);

        mainView mainItem = mainViewItem.get(position);

        Image.setImageDrawable(mainItem.getImage());
        title.setText(mainItem.getTitle());
        user.setText(mainItem.getUser());
        subTitle.setText(mainItem.getSubTitle());

        return convertView;
    }

    public void addItem(Drawable icon, String title, String user, String subTitle) {
        mainView item = new mainView();

        item.setImage(icon);
        item.setTitle(title);
        item.setUser(user);
        item.setSubTitle(subTitle);

        mainViewItem.add(item);
    }
}
