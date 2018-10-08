package com.example.onyshchenkov.android4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Activity4 extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        mListView = findViewById(R.id.listview);


        ArrayList<Student> students = new ArrayList<>();

        for(int i =0; i< 50;i++) {
            students.add(new Student("ivan"+i, "Ivanov"+i,i));
        }
/*
        ArrayAdapter<Student> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                students
        );
*/

        StudentAdapter adapter = new StudentAdapter(
                this,
                R.layout.student,
                students
        );

        mListView.setAdapter(adapter);
    }
}
