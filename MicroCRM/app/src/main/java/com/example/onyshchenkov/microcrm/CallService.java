package com.example.onyshchenkov.microcrm;


import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telecom.Call;
import android.telecom.InCallService;
import android.util.Log;

import static android.telecom.Call.Callback;

public class CallService extends InCallService {

    private Callback callCallback = new Callback() {
        public void onStateChanged(Call call, int state) {
            Log.d("MicroCRM", "Call.Callback onStateChanged: " + call + ", state: " + state);
            CallManager.INSTANCE.updateCall(call);
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);


        Log.d("MicroCRM", "onCallAdded");

        call.registerCallback(callCallback);


        Intent intent = new Intent(this, CallActivity.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        //startActivity(intent);

        CallManager.INSTANCE.updateCall(call);
    }


    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        Log.d("MicroCRM", "onCallRemoved");

        call.unregisterCallback(callCallback);
        CallManager.INSTANCE.updateCall(null);
    }

}

