package com.example.yeditepesocapp;

public class Constants {

    private static final String ROOT_URL = "http://192.168.1.38:8888/Android/v1/";
    //private static final String ROOT_URL ="http://172.20.10.3:8888/Android/v1/";
    public static final String URL_REGISTER = ROOT_URL+"signupUser.php";
    public static final String URL_LOGIN = ROOT_URL+"userLogin.php";
    public static final String URL_HOME = ROOT_URL+"writeOpinion.php";
    public static final String URL_OPINION = ROOT_URL+"getOpinion.php";
    public static final String URL_CREATE_EVENT = ROOT_URL+"createEvent.php";
    public static final String URL_EVENT = ROOT_URL+"getEvent.php";
    public static final String URL_CREATE_TOPIC = ROOT_URL+"createTopic.php";
    public static final String URL_TOPIC = ROOT_URL+"getTopic.php";
    public static final String URL_ADD_ENTRY = ROOT_URL+"addEntry.php";
    public static final String URL_ENTRY = ROOT_URL+"getEntry.php";
    public static final String URL_USER = ROOT_URL+"getUser.php";
    public static final String URL_ENTRYUSER = ROOT_URL+"getEntrywithUserId.php";
    public static final String URL_OPINIONUSER = ROOT_URL+"getUserOpinionwithUserId.php";
    public static final String URL_USERLIST = ROOT_URL+"getUserList.php";


}
