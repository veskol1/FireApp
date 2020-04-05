package com.example.planetmovieapp.AdministrationActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planetmovieapp.Objects.Hall;
import com.example.planetmovieapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddHallActivity extends AppCompatActivity {

    private TextView hallNameEditText;
    private AutoCompleteTextView rowDropdown, columnDropdown;
    private DatabaseReference mDatabase;
    private ArrayList<Integer> rowList = new ArrayList<>();
    private ArrayList<Integer> columnList = new ArrayList<>();
    private int maxLinesAndRows = 10; //max rows and columns is 10.
    private Integer selectedColumns, selectedRow;
    private String hallName, hallId;
    private boolean updateIsLegal = true;
    private final int RETURN_NAME_TO_DEFAULT = 1;
    private final int RETURN_ROWS_TO_DEFAULT = 2;
    private final int RETURN_COLUMNS_TO_DEFAULT = 3;


    private void updateList(ArrayList<Integer> list){
        for(int i = 1; i <= maxLinesAndRows; i++){
            list.add(i);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hall);
        updateList(rowList);
        updateList(columnList);
        MaterialButton btnAddMovie = findViewById(R.id.add_hall_btn);
        hallNameEditText = findViewById(R.id.et_hall_name);
        rowDropdown = findViewById(R.id.row_dropdown);
        columnDropdown = findViewById(R.id.column_dropdown);

       updateDropDowns();

        btnAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDatabase = FirebaseDatabase.getInstance().getReference("Halls");
                hallName = hallNameEditText.getText().toString();
                if(!hallName.equals("") && selectedColumns != null && selectedRow != null){
                    checkData();
                }
                else{
                    Toast.makeText(AddHallActivity.this, "Please enter all details.", Toast.LENGTH_SHORT).show();

                }
                }
            });

    }
        public void returnDefaults(){
            hallNameEditText.setText("");
            rowDropdown.setText("", false);
            columnDropdown.setText("", false);

/*            hallDropdown.setText("", false); *//*removes selection from Hall dropdown *//*
            hallInputLayout.setError(null); *//*removes the error message*//*
            selectedHall = null;

            dateDropdown.setText("", false); *//*removes selection from Date dropdown *//*
            selectedDate = null;

            hourDropdown.setText("", false); *//*removes selection from Hour dropdown *//*
            hourInputLayout.setError(null); *//*remove error message*//*
            selectedHour = null;*/

        }
        public void checkData(){
            mDatabase = FirebaseDatabase.getInstance().getReference("Halls");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        String name = ds.child("hallName").getValue(String.class);
                        if(hallName.toLowerCase().equals(name.toLowerCase())){
                            updateIsLegal = false;
                            Toast.makeText(AddHallActivity.this, "Hall name is already in use!", Toast.LENGTH_SHORT).show();
                            returnDefaults();
                        }
                        else{
                            updateIsLegal = true;
                        }
                    }
                    if (updateIsLegal)
                        addHallToDB();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddHallActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                }
            });
        }

        public void addHallToDB(){
            mDatabase = FirebaseDatabase.getInstance().getReference("Halls");
            hallId = mDatabase.push().getKey();

            Hall hall = new Hall(hallId, hallName,selectedRow, selectedColumns);
            mDatabase.child(hallId).setValue(hall);
            Toast.makeText(AddHallActivity.this, "Hall was successfully added to Database", Toast.LENGTH_SHORT).show();
            returnDefaults();
        }

        public void updateDropDowns(){
            //updating Column DropDown
            ArrayAdapter<Integer> columnAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, columnList);
            columnDropdown.setAdapter(columnAdapter);
            columnDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedColumns = (Integer) adapterView.getItemAtPosition(i);

                }
            });

            //updating Column DropDown
            ArrayAdapter<Integer> rowAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, columnList);
            rowDropdown.setAdapter(rowAdapter);
            rowDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedRow = (Integer) adapterView.getItemAtPosition(i);

                }
            });
        }



}
