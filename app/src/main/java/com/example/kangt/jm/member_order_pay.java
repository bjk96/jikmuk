package com.example.kangt.jm;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangt.jm.model.Message;
import com.example.kangt.jm.model.NotificationModel;
import com.example.kangt.jm.model.User_model;
import com.example.kangt.jm.model.member_present_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class member_order_pay extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Button btn,btn1;
    TextView txt_stadium, point_price, txt_shop, txt_price_pay, txt_deliveryPrice;
    EditText txt_seat;
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    private TextView user_pointA;
    private User_model user_modelMessage;
    private TextView order_contents;
    Gson gson = new Gson();
    NotificationModel notificationModel = new NotificationModel();
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_order_pay);
        btn = findViewById(R.id.btn_pay);
        btn1 = findViewById(R.id.btn_back);
        txt_stadium = findViewById(R.id.txt_stadium);
        txt_seat = findViewById(R.id.txt_seat);
        txt_shop = findViewById(R.id.txt_shop);
        point_price = findViewById(R.id.point_price);
        txt_price_pay = findViewById(R.id.point_price_total);
        txt_deliveryPrice = findViewById(R.id.point_delivery);
        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );
        user_pointA = (TextView)findViewById(R.id.user_pointA);
        order_contents = (TextView)findViewById(R.id.order_contents);
        setSupportActionBar( menuToolbar );

        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle(null);

        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        String uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User_model userModel = dataSnapshot.getValue(User_model.class);
                user_pointA.setText(userModel.getUserPoint());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });


        //경기장정보, 좌석정보 불러오기
        Intent intent = getIntent();
        String str_stadium = intent.getStringExtra("member_stadium_name");
        String str_seat = intent.getStringExtra("seat");
        String str_price = intent.getStringExtra("price");
        String str_menu = intent.getStringExtra("menucontent");
        String str_store = intent.getStringExtra("storename");
        txt_stadium.setText(str_stadium);

        if(str_seat.length() == 0){
            txt_seat.setEnabled(true);
            btn.setEnabled(false);
            btn.setBackgroundResource(R.drawable.btn_gray);
        }else{
            txt_seat.setText(str_seat);
            txt_seat.setBackground(null);
            txt_seat.setEnabled(false);
            txt_seat.setTextColor(Color.GRAY);
            btn.setEnabled(true);
            btn.setBackgroundResource(R.drawable.btn_yellow);
        }

        point_price.setText(str_price);
        order_contents.setText(str_menu);
        txt_shop.setText(str_store);

        Integer price, delivery, total;
        price = Integer.parseInt(point_price.getText().toString());
        delivery = Integer.parseInt(txt_deliveryPrice.getText().toString());
        total = price + delivery;
        txt_price_pay.setText(total.toString());

        //버튼 활성/비활성
        txt_seat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txt_seat.length() == 0){
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                }else{
                    btn.setEnabled(true);
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Back();
    }

    void Back()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("뒤로가기");
        builder.setMessage("메인화면으로 돌아가시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent( member_order_pay.this, member_main.class );
                        startActivity( intent );
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


    public void onClick(View view){

    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("결제");
        builder.setMessage("결제하시겠습니까?");
        builder.setPositiveButton("예",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    Intent intent = getIntent();
                    String str_stadium = intent.getStringExtra("storename");
                    databaseReference.child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                User_model user_model = dataSnapshot.getValue(User_model.class);
                                notificationModel.to = user_model.getPushToken();

                                String uid = firebaseAuth.getCurrentUser().getUid();
                                String User_Point = user_model.getUserPoint();
                                String str = point_price.getText().toString();
                                String seat = txt_seat.getText().toString();
                                String stadium = txt_stadium.getText().toString();
                                String status = "배정대기";
                                String shop = txt_shop.getText().toString();
                                String deliver_point = txt_deliveryPrice.getText().toString();

                                int strint = Integer.parseInt(User_Point);
                                int strint2 = Integer.parseInt(str);
                                int hap = strint - strint2;
                                String userpoint = String.valueOf(hap);

                                int delivery = Integer.parseInt(deliver_point);

                                user_model.setUserPoint(userpoint);
                                Map<String, Object> map = new HashMap<>();
                                map.put("users/user/" + uid + "/userPoint", userpoint);
                                databaseReference.updateChildren(map);

                                Message message = new Message(uid.toString(), order_contents.getText().toString(), getTime(), seat, stadium, status, shop, delivery, strint2);
                                databaseReference.child("orders").push().setValue(message);

                                String before = user_pointA.getText().toString();
                                String using = "-"+txt_price_pay.getText().toString();
                                Integer b = Integer.parseInt(before);
                                Integer u = Integer.parseInt(txt_price_pay.getText().toString());
                                Integer a = b-u;
                                String after = a.toString();
                                member_present_model model = new member_present_model(getTime(), uid, before, using, after, "결제", shop, stadium);
                                FirebaseDatabase.getInstance().getReference().child("point_receipt_member").push().setValue(model);
                            }

                            @Override
                            public void onCancelled (@NonNull DatabaseError databaseError){

                            }
                        });

                    sendFcm();

                    //intent = new Intent( member_order_pay.this, member_main.class );
                    //startActivity(intent);

                    Toast.makeText(member_order_pay.this, "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog.Builder dlg_nopay = new AlertDialog.Builder(this);
        dlg_nopay.setMessage("포인트가 부족합니다.");
        dlg_nopay.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        if(Integer.parseInt(user_pointA.getText().toString()) > Integer.parseInt(txt_price_pay.getText().toString()))
            builder.show();
        else dlg_nopay.show();
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

    void sendFcm(){

     /*   notificationModel.to = user_modelMessage.pushToken;
        Log.d("user_modelMessage.pushToke","user_modelMessage.pushToken"+notificationModel.to);
        */
        notificationModel.notificationModel.text = order_contents.getText().toString();
        notificationModel.notificationModel.title = "주문내용" ;

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(notificationModel));
        Request request = new Request.Builder().header("Content-Type","application/json")
                .addHeader("Authorization","key=AIzaSyDgAr0Xbd3erHc_qnS6rVQGKpc5LGpJrOY")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure (Call call, IOException e) {

            }

            @Override
            public void onResponse (Call call, Response response) throws IOException {

            }
        });

        switch (txt_shop.getText().toString()) {
            case "통밥":
                databaseReference.child("users").child("user").child("dP3MnertznRzqnPGQmlA0szrKk22").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User_model user_model = dataSnapshot.getValue(User_model.class);
                        String mPushToken = user_model.getPushToken();
                        SendNotification.sendNotification(mPushToken,  order_contents.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case "어메불족발곱창":
                databaseReference.child("users").child("user").child("6ArAOXgWqFRjMUeSflAaS0KHhXM2").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User_model user_model = dataSnapshot.getValue(User_model.class);
                        String mPushToken = user_model.getPushToken();
                        SendNotification.sendNotification(mPushToken,  order_contents.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case "공씨네주먹밥":
                databaseReference.child("users").child("user").child("mh13MphKUDZBJYQ3upwG3XzUOkA2").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User_model user_model = dataSnapshot.getValue(User_model.class);
                        String mPushToken = user_model.getPushToken();
                        SendNotification.sendNotification(mPushToken,  order_contents.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case "꼬꼬닭":
                databaseReference.child("users").child("user").child("pF5XpOdw2waJBEGoum6u0mDprJy2").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User_model user_model = dataSnapshot.getValue(User_model.class);
                        String mPushToken = user_model.getPushToken();
                        SendNotification.sendNotification(mPushToken,  order_contents.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case "BHC":
                databaseReference.child("users").child("user").child("qwVM2WiXZMVcUDR9581c4sQjKj02").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User_model user_model = dataSnapshot.getValue(User_model.class);
                        String mPushToken = user_model.getPushToken();
                        SendNotification.sendNotification(mPushToken,  order_contents.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
        }



    }



    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
