package com.example.fireapp.AdministrationActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fireapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();  //get instance of the database
        DatabaseReference ref = database.getReference();      // get reference to  database

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//                DatabaseReference postsRef = ref.child("Users");
//                DatabaseReference newPostRef = postsRef.push();
//
//                newPostRef.child("username").setValue(username);
//                newPostRef.child("password").setValue(password);

               // myRef.child("users").child(username).setValue(password);
               // myRef.child("Login").child(username).setValue(username);
               // myRef.child("Login").child(password).setValue(password);
              //  Toast.makeText(getApplicationContext(),"The user was registered to the database",Toast.LENGTH_LONG).show();

               // Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
               // startActivity(intent);
            }
        });

    }



}
