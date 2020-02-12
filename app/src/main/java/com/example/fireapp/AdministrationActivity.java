package com.example.fireapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.HashSet;

public class AdministrationActivity extends AppCompatActivity{
    public final String HALL_ARRAY = "com.project.arraylist.halls";
    private MaterialButton addMovieButton, addHallButton;
    private DatabaseReference mDatabase;
    private ArrayList<Hall> hallsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration);
        addMovieButton = findViewById(R.id.add_movie_btn);
        addHallButton = findViewById(R.id.add_hall_btn);

        retrieveHallsFromDbToArray();


        addMovieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(AdministrationActivity.this,AddMovieActivity.class);
                intent.putExtra(HALL_ARRAY,hallsArrayList);
                startActivity(intent);
            }
        });
        addHallButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }

    public void retrieveHallsFromDbToArray(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
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











}
