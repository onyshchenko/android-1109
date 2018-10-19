package com.example.onyshchenkov.homework_lesson11;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Activity2 extends AppCompatActivity {

    //public Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        //Intent intent = getIntent();
        //student =  intent.getParcelableExtra(MainActivity.EXTRA_STUDENT);

        //EditText editText_firstname = findViewById(R.id.editText_firstname);
        //EditText editText_lastname = findViewById(R.id.editText_lastname);
        //EditText editText_age = findViewById(R.id.editText_age);

        //editText_firstname.setText( student.firstName != null ? String.format("%s", student.firstName) : String.format("%s", "") );
        //editText_lastname.setText( student.lastName != null ? String.format("%s", student.lastName) : String.format("%s", "") );

        //editText_firstname.setText("");
        //editText_lastname.setText("");
        //editText_age.setText(student.age > 0 ? String.format("%d", student.age) : null);

    }

    public void clck(View v) {
        switch (v.getId()){
            case R.id.button_save: {
                EditText editText_firstname = findViewById(R.id.editText_firstname);
                EditText editText_lastname = findViewById(R.id.editText_lastname);
                EditText editText_age = findViewById(R.id.editText_age);

                Student student = new Student();

                student.FirstName = editText_firstname.getText().toString();
                student.LastName = editText_lastname.getText().toString();
                student.Age = Integer.parseInt(editText_age.getText().toString());
                //student.age = 15;

                //Log.d("Activity2", "editText_group " + ((EditText)findViewById(R.id.editText_group)).getText().toString());


                Intent intent = new Intent();
                intent.putExtra(MainActivity.EXTRA_STUDENT, student);
                intent.putExtra(MainActivity.EXTRA_GROUP, ((EditText)findViewById(R.id.editText_group)).getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
            break;
        }
    }
}


