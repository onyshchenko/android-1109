package com.example.onyshchenkov.android8;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClck(View v) {

        //SharedPreferences preferences = null;
        switch (v.getId()) {
            case R.id.button: {

                //long id = helper.insert_student(new Student("Ivan","Ivanov",22));
                //Toast.makeText(this, String.valueOf(id),Toast.LENGTH_SHORT).show();
                //startActivity( new Intent(this, Activity2.class));
            }
            break;
            case R.id.button2: {

                //startActivity( new Intent(this, Activity3.class));


            }
            break;
            case R.id.button3: {

                //startActivity( new Intent(this, Activity4.class));
            }
            break;
            case R.id.button4: {

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
