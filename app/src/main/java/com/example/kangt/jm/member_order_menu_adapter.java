package com.example.kangt.jm;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kangt.jm.model.favorite_model;
import com.example.kangt.jm.model.member_menu_model;
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

public class member_order_menu_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<member_menu_model> menuList = new ArrayList<>();

    public member_order_menu_adapter mContext;
    public Context c;
    String[] orderedmenu = new String[50];
    Integer total = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_menu;
        TextView txt_price;
        TextView txt_plus;
        TextView txt_minus;
        TextView txt_current;
        ImageView img_imgUrl;
        TextView txt_store;
        TextView txt_uid;
        //ImageView img_menu;
        CheckBox cb_star;

        String url;
        Integer num;
        String store;
        String menu;

        public final View mView;


        public MyViewHolder(@NonNull View v){
            super(v);
            mView = v;
            c = v.getContext();

            txt_menu = v.findViewById(R.id.txt_menu);
            txt_price = v.findViewById(R.id.point_price);
            txt_plus = v.findViewById(R.id.txt_plus);
            txt_minus = v.findViewById(R.id.txt_minus);
            txt_current = v.findViewById(R.id.txt_current);
            img_imgUrl = v.findViewById(R.id.menurowpic);
            txt_store = v.findViewById(R.id.txt_store);
            txt_uid = v.findViewById(R.id.txt_uid);
            //img_menu = v.findViewById(R.id.imageView1);
            cb_star = v.findViewById(R.id.cb_star);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_row, viewGroup, false);
        mContext = this;
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        MyViewHolder holder = (MyViewHolder) viewHolder;

        holder.txt_menu.setText(menuList.get(i).getMenuname());
        holder.txt_price.setText(menuList.get(i).getMenuprice());
//        holder.txt_imgUrl.setImageURI(Uri.parse(favoriteList.get(i).imgUrl));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(menuList.get(i).getImageUrl());
        //StorageReference pathReference =storageReference.child("picture/1483868222492.JPEG");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.img_imgUrl.getContext()).load(uri).into(((MyViewHolder) viewHolder).img_imgUrl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
             }
        });

        holder.txt_store.setText(menuList.get(i).getStorename());

        holder.url = menuList.get(i).getImageUrl();
        holder.num = Integer.parseInt(holder.txt_current.getText().toString());

        String str_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        holder.store = holder.txt_store.getText().toString();
        holder.menu = holder.txt_menu.getText().toString();
        //holder.cb_star.setChecked(false);


        FirebaseDatabase.getInstance().getReference().child("favorite").orderByChild("users/user/"+str_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    String s = item.child("store").getValue().toString();
                    String m = item.child("menu_name").getValue().toString();
                    String u = item.child("uid").getValue().toString();
                    if(u.equals(str_uid) && m.equals(holder.menu) && s.equals(holder.store)) {
                        holder.cb_star.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.cb_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //즐겨찾기에서 삭제
                if(!holder.cb_star.isChecked()){
                    FirebaseDatabase.getInstance().getReference().child("favorite").orderByChild("users/user/"+str_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot item : dataSnapshot.getChildren()){
                                String uid = item.child("uid").getValue().toString();
                                if(str_uid.equals(uid) && holder.store.equals(item.child("store").getValue().toString()) && holder.menu.equals(item.child("menu_name").getValue().toString())){
                                    item.getRef().removeValue();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {          }
                    });
                   Toast.makeText(v.getContext(), "즐겨찾기에서 '" + holder.menu + "'을(를) 삭제합니다.", Toast.LENGTH_SHORT).show();
                }
                //즐겨찾기에서 추가
                else {
                    FirebaseDatabase.getInstance().getReference().child("favorite").orderByChild("users/user/"+str_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            favorite_model model = new favorite_model(str_uid, holder.txt_store.getText().toString(), holder.txt_menu.getText().toString(), Integer.parseInt(holder.txt_price.getText().toString()), holder.url);
                            FirebaseDatabase.getInstance().getReference().child("favorite").push().setValue(model);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {            }
                    });
                    Toast.makeText(v.getContext(), "즐겨찾기에 '" + holder.menu + "'을(를) 추가합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txt_current.setText(String.valueOf(++holder.num));
                String data = holder.txt_menu.getText().toString() + holder.txt_current.getText().toString();
                orderedmenu[i] = data;
                total += Integer.parseInt(holder.txt_price.getText().toString());

                member_order_menu a = new member_order_menu();
                ((TextView)a.tv_total.findViewById(R.id.txt_totalprice)).setText(total.toString());
                member_order_menu.order = "";
                for(int cnt = 0; cnt < orderedmenu.length; cnt++){
                    if(orderedmenu[cnt] != null)
                        member_order_menu.order += orderedmenu[cnt] + " ";
                }
            }
        });

        holder.txt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.num > 0){
                    holder.txt_current.setText(String.valueOf(--holder.num));
                    if(holder.num == 0)
                        orderedmenu[i] = "";
                    else{
                        String data = holder.txt_menu.getText().toString() + holder.txt_current.getText().toString();
                        orderedmenu[i] = data;
                    }
                    total -= Integer.parseInt(holder.txt_price.getText().toString());
                    member_order_menu a = new member_order_menu();
                    ((TextView)a.tv_total.findViewById(R.id.txt_totalprice)).setText(total.toString());
                    member_order_menu.order = "";
                    for(int cnt = 0; cnt < orderedmenu.length; cnt++){
                        if(orderedmenu[cnt] != null)
                            member_order_menu.order += orderedmenu[cnt] + " ";
                    }
                }
                else
                    holder.txt_current.setText(String.valueOf(0));
            }
        });


    }


    @Override
    public int getItemCount() {
        return menuList.size();
    }

}
