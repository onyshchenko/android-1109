package com.example.onyshchenkov.microcrm;

import android.telecom.Call;
import android.telecom.PhoneAccountHandle;
import android.util.Log;

import java.util.ArrayList;

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


    public void updateCall(Call call ) {

        Log.d("MicroCRM", "CallManager. updateCall: " + call);
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
                    Log.d("MicroCRM", "CallManager. Add Call. Call count: " + mCall.size());
                }
            } else if (call.getState() == STATE_DISCONNECTED) {

                for (int i = 0; i < mCall.size(); i++) {
                    if (mCall.get(i).getDetails().hashCode() == hashCode) {
                        mCall.remove(i);
                        Log.d("MicroCRM", "CallManager. Call Removed. Call count: " + mCall.size());
                    }
                }

                if (mCall.size() == 0) {
                    if (listener != null) {
                        Log.d("MicroCRM", "CallManager. Activity finish. Call count: " + mCall.size());
                        listener.onFinishCallActivity();
                    }
                }
            }

            if (listener != null) {
                int cnt_active_calls = 0;
                if (mCall.size() > 1) {
                    for (int i = 0; i < mCall.size(); i++) {
                        if (mCall.get(i).getState() == Call.STATE_ACTIVE || mCall.get(i).getState() == Call.STATE_HOLDING) {
                            cnt_active_calls++;
                        }
                    }
                }
                if (cnt_active_calls == 2) {
                    listener.onShowChangeCallButton();
                } else {
                    listener.onHideChangeCallButton();
                }

                listener.onUpdateCall(call);
            }
        }
    }


    public void acceptPhoneAccount(Call call, PhoneAccountHandle data) {
        Log.d("MicroCRM", "CallManager. acceptPhoneAccount");

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

    public final void changeCall(Call call) {

        call.hold();
/*
        if (mCall.size() > 1) {
            for (int i = 0; i < mCall.size(); i++) {
                if (mCall.get(i).getState() == Call.STATE_HOLDING) {
                    mCall.get(i).unhold();

                }
            }
        }
*/
    }

    public void getCurStatus(Call call) {

        if (call != null) {
            if (listener != null) {
                listener.onUpdateCall(call);
            }
        } else if (mCall.size() == 1) {
            if (listener != null) {
                listener.onUpdateCall(mCall.get(0));
            }
        }
    }

    public void acceptCall(Call call) {
        Log.d("MicroCRM", "CallManager. acceptCall");

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

        public void onShowChangeCallButton();

        public void onHideChangeCallButton();

        //public void onPhoheAccount(ArrayList<SelectPA> data);
    }
}