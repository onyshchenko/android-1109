package com.example.onyshchenkov.homework_lesson10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Activity3 extends AppCompatActivity {

    private Student student;
    private String group_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        Intent intent = getIntent();
        student =  intent.getParcelableExtra(MainActivity.EXTRA_STUDENT);
        group_num = intent.getStringExtra(MainActivity.EXTRA_GROUP);

        EditText editText_firstname = findViewById(R.id.editText_firstname);
        EditText editText_lastname = findViewById(R.id.editText_lastname);
        EditText editText_age = findViewById(R.id.editText_age);
        EditText editText_group = findViewById(R.id.editText_group);

        Log.d("onCreate", "update_student: student.id = " + student.id);
        editText_firstname.setText(student.FirstName);
        editText_lastname.setText(student.LastName);
        editText_age.setText( student.Age > 0 ? String.format("%d", student.Age) : null );
        editText_group.setText( group_num);


    }

    public void clck(View v) {
        switch (v.getId()){
            case R.id.button_save: {
                EditText editText_firstname = findViewById(R.id.editText_firstname);
                EditText editText_lastname = findViewById(R.id.editText_lastname);
                EditText editText_age = findViewById(R.id.editText_age);
                EditText editText_group = findViewById(R.id.editText_group);

                student.FirstName = editText_firstname.getText().toString();
                student.LastName = editText_lastname.getText().toString();
                student.Age = Integer.parseInt(editText_age.getText().toString());
                group_num = editText_group.getText().toString();

                Intent intent = new Intent();
                intent.putExtra(MainActivity.EXTRA_STUDENT, student);
                intent.putExtra(MainActivity.EXTRA_GROUP, group_num);
                Log.d("clck", "update_student: student.id = " + student.id);

                //intent.putExtra(MainActivity.EXTRA_GROUP, Integer.valueOf(((EditText)findViewById(R.id.editText_group)).getText().toString()));

                setResult(RESULT_OK, intent);
                finish();
            }
            break;
        }
    }
}
