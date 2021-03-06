package com.example.onyshchenkov.android5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Random;

public class Activity4 extends AppCompatActivity {

    private ExpandableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        mListView = findViewById(R.id.listView);

        Random random = new Random();
        final ArrayList<Group> groups = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Student[] students = new Student[2+random.nextInt(3)];
            for (int j = 0; j < students.length; j++) {
                students[j] = new Student("Ivan " + j, "Ivanov " + j, j);
            }
            Group group = new Group("Group" + i, students);
            groups.add(group);
        }

        final ExpandableStudentAdapter adapter = new ExpandableStudentAdapter(this, R.layout.group, R.layout.student, groups);
        mListView.setAdapter(adapter);

        Button btn = findViewById(R.id.button11);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!groups.isEmpty()) {
                    //Log.d("ClickListener", "getCount: " + mListView.getCount());
                    /*
                    boolean flg_expanded = false;

                    if(mListView.isGroupExpanded(groups.size()-1)){
                        mListView.collapseGroup(groups.size()-1);
                        flg_expanded = true;
                    }
                    */
                    groups.remove(0);

                    adapter.notifyDataSetChanged();
                    if ( groups.size()>0) {
                        if (mListView.isGroupExpanded(groups.size() - 1)) {
                            mListView.collapseGroup(groups.size() - 1);
                            mListView.expandGroup(groups.size()-1);
                        }
                    }
/*
                    if (flg_expanded && groups.size()>0){
                        mListView.expandGroup(groups.size()-1);
                    }
                    */
                }
            }
        });
/*
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d("onGroupClick", "groupPosition: " + groupPosition);
                return false;
            }
        });

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d("onChildClick", "groupPosition: " + groupPosition + ", childPosition: " + childPosition);
                return true;
            }
        });

        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
*/

    }
}
