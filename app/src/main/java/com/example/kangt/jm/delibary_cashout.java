package com.example.kangt.jm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangt.jm.model.delibary_add_pointReceipt_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class delibary_cashout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button btn,btn1;
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    EditText edit_changePoint;
    EditText edit_accountHolder;
    EditText edit_accountNum;
    TextView txt_nowPoint, txt_remainPoint;
    Spinner spin_bank;
    ArrayAdapter<CharSequence> arrayAdapter;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delibary_cashout);
        btn = findViewById(R.id.btn_delibary_cashout);
        btn1 = findViewById(R.id.btn_back);
        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );
        edit_changePoint = (EditText)findViewById(R.id.txt_changepoint);
        edit_accountHolder=(EditText)findViewById(R.id.txt_accountname);
        edit_accountNum = (EditText)findViewById(R.id.txt_accountnumber);
        txt_nowPoint = (TextView)findViewById(R.id.txt_nowpoint);
        txt_remainPoint = (TextView)findViewById(R.id.txt_remainpoint);
        spin_bank = findViewById(R.id.spin_bankname);


        setSupportActionBar( menuToolbar );

        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle(null);

        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        String uid = firebaseAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                txt_nowPoint.setText(dataSnapshot.child("userPoint").getValue().toString());
                txt_remainPoint.setText(dataSnapshot.child("userPoint").getValue().toString());
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });

        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.bank, android.R.layout.simple_spinner_dropdown_item);
        spin_bank.setAdapter(arrayAdapter);

        //버튼 활성화/비활성화
        edit_changePoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer remain = Integer.parseInt(edit_changePoint.getText().toString());
                if(edit_changePoint.length() == 0 || edit_accountNum.length() == 0 || edit_accountHolder.length() == 0 || remain == -1){
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn.setEnabled(true);
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                }
                if(edit_changePoint.length() != 0){
                    Integer int_nowPoint = Integer.parseInt(txt_nowPoint.getText().toString());
                    Integer int_changePoint = Integer.parseInt(edit_changePoint.getText().toString());
                    Integer int_remainPoint = int_nowPoint - int_changePoint;
                    if(int_remainPoint >= 0){
                        txt_remainPoint.setText(int_remainPoint.toString());
                    } else {
                        Integer impossible = -1;
                        txt_remainPoint.setText(impossible.toString());
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        edit_accountHolder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edit_changePoint.length() == 0 || edit_accountNum.length() == 0 || edit_accountHolder.length() == 0){
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn.setEnabled(true);
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        edit_accountNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edit_changePoint.length() == 0 || edit_accountNum.length() == 0 || edit_accountHolder.length() == 0){
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn.setEnabled(true);
                    btn.setBackgroundResource(R.drawable.btn_yellow);
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
                    case R.id.delibary_regist:
                        Intent intent_bucket = new Intent(getApplicationContext(), delibary_start.class);
                        startActivity(intent_bucket);
                        finish();
                        break;

                    case R.id.delibary_receipt:
                        Intent intent_pointcharge = new Intent(getApplicationContext(), delibary_receipt.class);
                        startActivity(intent_pointcharge);
                        finish();
                        break;

                    case R.id.delibary_cashout:
                        Intent intent_favorite = new Intent(getApplicationContext(), delibary_cashout.class);
                        startActivity(intent_favorite);
                        finish();
                        break;

                    case R.id.delibary_cashout_receipt:
                        Intent intent_question = new Intent(getApplicationContext(), delibary_cashout_receipt.class);
                        startActivity(intent_question);
                        finish();
                        break;

                    case R.id.logout:
                        //delibary_main 죽이기
                        delibary_main killActivity = (delibary_main) delibary_main.delivery;
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
                Intent home_intent = new Intent(this, delibary_main.class);
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

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_delibary_cashout:
                String uid = firebaseAuth.getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("users/user/"+uid+"/userPoint", txt_remainPoint.getText().toString());
                        FirebaseDatabase.getInstance().getReference().updateChildren(map);

                        String bank = spin_bank.getSelectedItem().toString();
                        String accountHolder = edit_accountHolder.getText().toString();
                        String accountNum = edit_accountNum.getText().toString();
                        String beforePoint =  txt_nowPoint.getText().toString();
                        String usingPoint = edit_changePoint.getText().toString();
                        String afterPoint = txt_remainPoint.getText().toString();
                        delibary_add_pointReceipt_model model = new delibary_add_pointReceipt_model(bank, accountHolder, accountNum, beforePoint, usingPoint, afterPoint, uid, "환전", getTime());
                        FirebaseDatabase.getInstance().getReference().child("point_receipt_delivery").push().setValue(model);
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {

                    }
                });

                AlertDialog.Builder dlg = new AlertDialog.Builder(view.getContext());
                dlg.setMessage(edit_changePoint.getText().toString() + "포인트가 환전되었습니다.");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dlg.show();

                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem menuItem) {
        return false;
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
