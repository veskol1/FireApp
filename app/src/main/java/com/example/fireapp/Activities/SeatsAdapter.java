package com.example.fireapp.Activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fireapp.Objects.Hall;
import com.example.fireapp.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class SeatsAdapter extends RecyclerView.Adapter<SeatsAdapter.StatusHallHolder> {
    private ArrayList<Integer> actualSeatsHall;
    private Hall actualHall;

    public SeatsAdapter(ArrayList<Integer> actualSeatsHall, Hall actualHall){
        this.actualSeatsHall = actualSeatsHall;
        this.actualHall = actualHall;
    }

    @NonNull
    @Override
    public StatusHallHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seat_item,parent,false);
        StatusHallHolder statusHallHolder = new StatusHallHolder(view);
        return statusHallHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatusHallHolder holder, int position) {
        holder.imageView.setImageResource(R.drawable.seat_empty);
    }

    @Override
    public int getItemCount() {
        return actualSeatsHall.size();
    }

    public class StatusHallHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public StatusHallHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.seat_item_image_view);
        }
    }
}
