package com.example.kangt.jm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kangt.jm.model.game_info;

import java.util.ArrayList;

public class games_recylcerview_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<game_info> games_infoArrayList = new ArrayList<>();


    public static games_recylcerview_adapter mContext;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_team1;
        TextView txt_team2;
        TextView txt_league;
        TextView txt_time;


        MyViewHolder(View view){
            super(view);
            txt_team1 = view.findViewById(R.id.txt_team1);
            txt_team2 = view.findViewById(R.id.txt_team2);
            txt_league = view.findViewById(R.id.txt_league);
            txt_time = view.findViewById(R.id.txt_time);

        }
    }

    void addItem(game_info data){
        games_infoArrayList.add(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_games, parent, false);

        mContext = this;

        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.txt_team1.setText(games_infoArrayList.get(position).team1);
        myViewHolder.txt_team2.setText(games_infoArrayList.get(position).team2);
        myViewHolder.txt_league.setText(games_infoArrayList.get(position).league);
        myViewHolder.txt_time.setText(games_infoArrayList.get(position).time);

    }

    @Override
    public int getItemCount() {
        return games_infoArrayList.size();
    }

    //삭제부분







}
