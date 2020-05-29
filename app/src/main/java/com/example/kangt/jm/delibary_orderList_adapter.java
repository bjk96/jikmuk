package com.example.kangt.jm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangt.jm.model.delibary_main_model;
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

public class delibary_orderList_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<delibary_main_model> orderList = new ArrayList<>();
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

    public static delibary_orderList_adapter mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_seat;
        TextView txt_stadium;
        TextView txt_status;
        TextView txt_shop;

        String uid;
        String message;
        String ordertime;
        Integer price;

        public final View mView;
        public Context c;

        public MyViewHolder( View v){
            super(v);
            mView = v;
            c = v.getContext();

            txt_stadium = v.findViewById(R.id.txt_stadium);
            txt_shop = v.findViewById(R.id.txt_shop);
            txt_seat = v.findViewById(R.id.txt_seat);
            txt_status = v.findViewById(R.id.txt_status);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_order, viewGroup, false);
        mContext = this;
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        MyViewHolder holder = (MyViewHolder)viewHolder;

        holder.txt_stadium.setText(orderList.get(i).getStadium());
        holder.txt_shop.setText(orderList.get(i).getShop());
        holder.txt_seat.setText(orderList.get(i).getSeat());
        holder.txt_status.setText(orderList.get(i).getStatus());

        holder.uid = orderList.get(i).getUid();
        holder.message = orderList.get(i).getMeassage();
        holder.ordertime = orderList.get(i).getOrdertime();
        holder.price = orderList.get(i).getPrice();

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(view.getContext());
                dlg.setTitle("배달하시겠습니까?");

                FirebaseDatabase.getInstance().getReference().child("users").child("user").child(orderList.get(i).getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {

                        dlg.setMessage("주문자 : " + dataSnapshot.child("userName").getValue().toString()
                                + "\n\n연락처 : " + dataSnapshot.child("userPhone").getValue().toString()
                                + "\n\n경기장 : " + holder.txt_stadium.getText().toString()
                                + "\n\n 좌 석 : " + holder.txt_seat.getText().toString()
                                + "\n\n가게명 : " + holder.txt_shop.getText().toString()
                                + "\n\n주문내역\n" + holder.message
                                + "\n\n 총 액 : " + holder.price.toString() + "P");

                        dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String deliveryUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Map<String, Object> map = new HashMap<>();

                                FirebaseDatabase.getInstance().getReference().child("orders").orderByChild(deliveryUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange( DataSnapshot dataSnapshot) {

                                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                                            String a = item.child("uid").getValue().toString();
                                            //String b = holder.txt_uid.getText().toString();
                                            String c = item.child("meassage").getValue().toString();
                                            //String d = holder.txt_message.getText().toString();
                                            String e = item.child("ordertime").getValue().toString();
                                            //String f = holder.txt_time.getText().toString();

                                            if (a.equals(holder.uid) == true && c.equals(holder.message) == true && e.equals(holder.ordertime) == true) {
                                                map.put("orders/" + item.getKey() + "/deliveryUid", deliveryUid);
                                                map.put("orders/" + item.getKey() + "/deliveryStart", getTime());
                                                map.put("orders/" + item.getKey() + "/status", "배달중");
                                                FirebaseDatabase.getInstance().getReference().updateChildren(map);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled( DatabaseError databaseError) {

                                    }
                                });

                                Toast.makeText(view.getContext(), "배달을 시작합니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dlg.show();
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {

                    }
                });
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
