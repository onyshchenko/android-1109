package com.example.onyshchenkov.microcrm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.UUID;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MicroCRM.db";

    // Tables Name
    private final String TABLE_INCOMING_CALL = "incoming_call";
    private final String TABLE_CUSTOMERS_STAT = "customers_stat";
    private final String TABLE_NUMBERS_CUSTOMERS = "numbers_customers";
    private final String TABLE_CUSTOMER_ORDERS = "customer_orders";
    private final String TABLE_ORDERS_DETAILS = "orders_details";
    private final String TABLE_DELIVERY_ADDRESS = "delivery_address";

    private final String TABLE_CATEGORIES = "categories";
    private final String TABLE_SUB_CATEGORIES = "sub_categories";
    private final String TABLE_ITEMS = "items";

    // Columns Name
    // TABLE_INCOMING_CALL
    private final String COLUMN_ID = "_id";
    private final String COLUMN_INCOMING_NUMBER = "incoming_number";
    private final String COLUMN_DURATION = "duration";
    private final String COLUMN_CREATED_ACTION = "date";
    private final String COLUMN_STATUS = "call_status";

    // Table Create Statements
    private final String CREATE_TABLE_INCOMING_CALL = "CREATE TABLE " + TABLE_INCOMING_CALL + "(" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_INCOMING_NUMBER + " TEXT," +
            COLUMN_DURATION + " INTEGER," +
            COLUMN_CREATED_ACTION + " INTEGER," +
            COLUMN_STATUS + " INTEGER)";

    // TABLE_CUSTOMERS_STAT
    //private final String COLUMN_ID = "_id";
    private final String COLUMN_SURNAME = "surname"; //фамилия
    private final String COLUMN_NAME = "name"; // Имя
    private final String COLUMN_OLDNAME = "oldname"; // отчество
    private final String COLUMN_CITY = "city"; // город
    private final String COLUMN_PET = "pet";  // питомец

    private final String COLUMN_CNT_ORDERS = "cnt_orders"; // всего заказов
    private final String COLUMN_FIRST_ORDER = "first_order"; // дата перового заказа
    private final String COLUMN_LAST_ORDER = "last_order"; // дата последнего заказа

    private final String COLUMN_SUM_ALL = "sum_all"; // заработок на этом клиенте
    private final String COLUMN_COMMENT = "cmmnt"; // комментарий


    // Table Create Statements
    private final String CREATE_TABLE_CUSTOMERS_STAT = "CREATE TABLE " + TABLE_CUSTOMERS_STAT + "(" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_SURNAME + " TEXT," +
            COLUMN_NAME + " TEXT," +
            COLUMN_OLDNAME + " TEXT," +
            COLUMN_CITY + " TEXT," +
            COLUMN_PET + " TEXT," +
            COLUMN_CNT_ORDERS + " INTEGER," +
            COLUMN_FIRST_ORDER + " INTEGER," +
            COLUMN_LAST_ORDER + " INTEGER," +
            COLUMN_SUM_ALL + " NUMERIC," +
            COLUMN_COMMENT + " TEXT)";


    // TABLE_NUMBERS_CUSTOMERS
    //private final String COLUMN_ID = "_id";
    private final String COLUMN_CLIENT_ID = "client_id"; // ИД клиента
    private final String COLUMN_PHONE_NUMBER = "phone"; // номер телефона

    private final String CREATE_TABLE_NUMBERS_CUSTOMERS = "CREATE TABLE " + TABLE_NUMBERS_CUSTOMERS + "(" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_CLIENT_ID + " TEXT," +
            COLUMN_PHONE_NUMBER + " TEXT" +
            ")";


    //TABLE_CUSTOMER_ORDERS
    //private final String COLUMN_ID = "_id";
    //private final String COLUMN_CLIENT_ID = "client_id"; // ИД клиента
    private final String COLUMN_TRANSPORT_ID = "transport_id"; // ИД записи о доставке

    private final String CREATE_TABLE_CUSTOMER_ORDERS = "CREATE TABLE " + TABLE_CUSTOMER_ORDERS + "(" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_CLIENT_ID + " TEXT," +
            COLUMN_TRANSPORT_ID + " TEXT" +
            ")";


    // TABLE_ORDERS_DETAILS
    //private final String COLUMN_ID = "_id";
    //private final String COLUMN_CLIENT_ID = "client_id"; // ИД клиента
    private final String COLUMN_ORDER_ID = "order_id"; // ИД заказа
    private final String COLUMN_ITEM_ID = "item_id"; // ИД товара
    private final String COLUMN_CNT = "cnt"; // Количество
    private final String COLUMN_SUMM = "summ"; // Сумма

    private final String CREATE_TABLE_ORDERS_DETAILS = "CREATE TABLE " + TABLE_ORDERS_DETAILS + "(" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_ORDER_ID + " TEXT," +
            COLUMN_ITEM_ID + " TEXT," +
            COLUMN_CNT + " INTEGER," +
            COLUMN_SUMM + " NUMERIC" +
            ")";


    // TABLE_ITEMS
    //private final String COLUMN_ID = "_id";
    private final String COLUMN_SUBCATEGORY_ID = "subcategory_id"; // ИД заказа


    private final String CREATE_TABLE_ITEMS = "CREATE TABLE " + TABLE_ITEMS + "(" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_NAME + " TEXT," +
            COLUMN_SUBCATEGORY_ID + " TEXT" +
            ")";


    // TABLE_SUB_CATEGORIES
    //private final String COLUMN_ID = "_id";
    private final String COLUMN_CATEGORY_ID = "category_id"; // ИД заказа


    private final String CREATE_TABLE_SUB_CATEGORIES = "CREATE TABLE " + TABLE_SUB_CATEGORIES + "(" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_NAME + " TEXT," +
            COLUMN_CATEGORY_ID + " TEXT" +
            ")";


    // TABLE_CATEGORIES
    //private final String COLUMN_ID = "_id";

    private final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + "(" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_NAME + " TEXT" +
            ")";


    // TABLE_DELIVERY_ADDRESS
    //private final String COLUMN_ID = "_id";

    private final String CREATE_TABLE_DELIVERY_ADDRESS = "CREATE TABLE " + TABLE_DELIVERY_ADDRESS + "(" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_NAME + " TEXT" +
            ")";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INCOMING_CALL);
        db.execSQL(CREATE_TABLE_CUSTOMERS_STAT);
        db.execSQL(CREATE_TABLE_NUMBERS_CUSTOMERS);
        db.execSQL(CREATE_TABLE_CUSTOMER_ORDERS);
        db.execSQL(CREATE_TABLE_ORDERS_DETAILS);
        db.execSQL(CREATE_TABLE_ITEMS);
        db.execSQL(CREATE_TABLE_SUB_CATEGORIES);
        db.execSQL(CREATE_TABLE_DELIVERY_ADDRESS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOMING_CALL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS_STAT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NUMBERS_CUSTOMERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_ORDERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS_DETAILS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_CATEGORIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERY_ADDRESS);
        // create new tables
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOMING_CALL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS_STAT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NUMBERS_CUSTOMERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_ORDERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS_DETAILS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_CATEGORIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERY_ADDRESS);
        // create new tables
        onCreate(sqLiteDatabase);
    }

    public String getClientId(String str_phone_number){
        String clientid = "";
        Cursor cursor = null;
        SQLiteDatabase db = getReadableDatabase();

        try {

            String strSQL = "Select t1." + COLUMN_CLIENT_ID +
                    " from " + TABLE_NUMBERS_CUSTOMERS + " t1" +
                    " where " + COLUMN_PHONE_NUMBER + " = " + str_phone_number;


            cursor = db.rawQuery(strSQL, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    clientid = cursor.getString(cursor.getColumnIndex(COLUMN_CLIENT_ID));

                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return clientid;
    }

    public String getClientInfo(String str_clientid){
        String clientid = "";
        Cursor cursor = null;
        SQLiteDatabase db = getReadableDatabase();

        try {

            String strSQL = "Select t1." + COLUMN_CLIENT_ID +
                    " from " + TABLE_NUMBERS_CUSTOMERS + " t1" +
                    " where " + COLUMN_PHONE_NUMBER + " = " + str_clientid;


            cursor = db.rawQuery(strSQL, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    clientid = cursor.getString(cursor.getColumnIndex(COLUMN_CLIENT_ID));

                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return clientid;
    }


}
