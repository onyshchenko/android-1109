package com.example.onyshchenkov.android11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RequiredEditText mEditText1,mEditText2,mEditText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText1 = findViewById(R.id.requiredEditText1);
        mEditText2 = findViewById(R.id.requiredEditText2);;
        mEditText3 = findViewById(R.id.requiredEditText3);;

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText1.validate() && mEditText2.validate() && mEditText3.validate()){
                    Toast.makeText(MainActivity.this,"Validated", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,"ERROR!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
