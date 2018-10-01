package com.example.onyshchenkov.android2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        Intent intent = getIntent();
        Student student =  intent.getParcelableExtra(MainActivity.EXTRA_STUDENT);

        TextView textView = findViewById(R.id.textView);
        //textView.setText(student.firstNmae + " " + student.lastNmae + " " + student.age);
        textView.setText(String.format("%s %s, age %d", student.firstNmae, student.lastNmae, student.age));

    }
}
