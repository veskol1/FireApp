package com.example.planetmovieapp.AdministrationActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planetmovieapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddShowTime extends AppCompatActivity {
    private AutoCompleteTextView movieDropdown;
    private AutoCompleteTextView hallDropdown;
    private AutoCompleteTextView dateDropdown;
    private AutoCompleteTextView hourDropdown;
    private DatabaseReference mDatabase;
    private ArrayList<String> movieNamesList = new ArrayList<>();
    private ArrayList<String> hallNamesList = new ArrayList<>();
    private String selectedMovie;
    private String selectedHall;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_show_time);
        movieDropdown = findViewById(R.id.movie_list_dropdown);
        hallDropdown = findViewById(R.id.hall_list_dropdown);
        dateDropdown = findViewById(R.id.date_list_dropdown);
        hourDropdown = findViewById(R.id.hour_list_dropdown);

        mDatabase = FirebaseDatabase.getInstance().getReference("Movies");

        getMovieListFromDb();
        getHallsListFromDb();
    }

    /*get movies list from Db*/
    public void getMovieListFromDb(){
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

    /*update dropdown with the movies list*/
    public void updateMoviesDropdown(){
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_menu_popup_item, movieNamesList);
        movieDropdown.setAdapter(adapter);

        dateDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMovie = (String)parent.getItemAtPosition(position);
            }
        });
    }


    /*get halls list from Db*/
    public void getHallsListFromDb(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Halls");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    hallNamesList.add(ds.child("hallName").getValue(String.class));
                }
                updateHallsDropdown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void updateHallsDropdown(){
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.dropdown_menu_popup_item, hallNamesList);
        hallDropdown.setAdapter(adapter);

        hallDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedMovie==null){

                }
                else {
                    selectedHall = (String) parent.getItemAtPosition(position);
                }
            }


        });


    }

}
