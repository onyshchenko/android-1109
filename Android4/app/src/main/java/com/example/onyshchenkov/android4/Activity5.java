package com.example.onyshchenkov.android4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Activity5 extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);

        mListView = findViewById(R.id.listview);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        String[] items = new String[]{"eee","eef","r5","t6"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1,
                items
        );

        mListView.setAdapter(adapter);

        findViewById(R.id.button22).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mListView.getCheckedItemPosition();
                Toast.makeText(Activity5.this, String.valueOf(position), Toast.LENGTH_LONG).show();
            }
        });
    }
}
