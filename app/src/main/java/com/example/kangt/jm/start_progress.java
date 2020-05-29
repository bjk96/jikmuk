package com.example.kangt.jm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

public class start_progress extends AppCompatActivity {
    ProgressBar progressBar;

    MyHandler handler;
    public static final int INCREMENT_PROGRESS = 0;
    public static final int FINISH_PROGRESS = 1;
    public static Activity startActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_progress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //status bar 색상 변경 SDK 버전 21이상
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.WHITE);
        }

        startActivity = start_progress.this;

        handler = new MyHandler();
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 20; i++) {
                        sleep(100);
                        handler.sendMessage(handler.obtainMessage(INCREMENT_PROGRESS));
                    }
                    handler.sendMessage(handler.obtainMessage(FINISH_PROGRESS, "finish"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case INCREMENT_PROGRESS:

                    progressBar.incrementProgressBy(5);

                    break;

                case FINISH_PROGRESS:

                    Intent intent = new Intent(getApplicationContext(), member_login.class);
                    startActivity(intent);
                    break;
            }
            super.handleMessage(msg);
        }
    }
}