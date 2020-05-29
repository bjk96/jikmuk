package com.example.kangt.jm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.example.kangt.jm.model.food_receipt_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_LONG;

public class food_order_receipt extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private RecyclerView rv_receipt;
    private food_receipt_adapter adapter = new food_receipt_adapter();
    Calendar cal = Calendar.getInstance();

    int Year =cal.get(Calendar.YEAR);
    int Month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DATE);

    Toolbar menuToolbar;
    DrawerLayout drawerLayout;

    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    int y=0, m=0, d=0;
    TextView setdate2;
    TextView setdate1;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_receipt);



        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );
        setSupportActionBar( menuToolbar );

        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle(null);

        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        rv_receipt = findViewById(R.id.rv_receipt);

        rv_receipt.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_receipt.setLayoutManager(manager);
        rv_receipt.setAdapter(adapter);
        showReceipt();
        //여기부분부터 날짜검색
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
                adapter.receiptList.clear();
                adapter.notifyDataSetChanged(); //현재 전부 삭제

            }
        });

        search.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                FirebaseDatabase.getInstance().getReference().child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FirebaseDatabase.getInstance().getReference().child("orders").orderByChild("/users/user"+uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                adapter.receiptList.clear();

                                for(DataSnapshot item : dataSnapshot2.getChildren()){
                                    String name = dataSnapshot.child("userName").getValue().toString();
                                    String shop = item.child("shop").getValue().toString();
                                    if(name.equals(shop) == true){
                                        String re = String.valueOf(item.getValue(food_receipt_model.class).getOrdertime());
                                        String cut = re.substring(0,10);

                                        int i = String.valueOf(setdate1.getText()).compareTo(cut);

                                        int j = String.valueOf(setdate2.getText()).compareTo(cut);
                                        //cut 타입 =  String
                                       if(i<=0) {
                                            if(j>=0) {
                                                food_receipt_model model = item.getValue(food_receipt_model.class);
                                                adapter.receiptList.add(0, model);
                                            }
                                        }


                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
//여기까지
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
                        Intent intent_regist = new Intent(getApplicationContext(), food_store_manage.class);
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
                        Toast.makeText(getApplicationContext(), "로그아웃 되셨습니다.", LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext() , member_login.class);
                        startActivity(intent);
                        finish();
                        break;
                }

                return true;
            }
        });
    }
//여기부터
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

//여기까지

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

    void showReceipt(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseDatabase.getInstance().getReference().child("orders").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                        adapter.receiptList.clear();

                        for(DataSnapshot item : dataSnapshot2.getChildren()){
                            String name = dataSnapshot.child("userName").getValue().toString();
                            String shop = item.child("shop").getValue().toString();
                            if(name.equals(shop) == true){
                                food_receipt_model model = item.getValue(food_receipt_model.class);
                                adapter.receiptList.add(0, model);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }




}
