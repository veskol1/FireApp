package com.example.planetmovieapp.AdministrationActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.planetmovieapp.R;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GetStatisticsActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mDatabase;
    private ArrayList<String> movieNamesList = new ArrayList<>();
    private ArrayList<String> currentSeatsHallStatus = new ArrayList<>();
    private AutoCompleteTextView movieDropdown;
    StringBuilder selectedMovieStat = new StringBuilder();
    final private String SEAT_IS_TAKEN = "1";
    private String selectedMovie, movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_statistics);
        MaterialButton confirmButton = findViewById(R.id.statistics_confirm_button);
        movieDropdown = findViewById(R.id.statistics_movie_list_dropdown);
        confirmButton.setOnClickListener(this);

        getMovieListFromDb();
    }


    /*get movie names from Db to arrayList*/
    public void getMovieListFromDb(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Movies");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    movieNamesList.add(ds.child("movieName").getValue(String.class));
                }
                updateMoviesDropdown();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /*update movie dropdown with the movie names */
    public void updateMoviesDropdown(){
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_menu_popup_item, movieNamesList);
        movieDropdown.setAdapter(adapter);

        movieDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMovie = (String)parent.getItemAtPosition(position);
            }
        });
    }


    /*retrieves from database the movieId matching the selected movie name*/
    public void updateMovieId(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Movies");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.child("movieName").getValue(String.class).equals(selectedMovie)) {
                        movieId = ds.child("movieId").getValue(String.class);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void getMovieStat() {
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                selectedMovieStat.append("Movie Name: "+ (selectedMovie) + "\n\n");
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String currMovieId = ds.child("movieId").getValue(String.class);
                    if(currMovieId.equals(movieId)){
                        currentSeatsHallStatus = (ArrayList<String>) ds.child("seatsHall").getValue();
                        selectedMovieStat.append("Date: " + (ds.child("date").getValue(String.class)) + ", Hour: " + (ds.child("hour").getValue(String.class)) + ", Hall name: " + (ds.child("hallName").getValue(String.class)));
                        int counter = 0;
                        for(int i = 0; i < currentSeatsHallStatus.size(); i++){
                            if(currentSeatsHallStatus.get(i).equals(SEAT_IS_TAKEN)){
                                counter++;
                            }
                        }
                        selectedMovieStat.append("\nNumber of tickets sold: " + (counter) + ", Out of: " + (currentSeatsHallStatus.size()) +"\n\n");

                    }
                }
                Intent intent = new Intent(GetStatisticsActivity.this, ShowStatisticsResult.class);
                intent.putExtra("SELLING_STATISTICS",selectedMovieStat.toString());
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onClick(View view) {
        if(selectedMovie != null) {
            updateMovieId();
            getMovieStat();
        }
        else {
            Toast.makeText(GetStatisticsActivity.this,"Please select movie",Toast.LENGTH_SHORT).show();
        }
    }
}
