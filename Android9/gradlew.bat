package com.example.onyshchenkov.homework_lesson12;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_STUDENT = "com.example.onyshchenkov.homework_lesson12.student";
    public static final String EXTRA_GROUP = "com.example.onyshchenkov.homework_lesson12.group";

    public final String BACKUP_FOLDER = "MyFolder_lesson12";
    public final String BACKUP_FILE_STUDENTS = "Students.txt";
    //public final String BACKUP_FILE_GROUPS = "Groups.txt";

    //public static Cursor mstudentsCursor = null;
    private int mPosition = -1;
    private ArrayList<Student> mStudents = null;
    private ListView mlistView;
    //private SimpleCursorAdapter mAdapter;
    private ArrayAdapter mAdapter;
    //private DataBaseHelper mHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mlistView = (ListView) findViewById(R.id.listView);

        //mHelper = new DataBaseHelper((this));

        //mstudentsCursor = mHelper.getStudentsAndGroups();

        mStudents = new Arra