package com.example.planetmovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText,passwordEditText;
    private Button regButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.username_edit);
        passwordEditText = findViewById(R.id.password_edit);
        regButton = findViewById(R.id.reg_button);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(email.isEmpty() || password.isEmpty() )
                    Toast.makeText(RegisterActivity.this,"Please enter Email and Password",Toast.LENGTH_SHORT).show();
                else
                    registerACount(email,password);
            }
        });
    }


    public void registerACount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    if (password.length()<6)
                        Toast.makeText(RegisterActivity.this, "Please enter at least 6 chars Password", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(RegisterActivity.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
