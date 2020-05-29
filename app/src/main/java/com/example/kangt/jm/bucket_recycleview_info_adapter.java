package com.example.kangt.jm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class bucket_recycleview_info_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{


    public static bucket_recycleview_info_adapter mContext;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView Picture;
        TextView txt_Price;
        TextView txt_Menuname;
        TextView txt_Info;
        ImageView Picture_1;


        MyViewHolder(View view){
            super(view);
            Picture = view.findViewById(R.id.iv_picture);
            txt_Menuname = view.findViewById(R.id.txt_Menuname);
            Picture_1 = view.findViewById( R.id.iv_ikon );
            txt_Price = view.findViewById( R.id.txt_Price );
            txt_Info = view.findViewById( R.id.txt_Info );

        }
    }

    private ArrayList<member_bucket_info> member_bucket_infoArrayList;
    bucket_recycleview_info_adapter(ArrayList<member_bucket_info> bucket_infoArrayList){
        this.member_bucket_infoArrayList = bucket_infoArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_bucket_recycler_view, parent, false);

        mContext = this;

        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.Picture.setImageResource(member_bucket_infoArrayList.get(position).drawableId);
        myViewHolder.txt_Menuname.setText( member_bucket_infoArrayList.get(position).menuname );
        myViewHolder.Picture_1.setImageResource( member_bucket_infoArrayList.get( position ).X );
        myViewHolder.txt_Price.setText(member_bucket_infoArrayList.get(position).price_set);
        myViewHolder.txt_Info.setText( member_bucket_infoArrayList.get( position ).info );

        myViewHolder.Picture_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member_bucket_infoArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,member_bucket_infoArrayList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return member_bucket_infoArrayList.size();
    }

    //삭제부분







}
