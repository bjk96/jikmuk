package com.example.kangt.jm;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.kangt.jm.model.User_model;
import com.example.kangt.jm.model.food_add_pointReceipt_model;
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

public class food_cashout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button btn_cashout,btn_back;
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;
    EditText edit_changePoint, edit_accountHolder, edit_accountNum;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    //private TextView txt_changepoint;
    private TextView txt_nowpoint;
    private TextView txt_remainPoint;
    Spinner spin_bank;
    ArrayAdapter<CharSequence> arrayAdapter;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cashout);
        btn_cashout = findViewById(R.id.btn_cashout);
        btn_back = findViewById(R.id.btn_back);
        edit_changePoint = findViewById(R.id.txt_changepoint);
        edit_accountHolder = findViewById(R.id.txt_accountname);
        edit_accountNum = findViewById(R.id.txt_accountnumber);
        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );
        txt_nowpoint = (TextView)findViewById(R.id.txt_nowpoint);
        txt_remainPoint = findViewById(R.id.txt_remainpoint);
        spin_bank = findViewById(R.id.spin_bankname);
        setSupportActionBar( menuToolbar );

        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle(null);

        String uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("users").child("user").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange ( DataSnapshot dataSnapshot) {
                txt_nowpoint.setText(dataSnapshot.child("userPoint").getValue().toString());
                txt_remainPoint.setText(dataSnapshot.child("userPoint").getValue().toString());
            }

            @Override
            public void onCancelled ( DatabaseError databaseError) {

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
                Integer remain = Integer.parseInt(txt_remainPoint.getText().toString());
                if(edit_changePoint.length()==0 || edit_accountHolder.length()==0 || edit_accountNum.length()==0 || remain < 0)
                {
                    btn_cashout.setEnabled(false);
                    btn_cashout.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn_cashout.setEnabled(true);
                    btn_cashout.setBackgroundResource(R.drawable.btn_yellow);
                }
                if(edit_changePoint.length() > 0){
                    Integer int_now = Integer.parseInt(txt_nowpoint.getText().toString());
                    Integer int_change = Integer.parseInt(edit_changePoint.getText().toString());
                    Integer int_after = int_now-int_change;
                    txt_remainPoint.setText(int_after.toString());
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
                if(edit_changePoint.length()==0 || edit_accountHolder.length()==0 || edit_accountNum.length()==0)
                {
                    btn_cashout.setEnabled(false);
                    btn_cashout.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn_cashout.setEnabled(true);
                    btn_cashout.setBackgroundResource(R.drawable.btn_yellow);
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
                if(edit_changePoint.length()==0 || edit_accountHolder.length()==0 || edit_accountNum.length()==0)
                {
                    btn_cashout.setEnabled(false);
                    btn_cashout.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn_cashout.setEnabled(true);
                    btn_cashout.setBackgroundResource(R.drawable.btn_yellow);
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
                    case R.id.food_regist:
                        Intent intent_regist = new Intent(getApplicationContext(),food_regist.class);
                        startActivity(intent_regist);
                        finish();
                        break;

                    case R.id.food_menu:
                        Intent intent_menu = new Intent(getApplicationContext(),food_menu.class);
                        startActivity(intent_menu);
                        finish();
                        break;

                    case R.id.food_order_receipt:
                        Intent intent_order_receipt = new Intent(getApplicationContext(), food_order_receipt.class);
                        startActivity(intent_order_receipt);
                        finish();
                        break;

                    case R.id.food_cashout:
                        Intent intent_favorite = new Intent(getApplicationContext(),food_cashout.class);
                        startActivity(intent_favorite);
                        finish();
                        break;

                    case R.id.food_cashout_receipt:
                        Intent intent_question = new Intent(getApplicationContext(),food_cashout_receipt.class);
                        startActivity(intent_question);
                        finish();
                        break;

                    case R.id.logout:
                        //food_start 죽이기
                        food_start killActivity = (food_start)food_start.food;
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
                Intent home_intent = new Intent(this, food_start.class);
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
            case R.id.btn_cashout:
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("users/user/"+uid+"/userPoint", txt_remainPoint.getText().toString());
                        FirebaseDatabase.getInstance().getReference().updateChildren(map1);

                        String str_bank = spin_bank.getSelectedItem().toString();
                        String accountHolder = edit_accountHolder.getText().toString();
                        String accountNum = edit_accountNum.getText().toString();
                        String beforePoint = txt_nowpoint.getText().toString();
                        String usingPoint = edit_changePoint.getText().toString();
                        String afterPoint = txt_remainPoint.getText().toString();
                        food_add_pointReceipt_model model = new food_add_pointReceipt_model(str_bank, accountHolder, accountNum, beforePoint, usingPoint, afterPoint, uid, "환전", getTime());
                        FirebaseDatabase.getInstance().getReference().child("point_receipt_store").push().setValue(model);
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
void createcash(){
    String uid = firebaseAuth.getCurrentUser().getUid();
    databaseReference.child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange( DataSnapshot dataSnapshot) {
            User_model userModel = dataSnapshot.getValue(User_model.class);
            txt_nowpoint.setText(userModel.getUserPoint());
        }

        @Override
        public void onCancelled( DatabaseError databaseError) {

        }
    });
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
