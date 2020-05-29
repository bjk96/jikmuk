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
import com.example.kangt.jm.model.favoriteList_model;
import com.example.kangt.jm.model.favorite_model;
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

public class member_favorite_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<favoriteList_model> favoriteList = new ArrayList<>();

    public member_favorite_adapter mContext;
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

        holder.txt_menu.setText(favoriteList.get(i).menu_name);
        holder.txt_price.setText(favoriteList.get(i).menu_price.toString());
//        holder.txt_imgUrl.setImageURI(Uri.parse(favoriteList.get(i).imgUrl));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(favoriteList.get(i).imgUrl);
        //StorageReference pathReference =storageReference.child("picture/1483868222492.JPEG");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.img_imgUrl.getContext()).load(uri).into(((MyViewHolder) viewHolder).img_imgUrl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //   Toast.makeText(context, "다운로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
//        Glide.with(holder.txt_imgUrl.getContext()).load(Uri.parse(favoriteList.get(i).imgUrl)).into(((MyViewHolder) viewHolder).txt_imgUrl);
       //Toast.makeText(c,storageReference+"",Toast.LENGTH_SHORT).show();

        holder.txt_store.setText(favoriteList.get(i).store);
        holder.txt_uid.setText(favoriteList.get(i).uid);
        holder.cb_star.setChecked(true);

        holder.url = favoriteList.get(i).imgUrl;
        holder.num = Integer.parseInt(holder.txt_current.getText().toString());

        String loginedUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String uid = holder.txt_uid.getText().toString();
        String store = holder.txt_store.getText().toString();
        String menu = holder.txt_menu.getText().toString();
        //String price = holder.txt_price.getText().toString();

        holder.cb_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //즐겨찾기에서 삭제
                if(!holder.cb_star.isChecked()){
                    FirebaseDatabase.getInstance().getReference().child("favorite").orderByChild("users/user/"+loginedUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot item : dataSnapshot.getChildren()){
                                if(uid.equals(loginedUid) && store.equals(item.child("store").getValue().toString()) && menu.equals(item.child("menu_name").getValue().toString())){
                                    item.getRef().removeValue();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {          }
                    });
                    Toast.makeText(v.getContext(), "즐겨찾기에서 '" + menu + "'을(를) 삭제합니다.", Toast.LENGTH_SHORT).show();
                }
                //즐겨찾기에서 추가
                else {
                    FirebaseDatabase.getInstance().getReference().child("favorite").orderByChild("users/user/"+loginedUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            favorite_model model = new favorite_model(holder.txt_uid.getText().toString(), holder.txt_store.getText().toString(), holder.txt_menu.getText().toString(), Integer.parseInt(holder.txt_price.getText().toString()), holder.url);
                            FirebaseDatabase.getInstance().getReference().child("favorite").push().setValue(model);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {            }
                    });
                    Toast.makeText(v.getContext(), "즐겨찾기에 '" + menu + "'을(를) 추가합니다.", Toast.LENGTH_SHORT).show();
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

                member_favorite a = new member_favorite();
                ((TextView)a.tv_total.findViewById(R.id.txt_totalprice)).setText(total.toString());
                member_favorite.order = "";
                for(int cnt = 0; cnt < orderedmenu.length; cnt++){
                    if(orderedmenu[cnt] != null)
                        member_favorite.order += orderedmenu[cnt] + " ";
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
                    member_favorite a = new member_favorite();
                    ((TextView)a.tv_total.findViewById(R.id.txt_totalprice)).setText(total.toString());
                    member_favorite.order = "";
                    for(int cnt = 0; cnt < orderedmenu.length; cnt++){
                        if(orderedmenu[cnt] != null)
                            member_favorite.order += orderedmenu[cnt] + " ";
                    }
                }
                else
                    holder.txt_current.setText(String.valueOf(0));
            }
        });


    }


    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public String[] getOrderedmenu() {
        return orderedmenu;
    }
}
