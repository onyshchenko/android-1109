package com.example.onyshchenkov.homework_lesson8;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentArrayAdapter  extends ArrayAdapter<Student> {
    private int mResource;
    private ArrayList<Student> mStudents;
    private LayoutInflater minflater;


    public StudentArrayAdapter(Context context, int resource, ArrayList<Student> objects) {
        super(context, resource, objects);

        mResource = resource;
        mStudents = objects;
        minflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = minflater.inflate(mResource, null);
        Student student = mStudents.get(position);

        ((TextView) convertView.findViewById(R.id.textViewFirstName)).setText(student.firstName);
        ((TextView) convertView.findViewById(R.id.textViewLastName)).setText(student.lastName);
        ((TextView) convertView.findViewById(R.id.textViewAge)).setText(String.valueOf(student.age));


        return convertView;
    }


}
