package com.example.onyshchenkov.android5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity5 extends AppCompatActivity {

    private RecyclerView mRecycleView;
    private ArrayList<Student> mStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);

        mRecycleView = findViewById(R.id.recycler);

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mStudents = new ArrayList<>();

        for (int i=0; i< 50; i++){
            mStudents.add(new Student("Ivan " + i, "Ivanov " + i, i));
        }

        RecyclerStudentAdapter adapter = new RecyclerStudentAdapter(this, R.layout.student, mStudents);
        mRecycleView.setAdapter(adapter);

        adapter.setActionListener(new RecyclerStudentAdapter.ActionListener() {
            @Override
            public void onClick(Student student) {
                Toast.makeText(Activity5.this, student.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
