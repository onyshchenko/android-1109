package com.example.onyshchenkov.android9;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

public class StudentsLoader extends AsyncTaskLoader<ArrayList<Student>> {

    private DataBaseHelper mHelper;

    public StudentsLoader(@NonNull Context context) {
        super(context);
        //mHelper
    }

    @Nullable
    @Override
    public ArrayList<Student> loadInBackground() {
        return null;
    }
}
