package com.example.fireapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText searchText;
    private ArrayList<ExampleItem> exampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchText = findViewById(R.id.search_text);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        exampleList = new ArrayList<>();
        exampleList.add(new ExampleItem(R.drawable.ic_android,"vesko ","good good"));
        exampleList.add(new ExampleItem(R.drawable.ic_assistant,"fast ","good2 good2"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"very ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"good ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"better ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"best ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"noashemesh ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"lamp ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"goot ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"yah ","good3 good3"));
        exampleList.add(new ExampleItem(R.drawable.ic_insert_emoticon,"regular ","good3 good3"));

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        mAdapter = new ExampleAdapter(exampleList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    public void filter(String text){
        ArrayList<ExampleItem> filteredList = new ArrayList<>();
        for(ExampleItem item : exampleList){
            if(item.getMtext1().toLowerCase().contains(text.toLowerCase()))
                filteredList.add(item);
        }

        mAdapter.filterList(filteredList);
    }

}
