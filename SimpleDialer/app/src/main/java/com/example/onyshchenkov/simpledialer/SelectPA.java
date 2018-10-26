package com.example.onyshchenkov.simpledialer;

import android.graphics.drawable.Icon;
import android.os.Parcel;
import android.os.Parcelable;
import android.telecom.PhoneAccountHandle;

public class SelectPA implements Parcelable {
    int position;
    PhoneAccountHandle handle;
    String name;
    Icon bitmap;

    public SelectPA(int position, PhoneAccountHandle handle, String name, Icon bitmap) {
        this.position = position;
        this.handle = handle;
        this.name = name;
        this.bitmap = bitmap;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.position);
        dest.writeParcelable(this.handle, flags);
        dest.writeString(this.name);
        dest.writeParcelable(this.bitmap, flags);
    }

    protected SelectPA(Parcel in) {
        this.position = in.readInt();
        this.handle = in.readParcelable(PhoneAccountHandle.class.getClassLoader());
        this.name = in.readString();
        this.bitmap = in.readParcelable(Icon.class.getClassLoader());
    }

    public static final Parcelable.Creator<SelectPA> CREATOR = new Parcelable.Creator<SelectPA>() {
        @Override
        public SelectPA createFromParcel(Parcel source) {
            return new SelectPA(source);
        }

        @Override
        public SelectPA[] newArray(int size) {
            return new SelectPA[size];
        }
    };
}
