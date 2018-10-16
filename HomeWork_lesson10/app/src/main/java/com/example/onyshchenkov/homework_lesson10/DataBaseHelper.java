package com.example.onyshchenkov.homework_lesson10;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    private static final String COLUMN_NAME_GROUPS = "groupname";

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

    public long insert_student(Student student, String group) {
        long id = 0;
        SQLiteDatabase db = getWritableDatabase();

        try {
            id = getGroupId(group);
            if (id == 0) {
                //Log.d("update_student", "getGroupId " + id);
                // такой группы еще нет
                id = insertGroup(group);
            }
            // Такая группа уже существует


            ContentValues values = new ContentValues();
            /*
            values.put(COLUMN_NAME_GROUPS, group);
            id = db.insert(TABLE_NAME_GROUPS, null, values);

            values.clear();
            */
            values.put(COLUMN_GROUPID_STUDENTS, id);
            values.put(COLUMN_FIRSTNAME_STUDENTS, student.FirstName);
            values.put(COLUMN_LASTNAME_STUDENTS, student.LastName);
            values.put(COLUMN_AGE_STUDENTS, student.Age);

            id = db.insert(TABLE_NAME_STUDENTS, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public long update_student(Student student, String group) {
        long id = 0;
        int cnt_rows = 0;
        SQLiteDatabase db = getWritableDatabase();

        try {

            id = getGroupId(group);
            if (id == 0) {
                //Log.d("update_student", "getGroupId " + id);
                // такой группы еще нет
                id = insertGroup(group);
            }
            // Такая группа уже существует

            ContentValues values = new ContentValues();
            values.put(COLUMN_FIRSTNAME_STUDENTS, student.FirstName);
            values.put(COLUMN_LASTNAME_STUDENTS, student.LastName);
            values.put(COLUMN_AGE_STUDENTS, student.Age);
            values.put(COLUMN_GROUPID_STUDENTS, id);

            //Log.d("update_student", "student.id " + student.id);


            // updating row
            cnt_rows = db.update(TABLE_NAME_STUDENTS, values, COLUMN_ID_STUDENTS + " = " + student.id, null);
            //Log.d("update_student", "cnt_rows " + cnt_rows);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnt_rows;
    }

    public ArrayList<Student> getStudents() {
        ArrayList<Student> students = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = getWritableDatabase();

        String[] arr_column = {
                COLUMN_ID_STUDENTS,
                COLUMN_NAME_GROUPS,
                COLUMN_FIRSTNAME_STUDENTS,
                COLUMN_LASTNAME_STUDENTS,
                COLUMN_AGE_STUDENTS
        };

        try {
            cursor = db.query(TABLE_NAME_STUDENTS, arr_column, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Student student = new Student();
                    student.id =
                            cursor.getLong(cursor.getColumnIndex("_id"));
                    //student. =
                    //        cursor.getLong(cursor.getColumnIndex("groupid"));
                    student.FirstName =
                            cursor.getString(cursor.getColumnIndex("FirstName"));
                    student.LastName =
                            cursor.getString(cursor.getColumnIndex("LastName"));
                    student.Age =
                            cursor.getInt(cursor.getColumnIndex("Age"));
                    students.add(student);
                    cursor.moveToNext();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return students;
    }

    public Cursor getStudentsAndGroups() {
        //ArrayList<Student> students = new ArrayList<>();
        //Cursor cursor = null;
        SQLiteDatabase db = getWritableDatabase();

        try {
            //MainActivity.mstudentsCursor = db.query(TABLE_NAME_STUDENTS, arr_column,null,null,null,null,null, null);

            String strSQL = "Select t1." + COLUMN_ID_STUDENTS + ", t1." + COLUMN_GROUPID_STUDENTS + ", t2." + COLUMN_NAME_GROUPS + ", t1." + COLUMN_FIRSTNAME_STUDENTS + ", t1." + COLUMN_LASTNAME_STUDENTS + ", t1." + COLUMN_AGE_STUDENTS
                    + " from " + TABLE_NAME_STUDENTS + " t1  left join " + TABLE_NAME_GROUPS + " t2 on t1." + COLUMN_GROUPID_STUDENTS + " = t2." + COLUMN_ID_GROUPS;

            MainActivity.mstudentsCursor = db.rawQuery(strSQL, null);


        } catch (Exception e) {
            e.printStackTrace();
            MainActivity.mstudentsCursor = null;
        }
        /*
        finally {
            if (MainActivity.mstudentsCursor != null) {
                MainActivity.mstudentsCursor.close();
            }
        }
        */
        return MainActivity.mstudentsCursor;
    }

    public long insertGroup(String group) {
        long id = 0;
        SQLiteDatabase db = getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            values.put(COLUMN_NAME_GROUPS, group);

            id = db.insert(TABLE_NAME_GROUPS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public long getGroupId(String group) {
        long id = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String strSQL = "Select " + COLUMN_ID_GROUPS
                    + " from " + TABLE_NAME_GROUPS + " where " + COLUMN_NAME_GROUPS + " = '" + group + "'";

            //Log.d("getGroupId", "strSQL " + strSQL);

            cursor = db.rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID_GROUPS));
                //Log.d("getGroupId", "id " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return id;
    }

    public int deleteStudent(long id) {
        int count = 0;
        SQLiteDatabase db = getWritableDatabase();

        try {
            count = db.delete(TABLE_NAME_STUDENTS, COLUMN_ID_STUDENTS + "=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}
