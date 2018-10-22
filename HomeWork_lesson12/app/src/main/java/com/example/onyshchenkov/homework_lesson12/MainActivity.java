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

    public final String BACKUP_FOLDER = "MyFolder";
    public final String BACKUP_FILE_STUDENTS = "Students.txt";
    public final String BACKUP_FILE_GROUPS = "Groups.txt";

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

        mStudents = new ArrayList<>();

        mAdapter = new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mStudents
                );

        mlistView.setAdapter(mAdapter);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemClick", "position= " + position + " mstudentsCursor.getLong: ");
                mPosition = position;
            }
        });

        MyIntentService.getStudents(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:
                // Экспорт
                if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    exportToFile();
                } else {
                    requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
                }

                break;
            case R.id.menu2:
                // Импорт
                if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    importToDB();
                } else {
                    requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
                }
                break;
            case R.id.menu3:
                // Настройки
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED;
        }

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true; //super.onCreateOptionsMenu(menu);
    }

    public void OnClck(View v) {
        //mposition = -1;
        switch (v.getId()) {
            case R.id.button: {
                // ADD
                startActivityForResult(new Intent(this, Activity2.class), 1);
            }
            break;
            case R.id.button2: {
                // EDIT
                if (mPosition != -1) {
                    if (mPosition >= 0 && mStudents.size() > mPosition) {
                        Intent intent = new Intent(this, Activity3.class);
                        Student student = new Student();

                        student.id = mStudents.get(mPosition).id;
                        student.FirstName = mStudents.get(mPosition).FirstName;
                        student.LastName = mStudents.get(mPosition).LastName;
                        student.Age = mStudents.get(mPosition).Age;
                        student.GroupName = mStudents.get(mPosition).GroupName;
                        student.GroupId = mStudents.get(mPosition).GroupId;

                        intent.putExtra(EXTRA_STUDENT, student);
                        intent.putExtra(EXTRA_GROUP, student.GroupName);
                        startActivityForResult(intent, 2);
                    }
                }
            }
            break;
            case R.id.button3: {
                // DELETE
                if (mPosition != -1) {

                    if (mPosition >= 0 && mStudents.size() > mPosition) {
                        //Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_LONG).show();

                        MyIntentService.deleteStudent(this, mStudents.get(mPosition).id);

                    }
                }
                //Log.d("OnClck", "mGroups.size= " +  mGroups.size());
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) { //ADD
            if (resultCode == RESULT_OK) {
                //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

                if (data.getExtras() != null) {
                    Student student = data.getParcelableExtra(EXTRA_STUDENT);
                    student.GroupName = data.getStringExtra(EXTRA_GROUP);

                    MyIntentService.saveStudent(this, student);

                }
            }
        } else if (requestCode == 2) { //EDIT
            if (resultCode == RESULT_OK) {
                //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

                if (data.getExtras() != null) {
                    Student student = data.getParcelableExtra(EXTRA_STUDENT);
                    student.GroupName = data.getStringExtra(EXTRA_GROUP);
                    MyIntentService.updateStudent(this, student);

                    //Log.d("onActivityResult", "update_student: student.id = " + student.id);

                    //mStudents.set(mPosition, student);
                    //mAdapter.notifyDataSetChanged();

                }
            }
        } else if (requestCode == MyIntentService.REQUEST_CODE_GET_STUDENTS){
            if (resultCode == RESULT_OK) {
                mStudents.clear();

                ArrayList<Student> students = data.getParcelableArrayListExtra(MyIntentService.EXTRA_STUDENTS);
                mStudents.addAll(students);
                //mlistView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();


            }
        } else if (requestCode == MyIntentService.REQUEST_CODE_SAVE_STUDENT){
            if (resultCode == RESULT_OK) {
                Student student = data.getParcelableExtra(MyIntentService.EXTRA_STUDENT);
                mStudents.add(student);
                mAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == MyIntentService.REQUEST_CODE_DELETE_STUDENT){
            if (resultCode == RESULT_OK) {
                if (mPosition != -1 && data.getIntExtra(MyIntentService.EXTRA_STUDENT,0) > 0) {
                    mStudents.remove(mPosition);
                    mAdapter.notifyDataSetChanged();
                    mPosition = -1;
                }

            }
        } else if (requestCode == MyIntentService.REQUEST_CODE_UPDATE_STUDENT){
            if (resultCode == RESULT_OK) {
                if (mPosition != -1 ) {
                    Student student = data.getParcelableExtra(MyIntentService.EXTRA_STUDENT);

                    mStudents.set(mPosition, student);

                    mAdapter.notifyDataSetChanged();
                    mPosition = -1;
                }

            }
        }

    }

    private void exportToFile(){
        Log.d("exportToFile", "      ***********************");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + BACKUP_FOLDER);
/*
            ArrayList<Student> students = mHelper.getStudents();
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(students);

            Log.d("json", json);

            saveExternalFile(folder, BACKUP_FILE_STUDENTS, json);

            ArrayList<Group> groups = mHelper.getGroups();
            json = gson.toJson(groups);
            Log.d("json", json);

            saveExternalFile(folder, BACKUP_FILE_GROUPS, json);
            */
        }
        else {
            Toast.makeText(this, "External flash memory didn't mounted", Toast.LENGTH_LONG).show();
        }
    }

    private void importToDB(){
        Log.d("importToDB", "      ***********************");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + BACKUP_FOLDER);
            String json = readExternalFile(folder, BACKUP_FILE_GROUPS);

            ArrayList<Group> groups = new Gson().fromJson(json, new TypeToken<ArrayList<Group>>(){}.getType());
