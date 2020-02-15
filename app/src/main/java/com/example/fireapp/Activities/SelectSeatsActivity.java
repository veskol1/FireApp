package com.example.fireapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fireapp.Objects.Hall;
import com.example.fireapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectSeatsActivity extends AppCompatActivity {
    private String showMovieId;
    private String showDateSelected;
    private String showHourSelected;
    private DatabaseReference mDatabase;
    private ArrayList<Integer> actualSeatsHall;
    private Hall actualHall;
    private RecyclerView seatsHallRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    //private RecyclerView.Adapter mAdapter;
    private SeatsAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seats);
        seatsHallRecyclerView = findViewById(R.id.seats_recycler_view);

        Intent intent = getIntent();
        showMovieId = intent.getStringExtra("movieId.to.seats");
        showDateSelected = intent.getStringExtra("selectedDate.to.seats");
        showHourSelected = intent.getStringExtra("selectedHour.to.seats");

        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String movieId = (String) ds.child("movieId").getValue();
                    String showDate = (String) ds.child("date").getValue();
                    String showHour = (String) ds.child("hour").getValue();
                    if (showMovieId.equals(movieId) && showDateSelected.equals(showDate) && showHourSelected.equals(showHour)) {
                        actualSeatsHall = (ArrayList<Integer>) ds.child("seatsHall").getValue();
                        actualHall = (Hall) ds.child("hall").getValue(Hall.class);
                    }
                }

                layoutManager = new GridLayoutManager(SelectSeatsActivity.this,actualHall.getColumn());
                mAdapter = new SeatsAdapter(actualSeatsHall,actualHall);
                seatsHallRecyclerView.setLayoutManager(layoutManager);
                seatsHallRecyclerView.setHasFixedSize(true);
                seatsHallRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });







    }
}
