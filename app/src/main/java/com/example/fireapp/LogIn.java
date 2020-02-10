package com.example.fireapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class LogIn extends AppCompatActivity {
    public final String HALL_ARRAY = "com.project.arraylist.halls";
    private EditText usernameLogEditText,passwordLogEditText;
    private TextView regTextView;
    private Button logButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private HashSet<String> hallsIdSet;
    private ArrayList<Hall> hallsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLogEditText = findViewById(R.id.username_log_edit);
        passwordLogEditText = findViewById(R.id.password_log_edit);
        logButton = findViewById(R.id.log_button);
        regTextView = findViewById(R.id.tv_register);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        hallsIdSet = new HashSet<>();
        Query query = mDatabaseRef.child("Halls");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot db : dataSnapshot.getChildren()){
                    Hall hall = db.getValue(Hall.class);
                    hallsArrayList.add(hall);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameLogEditText.getText().toString();
                String password = passwordLogEditText.getText().toString();
                ArrayList<String>arr = new ArrayList<>();
                arr.add("ves");
                arr.add("ves");
                arr.add("ves");
                arr.add("ves");
                if (username.equals("Admin") && password.equals("1234")) {
                    Intent intent = new Intent(LogIn.this, AddMovieActivity.class);
                    intent.putExtra(HALL_ARRAY,hallsArrayList);
                    startActivity(intent);
                }
           }
         });

    }
}





//                myRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Map<String,String>map= (Map)dataSnapshot.child("users").getValue();
//                        if(map!=null)
//                            if(map.containsKey(username))
//                                if(map.get(username).equals(password)) {
//                                    Toast.makeText(LogIn.this, "You are logged in", Toast.LENGTH_SHORT).show();
//                                    //Intent intent = new Intent(LogIn.this,MainActivity.class);
//                                    Intent intent = new Intent(LogIn.this,DatabaseActivity.class);
//                                    startActivity(intent);
//                                }
//                                else
//                                    Toast.makeText(LogIn.this, "Wrong username or password, Try again!", Toast.LENGTH_SHORT).show();
//                            else
//                                Toast.makeText(LogIn.this, "Wrong username or password, Try again!", Toast.LENGTH_SHORT).show();
//                        usernameLogEditText.getText().clear();
//                        passwordLogEditText.getText().clear();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Log.w("ha", "Failed to read value.", databaseError.toException());
//                    }
//                });
     //       }
   //     });

//        regTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentReg = new Intent(LogIn.this,Register.class);
//                startActivity(intentReg);
//            }
//        });





