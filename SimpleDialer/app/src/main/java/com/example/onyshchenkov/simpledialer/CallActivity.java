package com.example.onyshchenkov.simpledialer;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
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
import java.util.ArrayList;
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
import static android.telecom.Call.STATE_SELECT_PHONE_ACCOUNT;


public class CallActivity extends AppCompatActivity {

    private TextView mtextDisplayName;
    private TextView mtextDuration;
    private TextView mtextStatus;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    private long mStartCallTime;
    private String mPhoneNumber;
    private ArrayList<SelectPA> mSelectPA;
    private ArrayList<Call> mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //запретип поворот экрана, при повороте телефона
        setContentView(R.layout.activity_call);

        Log.d("CallActivity", "onCreate");
        mCall = new ArrayList<Call>();

        Intent intent = getIntent();
        int status = intent.getIntExtra("Status", 0);
        mPhoneNumber = intent.getStringExtra("PhoneNumber");
        mSelectPA = intent.getParcelableArrayListExtra("SelectPA");

        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY | WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);


        //int sdkInt = Build.VERSION.SDK_INT;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            //Log.d("checkDefaultDialer", "Build.VERSION.SDK_INT " + Build.VERSION.SDK_INT + " Build.VERSION_CODES.M " + Build.VERSION_CODES.M);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);//Window flag: as long as this window is visible to the user, keep the device's screen turned on and bright.
        } else {
            setTurnScreenOn(true);
        }

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Window flag: as long as this window is visible to the user, keep the device's screen turned on and bright.

        //this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        CallManager.INSTANCE.setCustomObjectListener(new CallManager.MyCustomObjectListener() {
            @Override
            public void onCallAdded(String title, Call call) {
                //Log.d("CallActivity", "onCallAdded " + title + " Call STATE: " + call.getState());
                updateView(call);
            }

            @Override
            public void onCallRemoved(String title, Call call) {
                //Log.d("CallActivity", "onCallRemoved " + title + " Call STATE: " + call.getState());
                updateView(call);
            }

            @Override
            public void onUpdateCall(String title, Call call) {
                //Log.d("CallActivity", "oncallCallback " + title + " Call STATE: " + call.getState());
                updateView(call);
            }
        });

        ImageView buttonAnswer = findViewById(R.id.buttonAnswer);
        ImageView buttonHangup = findViewById(R.id.buttonHangup);

        mtextDisplayName = findViewById(R.id.textDisplayName);
        mtextDuration = findViewById(R.id.textDuration);
        mtextStatus = findViewById(R.id.textStatus);

        mtextDisplayName.setText(searchincontacts(mPhoneNumber));
        mtextDuration.setVisibility(View.INVISIBLE);
        //mtextStatus.setVisibility(View.VISIBLE);
        //mtextStatus.setText("Incoming call");

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();

        buttonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.buttonAnswer).setEnabled(false);

                Log.d("CallActivity", "onClick_green");

                CallManager.INSTANCE.acceptCall();
            }
        });

        buttonHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.buttonHangup).setEnabled(false);
                findViewById(R.id.buttonAnswer).setEnabled(false);
                Log.d("CallActivity", "onClick_red");
                //mTimer.cancel();
                CallManager.INSTANCE.cancelCall();
            }
        });

        if (status == STATE_SELECT_PHONE_ACCOUNT) {

            final PhoneAccountFragment fragment = PhoneAccountFragment.newInstance(mSelectPA, mPhoneNumber);
            fragment.setActionListener(new PhoneAccountFragment.ActionListener() {

                @Override
                public void save(int position) {
                    fragment.dismiss();
                    CallManager.INSTANCE.acceptPhoneAccount(mSelectPA.get(position));
                }

                @Override
                public void cancel() {
                    //getSupportLoaderManager().initLoader(0, null, MainActivity.this);
                    fragment.dismiss();
                    CallManager.INSTANCE.cancelCall();
                }
            });

            fragment.show(getSupportFragmentManager(), "dialog");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);
        int status = intent.getIntExtra("Status", 0);
        mPhoneNumber = intent.getStringExtra("PhoneNumber");
        mSelectPA = intent.getParcelableArrayListExtra("SelectPA");

        CallManager.INSTANCE.getCurStatus();

    }

    private String searchincontacts(String number) {

        String DISPLAY_NAME = mPhoneNumber;

        Cursor contacts = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Loop through the contacts
        while (contacts.moveToNext()) {
            // Get the current contact name
            String name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));

            // Get the current contact phone number
            String phoneNumber = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\s+", "");

            if (mPhoneNumber.contains(phoneNumber)) {
                contacts.moveToLast();
                DISPLAY_NAME = name;
            }
        }
        contacts.close();
