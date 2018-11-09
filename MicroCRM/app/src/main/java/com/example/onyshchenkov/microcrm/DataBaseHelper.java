package com.example.onyshchenkov.microcrm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
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
            COLUMN_COMMENT + " TEXT" +
            ")";


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
            COLUMN_TRANSPORT_ID + " TEXT," +
            COLUMN_CREATED_ACTION + " INTEGER" +
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
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_DELIVERY_ADDRESS);

        //init_db(db);
    }

    private void init_db(SQLiteDatabase db){
        String client_id = "",
                phone_id = "",
                in_call_id = "",
                category_id = "",
                sub_category_id = "",
                item_id = "",
                delivery_id = "",
                order_id = "",
                order_det_id = "";

        long id;

        try {

            ContentValues values = new ContentValues();
            client_id = UUID.randomUUID().toString();
            values.put(COLUMN_ID, client_id);
            values.put(COLUMN_SURNAME, "Иванов");
            values.put(COLUMN_NAME, "Никита");
            values.put(COLUMN_CITY, "Киев");
            values.put(COLUMN_CNT_ORDERS, 4);
            values.put(COLUMN_FIRST_ORDER, (new Date()).getTime()-(86400 * 5)); ////
            values.put(COLUMN_LAST_ORDER, (new Date()).getTime()); ///long call_duration = (new Date()).getTime();
            values.put(COLUMN_SUM_ALL, 4453);
            values.put(COLUMN_COMMENT, "любит \"жаловаться\" на жизнь");


            id = db.insert(TABLE_CUSTOMERS_STAT, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ContentValues values = new ContentValues();
            phone_id = UUID.randomUUID().toString();
            values.put(COLUMN_ID, phone_id);
            values.put(COLUMN_CLIENT_ID, client_id);
            values.put(COLUMN_PHONE_NUMBER, "+380672363101");

            id = db.insert(TABLE_NUMBERS_CUSTOMERS, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ContentValues values = new ContentValues();
            in_call_id = UUID.randomUUID().toString();
            values.put(COLUMN_ID, in_call_id);
            values.put(COLUMN_INCOMING_NUMBER, "+380672363101");
            values.put(COLUMN_DURATION, 250);
            values.put(COLUMN_CREATED_ACTION, (new Date()).getTime());


            id = db.insert(TABLE_INCOMING_CALL, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ContentValues values = new ContentValues();
            category_id = UUID.randomUUID().toString();
            values.put(COLUMN_ID, category_id);
            values.put(COLUMN_NAME, "Первая категория");

            id = db.insert(TABLE_CATEGORIES, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ContentValues values = new ContentValues();
            sub_category_id = UUID.randomUUID().toString();
            values.put(COLUMN_ID, sub_category_id);
            values.put(COLUMN_NAME, "Первая категория");
            values.put(COLUMN_CATEGORY_ID, category_id);

            id = db.insert(TABLE_SUB_CATEGORIES, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ContentValues values = new ContentValues();
            item_id = UUID.randomUUID().toString();
            values.put(COLUMN_ID, item_id);
            values.put(COLUMN_NAME, "Первый товар");
            values.put(COLUMN_SUBCATEGORY_ID, sub_category_id);

            id = db.insert(TABLE_ITEMS, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ContentValues values = new ContentValues();
            delivery_id = UUID.randomUUID().toString();
            values.put(COLUMN_ID, delivery_id);
            values.put(COLUMN_NAME, "Первый товар");

            id = db.insert(TABLE_DELIVERY_ADDRESS, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ContentValues values = new ContentValues();
            order_id = UUID.randomUUID().toString();
            values.put(COLUMN_ID, order_id);
            values.put(COLUMN_CLIENT_ID, client_id);
            values.put(COLUMN_TRANSPORT_ID, delivery_id);
            values.put(COLUMN_CREATED_ACTION, (new Date()).getTime());

            id = db.insert(TABLE_CUSTOMER_ORDERS, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ContentValues values = new ContentValues();
            order_det_id = UUID.randomUUID().toString();
            values.put(COLUMN_ID, order_det_id);
            values.put(COLUMN_ORDER_ID, order_id);
            values.put(COLUMN_ITEM_ID, item_id);
            values.put(COLUMN_CNT, 2);
            values.put(COLUMN_SUMM, 150);

            id = db.insert(TABLE_ORDERS_DETAILS, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    " where " + COLUMN_PHONE_NUMBER + " = '" + str_phone_number + "'";


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

    public Client_Info getClientInfo(String str_clientid){

        String client_surname = "";
        String client_name = "";
        String client_oldname = "";
        String client_city = "";
        String client_pet = "";
        int client_cnt_orders = 0;
        long client_first_order = 0;
        long client_last_order = 0;
        double client_sum_all = 0;
        String client_comment = "";

        Client_Info client = null;

        Cursor cursor = null;
        SQLiteDatabase db = getReadableDatabase();

        try {

            String strSQL = "Select t1." + COLUMN_SURNAME +
                    ", t1." + COLUMN_NAME +
                    ", t1." + COLUMN_OLDNAME +
                    ", t1." + COLUMN_CITY +
                    ", t1." + COLUMN_PET +
                    ", t1." + COLUMN_CNT_ORDERS +
                    ", t1." + COLUMN_FIRST_ORDER +
                    ", t1." + COLUMN_LAST_ORDER +
                    ", t1." + COLUMN_SUM_ALL +
                    ", t1." + COLUMN_COMMENT +
                    " from " + TABLE_CUSTOMERS_STAT + " t1" +
                    " where " + COLUMN_ID + " = '" + str_clientid + "'";

            cursor = db.rawQuery(strSQL, null);

            if (cursor.moveToFirst()) {

                while (!cursor.isAfterLast()) {

                    client_surname = cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME));
                    client_name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    client_oldname = cursor.getString(cursor.getColumnIndex(COLUMN_OLDNAME));
                    client_city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
                    client_pet = cursor.getString(cursor.getColumnIndex(COLUMN_PET));
                    client_cnt_orders = cursor.getInt(cursor.getColumnIndex(COLUMN_CNT_ORDERS));
                    client_first_order = cursor.getLong(cursor.getColumnIndex(COLUMN_FIRST_ORDER));
                    client_last_order = cursor.getLong(cursor.getColumnIndex(COLUMN_LAST_ORDER));
                    client_sum_all = cursor.getDouble(cursor.getColumnIndex(COLUMN_SUM_ALL));
                    client_comment = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT));

                    cursor.moveToNext();
                }
                client = new Client_Info(str_clientid,client_surname,client_name,client_oldname,client_city,client_pet,
                        client_cnt_orders,client_first_order,client_last_order,client_sum_all,client_comment);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return client;
    }
}
