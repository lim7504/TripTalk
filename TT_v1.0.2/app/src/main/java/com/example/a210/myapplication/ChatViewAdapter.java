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

public class ChatViewAdapter extends BaseAdapter {
    private ArrayList<ChatView> chatViewItem = new ArrayList<ChatView>();
    public ChatViewAdapter() {}

    @Override
    public int getCount() {
        return chatViewItem.size();
    }

    @Override
    public Object getItem(int position) {
        return chatViewItem.get(position);
    }

    public ChatView getChatViewItem(int position) {
        return chatViewItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.customlayout,parent,false);
        }

        ImageView chatImage = (ImageView) convertView.findViewById(R.id.chatImageView);
        ImageView chatIcon = (ImageView) convertView.findViewById(R.id.chatIcon);
        TextView nick = (TextView)convertView.findViewById(R.id.chatNick);
        TextView grade = (TextView)convertView.findViewById(R.id.chatGrade);
        TextView subTitle = (TextView)convertView.findViewById(R.id.chatSubTitle);

        ChatView chatItem = chatViewItem.get(position);

        chatImage.setImageDrawable(chatItem.getImage());
        nick.setText(chatItem.getNick());
        grade.setText(chatItem.getGrade());
        subTitle.setText(chatItem.getSubTitle());
        chatIcon.setImageDrawable(chatItem.getIcon());

        return convertView;
    }

    public void addItem(Drawable image, Drawable icon, String nick, String grade, String subtitle, String wait_id) {
        ChatView item = new ChatView();

        item.setImage(image);
        item.setNick(nick);
        item.setGrade(grade);
        item.setSubTitle(subtitle);
        item.setIcon(icon);
        item.setWaitID(wait_id);

        chatViewItem.add(item);
    }
}
