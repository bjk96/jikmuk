package com.example.kangt.jm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.kangt.jm.model.User_model;
import com.example.kangt.jm.model.member_present_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class member_charge extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;
    RadioButton rb_charge_phone;
    RadioButton rb_charge_card;
    RadioButton rb_charge_giftcard;
    RadioButton rb_payA;
    RadioButton rb_payB;
    RadioButton rb_payC;
    RadioButton rb_payD;
    RadioButton rb_payE;
    EditText edit_charge;
    Button btn_charge;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    private List<User_model> userdetas = new ArrayList<User_model>();
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_charge);


        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );
        rb_charge_phone = (RadioButton)findViewById(R.id.rb_charge_phone);
        rb_charge_card = (RadioButton)findViewById(R.id.rb_charge_card);
        rb_charge_giftcard = (RadioButton)findViewById(R.id.rb_charge_giftcard);
        rb_payA = (RadioButton)findViewById(R.id.rb_payA);
        rb_payB = (RadioButton)findViewById(R.id.rb_payB);
        rb_payC = (RadioButton)findViewById(R.id.rb_payC);
        rb_payD = (RadioButton)findViewById(R.id.rb_payD);
        rb_payE = (RadioButton)findViewById(R.id.rb_payE);
        edit_charge = (EditText)findViewById(R.id.edit_charge);
        btn_charge = (Button)findViewById(R.id.btn_charge);
        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        btn_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = firebaseAuth.getCurrentUser().getUid();


                databaseReference.child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User_model user_model = dataSnapshot.getValue(User_model.class);
                        String uid = firebaseAuth.getCurrentUser().getUid();
                        String User_Point = dataSnapshot.child("userPoint").getValue().toString();
                        String str = edit_charge.getText().toString();

                        int strint = Integer.parseInt(User_Point);
                        int strint2 = Integer.parseInt(str);
                        int hap = strint + strint2 ;
                        String userpoint = String.valueOf(hap);
                        user_model.setUserPoint(userpoint);
                        Map<String , Object> map = new HashMap<>();
                        map.put("users/user/"+uid+"/userPoint",userpoint);
                        databaseReference.updateChildren(map);

                        member_present_model model = new member_present_model(getTime(), uid, User_Point, str, userpoint, "충전");
                        FirebaseDatabase.getInstance().getReference().child("point_receipt_member").push().setValue(model);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                dlg.setMessage("충전이 완료되었습니다.");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dlg.show();

                //Intent intent = new Intent(member_charge.this , member_main.class  );
                //startActivity(intent);
            }

        });



        // 네비게이션 뷰 아이템 클릭시 이뤄지는 이벤트
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();


                int id = item.getItemId();
                // 각 메뉴 클릭시 이뤄지는 이벤트
                switch (id){
                    case R.id.account:
                        //계정엔 뭘 널어야 하는가
                        break;

                    case R.id.point:
                        Intent intent_pointcharge = new Intent(getApplicationContext(), member_charge.class);
                        startActivity(intent_pointcharge);
                        finish();
                        break;

                    case R.id.star:
                        Intent intent_favorite = new Intent(getApplicationContext(), member_favorite.class);
                        startActivity(intent_favorite);
                        finish();
                        break;

                    case R.id.customer_center:
                        Intent intent_question = new Intent(getApplicationContext(), member_usual_request.class);
                        startActivity(intent_question);
                        finish();
                        break;

                    case R.id.setting:
                        //설정은 뭘 넣어야 하는가
                        break;

                    case R.id.logout:
                        //member_main 죽이기
                        member_main killActivity = (member_main) member_main.member;
                        killActivity.finish();

                        //로그아웃 기능
                        firebaseAuth.signOut();
                        Toast.makeText(getApplicationContext(), "로그아웃 되셨습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext() , member_login.class);
                        startActivity(intent);
                        finish();
                        break;
                }

                return true;
            }
        });

        //라디오버튼 클릭리스너
        rb_charge_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_charge_card.setChecked(false);
                rb_charge_giftcard.setChecked(false);

                //버튼 활성화
                if(rb_payA.isChecked() || rb_payB.isChecked() || rb_payC.isChecked() || rb_payD.isChecked() || (rb_payE.isChecked() && edit_charge.length() > 0)){
                    btn_charge.setBackgroundResource(R.drawable.btn_yellow);
                    btn_charge.setEnabled(true);
                }
            }
        });
        rb_charge_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_charge_phone.setChecked(false);
                rb_charge_giftcard.setChecked(false);

                //버튼 활성화
                if(rb_payA.isChecked() || rb_payB.isChecked() || rb_payC.isChecked() || rb_payD.isChecked() || (rb_payE.isChecked() && edit_charge.length() > 0)){
                    btn_charge.setBackgroundResource(R.drawable.btn_yellow);
                    btn_charge.setEnabled(true);
                }
            }
        });
        rb_charge_giftcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_charge_phone.setChecked(false);
                rb_charge_card.setChecked(false);

                //버튼 활성화
                if(rb_payA.isChecked() || rb_payB.isChecked() || rb_payC.isChecked() || rb_payD.isChecked() || (rb_payE.isChecked() && edit_charge.length() > 0)){
                    btn_charge.setBackgroundResource(R.drawable.btn_yellow);
                    btn_charge.setEnabled(true);
                }
            }
        });
        rb_payA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_payB.setChecked(false);
                rb_payC.setChecked(false);
                rb_payD.setChecked(false);
                rb_payE.setChecked(false);
                edit_charge.setText("5000");
                edit_charge.setEnabled(false);

                //버튼 활성화
                if(rb_charge_phone.isChecked() || rb_charge_card.isChecked() || rb_charge_giftcard.isChecked()){
                    btn_charge.setBackgroundResource(R.drawable.btn_yellow);
                    btn_charge.setEnabled(true);
                }
            }
        });
        rb_payB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_payA.setChecked(false);
                rb_payC.setChecked(false);
                rb_payD.setChecked(false);
                rb_payE.setChecked(false);
                edit_charge.setText("10000");
                edit_charge.setEnabled(false);

                //버튼 활성화
                if(rb_charge_phone.isChecked() || rb_charge_card.isChecked() || rb_charge_giftcard.isChecked()){
                    btn_charge.setBackgroundResource(R.drawable.btn_yellow);
                    btn_charge.setEnabled(true);
                }
            }
        });
        rb_payC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_payA.setChecked(false);
                rb_payB.setChecked(false);
                rb_payD.setChecked(false);
                rb_payE.setChecked(false);
                edit_charge.setText("30000");
                edit_charge.setEnabled(false);

                //버튼 활성화
                if(rb_charge_phone.isChecked() || rb_charge_card.isChecked() || rb_charge_giftcard.isChecked()){
                    btn_charge.setBackgroundResource(R.drawable.btn_yellow);
                    btn_charge.setEnabled(true);
                }
            }
        });
        rb_payD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_payA.setChecked(false);
                rb_payB.setChecked(false);
                rb_payC.setChecked(false);
                rb_payE.setChecked(false);
                edit_charge.setText("50000");
                edit_charge.setEnabled(false);

                //버튼 활성화
                if(rb_charge_phone.isChecked() || rb_charge_card.isChecked() || rb_charge_giftcard.isChecked()){
                    btn_charge.setBackgroundResource(R.drawable.btn_yellow);
                    btn_charge.setEnabled(true);
                }


            }
        });
        rb_payE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_payA.setChecked(false);
                rb_payB.setChecked(false);
                rb_payC.setChecked(false);
                rb_payD.setChecked(false);
                edit_charge.setEnabled(true);
                edit_charge.setText(null);

                //버튼 활성화
                if(edit_charge.length() > 0){
                    if((rb_charge_phone.isChecked() || rb_charge_card.isChecked() || rb_charge_giftcard.isChecked()) && edit_charge.length() > 0){
                        btn_charge.setBackgroundResource(R.drawable.btn_yellow);
                        btn_charge.setEnabled(true);
                    }
                }else if(edit_charge.length() == 0){
                    btn_charge.setBackgroundResource(R.drawable.btn_gray);
                    btn_charge.setEnabled(false);
                }

            }
        });

        //버튼 활성화 비활성화
        edit_charge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((rb_payE.isChecked() && edit_charge.length() > 0) || rb_charge_phone.isChecked() || rb_charge_card.isChecked() || rb_charge_giftcard.isChecked()){
                    btn_charge.setBackgroundResource(R.drawable.btn_yellow);
                    btn_charge.setEnabled(true);

                }else if(edit_charge.length() == 0){
                    btn_charge.setBackgroundResource(R.drawable.btn_gray);
                    btn_charge.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setSupportActionBar( menuToolbar );

        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle(null);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);

                return super.onOptionsItemSelected(item);

            case R.id.go_main:  //메인화면으로 이동
                Intent home_intent = new Intent(this, member_main.class);
                home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(home_intent);
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}

