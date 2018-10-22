package com.example.onyshchenkov.android8;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MyService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClck(View v) {

        //SharedPreferences preferences = null;
        switch (v.getId()) {
            case R.id.button: {
                Intent intent = new Intent(this, MyService.class);

                PendingIntent pendingIntent = createPendingResult(1,intent,0);
                intent.putExtra("Pending", pendingIntent);
                intent.putExtra("Text", "Some Text");
                startService(intent);

                //long id = helper.insert_student(new Student("Ivan","Ivanov",22));
                //Toast.makeText(this, String.valueOf(id),Toast.LENGTH_SHORT).show();
                //startActivity( new Intent(this, Activity2.class));
            }
            break;
            case R.id.button2: {
                if (mService == null){
                    ServiceConnection connection = new ServiceConnection() {
                        @Override
                        public void onServiceConnected(ComponentName name, IBinder service) {
                            mService = ((MyService.MyBinder) service).getService();
                        }

                        @Override
                        public void onServiceDisconnected(ComponentName name) {

                        }
                    };

                    Intent intent = new Intent(this,MyService.class);
                    bindService(intent, connection, BIND_AUTO_CREATE);
                }
                if (mService != null){
                    String text = mService.doSomething();
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode ==1){
            if (resultCode == RESULT_OK){
                String text = data.getStringExtra("Result");
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            }
        }
    }
}
