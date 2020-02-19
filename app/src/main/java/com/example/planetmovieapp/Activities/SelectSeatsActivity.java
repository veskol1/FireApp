package com.example.planetmovieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planetmovieapp.Objects.Hall;
import com.example.planetmovieapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectSeatsActivity extends AppCompatActivity implements SeatsAdapter.ListItemClickListener{
    private String showMovieId;
    private String showDateSelected;
    private String showHourSelected;
    private String showTimeId;
    private DatabaseReference mDatabase;
    private ArrayList<String> actualSeatsHall;
    private TextView selectedTicketTextView;
    private ImageView seatHallScreen;
    private Hall actualHall;
    private Button confirmButton;
    private RecyclerView seatsHallRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SeatsAdapter mAdapter;
    private ProgressBar progressBar;
    final private String SEAT_EMPTY = "0";
    final private String SEAT_IS_TAKEN = "1";
    final private String SEAT_CANDIDATE_TO_BE_TAKEN = "2";
    private ArrayList<Integer> listAllNewSelectedSeat = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seats);
        seatsHallRecyclerView = findViewById(R.id.seats_recycler_view);
        confirmButton = findViewById(R.id.btn_confirm);
        selectedTicketTextView = findViewById(R.id.tv_selected_tickets);
        progressBar = findViewById(R.id.seat_hall_progress_bar);
        seatHallScreen = findViewById(R.id.image_view_screen_hall);

        Intent intent = getIntent();
        showMovieId = intent.getStringExtra("movieId.to.seats");
        showDateSelected = intent.getStringExtra("selectedDate.to.seats");
        showHourSelected = intent.getStringExtra("selectedHour.to.seats");

        getSeatsHallData();
    }

    public void getSeatsHallData(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String movieId = (String) ds.child("movieId").getValue();
                    String showDate = (String) ds.child("date").getValue();
                    String showHour = (String) ds.child("hour").getValue();
                    if (showMovieId.equals(movieId) && showDateSelected.equals(showDate) && showHourSelected.equals(showHour)) {
                        actualSeatsHall = (ArrayList<String>) ds.child("seatsHall").getValue();
                        showTimeId = (String) ds.child("statusId").getValue(String.class);
                        actualHall = (Hall) ds.child("hall").getValue(Hall.class);
                    }
                }
                UpdateSeatHallUI();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void UpdateSeatHallUI(){
        layoutManager = new GridLayoutManager(SelectSeatsActivity.this,actualHall.getColumn());
        seatsHallRecyclerView.setHasFixedSize(true);
        mAdapter = new SeatsAdapter(SelectSeatsActivity.this, actualSeatsHall, actualHall,showTimeId);
        seatsHallRecyclerView.setLayoutManager(layoutManager);
        seatsHallRecyclerView.setAdapter(mAdapter);
        progressBar.setVisibility(View.GONE);
        seatHallScreen.setVisibility(View.VISIBLE);
    }


    @Override
    public void onListItemClick(int seatNumber, int numberOfTickets) {
        selectedTicketTextView.setText("Number of selected tickets:"+numberOfTickets);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOfTickets != 0) {//user is choose seat, seat is confirmed on database
                    Toast.makeText(SelectSeatsActivity.this, "You have been purchased " + numberOfTickets + " tickets", Toast.LENGTH_SHORT).show();
                    addCandidateSeat();
                    updateDatabase();
                    startNewActivity();
                }
                    else
                    Toast.makeText(SelectSeatsActivity.this,"Please select seat first",Toast.LENGTH_SHORT).show();
            }
        });
    }



    /*When user decides to go back and not purchase ticket*/
    @Override
    protected void onStop() {
        super.onStop();
        removeCandidateSeat();
        updateDatabase();
    }


    public void removeCandidateSeat(){  /*switches from '2' to '0'*/
        for(int i=0; i < actualSeatsHall.size(); i++)
            if(actualSeatsHall.get(i).equals(SEAT_CANDIDATE_TO_BE_TAKEN))
                actualSeatsHall.set(i,SEAT_EMPTY);
    }

    public void addCandidateSeat(){  /*switches from '2' to '1' */
        for(int i=0; i < actualSeatsHall.size(); i++)
            if(actualSeatsHall.get(i).equals(SEAT_CANDIDATE_TO_BE_TAKEN)) {
                actualSeatsHall.set(i, SEAT_IS_TAKEN);
                listAllNewSelectedSeat.add(i);
            }
    }

    public void updateDatabase(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes/"+showTimeId);
        mDatabase.child("seatsHall").setValue(actualSeatsHall);
        mAdapter.resetNumberOfSelectedTickets();  //updateS UI Text View-> "number of selected tickets :0"
    }


    public void startNewActivity(){
        Intent intent = new Intent(SelectSeatsActivity.this, OrderDetailsActivity.class);
        intent.putExtra("ListAllNewSelectedSeat",listAllNewSelectedSeat);
        intent.putExtra("actualHall",actualHall);
        startActivity(intent);


    }
}
