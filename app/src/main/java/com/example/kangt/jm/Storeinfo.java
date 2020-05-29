package com.example.kangt.jm;

class Storeinfo {

        private String type;
        private  String place;
        private String storename;
        private String ownername;
        private String ownernum;
        private String storedetail;
        private String ownerid;



        public Storeinfo(String type , String place , String storename , String ownername, String ownernum , String storedetail,String ownerid){
            this.type = type;
            this.place = place;
            this.storename = storename;
            this.ownername = ownername;
            this.ownernum = ownernum;
            this.storedetail = storedetail;
            this.ownerid = ownerid;
        }

        public void setType (String type){
            this.type=type;
        }

        public void setPlace(String place){
            this.place = place;
        }
        public void setstorename(String storename){
            this.storename = storename;
        }
        public void setOwnername(String ownername){
            this.ownername = ownername;
        }
        public void setOwnernum(String ownernum){
            this.ownernum = ownernum;
        }
        public void setStoredetail(String storedetail){
            this.storedetail = storedetail;
        }
        public void setOwnerid(String ownerid)
        {
            this.ownerid = ownerid;
        }


        public  String getType(){
            return type;
        }
        public  String getPlace(){
            return place;
        }
        public String getstorename(){
            return storename;
        }
        public String getownername(){
            return ownername;
        }
        public String getownernum(){
            return ownernum;
        }
        public String getstoredetail(){
            return storedetail;
        }
        public String getOwnerid()
        {
            return ownerid;
        }

    }

