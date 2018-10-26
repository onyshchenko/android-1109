package com.example.onyshchenkov.android10;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void CLC(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (v.getId()){
            case R.id.button:
                /*
                Fragment fragment1 = new BlankFragment();
                transaction.replace(R.id.fragmentView, fragment1);
                */

                Fragment fragment1 = BlankFragment.newInstance("Hello ftagment 1");
                transaction.replace(R.id.fragmentView, fragment1);

                break;
            case R.id.button2:
                Fragment fragment2 = BlankFragment2.newInstance("Hello ftagment 2");
                transaction.replace(R.id.fragmentView, fragment2);
                break;
        }

        transaction.commit();
    }
}
