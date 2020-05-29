package com.example.kangt.jm.model;

public class Message {
    public String uid;
    public String meassage;
    public Object ordertime;
    public String seat;
    public String stadium;
    public String status;
    public String deliveryUid;
    public String shop;
    public Integer deliveryPrice;
    public Integer price;

    public Message(String uid , String meassage , String ordertime, String seat, String stadium, String status, String shop, Integer deliveryPrice, Integer price){
        this.meassage = meassage;
        this.uid = uid;
        this.ordertime = ordertime;
        this.seat = seat;
        this.stadium = stadium;
        this.status = status;
        this.shop = shop;
        this.deliveryPrice = deliveryPrice;
        this.price = price;
    }

    public Message(String deliveryUid){
        this.deliveryUid = deliveryUid;
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

    public Object getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Object ordertime) {
        this.ordertime = ordertime;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Integer deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
