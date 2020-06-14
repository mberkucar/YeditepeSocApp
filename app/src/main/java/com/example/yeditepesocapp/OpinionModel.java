package com.example.yeditepesocapp;

public class OpinionModel {

    private String user_name, user_surname, opinion_body;
    private String date;

    //for search
    private String user_id,user_mail;

    public OpinionModel() {
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_surname() {
        return user_surname;
    }

    public void setUser_surname(String user_surname) {
        this.user_surname = user_surname;
    }

    public String getOpinion_body() {
        return opinion_body;
    }

    public void setOpinion_body(String opinion_body) {
        this.opinion_body = opinion_body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }
}
