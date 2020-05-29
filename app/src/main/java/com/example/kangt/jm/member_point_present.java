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
import android.widget.TextView;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class member_point_present extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    Button btn_pre, btn_cancel;
    private EditText editText6, editText9;
    private TextView txt_remainPoint, txt_point;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_point_present);

        btn_pre = (Button) findViewById(R.id.button18);
        btn_cancel = (Button) findViewById(R.id.button19);
        editText6 = findViewById(R.id.editText6);
        editText9 = findViewById(R.id.editText9);
        txt_remainPoint = findViewById(R.id.textView74);
        txt_point = findViewById(R.id.txt_point);
        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );
        setSupportActionBar( menuToolbar );

        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle(null);

        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_point.setText(dataSnapshot.child("userPoint").getValue().toString());
                txt_remainPoint.setText(dataSnapshot.child("userPoint").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //버튼활성화/비활성화
        editText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText6.length() == 0 || editText9.length() == 0){
                    btn_pre.setEnabled(false);
                    btn_pre.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    btn_pre.setEnabled(true);
                    btn_pre.setBackgroundResource(R.drawable.btn_yellow);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        editText9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer remain = Integer.parseInt(txt_remainPoint.getText().toString());
                if(editText6.length() == 0 || editText9.length() == 0 || remain < 0){
                    btn_pre.setEnabled(false);
                    btn_pre.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    btn_pre.setEnabled(true);
                    btn_pre.setBackgroundResource(R.drawable.btn_yellow);
                }
                if(editText9.length() != 0) {
                    Integer intb = Integer.parseInt(txt_point.getText().toString());
                    Integer intp = Integer.parseInt(editText9.getText().toString());
                    Integer intr = intb - intp;
                    txt_remainPoint.setText(intr.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
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

    }

    public void onClick(View view){
        switch (view.getId())
        {
            case R.id.button18:
                show();
                break;
            case R.id.button19:
                finish();
                break;
        }
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("선물");
        builder.setMessage("선물 하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String uid =  firebaseAuth.getCurrentUser().getUid();
                        databaseReference.child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User_model user_model = dataSnapshot.getValue(User_model.class);
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                String User_Point = user_model.getUserPoint();
                                String str = editText9.getText().toString();

                                int strint = Integer.parseInt(User_Point);
                                int strint2 = Integer.parseInt(str);
                                int hap = strint - strint2 ;
                                String userpoint = String.valueOf(hap);

                                user_model.setUserPoint(userpoint);
                                Map<String , Object> map = new HashMap<>();
                                map.put("users/user/"+uid+"/userPoint",userpoint);
                                databaseReference.updateChildren(map);

                                String before = txt_point.getText().toString();
                                String using = "-"+editText9.getText().toString();
                                String after = txt_remainPoint.getText().toString();
                                member_present_model model = new member_present_model(getTime(), uid, before, using, after, editText6.getText().toString(), "선물");
                                FirebaseDatabase.getInstance().getReference().child("point_receipt_member").push().setValue(model);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Intent intent = new Intent( member_point_present.this, member_main.class );
                        startActivity( intent );
                        finish();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
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
