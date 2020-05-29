package com.example.kangt.jm.model;

public class NotificationModel {
    public String to ;
    public Notification notificationModel = new Notification();
    public Data data = new Data();

    public static class Notification {
        public String title;
        public String text;
    }
    public static  class  Data {
        public String title;
        public String text;
    }
}
