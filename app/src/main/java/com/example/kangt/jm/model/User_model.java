package com.example.kangt.jm.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User_model {
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userPhone;
    private String userPoint;
    public String pushToken;
    public String userseat;
    public Map<String, Boolean> stars = new HashMap<>();


    public User_model (){}
    public User_model (String userPoint){
        this.userPoint = userPoint;
    }
    public User_model (String userName, String userEmail){
        this.userName = userName;
        this.userEmail = userEmail;
    }
    public User_model (String userEmail , String userName , String userPassword , String userPhone , String userPoint){
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userPoint = userPoint;
    }
    @Exclude
    public HashMap<String, Object> tomap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userPoint" , userPoint);
        return result;
    }

    public void setUserName (String userName){
        this.userName=userName;
    }
    public void setUserPoint (String userPoint){this.userPoint = userPoint;}
    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }
    public void setUserPassword(String userPassword){
        this.userPassword = userPassword;
    }
    public void setUserPhone(String userPhone){
        this.userPhone = userPhone;
    }
    public  String getUserName(){
        return userName;
    }
    public  String getUserEmail(){
        return userEmail;
    }
    public String getUserPassword(){
        return userPassword;
    }
    public String getUserPhone(){
        return userPhone;
    }
    public String getUserPoint(){return  userPoint;}
    public String getPushToken(){return pushToken;}
}
