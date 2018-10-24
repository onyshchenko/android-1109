package com.example.onyshchenkov.simpledialer;

import android.annotation.TargetApi;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.util.Log;
import android.view.View;

import java.util.Observable;


public class CallManager {

    private static Call currentCall;
    public static final CallManager INSTANCE;

    private MyCustomObjectListener  listener;

    public CallManager() {
        this.listener = null;
    }

    public void setCustomObjectListener(MyCustomObjectListener  listener) {
        this.listener = listener;
    }

    public void updateCall(Call call) {
        currentCall = call;
        //Log.d("CallManager", "Call.Callback onStateChanged: " + call);
        if (call != null) {
            if (listener != null) {
                listener.onUpdateCall("updateCall", currentCall);
            }
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

    public void acceptCall() {
        //Log.d("CallManager", "acceptCall");

        if (currentCall != null) {
            currentCall.answer(currentCall.getDetails().getVideoState());
            if (listener != null) {
                listener.onCallAdded("acceptCall", currentCall);
            }
        }

    }

    private void rejectCall() {
        //Log.d("CallManager", "rejectCall");

        if (currentCall != null) {
            currentCall.reject(false, "");
            if (listener != null) {
                listener.onCallRemoved("rejectCall", currentCall);
            }
        }

    }

    private void disconnectCall() {
        //Log.d("CallManager", "disconnectCall");

        if (currentCall != null) {
            currentCall.disconnect();
            if (listener != null) {
                listener.onCallRemoved("disconnectCall", currentCall);
            }
        }

    }

    static {
        INSTANCE = new CallManager();
    }

    public interface MyCustomObjectListener {
        public void onCallAdded(String title, Call call);

        public void onCallRemoved(String title, Call call);

        public void onUpdateCall(String title, Call call);
    }
}