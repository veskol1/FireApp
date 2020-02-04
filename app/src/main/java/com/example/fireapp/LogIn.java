package com.example.fireapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LogIn extends AppCompatActivity {

    private EditText usernameLogEditText,passwordLogEditText;
    private TextView regTextView;
    private Button logButton;
    private FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }



    private void updateUI(FirebaseUser user) {
        //hideProgressBar();

//        if (user != null) {
//            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
//                    user.getEmail(), user.isEmailVerified()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
//            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
//            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);
//
//            findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());
//        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
//
//            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
//            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
//            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
//        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLogEditText = findViewById(R.id.username_log_edit);
        passwordLogEditText = findViewById(R.id.password_log_edit);
        logButton = findViewById(R.id.log_button);
        regTextView = findViewById(R.id.tv_register);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference();

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameLogEditText.getText().toString();
                String password = passwordLogEditText.getText().toString();

                myRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String,String>map= (Map)dataSnapshot.child("users").getValue();
                        if(map!=null)
                            if(map.containsKey(username))
                                if(map.get(username).equals(password)) {
                                    Toast.makeText(LogIn.this, "You are logged in", Toast.LENGTH_SHORT).show();
                                    //Intent intent = new Intent(LogIn.this,MainActivity.class);
                                    Intent intent = new Intent(LogIn.this,DatabaseActivity.class);
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(LogIn.this, "Wrong username or password, Try again!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(LogIn.this, "Wrong username or password, Try again!", Toast.LENGTH_SHORT).show();
                        usernameLogEditText.getText().clear();
                        passwordLogEditText.getText().clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("ha", "Failed to read value.", databaseError.toException());
                    }
                });
            }
        });




        regTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg = new Intent(LogIn.this,Register.class);
                startActivity(intentReg);
            }
        });





    }
}
