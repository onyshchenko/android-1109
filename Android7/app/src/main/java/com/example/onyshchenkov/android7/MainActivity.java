package com.example.onyshchenkov.android7;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClck(View v) {

        SharedPreferences preferences = null;
        switch (v.getId()) {
            case R.id.button: {
                preferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("Test", "Hello preferences");
                editor.commit();

                //long id = helper.insert_student(new Student("Ivan","Ivanov",22));
                //Toast.makeText(this, String.valueOf(id),Toast.LENGTH_SHORT).show();
                //startActivity( new Intent(this, Activity2.class));
            }
            break;
            case R.id.button2: {
                preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("Test", "Hello shared preferences");
                editor.commit();

                //startActivity( new Intent(this, Activity3.class));


            }
            break;
            case R.id.button3: {
                preferences = getPreferences(MODE_PRIVATE);
                Toast.makeText(this, preferences.getString("Test", ""), Toast.LENGTH_LONG).show();
                //startActivity( new Intent(this, Activity4.class));
            }
            break;
            case R.id.button4: {
                /*
                preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                Toast.makeText(this, preferences.getString("Test",""), Toast.LENGTH_LONG).show();
                */
                startActivity(new Intent(this, SettingsActivity.class));
                //startActivity( new Intent(this, Activity5.class));
            }
            break;
            case R.id.button5:
                preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String pref = preferences.getString("edit_text_preference_1", "");
                Toast.makeText(this, pref, Toast.LENGTH_LONG).show();
                break;
            case R.id.button6:
                saveInternalFile("MyFile.txt", "File data");
                break;
            case R.id.button7:
                Toast.makeText(this, readInternalFile("MyFile.txt"), Toast.LENGTH_LONG).show();
                break;
            case R.id.button8:
                if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder");
                        saveExternalFile(folder,"MyFile.txt","File data");
                    }
                } else {
                    requestPermission();
                }
                break;
            case R.id.button9:
                if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder");
                        Toast.makeText(this,readExternalFile(folder, "MyFile.txt"), Toast.LENGTH_LONG).show();
                    }
                } else {
                    requestPermission();
                }
                break;
            case R.id.button10:
                Student student = new Student ("Ivan", "Ivanov", 22);

                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(student);

                saveInternalFile("Student.json", json);

                String json2 = readInternalFile("Student.json");
                Student student2 = gson.fromJson(json2, Student.class);
                Toast.makeText(this, student2.firstName, Toast.LENGTH_LONG).show();

                break;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            requestPermissions(permissions, 0);
        }
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
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

    private void saveInternalFile(String fileName, String data) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(fileName, MODE_PRIVATE)));

            writer.write(data);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readInternalFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();

            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
