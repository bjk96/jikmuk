package com.example.kangt.jm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kangt.jm.model.User_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class member_regist_complete extends AppCompatActivity {
    Button btn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_regist_complete);
        btn = findViewById( R.id.btn_bucket);


    }

    public void onClick(View view){
        switch (view.getId())
        {
            case R.id.btn_bucket:
                String email = getIntent().getStringExtra("email");
                String name = getIntent().getStringExtra("name");
                String password = getIntent().getStringExtra("password");
                String phone =getIntent().getStringExtra("phone");
                String point = getIntent().getStringExtra("point");

                User_model user_model = new User_model(email,name,password, phone,point);
                String uid = firebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference.child("users").child("user").child(uid).setValue(user_model);

                Intent intent = new Intent(this, member_login.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수

}