/*
        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToNext();

                DISPLAY_NAME = cur.getString(cur.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            }
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
*/
        return DISPLAY_NAME;
    }

    public void updateView(Call call) {
        Log.d("CallActivity", "Call onStateChanged: " + call.getState());


        switch (call.getState()) {
            case STATE_ACTIVE: {
                mtextStatus.setGravity(View.GONE);
                mtextStatus.setText("");
                mtextDuration.setVisibility(View.VISIBLE);
                mStartCallTime = (new Date()).getTime() / 1000;

                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer.purge();
                    mTimer = null;
                    mTimer = new Timer();
                    mTimer.schedule(mMyTimerTask, 1000, 1000);
                }

                /*
                try {

                } catch (Exception e){
                    e.printStackTrace();
                }
                */
            }
                break;
            case STATE_CONNECTING:
                mtextStatus.setGravity(View.VISIBLE);
                mtextStatus.setText("Connecting…");
                mtextDuration.setVisibility(View.INVISIBLE);
                break;
            case STATE_DIALING: {
                int hashCode = call.getDetails().hashCode();
                boolean compare_hash = false;

                for (int i = 0; i < mCall.size(); i++) {
                    if (mCall.get(i).getDetails().hashCode() == hashCode) {
                        compare_hash = true;
                    }
                }
                if (!compare_hash) {
                    mCall.add(call);
                }

                mtextStatus.setGravity(View.VISIBLE);
                mtextStatus.setText("Calling…");
                mtextDuration.setVisibility(View.INVISIBLE);
            }
            break;
            case STATE_RINGING: {
                int hashCode = call.getDetails().hashCode();
                boolean compare_hash = false;

                for (int i = 0; i < mCall.size(); i++) {
                    if (mCall.get(i).getDetails().hashCode() == hashCode) {
                        compare_hash = true;
                    }
                }
                if (!compare_hash) {
                    mCall.add(call);
                }

                mtextStatus.setGravity(View.VISIBLE);
                mtextStatus.setText("Incoming call");
                mtextDuration.setVisibility(View.INVISIBLE);
            }
                break;
            case STATE_DISCONNECTED: {
                int hashCode = call.getDetails().hashCode();

                for (int i = 0; i < mCall.size(); i++) {
                    if (mCall.get(i).getDetails().hashCode() == hashCode) {
                        mCall.remove(i);
                    }
                }

                mtextStatus.setGravity(View.VISIBLE);
                mtextStatus.setText("Finished call");
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer.purge();
                    mTimer = null;
                }
                //mtextDuration.setVisibility(View.INVISIBLE);
                finish();
            }
                break;
            case STATE_DISCONNECTING:
                mtextStatus.setGravity(View.VISIBLE);
                mtextStatus.setText("Finished call");
                mtextDuration.setVisibility(View.INVISIBLE);
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer.purge();
                    mTimer = null;
                }
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
            case STATE_SELECT_PHONE_ACCOUNT:
/*
               Intent intent = new Intent();
               intent.setComponent(new ComponentName("com.android.server.telecom","com.android.server.telecom.settings.EnableAccountPreferenceActivity"));
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
*/
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        CallManager.INSTANCE.getCurStatus();
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
