package com.example.onyshchenkov.android8;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String text = intent.getStringExtra("Text");
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

        PendingIntent pendingIntent = intent.getParcelableExtra("Pending");

        Intent result = new Intent();

        result.putExtra("Result", doSomething() );

        try {
            pendingIntent.send(this, Activity.RESULT_OK, result);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public String doSomething(){
        return "Result text";
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    public class MyBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }
    }
}
