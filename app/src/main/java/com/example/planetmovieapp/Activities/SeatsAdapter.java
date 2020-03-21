package com.example.planetmovieapp.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planetmovieapp.Objects.Hall;
import com.example.planetmovieapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SeatsAdapter extends RecyclerView.Adapter<SeatsAdapter.StatusHallHolder> {
    private ArrayList<String> actualSeatsHall;
    private Hall actualHall;
    private Context context;
    private String showTimeId;
    private DatabaseReference mDatabase;
    final private ListItemClickListener mOnClickListener;
    private Integer numberOfSelectedTickets = 0;
    final private String SEAT_CANDIDATE_TO_BE_TAKEN = "2";
    final private String SEAT_EMPTY = "0";


    public SeatsAdapter(ListItemClickListener mOnClickListener, ArrayList<String> actualSeatsHall, Hall actualHall, String showTimeId){
        this.mOnClickListener = mOnClickListener;
        this.actualSeatsHall = actualSeatsHall;
        this.actualHall = actualHall;
        this.showTimeId = showTimeId;
    }

    public interface ListItemClickListener {
        void onListItemClick(int seatNumber,int numberOfSelectedTickets);
        void updateTimerUI(ArrayList<String> actualSeatsHall);
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
       if (actualSeatsHall.get(position).equals(SEAT_EMPTY))
           holder.imageView.setImageResource(R.drawable.seat_empty);
       else
           holder.imageView.setImageResource(R.drawable.seat_taken);

       holder.imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(actualSeatsHall.get(position).equals(SEAT_EMPTY)) {
                   holder.imageView.setImageResource(R.drawable.seat_candidate);
                   actualSeatsHall.set(position,SEAT_CANDIDATE_TO_BE_TAKEN);
                   numberOfSelectedTickets++;

                   updateDb();
               }
               else if(actualSeatsHall.get(position).equals(SEAT_CANDIDATE_TO_BE_TAKEN)){
                   holder.imageView.setImageResource(R.drawable.seat_empty);
                   actualSeatsHall.set(position,SEAT_EMPTY);
                   numberOfSelectedTickets--;

                   updateDb();
               }
               mOnClickListener.onListItemClick(position,numberOfSelectedTickets);
               mOnClickListener.updateTimerUI(actualSeatsHall);
           }
       });
    }

    public void updateDb(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes/"+showTimeId);
        mDatabase.child("seatsHall").setValue(actualSeatsHall);
    }


    @Override
    public int getItemCount() {
        return actualSeatsHall.size();
    }

    public class StatusHallHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public StatusHallHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.seat_item_image_view);
        }


    }

    public void resetNumberOfSelectedTickets(){
        numberOfSelectedTickets=0;
        notifyDataSetChanged();
    }

    public void updateSeatsUI(ArrayList<String> actualSeatsHall){
        if (actualSeatsHall.contains(SEAT_CANDIDATE_TO_BE_TAKEN)){
            for(int i = 0; i <actualSeatsHall.size(); i++)
                if(actualSeatsHall.get(i).equals(SEAT_CANDIDATE_TO_BE_TAKEN))
                    actualSeatsHall.set(i,SEAT_EMPTY);
        }
        notifyDataSetChanged();
    }
}
