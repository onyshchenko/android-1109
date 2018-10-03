package com.example.onyshchenkov.android3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClck(View v) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        switch (v.getId()){
            case R.id.button:
                Toast.makeText(this,"Hello toast!", Toast.LENGTH_LONG).show();
            break;
            case R.id.button2: {
                Toast toast = Toast.makeText(this, "Hello toast!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, -300, 0);
                toast.show();
            }
            break;
            case R.id.button3: {
                Toast toast = Toast.makeText(this,"Hello toast!", Toast.LENGTH_LONG);
                LinearLayout layout = (LinearLayout) toast.getView();

                ImageView imageView = new ImageView (this);
                imageView.setImageResource(R.mipmap.ic_launcher);
                layout.addView(imageView,0);

                toast.show();
            }
            break;
            case R.id.button4: {
                View view = getLayoutInflater().inflate(R.layout.toast, null);
                TextView testView = view.findViewById(R.id.textView);

                testView.setText("My toast");

                Toast toast = new Toast(this);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(view);
                toast.show();
            }
            break;
            case R.id.button5:{
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,0);
                Notification notification = new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("Ticker")
                        .setContentTitle("Title")
                        .setContentText("Text")
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .build();

                notificationManager.notify(1, notification);
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action1:
                Toast.makeText(this,"Action 1 click", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action2:
                Toast.makeText(this,"Action 2 click", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action3:
                Toast.makeText(this,"Action 3 click", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }
}
