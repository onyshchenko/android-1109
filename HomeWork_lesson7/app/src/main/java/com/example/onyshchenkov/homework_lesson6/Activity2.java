package com.example.onyshchenkov.homework_lesson6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity2 extends AppCompatActivity {
    public Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Intent intent = getIntent();
        student =  intent.getParcelableExtra(MainActivity.EXTRA_STUDENT);

        TextView textview_firsname = findViewById(R.id.textview_firsname);
        TextView textview_lastname = findViewById(R.id.textview_lastname);
        TextView textview_age = findViewById(R.id.textview_age);

        textview_firsname.setText( student.firstName != null ? String.format("%s", student.firstName) : String.format("%s", "") );
        textview_lastname.setText( student.lastName != null ? String.format("%s", student.lastName) : String.format("%s", "") );
        textview_age.setText( student.age > 0 ? String.format("%d", student.age) : null );
    }

    @Override
    protected void onStop() {
        Toast toast2 = Toast.makeText(this,
                "Activity closed",
                Toast.LENGTH_SHORT);
//Получить View из toast
        LinearLayout toastContainer =
                (LinearLayout) toast2.getView();
//Создать ImageView с картинкой
        ImageView catImageView = new ImageView(this);
        catImageView.setImageResource(R.mipmap.ic_launcher);
//Добавить картинку в toast
        toastContainer.addView(catImageView, 0);
        toast2.show();

        super.onStop();
    }

}
