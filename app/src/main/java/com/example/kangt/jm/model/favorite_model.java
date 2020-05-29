package com.example.kangt.jm.model;

public class favorite_model {
    public String uid;
    public String store;
    public String menu_name;
    public Integer menu_price;
    public String imgUrl;

    public favorite_model(String uid , String store, String menu_name, Integer menu_price, String imgUrl){
        this.uid = uid;
        this.store = store;
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.imgUrl = imgUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public Integer getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(Integer menu_price) {
        this.menu_price = menu_price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
