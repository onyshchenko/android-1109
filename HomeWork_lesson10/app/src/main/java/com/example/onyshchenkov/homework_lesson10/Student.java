package com.example.onyshchenkov.homework_lesson10;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    public long id;
    //public int GroupId;
    public String FirstName;
    public String LastName;
    public int Age;

    public Student() {
    }

    public Student(long id, String firstNmae, String lastNmae, int age) {
        this.id = id;
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
        dest.writeLong(this.id);
        dest.writeString(this.FirstName);
        dest.writeString(this.LastName);
        dest.writeInt(this.Age);
    }

    protected Student(Parcel in) {
        this.id = in.readLong();
        this.FirstName = in.readString();
        this.LastName = in.readString();
        this.Age = in.readInt();
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
