package com.example.kangt.jm;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangt.jm.model.User_model;
import com.example.kangt.jm.model.game_info;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class member_main extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private Button btn,btn2,btn3,btn4;
    private Toolbar menuToolbar;
    private DrawerLayout drawerLayout;
    private TextView User_name_nav , User_email_nav;
    private FirebaseAuth auth;
    private TextView nameTextView ;
    private TextView emailTextView;
    BaseballAdapter baseballAdapter;
    FootballAdapter footballAdapter;
    games_recylcerview_adapter gamesAdapter = new games_recylcerview_adapter();
    ViewPager vp_baseball;
    ViewPager vp_football;
    RecyclerView rv_games;
    NavigationView navigationView;
    public static Activity member;
    private FirebaseDatabase firebaseDatabase ;
    private List<User_model> userdetas = new ArrayList<>();
    private List<User_model> username = new ArrayList<>();
    private TextView pointTextView;
    private AdView mAdView;
    TextView txt_noGame;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_member_main);
    btn = findViewById( R.id.btn_order );
    btn2 = findViewById( R.id.btn_point_manage );
    btn3 = findViewById( R.id.btn_favorite );
    btn4= findViewById( R.id.btn_customer );
    menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );
    navigationView = (NavigationView)findViewById(R.id.navigationView);
    vp_baseball = (ViewPager)findViewById(R.id.vp_baseball);
    vp_football = (ViewPager)findViewById(R.id.vp_football);
    auth = FirebaseAuth.getInstance();
    firebaseDatabase = FirebaseDatabase.getInstance();
    txt_noGame = findViewById(R.id.txt_noGame);

    member = member_main.this;
    setSupportActionBar( menuToolbar );
    getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
    getSupportActionBar().setDisplayHomeAsUpEnabled( true );
    getSupportActionBar().setTitle(null);
    initLayout(); //네비게이션 메뉴 초기화 함수

    AdView adView = new AdView(this);
    adView.setAdSize(AdSize.BANNER);
    adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

    mAdView = findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);

    View view  = navigationView.getHeaderView(0);
    nameTextView =(TextView) view.findViewById(R.id.User_name_nav);
    emailTextView =(TextView) view.findViewById(R.id.User_email_nav);
    pointTextView = (TextView) view.findViewById(R.id.User_Point_nav);
    emailTextView.setText(auth.getCurrentUser().getEmail());

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    firebaseDatabase.getReference().child("users").child("user").child(uid).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            User_model user_model = dataSnapshot.getValue(User_model.class);
            pointTextView.setText(user_model.getUserPoint());
            nameTextView.setText(user_model.getUserName());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
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
            break;

        case R.id.star:
            Intent intent_favorite = new Intent(getApplicationContext(), member_favorite.class);
            startActivity(intent_favorite);
            break;

        case R.id.customer_center:
            Intent intent_question = new Intent(getApplicationContext(), member_usual_request.class);
            startActivity(intent_question);
            break;

        case R.id.setting:
            //설정은 뭘 넣어야 하는가
            break;

        case R.id.logout:
            //로그아웃 기능
            auth.signOut();
            Toast.makeText(getApplicationContext(), "로그아웃 되셨습니다.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(member_main.this , member_login.class);
            startActivity(intent);
            finish();
            break;
    }

    return true;
}
});
    //여기까지 복붙

    //이미지 슬라이더
    baseballAdapter = new BaseballAdapter(this);
    footballAdapter = new FootballAdapter(this);
    vp_baseball.setAdapter(baseballAdapter);
    vp_football.setAdapter(footballAdapter);

    //경기일정
    rv_games = findViewById(R.id.rv_games);
    rv_games.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));

    LinearLayoutManager manager = new LinearLayoutManager(this);
    rv_games.setLayoutManager(manager);
    rv_games.setAdapter(gamesAdapter);
    getGames();
}
public boolean onNavigationItemSelected(@NonNull MenuItem item){
    switch (item.getItemId()){
        case R.id.logout:
            auth.signOut();
            Toast.makeText(getApplicationContext(), "로그아웃 되셨습니다.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(member_main.this , member_login.class);
            startActivity(intent);
            finish();
            break;
        case R.id.account: //계정
            break;
        case R.id.star: //즐겨찾기
            Intent intentA = new Intent(member_main.this, member_favorite.class);
            startActivity(intentA);
            break;
        case R.id.point: //포인트충전
            Intent intentP = new Intent(getApplicationContext(), member_charge.class);
            startActivity(intentP);
            break;
        case R.id.setting: //설정
            break;
    }
    drawerLayout.closeDrawer(GravityCompat.START);
    return false;
}
private void initLayout(){
    drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    navigationView = (NavigationView) findViewById(R.id.navigationView);
    navigationView.setNavigationItemSelectedListener(this);
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
    switch (item.getItemId())
    {
        case android.R.id.home:
            drawerLayout.openDrawer(GravityCompat.START);

            return super.onOptionsItemSelected(item);

        case R.id.go_main:  //메인화면으로 이동
            return true;

        default:
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            return super.onOptionsItemSelected(item);
    }
}


public void onClick(View view){
    switch (view.getId()) {

        case R.id.btn_order:
            PopupMenu order_pop = new PopupMenu( this, view );
            order_pop.setOnMenuItemClickListener( this );
            order_pop.inflate( R.menu.order_menu );
            order_pop.show();
            break;
        case R.id.btn_point_manage:
            PopupMenu point_pop = new PopupMenu( this, view );
            point_pop.setOnMenuItemClickListener( this );
            point_pop.inflate( R.menu.point_manage );
            point_pop.show();
            break;
        case R.id.btn_favorite:
            Intent intent = new Intent(this, member_favorite.class);
            startActivity(intent);
            break;
        case R.id.btn_customer:
            PopupMenu customer_pop = new PopupMenu( this, view );
            customer_pop.setOnMenuItemClickListener( this );
            customer_pop.inflate( R.menu.customer_center );
            customer_pop.show();
            break;
    }

}

@Override
public boolean onMenuItemClick(MenuItem menuItem)
{

    switch(menuItem.getItemId())
    {
        case R.id.order:
            Intent intent = new Intent(this, member_order.class);
            startActivity(intent);
            return true;
        case R.id.order_receipt:
            Intent intent1 = new Intent(this, member_order_receipt.class);
            startActivity(intent1);
            return true;
        case R.id.point_charge:
            Intent intent3 = new Intent(this, member_charge.class);
            startActivity(intent3);
            return true;
        case R.id.point_receipt:
            Intent intent4 = new Intent(this, member_point.class);
            startActivity(intent4);
            return true;
        case R.id.point_present:
            Intent intent5 = new Intent(this, member_point_present.class);
            startActivity(intent5);
            return true;
        case R.id.usual_requset:
            Intent intent6 = new Intent(this, member_usual_request.class);
            startActivity(intent6);
            return true;
        case R.id.one_request:
            Intent intent7 = new Intent(this, member_request_onevsone.class);
            startActivity(intent7);
            return true;

        default:
            return false;
    }
}

//경기일정 데이터
private void getGames(){
    //임의의 데이터
    List<String> listTeam1 = Arrays.asList("대한민국", "인천전자랜드", "삼성화재", "IBK기업은행");
    List<String> listTeam2 = Arrays.asList("캐나다", "울산현대모비스", "KB손해보험", "흥국생명");
    List<String> listLeague = Arrays.asList("프리미어12", "KBL", "V리그", "V리그");
    List<String> listTime = Arrays.asList("19:00", "19:00", "19:00", "19:00");

    for(int i=0; i<listTeam1.size(); i++){
        game_info data = new game_info(listTeam1.get(i), listTeam2.get(i), listLeague.get(i), listTime.get(i));

        gamesAdapter.addItem(data);
    }

    if(gamesAdapter.getItemCount() > 0)
        txt_noGame.setVisibility(View.GONE);
    else
        txt_noGame.setVisibility(View.VISIBLE);

    gamesAdapter.notifyDataSetChanged();
}

}
