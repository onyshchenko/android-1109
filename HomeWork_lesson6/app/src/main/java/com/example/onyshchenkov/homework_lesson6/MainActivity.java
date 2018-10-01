package com.example.onyshchenkov.homework_lesson6;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_STUDENT = "com.example.onyshchenkov.homework_lesson6.student";
    public Student student;

    public TextView textview_firsname;
    public TextView textview_lastname;
    public TextView textview_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        student = new Student();

        //student.firstName = "Ivan";

        textview_firsname = findViewById(R.id.textview_firsname);
        textview_lastname = findViewById(R.id.textview_lastname);
        textview_age = findViewById(R.id.textview_age);

        textview_firsname.setText(student.firstName != null ? String.format("%s", student.firstName) : String.format("%s", ""));
        textview_lastname.setText(student.lastName != null ? String.format("%s", student.lastName) : String.format("%s", ""));
        textview_age.setText(student.age > 0 ? String.format("%d", student.age) : null);
    }


    @Override
    protected void onResume() {
        super.onResume();
        textview_firsname.setText(student.firstName != null ? String.format("%s", student.firstName) : String.format("%s", ""));
        textview_lastname.setText(student.lastName != null ? String.format("%s", student.lastName) : String.format("%s", ""));
        textview_age.setText(student.age > 0 ? String.format("%d", student.age) : null);
    }

    public void clck(View v) {
        switch (v.getId()){
            case R.id.button_view: {
                Intent intent = new Intent(this, Activity2.class);
                intent.putExtra(EXTRA_STUDENT, student);
                startActivity(intent);
            }
            break;
            case R.id.button_edit:
            {
                Intent intent = new Intent(this, Activity3.class);
                intent.putExtra(EXTRA_STUDENT, student);
                startActivityForResult(intent, 1);
            }
            break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

                if (data.getExtras() != null) {
                    student = data.getParcelableExtra(EXTRA_STUDENT);

                    textview_firsname.setText(student.firstName != null ? String.format("%s", student.firstName) : String.format("%s", ""));
                    textview_lastname.setText(student.lastName != null ? String.format("%s", student.lastName) : String.format("%s", ""));
                    textview_age.setText(student.age > 0 ? String.format("%d", student.age) : null);

                }

            }
        }
    }

}
