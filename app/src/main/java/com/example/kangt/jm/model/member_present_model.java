package com.example.kangt.jm.model;

public class member_present_model {
    String uid;
    Object ordertime;
    String beforePoint;
    String usingPoint;
    String afterPoint;
    String type;
    String shop;
    String stadium;
    String receiver;

    public member_present_model(String timestamp, String uid, String beforePoint, String usingPoint, String afterPoint, String receiver, String type){
        this.uid = uid;
        this.ordertime = timestamp;
        this.beforePoint = beforePoint;
        this.usingPoint = usingPoint;
        this.afterPoint = afterPoint;
        this.receiver = receiver;
        this.type = type;
    }

    public member_present_model(String time, String uid, String beforePoint, String usingPoint, String afterPoint, String type){
        this.ordertime = time;
        this.uid = uid;
        this.beforePoint = beforePoint;
        this.usingPoint = usingPoint;
        this.afterPoint = afterPoint;
        this.type = type;
    }

    public member_present_model(String timestamp, String uid, String beforePoint, String usingPoint, String afterPoint, String type, String shop, String stadium){
        this.uid = uid;
        this.ordertime = timestamp;
        this.beforePoint = beforePoint;
        this.usingPoint = usingPoint;
        this.afterPoint = afterPoint;
        this.type = type;
        this.shop = shop;
        this.stadium = stadium;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Object getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Object ordertime) {
        this.ordertime = ordertime;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getBeforePoint() {
        return beforePoint;
    }

    public void setBeforePoint(String beforePoint) {
        this.beforePoint = beforePoint;
    }

    public String getUsingPoint() {
        return usingPoint;
    }

    public void setUsingPoint(String usingPoint) {
        this.usingPoint = usingPoint;
    }

    public String getAfterPoint() {
        return afterPoint;
    }

    public void setAfterPoint(String afterPoint) {
        this.afterPoint = afterPoint;
    }
}
