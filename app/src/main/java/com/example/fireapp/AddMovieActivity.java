package com.example.fireapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddMovieActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public final String HALL_ARRAY ="com.project.arraylist.halls";
    private EditText movieNameEditText,movieGenreEditText,movieTrailerEditText,moviePosterEditText,movieSummaryEditText,movieRatingEditText;
    private MaterialButton btnAddMovie;
    private DatabaseReference mDatabase;
    private Spinner spinHalls, spinDays, spinHours;
    private String hallSelected, daySelected, hourSelected;
    private Hall selectedHallObj; //get the selected spinner Object of Hall
    private boolean updateLegal = true;
    private ArrayList<Hall> hallsArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        movieNameEditText = findViewById(R.id.et_movie_name);
        movieGenreEditText = findViewById(R.id.et_movie_genre);
        movieSummaryEditText = findViewById(R.id.editTextMovieSummary);
        moviePosterEditText = findViewById(R.id.et_movie_poster_link);
        movieTrailerEditText = findViewById(R.id.et_movie_trailer_link);
        movieRatingEditText = findViewById(R.id.et_movie_rating);
        spinHalls = findViewById(R.id.spn_halls);
        spinDays = findViewById(R.id.spn_days);
        spinHours = findViewById(R.id.spn_hours);

        btnAddMovie = findViewById(R.id.add_movie_button);
        updateSpinners();

        btnAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAllShowsWithSameHall(hallSelected);
            }
        });
    }

    public void findAllShowsWithSameHall(String hallSelectedName){
        String movieName = movieNameEditText.getText().toString();
        String movieGenre = movieGenreEditText.getText().toString();
        String movieRating = movieRatingEditText.getText().toString();
        String movieSummary = movieSummaryEditText.getText().toString();
        String movieTrailer = movieTrailerEditText.getText().toString();
        String moviePoster = moviePosterEditText.getText().toString();

        mDatabase= FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String hall = ds.child("hallName").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String hour = ds.child("hour").getValue(String.class);
                    if(date.equals(daySelected) && hour.equals(hourSelected) && hall.equals(hallSelectedName)) {
                        updateLegal = false;
                        Toast.makeText(AddMovieActivity.this, "Please choose another date/hour/hall", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else {
                        Toast.makeText(AddMovieActivity.this, "update change to legal", Toast.LENGTH_SHORT).show();
                        updateLegal = true;
                    }
                }

                if (updateLegal) {
                    mDatabase = FirebaseDatabase.getInstance().getReference("Movies");
                    String generatedMovieId = mDatabase.push().getKey();
                    Movie movie = new Movie(generatedMovieId, movieName, movieGenre, movieRating, movieSummary, movieTrailer, moviePoster);
                    mDatabase.child(generatedMovieId).setValue(movie);

                    mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
                    String generatedShowId = mDatabase.push().getKey();
                    SeatStatus seatStatus = new SeatStatus(generatedShowId, generatedMovieId, daySelected, hourSelected, selectedHallObj);
                    mDatabase.child(generatedShowId).setValue(seatStatus);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void updateSpinners(){
        Intent intent = getIntent(); //load the halls from database on the parent Activity so it can displayed on the spinner
        hallsArrayList = (ArrayList<Hall>)intent.getSerializableExtra(HALL_ARRAY);
        ArrayList<String> hallNameArrayList = new ArrayList<>();
        for(Hall hall : hallsArrayList){
            hallNameArrayList.add(hall.getHallName());
        }

        ArrayAdapter hallsAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,hallNameArrayList);
        hallsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHalls.setAdapter(hallsAdapter);
        spinHalls.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter.createFromResource(this,R.array.days_array,android.R.layout.simple_spinner_item);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDays.setAdapter(daysAdapter);
        spinDays.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(this,R.array.hours_array,android.R.layout.simple_spinner_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHours.setAdapter(hourAdapter);
        spinHours.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spn_halls:
                hallSelected = (String) parent.getItemAtPosition(position); //get selected hall string name from spinner
                for(Hall findHall : hallsArrayList)
                    if(findHall.getHallName().equals(hallSelected)) {
                        selectedHallObj = findHall;     //find the hall object from the arraylist
                        break;
                    }
                break;
            case R.id.spn_days:
                daySelected = (String) parent.getItemAtPosition(position);
                break;
            case R.id.spn_hours:
                hourSelected = (String) parent.getItemAtPosition(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
