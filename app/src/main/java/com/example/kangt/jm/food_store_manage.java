package com.example.kangt.jm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class food_store_manage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener , NavigationView.OnNavigationItemSelectedListener {

    Toolbar menuToolbar;
    DrawerLayout drawerLayout;

    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    Button btn,btn1;
    private Spinner spinner2,spinner1;
    ArrayList<String> gametype;
    ArrayAdapter<CharSequence> arrayAdapter,arrayAdapter1;
    String type,place,storenname,ownername,ownernum,storedetail,ownerid;
    EditText txt,txt1,txt2,txt3;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_store_manage);

        btn = findViewById( R.id.btn_cancel );
        btn1 = findViewById( R.id.btn_confirm );
        txt = findViewById(R.id.txt_storename);
        txt1 = findViewById(R.id.txt_ownername);
        txt2 = findViewById(R.id.txt_ownernumber);
        txt3 = findViewById(R.id.txt_storedetail);

        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout)findViewById( R.id.drawerLayout );
        setSupportActionBar( menuToolbar );

        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_menu_white_24dp );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle(null);


        //버튼 활성화/비활성화
        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {         }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txt.length() == 0 || txt1.length() == 0 || txt2.length() == 0 || txt3.length() == 0){
                    btn1.setEnabled(false);
                    btn1.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    btn1.setEnabled(true);
                    btn1.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {            }
        });
        txt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txt.length() == 0 || txt1.length() == 0 || txt2.length() == 0 || txt3.length() == 0){
                    btn1.setEnabled(false);
                    btn1.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    btn1.setEnabled(true);
                    btn1.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {            }
        });
        txt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txt.length() == 0 || txt1.length() == 0 || txt2.length() == 0 || txt3.length() == 0){
                    btn1.setEnabled(false);
                    btn1.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    btn1.setEnabled(true);
                    btn1.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {            }
        });
        txt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txt.length() == 0 || txt1.length() == 0 || txt2.length() == 0 || txt3.length() == 0){
                    btn1.setEnabled(false);
                    btn1.setBackgroundResource(R.drawable.btn_gray);
                } else {
                    btn1.setEnabled(true);
                    btn1.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        spinner2 = (Spinner)findViewById(R.id.gametypesp);
        spinner1 = (Spinner)findViewById(R.id.gameplace);
        auth = FirebaseAuth.getInstance();

        // 네비게이션 뷰 아이템 클릭시 이뤄지는 이벤트
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                // 각 메뉴 클릭시 이뤄지는 이벤트
                switch (id){
                    case R.id.food_regist:
                    Intent intent_regist = new Intent(getApplicationContext(), food_store_manage.class);
                    startActivity(intent_regist);
                    finish();
                    break;
                    case R.id.food_order_receipt:
                        Intent intent_order_receipt = new Intent(getApplicationContext(), food_order_receipt.class);
                        startActivity(intent_order_receipt);
                        break;

                    case R.id.food_cashout:
                        Intent intent_favorite = new Intent(getApplicationContext(), food_cashout.class);
                        startActivity(intent_favorite);
                        break;

                    case R.id.food_cashout_receipt:
                        Intent intent_question = new Intent(getApplicationContext(), food_cashout_receipt.class);
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


        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.sports_array, android.R.layout.simple_spinner_dropdown_item);

        /*
        arrayAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                R.array.sports_array);
        */

        spinner2.setAdapter(arrayAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(arrayAdapter.getItem(i).equals("축구"))
                {
                    arrayAdapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sports_soccer, android.R.layout.simple_spinner_item);
                    //arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(arrayAdapter1);
                }
                else if(arrayAdapter.getItem(i).equals("야구"))
                {
                    arrayAdapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sports_baseball, android.R.layout.simple_spinner_item);
                    //arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(arrayAdapter1);
                }
                else if(arrayAdapter.getItem(i).equals("농구"))
                {
                    arrayAdapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sports_basketball, android.R.layout.simple_spinner_item);
                    //arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(arrayAdapter1);
                }
                else if(arrayAdapter.getItem(i).equals("배구"))
                {
                    arrayAdapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sports_volleyball, android.R.layout.simple_spinner_item);
                    //arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(arrayAdapter1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    //버튼 활성화 비활성화

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);

                return super.onOptionsItemSelected(item);

            case R.id.go_main:  //홈 버튼
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem)
    {

        switch(menuItem.getItemId())
        {
            case R.id.food_regist:
                Intent intent = new Intent(this, food_store_manage.class);
                startActivity(intent);
                return true;
            case R.id.food_menu:
                Intent intent1 = new Intent(this, food_menu.class);
                startActivity(intent1);
                return true;

            default:
                return false;
        }
    }

    public void onClick (View v){
        switch (v.getId())
        {
            case R.id.btn_confirm:
                storenname = txt.getText().toString();
                ownername = txt1.getText().toString();
                ownernum = txt2.getText().toString();
                storedetail = txt3.getText().toString();
                ownerid = auth.getCurrentUser().getEmail();
                if(txt.length() != 0||txt1.length() != 0||txt2.length() != 0||txt3.length() != 0) {

                    Storeinfo storeinfo = new Storeinfo(
                            spinner2.getSelectedItem().toString(), spinner1.getSelectedItem().toString(),
                            txt.getText().toString(), txt1.getText().toString(), txt2.getText().toString(), txt3.getText().toString(),auth.getCurrentUser().getEmail());
                    databaseReference.child("Store").child("storeindex").push().setValue(storeinfo);

                    txt.setText("");
                    txt1.setText("");
                    txt2.setText("");
                    txt3.setText("");

                }
                else
                {
                    Toast.makeText(this,"입력을 해주세요",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_cancel:
                finish();
                break;

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


}


