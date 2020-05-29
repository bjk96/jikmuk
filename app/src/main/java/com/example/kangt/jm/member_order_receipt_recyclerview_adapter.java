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

import com.example.kangt.jm.model.member_order_receipt_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.kangt.jm.R.drawable.camera;

public class member_order_receipt_recyclerview_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //public Object MyViewHolder;
    ArrayList<member_order_receipt_model> order_receipt_models = new ArrayList<>();

    public static member_order_receipt_recyclerview_adapter mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView pic_menu;
        TextView txt_menuname;
        TextView txt_time;
        TextView txt_uid;
        TextView txt_price;
        TextView txt_status;
        TextView txt_stadium;
        TextView txt_store;

        String shop;
        String deliveryUid;
        String deliveryComplete;
        String deliveryName;
        String deliveryPhone;

        public final View mView;

        MyViewHolder(View v){
            super(v);
            pic_menu = v.findViewById(R.id.menupic2);
            txt_menuname = v.findViewById(R.id.txt_menuname);
            txt_time = v.findViewById(R.id.txt_time);
            txt_uid = v.findViewById(R.id.txt_uid);
            txt_price = v.findViewById(R.id.txt_price);
            txt_status = v.findViewById(R.id.txt_status);
            txt_stadium = v.findViewById(R.id.txt_stadium);
            txt_store = v.findViewById(R.id.txt_store);

            mView = v;
        }
    }

    void addItem(member_order_receipt_model data){
        order_receipt_models.add(data);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderdetail, parent, false);
        mContext = this;
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){
        MyViewHolder viewHolder = (MyViewHolder)holder;

        viewHolder.txt_menuname.setText(order_receipt_models.get(position).getMeassage());
        viewHolder.txt_time.setText(order_receipt_models.get(position).getOrdertime());
        viewHolder.txt_uid.setText(order_receipt_models.get(position).getUid());
        viewHolder.txt_price.setText(order_receipt_models.get(position).getPrice().toString()+"P");
        viewHolder.txt_status.setText(order_receipt_models.get(position).getStatus());
        viewHolder.txt_stadium.setText(order_receipt_models.get(position).getStadium());
        viewHolder.txt_store.setText(order_receipt_models.get(position).getShop());

        viewHolder.shop = order_receipt_models.get(position).getShop();
        viewHolder.deliveryUid = order_receipt_models.get(position).getDeliveryUid();
        viewHolder.deliveryComplete = order_receipt_models.get(position).getDeliveryComplete();

        switch (order_receipt_models.get(position).getShop()){
            case "어메불족발곱창":
                viewHolder.pic_menu.setImageResource(R.drawable.amebul);
                break;
            case "통밥":
                viewHolder.pic_menu.setImageResource(R.drawable.ttong);
                break;
            case "공씨네주먹밥":
                viewHolder.pic_menu.setImageResource(R.drawable.gong);
                break;
            case "BHC":
                viewHolder.pic_menu.setImageResource(R.drawable.bhc);
                break;
            case "꼬꼬닭":
                viewHolder.pic_menu.setImageResource(R.drawable.ggo);
                break;
            default:
                viewHolder.pic_menu.setImageResource(R.drawable.eme);
                break;
        }

        if(viewHolder.deliveryUid != null){
            FirebaseDatabase.getInstance().getReference().child("users").child("user").child(viewHolder.deliveryUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    viewHolder.deliveryName = dataSnapshot.child("userName").getValue().toString();
                    viewHolder.deliveryPhone = dataSnapshot.child("userPhone").getValue().toString();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {           }
            });
        }


        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());

                if(viewHolder.deliveryComplete != null){
                    dlg.setMessage("주문시간 : " + viewHolder.txt_time.getText().toString() +
                            "\n\n배달시간 : " + viewHolder.deliveryComplete +
                            "\n\n배달원 : " + viewHolder.deliveryName + "(" + viewHolder.deliveryPhone + ")" +
                            "\n\n경기장 : " + viewHolder.txt_stadium.getText().toString() +
                            "\n\n가게명 : " + viewHolder.shop +
                            "\n\n주문내역\n" + viewHolder.txt_menuname.getText().toString() +
                            "\n\n 총 액 : " + viewHolder.txt_price.getText().toString() +
                            "\n\n" + viewHolder.txt_status.getText().toString());
                } else if(viewHolder.deliveryUid != null){
                    dlg.setMessage("주문시간 : " + viewHolder.txt_time.getText().toString() +
                            "\n\n배달원 : " + viewHolder.deliveryName + "(" + viewHolder.deliveryPhone + ")" +
                            "\n\n경기장 : " + viewHolder.txt_stadium.getText().toString() +
                            "\n\n가게명 : " + viewHolder.shop +
                            "\n\n주문내역\n" + viewHolder.txt_menuname.getText().toString() +
                            "\n\n 총 액 : " + viewHolder.txt_price.getText().toString() +
                            "\n\n" + viewHolder.txt_status.getText().toString());
                } else {
                    dlg.setMessage("주문시간 : " + viewHolder.txt_time.getText().toString() +
                            "\n\n경기장 : " + viewHolder.txt_stadium.getText().toString() +
                            "\n\n가게명 : " + viewHolder.shop +
                            "\n\n주문내역\n" + viewHolder.txt_menuname.getText().toString() +
                            "\n\n 총 액 : " + viewHolder.txt_price.getText().toString() +
                            "\n\n" + viewHolder.txt_status.getText().toString());
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
    public int getItemCount(){
        return order_receipt_models.size();
    }

}
