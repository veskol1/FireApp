package com.example.fireapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailedMovieActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView movieNameTextView;
    private Movie selectedMovie;
    private DatabaseReference mDatabase;
    private ArrayList<String> filteredDates ;
    private ArrayList<String> filteredHours ;
    private Spinner spinnerDates;
    private Spinner spinnerHours;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);
        movieNameTextView = findViewById (R.id.movie_name_text);
        spinnerDates = findViewById(R.id.spinner_date);
        spinnerHours = findViewById(R.id.spinner_hour);
        filteredDates = new ArrayList<>();
        filteredHours = new ArrayList<>();

        Intent intent = getIntent();
        selectedMovie = (Movie) intent.getSerializableExtra("selected.movie");
        movieNameTextView.setText(selectedMovie.getMovieName());


        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        Query query = mDatabase.orderByChild("movieId").equalTo(selectedMovie.getMovieId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    filteredDates.add(ds.child("date").getValue(String.class));

                updateDateSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateDateSpinner(){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,filteredDates);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDates.setAdapter(arrayAdapter);
        spinnerDates.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_date: {
                String dateSelected = (String)parent.getItemAtPosition(position);
                retrieveShowHour(dateSelected);
                break;
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void retrieveShowHour(String date){
        filteredHours.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        Query query = mDatabase.orderByChild("movieId").equalTo(selectedMovie.getMovieId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String dateFiltered = ds.child("date").getValue(String.class);
                    if(dateFiltered.equals(date)) {
                        filteredHours.add(ds.child("hour").getValue(String.class));
                    }
                }
                updateHourSpinner();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void updateHourSpinner(){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,filteredHours);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHours.setAdapter(arrayAdapter);
        spinnerHours.setOnItemSelectedListener(this);
    }


}
