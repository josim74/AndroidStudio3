package com.example.josimuddin.recyclerviewcheckboxapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by JosimUddin on 19/11/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    Context c;
    ArrayList<Recycles> recycles;
    ArrayList<Recycles> checkedPlayers=new ArrayList<>();

    public MyAdapter(Context c, ArrayList<Recycles> recycles) {
        this.c = c;
        this.recycles = recycles;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model, null);
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.nameTxt.setText(recycles.get(position).getName());
        holder.posTxt.setText(recycles.get(position).getPosition());
        holder.img.setImageResource(recycles.get(position).getImages());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                CheckBox checkBox = (CheckBox) v;
                if (checkBox.isChecked()) {
                    checkedPlayers.add(recycles.get(position));
                }else if(!checkBox.isChecked()){
                    checkedPlayers.remove(recycles.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return recycles.size();
    }
}
