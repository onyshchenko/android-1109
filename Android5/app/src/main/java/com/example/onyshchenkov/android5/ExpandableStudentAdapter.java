package com.example.onyshchenkov.android5;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpandableStudentAdapter extends BaseExpandableListAdapter {
    private int mGroupResource, mChildResource;
    private ArrayList<Group> mGroups;
    private LayoutInflater minflater;

    public ExpandableStudentAdapter(Context context, int groupId, int childId, ArrayList<Group> groups) {
        mGroupResource = groupId;
        mChildResource = childId;
        mGroups = groups;
        minflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).students.length;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).students[childPosition];
    }


    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = minflater.inflate(mGroupResource, null);
        Group group = (Group) getGroup(groupPosition);

        ((TextView)convertView.findViewById(R.id.textViewGroupName)).setText(group.number);

        View indicator = convertView.findViewById(R.id.indicator);
        if (isExpanded){
            indicator.setBackgroundColor(Color.RED);
        } else {
            indicator.setBackgroundColor(Color.GREEN);
        }

        if(group.students.length ==0 ){
            indicator.setBackgroundColor(Color.GRAY);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = minflater.inflate(mChildResource, null);
        Student student = (Student) getChild(groupPosition,childPosition);

        ((TextView)convertView.findViewById(R.id.textViewFirstName)).setText(student.firstName);
        ((TextView)convertView.findViewById(R.id.textViewLastName)).setText(student.lastName);
        ((TextView)convertView.findViewById(R.id.textViewAge)).setText(String.valueOf(student.age));

        return convertView;
    }




    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
