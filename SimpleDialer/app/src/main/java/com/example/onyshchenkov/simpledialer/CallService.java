package com.example.onyshchenkov.simpledialer;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.InCallService;
import android.telecom.PhoneAccountHandle;
import android.telecom.VideoProfile;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

import static android.telecom.Call.*;

public class CallService extends InCallService {

    private Call.Callback callCallback = new Callback() {
        public void onStateChanged(Call call, int state) {
            Log.d("CallService", "Call.Callback onStateChanged: " + call + ", state: " + state);
            CallManager.INSTANCE.updateCall(call);
        }
    };



    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);

        //Log.d("CallService", "onCallAdded");

        call.registerCallback(callCallback);

        Uri uri = call.getDetails().getHandle();
        String scheme = uri.getScheme();
        String schemeSpecificPart = uri.getSchemeSpecificPart();

        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra("Status", "RINGING");
        intent.putExtra("PhoneNumber", schemeSpecificPart);
        startActivity( intent );

        CallManager.INSTANCE.updateCall(call);
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        //Log.d("CallService", "onCallRemoved");

        call.unregisterCallback(callCallback);
        CallManager.INSTANCE.updateCall(null);
    }
}
