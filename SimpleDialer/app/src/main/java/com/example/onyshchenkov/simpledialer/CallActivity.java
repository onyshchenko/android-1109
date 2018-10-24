package com.example.onyshchenkov.simpledialer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.telecom.Call.STATE_ACTIVE;
import static android.telecom.Call.STATE_CONNECTING;
import static android.telecom.Call.STATE_DIALING;
import static android.telecom.Call.STATE_DISCONNECTED;
import static android.telecom.Call.STATE_DISCONNECTING;
import static android.telecom.Call.STATE_HOLDING;
import static android.telecom.Call.STATE_NEW;
import static android.telecom.Call.STATE_RINGING;


public class CallActivity extends AppCompatActivity {

    private TextView mtextDisplayName;
    private TextView mtextDuration;
    private TextView mtextStatus;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    private long mStartCallTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        Log.d("CallActivity", "onCreate");
        Intent intent = getIntent();
        String status = intent.getStringExtra("Status");
        String phoneNumber = intent.getStringExtra("PhoneNumber");

        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY | WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        //this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        ImageView buttonAnswer = findViewById(R.id.buttonAnswer);
        ImageView buttonHangup = findViewById(R.id.buttonHangup);

        mtextDisplayName = findViewById(R.id.textDisplayName);
        mtextDuration = findViewById(R.id.textDuration);
        mtextStatus = findViewById(R.id.textStatus);

        mtextDisplayName.setText(phoneNumber);
        mtextDuration.setVisibility(View.INVISIBLE);
        //mtextStatus.setVisibility(View.VISIBLE);
        mtextStatus.setText("Incoming call");



        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();

        CallManager.INSTANCE.setCustomObjectListener(new CallManager.MyCustomObjectListener() {
            @Override
            public void onCallAdded(String title, Call call) {
                //Log.d("CallActivity", "onCallAdded " + title + " Call STATE: " + call.getState());
            }

            @Override
            public void onCallRemoved(String title, Call call) {
                //Log.d("CallActivity", "onCallRemoved " + title + " Call STATE: " + call.getState());
            }

            @Override
            public void onUpdateCall(String title, Call call) {
                //Log.d("CallActivity", "oncallCallback " + title + " Call STATE: " + call.getState());
                updateView(call);
            }
        });

        buttonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CallActivity", "onClick_green");

                CallManager.INSTANCE.acceptCall();
            }
        });

        buttonHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CallActivity", "onClick_red");
                mTimer.cancel();
                CallManager.INSTANCE.cancelCall();
            }
        });
    }

   public void updateView (Call call){
       //Log.d("CallActivity", "Call onStateChanged: " + call);

       String phoneNumber = call.getDetails().getHandle().toString().substring(4);

       switch (call.getState()){
           case STATE_ACTIVE:
               mtextStatus.setGravity(View.GONE);
               mtextStatus.setText("");
               mtextDuration.setVisibility(View.VISIBLE);
               mStartCallTime = (new Date()).getTime() / 1000;
               mTimer.schedule(mMyTimerTask, 1000,1000);
               break;
           case STATE_CONNECTING:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("Connecting…");
               mtextDuration.setVisibility(View.INVISIBLE);
               break;
           case STATE_DIALING:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("Calling…");
               mtextDuration.setVisibility(View.INVISIBLE);
               break;
           case STATE_RINGING:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("Incoming call");
               mtextDuration.setVisibility(View.INVISIBLE);
               break;
           case STATE_DISCONNECTED:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("Finished call");
               mTimer.purge();
               //mtextDuration.setVisibility(View.INVISIBLE);
               finish();
               break;
           case STATE_DISCONNECTING:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("Finished call");
               mtextDuration.setVisibility(View.INVISIBLE);
               break;
           case STATE_HOLDING:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("");
               mtextDuration.setVisibility(View.VISIBLE);
               break;
           case STATE_NEW:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("");
               mtextDuration.setVisibility(View.INVISIBLE);
               break;
       }
   }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //private Long.toDurationString() = String.format("%02d:%02d:%02d", this / 3600, (this % 3600) / 60, (this % 60));

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    long call_duration = ((new Date()).getTime() / 1000) - mStartCallTime;
                    mtextDuration.setText(String.format("%02d:%02d:%02d", call_duration / 3600, (call_duration % 3600) / 60, (call_duration % 60)));
                    //String.format("%02d:%02d:%02d", call_duration / 3600, (call_duration % 3600) / 60, (call_duration % 60));
                }
            });
        }
    }

}
