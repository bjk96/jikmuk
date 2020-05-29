package com.example.kangt.jm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.kangt.jm.model.favorite_model;
import com.example.kangt.jm.model.member_menu_model;
import com.example.kangt.jm.model.store_info;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class member_order_menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    public static TextView gTvView;
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;
    Button btn_back, btn_order;
    public static String TAG = member_order_menu.class.getSimpleName();
    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    private Spinner member_sports;
    public static TextView tv_total;
    public static String order;
    private String menucontents;
    int count = 40,menuposition = 0; //이부분을 배열의 개수만큼 수정
    public static ArrayList<String> storelist = null;
    String[] array_word = new String[count];// 주문 내역을 위함
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference storeref = firebaseDatabase.getReference();
    public RequestManager mGlideRequestManager;
    private String[] storenamelist = new String[30];
    ArrayAdapter<Object> adapter1;
    static boolean calledAlready = false;
    public int menucount;
    public  String[] items;
    ArrayAdapter<String> storenameadapter;
    public store_info data = new store_info();
    public TextView member_order_store;
    public static GridView gv;

    private RecyclerView rv_menu;
    private TextView member_menu_stadium_name;
    private member_order_menu_adapter adp = new member_order_menu_adapter();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_order_menu);
        TextView member_menu_sportname = (TextView) findViewById(R.id.member_menu_sportname);
        member_menu_stadium_name = findViewById(R.id.member_menu_stadium_name);
        btn_back = (Button) findViewById(R.id.button3);
        btn_order = (Button) findViewById(R.id.button2);
        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(menuToolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        storelist = new ArrayList<String>();
        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        Intent intent = getIntent();
        String member_sports_name = intent.getStringExtra("member_sports_Name"); //member_sports_name으로 보내온 값을 member_sports_Name 저장
        String member_sports_stadium_name = intent.getStringExtra("member_stadium_Name");
        member_menu_sportname.setText(member_sports_name);
        member_menu_stadium_name.setText(member_sports_stadium_name);

        tv_total = findViewById(R.id.txt_totalprice);
        rv_menu = findViewById(R.id.rv_menu);

        firebaseDatabase.getReference().child("Store").child("storeindex").addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String storename = snapshot.child("storename").getValue(String.class);
                    String storeplace = snapshot.child("place").getValue(String.class);
                     if (storeplace.equals(intent.getStringExtra("member_stadium_Name")))
                    {
                       //여기에 두어야 값이 제대로 들어간다
                        storelist.add(storename);
                        i +=1;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        member_order_store = (TextView) findViewById(R.id.member_order_store);
        member_order_store.setOnClickListener(new member_order_menu()
        {
            @Override
            public void onClick(View v) {
                showdialog(v);
            }
        });

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rv_menu.setLayoutManager(manager);

        member_order_store.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                rv_menu.setAdapter(adp);
                getMenuList();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter1 = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);

       // 네비게이션 뷰 아이템 클릭시 이뤄지는 이벤트
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                // 각 메뉴 클릭시 이뤄지는 이벤트
                switch (id) {
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
                        Intent intent = new Intent(getApplicationContext(), member_login.class);
                        startActivity(intent);
                        finish();
                        break;
                }

                return true;
            }
        });




        tv_total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Integer.parseInt(tv_total.getText().toString()) == 0) {
                    btn_order.setEnabled(false);
                    btn_order.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    btn_order.setEnabled(true);
                    btn_order.setBackgroundResource(R.drawable.btn_yellow);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    //가게 선택 및 주문
    public void showdialog(View v) {
        String[] items = new String[storelist.size()] ;

        if (storelist.size()  == 0 || storelist.isEmpty()){
        }else {
            for (int i = 0; i<storelist.size();i++){
                items[i] = storelist.get(i);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext() );  //이부분에서 오류
        builder.setTitle("가게를 선택해주세요");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos];
                TextView setstore = v.findViewById(R.id.member_order_store);
                setstore.setText(selectedText);
            }
        });
        builder.show();

    }


    public void onBackPressed() {
       Back();
    }


    void Back() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("뒤로가기");
        builder.setMessage("메인화면으로 돌아가시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(member_order_menu.this, member_main.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        }
                });
        builder.show();
    }

    void getMenuList(){
        String str_store = member_order_store.getText().toString();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("images").orderByChild("users/user/"+uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adp.menuList.clear();

                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(str_store.equals(item.child("storename").getValue().toString())){
                        member_menu_model model = item.getValue(member_menu_model.class);
                        adp.menuList.add(model);
                    }
                }
                adp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void show() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setMessage("주문하시겠습니까?");
        dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String stadium = member_menu_stadium_name.getText().toString();
                String total = tv_total.getText().toString();
                String store = member_order_store.getText().toString();

                Intent i = getIntent();

                Intent intent = new Intent(member_order_menu.this, member_order_pay.class);
                intent.putExtra("member_stadium_name", stadium);
                intent.putExtra("seat", i.getStringExtra("seatNumber"));
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
                case R.id.button3:


                    finish();
                    break;

                case R.id.button2:
                    //주문하기 넘어가는 부분
                    show();
                    break;

        }

    }


}

