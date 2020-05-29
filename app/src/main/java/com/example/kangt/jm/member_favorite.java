package com.example.kangt.jm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.kangt.jm.model.favoriteList_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class member_favorite extends AppCompatActivity {
    Toolbar menuToolbar;
    public static String TAG = member_favorite.class.getSimpleName();
    /*
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<member_favorite_info> foodInfoArrayList = new ArrayList<>();
    */
    DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;

    private Spinner spin_sport;
    private Spinner spin_stadium;
    private TextView tv_store;
    private RecyclerView rv_favorite;
    public static TextView tv_total;
    private Button btn_order;
    private Button btn_back;

    public static String order = "";

    ArrayList sportsList = new ArrayList<>();
    ArrayList stadiumList = new ArrayList<>();
    ArrayList storeList = new ArrayList<>();

    ArrayAdapter<String> adapterSports;
    ArrayAdapter<CharSequence> adapterStadium;
    member_favorite_adapter favoriteList_adapter = new member_favorite_adapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_favorite);
        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(menuToolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);


        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        //spin_sport spin_stadium
        sportsList.add("축구");
        sportsList.add("야구");
        sportsList.add("농구");
        sportsList.add("배구");

        spin_sport = (Spinner)findViewById(R.id.spin_sport);
        spin_stadium = (Spinner)findViewById(R.id.spin_stadium);
        tv_store = (TextView)findViewById(R.id.txt_store);
        rv_favorite = (RecyclerView)findViewById(R.id.rv_favoite);
        tv_total = (TextView)findViewById(R.id.txt_totalprice);
        btn_order = findViewById(R.id.button2);
        btn_back = findViewById(R.id.button3);

        adapterSports = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sportsList);
        spin_sport.setAdapter(adapterSports);

        spin_sport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spin_sport.getSelectedItem().equals("축구")){
                    adapterStadium = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sports_soccer, android.R.layout.simple_spinner_dropdown_item);
                    spin_stadium.setAdapter(adapterStadium);
                }else if(spin_sport.getSelectedItem().equals("야구")){
                    adapterStadium = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sports_baseball, android.R.layout.simple_spinner_dropdown_item);
                    spin_stadium.setAdapter(adapterStadium);
                }else if(spin_sport.getSelectedItem().equals("농구")){
                    adapterStadium = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sports_basketball, android.R.layout.simple_spinner_dropdown_item);
                    spin_stadium.setAdapter(adapterStadium);
                }else if(spin_sport.getSelectedItem().equals("배구")){
                    adapterStadium = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sports_volleyball, android.R.layout.simple_spinner_dropdown_item);
                    spin_stadium.setAdapter(adapterStadium);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {            }
        });

        spin_stadium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FirebaseDatabase.getInstance().getReference().child("Store").child("storeindex").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        storeList.clear();

                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            String storename = item.child("storename").getValue(String.class);
                            String storeplace = item.child("place").getValue(String.class);

                            if(storeplace.equals(spin_stadium.getSelectedItem()))
                                storeList.add(storename);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {      }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {      }
        });

        tv_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog(v);
            }
        });

        //rv_favorite
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rv_favorite.setLayoutManager(manager);

        tv_store.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rv_favorite.setAdapter(favoriteList_adapter);
                getFavoriteList();
            }

            @Override
            public void afterTextChanged(Editable s) {      }
        });

        //주문버튼 활성/비활성
        tv_total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Integer.parseInt(tv_total.getText().toString()) == 0){
                    btn_order.setEnabled(false);
                    btn_order.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn_order.setEnabled(true);
                    btn_order.setBackgroundResource(R.drawable.btn_yellow);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        //주문
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order();
            }
        });

        //뒤로가기
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                Toast.makeText(getApplicationContext(), "메뉴 버튼 눌림", Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(GravityCompat.START);

                return super.onOptionsItemSelected(item);

            case R.id.go_main:  //메인화면으로 이동
                Toast.makeText(getApplicationContext(), "홈 버튼 클릭됨", Toast.LENGTH_LONG).show();
                Intent home_intent = new Intent(this, member_main.class);
                home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(home_intent);
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(getApplicationContext(), "나머지 버튼 클릭됨", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);

        }

    }

    public void showdialog(View v) {
        String[] items = new String[storeList.size()] ;

        if (storeList.size()  == 0 || storeList.isEmpty()){
            Log.i(TAG,"size of array list  is null" );
        }else {
            for (int i = 0; i<storeList.size();i++){
                items[i] = storeList.get(i).toString();
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext() );  //이부분에서 오류
        builder.setTitle("가게를 선택해주세요");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos];
                TextView setstore = v.findViewById(R.id.txt_store);
                setstore.setText(selectedText);
            }
        });
        builder.show();

    }

    void getFavoriteList(){
        String str_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String str_store = tv_store.getText().toString();

        FirebaseDatabase.getInstance().getReference().child("favorite").orderByChild("/users/user"+str_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favoriteList_adapter.favoriteList.clear();

                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(str_uid.equals(item.child("uid").getValue().toString()) && str_store.equals(item.child("store").getValue().toString())){
                        favoriteList_model model = item.getValue(favoriteList_model.class);

                        favoriteList_adapter.favoriteList.add(model);

                     }
                }
                favoriteList_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void Order(){
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setMessage("주문하시겠습니까?");
        dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String stadium = spin_stadium.getSelectedItem().toString();
                String total = tv_total.getText().toString();
                String store = tv_store.getText().toString();

                Intent intent = new Intent(member_favorite.this, member_order_pay.class);
                intent.putExtra("member_stadium_name", stadium);
                intent.putExtra("seat", "");
                intent.putExtra("price", total);
                intent.putExtra("menucontent", order);
                intent.putExtra("storename", store);
                startActivity(intent);

                finish();
            }
        });
        dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dlg.show();
    }
}


