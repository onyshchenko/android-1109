package com.example.onyshchenkov.android12;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.UUID;

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
            + COLUMN_ID_STUDENTS + " text primary key,"
            + COLUMN_GROUPID_STUDENTS + " text not null,"
            + COLUMN_FIRSTNAME_STUDENTS + " text not null,"
            + COLUMN_LASTNAME_STUDENTS + " text not null,"
            + COLUMN_AGE_STUDENTS + " integer not null)";

    private static final String CREATE_TABLE_GROUPS = "CREATE TABLE " + TABLE_NAME_GROUPS + "("
            + COLUMN_ID_GROUPS + " text primary key,"
            + COLUMN_NAME_GROUPS + " text not null UNIQUE )";


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

    public String insert_student(Student student, String group_name, String group_uuid) {
        long id = 0;
        String str_uuid_group = "";
        String str_uuid = UUID.randomUUID().toString();;
        SQLiteDatabase db = getWritableDatabase();

        try {
            if (group_uuid.equals("")) {
                str_uuid_group = getGroupId(group_name);
                if (str_uuid_group == "") {
                    //Log.d("update_student", "getGroupId " + id);
                    // такой группы еще нет
                    str_uuid_group = insertGroup(group_name, "");
                }
            }
            else {
                str_uuid_group = group_uuid;
            }
            // Такая группа уже существует
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID_STUDENTS, str_uuid);
            values.put(COLUMN_GROUPID_STUDENTS, str_uuid_group);
            values.put(COLUMN_FIRSTNAME_STUDENTS, student.FirstName);
            values.put(COLUMN_LASTNAME_STUDENTS, student.LastName);
            values.put(COLUMN_AGE_STUDENTS, student.Age);

            id = db.insert(TABLE_NAME_STUDENTS, null, values);

        } catch (Exception e) {
            str_uuid = "";
            e.printStackTrace();
        }
        return str_uuid;
    }

    public int update_student(Student student) {
        //long id = 0;
        int cnt_rows = 0;
        String str_uuid = "";
        SQLiteDatabase db = getWritableDatabase();

        try {

            str_uuid = getGroupId(student.GroupName);
            if (str_uuid == "") {
                //Log.d("update_student", "getGroupId " + id);
                // такой группы еще нет
                str_uuid = insertGroup(student.GroupName,"");
            }
            // Такая группа уже существует

            ContentValues values = new ContentValues();
            values.put(COLUMN_FIRSTNAME_STUDENTS, student.FirstName);
            values.put(COLUMN_LASTNAME_STUDENTS, student.LastName);
            values.put(COLUMN_AGE_STUDENTS, student.Age);
            values.put(COLUMN_GROUPID_STUDENTS, str_uuid);

            //Log.d("update_student", "student.id " + student.id);

            // updating row
            cnt_rows = db.update(TABLE_NAME_STUDENTS, values, COLUMN_ID_STUDENTS + " = '" + student.id + "'", null);
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
/*
        String[] arr_column = {
                COLUMN_ID_STUDENTS,
                COLUMN_NAME_GROUPS,
                COLUMN_FIRSTNAME_STUDENTS,
                COLUMN_LASTNAME_STUDENTS,
                COLUMN_AGE_STUDENTS
        };
*/
        try {
            //cursor = db.query(TABLE_NAME_STUDENTS, arr_column, null, null, null, null, null, null);
            String strSQL = "Select t1." + COLUMN_ID_STUDENTS + ", t1." + COLUMN_GROUPID_STUDENTS + ", t2." + COLUMN_NAME_GROUPS + ", t1." + COLUMN_FIRSTNAME_STUDENTS + ", t1." + COLUMN_LASTNAME_STUDENTS + ", t1." + COLUMN_AGE_STUDENTS
                    + " from " + TABLE_NAME_STUDENTS + " t1  left join " + TABLE_NAME_GROUPS + " t2 on t1." + COLUMN_GROUPID_STUDENTS + " = t2." + COLUMN_ID_GROUPS;

            cursor = db.rawQuery(strSQL, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Student student = new Student();
                    student.id =
                            cursor.getString(cursor.getColumnIndex(COLUMN_ID_STUDENTS));
                    student.GroupId =
                            cursor.getString(cursor.getColumnIndex(COLUMN_GROUPID_STUDENTS));
                    student.GroupName =
                            cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GROUPS));
                    student.FirstName =
                            cursor.getString(cursor.getColumnIndex(COLUMN_FIRSTNAME_STUDENTS));
                    student.LastName =
                            cursor.getString(cursor.getColumnIndex(COLUMN_LASTNAME_STUDENTS));
                    student.Age =
                            cursor.getInt(cursor.getColumnIndex(COLUMN_AGE_STUDENTS));
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
/*
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

        return MainActivity.mstudentsCursor;
    }
*/
    public String insertGroup(String group, String str_uuid) {
        long id = 0;
        SQLiteDatabase db = getWritableDatabase();

        if (str_uuid.equals("")) {
            str_uuid = UUID.randomUUID().toString();
        }
        //String str_uuid = UUID.randomUUID().toString();

        try {
            ContentValues values = new ContentValues();

            values.put(COLUMN_NAME_GROUPS, group);
            values.put(COLUMN_ID_GROUPS, str_uuid);

            id = db.insert(TABLE_NAME_GROUPS, null, values);
        } catch (Exception e) {
            str_uuid = "";
            e.printStackTrace();
        }
        return str_uuid;
    }

    public String getGroupId(String group) {
        //long id = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        String str_uuid = "";

        try {
            String strSQL = "Select " + COLUMN_ID_GROUPS
                    + " from " + TABLE_NAME_GROUPS + " where " + COLUMN_NAME_GROUPS + " = '" + group + "'";

            //Log.d("getGroupId", "strSQL " + strSQL);

            cursor = db.rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                str_uuid = cursor.getString(cursor.getColumnIndex(COLUMN_ID_GROUPS));
                //Log.d("getGroupId", "id " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return str_uuid;
    }

    public ArrayList<Group> getGroups() {
        //long id = 0;
        ArrayList<Group> groups = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        //String str_uuid = "";

        try {
            String strSQL = "Select " + COLUMN_ID_GROUPS + ", "
                    + COLUMN_NAME_GROUPS
                    + " from " + TABLE_NAME_GROUPS;

            //Log.d("getGroupId", "strSQL " + strSQL);

            cursor = db.rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Group group = new Group();
                    group.groupId = cursor.getString(cursor.getColumnIndex(COLUMN_ID_GROUPS));
                    group.groupName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GROUPS));
                    groups.add(group);
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
        return groups;
    }

    public int deleteStudent(String str_uuid) {
        int count = 0;
        SQLiteDatabase db = getWritableDatabase();

        try {
            count = db.delete(TABLE_NAME_STUDENTS, COLUMN_ID_STUDENTS + " = '" + str_uuid + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}
