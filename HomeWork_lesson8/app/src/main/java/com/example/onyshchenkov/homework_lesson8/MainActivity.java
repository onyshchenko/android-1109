package com.example.onyshchenkov.homework_lesson8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    public static final String EXTRA_STUDENT = "com.example.onyshchenkov.homework_lesson8.student";
    //public Student mStudent;

    ArrayList<Student> mStudents;
    StudentArrayAdapter madapter;

    int mposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.listview);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);


        mStudents = new ArrayList<>();

        madapter = new StudentArrayAdapter(
                this,
                R.layout.student,
                mStudents
        );
        mListView.setAdapter(madapter);
        //mListView.setItemsCanFocus(true);

        mListView.setOnItemClickListener( new AdapterView.OnItemClickListener () {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemClick", "mposition " + position);
                //Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void OnClck(View v) {
        mposition = -1;
        switch (v.getId()){
            case R.id.button: {
                startActivityForResult( new Intent(this, Activity2.class), 1);
            }
            break;
            case R.id.button2: {
                mposition = mListView.getCheckedItemPosition();
                //Log.d("OnClck", "mposition " + mposition);
                //Toast.makeText(MainActivity.this, String.valueOf(mposition), Toast.LENGTH_LONG).show();
                if (mposition>=0) {

                    Intent intent = new Intent(this, Activity3.class);
                    intent.putExtra(EXTRA_STUDENT, mStudents.get(mposition));
                    startActivityForResult(intent, 2);
                }
            }
            break;
            case R.id.button3: {
                mposition = mListView.getCheckedItemPosition();
                if (mposition>=0) {
                    //Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_LONG).show();
                    mStudents.remove(mposition);
                    madapter.notifyDataSetChanged();
                }
            }
            break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

        if(requestCode == 1){ //ADD
            if(resultCode == RESULT_OK){
                //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

                if (data.getExtras() != null) {
                    Student student = data.getParcelableExtra(EXTRA_STUDENT);
                    mStudents.add(student);
                    madapter.notifyDataSetChanged();


                }
            }
        }
        if(requestCode == 2){ //EDIT
            if(resultCode == RESULT_OK){
                //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

                if (data.getExtras() != null) {
                    Student student = data.getParcelableExtra(EXTRA_STUDENT);
                    mStudents.set(mposition,student);
                    madapter.notifyDataSetChanged();
                }
            }
        }
    }
}
