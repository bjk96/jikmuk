package com.example.kangt.jm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class favorite_recycleview_info_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    public static favorite_recycleview_info_adapter mContext;
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

    private ArrayList<member_favorite_info> foodInfoArrayList;
    favorite_recycleview_info_adapter(ArrayList<member_favorite_info> foodInfoArrayList){
        this.foodInfoArrayList = foodInfoArrayList;
    }

   @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_favorite_recycler_view, parent, false);

       mContext = this;

        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.Picture.setImageResource(foodInfoArrayList.get(position).drawableId);
        myViewHolder.txt_Menuname.setText( foodInfoArrayList.get(position).menuname );
        myViewHolder.Picture_1.setImageResource( foodInfoArrayList.get( position ).X );
        myViewHolder.txt_Price.setText(foodInfoArrayList.get(position).price);
        myViewHolder.txt_Info.setText( foodInfoArrayList.get( position ).info );



        myViewHolder.Picture_1.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view){

                foodInfoArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,foodInfoArrayList.size());


        }
        });
    }


    @Override
    public int getItemCount() {
        return foodInfoArrayList.size();
    }

    //삭제부분


}
