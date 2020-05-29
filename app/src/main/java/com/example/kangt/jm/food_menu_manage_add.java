package com.example.kangt.jm;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.CursorLoader;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class food_menu_manage_add extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener  {
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;

    private ImageView imageView;
    private static final String TAG = "Food_menu";
    private EditText menuname;
    private EditText menuprice;
    private FirebaseStorage storage;
    private FirebaseDatabase storedb;
    private static final int GALLERY_CODE = 10;
    private String imagePath,imagename ,imgname;
    private String fileUrl;
    private Uri uri,file;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu_manage_add);

        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(menuToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        imageView = findViewById(R.id.menuimageView);
        menuname = findViewById(R.id.txt_menuname_add);
        menuprice = findViewById(R.id.txt_menuprice_add);
        storage = FirebaseStorage.getInstance();
        btn = findViewById(R.id.btn_add);
        storedb = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        //버튼 활성화/비활성화
        menuname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(menuname.length() < 1 || menuprice.length() < 1){
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    btn.setEnabled(true);
                    btn.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        menuprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(menuname.length() < 1 || menuprice.length() < 1){
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.btn_gray);
                } else {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }

        String getstorename = getIntent().getStringExtra("getstorename");
    }

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
    public void onClick (View view) {
        switch (view.getId()) {
            case R.id.menuimageView:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                startActivityForResult(intent,GALLERY_CODE);
                break;

            case R.id.btn_add:
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setMessage("메뉴가 추가되었습니다.");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upload(imagePath);
                        finish();
                    }
                });
                dlg.show();
                break;

            case R.id.btn_menu_back:
                finish();
                break;
        }
    }


    public void upload(String uri)
    {
        StorageReference storageRef = storage.getReferenceFromUrl("gs://jmjm-ce4b7.appspot.com");
        StorageReference riversRef = storageRef.child("menulist/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                if(menuname.toString().length() != 0 && menuprice.toString().length() != 0) {
                    storenameC storenameC = new storenameC();
                     storenameC.imageUrl = riversRef.toString();
                     storenameC.storename = getIntent().getStringExtra("getstorename");
                     storenameC.menuname = menuname.getText().toString();
                     storenameC.menuprice = menuprice.getText().toString();
                     storedb.getReference().child("images").push().setValue(storenameC);
                    menuname.setText("");
                    menuprice.setText("");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.camera, getApplicationContext().getTheme()));}
                else
                {
                    Toast.makeText(getApplicationContext(),"값을 입력해 주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if (requestCode == GALLERY_CODE) {
            file = Uri.fromFile(new File(getPath(data.getData())));
            imageView.setImageURI(file);

        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
    public String getPath(Uri uri){
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        imgname = cursor.getString(index);
        return cursor.getString(index);
    }

}
