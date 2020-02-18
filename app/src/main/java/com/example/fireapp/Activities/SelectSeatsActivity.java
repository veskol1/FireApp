package com.example.fireapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fireapp.Objects.Hall;
import com.example.fireapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SelectSeatsActivity extends AppCompatActivity implements SeatsAdapter.ListItemClickListener{
    private String showMovieId;
    private String showDateSelected;
    private String showHourSelected;
    private String showTimeId;
    private DatabaseReference mDatabase;
    private ArrayList<String> actualSeatsHall;
    private TextView selectedTicketTextView;
    private Hall actualHall;
    private Button confirmButton;
    private RecyclerView seatsHallRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SeatsAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seats);
        seatsHallRecyclerView = findViewById(R.id.seats_recycler_view);
        confirmButton = findViewById(R.id.btn_confirm);
        selectedTicketTextView = findViewById(R.id.tv_selected_tickets);

        Intent intent = getIntent();
        showMovieId = intent.getStringExtra("movieId.to.seats");
        showDateSelected = intent.getStringExtra("selectedDate.to.seats");
        showHourSelected = intent.getStringExtra("selectedHour.to.seats");

        mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String movieId = (String) ds.child("movieId").getValue();
                    String showDate = (String) ds.child("date").getValue();
                    String showHour = (String) ds.child("hour").getValue();
                    if (showMovieId.equals(movieId) && showDateSelected.equals(showDate) && showHourSelected.equals(showHour)) {
                        actualSeatsHall = (ArrayList<String>) ds.child("seatsHall").getValue();
                        showTimeId = (String) ds.child("statusId").getValue(String.class);
                        actualHall = (Hall) ds.child("hall").getValue(Hall.class);
                    }
                }
                layoutManager = new GridLayoutManager(SelectSeatsActivity.this,actualHall.getColumn());
                seatsHallRecyclerView.setHasFixedSize(true);
                mAdapter = new SeatsAdapter(SelectSeatsActivity.this, actualSeatsHall, actualHall,showTimeId);
                seatsHallRecyclerView.setLayoutManager(layoutManager);
                seatsHallRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



    }

    @Override
    public void onListItemClick(int seatNumber, int numberOfTickets) {
        selectedTicketTextView.setText("Number of selected tickets:"+numberOfTickets);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOfTickets != 0) {//user is choose seat, seat is confirmed on database
                    Toast.makeText(SelectSeatsActivity.this, "You have been purchased " + numberOfTickets + " tickets", Toast.LENGTH_SHORT).show();
                    actualSeatsHall.set(seatNumber,"1");
                    mDatabase = FirebaseDatabase.getInstance().getReference("ShowTimes/"+showTimeId);
                    mDatabase.child("seatsHall").setValue(actualSeatsHall);
                }
                    else
                    Toast.makeText(SelectSeatsActivity.this,"Please select seat first",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
