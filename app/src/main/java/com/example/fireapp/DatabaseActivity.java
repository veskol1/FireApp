package com.example.fireapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class DatabaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public final String PUT_EXTRA_SET="SENT_HALL_ID_FROM_LOGIN";
    private TextView editText1,hallNameTv,hallRowTv,hallColumnTv;
    private MaterialButton buttonAddMovie, buttonAddHall;
    private DatabaseReference mDatabase;
    private Spinner spin;
    private HashSet<String> hallIdsSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);



        Intent intent = getIntent();
        hallIdsSet = (HashSet<String>)intent.getSerializableExtra(PUT_EXTRA_SET);
        List<String> hallIdsList = new ArrayList<>(hallIdsSet);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,hallIdsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);



        mDatabase = FirebaseDatabase.getInstance().getReference();


        buttonAddMovie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addMovie();
            }
        });

        buttonAddHall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addHall();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object obj = parent.getItemAtPosition(position);
        Log.d("print",""+obj.toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void addMovie(){

        Query query = mDatabase.child("Halls").orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Hall hall = issue.getValue(Hall.class);
                        Log.d("veskok",""+hall.getHallName());
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
//        mDatabase = FirebaseDatabase.getInstance().getReference("Movies");
//
//        String movieName = editText1.getText().toString();
//        String movieId = mDatabase.push().getKey();
//        Movie movie = new Movie(movieName,"5/10","hall1","day4","20:00");
//        mDatabase.child(movieId).setValue(movie);
//
//        mDatabase=mDatabase.child(movieId+"/showTimes");
//        String seatId = mDatabase.push().getKey();
//        SeatStatus seatStatus = new SeatStatus(movieId,"day4","20:00",new Hall("hall3",4,5));
//        mDatabase.child(seatId).setValue(seatStatus);


    }

    public void addHall(){
        String hallName = hallNameTv.getText().toString();
        int row = Integer.parseInt(hallRowTv.getText().toString());
        int column = Integer.parseInt(hallColumnTv.getText().toString());

        //Hall hall = new Hall(hallName,row,column);
        mDatabase = FirebaseDatabase.getInstance().getReference("Halls");
        String hallId = mDatabase.push().getKey();
        //mDatabase.child(hallId).setValue(hall);


    }



}
