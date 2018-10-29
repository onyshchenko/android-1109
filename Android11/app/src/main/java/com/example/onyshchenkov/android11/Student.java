package com.example.onyshchenkov.android11;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    public String id;
    public String GroupId;
    public String FirstName;
    public String LastName;
    public int Age;

    public String GroupName;

    public Student() {
    }

    public Student(String firstNmae, String lastNmae, int age, String id) {
        this.id = id;
        this.FirstName = firstNmae;
        this.LastName = lastNmae;
        this.Age = age;
    }

    public Student(String firstNmae, String lastNmae, int age) {
        this.FirstName = firstNmae;
        this.LastName = lastNmae;
        this.Age = age;
    }

    @Override
    public String toString() {
        return this.FirstName + " " + this.LastName + " " + this.Age;
        //return super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.GroupId);
        dest.writeString(this.FirstName);
        dest.writeString(this.LastName);
        dest.writeInt(this.Age);
        dest.writeString(this.GroupName);

    }

    protected Student(Parcel in) {
        this.id = in.readString();
        this.GroupId = in.readString();
        this.FirstName = in.readString();
        this.LastName = in.readString();
        this.Age = in.readInt();
        this.GroupName = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
