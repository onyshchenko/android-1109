package com.example.onyshchenkov.homework_lesson12;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Group implements Parcelable {
    public String groupId;
    public String groupName;
    public ArrayList<Student> students;

    public Group(){
    }

    public Group(String id, String number) {
        this.groupName = number;
        this.groupId = id;
    }

    public Group(String number, ArrayList<Student> students) {
        this.groupName = number;
        this.students = students;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupId);
        dest.writeString(this.groupName);
        dest.writeTypedList(this.students);
    }

    protected Group(Parcel in) {
        this.groupId = in.readString();
        this.groupName = in.readString();
        this.students = in.createTypedArrayList(Student.CREATOR);
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