/*
            for (int i=0; i < groups.size(); i++){
                String str_uuid = mHelper.insertGroup(groups.get(i).groupName, groups.get(i).groupId);
                //Log.d("importToDB", "groupName: " + groups.get(i).groupName + " groupId: " + groups.get(i).groupId + " str_uuid: " + str_uuid);
            }

            json = readExternalFile(folder, BACKUP_FILE_STUDENTS);
            ArrayList<Student> students = new Gson().fromJson(json, new TypeToken<ArrayList<Student>>(){}.getType());

            for (int i=0; i < students.size(); i++){
                String str_uuid = mHelper.insert_student(students.get(i), "", students.get(i).GroupId);
                //Log.d("importToDB", "groupName: " + groups.get(i).groupName + " groupId: " + groups.get(i).groupId + " str_uuid: " + str_uuid);
            }
            //mAdapter.notifyDataSetChanged();

/*
            mstudentsCursor = mHelper.getStudentsAndGroups();
            mAdapter.changeCursor(mstudentsCursor);
            mAdapter.notifyDataSetChanged();
  */
        }
        else {
            Toast.makeText(this, "External flash memory didn't mounted", Toast.LENGTH_LONG).show();
        }
    }

    private void requestPermission(String[] str_prmission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /*
            String[] permissions = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            */
            requestPermissions(str_prmission, 0);

            //onRequestPermissionsResult
        }
    }
//adb shell pm reset-permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("PermissionsResult", "requestCode " + requestCode + " permissions.len " + permissions.length + " permissions[0] " + permissions[0]);
        if (requestCode == 0 && grantResults[0] == PERMISSION_GRANTED) {
            if (permissions[0].equals("android.permission.WRITE_EXTERNAL_STORAGE") ) {
                exportToFile();
            }
            if (permissions[0].equals("android.permission.READ_EXTERNAL_STORAGE") ) {
                importToDB();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    private void saveExternalFile(File folder, String fileName, String data) {
        try {
            if (!folder.exists()){
                folder.mkdirs();
            }
            File file = new File(folder, fileName);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(file), "UTF8"));

            writer.write(data);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String readExternalFile(File folder, String fileName) {
        try {
            if(folder.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(new File(folder,fileName)));
                StringBuilder builder = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                reader.close();

                return builder.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
