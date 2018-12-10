package com.example.onyshchenkov.microcrm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

/*
        https://material.io/tools/icons/?icon=sync&style=sharp
*/
public class CallActivity extends AppCompatActivity {

    private TextView mtextDisplayName;
    //private TextView mtextDuration;
    private TextView mtextStatus_Duration;
    private ImageView mbuttonChange;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    private Call mСurrentCall = null;
    private ArrayList<Work_with_DB> listtasks = null;

    private boolean mPendingShowDialog = false;

    //private long mStartCallTime;
    //private String mPhoneNumber;
    //private ArrayList<SelectPA> mSelectPA;
    //private ArrayList<Call> mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //запретип поворот экрана, при повороте телефона

        Log.d("MicroCRM", "CallActivity. onCreate");

        //mCall = new ArrayList<Call>();

        //Intent intent = getIntent();
        //int status = intent.getIntExtra("Status", 0);
        //mPhoneNumber = intent.getStringExtra("PhoneNumber");
        //mSelectPA = intent.getParcelableArrayListExtra("SelectPA");

        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY |
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //int sdkInt = Build.VERSION.SDK_INT;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            //Log.d("checkDefaultDialer", "Build.VERSION.SDK_INT " + Build.VERSION.SDK_INT + " Build.VERSION_CODES.M " + Build.VERSION_CODES.M);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);//Window flag: as long as this window is visible to the user, keep the device's screen turned on and bright.
        } else {
            setTurnScreenOn(true);
        }

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_call);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Window flag: as long as this window is visible to the user, keep the device's screen turned on and bright.

        //this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        CallManager.INSTANCE.setCustomObjectListener(new CallManager.MyCustomObjectListener() {
            @Override
            public void onCallAdded(Call call) {
                //Log.d("CallActivity", "onCallAdded " + title + " Call STATE: " + call.getState());
                //updateView(call);
            }

            @Override
            public void onCallRemoved(Call call) {
                //Log.d("CallActivity", "onCallRemoved " + title + " Call STATE: " + call.getState());
                //updateView(call);
            }

            @Override
            public void onUpdateCall(Call call) {
                //Log.d("CallActivity", "oncallCallback " + title + " Call STATE: " + call.getState());
                mСurrentCall = call;
                updateView(call);
            }

            @Override
            public void onFinishCallActivity() {
                FinishCallActivity();
            }

            @Override
            public void onShowChangeCallButton() {
                mbuttonChange.setVisibility(View.VISIBLE);
            }

            @Override
            public void onHideChangeCallButton() {
                mbuttonChange.setVisibility(View.INVISIBLE);
            }
        });

        ImageView buttonAnswer = findViewById(R.id.buttonAnswer);
        ImageView buttonHangup = findViewById(R.id.buttonHangup);
        mbuttonChange = findViewById(R.id.buttonChange);
        mbuttonChange.setVisibility(View.GONE);

        mtextDisplayName = findViewById(R.id.textDisplayName);
        //mtextStatus_Duration = findViewById(R.id.textDuration);
        mtextStatus_Duration = findViewById(R.id.textStatus);
        //mtextStatus_Duration.setVisibility(View.INVISIBLE);

/*
        Uri uri = call.getDetails().getHandle();
        String scheme = uri.getScheme();
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        mtextDisplayName.setText(searchincontacts(mPhoneNumber));
*/

        //mtextStatus.setVisibility(View.VISIBLE);
        //mtextStatus.setText("Incoming call");

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 1000, 1000);

        buttonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MicroCRM", "CallActivity. onClick_green");
                //v.setVisibility(View.INVISIBLE);

                CallManager.INSTANCE.acceptCall(mСurrentCall);
            }
        });

        buttonHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MicroCRM", "CallActivity. onClick_red");

                CallManager.INSTANCE.cancelCall(mСurrentCall);
            }
        });


        mbuttonChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("MicroCRM", "CallActivity. OnClick_grey");
                CallManager.INSTANCE.changeCall(mСurrentCall);
            }
        });
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);
        //int status = intent.getIntExtra("Status", 0);
        //mPhoneNumber = intent.getStringExtra("PhoneNumber");
        //mSelectPA = intent.getParcelableArrayListExtra("SelectPA");

        //CallManager.INSTANCE.getCurStatus();
