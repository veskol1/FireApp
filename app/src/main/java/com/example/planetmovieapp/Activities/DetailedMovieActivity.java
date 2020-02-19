package com.example.planetmovieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.planetmovieapp.Objects.Movie;
import com.example.planetmovieapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailedMovieActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView movieNameTextView;
    private Button nextButton;
    private Movie selectedMovie;
    private String selectedShowDate;
    private String selectedShowHour;
    private DatabaseReference mDatabase;
    private ArrayList<String> filteredDatesArray;
    private ArrayList<String> filteredHoursArray;
    private Spinner spinnerDates;
    private Spinner spinnerHours;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);
        movieNameTextView = findViewById (R.id.movie_name_text);
        nextButton = findViewById(R.id.btn_next);
        spinnerDates = findViewById(R.id.spinner_date);
        spinnerHours = findViewById(R.id.spinner_hour);
        filteredDatesArray = new ArrayList<>();
        filteredHoursArray = new ArrayList<>();

        Intent intent = getIntent();
        selectedMovie = (Movie) intent.getSerializableExtra("selected.movie");
        movieNameTextView.setText(selectedMovie.getMovieName());

        retrieveData();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedMovieActivity.this,SelectSeatsActivity.class);
                intent.putExtra("movieId.to.seats",selectedMovie.getMovieId());
                intent.putExtra("selectedDate.to.seats",selectedShowDate);
                intent.putExtra("selectedHour.to.seats",selectedShowHour);
                startActivity(intent);
            }
        });
    }

    public void retrieveData(){

        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        Query query = mDatabase.orderByChild("movieId").equalTo(selectedMovie.getMovieId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    filteredDatesArray.add(ds.child("date").getValue(String.class));

                updateSpinnerDates();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void updateSpinnerDates(){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, filteredDatesArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDates.setAdapter(arrayAdapter);
        spinnerDates.setOnItemSelectedListener(this);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_date: {
                selectedShowDate = (String)parent.getItemAtPosition(position);
                retrieveShowHour(selectedShowDate);
                break;
            }
            case R.id.spinner_hour:{
                selectedShowHour = (String)parent.getItemAtPosition(position);
                break;
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    public void retrieveShowHour(String date){
        filteredHoursArray.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        Query query = mDatabase.orderByChild("movieId").equalTo(selectedMovie.getMovieId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String dateFiltered = ds.child("date").getValue(String.class);
                    if(dateFiltered.equals(date)) {
                        filteredHoursArray.add(ds.child("hour").getValue(String.class));
                    }
                }
                updateSpinnerHours();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void updateSpinnerHours(){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, filteredHoursArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHours.setAdapter(arrayAdapter);
        spinnerHours.setOnItemSelectedListener(this);
    }





}
