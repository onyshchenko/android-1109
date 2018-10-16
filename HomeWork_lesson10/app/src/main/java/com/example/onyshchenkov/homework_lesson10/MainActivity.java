package com.example.onyshchenkov.homework_lesson10;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.widget.ExpandableListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_STUDENT = "com.example.onyshchenkov.homework_lesson10.student";
    public static final String EXTRA_GROUP = "com.example.onyshchenkov.homework_lesson10.group";

    //private ExpandableListView mListView;
    public static Cursor mstudentsCursor;
    private int mPosition = -1;
    private ListView mlistView;
    private SimpleCursorAdapter mAdapter;
    private DataBaseHelper mHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mlistView = (ListView) findViewById(R.id.listView);

        mHelper = new DataBaseHelper((this));

        mstudentsCursor = mHelper.getStudentsAndGroups();

        mAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                mstudentsCursor,
                new String[]{"firstname", "lastname"},
                new int[]{android.R.id.text1, android.R.id.text2});

        mlistView.setAdapter(mAdapter);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d("onItemClick", "position= " + position + " mstudentsCursor.getLong: ");
                mPosition = position;
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop", "");

        if (mstudentsCursor != null) {
            mstudentsCursor.close();
            mstudentsCursor = null;

        }
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
                if(mPosition != -1) {
                    mstudentsCursor.moveToPosition(mPosition);
                    mstudentsCursor.getLong(mstudentsCursor.getColumnIndex("_id"));

                    Intent intent = new Intent(this, Activity3.class);

                    Student student = new Student();

                    //Log.d("OnClck", "groupname= " + mstudentsCursor.getString(mstudentsCursor.getColumnIndex("groupname")));

                    student.id = mstudentsCursor.getLong(mstudentsCursor.getColumnIndex("_id"));
                    student.FirstName = mstudentsCursor.getString(mstudentsCursor.getColumnIndex("firstname"));
                    student.LastName = mstudentsCursor.getString(mstudentsCursor.getColumnIndex("lastname"));
                    student.Age = mstudentsCursor.getInt(mstudentsCursor.getColumnIndex("age"));

                    //Log.d("OnClck", "update_student: student.id = " + student.id);

                    intent.putExtra(EXTRA_STUDENT, student);
                    intent.putExtra(EXTRA_GROUP,  mstudentsCursor.getString(mstudentsCursor.getColumnIndex("groupname")));
                    startActivityForResult(intent, 2);
                    mPosition = -1;
                }
            }
            break;
            case R.id.button3: {
                // DELETE
                if(mPosition != -1) {
                    mstudentsCursor.moveToPosition(mPosition);
                    if (mHelper.deleteStudent(mstudentsCursor.getLong(mstudentsCursor.getColumnIndex("_id"))) > 0) {
                        mstudentsCursor = mHelper.getStudentsAndGroups();
                        mAdapter.changeCursor(mstudentsCursor);
                        mAdapter.notifyDataSetChanged();
                    }
                    mPosition = -1;
                }
                //Log.d("OnClck", "mGroups.size= " +  mGroups.size());
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) { //ADD
            if (resultCode == RESULT_OK) {
                //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

                if (data.getExtras() != null) {
                    Student student = data.getParcelableExtra(EXTRA_STUDENT);
                    String group_num = data.getStringExtra(EXTRA_GROUP);

                    if (mHelper.insert_student(student, group_num) > 0 ) {
                        mstudentsCursor = mHelper.getStudentsAndGroups();
                        mAdapter.changeCursor(mstudentsCursor);
                        mAdapter.notifyDataSetChanged();
                        //Log.d("onActivityResult", "insert_student");
                    }
                }
            }
        }
        if (requestCode == 2) { //EDIT
            if (resultCode == RESULT_OK) {
                //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

                if (data.getExtras() != null) {
                    Student student = data.getParcelableExtra(EXTRA_STUDENT);
                    String group_num = data.getStringExtra(EXTRA_GROUP);
                    Log.d("onActivityResult", "update_student: student.id = " + student.id);

                    if (mHelper.update_student(student, group_num) > 0 ) {
                        mstudentsCursor = mHelper.getStudentsAndGroups();
                        mAdapter.changeCursor(mstudentsCursor);
                        mAdapter.notifyDataSetChanged();
                        //Log.d("onActivityResult", "insert_student");
                    }
                    /*
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
                    */

                }
            }
        }
    }

}
