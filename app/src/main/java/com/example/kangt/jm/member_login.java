package com.example.kangt.jm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;


public class member_login extends AppCompatActivity {
    private Button btn, btn2;
    private RadioButton radio1, radio2, radio3;
    private Intent intent;
    private EditText email, password;
    private CheckBox checkBox;
    private FirebaseRemoteConfig firebaseRemothConfig;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener ;
    private SharedPreferences appData;
    private boolean saveLoginData;
    private String id,pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        firebaseRemothConfig = FirebaseRemoteConfig.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        setContentView( R.layout.activity_member_login );
        btn = (Button)findViewById( R.id.btn_login );
        btn2 = (Button)findViewById( R.id.btn_regist );
        radio1 =(RadioButton)findViewById( R.id.radio_member );
        radio2 = (RadioButton)findViewById( R.id.radio_food );
        radio3 = (RadioButton)findViewById( R.id.radio_delibary );
        email = (EditText) findViewById( R.id.User_email_login );
        password = (EditText) findViewById( R.id.User_password_login);
        checkBox = findViewById(R.id.checkBox);

        //start_progress 죽이기
        start_progress killActivity = (start_progress) start_progress.startActivity;
        killActivity.finish();

        //버튼 활성화 비활성화
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(email.length() == 0 || password.length() == 0) {
                    btn.setBackgroundResource(R.drawable.btn_gray);
                    btn.setEnabled(false);
                }
                else if(email.length() > 0 && password.length() > 0) {
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                    btn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener((new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(email.length() == 0 || password.length() == 0) {
                    btn.setBackgroundResource(R.drawable.btn_gray);
                    btn.setEnabled(false);
                }
                else if(email.length() > 0 && password.length() > 0) {
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                    btn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }));

        appData = getSharedPreferences("appData",MODE_PRIVATE);
        load();
        if(saveLoginData)
        {
            email.setText(id);
            password.setText(pwd);
          //  checkBox.setChecked(saveLoginData);

        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked() == false)
                {
                    checkBox.setChecked(true);
                }
                save();
                loginEvent();
            }
        });
        //로그인 인터페이스 리스너
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(radio1.isChecked()) {
                    if (user != null) {
                        //로그인
                        passPushTokenToServer();
                        Intent intent = new Intent(member_login.this, member_main.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //로그아웃
                    }
                }
                else if(radio2.isChecked())
                {
                    if (user != null) {
                        //로그인
                        passPushTokenToServer();
                        Intent intent = new Intent(member_login.this, food_start.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //로그아웃
                    }
                }
                else if(radio3.isChecked())
                {
                    if (user != null) {
                        //로그인
                        passPushTokenToServer();
                        Intent intent = new Intent(member_login.this, delibary_main.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //로그아웃
                    }
                }
            }
        };





    }

    private void save() {
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_LOGIN_DATA",checkBox.isChecked());
        editor.putString("id",email.getText().toString().trim());
        editor.putString("pwd",password.getText().toString().trim());

        editor.apply();
    }
    private void load() {
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA",false);
        id = appData.getString("id","");
        pwd = appData.getString("pwd","");
    }
    //회원가입으로 넘어가는 함수부분
    private void regist(){
        intent = new Intent( this, member_agree.class );
        startActivity( intent );
    }
    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.menu, menu );
        return true;
    }

    public void onClick(View view) {
        String getEdit = email.getText().toString();
        String getEdit2 = password.getText().toString();

        if (getEdit.getBytes().length > 0 || getEdit2.getBytes().length > 0) {  //로그인 화면전환 간소화 시키려 일부러 ||로 바꿈
            if (radio1.isChecked()) {
                switch (view.getId()) {

                    case R.id.btn_login:
                        intent = new Intent( this, member_main.class );
                        startActivity( intent );
                        break;
                    case R.id.btn_regist:
                        regist();
                        break;
                }
            } else if (radio2.isChecked()) {
                switch (view.getId()) {

                    case R.id.btn_login:
                        intent = new Intent( this, food_start.class );
                        startActivity( intent );
                        break;
                    case R.id.btn_regist:
                        regist();
                        break;
                }

            } else {
                switch (view.getId()) {

                    case R.id.btn_login:
                        intent = new Intent( this, delibary_main.class );
                        startActivity( intent );
                        break;
                    case R.id.btn_regist:
                        regist();
                        break;
                }
            }
        } else {

            switch (view.getId()) {

                case R.id.btn_login:
                    Toast.makeText( this, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT ).show();
                    break;
                case R.id.btn_regist:
                    regist();
                    break;
            }
        }
    }

    void loginEvent(){
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    //로그인 실패시
                    Toast.makeText(member_login.this ,"아이디와 비밀번호를 확인해주세요" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
    void passPushTokenToServer(){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String , Object> map = new HashMap<>();
        map.put("pushToken",token);

        FirebaseDatabase.getInstance().getReference().child("users").child("user").child(uid).updateChildren(map);
    }
}


