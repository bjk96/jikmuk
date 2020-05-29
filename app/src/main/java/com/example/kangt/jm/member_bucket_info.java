package com.example.kangt.jm;

public class member_bucket_info {
    public int drawableId;
    public String menuname;
    public String price_set;
    public String info;
    public int X;


    public member_bucket_info(int drawableId, String menuname , int X, String price, String info) {
        this.drawableId = drawableId;
        this.menuname= menuname;
        this.X = X;
        this.price_set = price;
        this.info =info;

    }
}
