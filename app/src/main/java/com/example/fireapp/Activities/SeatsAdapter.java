package com.example.fireapp.Activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fireapp.Objects.Hall;
import com.example.fireapp.R;

import java.util.ArrayList;

public class SeatsAdapter extends RecyclerView.Adapter<SeatsAdapter.StatusHallHolder> {
    private ArrayList<String> actualSeatsHall;
    private Hall actualHall;
    private Context context;

    public SeatsAdapter(Context ct, ArrayList<String> actualSeatsHall, Hall actualHall){
        this.context = ct;
        this.actualSeatsHall = actualSeatsHall;
        this.actualHall = actualHall;
    }

    @NonNull
    @Override
    public StatusHallHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.seat_item,parent,false);
        StatusHallHolder statusHallHolder = new StatusHallHolder(view);
        return statusHallHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatusHallHolder holder, int position) {
       if (actualSeatsHall.get(position).equals("0"))
           holder.imageView.setImageResource(R.drawable.seat_empty);
       else
           holder.imageView.setImageResource(R.drawable.seat_taken);

       holder.imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(actualSeatsHall.get(position).equals("0")) {
                   holder.imageView.setImageResource(R.drawable.seat_taken);
                   actualSeatsHall.add(position,"1");
               }
               else {
                   holder.imageView.setImageResource(R.drawable.seat_empty);
                   actualSeatsHall.add(position,"0");
               }
           }
       });
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
