package com.example.onyshchenkov.android4;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    public String firstName;
    public String lastName;
    public int age;

    public Student() {
    }

    public Student(String firstNmae, String lastNmae, int age) {
        this.firstName = firstNmae;
        this.lastName = lastNmae;
        this.age = age;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " " + this.age;
        //return super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeInt(this.age);
    }

    protected Student(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.age = in.readInt();
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
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
