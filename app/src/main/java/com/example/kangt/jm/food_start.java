package com.example.kangt.jm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kangt.jm.model.food_start_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class food_start extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener , NavigationView.OnNavigationItemSelectedListener{
    Button btn1,btn2,btn3,btn4;
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;

    private RecyclerView rv_order;
    private food_start_recyclerview adapter = new food_start_recyclerview();

    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;

    public static Activity food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_start);
        btn1 = findViewById(R.id.btn_manage);
        btn2 = findViewById( R.id.btn_order_receipt );
        btn3 = findViewById(R.id.btn_cashout);
        btn4 = findViewById( R.id.btn_cashout_receipt );
        rv_order = findViewById(R.id.rv_orderorder);

        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );
        setSupportActionBar( menuToolbar );

        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle(null);

        food = food_start.this;

        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_order.setLayoutManager(manager);
        rv_order.setAdapter(adapter);
        getOrder();

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
                        break;

                    case R.id.food_menu:
                        Intent intent_menu = new Intent(getApplicationContext(), food_menu.class);
                        startActivity(intent_menu);
                        break;

                    case R.id.food_order_receipt:
                        Intent intent_order_receipt = new Intent(getApplicationContext(), food_order_receipt.class);
                        startActivity(intent_order_receipt);
                        break;

                    case R.id.food_cashout:
                        Intent intent_favorite = new Intent(getApplicationContext(), food_cashout.class);
                        startActivity(intent_favorite);
                        break;

                    case R.id.food_cashout_receipt:
                        Intent intent_question = new Intent(getApplicationContext(), food_cashout_receipt.class);
                        startActivity(intent_question);
                        break;

                    case R.id.logout:
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
            case R.id.go_main:  //홈 버튼
                // User chose the "Settings" item, show the app settings UI...
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void onClick(View view){
        switch (view.getId())
        {
            case R.id.btn_manage:
                PopupMenu order_pop = new PopupMenu(this, view);
                order_pop.setOnMenuItemClickListener(this);
                order_pop.inflate(R.menu.food_mana);
                order_pop.show();
                break;

            case R.id.btn_order_receipt:
                Intent intent1 = new Intent(this, food_order_receipt.class);
                startActivity(intent1);
                break;
            case R.id.btn_cashout:
                Intent intent2 = new Intent(this, food_cashout.class);
                startActivity(intent2);
                break;
            case R.id.btn_cashout_receipt:
                Intent intent3 = new Intent(this, food_cashout_receipt.class);
                startActivity(intent3);
                break;
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem)
    {

        switch(menuItem.getItemId())
        {
            case R.id.food_regist:
                Intent intent = new Intent(this, food_store_manage.class);
                startActivity(intent);
                return true;
            case R.id.food_menu:
                Intent intent1 = new Intent(this, food_menu.class);
                startActivity(intent1);
                return true;

            default:
                return false;
        }
    }

    void getOrder(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                FirebaseDatabase.getInstance().getReference().child("orders").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                        adapter.orderList.clear();

                        for(DataSnapshot item : dataSnapshot2.getChildren()){
                            if(dataSnapshot.child("userName").getValue().toString().equals(item.child("shop").getValue().toString()) == true &&
                                    uid.equals(item.child("uid").getValue().toString()) == false &&
                                    item.child("status").getValue().toString().equals("배정대기") == true){
                                food_start_model model = item.getValue(food_start_model.class);
                                adapter.orderList.add(0, model);
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
