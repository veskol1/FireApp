package com.example.planetmovieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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
    private String showMovieId, showDateSelected, showHourSelected, showTimeId;
    private DatabaseReference mDatabase;
    private ArrayList<String> currentSeatsHallStatus;
    private TextView selectedTicketTextView;
    private ImageView seatHallScreen;
    private Hall actualHall;
    private Button confirmButton;
    private RecyclerView seatsHallRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SeatsAdapter mAdapter;
    private ProgressBar progressBar;
    private ArrayList<Integer> listAllNewSelectedSeat = new ArrayList<>();
    boolean timerStatus = false; /*will hold the current status of the timerTextView */
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftMilliseconds = 200000; //10 mins
    final private String SEAT_EMPTY = "0";
    final private String SEAT_IS_TAKEN = "1";
    final private String SEAT_CANDIDATE_TO_BE_TAKEN = "2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seats);
        seatsHallRecyclerView = findViewById(R.id.seats_recycler_view);
        confirmButton = findViewById(R.id.btn_confirm);
        selectedTicketTextView = findViewById(R.id.tv_selected_tickets);
        progressBar = findViewById(R.id.seat_hall_progress_bar);
        seatHallScreen = findViewById(R.id.image_view_screen_hall);
        timerTextView = findViewById(R.id.timer);

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
                        currentSeatsHallStatus = (ArrayList<String>) ds.child("seatsHall").getValue();
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
        mAdapter = new SeatsAdapter(this,SelectSeatsActivity.this, currentSeatsHallStatus, showTimeId);
        seatsHallRecyclerView.setLayoutManager(layoutManager);
        seatsHallRecyclerView.setAdapter(mAdapter);
        progressBar.setVisibility(View.GONE);
        seatHallScreen.setVisibility(View.VISIBLE);
    }


    public void updateTimer(){
        int minutes = (int) timeLeftMilliseconds / 20000;
        int seconds = (int) timeLeftMilliseconds % 20000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if(seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;
        timerTextView.setText(timeLeftText);
    }

    public void startTimer(){
        timerStatus = true;
        countDownTimer = new CountDownTimer(15000, 1000){
            @Override
            public void onTick(long l) {
                timeLeftMilliseconds = l;
                updateTimer();
            }
            @Override
            public void onFinish() {
                timerStatus = false;
                timerTextView.setText("");
                selectedTicketTextView.setText("Number of selected tickets: " + 0);
                mAdapter.updateSeatsUI(currentSeatsHallStatus);  // erase all 'green' selected seats
            }
        }.start();

    }

    /*Update timer status and UI on clicking while seats hall*/
    public void updateTimerUI(ArrayList<String> currentSeatsHallStatus) {
        this.currentSeatsHallStatus = currentSeatsHallStatus;
        if(!timerStatus){  /*if timer is not running now*/
            if(currentSeatsHallStatus.contains(SEAT_CANDIDATE_TO_BE_TAKEN)) {  // if seat was clicked and changed to green
                timerStatus = true; // update timer status
                startTimer();  // start timer
                }
        }
        else {  /*if timer status is running*/
            if(!currentSeatsHallStatus.contains(SEAT_CANDIDATE_TO_BE_TAKEN)) { // if user decides to remove all his selections (from green to white seats) there is not candidates
                timerStatus = false;
                countDownTimer.cancel();
                timerTextView.setText("");
            }
        }
    }

    /*dsfsdf*/
    @Override
    public void onListItemClick(ArrayList<Integer> seatsTakenByMe, ArrayList<String> currentSeatsHallStatus) {
        updateTimerUI(currentSeatsHallStatus); //update timer
        int numberOfSelectedTickets = seatsTakenByMe.size();
        selectedTicketTextView.setText("Number of selected tickets: " + numberOfSelectedTickets);

        confirmButton.setOnClickListener(new View.OnClickListener() { //start new Activity
            @Override
            public void onClick(View v) {
                if(numberOfSelectedTickets != 0) {//user has choose a seat, seat is confirmed on database
                    addCandidateSeatToSeatsHall(seatsTakenByMe);
                    timerStatus = false;
                    countDownTimer.cancel();
                    startNewActivity();
                }
                    else
                    Toast.makeText(SelectSeatsActivity.this,"Please select seat first",Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*Adds selected seats to the currentSeatsHallStatus and updates the DB*/
    public void addCandidateSeatToSeatsHall(ArrayList<Integer> seatsTakenByMe){  /*switches from '2' to '1' */
        for(int i = 0; i < currentSeatsHallStatus.size(); i++)
            if(currentSeatsHallStatus.get(i).equals(SEAT_CANDIDATE_TO_BE_TAKEN) && seatsTakenByMe.contains(i)) {
                currentSeatsHallStatus.set(i, SEAT_IS_TAKEN);
                listAllNewSelectedSeat.add(i);
            }
        updateDatabase();
    }

    /*Updates DB with the new currentSeatsHallStatus*/
    public void updateDatabase(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes/"+showTimeId);
        mDatabase.child("seatsHall").setValue(currentSeatsHallStatus);
    }


    public void startNewActivity(){
        Intent intent = new Intent(SelectSeatsActivity.this, OrderDetailsActivity.class);
        intent.putExtra("ListAllNewSelectedSeat",listAllNewSelectedSeat);
        intent.putExtra("actualHall",actualHall);
        startActivity(intent);
    }


    /*When user decides to go back and not purchase ticket*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerStatus) {
            countDownTimer.cancel();
            mAdapter.updateSeatsUI(currentSeatsHallStatus);
            timerStatus = false;
        }
    }


}
