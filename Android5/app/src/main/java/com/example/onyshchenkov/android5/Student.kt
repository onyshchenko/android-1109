package com.example.onyshchenkov.android5

import android.os.Parcel
import android.os.Parcelable

class Student : Parcelable {
    var firstName: String? = null
    var lastName: String? = null
    var age: Int = 0

    constructor() {}

    constructor(firstNmae: String, lastNmae: String, age: Int) {
        this.firstName = firstNmae
        this.lastName = lastNmae
        this.age = age
    }

    override fun toString(): String {
        return this.firstName + " " + this.lastName + " " + this.age
        //return super.toString();
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.firstName)
        dest.writeString(this.lastName)
        dest.writeInt(this.age)
    }

    protected constructor(`in`: Parcel) {
        this.firstName = `in`.readString()
        this.lastName = `in`.readString()
        this.age = `in`.readInt()
    }

    companion object {

        val CREATOR: Parcelable.Creator<Student> = object : Parcelable.Creator<Student> {
            override fun createFromParcel(source: Parcel): Student {
                return Student(source)
            }

            override fun newArray(size: Int): Array<Student> {
                return arrayOfNulls(size)
            }
        }
    }
}
