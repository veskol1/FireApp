package com.example.fireapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText searchText;
    private ArrayList<ExampleItem> exampleList;
    private ArrayList<Movie> moviesList;
    private DatabaseReference mDataBase;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == (R.id.settings_menu)) {
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addSearchBar();

        getMovieData();


        exampleList = new ArrayList<>();
        exampleList.add(new ExampleItem(R.drawable.ic_android,"vesko ","good good"));
        exampleList.add(new ExampleItem(R.drawable.ic_assistant,"fast ","good2 good2"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"very ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"good ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"better ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"best ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"noashemesh ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"lamp ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"goot ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"yah ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"regular ","good3 good3"));

        recyclerView = findViewById(R.id.recyclerView);
//        layoutManager = new LinearLayoutManager(this);
        layoutManager = new GridLayoutManager(this,3);
        recyclerView.setHasFixedSize(true);
        //mAdapter = new ExampleAdapter(exampleList);
//        mAdapter = new ExampleAdapter(moviesList);
//
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(mAdapter);

    }


    public void getMovieData(){
        moviesList = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference("Movies");

        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    moviesList.add(ds.getValue(Movie.class));
                    Log.d("kok",""+ds.getValue(Movie.class).getMovieId());
                }

                Log.d("kok","size is"+moviesList.size());
                mAdapter = new ExampleAdapter(moviesList);

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }




    public void addSearchBar(){
        searchText = findViewById(R.id.search_text);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    public void filter(String text){
        ArrayList<ExampleItem> filteredList = new ArrayList<>();
        for(ExampleItem item : exampleList){
            if(item.getMtext1().toLowerCase().contains(text.toLowerCase()))
                filteredList.add(item);
        }

        mAdapter.filterList(filteredList);
    }

}
