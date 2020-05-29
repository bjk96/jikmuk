package com.example.kangt.jm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kangt.jm.model.point_receipt_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class delibary_point_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<point_receipt_model> pointReceipt = new ArrayList<>();
    public static delibary_point_adapter mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_time;
        TextView txt_before;
        TextView txt_after;
        TextView txt_change;
        TextView txt_type;
        TextView txt_uid;
        TextView txt_orderer;
        TextView txt_shop;
        TextView txt_stadium;

        String deliveryComplete;
        String ordererName;
        String ordererPhone;

        public final View mView;

        public MyViewHolder( View itemView) {
            super(itemView);

            mView = itemView;

            txt_time = itemView.findViewById(R.id.txt_ordertime);
            txt_before = itemView.findViewById(R.id.txt_beforePoint);
            txt_after = itemView.findViewById(R.id.txt_afterPoint);
            txt_change = itemView.findViewById(R.id.txt_changePoint);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_uid = itemView.findViewById(R.id.txt_uid);
            txt_orderer = itemView.findViewById(R.id.txt_orderer);
            txt_shop = itemView.findViewById(R.id.txt_shop);
            txt_stadium = itemView.findViewById(R.id.txt_stadium);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delibary_point_receipt_recyclerview, viewGroup, false);
        mContext = this;
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder)viewHolder;

        holder.txt_before.setText(pointReceipt.get(i).getBeforePoint() + "P");
        holder.txt_after.setText(pointReceipt.get(i).getAfterPoint() + "P");
        holder.txt_type.setText(pointReceipt.get(i).getType());
        holder.txt_uid.setText(pointReceipt.get(i).getUid());
        holder.txt_orderer.setText(pointReceipt.get(i).getOderer_uid());
        holder.txt_shop.setText(pointReceipt.get(i).getShop());
        holder.txt_stadium.setText(pointReceipt.get(i).getStadium());

        holder.deliveryComplete = pointReceipt.get(i).getTimestamp();

        Integer change = Integer.parseInt(pointReceipt.get(i).getUsingPoint());

        if(holder.txt_type.getText().toString().equals("배달")){
            holder.txt_time.setText(pointReceipt.get(i).getOrdertime());
            holder.txt_change.setTextColor(0xFF0000FF);
            holder.txt_change.setText("+"+change.toString() + "P");

            String orderer = holder.txt_orderer.getText().toString();

            FirebaseDatabase.getInstance().getReference().child("users").child("user").child(orderer).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    holder.ordererName = dataSnapshot.child("userName").getValue().toString();
                    holder.ordererPhone = dataSnapshot.child("userPhone").getValue().toString();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {       }
            });

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                    dlg.setMessage("주문시간 : " + holder.txt_time.getText().toString() +
                            "\n\n배달시간 : " + holder.deliveryComplete +
                            "\n\n" + holder.txt_before.getText().toString() + " > " + holder.txt_after.getText().toString() +
                            "\n\n주문자 : " + holder.ordererName + "(" + holder.ordererPhone + ")" +
                            "\n\n경기장 : " + holder.txt_stadium.getText().toString() +
                            "\n\n가게명 : " + holder.txt_shop.getText().toString());
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dlg.show();
                }
            });
        } else if(holder.txt_type.getText().toString().equals("환전")){
            holder.txt_time.setText(pointReceipt.get(i).getCashoutTime());
            holder.txt_change.setTextColor(0xFFFF0000);
            holder.txt_change.setText("-"+change.toString() + "P");
        }
    }

    @Override
    public int getItemCount() {
        return pointReceipt.size();
    }
}
