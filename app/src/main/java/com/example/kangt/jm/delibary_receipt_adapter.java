package com.example.kangt.jm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangt.jm.model.delibary_add_pointReceipt_model;
import com.example.kangt.jm.model.delibary_receipt_model;
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

@SuppressWarnings("ALL")
public class delibary_receipt_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<delibary_receipt_model> receiptList = new ArrayList<>();
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

    public static delibary_receipt_adapter mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView pic_menu;
        TextView txt_time;
        //TextView txt_uid;
        TextView txt_orderName;
        TextView txt_seat;
        TextView txt_menu;
        TextView txt_status;
        //TextView txt_deliveryPrice;
        //TextView txt_deliveryUid;
        //TextView txt_price;
        //TextView txt_stadium;
        TextView txt_store;
        //TextView txt_userPoint;

        public final View mView;

        String phoneNum;
        String deliveryComplete;
        String uid;
        Integer deliveryPrice;
        String deliveryUid;
        Integer price;
        String stadium;
        String shop;
        Integer userPoint;

        public MyViewHolder(View v) {
            super(v);

            mView = v;
            pic_menu = v.findViewById(R.id.del_menupic);
            txt_time = v.findViewById(R.id.txt_time);
            txt_orderName = v.findViewById(R.id.txt_orderName);
            txt_seat = v.findViewById(R.id.txt_seat);
            txt_menu = v.findViewById(R.id.txt_menu);
            txt_status = v.findViewById(R.id.txt_status);
            txt_store = v.findViewById(R.id.txt_store);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delibary_receipt_recyclerview, viewGroup, false);
        mContext = this;
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder)viewHolder;
        switch (receiptList.get(i).getShop())
        {

            case "어메불족발곱창":
                holder.pic_menu.setImageResource(R.drawable.amebul);
                break;
            case"통밥":
                holder.pic_menu.setImageResource(R.drawable.ttong);
                break;
            case"공씨네주먹밥":
                holder.pic_menu.setImageResource(R.drawable.gong);
                break;
            case"BHC":
                holder.pic_menu.setImageResource(R.drawable.bhc);
                break;
            case"꼬꼬닭":
                holder.pic_menu.setImageResource(R.drawable.ggo);
                break;
            default:
                holder.pic_menu.setImageResource(R.drawable.eme);
                break;

        }
        holder.txt_time.setText(receiptList.get(i).getOrdertime());
        holder.txt_seat.setText(receiptList.get(i).getSeat());
        holder.txt_menu.setText(receiptList.get(i).getMeassage());
        holder.txt_status.setText(receiptList.get(i).getStatus());
        holder.txt_store.setText(receiptList.get(i).getShop());

        holder.deliveryComplete = receiptList.get(i).getDeliveryComplete();
        holder.uid = receiptList.get(i).getUid();
        holder.deliveryPrice = receiptList.get(i).getDeliveryPrice();
        holder.deliveryUid = receiptList.get(i).getDeliveryUid();
        holder.price = receiptList.get(i).getPrice();
        holder.stadium = receiptList.get(i).getStadium();
        holder.shop = receiptList.get(i).getShop();

        FirebaseDatabase.getInstance().getReference().child("users").child("user").child(holder.uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                holder.txt_orderName.setText(dataSnapshot.child("userName").getValue().toString());
                holder.phoneNum = dataSnapshot.child("userPhone").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                //holder.txt_userPoint.setText(dataSnapshot.child("userPoint").getValue().toString());
                holder.userPoint = Integer.parseInt(dataSnapshot.child("userPoint").getValue().toString());
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });

        switch (receiptList.get(i).getStatus()){
            case "배달중":
                holder.txt_status.setTypeface(holder.txt_store.getTypeface(), Typeface.BOLD);
                break;
            case "배달완료":
                holder.txt_status.setTypeface(holder.txt_store.getTypeface(), Typeface.NORMAL);
                break;
            default:
                break;
        }

        //item click event
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //status = 배달중
                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                dlg.setTitle("배달을 완료하시겠습니까?");
                dlg.setMessage("주문시간 : " + holder.txt_time.getText().toString() +
                            "\n\n주문자 : " + holder.txt_orderName.getText().toString() +
                            "\n\n연락처 : " + holder.phoneNum +
                            "\n\n경기장 : " + holder.stadium +
                            "\n\n 좌 석 : " + holder.txt_seat.getText().toString() +
                            "\n\n가게명 : " + holder.shop +
                            "\n\n주문내역\n" + holder.txt_menu.getText().toString());
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
                                    String d = holder.txt_menu.getText().toString();
                                    String e = item.child("ordertime").getValue().toString();
                                    String f = holder.txt_time.getText().toString();

                                    if (a.equals(holder.uid) == true && c.equals(d) == true && e.equals(f) == true) {
                                        map.put("orders/" + item.getKey() + "/status", "배달완료");
                                        map.put("orders/" + item.getKey() + "/deliveryComplete", getTime());
                                        FirebaseDatabase.getInstance().getReference().updateChildren(map);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled( DatabaseError databaseError) {

                            }
                        });

                        FirebaseDatabase.getInstance().getReference().child("users").child("user").child(deliveryUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange( DataSnapshot dataSnapshot) {
                                Integer point = Integer.parseInt(dataSnapshot.child("userPoint").getValue().toString());
                                //Integer deliveryPoint = Integer.parseInt(holder.txt_deliveryPrice.getText().toString());
                                Integer totalPoint = point + holder.deliveryPrice;
                                map.put("users/user/"+deliveryUid+"/userPoint", totalPoint.toString());
                                FirebaseDatabase.getInstance().getReference().updateChildren(map);
                            }

                            @Override
                            public void onCancelled( DatabaseError databaseError) {

                            }
                        });

                        //Integer before = Integer.parseInt(holder.txt_userPoint.getText().toString());
                        //Integer plus = Integer.parseInt(holder.txt_deliveryPrice.getText().toString());
                        Integer after = holder.userPoint + holder.deliveryPrice;
                        String ordertime = holder.txt_time.getText().toString();
                        //String orderer = holder.txt_uid.getText().toString();
                        //String stadium = holder.txt_stadium.getText().toString();
                        //String shop = holder.txt_shop.getText().toString();
                        delibary_add_pointReceipt_model model = new delibary_add_pointReceipt_model(holder.userPoint.toString(), holder.deliveryPrice.toString(), after.toString(), deliveryUid, getTime(), ordertime, "배달", holder.uid, holder.stadium, holder.shop);
                        FirebaseDatabase.getInstance().getReference().child("point_receipt_delivery").push().setValue(model);

                        FirebaseDatabase.getInstance().getReference().child("point_receipt_member").orderByChild("users/user/"+holder.uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            Map<String, Object> map = new HashMap<>();
                            @Override
                            public void onDataChange( DataSnapshot dataSnapshot) {
                                for(DataSnapshot item : dataSnapshot.getChildren()){
                                    String a = item.child("uid").getValue().toString();         //orderer와 비교
                                    String b = item.child("ordertime").getValue().toString();   //ordertime과 비교

                                    if(a.equals(holder.uid) == true && b.equals(ordertime) == true){
                                        map.put("point_receipt_member/"+item.getKey()+"/deliveryUid", holder.deliveryUid);
                                        map.put("point_receipt_member/"+item.getKey()+"/deliveryComplete", getTime());
                                        FirebaseDatabase.getInstance().getReference().updateChildren(map);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled( DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(v.getContext(), "배달을 완료합니다.", Toast.LENGTH_SHORT).show();

                    }
                });
                dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                //status = 배달완료
                AlertDialog.Builder dlg_completed = new AlertDialog.Builder(v.getContext());
                dlg_completed.setMessage("주문시간 : " + holder.txt_time.getText().toString() +
                                "\n\n배달시간 : " + holder.deliveryComplete +
                                "\n\n주문자 : " + holder.txt_orderName.getText().toString() +
                                "\n\n경기장 : " + holder.stadium +
                                "\n\n 좌 석 : " + holder.txt_seat.getText().toString() +
                                "\n\n가게명 : " + holder.shop +
                                "\n\n주문내역\n" + holder.txt_menu.getText().toString());
                dlg_completed.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                switch (receiptList.get(i).getStatus()){
                    case "배달중":
                        dlg.show();
                        break;
                    case "배달완료":
                        dlg_completed.show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return receiptList.size();
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
