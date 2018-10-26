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
            Log.d("CallService", "Call.Callback onStateChanged: " + call + ", state: " + state);
            CallManager.INSTANCE.updateCall(call);
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);

        Log.d("CallService", "onCallAdded");

        call.registerCallback(callCallback);

        Uri uri = call.getDetails().getHandle();
        String scheme = uri.getScheme();
        String schemeSpecificPart = uri.getSchemeSpecificPart();

        Intent intent = new Intent(this, CallActivity.class);
        int state = call.getState();

        intent.putExtra("Status", call.getState());
        intent.putExtra("PhoneNumber", schemeSpecificPart);

        ArrayList<SelectPA> data = new ArrayList<SelectPA>();

        if (call.getState() == STATE_SELECT_PHONE_ACCOUNT) {
            TelecomManager tm = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);

            List<PhoneAccountHandle> phoneAccountHandleList = tm.getCallCapablePhoneAccounts();
            PhoneAccount phoneAccount;

            for (int i = 0; i < phoneAccountHandleList.size(); i++) {
                phoneAccount = tm.getPhoneAccount(phoneAccountHandleList.get(i));

                data.add(new SelectPA(i, phoneAccountHandleList.get(i), phoneAccount.getLabel().toString(), phoneAccount.getIcon()));
            }
        }

        intent.putParcelableArrayListExtra("SelectPA", data);

        startActivity(intent);

            //call.phoneAccountSelected(handle, false);
            /*

            PhoneAccount phoneAccount = null;
            if (handle != null) {
                phoneAccount = tm.getPhoneAccount(handle);
            }
*/
/*
            String label = "nextArgRequired";
            PhoneAccount account = PhoneAccount.builder( handle, label).setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER).build();
            tm.registerPhoneAccount(account);
*/
/*
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.android.server.telecom", "com.android.server.telecom.settings.EnableAccountPreferenceActivity"));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
*/


        CallManager.INSTANCE.updateCall(call, data);
    }


    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        Log.d("CallService", "onCallRemoved");

        call.unregisterCallback(callCallback);
        CallManager.INSTANCE.updateCall(null);
    }


}

