package com.example.kangt.jm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import static com.example.kangt.jm.R.id.btn_bucket;

public class member_agree extends AppCompatActivity {
    Button btn;
    Toolbar menuToolbar;
    CheckBox cbx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_agree);
        btn = (Button)findViewById(btn_bucket);
        cbx = findViewById( R.id.checkbox_agree );
        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar( menuToolbar );

        //버튼 활성화/비활성화
        cbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbx.isChecked()) {
                    btn.setEnabled(true);
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                }else{
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                }
            }
        });
    }

    public void onClick(View view){

        if (cbx.isChecked())
        {
            switch (view.getId())
            {
                case R.id.btn_bucket:
                    Intent intent = new Intent(this, member_regist.class);
                    finish();
                    startActivity(intent);
                    break;
            }
        }
        else
        {
            switch (view.getId())
            {
                case R.id.btn_bucket:
                    Toast.makeText(this,"약관에 동의해주세요",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    }



