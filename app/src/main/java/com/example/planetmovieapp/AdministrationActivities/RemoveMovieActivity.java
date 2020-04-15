package com.example.planetmovieapp.AdministrationActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planetmovieapp.Objects.Movie;
import com.example.planetmovieapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RemoveMovieActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ArrayList<Movie> movieObjectsList = new ArrayList<>();
    private ArrayList<String> movieNamesList = new ArrayList<>();
    private AutoCompleteTextView movieDropdown;

    private String selectedMovieName;
    private Movie selectedMovieObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_movie);
        MaterialButton confirmRemoveButton = findViewById(R.id.confirm_remove_button);
        movieDropdown = findViewById(R.id.movie_remove_list_dropdown);

        getMovieListFromDb();

        confirmRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedMovieName != null) {
                    removeMovieFromDB();
                    removeMovieShowTimeFromDB();
                    Toast.makeText(RemoveMovieActivity.this,"Movie and ShowTime were removed ",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(RemoveMovieActivity.this,"Please select movie to remove",Toast.LENGTH_LONG).show();
            }
        });

    }


    /*get movie names from Db to arrayList*/
    public void getMovieListFromDb(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Movies");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    movieObjectsList.add(ds.getValue(Movie.class));
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
                selectedMovieName = (String)parent.getItemAtPosition(position);
                selectedMovieObject = findSelectedObject(selectedMovieName);
            }
        });
    }

    /*find the movie object of the selected movie(String)name*/
    public Movie findSelectedObject(String selectedMovieName){
        for(Movie movie : movieObjectsList){
            if (movie.getMovieName().equals(selectedMovieName)){
                return movie;
            }
        }
        return null; //won't happen ever
    }


    public void removeMovieFromDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Movies");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                      if (ds.getKey().equals(selectedMovieObject.getMovieId()))
                          mDatabase.getDatabase().getReference("Movies/"+ds.getKey()).removeValue(); //path in DB to the movie
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void removeMovieShowTimeFromDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.child("movieId").getValue(String.class).equals(selectedMovieObject.getMovieId()))
                        mDatabase.getDatabase().getReference("ShowTimes/"+ds.getKey()).removeValue(); //path in DB to the movie showtime
                }
                movieDropdown.setText("", false); /*removes selection from movieDropDown dropdown */
                getMovieListFromDb();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}


