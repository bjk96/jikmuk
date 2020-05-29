package com.example.kangt.jm;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.kangt.jm.model.food_menu_adapter_model;
import java.util.ArrayList;

public class food_menu_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<food_menu_adapter_model> storeList = new ArrayList<>();
    public static food_menu_adapter mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_type;
        TextView txt_stadium;
        TextView txt_store;

        String storeuid;

        public final View mView;

        public MyViewHolder(View view) {
            super(view);
            txt_type = view.findViewById(R.id.gametype);
            txt_stadium = view.findViewById(R.id.gameplace);
            txt_store = view.findViewById(R.id.storename);

            mView = view;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_foodstore_recycler_view, viewGroup, false);
        mContext = this;
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder)viewHolder;

        holder.txt_type.setText(storeList.get(i).getType());
        holder.txt_stadium.setText(storeList.get(i).getPlace());
        holder.txt_store.setText(storeList.get(i).getStorename());

        holder.storeuid = storeList.get(i).getStoreuid();

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), food_menu_manage.class);
                intent.putExtra("getstorename", holder.txt_store.getText().toString());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }
}
