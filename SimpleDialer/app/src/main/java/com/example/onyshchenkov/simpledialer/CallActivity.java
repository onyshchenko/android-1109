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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.util.Observable;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        ImageView buttonAnswer = findViewById(R.id.buttonAnswer);
        ImageView buttonHangup = findViewById(R.id.buttonHangup);

        mtextDisplayName = findViewById(R.id.textDisplayName);
        mtextDuration = findViewById(R.id.textDuration);
        mtextStatus = findViewById(R.id.textStatus);

        //textStatus

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
                //Log.d("CallActivity", "onClick_green");

                CallManager.INSTANCE.acceptCall();
            }
        });

        buttonHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("CallActivity", "onClick_red");
                CallManager.INSTANCE.cancelCall();
            }
        });
    }

   public void updateView (Call call){ //(gsmCall gsmCall){
       //Log.d("CallActivity", "Call onStateChanged: " + call);

       String phoneNumber = call.getDetails().getHandle().toString().substring(4);

       switch (call.getState()){
           case STATE_ACTIVE:
               mtextStatus.setGravity(View.GONE);
               mtextStatus.setText("");
               break;
           case STATE_CONNECTING:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("Connecting…");
               break;
           case STATE_DIALING:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("Calling…");
               break;
           case STATE_RINGING:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("Incoming call");
               break;
           case STATE_DISCONNECTED:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("Finished call");
               finish();
               break;
           case STATE_DISCONNECTING:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("Finished call");
               break;
           case STATE_HOLDING:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("");
               break;
           case STATE_NEW:
               mtextStatus.setGravity(View.VISIBLE);
               mtextStatus.setText("");
               break;
       }





 //      finish();

   }





    @Override
    protected void onResume() {
        super.onResume();
        //CallManager.INSTANCE.u
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //private Long.toDurationString() = String.format("%02d:%02d:%02d", this / 3600, (this % 3600) / 60, (this % 60));

}
