package com.example.planetmovieapp.AdministrationActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planetmovieapp.Objects.Hall;
import com.example.planetmovieapp.Objects.Movie;
import com.example.planetmovieapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RemoveShowTimeActivity extends AppCompatActivity {
    private AutoCompleteTextView movieDropdown, showDatesDropdown, hallDropdown, hourDropdown;
    private String selectedMovieName, selectedHall, selectedShowDate, selectedHour;
    private Movie selectedMovieObject;
    private DatabaseReference mDatabase;
    private ArrayList<String> movieNamesList = new ArrayList<>();
    private ArrayList<Movie> movieObjectsList = new ArrayList<>();
    private ArrayList<String> showDatesList = new ArrayList<>();
    private ArrayList<String> hallNamesList = new ArrayList<>();
    private ArrayList<String> hourList = new ArrayList<>();
    private TextInputLayout hallInputLayout,hourInputLayout;
    final int ERASE_BOTTOM_THREE_FIELDS = 3;
    final int ERASE_BOTTOM_TWO_FIELDS = 2;
    final int ERASE_BOTTOM_ONE_FIELDS = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_showtime);
        movieDropdown = findViewById(R.id.remove_movie_list_dropdown);
        showDatesDropdown = findViewById(R.id.remove_date_list_dropdown);
        hallDropdown = findViewById(R.id.remove_hall_list_dropdown);
        hourDropdown = findViewById(R.id.remove_hour_list_dropdown);
        hallInputLayout = findViewById(R.id.remove_hall_layout_input);
        hourInputLayout = findViewById(R.id.remove_hour_input_layout);
        Button confirmRemoveButton = findViewById(R.id.confirm_remove_button);

        getMovieListFromDb();


        confirmRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedMovieName != null && selectedHall != null && selectedShowDate != null && selectedHour != null) {
                    removeMovieShowTimeFromDB();
                    Toast.makeText(RemoveShowTimeActivity.this, "added successfully", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(RemoveShowTimeActivity.this, "Please select all fields", Toast.LENGTH_LONG).show();
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
                restoreDefaults(ERASE_BOTTOM_THREE_FIELDS);
                selectedMovieName = (String)parent.getItemAtPosition(position);
                selectedMovieObject = findSelectedObject(selectedMovieName);
                getShowDatesListFromDb1();
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



    public void getShowDatesListFromDb1(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String movieId = ds.child("movieId").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);

                    if(movieId.equals(selectedMovieObject.getMovieId()))
                        if(!showDatesList.contains(date)) //remove duplicate dates
                            showDatesList.add(date);
                }
                updateShowDatesDropdown();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void updateShowDatesDropdown(){
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.dropdown_menu_popup_item, showDatesList);
        showDatesDropdown.setAdapter(adapter);

        showDatesDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    restoreDefaults(ERASE_BOTTOM_TWO_FIELDS);
                    selectedShowDate = (String) parent.getItemAtPosition(position);
                    getHallsListFromDb();
            }
        });
    }



    /*get halls names from Db to arrayList*/
    public void getHallsListFromDb(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String movieId = ds.child("movieId").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String hall = ds.child("hallName").getValue(String.class);

                    if(movieId.equals(selectedMovieObject.getMovieId()) && date.equals(selectedShowDate))
                        if(!hallNamesList.contains(hall))           //remove duplicate halls
                            hallNamesList.add(ds.child("hallName").getValue(String.class));
                }
                updateHallsDropdown();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /*update halls dropdown with the halls names */
    public void updateHallsDropdown(){
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.dropdown_menu_popup_item, hallNamesList);
        hallDropdown.setAdapter(adapter);

        hallDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                restoreDefaults(ERASE_BOTTOM_ONE_FIELDS);
                selectedHall = (String) parent.getItemAtPosition(position);
                getHoursFromDB();
            }
        });
    }


    /*update movie dropdown with the movie names arrayList*/
    public void getHoursFromDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String movieId = ds.child("movieId").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String hall = ds.child("hallName").getValue(String.class);
                    String hour = ds.child("hour").getValue(String.class);

                    if(movieId.equals(selectedMovieObject.getMovieId())  &&  date.equals(selectedShowDate)  &&  hall.equals(selectedHall))
                        if(!hourList.contains(hour))
                            hourList.add(ds.child("hour").getValue(String.class));
                }
                updateHoursDropdown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void updateHoursDropdown(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.dropdown_menu_popup_item, hourList);
        hourDropdown.setAdapter(adapter);

        hourDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedHour = (String) parent.getItemAtPosition(position);
            }
        });
    }



    /*restores default value depending on num value and removes error messages*/
    public void restoreDefaults(int num){
        switch (num) {
            case 3 :{  //when clicking movie dropdown
                showDatesDropdown.setText("", false); /*removes selection from Hall dropdown */
                ///////////hallInputLayout.setError(null); /*removes the error message*/
                selectedShowDate = null;

                hallDropdown.setText("", false); /*removes selection from Hall dropdown */
                hallInputLayout.setError(null); /*removes the error message*/
                selectedHall = null;

                hourDropdown.setText("", false); /*removes selection from Hour dropdown */
                hourInputLayout.setError(null); /*remove error message*/
                selectedHour = null;
                break;
            }

            case 2:{  //when clicking showDate dropdown
                hallDropdown.setText("", false); /*removes selection from Hall dropdown */
                hallInputLayout.setError(null); /*removes the error message*/
                selectedHall = null;

                hourDropdown.setText("", false); /*removes selection from Hour dropdown */
                hourInputLayout.setError(null); /*remove error message*/
                selectedHour = null;
                break;
            }

            case 1 :{  //when clicking hall dropdown
                hourDropdown.setText("", false); /*removes selection from Hour dropdown */
                hourInputLayout.setError(null); /*remove error message*/
                selectedHour = null;
                break;
            }
        }
    }


    public void removeMovieShowTimeFromDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String movieId = ds.child("movieId").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String hall = ds.child("hallName").getValue(String.class);
                    String hour = ds.child("hour").getValue(String.class);

                    if(movieId.equals(selectedMovieObject.getMovieId())  &&  date.equals(selectedShowDate)  &&  hall.equals(selectedHall) && hour.equals(selectedHour))
                        mDatabase.getDatabase().getReference("ShowTimes/"+ds.getKey()).removeValue(); //path in DB to the movie showtime
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RemoveShowTimeActivity.this,  databaseError.getDetails(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
