package com.example.kangt.jm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kangt.jm.model.User_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class member_regist extends AppCompatActivity {
    Button btn,btn1;
    private EditText email;
    private EditText name;
    private EditText password;
    private EditText password_check;
    private EditText phone;
    private EditText address;
    private EditText address2;
    private EditText point;
    private  AlertDialog dialog;
    private Button same_check;
    private boolean validate = false;
    private FirebaseAuth mAuth ;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_regist);
        btn = (Button) findViewById( R.id.btn_bucket);
        btn1 = findViewById(R.id.btn_back);
        email = (EditText)findViewById(R.id.User_email);
        name = (EditText)findViewById(R.id.User_name);
        password = (EditText)findViewById(R.id.User_password);
        password_check = (EditText)findViewById(R.id.editText5);
        phone = (EditText)findViewById(R.id.User_phone);
        address = (EditText)findViewById(R.id.User_address);
        address2 = (EditText)findViewById(R.id.User_address2);
        same_check = (Button)findViewById(R.id.same_check);
        point =(EditText)findViewById(R.id.User_Point);
        mAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser(email.getText().toString() , password.getText().toString());


                Intent intent = new Intent(member_regist.this , member_regist_complete.class);
                intent.putExtra("email",email.getText().toString());
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("password",password.getText().toString());
                intent.putExtra("phone",phone.getText().toString());
                intent.putExtra("point",point.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //버튼 활성화 비활성화
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(email.length() == 0){
                    same_check.setEnabled(false);
                    same_check.setBackgroundResource(R.drawable.btn_gray);

                    if(password.length() == 0 || password_check.length() == 0 || name.length() == 0 || phone.length() == 0 || address.length() == 0 || address2.length() == 0){
                        btn.setEnabled(false);
                        btn.setBackgroundResource(R.drawable.btn_gray);
                    }
                }else {
                    same_check.setEnabled(true);
                    same_check.setBackgroundResource(R.drawable.btn_yellow);

                    if(password.length() > 0 || password_check.length() > 0 || name.length() > 0 || phone.length() > 0 || address.length() > 0 || address2.length() > 0){
                        btn.setEnabled(true);
                        btn.setBackgroundResource(R.drawable.btn_yellow);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(email.length() == 0 || password.length() == 0 || password_check.length() == 0 || name.length() == 0 || phone.length() == 0 || address.length() == 0 || address2.length() == 0){
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn.setEnabled(true);
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {   }
        });

        password_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(email.length() == 0 || password.length() == 0 || password_check.length() == 0 || name.length() == 0 || phone.length() == 0 || address.length() == 0 || address2.length() == 0){
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn.setEnabled(true);
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {   }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(email.length() == 0 || password.length() == 0 || password_check.length() == 0 || name.length() == 0 || phone.length() == 0 || address.length() == 0 || address2.length() == 0){
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn.setEnabled(true);
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {   }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(email.length() == 0 || password.length() == 0 || password_check.length() == 0 || name.length() == 0 || phone.length() == 0 || address.length() == 0 || address2.length() == 0){
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn.setEnabled(true);
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {   }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(email.length() == 0 || password.length() == 0 || password_check.length() == 0 || name.length() == 0 || phone.length() == 0 || address.length() == 0 || address2.length() == 0){
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn.setEnabled(true);
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {   }
        });

        address2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(email.length() == 0 || password.length() == 0 || password_check.length() == 0 || name.length() == 0 || phone.length() == 0 || address.length() == 0 || address2.length() == 0){
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn.setEnabled(true);
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {   }
        });
    }
    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void createUser(final String email , final String password ){
        mAuth.createUserWithEmailAndPassword(email , password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                        }
                        else {
                            loginUser(email,password);

                        }
                    }
                });

    }
    private void loginUser(String email , String password){
        mAuth.signInWithEmailAndPassword(email , password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            updateUI(null);
                        }
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser currentUser) {
    }

}
