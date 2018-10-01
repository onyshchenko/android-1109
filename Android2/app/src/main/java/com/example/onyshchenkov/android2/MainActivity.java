package com.example.onyshchenkov.android2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com1";
    public static final String EXTRA_STUDENT = "com2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClck(View v) {
        switch (v.getId()){
            case R.id.button: {
                Intent intent = new Intent(this, Activity2.class);
                intent.putExtra(EXTRA_TEXT,"Hello activity!");
                intent.putExtra("int1", 33);
                startActivity(intent);
            }
                break;
            case R.id.button2:
            {
                Intent intent = new Intent(this, Activity2.class);
                intent.putExtra(EXTRA_TEXT,"Hello activity!");
                intent.putExtra("int1", 33);
                startActivity(intent);
            }
                break;
            case R.id.button3: {
                Intent intent = new Intent(this, Activity3.class);
                intent.putExtra(EXTRA_STUDENT, new Student("Ivan", "Ivanov", 22));
                startActivity(intent);
            }
                break;
            case R.id.button4:{
                //startActivity(new Intent(this, Activity4.class));
                startActivityForResult(new Intent(this, Activity4.class),1);
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
