package com.example.planetmovieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.planetmovieapp.Objects.Hall;
import com.example.planetmovieapp.R;
import java.util.ArrayList;


public class OrderDetailsActivity extends AppCompatActivity {
    private TextView textView;
    private Hall actualHall;
    private ArrayList<Integer> listAllNewSelectedSeat = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        textView = findViewById(R.id.text_view_check);


        Intent intent = getIntent();
        listAllNewSelectedSeat= intent.getIntegerArrayListExtra("ListAllNewSelectedSeat");
        actualHall = (Hall)intent.getSerializableExtra("actualHall");
        Integer hallColumns = actualHall.getColumn();
        for(Integer seat :listAllNewSelectedSeat) {
            textView.append(
                    "Your order has confirmed!\n\n" + "Number of tickets: " + (listAllNewSelectedSeat.size()) + "\n\n" +
                            "Row: " + (((seat) / hallColumns) + 1) + ", " + "Column: " + (((seat) % hallColumns) + 1) + "\n");

        }


    }

}
