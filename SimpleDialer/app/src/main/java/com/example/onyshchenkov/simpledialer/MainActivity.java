package com.example.onyshchenkov.simpledialer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_SET_DEFAULT_DIALER = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkDefaultDialer();

        if (hasPermission(Manifest.permission.READ_CONTACTS)) {
            readcontact();
        } else {
            requestPermission(new String[]{Manifest.permission.READ_CONTACTS});
        }
    }

    private void readcontact(){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode("673214159"));
        String name = "?";

        ContentResolver contentResolver = getContentResolver();
//content://com.android.contacts/phone_lookup/0673214159
        //new String[] {BaseColumns._ID,
        //                ContactsContract.PhoneLookup.DISPLAY_NAME }
        Cursor contactLookup = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                String[] columnNames = contactLookup.getColumnNames();
                contactLookup.moveToNext();

                String string11;
                for(int i=0;i<columnNames.length;i++){
                    string11 = contactLookup.getString(contactLookup.getColumnIndex(columnNames[i]));
                }

                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));

            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }
    }


    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED;
        }

        return true;
    }

    private void requestPermission(String[] str_prmission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /*
            String[] permissions = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            */
            requestPermissions(str_prmission, 0);
        }
    }

    //adb shell pm reset-permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("PermissionsResult", "requestCode " + requestCode + " permissions.len " + permissions.length + " permissions[0] " + permissions[0]);
        if (requestCode == 0 && grantResults[0] == PERMISSION_GRANTED) {
            /*
            ничего не делаем, т.к. это запрос на "будущее"
            if (permissions[0].equals("android.permission.READ_CONTACTS") ) {
                readcontact();
            }
            */
        } else {
            if (permissions[0].equals("android.permission.READ_CONTACTS") ) {
                Toast.makeText(this,"Без этого разрешения названия контактов из телефонной книги отображаться не будут",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkDefaultDialer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.d("checkDefaultDialer", "Build.VERSION.SDK_INT " + Build.VERSION.SDK_INT + " Build.VERSION_CODES.M " + Build.VERSION_CODES.M);
            return;
        }

        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
        //String isAlreadyDefaultDialer = telecomManager.getDefaultDialerPackage();

        boolean isAlreadyDefaultDialer = getPackageName()
                .equals(telecomManager.getDefaultDialerPackage());

        if (isAlreadyDefaultDialer) {
            // уже наша программа назначена програмой по умолчанию
            Log.d("checkDefaultDialer", "isAlreadyDefaultDialer " + isAlreadyDefaultDialer);
            return;
        }

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
                    //Toast.makeText(this, "User accepted request to become default dialer", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Log.d("onActivityResult", "User declined request to become default dialer");
                    //Toast.makeText(this, "User declined request to become default dialer", Toast.LENGTH_SHORT).show();
                    break;
                    default:
                        //Toast.makeText(this, "Unexpected result code " + requestCode, Toast.LENGTH_SHORT).show();
                        break;
            }
        }
    }

}
