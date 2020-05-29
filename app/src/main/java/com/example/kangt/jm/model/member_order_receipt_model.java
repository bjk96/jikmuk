package com.example.kangt.jm.model;

import android.widget.ImageView;

import com.example.kangt.jm.R;

public class member_order_receipt_model {
    ImageView pic_menu;
    String ordertime;
    String meassage;
    String uid;
    Integer price;
    String status;
    String stadium;
    String shop;

    String deliveryUid;
    String deliveryComplete;



    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }


    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getDeliveryUid() {
        return deliveryUid;
    }

    public void setDeliveryUid(String deliveryUid) {
        this.deliveryUid = deliveryUid;
    }

    public String getDeliveryComplete() {
        return deliveryComplete;
    }

    public void setDeliveryComplete(String deliveryComplete) {
        this.deliveryComplete = deliveryComplete;
    }

    public String getMeassage() {
        return meassage;
    }

    public void setMeassage(String meassage) {
        this.meassage = meassage;
    }

    public String getUid() {  return uid;    }

    public void setUid(String uid) {  this.uid = uid;    }

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

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }
}
