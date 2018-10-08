package com.example.onyshchenkov.android4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Activity3 extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        mListView = findViewById(R.id.listview);

        String[] items = new String[]{"eee","eef","r5","t6"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                items
        );

        mListView.setAdapter(adapter);
    }
}
