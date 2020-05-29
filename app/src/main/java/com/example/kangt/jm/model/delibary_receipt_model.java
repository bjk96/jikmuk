package com.example.kangt.jm.model;

public class delibary_receipt_model {
    String ordertime;
    String uid;
    String seat;
    String meassage;
  String status;
     Integer deliveryPrice;
     String deliveryUid;
     Integer price;
    String shop;
     String stadium;
     String deliveryComplete;

    public String getDeliveryComplete() {
        return deliveryComplete;
    }

    public void setDeliveryComplete(String deliveryComplete) {
        this.deliveryComplete = deliveryComplete;
    }

    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Integer deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getDeliveryUid() {
        return deliveryUid;
    }

    public void setDeliveryUid(String deliveryUid) {
        this.deliveryUid = deliveryUid;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getMeassage() {
        return meassage;
    }

    public void setMeassage(String meassage) {
        this.meassage = meassage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
