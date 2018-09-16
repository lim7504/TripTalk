package com.example.a210.myapplication;

import android.graphics.drawable.Drawable;

public class ChatView {
    private Drawable image;
    private Drawable icon;
    private String nick;
    private String grade;
    private String subTitle;
    private String wait_id;

    public String getWaitID() {
        return wait_id;
    }

    public void setWaitID(String wait_id) {
        this.wait_id = wait_id;
    }


    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
