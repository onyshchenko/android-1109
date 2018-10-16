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
                // ADD
                startActivityForResult(new Intent(this, Activity2.class), 1);
            }
            break;
            case R.id.button2: {
                // EDIT
                //mAdapter.getChild(mAdapter.getGroupPosition(), mAdapter.getChildPosition());
                Group group = mGroups.get(mAdapter.getGroupPosition());
                ArrayList<Student> Students = group.students;


                //Toast.makeText(MainActivity.this, String.valueOf(mposition), Toast.LENGTH_LONG).show();
                //if (mposition >= 0 && mStudents.size() > mposition) {

                Intent intent = new Intent(this, Activity3.class);
                intent.putExtra(EXTRA_STUDENT, Students.get(mAdapter.getChildPosition()));
                intent.putExtra(EXTRA_GROUP, group.number);
                startActivityForResult(intent, 2);
                //}

                // Log.d("Students", "Students size: " + mStudents.size());
            }
            break;
            case R.id.button3: {
                // DELETE
                if(!mGroups.isEmpty()) {
                    mGroups.get(mAdapter.getGroupPosition()).students.remove(mAdapter.getChildPosition());
                    if (mGroups.get(mAdapter.getGroupPosition()).students.isEmpty()) {
                        mGroups.remove(mAdapter.getGroupPosition());
                    }
                    /*
                    Group group = mGroups.get(mAdapter.getGroupPosition());
                    ArrayList<Student> Students = group.students;
                    Students.remove(mAdapter.getChildPosition());

                    if (Students.isEmpty()) {
                        mGroups.remove(mAdapter.getGroupPosition());
                    } else {
                        group.students = Students;
                        mGroups.set(mAdapter.getGroupPosition(), group);
                    }
                    */

                    mAdapter.notifyDataSetChanged();

                    if(mListView.isGroupExpanded(mAdapter.getGroupPosition())){
                        mListView.collapseGroup(mAdapter.getGroupPosition());
                        mListView.expandGroup(mAdapter.getGroupPosition());
                    }else {
                        mListView.collapseGroup(mAdapter.getGroupPosition());
                        mListView.expandGroup(mAdapter.getGroupPosition());
                    }

                    mAdapter.clrPosition();

                }
                //Log.d("OnClck", "mGroups.size= " +  mGroups.size());
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
                    int group_num = data.getIntExtra(EXTRA_GROUP,0);
                    ArrayList<Student> Students = new ArrayList<>();

                    int i = 0;

                    if (!mGroups.isEmpty()) {
                        // Ищем есть ли у нас уже группа с таким номером
                        //Log.d("onActivityResult", "group_num= " + group_num + "  mGroups.size= " +  mGroups.size());

                        while (i < mGroups.size()) {
                            //Log.d("onActivityResult", "i= " + i + "  mGroups.size= " +  mGroups.size());
                            if (mGroups.get(i).number == group_num) {
                                break;
                            }
                            i++;
                        }
                    }

                    if (i+1 > mGroups.size()) {
                        //ADD New group
                        Students.add(student);
                        Group group = new Group(group_num, Students);
                        mGroups.add(group);
                    } else {
                        // нужно добавить студента в сущ. группу
                        Group group = mGroups.get(i);
                        Students = group.students;
                        Students.add(student);
                        group.students = Students;
                        mGroups.set(i, group);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
        if (requestCode == 2) { //EDIT
            if (resultCode == RESULT_OK) {
                //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

                if (data.getExtras() != null) {
                    Student student = data.getParcelableExtra(EXTRA_STUDENT);
                    int group_num = data.getIntExtra(EXTRA_GROUP,0);
                    ArrayList<Student> Students;

                    if (mGroups.get(mAdapter.getGroupPosition()).number == group_num) {
                        // группа не поменялась
                        Group group = mGroups.get(mAdapter.getGroupPosition());
                        Students = group.students;
                        Students.set(mAdapter.getChildPosition(),student);
                        group.students = Students;
                        mGroups.set(mAdapter.getGroupPosition(), group);
                    }
                    else {
                        // группа поменялась

                        //удалим выбранный элемент из группы
                        Group group = mGroups.get(mAdapter.getGroupPosition());
                        Students = group.students;
                        Students.remove(mAdapter.getChildPosition());

                        if(Students.isEmpty()) {
                            mGroups.remove(mAdapter.getGroupPosition());
                        }
                        else {
                            group.students = Students;
                            mGroups.set(mAdapter.getGroupPosition(), group);
                        }

                        //добавим "нового" студента в новую группу
                        int i = 0;

                        if (!mGroups.isEmpty()) {
                            // Ищем есть ли у нас уже группа с таким номером
                            //Log.d("onActivityResult", "group_num= " + group_num + "  mGroups.size= " +  mGroups.size());

                            while (i < mGroups.size()) {
                                //Log.d("onActivityResult", "i= " + i + "  mGroups.size= " +  mGroups.size());
                                if (mGroups.get(i).number == group_num) {
                                    break;
                                }
                                i++;
                            }
                        }

                        if (i+1 > mGroups.size()) {
                            //ADD New group
                            ArrayList<Student> Students_new = new ArrayList<>();
                            Students_new.add(student);
                            group = new Group(group_num, Students_new);
                            mGroups.add(group);
                        } else {
                            // нужно добавить студента в сущ. группу
                            group = mGroups.get(i);
                            Students = group.students;
                            Students.add(student);
                            group.students = Students;
                            mGroups.set(i, group);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
