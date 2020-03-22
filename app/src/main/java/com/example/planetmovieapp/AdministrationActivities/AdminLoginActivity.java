package com.example.planetmovieapp.AdministrationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planetmovieapp.R;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText usernameLogEditText,passwordLogEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        usernameLogEditText = findViewById(R.id.admin_log_edit);
        passwordLogEditText = findViewById(R.id.admin_password_log_edit);
        Button logButton = findViewById(R.id.admin_log_button);


        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameLogEditText.getText().toString();
                String password = passwordLogEditText.getText().toString();
                if (username.equals("Admin") && password.equals("1234")) {
                    Intent intent = new Intent(AdminLoginActivity.this, AdministrationActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(AdminLoginActivity.this,"Are you administrator?",Toast.LENGTH_LONG).show();
           }
         });

    }
}






