package com.example.kangt.jm.model;

public class point_receipt_model {
    String ordertime;
    String timestamp;   //point_receipt_delivery에서 배달완료시간
    String uid;
    String oderer_uid;
    String beforePoint;
    String usingPoint;
    String afterPoint;
    String type;
    String shop;
    String stadium;
    String cashoutTime;

    public String getCashoutTime() {
        return cashoutTime;
    }

    public void setCashoutTime(String cashoutTime) {
        this.cashoutTime = cashoutTime;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getOderer_uid() {
        return oderer_uid;
    }

    public void setOderer_uid(String oderer_uid) {
        this.oderer_uid = oderer_uid;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
