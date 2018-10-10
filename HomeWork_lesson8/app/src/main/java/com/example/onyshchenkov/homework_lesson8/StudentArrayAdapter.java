package com.example.onyshchenkov.homework_lesson8;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
    private int mposition = -1;


    public StudentArrayAdapter(Context context, int resource, ArrayList<Student> objects) {
        super(context, resource, objects);

        mResource = resource;
        mStudents = objects;
        minflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = minflater.inflate(mResource, null);
        Student student = mStudents.get(position);
        //mposition = position;

        ((TextView) convertView.findViewById(R.id.textViewFirstName)).setText(student.firstName);
        ((TextView) convertView.findViewById(R.id.textViewLastName)).setText(student.lastName);
        ((TextView) convertView.findViewById(R.id.textViewAge)).setText(String.valueOf(student.age));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onItemClick", "position " + position + " mposition " + mposition );
/*
                if(mposition >= 0){
                    //notifyItemChanged(mPosition);
                    notifyDataSetChanged();
                }
*/
                mposition = position;
                notifyDataSetChanged();
            }
        });

        if(position == mposition){
            //radioButton.setChecked(true);
            Log.d("setBackgroundColor", "setBackgroundColor " + R.color.colorselected );
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorselected));
        }else {
            //radioButton.setChecked(false);
            Log.d("setBackgroundColor", "setBackgroundColor " + R.color.colorunselected );
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorunselected));
        }

        return convertView;
    }

    public int getclckItemPosition(){
        return mposition;
    }


}
