package com.example.planetmovieapp.Activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planetmovieapp.Objects.Hall;
import com.example.planetmovieapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeatsAdapter extends RecyclerView.Adapter<SeatsAdapter.StatusHallHolder> {
    private ArrayList<String> currentSeatsHallStatus;  // will hold the seats hall status
    private ArrayList<Integer> selectedSeats = new ArrayList<Integer>();  //will hold all selected seats numbers by the user
    private Context context;    // used for communication with the activity
    private String showTimeId;  // showtime unique id
    private DatabaseReference mDatabase; // reference to DB
    final private ListItemClickListener mOnClickListener;
    private Integer numberOfSelectedTickets = 0;
    final private String SEAT_CANDIDATE_TO_BE_TAKEN = "2";
    final private String SEAT_WAS_ALREADY_TAKEN = "1";
    final private String SEAT_EMPTY = "0";


    public SeatsAdapter(Context context, ListItemClickListener mOnClickListener, ArrayList<String> currentSeatsHallStatus, String showTimeId){
        this.context = context;
        this.mOnClickListener = mOnClickListener;
        this.currentSeatsHallStatus = currentSeatsHallStatus;
        this.showTimeId = showTimeId;
    }

    /*interface used to communicate with the SelectSeatsActivity*/
    public interface ListItemClickListener {
        void onListItemClick(ArrayList<Integer> seatsTakenByMe, ArrayList<String> currentSeatsHallStatus);
    }


    /*Al those 3 functions are part of implementation of Recyclerview : onCreateViewHolder, onBindViewHolder, getItemCount */
    @NonNull
    @Override
    public StatusHallHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seat_item,parent,false);
        StatusHallHolder statusHallHolder = new StatusHallHolder(view);
        return statusHallHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatusHallHolder holder, int position) {
        initializeUI(holder,position);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                retrieveSeatHallStatusFromDB(holder,position);
           }
       });
    }

    @Override
    public int getItemCount() {
        return currentSeatsHallStatus.size();
    }


    /*This is the first UI initialization*/
    private void initializeUI (@NonNull StatusHallHolder holder, int position){
        if (currentSeatsHallStatus.get(position).equals(SEAT_EMPTY)) // if seat is empty
            holder.imageView.setImageResource(R.drawable.seat_empty);
        else if (currentSeatsHallStatus.get(position).equals(SEAT_WAS_ALREADY_TAKEN)) // if seat is taken
            holder.imageView.setImageResource(R.drawable.seat_taken);
        else if (selectedSeats.contains(position))  // if seat was taken by me but still timer is running
            holder.imageView.setImageResource(R.drawable.seat_candidate);
        else    // if seat was caught by other user but still not was purchased , so we will se empty seat but cannot be taken by me
            holder.imageView.setImageResource(R.drawable.seat_empty);
    }

    /*When clicking on seat, first we want to retrieve the seats current status from DB*/
    private void retrieveSeatHallStatusFromDB(@NonNull StatusHallHolder holder, int position){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child(showTimeId);
                currentSeatsHallStatus = (ArrayList<String>) ds.child("seatsHall").getValue();
                updateSeatsUIOnclick(holder,position);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /*Update UI when clicking on seat*/
    public void updateSeatsUIOnclick (@NonNull StatusHallHolder holder, int position){
        if(currentSeatsHallStatus.get(position).equals(SEAT_EMPTY)) { // seat is empty and not taken by any user "now"
            selectedSeats.add(position);    // add seat number to array of selected seats by this user
            holder.imageView.setImageResource(R.drawable.seat_candidate);  //update seat to 'green'
            currentSeatsHallStatus.set(position,SEAT_CANDIDATE_TO_BE_TAKEN); // update current seats hall status
            updateDb(); // update DB with the current seats status
            mOnClickListener.onListItemClick(selectedSeats , currentSeatsHallStatus);  // update the activity ui -> start/stop timer
        }
        else if(currentSeatsHallStatus.get(position).equals(SEAT_CANDIDATE_TO_BE_TAKEN) && selectedSeats.contains(position)){  // seat was already selected by me
            selectedSeats.remove(Integer.valueOf(position));   // remove seat number from array of selected seats by this user
            holder.imageView.setImageResource(R.drawable.seat_empty);  // update seat to 'white'
            currentSeatsHallStatus.set(position,SEAT_EMPTY);  // update current seats hall status
            updateDb(); // update DB with the current seats status
            mOnClickListener.onListItemClick(selectedSeats, currentSeatsHallStatus);  // update the activity ui -> start/stop timer
        }
        else{ // seat was already selected by other user!
            Toast.makeText(context,"Seat is not available now !",Toast.LENGTH_LONG).show();
        }

    }

    /*Update DB */
    private void updateDb(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes/"+showTimeId);
        mDatabase.child("seatsHall").setValue(currentSeatsHallStatus);
        notifyDataSetChanged();
    }


    /*This function is triggered when something on the SelectedSeatsActivity is change as timer changes, exit from the activity...
      first we retrieve the seats status from DB, than we remove all (for loop) seats that has been selected (green) but has not purchased.
      and finally we update the DB again with the new currentSeatsHallStatus.
    */
    public void updateSeatsUI(ArrayList<String> actualSeatsHall){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child(showTimeId);
                currentSeatsHallStatus = (ArrayList<String>) ds.child("seatsHall").getValue();
                for(int i = 0; i <currentSeatsHallStatus.size(); i++) {
                    if (currentSeatsHallStatus.get(i).equals(SEAT_CANDIDATE_TO_BE_TAKEN) && selectedSeats.contains(i))
                        currentSeatsHallStatus.set(i, SEAT_EMPTY);
                }
                selectedSeats.clear();
                updateDb();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public class StatusHallHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public StatusHallHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.seat_item_image_view);
        }
    }

}