/*
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
*/
        Log.d("MicroCRM", "CallActivity. onNewIntent");
        PowerManager powermanager=  ((PowerManager) getSystemService(Context.POWER_SERVICE));
        powermanager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag").acquire();

    }

    private void show_info(String number) {

        if (listtasks == null){
            listtasks = new ArrayList();
        }

        Work_with_DB task = new Work_with_DB();

        task.execute("show client", number);
        listtasks.add(task);

    }

    private String SearchInContacts(String number) {

        String DISPLAY_NAME = number;

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
            String phoneNumber = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[^0123456789+]", "");//.replaceAll("\\s+", "").replaceAll("\\D+", "");

            if (number.contains(phoneNumber) && phoneNumber.length()+3 >= number.length()) {
                contacts.moveToLast();
                DISPLAY_NAME = name;
            }
        }
        contacts.close();

        return DISPLAY_NAME;
    }

    public void updateView(Call call) {
        Log.d("MicroCRM", "CallActivity. updateView: " + call.getState());

        switch (call.getState()) {
            case STATE_ACTIVE: {
                show_info(call.getDetails().getHandle().getSchemeSpecificPart());
                findViewById(R.id.buttonAnswer).setVisibility(View.INVISIBLE);
                //mtextStatus.setGravity(View.GONE);
                //mtextStatus.setText("");
                //mtextDuration.setVisibility(View.VISIBLE);
            }
                break;
            case STATE_CONNECTING:
                //mtextStatus_Duration.setGravity(View.VISIBLE);
                mtextStatus_Duration.setText("Connecting…");
                findViewById(R.id.buttonAnswer).setVisibility(View.INVISIBLE);
                //mtextDuration.setVisibility(View.INVISIBLE);
                break;
            case STATE_DIALING: {
                //mtextDisplayName.setText(searchincontacts(call.getDetails().getHandle().getSchemeSpecificPart()));
                show_info(call.getDetails().getHandle().getSchemeSpecificPart());
                findViewById(R.id.buttonAnswer).setVisibility(View.INVISIBLE);
                //mtextStatus_Duration.setGravity(View.VISIBLE);
                mtextStatus_Duration.setText("Calling…");
                //mtextDuration.setVisibility(View.INVISIBLE);
            }
            break;
            case STATE_RINGING: {
                //mtextDisplayName.setText(searchincontacts(call.getDetails().getHandle().getSchemeSpecificPart()));
                show_info(call.getDetails().getHandle().getSchemeSpecificPart());
                findViewById(R.id.buttonAnswer).setVisibility(View.VISIBLE);
                //mtextStatus_Duration.setGravity(View.VISIBLE);
                mtextStatus_Duration.setText("Incoming call");
                //mtextDuration.setVisibility(View.INVISIBLE);
            }
                break;
            case STATE_DISCONNECTED: {
                //mtextStatus_Duration.setGravity(View.VISIBLE);
                mtextStatus_Duration.setText("Finished call");
                findViewById(R.id.buttonAnswer).setVisibility(View.INVISIBLE);
                //mtextDuration.setVisibility(View.INVISIBLE);
                CallManager.INSTANCE.getCurStatus(null);
            }
                break;
            case STATE_DISCONNECTING:
                //mtextStatus_Duration.setGravity(View.VISIBLE);
                mtextStatus_Duration.setText("Finishing call");
                findViewById(R.id.buttonAnswer).setVisibility(View.INVISIBLE);
                //mtextDuration.setVisibility(View.INVISIBLE);
                break;
            case STATE_HOLDING:
                //mtextStatus_Duration.setGravity(View.VISIBLE);
                mtextStatus_Duration.setText("");
                //mtextDuration.setVisibility(View.VISIBLE);
                break;
            case STATE_NEW:
                //mtextStatus_Duration.setGravity(View.VISIBLE);
                mtextStatus_Duration.setText("");
                //mtextDuration.setVisibility(View.INVISIBLE);
                break;
            case STATE_SELECT_PHONE_ACCOUNT:
                findViewById(R.id.buttonAnswer).setVisibility(View.VISIBLE);
                if (!mPendingShowDialog) {

                    TelecomManager tm = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);

                    @SuppressLint("MissingPermission") List<PhoneAccountHandle> phoneAccountHandleList = tm.getCallCapablePhoneAccounts();
                    PhoneAccount phoneAccount;
                    final ArrayList<SelectPA> phoneAccounts = new ArrayList<SelectPA>();

                    for (int i = 0; i < phoneAccountHandleList.size(); i++) {
                        phoneAccount = tm.getPhoneAccount(phoneAccountHandleList.get(i));

                        phoneAccounts.add(new SelectPA(i, phoneAccountHandleList.get(i), phoneAccount.getLabel().toString(), phoneAccount.getIcon()));
                    }

                    /* handle */
                    /*mtextDisplayName.setText(searchincontacts(call.getDetails().getHandle().getSchemeSpecificPart()));*/

                    final PhoneAccountFragment fragment = PhoneAccountFragment.newInstance(phoneAccounts, call.getDetails().getHandle().getSchemeSpecificPart());
                    fragment.setActionListener(new PhoneAccountFragment.ActionListener() {

                        @Override
                        public void save(int position) {
                            fragment.dismiss();
                            mPendingShowDialog = false;
                            CallManager.INSTANCE.acceptPhoneAccount(mСurrentCall, phoneAccounts.get(position).handle);
                        }

                        @Override
                        public void cancel() {
                            fragment.dismiss();
                            mPendingShowDialog = false;
                            CallManager.INSTANCE.cancelCall(mСurrentCall);
                        }
                    });

                    fragment.show(getSupportFragmentManager(), "dialog");
                    mPendingShowDialog = true;
                }

                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MicroCRM", "CallActivity. onResume");
        CallManager.INSTANCE.getCurStatus(mСurrentCall);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MicroCRM", "CallActivity. onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MicroCRM", "CallActivity. onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MicroCRM", "CallActivity. onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }

        if (listtasks != null) {
            for (int i = 0; i < listtasks.size(); i++) {
                if (!listtasks.get(i).isCancelled()){
                    listtasks.get(i).cancel(true);
                }
            }
        }

        CallManager.INSTANCE.setCustomObjectListener(null);

        Log.d("MicroCRM", "CallActivity. onDestroy");
    }

    private void FinishCallActivity(){
        finish();
        Log.d("MicroCRM", "CallActivity. FinishCallActivity");
    }


    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (mСurrentCall != null) {
                        if ( mСurrentCall.getState() == STATE_ACTIVE) {

                            long call_duration = (new Date()).getTime();

                            call_duration = (call_duration - mСurrentCall.getDetails().getConnectTimeMillis()) / 1000;

                            mtextStatus_Duration.setText(String.format("%02d:%02d:%02d", call_duration / 3600, (call_duration % 3600) / 60, (call_duration % 60)));
                        }
                    }
                    else {
                        CallManager.INSTANCE.getCurStatus(null);
                    }
                }
            });
        }
    }

    private class Work_with_DB extends AsyncTask<String, Void, ArrayList<String>> {

        private DataBaseHelper helper;
        ArrayList<String> ret_list = new ArrayList<>();

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            helper = new DataBaseHelper(CallActivity.this);

            ret_list.add(strings[0]);

            switch (strings[0]) {
                case "show client":
                    String client_id = "";
                    client_id = helper.getClientId(strings[1]);
                    if (client_id == "") {
                        //не клиент, проверить контакты
                        ret_list.add("not_client");
                        ret_list.add(SearchInContacts(strings[1]));
                    } else {
                        //клиент - получить и показать инфу
                        ret_list.add("client");
                        Client_Info client = helper.getClientInfo(client_id);
                        if(client != null) {

                            ret_list.add(String.format("%s%s%s", client.client_surname != null ? String.format(" %s", client.client_surname) : String.format(""),
                                    client.client_name != null ? String.format(" %s", client.client_name) : String.format(""),
                                    client.client_oldname != null ? String.format(" %s", client.client_oldname) : String.format("")));

                            ret_list.add(client.client_city != null ? String.format("%s", client.client_city) : String.format(""));
                            ret_list.add(client.client_pet != null ? String.format("%s", client.client_pet) : String.format(""));
                            ret_list.add(String.valueOf(client.client_cnt_orders));
                            ret_list.add(String.valueOf(client.client_sum_all));
                            ret_list.add(String.valueOf(client.client_first_order));
                            ret_list.add(String.valueOf(client.client_last_order));
                            ret_list.add(client.client_comment);
                            //ret_list.add(client.client_first_order != null ? String.format(" %s", client.client_pet) : String.format(""));

                        } else {
                            ret_list.add("It's our clients");
                        }
                    }
                    break;
            }
            //strings.length
            return ret_list;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            switch (strings.get(0)) {
                case "show client":
                    if (strings.get(1).equals("not_client")) {
                        showForOther();
                        mtextDisplayName.setText(strings.get(2));
                    } else {
                        showForClient();
                        mtextDisplayName.setText(strings.get(2));

                        TextView textViewCity = findViewById(R.id.textViewCity);
                        textViewCity.setText(strings.get(3));

                        TextView textViewPet = findViewById(R.id.textViewPet);
                        textViewPet.setText(strings.get(4));


//Toast example
                        String call_act_orders_summ = getString(R.string.call_act_orders_summ, strings.get(5), strings.get(6));
                        TextView textViewFreqOrdes = findViewById(R.id.textViewFreqOrdes);
                        textViewFreqOrdes.setText(Html.fromHtml(call_act_orders_summ));
                        //tv.setText(Html.fromHtml(myColorText));


                        TextView textViewComment = findViewById(R.id.textViewComment);
                        textViewComment.setText(strings.get(9));

                    }
                    break;
            }
        }
    }

    private void showForClient(){
        findViewById(R.id.imageBackground).setVisibility(View.INVISIBLE);
        findViewById(R.id.textViewCity).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewPet).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewFreqOrdes).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewComment).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewLastOrders).setVisibility(View.VISIBLE);

        mtextDisplayName.setTextColor(Color.BLACK);
        mtextStatus_Duration.setTextColor(Color.BLACK);

    }

    private void showForOther(){
        findViewById(R.id.imageBackground).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewCity).setVisibility(View.INVISIBLE);
        findViewById(R.id.textViewPet).setVisibility(View.INVISIBLE);
        findViewById(R.id.textViewFreqOrdes).setVisibility(View.INVISIBLE);
        findViewById(R.id.textViewComment).setVisibility(View.INVISIBLE);
        findViewById(R.id.textViewLastOrders).setVisibility(View.INVISIBLE);

        mtextDisplayName.setTextColor(Color.WHITE);
        mtextStatus_Duration.setTextColor(Color.WHITE);
    }
}
