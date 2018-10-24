package com.example.onyshchenkov.simpledialer;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_SET_DEFAULT_DIALER = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkDefaultDialer();
    }

    private void checkDefaultDialer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.d("checkDefaultDialer", "Build.VERSION.SDK_INT " + Build.VERSION.SDK_INT + " Build.VERSION_CODES.M " + Build.VERSION_CODES.M);
            return;
        }

        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
        //String isAlreadyDefaultDialer = telecomManager.getDefaultDialerPackage();

        //Log.d("checkDefaultDialer", "isAlreadyDefaultDialer " + isAlreadyDefaultDialer);

/*
        boolean isAlreadyDefaultDialer = getPackageName()
                .equals(telecomManager.getDefaultDialerPackage());

        if (isAlreadyDefaultDialer) {
            Log.d("checkDefaultDialer", "isAlreadyDefaultDialer " + isAlreadyDefaultDialer);
            return;
        }
*/


        Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
        intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, this.getPackageName());
        startActivityForResult(intent, REQUEST_CODE_SET_DEFAULT_DIALER);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

        if (requestCode == REQUEST_CODE_SET_DEFAULT_DIALER) {
            switch (resultCode) {
                case RESULT_OK:
                    Log.d("onActivityResult", "User accepted request to become default dialer");
                    Toast.makeText(this, "User accepted request to become default dialer", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Log.d("onActivityResult", "User declined request to become default dialer");
                    Toast.makeText(this, "User declined request to become default dialer", Toast.LENGTH_SHORT).show();
                    break;
                    default:
                        Toast.makeText(this, "Unexpected result code " + requestCode, Toast.LENGTH_SHORT).show();
                        break;
            }

                //Log.d("onActivityResult", "requestCode " + requestCode + " resultCode " + resultCode);

        }
    }

}
