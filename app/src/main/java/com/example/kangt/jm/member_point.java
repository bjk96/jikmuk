package com.example.kangt.jm;

import android.app.DatePickerDialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangt.jm.model.member_point_receipt_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class member_point extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    RecyclerView rv;
    member_point_adapter adp = new member_point_adapter();

    Toolbar menuToolbar;
    DrawerLayout drawerLayout;

    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;

    Calendar cal = Calendar.getInstance();
    int Year =cal.get(Calendar.YEAR);
    int Month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DATE);

    int y=0, m=0, d=0;
    TextView setdate2;
    TextView setdate1;
    Button search;
    String cut,re;

    public static ArrayList<String> storelist = null;
    //private Spinner sp_gametye,sp_gameplace;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_point);

        rv = findViewById(R.id.recyclerview);
        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );
        setSupportActionBar( menuToolbar );

        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle(null);


        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        rv.setAdapter(adp);
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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                FirebaseDatabase.getInstance().getReference().child("point_receipt_member").orderByChild("/users/user"+uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adp.pointReceipt.clear();
                        //String st =sp_gameplace.getSelectedItem().toString();

                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            String struid = item.child("uid").getValue().toString();

                            if(struid.equals(uid)){
                                re = String.valueOf(item.getValue(member_point_receipt_model.class).getOrdertime());

                                cut = re.substring(0,10);
                                int i = String.valueOf(setdate1.getText()).compareTo(cut);
                                int j = String.valueOf(setdate2.getText()).compareTo(cut);

                                if(i<=0) {
                                    if (j >= 0) {
                                        if(item.child("stadium").exists() == false) {

                                            member_point_receipt_model model = item.getValue(member_point_receipt_model.class);
                                            adp.pointReceipt.add(0, model);
                                        }
                                    }
                                }
                            }
                        }
                        adp.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

//        sp_gametye = findViewById(R.id.sp_gametype);
//        sp_gameplace = findViewById(R.id.sp_gameplace);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sports_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_gametye.setAdapter(adapter);
//        sp_gametye.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (sp_gametye.getSelectedItem().equals("축구")) {
//                    ArrayAdapter<CharSequence> adapte = ArrayAdapter.createFromResource(member_point.this, R.array.sports_soccer,
//                            android.R.layout.simple_spinner_item);
//                    adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    sp_gameplace.setAdapter(adapte);
//                } else if (sp_gametye.getSelectedItem().toString().equals("야구")) {
//                    ArrayAdapter<CharSequence> adapte = ArrayAdapter.createFromResource(member_point.this, R.array.sports_baseball,
//                            android.R.layout.simple_spinner_item);
//                    adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    sp_gameplace.setAdapter(adapte);
//                } else if (sp_gametye.getSelectedItem().toString().equals("농구")) {
//                    ArrayAdapter<CharSequence> adapte = ArrayAdapter.createFromResource(member_point.this, R.array.sports_basketball,
//                            android.R.layout.simple_spinner_item);
//                    adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    sp_gameplace.setAdapter(adapte);
//                } else if (sp_gametye.getSelectedItem().toString().equals("배구")) {
//                    ArrayAdapter<CharSequence> adapte = ArrayAdapter.createFromResource(member_point.this, R.array.sports_volleyball,
//                            android.R.layout.simple_spinner_item);
//                    adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    sp_gameplace.setAdapter(adapte);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

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

    void getPointReceipt(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("point_receipt_member").orderByChild("/users/user"+uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adp.pointReceipt.clear();

                for(DataSnapshot item : dataSnapshot.getChildren()){
                    String struid = item.child("uid").getValue().toString();
                    String type = item.child("type").getValue().toString();

                    if(struid.equals(uid) && !type.equals("결제")){
                        member_point_receipt_model model = item.getValue(member_point_receipt_model.class);
                        adp.pointReceipt.add(0,model);
                    }
                }
                adp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    void showReceipt(){
        String deliveryUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adp.pointReceipt.clear();
                String duid;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.child("deliveryUid").exists() == true){
                        duid = ds.child("deliveryUid").getValue().toString();


                        if(deliveryUid.equals(duid) == true && ds.child("status").getValue().toString().equals("배정대기") == false){
                            //배정대기가 아닌 배달중 배달 완료까지만
                            member_point_receipt_model model = ds.getValue(member_point_receipt_model.class);
                            adp.pointReceipt.add(0, model);
                        }
                    }
                }
                adp.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
