package com.example.kangt.jm.model;

public class food_receipt_model {
    String ordertime;
    String stadium;
    String seat;
    String shop;
    String uid;
    String meassage;
    Integer price;
    String status;

    String deliveryUid;
    String deliveryComplete;

    public String getDeliveryUid() {
        return deliveryUid;
    }

    public void setDeliveryUid(String deliveryUid) {
        this.deliveryUid = deliveryUid;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMeassage() {
        return meassage;
    }

    public void setMeassage(String meassage) {
        this.meassage = meassage;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getDeliveryComplete() {
        return deliveryComplete;
    }

    public void setDeliveryComplete(String deliveryComplete) {
        this.deliveryComplete = deliveryComplete;
    }
}
