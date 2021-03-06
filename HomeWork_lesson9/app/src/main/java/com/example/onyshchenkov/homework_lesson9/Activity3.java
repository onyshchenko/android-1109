package com.example.onyshchenkov.homework_lesson9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Activity3 extends AppCompatActivity {

    public Student student;
    public int group_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        Intent intent = getIntent();
        student =  intent.getParcelableExtra(MainActivity.EXTRA_STUDENT);
        group_num = intent.getIntExtra(MainActivity.EXTRA_GROUP, 0);

        EditText editText_firstname = findViewById(R.id.editText_firstname);
        EditText editText_lastname = findViewById(R.id.editText_lastname);
        EditText editText_age = findViewById(R.id.editText_age);
        EditText editText_group = findViewById(R.id.editText_group);

        //editText_firstname.setText( student.firstName != null ? String.format("%s", student.firstName) : String.format("%s", "") );
        //editText_lastname.setText( student.lastName != null ? String.format("%s", student.lastName) : String.format("%s", "") );
        editText_firstname.setText(student.firstName);
        editText_lastname.setText(student.lastName);
        editText_age.setText( student.age > 0 ? String.format("%d", student.age) : null );
        editText_group.setText( group_num > 0 ? String.format("%d", group_num) : null );
    }

    public void clck(View v) {
        switch (v.getId()){
            case R.id.button_save: {
                EditText editText_firstname = findViewById(R.id.editText_firstname);
                EditText editText_lastname = findViewById(R.id.editText_lastname);
                EditText editText_age = findViewById(R.id.editText_age);
                EditText editText_group = findViewById(R.id.editText_group);

                student.firstName = editText_firstname.getText().toString();
                student.lastName = editText_lastname.getText().toString();
                student.age = Integer.parseInt(editText_age.getText().toString());
                group_num = Integer.valueOf(editText_group.getText().toString());

                Intent intent = new Intent();
                intent.putExtra(MainActivity.EXTRA_STUDENT, student);
                intent.putExtra(MainActivity.EXTRA_GROUP, group_num);
                //intent.putExtra(MainActivity.EXTRA_GROUP, Integer.valueOf(((EditText)findViewById(R.id.editText_group)).getText().toString()));

                setResult(RESULT_OK, intent);
                finish();
            }
            break;
        }
    }
}
