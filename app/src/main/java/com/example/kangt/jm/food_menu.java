package com.example.kangt.jm;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.kangt.jm.model.food_menu_adapter_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class food_menu<article> extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    RecyclerView storeRecy;
    private food_menu_adapter adapter = new food_menu_adapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(menuToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        storeRecy = findViewById(R.id.storerecycle);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        storeRecy.setLayoutManager(lin);
        storeRecy.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
        storeRecy.setAdapter(adapter);
        getStoreList();

        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        // 네비게이션 뷰 아이템 클릭시 이뤄지는 이벤트
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                // 각 메뉴 클릭시 이뤄지는 이벤트
                switch (id) {
                    case R.id.food_regist:
                        Intent intent_regist = new Intent(getApplicationContext(), food_store_manage.class);
                        startActivity(intent_regist);
                        finish();
                        break;
                    case R.id.food_menu:
                        Intent intent_menu = new Intent(getApplicationContext(), food_store_manage.class);
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
                        Intent intent = new Intent(getApplicationContext(), member_login.class);
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

    @Override
    public boolean onNavigationItemSelected( MenuItem menuItem) {
        return false;
    }

    void getStoreList(){
        FirebaseDatabase.getInstance().getReference().child("Store").child("storeindex").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                adapter.storeList.clear();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(uid.equals(item.child("storeuid").getValue().toString())){
                        food_menu_adapter_model model = item.getValue(food_menu_adapter_model.class);
                        adapter.storeList.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
    }

}

