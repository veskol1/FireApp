package com.example.planetmovieapp.AdministrationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planetmovieapp.Objects.Hall;
import com.example.planetmovieapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdministrationActivity extends AppCompatActivity implements View.OnClickListener {
    public final String HALL_ARRAY = "com.project.arraylist.halls";
    private ArrayList<Hall> hallsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration);
        MaterialButton addMovieButton = findViewById(R.id.add_movie_btn);
        MaterialButton addHallButton = findViewById(R.id.add_hall_btn);
        MaterialButton addShowTimeButton = findViewById(R.id.add_showtime_btn);
        MaterialButton statisticButton = findViewById(R.id.get_statistics_btn);

        retrieveHallsFromDbToArray();

        addMovieButton.setOnClickListener(this);
        addHallButton.setOnClickListener(this);
        addShowTimeButton.setOnClickListener(this);
        statisticButton.setOnClickListener(this);
    }

    public void retrieveHallsFromDbToArray(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("Halls");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot db : dataSnapshot.getChildren()){
                    Hall hall = db.getValue(Hall.class);
                    hallsArrayList.add(hall);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_movie_btn : {
                Intent intent = new Intent(AdministrationActivity.this, AddMovieActivity.class);
                intent.putExtra(HALL_ARRAY,hallsArrayList);
                startActivity(intent);
                break;
            }
            case R.id.add_hall_btn : {
                Intent intent = new Intent(AdministrationActivity.this, AddHallActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.add_showtime_btn : {
                Intent intent = new Intent(AdministrationActivity.this, AddShowTime.class);
                startActivity(intent);
                break;
            }
            case R.id.get_statistics_btn : {
                Intent intent = new Intent(AdministrationActivity.this, GetStatisticsActivity.class);
                startActivity(intent);
                break;
            }

        }
    }
}
