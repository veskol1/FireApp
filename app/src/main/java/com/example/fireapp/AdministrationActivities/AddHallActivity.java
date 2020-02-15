package com.example.fireapp.AdministrationActivities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fireapp.R;
import com.google.firebase.database.FirebaseDatabase;

public class AddHallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hall);

    }

//    public void addHall(){
//        String hallName = hallNameTv.getText().toString();
//        int row = Integer.parseInt(hallRowTv.getText().toString());
//        int column = Integer.parseInt(hallColumnTv.getText().toString());
//
//        //Hall hall = new Hall(hallName,row,column);
//        mDatabase = FirebaseDatabase.getInstance().getReference("Halls");
//        String hallId = mDatabase.push().getKey();
//        //mDatabase.child(hallId).setValue(hall);
//
//
//    }
}
