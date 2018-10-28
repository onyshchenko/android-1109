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

import static android.telecom.Call.STATE_SELECT_PHONE_ACCOUNT;


public class CallManager {

    private static Call currentCall;
    public static final CallManager INSTANCE;

    private MyCustomObjectListener listener;

    public CallManager() {
        this.listener = null;
    }

    public void setCustomObjectListener(MyCustomObjectListener listener) {
        this.listener = listener;
        //listener.onUpdateCall("updateCall", currentCall);
    }

    public void updateCall(Call call) {
        updateCall(call, new ArrayList<SelectPA>());
    }

    public void updateCall(Call call, ArrayList<SelectPA> data) {
        currentCall = call;
        Log.d("CallManager", "Call.Callback onStateChanged: " + call);
        if (call != null) {
            if (listener != null) {
                listener.onUpdateCall("updateCall", currentCall);
            }
        }
    }

    public void acceptPhoneAccount(SelectPA data) {
        Log.d("CallManager", "acceptPhoneAccount");

        if (currentCall != null) {
            currentCall.phoneAccountSelected(data.handle, false);
        }
    }


    public final void cancelCall() {

        if (currentCall != null) {
            switch (currentCall.getState()) {
                case 2:
                    INSTANCE.rejectCall();
                    break;
                default:
                    INSTANCE.disconnectCall();
            }
        }
    }

    public final void getCurStatus() {

        if (currentCall != null) {
            if (listener != null) {
                listener.onUpdateCall("updateCall", currentCall);
            }
        }
    }

    public void acceptCall() {
        Log.d("CallManager", "acceptCall");

        if (currentCall != null) {
            currentCall.answer(currentCall.getDetails().getVideoState());
            /*
            if (listener != null) {
                listener.onCallAdded("acceptCall", currentCall);
            }
            */
        }
    }

    private void rejectCall() {
        Log.d("CallManager", "rejectCall");

        if (currentCall != null) {
            currentCall.reject(false, "");
            /*
            if (listener != null) {
                listener.onCallRemoved("rejectCall", currentCall);
            }
            */
        }

    }

    private void disconnectCall() {
        Log.d("CallManager", "disconnectCall");

        if (currentCall != null) {
            currentCall.disconnect();
            /*
            if (listener != null) {
                listener.onCallRemoved("disconnectCall", currentCall);
            }
            */
        }

    }

    static {
        INSTANCE = new CallManager();
    }

    public interface MyCustomObjectListener {
        public void onCallAdded(String title, Call call);

        public void onCallRemoved(String title, Call call);

        public void onUpdateCall(String title, Call call);

        //public void onPhoheAccount(ArrayList<SelectPA> data);
    }
}