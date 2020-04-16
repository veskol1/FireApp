package com.example.planetmovieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.planetmovieapp.Objects.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*MainActivity is inflating the movies posters using Recyclerview*/
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText searchText;
    private ArrayList<Movie> moviesList;
    private DatabaseReference mDataBase;
    private ProgressBar progressBar;
    private TextView userTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        userTextView = findViewById(R.id.user_text_view);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // User is signed in
        String userMail =  user.getEmail().split("@")[0]; //gives us the left string before '@'
        userTextView.append(userMail);

        addSearchBar();
        getMovieData();

        layoutManager = new GridLayoutManager(this,3);
        recyclerView.setHasFixedSize(true);
    }

    /*get movies data from DB and sent them to the adapter to inflate the movie posters*/
    public void getMovieData(){
        moviesList = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference("Movies");

        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                    moviesList.add(ds.getValue(Movie.class));

                mAdapter = new MovieAdapter(MainActivity.this ,moviesList);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(mAdapter);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addSearchBar(){
        searchText = findViewById(R.id.search_text);

        TextWatcher mSearchTw  = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) { //after text is entered into the search bar
                if(s!=null)
                    filter(s.toString());
            }
        };
        searchText.addTextChangedListener(mSearchTw);

    }


    //filter the movies by the search and update the adapter
    public void filter(String text){
        ArrayList<Movie> filteredList = new ArrayList<>();
        for(Movie movie : moviesList){
            if(movie.getMovieName().toLowerCase().contains(text.toLowerCase()))
                filteredList.add(movie);
        }
        mAdapter.filterList(filteredList);
    }

}
