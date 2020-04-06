package com.example.planetmovieapp.AdministrationActivities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.planetmovieapp.Objects.Hall;
import com.example.planetmovieapp.Objects.Movie;
import com.example.planetmovieapp.R;
import com.example.planetmovieapp.Objects.ShowTimes;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AddMovieActivity extends AppCompatActivity  {
    public final String HALL_ARRAY ="com.project.arraylist.halls";
    public final String ERROR_NUMBER_NOT_BETWEEN_ZERO_TO_TEN ="Rating should between 1-10";
    private EditText movieNameEditText, movieGenreEditText, movieTrailerEditText, moviePosterEditText, movieSummaryEditText, movieRatingEditText;
    private TextInputLayout movieRatingInput;
    private TextInputEditText selectedDateInput;
    private DatabaseReference mDatabase;
    private AutoCompleteTextView hallsDropdown, hoursDropdown;
    private String selectedHall, selectedDate, selectedHour; // holds the selected hall/date/hour dropDowns strings
    private Hall selectedHallObj; //holds the selected dropdown Object of Hall
    private boolean updateIsLegal = true; //approves if on the same date & hour & hall , the movie can be added to DB
    private ArrayList<Hall> hallsArrayList; //holds the halls that are already in DB
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        MaterialButton btnAddMovie = findViewById(R.id.add_movie_button);
        movieNameEditText = findViewById(R.id.et_movie_name);
        movieGenreEditText = findViewById(R.id.et_movie_genre);
        movieSummaryEditText = findViewById(R.id.editTextMovieSummary);
        moviePosterEditText = findViewById(R.id.et_movie_poster_link);
        movieTrailerEditText = findViewById(R.id.et_movie_trailer_link);
        movieRatingEditText = findViewById(R.id.et_movie_rating);
        hallsDropdown = findViewById(R.id.spn_halls);
        selectedDateInput = findViewById(R.id.date_select);
        hoursDropdown = findViewById(R.id.spn_hours);
        movieRatingInput = findViewById(R.id.rating_input_text);

        Intent intent = getIntent(); //load the halls from database on the parent Activity so it can displayed on the spinner
        hallsArrayList = (ArrayList<Hall>)intent.getSerializableExtra(HALL_ARRAY);


        initializeDropdowns();
        initializeAndAddOnClickDatePicker();
        addOnDropDownItemClickListeners();


        btnAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieRatingEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movieRatingInput.setError(null);
                    }
                });
                try {
                    Integer movieRating = Integer.parseInt(movieRatingEditText.getText().toString());
                    if (movieRating >= 1 && movieRating < 10)  //movie rating should be between 1-10
                        getDataFromDb();
                    else
                        movieRatingInput.setError(ERROR_NUMBER_NOT_BETWEEN_ZERO_TO_TEN);
                }catch (Exception e){
                    Toast.makeText(AddMovieActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    //this function is responsible on updating the Date textView after picking the date we want
    public void initializeAndAddOnClickDatePicker(){
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("select date:");
        CalendarConstraints.Builder calendarConstrains = new CalendarConstraints.Builder();
        calendarConstrains.setValidator(DateValidatorPointForward.now());
        builder.setCalendarConstraints(calendarConstrains.build());
        final MaterialDatePicker materialDatePicker = builder.build();


        //Opens dialog for picking Date
        selectedDateInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    materialDatePicker.show(getSupportFragmentManager(),"TIME_PICKER");
            }
        });

        //Opens dialog for picking Date when dialog closed without picking date (solves bug)
        selectedDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"sdfds");
            }
        });


        //updates the textView with the selected Date
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                selectedDate = materialDatePicker.getHeaderText();
                selectedDateInput.setText(selectedDate);
            }
        });
    }


    public void initializeDropdowns(){
        //get hall names from Hall objects list
        ArrayList<String> hallNamesList = new ArrayList<>();
        for(Hall hall : hallsArrayList){
            hallNamesList.add(hall.getHallName());
        }

        //initialize hall dropdown list
        ArrayAdapter hallsAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,hallNamesList);
        hallsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hallsDropdown.setAdapter(hallsAdapter);

        //initialize hours dropdown list
        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(this,R.array.hours_array,android.R.layout.simple_spinner_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hoursDropdown.setAdapter(hourAdapter);
    }


    //this function is responsible on the functionality when selecting hall/hour on drop down
    public void addOnDropDownItemClickListeners(){
        hallsDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedHall = (String) parent.getItemAtPosition(position);
                for(Hall findHall : hallsArrayList)
                    if(findHall.getHallName().equals(selectedHall)) {
                        selectedHallObj = findHall;     //find the hall object from the arrayList
                        break;
                    }
            }
        });

        hoursDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedHour = (String) parent.getItemAtPosition(position);
            }
        });
    }

    //this function checks if the selectd hall/date/hour is not taken
    public void getDataFromDb(){
        mDatabase= FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String hall = ds.child("hallName").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String hour = ds.child("hour").getValue(String.class);
                    if(date.equals(selectedDate) && hour.equals(selectedHour) && hall.equals(selectedHall)) {
                        updateIsLegal = false;
                        Toast.makeText(AddMovieActivity.this, "Can't add movie to Database! \nPlease choose another date, hour, hall", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else
                        updateIsLegal = true;
                }
                if (updateIsLegal)
                    AddMovieToDb();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddMovieActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //adds all the filled data to the DB -> adds new Movie to DB and New Showtime
    public void AddMovieToDb(){
        /*get inserted info from Edit Texts */
        String movieName = movieNameEditText.getText().toString();
        String movieGenre = movieGenreEditText.getText().toString();
        Integer movieRating = Integer.parseInt(movieRatingEditText.getText().toString());
        String movieSummary = movieSummaryEditText.getText().toString();
        String movieTrailer = movieTrailerEditText.getText().toString();
        String moviePoster = moviePosterEditText.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference("Movies");
        String generatedMovieId = mDatabase.push().getKey();
        Movie movie = new Movie(generatedMovieId, movieName, movieGenre, movieRating, movieSummary, movieTrailer, moviePoster);
        mDatabase.child(generatedMovieId).setValue(movie);

        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        String generatedShowId = mDatabase.push().getKey();
        ShowTimes showTimes = new ShowTimes(generatedShowId, generatedMovieId, selectedDate, selectedHour, selectedHallObj);
        mDatabase.child(generatedShowId).setValue(showTimes);

        Toast.makeText(AddMovieActivity.this, "New movie and showtime was successfully added to Database", Toast.LENGTH_SHORT).show();
    }




}