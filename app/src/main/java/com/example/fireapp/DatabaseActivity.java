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

        mDatabase = FirebaseDatabase.getInstance().getReference("Movies/vesko");


        buttonRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMovie();
            }
        });
    }


    public void addMovie(){
        ArrayList<String>arr = new ArrayList<>();
        arr.add("a");
        arr.add("b");
        arr.add("c");

        String movieName = editText1.getText().toString();
        String movieId = mDatabase.push().getKey();
        Movie movie = new Movie(movieId,movieName,"bla",arr);
        mDatabase.child(movieId).setValue(movie);

    }



}
