package com.example.onyshchenkov.android4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.onyshchenkov.android4.R;
import com.example.onyshchenkov.android4.Student;

import java.util.ArrayList;

public class StudentAdapter extends ArrayAdapter<Student> {

    private int mResource;
    private ArrayList<Student> mStudents;
    private LayoutInflater minflater;

    public StudentAdapter(Context context, int resource, ArrayList<Student> objects) {
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
