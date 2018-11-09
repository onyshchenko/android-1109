package com.example.onyshchenkov.microcrm;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Date;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_SET_DEFAULT_DIALER = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkDefaultDialer();

        if (hasPermission(Manifest.permission.READ_CONTACTS)) {
            //readcontact();
        } else {
            requestPermission(new String[]{Manifest.permission.READ_CONTACTS});
        }


        //Manifest.permission.READ_PHONE_STATE

        if (hasPermission(Manifest.permission.READ_PHONE_STATE)) {
            //readcontact();
        } else {
            requestPermission(new String[]{Manifest.permission.READ_PHONE_STATE});

            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);

        }

        //new DataBaseHelper(this);

        Date date = new Date();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_get_from_server:
                //Intent intent = new Intent(this, Activity4.class);
                //startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readcontact() {
/*
        Cursor contacts = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Loop through the contacts
        while (contacts.moveToNext())
        {
            // Get the current contact name
            String name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));

            // Get the current contact phone number
            String phoneNumber = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\s+","");

            contacts.moveToLast();

        }
        contacts.close();
*/


        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

//                if (name.equals("Коханий")){
//                    Log.d("Names", name);
//                }
                Log.d("Names", name);
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    // Query phone here. Covered next
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                    while (phones.moveToNext()) {
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\s+", "");
                        Log.d("Number", phoneNumber);
                    }
                    phones.close();
                }

            }
        }



        //ContentResolver cr = getContentResolver();
/*
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = 394" , null, null);
        while (phones.moveToNext()) {
            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            switch (type) {
                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                    // do something with the Home number here...
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                    // do something with the Mobile number here...
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                    // do something with the Work number here...
                    break;
            }
        }
        phones.close();
*/

/*
        //ContactsContract.Contacts.CONTENT_URI
        Cursor cur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " like '%+67 236 3101%'", null, null);
        if (cur != null && cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                String id2 = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String number = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                String[] columnNames = cur.getColumnNames();

                String string1;
                for (int i = 0; i < columnNames.length; i++) {
                    string1 = cur.getString(cur.getColumnIndex(columnNames[i]));
                }
// Data10 - all; Data4; Data1


                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    //Query phone here
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            null,//ContactsContract.CommonDataKinds.Phone.NUMBER + " like '%23631%' ",
                            null,

                            null);
                    while (pCur != null && pCur.moveToNext()) {
                        // Get phone numbers here

                        String disp_name = pCur.getString(pCur.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));

                        String[] columnNames1 = pCur.getColumnNames();

                        String string11;
                        for (int i = 0; i < columnNames1.length; i++) {
                            string11 = pCur.getString(pCur.getColumnIndex(columnNames1[i]));
                        }

                        //ContactsContract.CommonDataKinds.Phone._ID + " = " + identifier

                    }
                    pCur.close();

                }
            }
        }

        cur.close();


        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode("673214159"));
        String name = "?";


        ContentResolver contentResolver = getContentResolver();
//content://com.android.contacts/phone_lookup/0673214159
        //new String[] {BaseColumns._ID,
        //                ContactsContract.PhoneLookup.DISPLAY_NAME }
        Cursor contactLookup = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);


        //ContactsContract.CommonDataKinds.Phone.NUMBER

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                String[] columnNames = contactLookup.getColumnNames();
                contactLookup.moveToNext();

                String string11;
                for (int i = 0; i < columnNames.length; i++) {
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
*/

        //finish();
        //setVisible(false);

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
            if (permissions[0].equals("android.permission.READ_CONTACTS")) {
                Toast.makeText(this, "Без этого разрешения названия контактов из телефонной книги отображаться не будут", Toast.LENGTH_SHORT).show();
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
