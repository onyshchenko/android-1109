package com.example.onyshchenkov.homework_lesson9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //private int mposition;
    public static final String EXTRA_STUDENT = "com.example.onyshchenkov.homework_lesson9.student";
    public static final String EXTRA_GROUP = "com.example.onyshchenkov.homework_lesson9.group";
    //private ArrayList<Student> mStudents;
    private ArrayList<Group> mGroups;
    private ExpandableListView mListView;
    private ExpandableStudentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mStudents = new ArrayList<>();
        mGroups = new ArrayList<>();
        mListView = findViewById(R.id.listView);

        mAdapter = new ExpandableStudentAdapter(this, R.layout.group, R.layout.student, mGroups);
        mListView.setAdapter(mAdapter);
    }

    public void OnClck(View v) {
        //mposition = -1;
        switch (v.getId()) {
            case R.id.button: {
                startActivityForResult(new Intent(this, Activity2.class), 1);
            }
            break;
            case R.id.button2: {
                //mposition = mListView.getCheckedItemPosition();
                //mposition = madapter.getclckItemPosition();
                //Log.d("OnClck", "mposition " + mposition);
                mAdapter.getChild(mAdapter.getGroupPosition(), mAdapter.getChildPosition());
                //Toast.makeText(MainActivity.this, String.valueOf(mposition), Toast.LENGTH_LONG).show();
                //if (mposition >= 0 && mStudents.size() > mposition) {

                    //Intent intent = new Intent(this, Activity3.class);
                    //intent.putExtra(EXTRA_STUDENT, mStudents.get(mposition));
                    //startActivityForResult(intent, 2);
                //}

               // Log.d("Students", "Students size: " + mStudents.size());
            }
            break;
            case R.id.button3: {
                //mposition = mListView.getCheckedItemPosition();
                //mposition = madapter.getclckItemPosition();
                //Log.d("OnClck", "mposition " + mposition);
                //if (mposition >= 0 && mStudents.size() > mposition) {
                    //Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_LONG).show();
                    //mStudents.remove(mposition);
                    //madapter.notifyDataSetChanged();
                //}
                //Log.d("Students", "Students size: " + mStudents.size());
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
                    String group_num = data.getStringExtra(EXTRA_GROUP);
                    ArrayList<Student> Students = new ArrayList<>();

                    int i = 0;

                    while (i <= mGroups.size()+1 ) {
                        if (mGroups.get(i).number == group_num) {
                            break;
                        }
                        i++;
                    };

                    if (i > mGroups.size()+1) {

                    }
                    else {

                    }


                    mGroups.get(i).number

                    Students.add(student);
                    Group group = new Group(group_num, Students);
                    mGroups.add(group);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
        if (requestCode == 2) { //EDIT
            if (resultCode == RESULT_OK) {
                //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

                if (data.getExtras() != null) {
                    Student student = data.getParcelableExtra(EXTRA_STUDENT);
                    //mStudents.set(mposition, student);
                    //madapter.notifyDataSetChanged();
                }
            }
        }
    }
}
