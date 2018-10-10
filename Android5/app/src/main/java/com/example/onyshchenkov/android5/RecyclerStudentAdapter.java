package com.example.onyshchenkov.android5;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerStudentAdapter extends RecyclerView.Adapter<RecyclerStudentAdapter.StudentViewHolder> {

    private int mResourceid;
    private ArrayList<Student> mStudents;
    private LayoutInflater minflater;

    public RecyclerStudentAdapter(Context context, int resourceid, ArrayList<Student> students) {
        mResourceid = resourceid;
        mStudents = students;
        minflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = minflater.inflate(mResourceid, viewGroup, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder studentViewHolder, int i) {
        final Student student = mStudents.get(i);
        studentViewHolder.setStudent(student);

        studentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(student);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStudents.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewFirstName, mTextViewLastName, mTextViewAge;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewFirstName = itemView.findViewById(R.id.textViewFirstName);
            mTextViewLastName = itemView.findViewById(R.id.textViewLastName);
            mTextViewAge = itemView.findViewById(R.id.textViewAge);
        }

        public void setStudent(Student student) {
            mTextViewFirstName.setText(student.firstName);
            mTextViewLastName.setText(student.lastName);
            mTextViewAge.setText(String.valueOf(student.age));
        }
    }

    public interface ActionListener{
        void onClick(Student student);
    }
    private ActionListener mListener;
    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }
}
