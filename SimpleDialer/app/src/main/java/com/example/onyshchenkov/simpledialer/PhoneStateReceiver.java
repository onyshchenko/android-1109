package com.example.onyshchenkov.simpledialer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStateReceiver extends BroadcastReceiver {
    private static final String TAG ="broadcast_intent";
    public static String incoming_number;
    private SharedPreferences sp, sp1;
    private SharedPreferences.Editor spEditor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onReceive(Context context, Intent intent) {
        //Log.d("intent_log", "Intent" + intent);
        //dialog=true;

        String action = intent.getAction();
        Log.d(TAG, action);


        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager
                    .EXTRA_NO_CONNECTIVITY, false);
            NetworkInfo info1 = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK);
            NetworkInfo info2 = intent.getParcelableExtra(ConnectivityManager
                    .EXTRA_OTHER_NETWORK_INFO);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean failOver = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
            Log.d("MY_TAG", "onReceive(): mNetworkInfo=" + info1 + " mOtherNetworkInfo = " +
                    (info2 == null ? "[none]" : info2 + " noConn=" + noConnectivity));

//            Toast.makeText(context, "onReceive(): mNetworkInfo=" + info1 + " mOtherNetworkInfo = " +
//                    (info2 == null ? "[none]" : info2 + " noConn=" + noConnectivity), Toast.LENGTH_SHORT).show();

        }
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            Context context1 = context;
            String event = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            incoming_number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.d(TAG, "The received event : "+ event +", incoming_number : " + incoming_number);
            String previus_state = getCallState(context);
            String current_state = "IDLE";

            if(incoming_number!=null){
                updateIncomingNumber(incoming_number,context);
            }else {
                incoming_number=getIncomingNumber(context);
            }

            switch (event) {
                case "RINGING":
                    //Log.d(TAG, "State : Ringing, incoming_number : " + incoming_number);
                    if((previus_state.equals("IDLE")) || (previus_state.equals("FIRST_CALL_RINGING"))){
                        current_state ="FIRST_CALL_RINGING";
                    }
                    if((previus_state.equals("OFFHOOK"))||(previus_state.equals("SECOND_CALL_RINGING"))){
                        current_state = "SECOND_CALL_RINGING";
                    }

                    break;
                case "OFFHOOK":
                    //Log.d(TAG, "State : offhook, incoming_number : " + incoming_number);
                    if((previus_state.equals("IDLE")) ||(previus_state.equals("FIRST_CALL_RINGING")) || previus_state.equals("OFFHOOK")){
                        current_state = "OFFHOOK";
                    }
                    if(previus_state.equals("SECOND_CALL_RINGING")){
                        current_state ="OFFHOOK";
                        //startDialog(context);
                    }
                    break;
                case "IDLE":
                    //Log.d(TAG, "State : idle and  incoming_number : " + incoming_number);
                    if((previus_state.equals("OFFHOOK")) || (previus_state.equals("SECOND_CALL_RINGING")) || (previus_state.equals("IDLE"))){
                        current_state ="IDLE";
                    }
                    if(previus_state.equals("FIRST_CALL_RINGING")){
                        current_state = "IDLE";
                        //startDialog(context);
                    }
                    updateIncomingNumber("no_number",context);
                    //Log.d(TAG,"stored incoming number flushed");
                    break;
            }
            if(!current_state.equals(previus_state)){
                //Log.d(TAG, "Updating  state from "+previus_state +" to "+current_state);
                updateCallState(current_state, context);
            }
            Log.d(TAG, "current_state = " + current_state + " previus_state = " + previus_state + " incoming_number = " + incoming_number);

            Intent intent_br;


            // current_state = OFFHOOK previus_state = SECOND_CALL_RINGING incoming_number = +380676962671


        }

        //telephonyManager.listen(new CustomPhoneStateListener(context), PhoneStateListener.LISTEN_CALL_STATE);

            /*
            try {
                GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
                int cellid= cellLocation.getCid();
                int celllac = cellLocation.getLac();

                Log.d("CellLocation", cellLocation.toString());
                Log.d("GSM CELL ID",  String.valueOf(cellid));
                Log.d("GSM Location Code", String.valueOf(celllac));

                Log.d("getNetworkCountryIso",telephonyManager.getNetworkCountryIso());

                Log.d("getSimOperator",telephonyManager.getSimOperator());
                Log.d("getNetworkOperator",telephonyManager.getNetworkOperator());
            } catch (SecurityException e) {
                e.printStackTrace();
                Log.d("GsmCellLocation","Error !\n" + e.getMessage());
            }
            */
            /*
            String DeviceId = "";
            try {
                DeviceId =  telephonyManager.getDeviceId();
                //Log.d("DeviceId",DeviceId);
            } catch (SecurityException e) {
                e.printStackTrace();
                Log.d("DeviceId","Error !\n" + e.getMessage());
            }
*/
    }



    public void updateCallState(String state, Context context){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        spEditor = sp.edit();
        spEditor.putString("call_state", state);
        spEditor.apply();
        //Log.d(TAG, "state updated");

    }

    public void updateIncomingNumber(String inc_num, Context context){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        spEditor = sp.edit();
        spEditor.putString("inc_num", inc_num);
        spEditor.apply();
        //Log.d(TAG, "incoming number updated");
    }

    public String getCallState(Context context){
        sp1 = PreferenceManager.getDefaultSharedPreferences(context);
        String st =sp1.getString("call_state", "IDLE");
        //Log.d(TAG,"get previous state as :"+st);
        return st;
    }

    public String getIncomingNumber(Context context){
        sp1 = PreferenceManager.getDefaultSharedPreferences(context);
        String st =sp1.getString("inc_num", "no_num");
        //Log.d(TAG,"get incoming number as :"+st);
        return st;
    }
}