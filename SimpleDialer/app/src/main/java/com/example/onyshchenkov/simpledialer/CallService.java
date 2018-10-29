package com.example.onyshchenkov.simpledialer;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.InCallService;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.telecom.Call.*;

public class CallService extends InCallService {

    private Call.Callback callCallback = new Callback() {
        public void onStateChanged(Call call, int state) {
            //Log.d("CallService", "Call.Callback onStateChanged: " + call + ", state: " + state);
            CallManager.INSTANCE.updateCall(call);
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);

        //int debug = 1;

        //Log.d("CallService", "onCallAdded");

        call.registerCallback(callCallback);


        Intent intent = new Intent(this, CallActivity.class);
        //Bundle bundle = new Bundle();

        //bundle.put("ddd", call);
        /*
        int state = call.getState();

        if (debug != 1) {
            intent.putExtra("Status", call.getState());
        }

        //intent.putExtras()
        //intent.putExtra("PhoneNumber", schemeSpecificPart);
/*
        ArrayList<SelectPA> data = new ArrayList<SelectPA>();

        if (call.getState() == STATE_SELECT_PHONE_ACCOUNT || debug ==1 ) {

            if (debug == 1) {
                intent.putExtra("Status", STATE_SELECT_PHONE_ACCOUNT);
            }

            TelecomManager tm = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);

            List<PhoneAccountHandle> phoneAccountHandleList = tm.getCallCapablePhoneAccounts();
            PhoneAccount phoneAccount;

            for (int i = 0; i < phoneAccountHandleList.size(); i++) {
                phoneAccount = tm.getPhoneAccount(phoneAccountHandleList.get(i));

                data.add(new SelectPA(i, phoneAccountHandleList.get(i), phoneAccount.getLabel().toString(), phoneAccount.getIcon()));
            }
        }

        intent.putParcelableArrayListExtra("SelectPA", data);
*/
        startActivity(intent);

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

