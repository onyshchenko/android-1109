package com.example.onyshchenkov.homework_lesson9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int mposition;
    public static final String EXTRA_STUDENT = "com.example.onyshchenkov.homework_lesson9.student";
    public static final String EXTRA_GROUP = "com.example.onyshchenkov.homework_lesson9.group";
    private ArrayList<Student> mStudents;
    private ArrayList<Group> mSroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClck(View v) {
        mposition = -1;
        switch (v.getId()) {
            case R.id.button: {
                startActivityForResult(new Intent(this, Activity2.class), 1);
            }
            break;
            case R.id.button2: {
                //mposition = mListView.getCheckedItemPosition();
                mposition = madapter.getclckItemPosition();
                Log.d("OnClck", "mposition " + mposition);
                //Toast.makeText(MainActivity.this, String.valueOf(mposition), Toast.LENGTH_LONG).show();
                if (mposition >= 0 && mStudents.size() > mposition) {

                    Intent intent = new Intent(this, Activity3.class);
                    intent.putExtra(EXTRA_STUDENT, mStudents.get(mposition));
                    startActivityForResult(intent, 2);
                }

                Log.d("Students", "Students size: " + mStudents.size());
            }
            break;
            case R.id.button3: {
                //mposition = mListView.getCheckedItemPosition();
                mposition = madapter.getclckItemPosition();
                Log.d("OnClck", "mposition " + mposition);
                if (mposition >= 0 && mStudents.size() > mposition) {
                    //Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_LONG).show();
                    mStudents.remove(mposition);
                    madapter.notifyDataSetChanged();
                }
                Log.d("Students", "Students size: " + mStudents.size());
            }
            break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

        if (requestCode == 1) { //ADD
            if (resultCode == RESULT_OK) {
                //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

                if (data.getExtras() != null) {
                    Student student = data.getParcelableExtra(EXTRA_STUDENT);
                    int group = data.getParcelableExtra(EXTRA_GROUP);
                    mStudents.add(student);
                    Group group = new Group("Group " + i, students);

                    mSroups.add(group,student);
                    madapter.notifyDataSetChanged();


                }
            }
        }
        if (requestCode == 2) { //EDIT
            if (resultCode == RESULT_OK) {
                //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

                if (data.getExtras() != null) {
                    Student student = data.getParcelableExtra(EXTRA_STUDENT);
                    mStudents.set(mposition, student);
                    madapter.notifyDataSetChanged();
                }
            }
        }
    }
}
