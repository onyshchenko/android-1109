package com.example.onyshchenkov.homework_lesson6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);
    }
    public void clck(View v) {
        switch (v.getId()) {
            case R.id.button:
                finish();
                break;
        }
    }

}
