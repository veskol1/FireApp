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
import com.example.planetmovieapp.Objects.ShowTimes;
import com.example.planetmovieapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;
/*This class is responsible on adding new show time for selected movie that is already in database*/
public class AddShowTime extends AppCompatActivity {
    private AutoCompleteTextView movieDropdown,hallDropdown,dateDropdown,hourDropdown;
    private String selectedMovie,movieId,selectedHall,selectedDate,selectedHour;
    private DatabaseReference mDatabase;
    private ArrayList<String> movieNamesList = new ArrayList<>();
    private ArrayList<Movie> movieList = new ArrayList<>();
    private ArrayList<String> hallNamesList = new ArrayList<>();
    private ArrayList<String> defaultHourList = new ArrayList<>(Arrays.asList("16:00","18:00","20:00","22:00")) ;
    private Hall HallObject = new Hall();
    private Button confirmButton;
    private TextInputLayout hallInputLayout,hourInputLayout;
    final int ERASE_BOTTOM_THREE_FIELDS = 3;
    final int ERASE_BOTTOM_TWO_FIELDS = 2;
    final int ERASE_BOTTOM_ONE_FIELDS = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_show_time);
        movieDropdown = findViewById(R.id.movie_list_dropdown);
        hallDropdown = findViewById(R.id.hall_list_dropdown);
        dateDropdown = findViewById(R.id.date_list_dropdown);
        hourDropdown = findViewById(R.id.hour_list_dropdown);
        hallInputLayout = findViewById(R.id.hall_layout_input);
        hourInputLayout = findViewById(R.id.hour_input_layout);
        confirmButton = findViewById(R.id.confirm_add_button);

        getMovieListFromDb();
        getHallsListFromDb();
        updateDatesDropdown();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedMovie != null && selectedHall != null && selectedDate != null && selectedHour != null) {
                    updateMovieId();
                    updateHall();
                    Toast.makeText(AddShowTime.this, "added successfully", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(AddShowTime.this, "Please select all fields", Toast.LENGTH_LONG).show();
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
                    movieList.add(ds.getValue(Movie.class));
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
                selectedMovie = (String)parent.getItemAtPosition(position);
            }
        });
    }


    /*get halls names from Db to arrayList*/
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

    /*update halls dropdown with the halls names */
    public void updateHallsDropdown(){
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.dropdown_menu_popup_item, hallNamesList);
        hallDropdown.setAdapter(adapter);

        hallDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedMovie == null)
                    hallInputLayout.setError("Please, first select Movie");
                else {
                    restoreDefaults(ERASE_BOTTOM_TWO_FIELDS);
                    selectedHall = (String) parent.getItemAtPosition(position);
                }
            }
        });
    }



    public void updateDatesDropdown(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.dropdown_menu_popup_item, getResources().getStringArray(R.array.days_array));
        dateDropdown.setAdapter(adapter);

        dateDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                restoreDefaults(ERASE_BOTTOM_ONE_FIELDS);
                selectedDate = (String) parent.getItemAtPosition(position);
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
                    String hall = ds.child("hallName").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String hour = ds.child("hour").getValue(String.class);

                    //remove hours that already taken
                    if (hall.equals(selectedHall) && date.equals(selectedDate) && defaultHourList.contains(hour))
                        defaultHourList.remove(hour);

                    if (defaultHourList.isEmpty())
                        hourInputLayout.setError("Select another hall or date");
                    else
                        updateHoursDropdown();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void updateHoursDropdown(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.dropdown_menu_popup_item, defaultHourList);
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
                hallDropdown.setText("", false); /*removes selection from Hall dropdown */
                hallInputLayout.setError(null); /*removes the error message*/
                selectedHall = null;

                dateDropdown.setText("", false); /*removes selection from Date dropdown */
                selectedDate = null;

                hourDropdown.setText("", false); /*removes selection from Hour dropdown */
                hourInputLayout.setError(null); /*remove error message*/
                selectedHour = null;
                break;
            }

            case 2:{  //when clicking hall dropdown
                dateDropdown.setText("", false); /*removes selection from Date dropdown */
                selectedDate = null;

                hourDropdown.setText("", false); /*removes selection from Hour dropdown */
                hourInputLayout.setError(null); /*remove error message*/
                selectedHour = null;
                break;
            }

            case 1 :{  //when clicking date dropdown
                hourDropdown.setText("", false); /*removes selection from Hour dropdown */
                hourInputLayout.setError(null); /*remove error message*/
                selectedHour = null;
                defaultHourList = new ArrayList<>(Arrays.asList("16:00","18:00","20:00","22:00"));
                break;
            }
        }
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
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    /*retrieves from database the Hall object that matching the selected hall name*/
    public void updateHall(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Halls");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.child("hallName").getValue(String.class).equals(selectedHall)) {
                        HallObject = ds.getValue(Hall.class);
                        break;
                    }
                }
                addShowTimeToDb();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    /*After we got all the info we need, we update our DB*/
    public void addShowTimeToDb(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        String generatedShowId = mDatabase.push().getKey();
        ShowTimes showTimes = new ShowTimes(generatedShowId, movieId, selectedDate, selectedHour, HallObject);
        mDatabase.child(generatedShowId).setValue(showTimes);
    }


}
