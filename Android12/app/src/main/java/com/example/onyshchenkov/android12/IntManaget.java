package com.example.onyshchenkov.android12;

import android.content.Context;
import android.content.SharedPreferences;

public class IntManaget {

    public static int nextInc(Context context){
        SharedPreferences preferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        int num = preferences.getInt("Number", 0);

        num++;
        if(num > 999999) {
            num = 1;
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Number", num);
        editor.apply();

        return num;
    }
}
