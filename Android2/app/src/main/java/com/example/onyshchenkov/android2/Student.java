package com.example.onyshchenkov.android2;

import android.os.Parcel;
import android.os.Parcelable;


public class Student implements Parcelable {
    public String firstNmae;
    public String lastNmae;
    public int age;
    public int age1;

    public Student() {
    }

    public Student(String firstNmae, String lastNmae, int age) {
        this.firstNmae = firstNmae;
        this.lastNmae = lastNmae;
        this.age = age;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstNmae);
        dest.writeString(this.lastNmae);
        dest.writeInt(this.age);
        dest.writeInt(this.age1);
    }

    protected Student(Parcel in) {
        this.firstNmae = in.readString();
        this.lastNmae = in.readString();
        this.age = in.readInt();
        this.age1 = in.readInt();
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
