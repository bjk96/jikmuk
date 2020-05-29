package com.example.kangt.jm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kangt.jm.model.menumodel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class food_menurec_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<menumodel> menumodelArrayList = new ArrayList<>();
    public static food_menurec_adapter mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //ImageView imageUrl;
        ImageView foodpic;
        TextView menuname;
        TextView menuprice , storename;
        ImageButton ib_delete;

        public final View mView;


        public MyViewHolder(@NonNull View view) {
            super(view);
            foodpic = view.findViewById(R.id.menurowpic);
            menuname = view.findViewById(R.id.txt_menu);
            menuprice = view.findViewById(R.id.point_price);
            storename = view.findViewById(R.id.textView45);
            ib_delete = view.findViewById(R.id.btn_delete);
            //imageUrl = view.findViewById(R.id.imageView1);

            mView = view;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_menu_fow, viewGroup, false);
        mContext = this;
        return new food_menurec_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder)viewHolder;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl( menumodelArrayList.get(i).getImageUrl());
        //menumodelArrayList.get(i).getImageUrl()은 저장소 위치를 받아옴 이후 getDownlOADurl함수로 저장소 위치의 uri값을 가져온다
        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.foodpic.getContext()).load(uri).into(((MyViewHolder) holder).foodpic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        holder.menuname.setText(menumodelArrayList.get(i).getMenuname());
        holder.menuprice.setText(menumodelArrayList.get(i).getMenuprice());
        holder.storename.setText(menumodelArrayList.get(i).getStorename());
   //     holder.imageUrl.

        //삭제
        holder.ib_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                dlg.setMessage("'" + holder.menuname.getText().toString() + "' 을(를) 삭제하시겠습니까?");
                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("images").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot item : dataSnapshot.getChildren()){
                                    String menu = item.child("menuname").getValue().toString();
                                    String name = item.child("storename").getValue().toString();
                                    if(name.equals(holder.storename.getText().toString()) && menu.equals(holder.menuname.getText().toString())){
                                        item.getRef().removeValue();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });

                    }
                });
                dlg.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
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
        return menumodelArrayList.size();
    }
}
