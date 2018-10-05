package com.example.onyshchenkov.homework_lesson6;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.PopupMenu;
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

        //student.firstName = null;
        //student.lastName = null;
        //student.age = null;

        textview_firsname = findViewById(R.id.textview_firsname);
        textview_lastname = findViewById(R.id.textview_lastname);
        textview_age = findViewById(R.id.textview_age);
/*
        textview_firsname.setText(student.firstName != null ? String.format("%s", student.firstName) : String.format("%s", ""));
        textview_lastname.setText(student.lastName != null ? String.format("%s", student.lastName) : String.format("%s", ""));
        textview_age.setText(student.age > 0 ? String.format("%d", student.age) : null);
*/

        textview_firsname.setText(student.firstName);
        textview_lastname.setText(student.lastName);
       /* textview_age.setText(student.age);
  */
    }

/*
    @Override
    protected void onResume() {
        super.onResume();

        textview_firsname.setText(student.firstName != null ? String.format("%s", student.firstName) : String.format("%s", ""));
        textview_lastname.setText(student.lastName != null ? String.format("%s", student.lastName) : String.format("%s", ""));
        textview_age.setText(student.age > 0 ? String.format("%d", student.age) : null);
    }
*/
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
            case R.id.button2:
            {
                PopupMenu popupMenu1 = new PopupMenu(this, v);
                Menu menu = popupMenu1.getMenu();
//Меню
                menu.add(0, 0, 0, "View data");
                menu.add(0, 1, 1, "Edit data");
/*
                //Подменю
                SubMenu subMenu = menu.addSubMenu(0, 1, 1, "Submenu 1");
                subMenu.add(0, 2, 2, "context menu 2");
                */
                popupMenu1.setOnMenuItemClickListener(
                        new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
//Обработчик кликов
                                switch (menuItem.getItemId()){
                                    case 0: {
                                        Intent intent = new Intent(MainActivity.this, Activity2.class);
                                        intent.putExtra(EXTRA_STUDENT, student);
                                        startActivity(intent);
                                        break;
                                    }
                                    case 1: {
                                        Intent intent = new Intent(MainActivity.this, Activity3.class);
                                        intent.putExtra(EXTRA_STUDENT, student);
                                        startActivityForResult(intent, 1);
                                        break;
                                    }

                                }
                                return false;
                            }
                        });
                popupMenu1.show();
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
                Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

                if (data.getExtras() != null) {
                    student = data.getParcelableExtra(EXTRA_STUDENT);

//                    textview_firsname.setText(student.firstName != null ? String.format("%s", student.firstName) : String.format("%s", ""));
//                    textview_lastname.setText(student.lastName != null ? String.format("%s", student.lastName) : String.format("%s", ""));

                    textview_firsname.setText(student.firstName);
                    textview_lastname.setText(student.lastName);

// Если не использовать String.format, то получаем ошибку: android.content.res.Resources$NotFoundException:
//                    textview_age.setText(student.age);
                    textview_age.setText(student.age > 0 ? String.format("%d", student.age) : null);

                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu1:
                Intent intent = new Intent(this, Activity4.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
