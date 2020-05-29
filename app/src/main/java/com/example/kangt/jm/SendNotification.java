package com.example.kangt.jm;

import android.os.AsyncTask;
import android.util.Log;
import android.view.textclassifier.TextLinks;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendNotification {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static void sendNotification(String regToken, String messsage){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... parms) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body", messsage);
                    dataJson.put("title", "주문이 왔습니다");
                    json.put("notification", dataJson);
                    json.put("to", regToken);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + "AAAAx8bKJFc:APA91bGQB0SmYUtPf_eW2zw9aEl0FM9PNMqMkAU3X_w2pvJRFybHTyxYhyqTAjzDCpjQ81W6NEdWw5py3cbEBl2TlNPQGMXOpjRvpPWb-Pl22gk1wbE7nGkDu05KVoAp2Ok4hAAcEZyo")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                }catch (Exception e){
                }
                return  null;
            }
        }.execute();
    }
}