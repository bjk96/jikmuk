package com.example.kangt.jm;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kangt.jm.model.delibary_main_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class delibary_main extends AppCompatActivity implements   NavigationView.OnNavigationItemSelectedListener{
    private File tempFile;
    private Button btn,btn1,btn2,btn3;
    private RecyclerView rv_orderList;
    private Spinner sp_sports;
    private Spinner sp_stadium;
    private delibary_orderList_adapter orderList_adapter = new delibary_orderList_adapter();
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;

    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;

    public static Activity delivery;

    final static int TAKE_PICTURE = 1;
    private static final int PICK_FROM_ALBUM = 1;
    ImageView imageView;
    final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delibary_main);
        btn = findViewById(R.id.btn_delibary_start);
        btn1 = findViewById(R.id.btn_delivary_delibaryreceipt);
        btn2 = findViewById(R.id.btn_delibary_cashout);
        btn3 = findViewById(R.id.btn_delibary_cashoutreceipt);
        sp_sports = findViewById(R.id.spin_sports);
        sp_stadium = findViewById(R.id.spin_stadium);
        rv_orderList = findViewById(R.id.rv_orderorder);

        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );
        setSupportActionBar( menuToolbar );

        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle(null);

        delivery = delibary_main.this;

        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        View view  = navigationView.getHeaderView(0);
        imageView =(ImageView) view.findViewById(R.id.imageView_nav);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            show();

            }
        });

        //rv_orderList에 adapter set
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_orderList.setLayoutManager(manager);
        rv_orderList.setAdapter(orderList_adapter);
        //getOrderList();

        //spinner set
        setSpinnerSports();
        sp_sports.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSpinnerStadium(sp_sports.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //경기장 선택 시
        sp_stadium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getOrderList(sp_stadium.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getOrderList();
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

                    case R.id.delibary_regist:

                        Intent intent_bucket = new Intent(getApplicationContext(), delibary_start.class);
                        startActivity(intent_bucket);

                        break;

                    case R.id.delibary_receipt:
                        Intent intent_pointcharge = new Intent(getApplicationContext(), delibary_receipt.class);
                        startActivity(intent_pointcharge);
                        break;

                    case R.id.delibary_cashout:
                        Intent intent_favorite = new Intent(getApplicationContext(), delibary_cashout.class);
                        startActivity(intent_favorite);
                        break;

                    case R.id.delibary_cashout_receipt:
                        Intent intent_question = new Intent(getApplicationContext(), delibary_cashout_receipt.class);
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

        // 6.0 마쉬멜로우 이상일 경우에는 권한 체크 후 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }
    // 권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
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
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void onClick (View view)
    {
        switch (view.getId()){

            case R.id.btn_delibary_start:
                Intent intent = new Intent(this, delibary_start.class);
                startActivity(intent);
                break;
            case R.id.btn_delivary_delibaryreceipt:
                Intent intent1 = new Intent(this, delibary_receipt.class);
                startActivity(intent1);
                break;
            case R.id.btn_delibary_cashout:
                Intent intent2 = new Intent(this, delibary_cashout.class);
                startActivity(intent2);
                break;
            case R.id.btn_delibary_cashoutreceipt:
                Intent intent3 = new Intent(this, delibary_cashout_receipt.class);
                startActivity(intent3);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem menuItem) {
        return false;
    }
    private void setImage() {

        ImageView imageView = findViewById(R.id.imageView_nav);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        imageView.setImageBitmap(originalBm);

    }



    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "blackJin_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/blackJin/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != Activity.RESULT_OK) {


            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if (requestCode == TAKE_PICTURE) {

                if (resultCode == RESULT_OK && intent.hasExtra("data")) {
                    Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }

                }

        }
        else if(requestCode == PICK_FROM_ALBUM)
        {
            setImage();
        }
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지 설정하기");
        builder.setMessage("사진 찍기/앨범에서 가져오기");
        builder.setPositiveButton("사진찍기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, TAKE_PICTURE);
                    }
                });
        builder.setNegativeButton("앨범에서 가져오기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, PICK_FROM_ALBUM);
                    }
                });
        builder.show();
    }

    void getOrderList(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                orderList_adapter.orderList.clear();

                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(uid.equals(item.child("uid").getValue().toString()) == false && item.child("status").getValue().toString().equals("배달대기") == true) {
                        delibary_main_model orderList = item.getValue(delibary_main_model.class);
                        orderList_adapter.orderList.add(0, orderList);
                    }
                }
                orderList_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
    }

    void getOrderList(String selectedStadium){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("orders").orderByChild("/users/user"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                orderList_adapter.orderList.clear();

                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(uid.equals(item.child("uid").getValue().toString()) == false && item.child("status").getValue().toString().equals("배달대기") == true
                            && item.child("stadium").getValue().toString().equals(selectedStadium)) {
                        delibary_main_model orderList = item.getValue(delibary_main_model.class);
                        orderList_adapter.orderList.add(0, orderList);
                    }
                }
                orderList_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
    }

    void setSpinnerSports(){
        ArrayAdapter<CharSequence> sp_sports_adapter = ArrayAdapter.createFromResource(this, R.array.sports_array, android.R.layout.simple_spinner_item);
        sp_sports_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sports.setAdapter(sp_sports_adapter);
    }

    void setSpinnerStadium(String selectedSports){
        ArrayAdapter<CharSequence> sp_stadium_adapter;

        if(selectedSports.equals("축구")){
            sp_stadium_adapter = ArrayAdapter.createFromResource(delibary_main.this, R.array.sports_soccer, android.R.layout.simple_spinner_item);
            sp_stadium_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_stadium.setAdapter(sp_stadium_adapter);
        }else if(selectedSports.equals("야구")){
            sp_stadium_adapter = ArrayAdapter.createFromResource(delibary_main.this, R.array.sports_baseball, android.R.layout.simple_spinner_item);
            sp_stadium_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_stadium.setAdapter(sp_stadium_adapter);
        }else if(selectedSports.equals("농구")){
            sp_stadium_adapter = ArrayAdapter.createFromResource(delibary_main.this, R.array.sports_basketball, android.R.layout.simple_spinner_item);
            sp_stadium_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_stadium.setAdapter(sp_stadium_adapter);
        }else if(selectedSports.equals("배구")){
            sp_stadium_adapter = ArrayAdapter.createFromResource(delibary_main.this, R.array.sports_volleyball, android.R.layout.simple_spinner_item);
            sp_stadium_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_stadium.setAdapter(sp_stadium_adapter);
        }
    }
}

