package com.example.onyshchenkov.simpledialer;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static android.telecom.Call.STATE_DIALING;
import static android.telecom.Call.STATE_DISCONNECTED;
import static android.telecom.Call.STATE_RINGING;
import static android.telecom.Call.STATE_SELECT_PHONE_ACCOUNT;


public class CallManager {

    //private static Call currentCall;
    public static final CallManager INSTANCE;
    private MyCustomObjectListener listener;
    private ArrayList<Call> mCall;

    public CallManager() {
        this.listener = null;
        mCall = new ArrayList<Call>();
    }

    public void setCustomObjectListener(MyCustomObjectListener listener) {
        this.listener = listener;
    }

    /*
    public void updateCall(Call call) {
        updateCall(call, new ArrayList<SelectPA>());
    }
*/

    public void updateCall(Call call /*, ArrayList<SelectPA> data*/) {
        //currentCall = call;
        Log.d("CallManager", "Call.Callback onStateChanged: " + call);
        if (call != null) {

            int hashCode = call.getDetails().hashCode();
            boolean compare_hash = false;

            if (call.getState() == STATE_SELECT_PHONE_ACCOUNT | call.getState() == STATE_DIALING | call.getState() == STATE_RINGING) {

                for (int i = 0; i < mCall.size(); i++) {
                    if (mCall.get(i).getDetails().hashCode() == hashCode) {
                        compare_hash = true;
                    }
                }
                if (!compare_hash) {
                    mCall.add(call);
                }
            } else if (call.getState() == STATE_DISCONNECTED ) {

                for (int i = 0; i < mCall.size(); i++) {
                    if (mCall.get(i).getDetails().hashCode() == hashCode) {
                        mCall.remove(i);
                    }
                }

                if(mCall.size() == 0) {
                    if (listener != null) {
                        listener.onFinishCallActivity();
                    }
                }
            }

            if (listener != null) {
                listener.onUpdateCall(call);
            }
        }
    }



    public void acceptPhoneAccount(Call call, PhoneAccountHandle data) {
        //Log.d("CallManager", "acceptPhoneAccount");

        if (call != null) {
            call.phoneAccountSelected(data, false);
        }
    }


    public final void cancelCall(Call call) {

        if (call != null) {
            switch (call.getState()) {
                case 2:
                    call.reject(false, "");
                    break;
                default:
                    call.disconnect();
            }
        }
    }

    public final void getCurStatus(Call call) {

        if (call != null) {
            if (listener != null) {
                listener.onUpdateCall(call);
            }
        } else if (mCall.size() == 1){
            if (listener != null) {
                listener.onUpdateCall(mCall.get(0));
            }
        } else {
            String ss = "ssss";
        }
    }

    public void acceptCall(Call call) {
        Log.d("CallManager", "acceptCall");

        if (call != null) {
            call.answer(call.getDetails().getVideoState());
        }
    }

    static {
        INSTANCE = new CallManager();
    }

    public interface MyCustomObjectListener {
        public void onCallAdded(Call call);

        public void onCallRemoved(Call call);

        public void onUpdateCall(Call call);

        public void onFinishCallActivity();

        //public void onPhoheAccount(ArrayList<SelectPA> data);
    }
}