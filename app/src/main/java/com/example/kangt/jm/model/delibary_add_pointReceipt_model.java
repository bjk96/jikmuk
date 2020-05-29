package com.example.kangt.jm.model;

public class delibary_add_pointReceipt_model {
    String beforePoint;
    String usingPoint;
    String afterPoint;
    String uid;
    String type;
    Object cashoutTime;
    String accountNumber;
    String accoutnHolder;
    String bank;
    Object ordertime;
    Object timestamp;
    String stadium;
    String shop;
    String oderer_uid;

    public delibary_add_pointReceipt_model(String bank, String accountHolder, String accountNumber, String beforePoint, String usingPoint, String afterPoint, String uid, String type, Object cashoutTime){
        this.beforePoint = beforePoint;
        this.usingPoint = usingPoint;
        this.afterPoint = afterPoint;
        this.uid = uid;
        this.type = type;
        this.cashoutTime = cashoutTime;
        this.accountNumber = accountNumber;
        this.accoutnHolder = accountHolder;
        this.bank = bank;
    }

    public delibary_add_pointReceipt_model(String beforePoint, String usingPoint, String afterPoint, String uid, Object timestamp, Object ordertime, String type, String oderer_uid, String stadium, String shop){
        this.beforePoint = beforePoint;
        this.usingPoint = usingPoint;
        this.afterPoint = afterPoint;
        this.uid = uid;
        this.timestamp = timestamp;
        this.ordertime = ordertime;
        this.type = type;
        this.oderer_uid = oderer_uid;
        this.stadium = stadium;
        this.shop = shop;
    }
    public Object getCashoutTime(){return cashoutTime;}
    public void setCashoutTime(Object cashoutTime){this.cashoutTime=cashoutTime;}
    public Object getOrdertime(){return  ordertime;}
    public void setOrdertime(Object ordertime ){this.ordertime = ordertime;}
    public String getShop(){return  shop;}
    public void setShop(String shop){this.shop = shop;}
    public  String getStadium(){return stadium;}
    public void setStadium(String stadium){this.stadium =stadium;}
    public String getOderer_uid(){return oderer_uid;}
    public void setOderer_uid(){this.oderer_uid = oderer_uid;}
    public String getAccoutnHolder() {
        return accoutnHolder;
    }
    public void setAccoutnHolder(String accoutnHolder) {
        this.accoutnHolder = accoutnHolder;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
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
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Object getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

}
