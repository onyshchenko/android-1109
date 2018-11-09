package com.example.onyshchenkov.microcrm;

import android.os.Parcel;
import android.os.Parcelable;

public class Client_Info implements Parcelable {
    String client_id;
    String client_surname;
    String client_name;
    String client_oldname;
    String client_city;
    String client_pet;
    int client_cnt_orders;
    long client_first_order;
    long client_last_order;
    double client_sum_all;
    String client_comment;

    public Client_Info(String client_id, String client_surname, String client_name, String client_oldname, String client_city, String client_pet, int client_cnt_orders, long client_first_order, long client_last_order, double client_sum_all, String client_comment) {
        this.client_id = client_id;
        this.client_surname = client_surname;
        this.client_name = client_name;
        this.client_oldname = client_oldname;
        this.client_city = client_city;
        this.client_pet = client_pet;
        this.client_cnt_orders = client_cnt_orders;
        this.client_first_order = client_first_order;
        this.client_last_order = client_last_order;
        this.client_sum_all = client_sum_all;
        this.client_comment = client_comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.client_id);
        dest.writeString(this.client_surname);
        dest.writeString(this.client_name);
        dest.writeString(this.client_oldname);
        dest.writeString(this.client_city);
        dest.writeString(this.client_pet);
        dest.writeInt(this.client_cnt_orders);
        dest.writeLong(this.client_first_order);
        dest.writeLong(this.client_last_order);
        dest.writeDouble(this.client_sum_all);
        dest.writeString(this.client_comment);
    }

    protected Client_Info(Parcel in) {
        this.client_id = in.readString();
        this.client_surname = in.readString();
        this.client_name = in.readString();
        this.client_oldname = in.readString();
        this.client_city = in.readString();
        this.client_pet = in.readString();
        this.client_cnt_orders = in.readInt();
        this.client_first_order = in.readLong();
        this.client_last_order = in.readLong();
        this.client_sum_all = in.readDouble();
        this.client_comment = in.readString();
    }

    public static final Parcelable.Creator<Client_Info> CREATOR = new Parcelable.Creator<Client_Info>() {
        @Override
        public Client_Info createFromParcel(Parcel source) {
            return new Client_Info(source);
        }

        @Override
        public Client_Info[] newArray(int size) {
            return new Client_Info[size];
        }
    };
}