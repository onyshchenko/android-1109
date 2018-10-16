package com.example.onyshchenkov.android6;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClck(View v) {

        DataBaseHelper helper = new DataBaseHelper((this));


        switch (v.getId()){
            case R.id.button: {

                long id = helper.insert_student(new Student("Ivan","Ivanov",22));
                Toast.makeText(this, String.valueOf(id),Toast.LENGTH_SHORT).show();
                //startActivity( new Intent(this, Activity2.class));
            }
            break;
            case R.id.button2: {
                //startActivity( new Intent(this, Activity3.class));
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("age", 33);
                int count = db.update("students", values, "_id>?", new String[]{"1" });
                Toast.makeText(this, String.valueOf(count),Toast.LENGTH_SHORT).show();

            }
            break;
            case R.id.button3: {
                //startActivity( new Intent(this, Activity4.class));
            }
            break;
            case R.id.button4: {
                //startActivity( new Intent(this, Activity5.class));
            }
            break;
            case R.id.button5:
                break;
            case R.id.button6:
                break;
            case R.id.button7:
                break;
            case R.id.button8:
                break;
            case R.id.button9:
                break;
            case R.id.button10:
                break;
        }
    }
}
