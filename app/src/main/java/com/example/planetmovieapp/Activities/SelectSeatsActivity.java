package com.example.planetmovieapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import com.example.planetmovieapp.MainActivity;
import com.example.planetmovieapp.Objects.Hall;
import com.example.planetmovieapp.Objects.Movie;
import com.example.planetmovieapp.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectSeatsActivity extends AppCompatActivity implements SeatsAdapter.ListItemClickListener{
    private String showMovieId, showDateSelected, showHourSelected, showTimeId, selectedHall;
    private DatabaseReference mDatabase;
    private ArrayList<String> currentSeatsHallStatus;
    private TextView selectedTicketTextView, selectHallTextView, MovieTitleTextView , movieDateTextView;
    private ImageView seatHallScreen;
    private Movie selectedMovie;
    private Hall actualHall;
    private Button confirmButton;
    private RecyclerView seatsHallRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SeatsAdapter mAdapter;
    private ProgressBar progressBar;
    private ArrayList<Integer> listAllNewSelectedSeat = new ArrayList<>();
    private ArrayList<String> availableHalls = new ArrayList<>();
    boolean timerStatus = false; /*will hold the current status of the timerTextView */
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftMilliseconds = 200000; //10 mins
    final private String SEAT_EMPTY = "0";
    final private String SEAT_IS_TAKEN = "1";
    final private String SEAT_CANDIDATE_TO_BE_TAKEN = "2";
    private AutoCompleteTextView hallsDropdown;
    private TextInputLayout hallsTextInputDropDown;
    private int getListSelection ;

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
        hallsDropdown = findViewById(R.id.hall_dropdown);
        hallsTextInputDropDown = findViewById(R.id.text_input_layout_hall_dropdown);
        selectHallTextView = findViewById(R.id.text_select_hall);
        MovieTitleTextView = findViewById(R.id.movie_title_text_view);
        movieDateTextView = findViewById(R.id.movie_date_text_view);

        Intent intent = getIntent();
        selectedMovie = (Movie) intent.getSerializableExtra("selectedMovie.to.seats");
        showMovieId = selectedMovie.getMovieId();
        showDateSelected = intent.getStringExtra("selectedDate.to.seats");
        showHourSelected = intent.getStringExtra("selectedHour.to.seats");

        MovieTitleTextView.setText(selectedMovie.getMovieName());
        movieDateTextView.setText(showDateSelected+ ", "+showHourSelected);
        loadAvailableHallsFromDb();
    }

    /*this function loads show data dates for the selected movie and updates the date data dropdown */
    public void loadAvailableHallsFromDb(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        Query query = mDatabase.orderByChild("movieId").equalTo(selectedMovie.getMovieId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String movieId = (String) ds.child("movieId").getValue();
                    String showDate = (String) ds.child("date").getValue();
                    String showHour = (String) ds.child("hour").getValue();
                    if (showMovieId.equals(movieId) && showDateSelected.equals(showDate) && showHourSelected.equals(showHour)) {
                        String filteredHall = (String) ds.child("hallName").getValue();
                        availableHalls.add(filteredHall);
                    }
                }
                if(availableHalls.size()>1)
                    inflateHallsDropdown(); //more then 1 hall for the same movie & date & hour
                else
                    inflateHallDropdownOne(); //handle situation when we have only 1 hall for the showtime
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void inflateHallDropdownOne(){
        selectHallTextView.setVisibility(View.GONE);
        selectedHall = availableHalls.get(0);
        hallsDropdown.setText(selectedHall);
        getSeatsHallData(selectedHall);
    }


    public void inflateHallsDropdown(){
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_menu_popup_item, availableHalls);
        hallsDropdown.setAdapter(adapter);

        hallsDropdown.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(timerStatus) {
                        Toast.makeText(SelectSeatsActivity.this, "Can't switch halls while seats are selected ", Toast.LENGTH_SHORT).show();
                        hallsDropdown.setVisibility(View.GONE);
                    }
                    else
                        hallsDropdown.setVisibility(View.VISIBLE);
                }
            }
        });


        hallsDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectHallTextView.setVisibility(View.GONE);
                if(!timerStatus) { //if timer is not! running
                    selectedHall = (String) parent.getItemAtPosition(position);
                    getSeatsHallData(selectedHall);

                    hallsDropdown.clearFocus();
                    hallsDropdown.setVisibility(View.VISIBLE);
                }
                else {
                    hallsDropdown.setVisibility(View.GONE);
                    hallsDropdown.clearFocus();
                }
            }
        });

    }


    public void getSeatsHallData(String showHallSelected) {
        progressBar.setVisibility(View.VISIBLE);
        seatsHallRecyclerView.setVisibility(View.GONE);

        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String movieId = (String) ds.child("movieId").getValue();
                    String showDate = (String) ds.child("date").getValue();
                    String showHour = (String) ds.child("hour").getValue();
                    String showHall = (String) ds.child("hallName").getValue();
                    if (showMovieId.equals(movieId) && showDateSelected.equals(showDate) && showHourSelected.equals(showHour) && showHallSelected.equals(showHall)) {
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
        seatsHallRecyclerView.setVisibility(View.VISIBLE);
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
        timerTextView.setText("Tickets will be saved for: "+timeLeftText);
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
                selectedTicketTextView.setText("Selected tickets: " + 0);
                mAdapter.updateSeatsUI(currentSeatsHallStatus);  // erase all 'green' selected seats
                hallsDropdown.setVisibility(View.VISIBLE);   //Updates the visibility of the halls dropdown
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
        selectedTicketTextView.setText("Selected tickets: " + numberOfSelectedTickets);

        confirmButton.setOnClickListener(new View.OnClickListener() { //start new Activity
            @Override
            public void onClick(View v) {
                if(numberOfSelectedTickets != 0) {//user has choose a seat, seat is confirmed on database
                    addCandidateSeatToSeatsHall(seatsTakenByMe);
                    countDownTimer.cancel();
                    timerStatus = false;

                    Integer hallColumns = actualHall.getColumn();
                    StringBuilder dialogSummaryTickets = new StringBuilder();
                    dialogSummaryTickets.append("Number of selected tickets:" + numberOfSelectedTickets + "\n\n");
                    for(Integer seat : listAllNewSelectedSeat){
                        dialogSummaryTickets.append("*Seat Row:" + (((seat) / hallColumns) + 1) + " " + "Number:" + (((seat) % hallColumns) + 1) + "\n");
                    }

                    new MaterialAlertDialogBuilder(SelectSeatsActivity.this ,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                            .setTitle("Confirm selected tickets:")
                            .setMessage(dialogSummaryTickets)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    countDownTimer.start();
                                    timerStatus = true;
                                    listAllNewSelectedSeat.clear();
                                }
                            })
                            .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(SelectSeatsActivity.this,"Order Confirmed! redirecting main page... ",Toast.LENGTH_LONG).show();
                                    addCandidateSeatToSeatsHall(seatsTakenByMe);
                                    updateDatabase();
                                    timerStatus = false;
                                    startActivity(new Intent(SelectSeatsActivity.this, MainActivity.class));
                                }
                            })
                            .show();
                }
                    else {
                    Toast.makeText(SelectSeatsActivity.this, "Please select seat first", Toast.LENGTH_SHORT).show();
                    Log.d("kok","here"+numberOfSelectedTickets);
                }
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
    }

    /*Updates DB with the new currentSeatsHallStatus*/
    public void updateDatabase(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes/"+showTimeId);
        mDatabase.child("seatsHall").setValue(currentSeatsHallStatus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(timerStatus)
            countDownTimer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timerStatus)
            countDownTimer.cancel();
    }

    /*Triggered when user decides to go back and not purchase ticket*/
    @Override
    protected void onStop() {
        super.onStop();
        if(timerStatus)
            countDownTimer.cancel();
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
