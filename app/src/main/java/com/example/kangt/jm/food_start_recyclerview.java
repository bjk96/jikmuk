package com.example.kangt.jm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangt.jm.model.food_add_pointReceipt_model;
import com.example.kangt.jm.model.food_start_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class food_start_recyclerview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<food_start_model> orderList = new ArrayList<>();
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    public static food_start_recyclerview mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_orderName;
        TextView txt_menu;
        TextView txt_seat;
        TextView txt_status;

        public final View mView;

        String userPhone;
        String storeName;
        String uid;
        String time;
        Integer price;
        Integer point;
        String stadium;

        public MyViewHolder(@NonNull View v) {
            super(v);

            txt_orderName = v.findViewById(R.id.txt_orderName);
            txt_menu = v.findViewById(R.id.txt_menu);
            txt_seat = v.findViewById(R.id.txt_seat);
            txt_status = v.findViewById(R.id.txt_status);

            mView = v;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_food_order, viewGroup, false);
        mContext = this;
        return new food_start_recyclerview.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder)viewHolder;

        holder.txt_menu.setText(orderList.get(i).getMeassage());
        holder.txt_seat.setText(orderList.get(i).getSeat());
        holder.txt_status.setText(orderList.get(i).getStatus());

        holder.storeName = orderList.get(i).getShop();
        holder.stadium = orderList.get(i).getStadium();
        holder.uid = orderList.get(i).getUid();
        holder.time = orderList.get(i).getOrdertime();
        holder.price = orderList.get(i).getPrice();


        String suid = FirebaseAuth.getInstance().getUid();


        FirebaseDatabase.getInstance().getReference().child("users").child("user").child(orderList.get(i).getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.txt_orderName.setText(dataSnapshot.child("userName").getValue().toString());
                holder.userPhone = dataSnapshot.child("userPhone").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child("user").child(suid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.point = Integer.parseInt(dataSnapshot.child("userPoint").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                dlg.setTitle("배달원을 배정받으시겠습니까?");

                dlg.setMessage(holder.time +
                        "\n\n주문자 : " + holder.txt_orderName.getText().toString() +
                        "\n\n연락처 : " + holder.userPhone +
                        "\n\n경기장 : " + holder.stadium +
                        "\n\n 좌 석 : " + holder.txt_seat.getText().toString() +
                        "\n\n가게명 : " + holder.storeName +
                        "\n\n주문내역\n" + holder.txt_menu.getText().toString() +
                        "\n\n 총 액 : " + holder.price.toString());

                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> map = new HashMap<>();

                        FirebaseDatabase.getInstance().getReference().child("orders").orderByChild("/users/user" + suid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot item : dataSnapshot.getChildren()) {
                                    String a = item.child("uid").getValue().toString();
                                    //String b = holder.txt_uid.getText().toString();
                                    String c = item.child("meassage").getValue().toString();
                                    String d = holder.txt_menu.getText().toString();
                                    String e = item.child("ordertime").getValue().toString();
                                    //String f = holder.txt_time.getText().toString();

                                    if (a.equals(holder.uid) == true && c.equals(d) == true && e.equals(holder.time) == true) {
                                        map.put("orders/" + item.getKey() + "/status", "배달대기");
                                        FirebaseDatabase.getInstance().getReference().updateChildren(map);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        FirebaseDatabase.getInstance().getReference().child("users").child("user").child(suid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Integer total = holder.point + holder.price;
                                map.put("users/user/" + suid + "/userPoint", total.toString());
                                FirebaseDatabase.getInstance().getReference().updateChildren(map);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Integer after = holder.point + holder.price;
                        food_add_pointReceipt_model model = new food_add_pointReceipt_model(holder.point.toString(), holder.price.toString(), after.toString(), suid, getTime(), holder.time, "주문", holder.uid, holder.stadium, holder.storeName);
                        FirebaseDatabase.getInstance().getReference().child("point_receipt_store").push().setValue(model); //오류 나는 부분

                        Toast.makeText(v.getContext(), "배달원 배정을 요청합니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dlg.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
