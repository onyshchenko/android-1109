package com.example.onyshchenkov.homework_lesson6;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.onyshchenkov.homework_lesson6.MainActivity.EXTRA_STUDENT;

public class Activity3 extends AppCompatActivity {

    public Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        Intent intent = getIntent();
        student =  intent.getParcelableExtra(MainActivity.EXTRA_STUDENT);

        EditText editText_firstname = findViewById(R.id.editText_firstname);
        EditText editText_lastname = findViewById(R.id.editText_lastname);
        EditText editText_age = findViewById(R.id.editText_age);

        editText_firstname.setText( student.firstName != null ? String.format("%s", student.firstName) : String.format("%s", "") );
        editText_lastname.setText( student.lastName != null ? String.format("%s", student.lastName) : String.format("%s", "") );
        editText_age.setText( student.age > 0 ? String.format("%d", student.age) : null );
    }


    public void clck(View v) {
        switch (v.getId()){
            case R.id.button_save: {
                EditText editText_firstname = findViewById(R.id.editText_firstname);
                EditText editText_lastname = findViewById(R.id.editText_lastname);
                EditText editText_age = findViewById(R.id.editText_age);

                student.firstName = editText_firstname.getText().toString();
                student.lastName = editText_lastname.getText().toString();
                student.age = Integer.parseInt(editText_age.getText().toString());
                //student.age = 15;

                Intent intent = new Intent();
                intent.putExtra(MainActivity.EXTRA_STUDENT, student);

                setResult(RESULT_OK, intent);
                finish();


                //NotificationManager отвечает за вывод уведомлений
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                //Уведомление
                Notification notification = new NotificationCompat.Builder(this)
                        .setAutoCancel(true) //Скрывать по клику
                        .setSmallIcon(R.mipmap.ic_launcher) //Картинка в строке состояния
                        .setDefaults(Notification.DEFAULT_ALL) //Стандартные звуки и т.д.
                        .setTicker("Notification (activity is closing)!") //Текст в строке состояния
                        .setContentTitle("Activity closed") //Заголовок уведомления
                        .setContentText("Activity closed") //Текст уведомления
                        //.setShowWhen(true)
                        .setWhen(System.currentTimeMillis()) //Время уведомления
                        //.setContentIntent(pendingIntent) //Клик на уведомление
                        .build();
//Запуск уведомления
                notificationManager.notify(1, notification);

            }
            break;
        }
    }
}
