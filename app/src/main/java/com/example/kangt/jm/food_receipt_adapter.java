package com.example.kangt.jm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kangt.jm.model.food_receipt_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class food_receipt_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<food_receipt_model> receiptList = new ArrayList<>();
    public static food_receipt_adapter mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView menupic;
        TextView txt_time;
        TextView txt_uid;
        TextView txt_orderer;
        TextView txt_menu;
        TextView txt_price;
        TextView txt_status;
        TextView txt_seat;
        TextView txt_deliveryUid;
        TextView txt_deliverMan;

        String deliveryTime;
        String userPhone;
        String deliveryPhone;
        String stadium;
        String store;

        public final View mView;


        public MyViewHolder(@NonNull View view) {
            super(view);
            menupic = view.findViewById(R.id.menupic);
            txt_time = view.findViewById(R.id.txt_time);
            txt_uid = view.findViewById(R.id.txt_uid);
            txt_orderer = view.findViewById(R.id.txt_orderName);
            txt_menu = view.findViewById(R.id.txt_menu);
            txt_price = view.findViewById(R.id.txt_price);
            txt_status = view.findViewById(R.id.txt_status);
            txt_seat = view.findViewById(R.id.txt_seat);
            txt_deliveryUid = view.findViewById(R.id.txt_deliveryUid);
            txt_deliverMan = view.findViewById(R.id.txt_deliveryMan);

            mView = view;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_receipt_recyclerview, viewGroup, false);
        mContext = this;
        return new food_receipt_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder)viewHolder;

        String str = receiptList.get(i).getMeassage();
        String[] str1 = str.split(" "); // 냉우동1
        //if(i <= 52) {
            str1[0] = str.substring(0, str1[0].length() - 1);
        //}
        Log.e("로그",String.valueOf(i));

        //Random rnd = new Random();
        //int ra = rnd.nextInt(5)+1;
        switch (str1[0])
        {
            case "통통만두":
                holder.menupic.setImageResource(R.drawable.t);
                break;
            case "냉우동":
                holder.menupic.setImageResource(R.drawable.tt);
                break;
            case "열무국수":
                holder.menupic.setImageResource(R.drawable.ttt);
                break;
            case "우동":
                holder.menupic.setImageResource(R.drawable.tttt);
                break;
            case "짜장면":
                holder.menupic.setImageResource(R.drawable.ttttt);
                break;
            case "불막창":
                holder.menupic.setImageResource(R.drawable.menu_ame_gob);
                break;
            case "왕족발":
                holder.menupic.setImageResource(R.drawable.menu_ame_jokbal);
                break;
            case "불닭발":
                holder.menupic.setImageResource(R.drawable.menu_ame_cf);
                break;
            case "삼겹살":
                holder.menupic.setImageResource(R.drawable.menu_ame_sam);
                break;
            case "소떡소떡":
                holder.menupic.setImageResource(R.drawable.menu_ame_sotteok);
                break;
            case "제육김치":
                holder.menupic.setImageResource(R.drawable.menu_gong_jeyuk);
                break;
            case "갈릭떡갈비":
                holder.menupic.setImageResource(R.drawable.menu_gong_garlic);
                break;
            case "양념갈비":
                holder.menupic.setImageResource(R.drawable.menu_gong_galbi);
                break;
            case "치즈날치알":
                holder.menupic.setImageResource(R.drawable.menu_gong_cheese);
                break;
            case "참치김치":
                holder.menupic.setImageResource(R.drawable.menu_gong_tuna);
                break;
            case "떡볶이":
                holder.menupic.setImageResource(R.drawable.menu_gong_tteok);
                break;
            default:
                holder.menupic.setImageResource(R.drawable.eme);
                break;

        }

        holder.txt_time.setText(receiptList.get(i).getOrdertime()); //날짜 + 시간 이걸 뒤를 잘라서 비교하면됨
        holder.txt_uid.setText(receiptList.get(i).getUid());
        holder.txt_menu.setText(receiptList.get(i).getMeassage());
        holder.txt_price.setText(receiptList.get(i).getPrice().toString()+"P");
        holder.txt_status.setText(receiptList.get(i).getStatus());
        holder.txt_seat.setText(receiptList.get(i).getSeat());
        holder.txt_deliveryUid.setText(receiptList.get(i).getDeliveryUid());

        holder.deliveryTime = receiptList.get(i).getDeliveryComplete();
        holder.stadium = receiptList.get(i).getStadium();
        holder.store = receiptList.get(i).getShop();

        FirebaseDatabase.getInstance().getReference().child("users").child("user").child(holder.txt_uid.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.txt_orderer.setText(dataSnapshot.child("userName").getValue().toString());
                holder.userPhone = dataSnapshot.child("userPhone").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(holder.txt_deliveryUid.getText().toString().length() > 0){
            FirebaseDatabase.getInstance().getReference().child("users").child("user").child(holder.txt_deliveryUid.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    holder.txt_deliverMan.setText(dataSnapshot.child("userName").getValue().toString());
                    holder.deliveryPhone = dataSnapshot.child("userPhone").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                if(holder.deliveryTime == null && holder.txt_deliveryUid.getText().toString() == ""){
                    //배정대기 or 배달대기
                    dlg.setMessage("주문시간 : " + holder.txt_time.getText().toString() +
                            "\n\n주문자 : " + holder.txt_orderer.getText().toString() + "(" + holder.userPhone + ")" +
                            "\n\n경기장 : " + holder.stadium +
                            "\n\n 좌 석 : " + holder.txt_seat.getText().toString() +
                            "\n\n가게명 : " + holder.store +
                            "\n\n주문내역\n" + holder.txt_menu.getText().toString() +
                            "\n\n 수 입 : " + holder.txt_price.getText().toString());
                } else if (holder.deliveryTime == null && holder.txt_deliveryUid.getText().toString() != ""){
                    //배달중
                    dlg.setMessage("주문시간 : " + holder.txt_time.getText().toString() +
                            "\n\n주문자 : " + holder.txt_orderer.getText().toString() + "(" + holder.userPhone + ")" +
                            "\n\n경기장 : " + holder.stadium +
                            "\n\n 좌 석 : " + holder.txt_seat.getText().toString() +
                            "\n\n가게명 : " + holder.store +
                            "\n\n배달원 : " + holder.txt_deliverMan.getText().toString() + "(" + holder.deliveryPhone + ")" +
                            "\n\n주문내역\n" + holder.txt_menu.getText().toString() +
                            "\n\n 수 입 : " + holder.txt_price.getText().toString());
                }else if(holder.deliveryTime != null && holder.txt_deliveryUid.getText().toString() != ""){
                    //배달완료
                    dlg.setMessage("주문시간 : " + holder.txt_time.getText().toString() +
                            "\n\n배달시간 : " + holder.deliveryTime +
                            "\n\n주문자 : " + holder.txt_orderer.getText().toString() + "(" + holder.userPhone + ")" +
                            "\n\n경기장 : " + holder.stadium +
                            "\n\n 좌 석 : " + holder.txt_seat.getText().toString() +
                            "\n\n배달원 : " + holder.txt_deliverMan.getText().toString() + "(" + holder.deliveryPhone + ")" +
                            "\n\n가게명 : " + holder.store +
                            "\n\n주문내역\n" + holder.txt_menu.getText().toString() +
                            "\n\n 수 입 : " + holder.txt_price.getText().toString());
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

    @Override
    public int getItemCount() {
        return receiptList.size();
    }
}
