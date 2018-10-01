package com.example.onyshchenkov.android2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Intent intent = getIntent();
        //String text = intent.getStringExtra(MainActivity.EXTRA_TEXT);

        String text = intent.getStringExtra("com1");
        int number = intent.getIntExtra("int1",0);

        TextView textView = findViewById(R.id.textView);
        textView.setText(text);
    }
}
