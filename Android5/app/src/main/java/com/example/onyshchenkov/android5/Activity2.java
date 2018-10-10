package com.example.onyshchenkov.android5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class Activity2 extends AppCompatActivity {

    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        mGridView = findViewById(R.id.gridview);
        mGridView.setNumColumns(3);

        String[] items = new String[]{"q","w","e","r","t","y"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                items
        );

        mGridView.setAdapter(adapter);
    }
}
