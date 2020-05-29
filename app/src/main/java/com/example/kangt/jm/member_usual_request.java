package com.example.kangt.jm;


import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class member_usual_request extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;

    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    private TextView txt1,txt2,txt3,txt4,txt5,txt6;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_usual_request);
        txt1 = findViewById(R.id.txt_order1);
        txt2 = findViewById(R.id.txt_order2);
        txt3 = findViewById(R.id.txt_del1);
        txt4 = findViewById(R.id.txt_del2);
        txt5 = findViewById(R.id.txt_point1);
        txt6 = findViewById(R.id.txt_point2);
        btn = findViewById(R.id.button_back);
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.txt_order1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Q 주문을 하려는데 결제 오류가 떠요").setMessage("A.포인트가 부족해서 생기는 문제입니다. 고객님 잔여 포인트가 소모 포인트 보다 많은지 확인 해주세요");

                AlertDialog alertDialog = builder.create();

                alertDialog.show();
                break;
            case R.id.txt_order2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

                builder2.setTitle("Q. 한번의 한 가게에서만 주문이 되나요?").setMessage("A. 네 한번 주문시 하나의 가게에서만 주문이 가능합니다,");

                AlertDialog alertDialog2 = builder2.create();

                alertDialog2.show();
                break;
            case R.id.txt_del1:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);

                builder3.setTitle("Q. 언제 배달되는지 알고싶어요.").setMessage("A. 주문 내역에서 주문하신 현재 물품이 어떤상황인지 표기되어 있습니다. 확인해주시기 바랍니다.");

                AlertDialog alertDialog3 = builder3.create();

                alertDialog3.show();
                break;
            case R.id.txt_del2:
                AlertDialog.Builder builder4 = new AlertDialog.Builder(this);

                builder4.setTitle("Q. 배달이 오지 않는것 같습니다.").setMessage("A. 고객님의 현재 좌석과 주문시 기입한 좌석이 같은지 확인해 주시기 바랍니다.");

                AlertDialog alertDialog4 = builder4.create();

                alertDialog4.show();
                break;
            case R.id.txt_point1:
                AlertDialog.Builder builder5 = new AlertDialog.Builder(this);

                builder5.setTitle("Q. 포인트 충전은 어떻게 하나요?").setMessage("A. 처음 메인 화면에서 포인트관리-포인트 충전 을 눌러주시길 바랍니다.");

                AlertDialog alertDialog5 = builder5.create();

                alertDialog5.show();
                break;
            case R.id.txt_point2:
                AlertDialog.Builder builder6 = new AlertDialog.Builder(this);

                builder6.setTitle("Q. 포인트는 어떻게 사용하나요?").setMessage("A. 주문관리 - 주문하기를 누르시면 각 경기장에 등록되어있는 음식을 주문하는데 사용하실 수 있습니다.");

                AlertDialog alertDialog6 = builder6.create();

                alertDialog6.show();
                break;
            case R.id.button_back:
                finish();
                break;
        }
    }
}
