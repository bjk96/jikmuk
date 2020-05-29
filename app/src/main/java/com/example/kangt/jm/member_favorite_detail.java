package com.example.kangt.jm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class member_favorite_detail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button btn,btn1,btn2;
    DrawerLayout drawerLayout;
    Toolbar menuToolbar;
    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_favorite_detail);
        btn = findViewById(R.id.btn_order);
        btn1 = findViewById( R.id.btn_bucket);
        btn2 =  findViewById(R.id.btn_back);

        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );
        setSupportActionBar( menuToolbar );

        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle(null);

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
        switch (view.getId()){
            case R.id.btn_order:
                Toast.makeText(this,"주문 버튼을 눌렀을때",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, member_order_pay.class);
                startActivity(intent);
                break;
            case R.id.btn_bucket:
                Toast.makeText(this,"장바구니 버튼을 눌렀을때",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_back:
                Toast.makeText(this,"뒤로가기 버튼을 눌렀을때",Toast.LENGTH_SHORT).show();
                finish(); /*사실상 취소 버튼과 확인 버튼이 동일 정보 변경의 부분을 넣는다면 차이가 생길듯 그전까진 동일*/
                break;
        }
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
                Toast.makeText( getApplicationContext(),"메뉴 버튼 눌림",Toast.LENGTH_SHORT ).show();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
