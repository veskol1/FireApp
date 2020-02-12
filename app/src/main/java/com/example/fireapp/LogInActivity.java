package com.example.fireapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {
    private EditText usernameLogEditText,passwordLogEditText;
    private TextView regTextView;
    private Button logButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLogEditText = findViewById(R.id.username_log_edit);
        passwordLogEditText = findViewById(R.id.password_log_edit);
        logButton = findViewById(R.id.log_button);
        regTextView = findViewById(R.id.tv_register);


        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameLogEditText.getText().toString();
                String password = passwordLogEditText.getText().toString();
                if (username.equals("Admin") && password.equals("1234")) {
                    Intent intent = new Intent(LogInActivity.this, AdministrationActivity.class);
                    startActivity(intent);
                }
           }
         });

    }
}






