package com.example.kangt.jm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class favorite_recycler_view extends AppCompatActivity {
    ImageView iv,iv_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_favorite_recycler_view );

        iv_1 = findViewById(R.id.iv_ikon );
    }


}
