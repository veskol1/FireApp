package com.example.planetmovieapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.planetmovieapp.Objects.Movie;
import com.example.planetmovieapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class DetailedMovieActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView movieNameTextView;
    private TextView movieGenreTextView;
    private TextView movieRatingTextView;
    private TextView movieSummaryTextView;
    private Button nextButton;
    private ImageView posterImageVIew;
    private ImageView trailerImageView;
    private RatingBar movieRatingBar;
    private Movie selectedMovie;
    private String selectedShowDate;
    private String selectedShowHour;
    private DatabaseReference mDatabase;
    private ArrayList<String> filteredDatesArray;
    private ArrayList<String> filteredHoursArray;
    private Spinner spinnerDates;
    private Spinner spinnerHours;
    private final String YOUTUBE_THUMBNAIL_BASE_LINK = "https://img.youtube.com/vi/";
    private final String YOUTUBE_THUMBNAIL_END_LINK = "/mqdefault.jpg";
    private final String YOUTUBE_TRAILER_BASE_LINK = "https://www.youtube.com/watch?v=";
    private AutoCompleteTextView editTextFilledExposedDropdown;
    private AutoCompleteTextView editTextFilledExposedDropdown2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);
        movieNameTextView = findViewById (R.id.movie_name_text);
        nextButton = findViewById(R.id.btn_next);
        spinnerDates = findViewById(R.id.spinner_date);
        spinnerHours = findViewById(R.id.spinner_hour);
        posterImageVIew = findViewById(R.id.image_view_poster);
        trailerImageView = findViewById(R.id.image_view_trailer);
        movieGenreTextView = findViewById(R.id.movie_genre_text);
        movieRatingTextView = findViewById(R.id.movie_rating_text);
        movieSummaryTextView = findViewById(R.id.movie_summary_text);
        movieRatingBar = findViewById(R.id.movie_ratingBar);

        editTextFilledExposedDropdown = findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown2 = findViewById(R.id.filled_exposed_dropdown2);

        filteredDatesArray = new ArrayList<>();
        filteredHoursArray = new ArrayList<>();

        Intent intent = getIntent();
        selectedMovie = (Movie) intent.getSerializableExtra("selected.movie");

        Picasso.get().
                load(selectedMovie.getPosterLink())
                .into(posterImageVIew);

        movieNameTextView.setText(selectedMovie.getMovieName());
        movieGenreTextView.setText(selectedMovie.getGenre());
        Float movieRating = ((float)selectedMovie.getRating()/10)*5;
        movieRatingBar.setRating(movieRating);
        movieSummaryTextView.setText(selectedMovie.getSummary());

        String LinkToTumbnail = YOUTUBE_THUMBNAIL_BASE_LINK+selectedMovie.getTrailerLink()+YOUTUBE_THUMBNAIL_END_LINK;
        Picasso.get().load(LinkToTumbnail)
                .into(trailerImageView);

        trailerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_TRAILER_BASE_LINK+selectedMovie.getTrailerLink())));
            }
        });
        retrieveData();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedMovieActivity.this,SelectSeatsActivity.class);
                intent.putExtra("movieId.to.seats",selectedMovie.getMovieId());
                intent.putExtra("selectedDate.to.seats",selectedShowDate);
                intent.putExtra("selectedHour.to.seats",selectedShowHour);
                startActivity(intent);
            }
        });

    }

    public void retrieveData(){

        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        Query query = mDatabase.orderByChild("movieId").equalTo(selectedMovie.getMovieId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    filteredDatesArray.add(ds.child("date").getValue(String.class));

                //updateSpinnerDates();
                updateSpinnerDates1();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void updateSpinnerDates1() {

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_menu_popup_item, filteredDatesArray);
        editTextFilledExposedDropdown.setAdapter(adapter);

        editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedShowDate = (String)parent.getItemAtPosition(position);
                        retrieveShowHour(selectedShowDate);
            }
        });

    }



    public void updateSpinnerDates(){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, filteredDatesArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDates.setAdapter(arrayAdapter);
        spinnerDates.setOnItemSelectedListener(this);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_date: {
                selectedShowDate = (String)parent.getItemAtPosition(position);
                retrieveShowHour(selectedShowDate);
                break;
            }
            case R.id.spinner_hour:{
                selectedShowHour = (String)parent.getItemAtPosition(position);
                break;
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    public void retrieveShowHour(String date){
        filteredHoursArray.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        Query query = mDatabase.orderByChild("movieId").equalTo(selectedMovie.getMovieId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String dateFiltered = ds.child("date").getValue(String.class);
                    if(dateFiltered.equals(date)) {
                        filteredHoursArray.add(ds.child("hour").getValue(String.class));
                    }
                }
                //updateSpinnerHours();
                updateSpinnerHours1();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void updateSpinnerHours(){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, filteredHoursArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHours.setAdapter(arrayAdapter);
        spinnerHours.setOnItemSelectedListener(this);
    }

    public void updateSpinnerHours1(){
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_menu_popup_item, filteredHoursArray);
        editTextFilledExposedDropdown2.setAdapter(adapter);

        editTextFilledExposedDropdown2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedShowHour = (String)parent.getItemAtPosition(position);
            }
        });

    }



}
