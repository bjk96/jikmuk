package com.example.kangt.jm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class member_order extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar menuToolbar;
    private Button btn,btn1;
    private DrawerLayout drawerLayout;
    private Spinner member_sports , member_stadium_name;
    private String Name;
    private EditText editSeat;

    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;

    public void Stadium(String path){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child(path);
        final long ONE_MEGABYTE = 1920 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                ImageView img = (ImageView)findViewById(R.id.imageView2);
                Bitmap image = BitmapFactory.decodeByteArray( bytes , 0 , bytes.length);
                img.setImageBitmap(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_order);
        btn = findViewById(R.id.btn_next);
        btn1 = findViewById(R.id.btn_back);
        editSeat = findViewById(R.id.editSeat);
        member_sports = (Spinner) findViewById(R.id.member_sports);
        member_stadium_name = (Spinner) findViewById(R.id.member_stadium_name);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(menuToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        member_sports.setAdapter(adapter);
        member_sports.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (member_sports.getSelectedItem().equals("축구")){
                    ArrayAdapter<CharSequence> adapte = ArrayAdapter.createFromResource(member_order.this, R.array.sports_soccer,
                            android.R.layout.simple_spinner_item);
                    adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    member_stadium_name.setAdapter(adapte);
                } else if (member_sports.getSelectedItem().toString().equals("야구")) {
                    ArrayAdapter<CharSequence> adapte = ArrayAdapter.createFromResource(member_order.this, R.array.sports_baseball,
                            android.R.layout.simple_spinner_item);
                    adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    member_stadium_name.setAdapter(adapte);
                } else if (member_sports.getSelectedItem().toString().equals("농구")) {
                    ArrayAdapter<CharSequence> adapte = ArrayAdapter.createFromResource(member_order.this, R.array.sports_basketball,
                            android.R.layout.simple_spinner_item);
                    adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    member_stadium_name.setAdapter(adapte);
                } else if (member_sports.getSelectedItem().toString().equals("배구")) {
                    ArrayAdapter<CharSequence> adapte = ArrayAdapter.createFromResource(member_order.this, R.array.sports_volleyball,
                            android.R.layout.simple_spinner_item);
                    adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    member_stadium_name.setAdapter(adapte);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


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
                        Intent intent = new Intent(getApplicationContext() , member_login.class);
                        startActivity(intent);
                        finish();
                        break;
                }

                return true;
            }
        });

        member_stadium_name.setAdapter(adapter);
        member_stadium_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //다운로드할 파일을 가르키는 참조 만orageReference = storage.getReference();들기
                //StorageReference pathReference = storageReference.child(picUrl);
                if (member_stadium_name.getSelectedItem().equals("서울월드컵경기장(FC 서울)")){
                    Stadium("축구/서울월드컵경기장(FC 서울).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("잠실종합운동장 올림픽주경기장(서울 이랜드 FC)")) {
                    Stadium("축구/잠실종합운동장 올림픽주경기장(서울 이랜드 FC).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("인천축구전용경기장(인천 유나이티드 FC)")) {
                    Stadium("축구/인천축구전용경기장(인천 유나이티드 FC).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("수원월드컵경기장(수원 삼성 블루윙즈 FC)")) {
                    Stadium("축구/수원월드컵경기장(수원 삼성 블루윙즈 FC).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("수원종합운동장(수원 FC)")) {
                    Stadium("축구/수원종합운동장(수원 FC).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("탄천종합운동장(성남 FC)")) {
                    Stadium("축구/탄천종합운동장(성남 FC).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("부천종합운동장(부천 FC 1995)")) {
                    Stadium("축구/부천종합운동장(부천 FC 1995).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("안양종합운동장(FC 안양)")) {
                    Stadium("축구/안양종합운동장(FC 안양).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("안산와스타디움(안산 그리너스 FC)")) {
                    Stadium("축구/안산와스타디움(안산 그리너스 FC).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("춘천송암레포츠타운(강원 FC)")) {
                    Stadium("축구/춘천송암레포츠타운(강원 FC).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("대전월드컵경기장(대전 시티즌 FC)")) {
                    Stadium("축구/대전월드컵경기장(대전 시티즌 FC).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("이순신종합운동장(아산 무궁화 축구단)")) {
                    Stadium("축구/이순신종합운동장(아산 무궁화 축구단).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("전주월드컵경기장(전북 현대 모터스 FC)")) {
                    Stadium("축구/전주월드컵경기장(전북 현대 모터스 FC).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("광주월드컵경기장(광주 FC)")) {
                    Stadium("축구/광주월드컵경기장(광주 FC).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("광양축구전용구장(전남 드래곤즈 FC)")) {
                    Stadium("축구/광양축구전용구장.png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("포항스틸야드(FC 포항 스틸러스)")) {
                    Stadium("축구/포항스틸야드(FC 포항 스틸러스.png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("상주시민운동장(상주 상무 축구단)")) {
                    Stadium("축구/상주시민운동장(상주 상무 축구단).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("구덕종합운동장(부산 아이파크 FC)")) {
                    Stadium("축구/구덕종합운동장(부산 아이파크 FC).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("울산문수축구경기장(울산 현대 축구단)")) {
                    Stadium("축구/울산문수축구경기장(울산 현대 축구단).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("창원축구센터(경남 FC)")) {
                    Stadium("축구/창원축구센터(경남 FC).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("제주월드컵경기장(제주 유나이티드 FC)")) {
                    Stadium("축구/제주월드컵경기장(제주 유나이티드 FC.png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("잠실야구장(두산 베어스, LG 트윈스)")) {
                    Stadium("야구/잠실야구장(두산 베어스,LG트윈스).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("고척스카이돔(키움 히어로즈)")) {
                    Stadium("야구/고척스카이돔(키움 히어로즈).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("인천 SK 행복드림구장(SK 와이번스)")) {
                    Stadium("야구/인천 SK 행복드림구장(SK 와이번스).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("수원 KT 위즈 파크(KT 위즈)")) {
                    Stadium("야구/수원 KT 위즈 파크(KT 위즈).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("한화생명 이글스 파크(한화 이글스)")) {
                    Stadium("야구/한화생명 이글스 파크(한화 이글스).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("광주KIA챔피언스필드(KIA 타이거즈)")) {
                    Stadium("야구/광주기아챔피언스필드(KIA 타이거즈).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("대구 삼성 라이온즈 파크(삼성 라이온즈)")) {
                    Stadium("야구/대구 삼성 라이온즈 파크(삼성 라이온즈).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("사직야구장(롯데 자이언츠)")) {
                    Stadium("야구/사직야구장(롯데 자이언츠).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("잠실체육관(서울 삼성 썬더스)")) {
                    Stadium("농구/잠실체육관(서울 삼성 썬더스).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("잠실학생체육관(서울 SK 나이츠)")) {
                    Stadium("농구/잠실학생체육관(서울 SK 나이츠).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("삼산월드체육관(인천 전자랜드 엘리펀츠)")) {
                    Stadium("농구/삼산월드체육관(인천 전자랜드 엘리펀츠).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("고양체육관(고양 오리온 오리온스)")) {
                    Stadium("농구/고양체육관(고양 오리온 오리온스).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("안양체육관(안양 KGC 인삼공사)")) {
                    Stadium("농구/안양체육관(안양 KGC 인삼공사).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("원주종합체육관(원주 DB 프로미)")) {
                    Stadium("농구/원주종합체육관(원주 DB 프로미).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("전주실내체육관(전주 KCC 이지스)")) {
                    Stadium("농구/전주실내체육관(전주 KCC 이지스).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("사직실내체육관(부산 kt 소닉붐)")) {
                    Stadium("농구/사직실내체육관(부산 kt 소닉붐).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("동천체육관(울산 현대모비스 피버스)")) {
                    Stadium("농구/동천체육관(울산 현대모비스 피버스).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("창원실내체육관(창원 LG 세이커스)")) {
                    Stadium("농구/창원실내체육관(창원 LG 세이커스).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("도원실내체육관(인천 신한은행 에스버드)")) {
                    Stadium("농구/도원실내체육관(인천 신한은행 에스버드.png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("서수원칠보체육관(수원 OK저축은행 읏샷)")) {
                    Stadium("농구/서수원칠보체육관(수원 OK저축은행 읏샷).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("용인실내체육관(용인 삼성생명 블루밍스)")) {
                    Stadium("농구/용인실내체육관(용인 삼성생명 블루밍스).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("부천실내체육관(부천 KEB하나은행)")) {
                    Stadium("농구/부천실내체육관(부천 KEB 하나은행).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("청주실내체육관(청주 KB 스타즈)")) {
                    Stadium("농구/청주실내체육관(청주 KB 스타즈).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("이순신빙상장실내체육관(아산 우리은행 위비)")) {
                    Stadium("농구/이순신빙상장실내체육관(아산 우리은행 위비).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("장충체육관(서울 우리카드 위비, GS칼텍스 서울 KIXX 배구단)")) {
                    Stadium("배구/장충체육관(서울 우리카드 위비, GS칼텍스 서울 KIXX 배구단).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("인천계양체육관(대한항공 점보스, 흥국생명 핑크스파이더스)")) {
                    Stadium("배구/인천계양체육관(대한항공 점보스, 흥국생명 핑크스파이더스).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("수원실내체육관(수원 한국전력 빅스톰, 현대건설 힐스테이트 배구단)")) {
                    Stadium("배구/수원실내체육관(수원 한국전력 빅스톰, 현대건설 힐스테이트 배구단).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("화성종합경기타운 실내체육관(IBK 기업은행 알토스 배구단)")) {
                    Stadium("배구/화성종합경기타운 실내체육관(IBK 기업은행 알토스 배구단).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("안산상록수체육관(OK저축은행 러시앤캐시)")) {
                    Stadium("배구/안산상록수체육관(OK저축은행 러시앤캐시).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("의정부실내체육관(KB손해보험 스타즈)")) {
                    Stadium("배구/의정부실내체육관(KB손해보험 스타즈).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("대전충무체육관(삼성화재 블루팡스, KGC 인삼공사 배구단)")) {
                    Stadium("배구/대전충무체육관(삼성화재 블루팡스, KGC 인삼공사 배구단).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("천안 유관순체육관(현대캐피탈 스카이워커스)")) {
                    Stadium("배구/천안 유관순체육관(현대캐피탈 스카이워커스).png");
                }
                else if (member_stadium_name.getSelectedItem().toString().equals("김천실내체육관(한국도로공사 하이패스 배구단)")) {
                    Stadium("배구/김천실내체육관(한국도로공사 하이패스 배구단).png");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //버튼 활성화 비활성화
        editSeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editSeat.length() == 0){
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

    }

    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.btn_next:
                String member_sports_name = member_sports.getSelectedItem().toString();
                String member_sports_stadium_name = member_stadium_name.getSelectedItem().toString();
                String seat = editSeat.getText().toString();
                Intent intent = new Intent(this, member_order_menu.class);
                intent.putExtra("member_sports_Name", member_sports_name);	//member_sports_Name 이라는 스트링으로 member_sports_name 값을 넘긴다.
                intent.putExtra("member_stadium_Name", member_sports_stadium_name);
                intent.putExtra("seatNumber", seat);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_back:
                finish();
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
}
