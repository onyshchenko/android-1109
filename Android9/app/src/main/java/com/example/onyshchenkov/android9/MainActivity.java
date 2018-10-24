package com.example.onyshchenkov.android9;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private SaveTask mSaveTask;
    private SaveManyTask mSaveManyTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }

        if(mSaveManyTask != null){
            mSaveManyTask.cancel(true);
        }
    }

    public void OnClck(View v) {

        //SharedPreferences preferences = null;
        switch (v.getId()) {
            case R.id.button: {
                mSaveTask = new SaveTask();
                mSaveTask.execute(new Student("Ivan", "Ivanov", 22));
            }
            break;
            case R.id.button2: {

                mSaveManyTask = new SaveManyTask();
                mSaveManyTask.execute(
                        new Student("Ivan", "Ivanov", 22),
                        new Student("Ivan", "Ivanov", 22),
                        new Student("Ivan", "Ivanov", 22),
                        new Student("Ivan", "Ivanov", 22),
                        new Student("Ivan", "Ivanov", 22),
                        new Student("Ivan", "Ivanov", 22)
                        );


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String text = data.getStringExtra("Result");
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            }
        }
    }

    public class SaveTask extends AsyncTask<Student, Void, String> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(Student... students) {
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Student student = students[0];
            DataBaseHelper helper = new DataBaseHelper((MainActivity.this));
            helper.insert_student(student, "", "");
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            //super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, aVoid, Toast.LENGTH_SHORT).show();

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    public class SaveManyTask extends AsyncTask<Student, Integer, Void> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Student... students) {
            DataBaseHelper helper = new DataBaseHelper((MainActivity.this));
            int count = 0;

            for (Student student : students) {
                helper.insert_student(student, "", "");

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                count++;

                publishProgress(count, students.length);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            String str = String.format("Saved %d from %d students", values[0], values[1]);
            mDialog.setMessage(str);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mDialog.hide();
        }
    }

}

