package com.example.fireapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseActivity extends AppCompatActivity {
    private TextView editText1;
    private MaterialButton buttonRef;
    private DatabaseReference mDatabase;
    private Spinner spin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        editText1 = findViewById(R.id.editTextMovieName);
        buttonRef=findViewById(R.id.materialButton);

        mDatabase = FirebaseDatabase.getInstance().getReference("Movies");

        // Typecasting the variable here
        spin = (Spinner) findViewById(R.id.spn1);

// Array of Months acting as a data pump
        String[] objects = { "January", "Feburary", "March", "April", "May",
                "June", "July", "August", "September", "October", "November","December" };

// Declaring an Adapter and initializing it to the data pump
        ArrayAdapter adapter = new ArrayAdapter(
                getApplicationContext(),android.R.layout.simple_list_item_1 ,objects);

// Setting Adapter to the Spinner
        spin.setAdapter(adapter);

// Setting OnItemClickListener to the Spinner
        //spin.setOnItemSelectedListener(this);




        buttonRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMovie();
            }
        });
    }
//
//    // Defining the Callback methods here
//    public void onItemSelected(AdapterView parent, View view, int pos,
//                               long id) {
//        Toast.makeText(getApplicationContext(),
//                spin.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG)
//                .show();
//    }
//
//    // Defining the Callback methods here
//    @Override
//    public void onNothingSelected(AdapterView arg0) {
//// TODO Auto-generated method stub
//
//    }


    public void addMovie(){
        Hall hall1 = new Hall("hall1",6,7);

        String movieName = editText1.getText().toString();
        String movieId = mDatabase.push().getKey();
        Movie movie = new Movie(movieId,movieName,"bla",hall1,"day4","20:00");
       // mDatabase.child(movieId).setValue(movie);

        //SeatStatus seatStatus = new SeatStatus(movieId,"day4","20:00",hall1);
       // mDatabase.child("Seating").setValue(seatStatus);

//        ArrayList<List<Integer>> group = new ArrayList<List<Integer>>();
//        for(int i = 0; i < 7; i++)
//        {
//            group.add(new ArrayList<Integer>());
//        }

    }



}
