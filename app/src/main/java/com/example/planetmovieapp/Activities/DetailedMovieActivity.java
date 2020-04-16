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
import android.widget.TextView;
import android.widget.Toast;

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

import java.sql.Time;
import java.util.ArrayList;

/*DetailedMovieActivity is showing all details about the movie
* at this Activity the user can get all the details about the selected movie
* and choose date and hour for the showtime
*/
public class DetailedMovieActivity extends AppCompatActivity  {
    private TextView movieNameTextView;
    private TextView movieGenreTextView;
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
    private final String YOUTUBE_THUMBNAIL_BASE_LINK = "https://img.youtube.com/vi/";
    private final String YOUTUBE_THUMBNAIL_END_LINK = "/mqdefault.jpg";
    private AutoCompleteTextView dateDropdown;
    private AutoCompleteTextView hourDropdown;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);
        movieNameTextView = findViewById (R.id.movie_name_text);
        nextButton = findViewById(R.id.btn_next);
        posterImageVIew = findViewById(R.id.image_view_poster);
        trailerImageView = findViewById(R.id.image_view_trailer);
        movieGenreTextView = findViewById(R.id.movie_genre_text);
        movieSummaryTextView = findViewById(R.id.movie_summary_text);
        movieRatingBar = findViewById(R.id.movie_ratingBar);
        dateDropdown = findViewById(R.id.date_dropdown);
        hourDropdown = findViewById(R.id.hour_dropdown);
        filteredDatesArray = new ArrayList<>();
        filteredHoursArray = new ArrayList<>();


        Intent intent = getIntent();
        selectedMovie = (Movie) intent.getSerializableExtra("selected.movie");

        inflateUserInterface();
        loadShowDateFromDb();
    }

    public void inflateUserInterface(){
        Picasso.get().
                load(selectedMovie.getPosterLink())
                .into(posterImageVIew);

        movieNameTextView.setText(selectedMovie.getMovieName());
        movieGenreTextView.setText(selectedMovie.getGenre());
        Float movieRating = ((float)selectedMovie.getRating()/10)*5; //calculate the relative rating number 1-10 to 1-5 Stars
        movieRatingBar.setRating(movieRating);
        movieSummaryTextView.setText(selectedMovie.getSummary());
        String linkId = selectedMovie.getTrailerLink().split("=")[1];;

        String LinkToThumbnail = YOUTUBE_THUMBNAIL_BASE_LINK+linkId+YOUTUBE_THUMBNAIL_END_LINK;
        Picasso.get().load(LinkToThumbnail)
                .into(trailerImageView);

        trailerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selectedMovie.getTrailerLink())));
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(selectedShowDate == null) &&!(selectedShowHour == null)) {
                    Intent intent = new Intent(DetailedMovieActivity.this, SelectSeatsActivity.class);
                    intent.putExtra("selectedMovie.to.seats", selectedMovie);
                    intent.putExtra("selectedDate.to.seats", selectedShowDate);
                    intent.putExtra("selectedHour.to.seats", selectedShowHour);
                    startActivity(intent);
                }
                else
                    Toast.makeText(DetailedMovieActivity.this,"Please select date and hour",Toast.LENGTH_SHORT).show();
            }
        });

    }


    /*this function loads show data dates for the selected movie and updates the date data dropdown */
    public void loadShowDateFromDb(){
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        Query query = mDatabase.orderByChild("movieId").equalTo(selectedMovie.getMovieId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String date = ds.child("date").getValue(String.class);
                    if(!filteredDatesArray.contains(date)) /*we want to remove duplicate dates*/
                        filteredDatesArray.add(ds.child("date").getValue(String.class));
                }
                inflateDatesDropdown();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void inflateDatesDropdown() {
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_menu_popup_item, filteredDatesArray);
        dateDropdown.setAdapter(adapter);

        dateDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedShowDate = (String)parent.getItemAtPosition(position);
                loadShowHoursFromDb(selectedShowDate);
                hourDropdown.setText("");
            }
        });
    }


    /*this function loads show data hours for the selected movie and date and finally updates the date data dropdown */
    public void loadShowHoursFromDb(String date){
        filteredHoursArray.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        Query query = mDatabase.orderByChild("movieId").equalTo(selectedMovie.getMovieId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String filteredDate = ds.child("date").getValue(String.class);
                    if(filteredDate.equals(date) ) { //find show with the selected date
                        String filteredHour = ds.child("hour").getValue(String.class);
                        if(!filteredHoursArray.contains(filteredHour))  //remote hours that are duplicates -> can happen when have 1 movie same data different halls
                            filteredHoursArray.add(ds.child("hour").getValue(String.class));
                    }
                }
                inflateHoursDropdown();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void inflateHoursDropdown(){
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_menu_popup_item, filteredHoursArray);
        hourDropdown.setAdapter(adapter);

        hourDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedShowHour = (String)parent.getItemAtPosition(position);
            }
        });
    }

}
