package com.example.fireapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.Map;

public class DatabaseActivity extends AppCompatActivity {
    private TextView editText1;
    private MaterialButton buttonRef;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        editText1 = findViewById(R.id.editText1);
        buttonRef=findViewById(R.id.materialButton);

        mDatabase = FirebaseDatabase.getInstance().getReference("Movies");


        buttonRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMovie();
            }
        });
    }


    public void addMovie(){
        Map<String,String> dates = new HashMap<>();
        dates.put("day1","18:00");
        dates.put("day2","20:00");
        Hall hall1 = new Hall("hall1","6","7");

        String movieName = editText1.getText().toString();
        String movieId = mDatabase.push().getKey();
        Movie movie = new Movie(movieId,movieName,"bla",dates,hall1);
        mDatabase.child(movieId).setValue(movie);

    }



}
