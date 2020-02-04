package com.example.fireapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DatabaseActivity extends AppCompatActivity {
    private TextView textView;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        textView = findViewById(R.id.textView);


        mDatabase = FirebaseDatabase.getInstance().getReference("movies");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
 //               Log.d("blabla",""+dataSnapshot.getChildren().toString());
//                Map<String,String> map= (Map)dataSnapshot.child("movies").getValue();
//                if(map!=null)
//                    textView.setText(map.get("title"));
//                    //Toast.makeText(DatabaseActivity.this,"blabla"+map.size(),Toast.LENGTH_LONG).show();
                for(DataSnapshot ds :dataSnapshot.getChildren()){
                    String var= ds.child("title").getValue(String.class);
                    if(var!=null)
                         textView.append(var);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
}
