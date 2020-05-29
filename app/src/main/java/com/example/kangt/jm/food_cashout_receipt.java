package com.example.kangt.jm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangt.jm.model.food_point_receipt_model;
import com.example.kangt.jm.model.point_receipt_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class food_cashout_receipt extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Calendar cal = Calendar.getInstance();
    int Year =cal.get(Calendar.YEAR);
    int Month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DATE);

    RecyclerView r;
    food_point_adapter adp = new food_point_adapter();
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;

    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;

    int y=0, m=0, d=0;
    TextView setdate2;
    TextView setdate1;
    Button search;
    String cut,re;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cashout_receipt);

        r = findViewById(R.id.recycle);

        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar( menuToolbar );
        drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );

        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle(null);

        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        r.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        r.setLayoutManager(manager);
        r.setAdapter(adp);
        getPointReceipt();



        setdate2 = findViewById(R.id.day_until);
        setdate1 = findViewById(R.id.day_form);
        search = findViewById(R.id.btn_search);
        setdate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate1();

            }
        });
        setdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate2();
            }

        });

        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("point_receipt_store").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {

                        FirebaseDatabase.getInstance().getReference().child("point_receipt_store").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange( DataSnapshot dataSnapshot2) {
                                adp.pointReceipt.clear();
                                adp.notifyDataSetChanged();

                                for(DataSnapshot item : dataSnapshot2.getChildren())
                                {
                                    String struid = item.child("uid").getValue().toString();
                                    if(struid.equals(uid) == true){
                                        if(item.getValue(point_receipt_model.class).getType().equals("주문")) {
                                            re = String.valueOf(item.getValue(point_receipt_model.class).getTimestamp());
                                        }
                                        else if(item.getValue(point_receipt_model.class).getType().equals("환전"))
                                        {
                                            re = String.valueOf(item.getValue(point_receipt_model.class).getCashoutTime());
                                        }
                                        cut = re.substring(0,10);


                                        int i = String.valueOf(setdate1.getText()).compareTo(cut);
                                        int j = String.valueOf(setdate2.getText()).compareTo(cut);
                                        if(i<=0)
                                        {
                                            if(j>=0 && item.getValue(point_receipt_model.class).getType().equals("환전"))
                                            {
                                                food_point_receipt_model model = item.getValue(food_point_receipt_model.class);
                                                adp.pointReceipt.add(0, model);
                                            }
                                        }
                                      }
                                }
                                adp.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled( DatabaseError databaseError) {
                            }
                        });
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {

                    }
                });
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
                    case R.id.food_regist:
                        Intent intent_regist = new Intent(getApplicationContext(), food_regist.class);
                        startActivity(intent_regist);
                        finish();
                        break;

                    case R.id.food_menu:
                        Intent intent_menu = new Intent(getApplicationContext(), food_menu.class);
                        startActivity(intent_menu);
                        finish();
                        break;

                    case R.id.food_order_receipt:
                        Intent intent_order_receipt = new Intent(getApplicationContext(), food_order_receipt.class);
                        startActivity(intent_order_receipt);
                        finish();
                        break;

                    case R.id.food_cashout:
                        Intent intent_favorite = new Intent(getApplicationContext(), food_cashout.class);
                        startActivity(intent_favorite);
                        finish();
                        break;

                    case R.id.food_cashout_receipt:
                        Intent intent_question = new Intent(getApplicationContext(), food_cashout_receipt.class);
                        startActivity(intent_question);
                        finish();
                        break;

                    case R.id.logout:
                        //food_start 죽이기
                        food_start killActivity = (food_start) food_start.food;
                        killActivity.finish();

                        //로그아웃 기능
                        firebaseAuth.signOut();
                        Toast.makeText(getApplicationContext(), "로그아웃 되셨습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext() , member_login.class);
                        startActivity(intent);
                        finish();
                        break;
                }

                return false;
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

    @Override
    public boolean onNavigationItemSelected( MenuItem menuItem) {
        return false;
    }

    //초반 추가 하는 부분

    void getPointReceipt(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("point_receipt_store").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adp.pointReceipt.clear();

                for(DataSnapshot item : dataSnapshot.getChildren()){
                    String struid = item.child("uid").getValue().toString();
                    String type = item.child("type").getValue().toString();
                    if(struid.equals(uid) == true && type.equals("환전")){
                        food_point_receipt_model model = item.getValue(food_point_receipt_model.class);
                        adp.pointReceipt.add(0, model);

                       }
                }
                adp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void showDate2() {
        DatePickerDialog datePickerDialog1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;

                if(m >=10 ) {
                    if (d >= 10) {
                        setdate2.setText(String.valueOf(y + "-" + m + "-" + d));

                    }
                    else
                    {
                        setdate2.setText(String.valueOf(y + "-" + m + "-"+"0" + d));

                    }
                }
                else
                {
                    if(d>= 10)
                    {
                        setdate2.setText(String.valueOf(y + "-"+"0" + m + "-" + d));
                    }
                    else
                    {
                        setdate2.setText(String.valueOf(y + "-"+"0" + m + "-"+"0" + d));

                    }
                }


            }
        },Year, Month, day);

        datePickerDialog1.setMessage("마지막 날짜 검색");
        datePickerDialog1.show();

    }

    public void showDate1() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;

                if(m >=10 ) {
                    if (d >= 10) {
                        setdate1.setText(String.valueOf(y + "-" + m + "-" + d));

                    }
                    else
                    {
                        setdate1.setText(String.valueOf(y + "-" + m + "-"+"0" + d));

                    }
                }
                else if(m < 10)
                {
                    if(d>= 10)
                    {
                        setdate1.setText(String.valueOf(y + "-"+"0" + m + "-" + d));
                    }
                    else
                    {
                        setdate1.setText(String.valueOf(y + "-"+"0" + m + "-"+"0" + d));

                    }
                }


            }
        },Year, Month, day);

        datePickerDialog.setMessage("처음 날짜 검색");
        datePickerDialog.show();

    }

}
