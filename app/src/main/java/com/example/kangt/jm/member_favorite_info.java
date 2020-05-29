package com.example.kangt.jm;

public class member_favorite_info
{
    public int drawableId;
    public String menuname;
    public String price;
    public String info;
    public int X;


    public member_favorite_info(int drawableId,String menuname ,int X,String price,String info) {
        this.drawableId = drawableId;
        this.menuname= menuname;
        this.X = X;
        this.price = price;
        this.info =info;

    }
}