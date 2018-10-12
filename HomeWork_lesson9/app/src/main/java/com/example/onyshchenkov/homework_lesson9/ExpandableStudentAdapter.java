package com.example.onyshchenkov.homework_lesson9;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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

    private int mgroupPosition = -1;
    private int mchildPosition = -1;

    public ExpandableStudentAdapter(Context context, int groupId, int childId, ArrayList<Group> groups) {
        mGroupResource = groupId;
        mChildResource = childId;
        mGroups = groups;
        minflater = LayoutInflater.from(context);
    }

    public int getGroupPosition(){
        return mgroupPosition;
    }

    public int getChildPosition(){
        return mchildPosition;
    }

    @Override
    public int getGroupCount() {
        //Log.d("StudentAdapter", "getGroupCount " + mGroups.size());
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //Log.d("StudentAdapter", "getChildrenCount " + mGroups.get(groupPosition).students.size());
        return mGroups.get(groupPosition).students.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //Log.d("StudentAdapter", "getChild " + mGroups.get(groupPosition).students.get(childPosition));
        return mGroups.get(groupPosition).students.get(childPosition);
    }


    @Override
    public Object getGroup(int groupPosition) {
       // Log.d("StudentAdapter", "getGroup " + mGroups.get(groupPosition));
        return mGroups.get(groupPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = minflater.inflate(mGroupResource, null);

        Group group = (Group) getGroup(groupPosition);
        ((TextView)convertView.findViewById(R.id.textViewGroupName)).setText(String.valueOf(group.number));

        /*

        View indicator = convertView.findViewById(R.id.indicator);
        if (isExpanded){
            indicator.setBackgroundColor(Color.RED);
        } else {
            indicator.setBackgroundColor(Color.GREEN);
        }

        if(group.students.size() ==0 ){
            indicator.setBackgroundColor(Color.GRAY);
        }
        */

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = minflater.inflate(mChildResource, null);
        Student student = (Student) getChild(groupPosition,childPosition);

        ((TextView)convertView.findViewById(R.id.textViewFirstName)).setText(student.firstName);
        ((TextView)convertView.findViewById(R.id.textViewLastName)).setText(student.lastName);
        ((TextView)convertView.findViewById(R.id.textViewAge)).setText(String.valueOf(student.age));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("getChildView", "groupPosition: " + groupPosition + ", childPosition: " + childPosition);
                mgroupPosition = groupPosition;
                mchildPosition = childPosition;
                notifyDataSetChanged();
            }
        });

        if(groupPosition == mgroupPosition && childPosition == mchildPosition){
            //radioButton.setChecked(true);
            //Log.d("setBackgroundColor", "setBackgroundColor " + R.color.colorselected );
            convertView.setBackgroundResource(R.color.colorselected);


                    //(ContextCompat.getColor(getContext(),R.color.colorselected));
        }else {
            //radioButton.setChecked(false);
            //Log.d("setBackgroundColor", "setBackgroundColor " + R.color.colorunselected );
            convertView.setBackgroundResource(R.color.colorunselected);
        }

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
