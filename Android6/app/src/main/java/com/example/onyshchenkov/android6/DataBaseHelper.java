package com.example.onyshchenkov.android6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MyDB.db";

    private static final String TABLE_NAME_STUDENTS = "students";
    private static final String TABLE_NAME_GROUPS = "groups";

    private static final String COLUMN_ID_STUDENTS = "_id";
    private static final String COLUMN_GROUPID_STUDENTS = "groupid";
    private static final String COLUMN_FIRSTNAME_STUDENTS = "firstname";
    private static final String COLUMN_LASTNAME_STUDENTS = "lastname";
    private static final String COLUMN_AGE_STUDENTS = "age";

    private static final String COLUMN_ID_GROUPS = "_id";
    private static final String COLUMN_NAME_GROUPS = "groupid";

    // Table Create Statements
    private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_NAME_STUDENTS + "("
            + COLUMN_ID_STUDENTS + " integer primary key autoincrement,"
            + COLUMN_GROUPID_STUDENTS + " integer not null,"
            + COLUMN_FIRSTNAME_STUDENTS + " text not null,"
            + COLUMN_LASTNAME_STUDENTS + " text not null,"
            + COLUMN_AGE_STUDENTS + " integer not null)";

    private static final String CREATE_TABLE_GROUPS = "CREATE TABLE " + TABLE_NAME_GROUPS + "("
            + COLUMN_ID_GROUPS + " integer primary key autoincrement,"
            + COLUMN_NAME_GROUPS + " text not null)";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENTS);
        db.execSQL(CREATE_TABLE_GROUPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUPS);
        // create new tables
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUPS);
        // create new tables
        onCreate(sqLiteDatabase);
    }

    public long insert_student(Student student) {
        long id = 0;
        SQLiteDatabase db = getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(COLUMN_FIRSTNAME_STUDENTS, student.firstName);
            values.put(COLUMN_LASTNAME_STUDENTS, student.lastName);
            values.put(COLUMN_AGE_STUDENTS, student.age);
            id = db.insert(TABLE_NAME_STUDENTS, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public ArrayList<Student> getStudents(){
        ArrayList<Student> students = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = getWritableDatabase();

        String[] arr_column = {
                COLUMN_ID_STUDENTS,
                COLUMN_FIRSTNAME_STUDENTS,
                COLUMN_LASTNAME_STUDENTS,
                COLUMN_AGE_STUDENTS
        };


        try {
            cursor = db.query(TABLE_NAME_STUDENTS, arr_column,null,null,null,null,null, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Student student = new Student();
                    student.id =
                            cursor.getLong(cursor.getColumnIndex("_id"));
                    //student. =
                    //        cursor.getLong(cursor.getColumnIndex("groupid"));
                    student.firstName =
                            cursor.getString(cursor.getColumnIndex("FirstName"));
                    student.lastName =
                            cursor.getString(cursor.getColumnIndex("LastName"));
                    student.age =
                            cursor.getInt(cursor.getColumnIndex("Age"));
                    students.add(student);
                    cursor.moveToNext();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return students;
    }
}
