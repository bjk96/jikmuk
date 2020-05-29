package com.example.kangt.jm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kangt.jm.model.food_point_receipt_model;
import com.example.kangt.jm.model.point_receipt_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class food_point_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<food_point_receipt_model> pointReceipt = new ArrayList<>();
    public static food_point_adapter mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_ordertime;
        TextView txt_before;
        TextView txt_after;
        TextView txt_change;
        TextView txt_type;
        TextView txt_uid;
        TextView txt_stadium;
        TextView txt_shop;
        TextView txt_orderer;

        String ordererName;
        String ordererPhone;
        String deliveryUid;
        String deliveryComplete;
        String deliveryName;
        String deliveryPhone;

        String str_type;

        public final View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            txt_ordertime = itemView.findViewById(R.id.txt_ordertime);
            txt_before = itemView.findViewById(R.id.txt_beforePoint);
            txt_after = itemView.findViewById(R.id.txt_afterPoint);
            txt_change = itemView.findViewById(R.id.txt_changePoint);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_uid = itemView.findViewById(R.id.txt_uid);
            txt_stadium = itemView.findViewById(R.id.txt_stadium);
            txt_shop = itemView.findViewById(R.id.txt_shop);
            txt_orderer = itemView.findViewById(R.id.txt_orderer);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delibary_point_receipt_recyclerview, viewGroup, false);
        mContext = this;
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder)viewHolder;

        //holder.txt_ordertime.setText(pointReceipt.get(i).getOrdertime());
        holder.txt_before.setText(pointReceipt.get(i).getBeforePoint() + "P");
        holder.txt_after.setText(pointReceipt.get(i).getAfterPoint() + "P");
        holder.txt_type.setText(pointReceipt.get(i).getType());
        holder.txt_uid.setText(pointReceipt.get(i).getUid());
        holder.txt_stadium.setText(pointReceipt.get(i).getStadium());
        holder.txt_shop.setText(pointReceipt.get(i).getShop());
        holder.txt_orderer.setText(pointReceipt.get(i).getOderer_uid());

        Integer change = Integer.parseInt(pointReceipt.get(i).getUsingPoint());

        if(holder.txt_type.getText().toString().equals("주문")) {
            holder.txt_ordertime.setText(pointReceipt.get(i).getOrdertime());
            holder.txt_change.setTextColor(0xFF0000FF);
            holder.txt_change.setText("+"+change.toString() + "P");

            String orderer = holder.txt_orderer.getText().toString();
            String oTime = holder.txt_ordertime.getText().toString();

            FirebaseDatabase.getInstance().getReference().child("users").child("user").child(orderer).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    holder.ordererName = dataSnapshot.child("userName").getValue().toString();
                    holder.ordererPhone = dataSnapshot.child("userPhone").getValue().toString();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {       }
            });

            FirebaseDatabase.getInstance().getReference().child("point_receipt_member").orderByChild("users/user/"+orderer).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        String a = ds.child("uid").getValue().toString();
                        String c = ds.child("ordertime").getValue().toString();

                        if(a.equals(orderer) == true && c.equals(oTime) == true && ds.child("deliveryUid").exists() && ds.child("deliveryComplete").exists()){
                            holder.deliveryUid = ds.child("deliveryUid").getValue().toString();
                            holder.deliveryComplete = ds.child("deliveryComplete").getValue().toString();

                            FirebaseDatabase.getInstance().getReference().child("users").child("user").child(holder.deliveryUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange( DataSnapshot dataSnapshot) {
                                    holder.deliveryName = dataSnapshot.child("userName").getValue().toString();
                                    holder.deliveryPhone = dataSnapshot.child("userPhone").getValue().toString();
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {       }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled( DatabaseError databaseError) {      }
            });

            //item click event
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //type = 주문
                    AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());

                    if(holder.deliveryComplete != null){
                        dlg.setMessage("주문시간 : " + oTime +
                                "\n\n배달시간 : " + holder.deliveryComplete +
                                "\n\n" + holder.txt_before.getText().toString() + " > " + holder.txt_after.getText().toString() +
                                "\n\n주문자 : " + holder.ordererName + "(" + holder.ordererPhone + ")" +
                                "\n\n배달원 : " + holder.deliveryName + "(" + holder.deliveryPhone + ")" +
                                "\n\n경기장 : " + holder.txt_stadium.getText().toString() +
                                "\n\n가게명 : " + holder.txt_shop.getText().toString());
                    } else if(holder.deliveryUid != null){
                        dlg.setMessage("주문시간 : " + oTime +
                                "\n\n" + holder.txt_before.getText().toString() + " > " + holder.txt_after.getText().toString() +
                                "\n\n주문자 : " + holder.ordererName + "(" + holder.ordererPhone + ")" +
                                "\n\n배달원 : " + holder.deliveryName + "(" + holder.deliveryPhone + ")" +
                                "\n\n경기장 : " + holder.txt_stadium.getText().toString() +
                                "\n\n가게명 : " + holder.txt_shop.getText().toString());
                    } else {
                        dlg.setMessage("주문시간 : " + oTime +
                                "\n\n" + holder.txt_before.getText().toString() + " > " + holder.txt_after.getText().toString() +
                                "\n\n주문자 : " + holder.ordererName + "(" + holder.ordererPhone + ")" +
                                "\n\n경기장 : " + holder.txt_stadium.getText().toString() +
                                "\n\n가게명 : " + holder.txt_shop.getText().toString());
                    }

                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    dlg.show();
                }
            });
        }
        else {
            holder.txt_ordertime.setText(pointReceipt.get(i).getCashoutTime());
            holder.txt_change.setTextColor(0xFFFF0000);
            holder.txt_change.setText("-" + change.toString() + "P");
        }

    }

    @Override
    public int getItemCount() {
        return pointReceipt.size();
    }
}
