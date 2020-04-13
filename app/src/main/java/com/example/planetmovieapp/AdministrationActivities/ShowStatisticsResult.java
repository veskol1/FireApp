package com.example.planetmovieapp.AdministrationActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.planetmovieapp.R;

public class ShowStatisticsResult extends AppCompatActivity {
    String movie_stat;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_statistics_result);
        textView = findViewById(R.id.statistics_result);
        try {
            Intent intent = getIntent();
            movie_stat = (String) intent.getSerializableExtra("SELLING_STATISTICS");
            textView.setText(movie_stat);

        }catch (Exception e){
        }
    }
}
