package com.example.kangt.jm;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.kangt.jm.model.menumodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class food_menu_manage extends AppCompatActivity  implements  NavigationView.OnNavigationItemSelectedListener {
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    TextView txt;
    ImageButton ib_add;

    private RecyclerView rv_menu;
    food_menurec_adapter menurec_adapter = new food_menurec_adapter();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference menuRef = firebaseDatabase.getReference();
    public RequestManager mGlideRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu_manage);
        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        txt = findViewById(R.id.txt_getstorename);
        ib_add = findViewById(R.id.ib_add);
        setSupportActionBar(menuToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        mGlideRequestManager = Glide.with(getApplicationContext());
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rv_menu = findViewById(R.id.rv_menu);
        rv_menu.setLayoutManager(manager);
        rv_menu.setAdapter(menurec_adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        String getstorename = getIntent().getStringExtra("getstorename");
        txt.setText(getstorename);
        firebaseDatabase.getReference().child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange ( DataSnapshot dataSnapshot) {
                menurec_adapter.menumodelArrayList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String storename = dataSnapshot1.child("storename").getValue().toString();

                    if( storename.equals(getIntent().getStringExtra("getstorename"))){
                        menumodel model = dataSnapshot1.getValue(menumodel.class);
                        menurec_adapter.menumodelArrayList.add(model);
                    }
                }menurec_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled ( DatabaseError databaseError) {

            }
        });

        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), food_menu_manage_add.class);
                intent.putExtra("getstorename",txt.getText().toString());
                startActivity(intent);
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
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem menuItem) {
        return false;
    }

}


