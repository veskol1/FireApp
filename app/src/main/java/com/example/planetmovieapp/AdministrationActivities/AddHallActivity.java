package com.example.planetmovieapp.AdministrationActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planetmovieapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddHallActivity extends AppCompatActivity {

    private TextView hallNameEditText;
    private AutoCompleteTextView rowDropdown, columnDropdown;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hall);
        MaterialButton btnAddMovie = findViewById(R.id.add_hall_btn);
        hallNameEditText = findViewById(R.id.et_hall_name);
        rowDropdown = findViewById(R.id.row_dropdown);
        columnDropdown = findViewById(R.id.column_dropdown);

       // updateDropDowns();




//        btnAddMovie.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String hallName = hallNameEditText.getText().toString();
//                int row = Integer.parseInt(hallRowsEditText.getText().toString());
//                int column = Integer.parseInt(hallColumnsEditText.getText().toString());
//
//
//                //Hall hall = new Hall(hallName,row,column);
//                mDatabase = FirebaseDatabase.getInstance().getReference("Halls");
//                String hallId = mDatabase.push().getKey();
//                //mDatabase.child(hallId).setValue(hall);
//                }
//            });
        }

        public void updateDropDowns(){
          //  ArrayList<int> rowArray = new ArrayList<Integer>();



        }


}
