package com.nocson.eDepot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EzDatabaseAccess extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eDepot.db";
    private static final String TABLE_SETTING = "tbl_setting_info";

    public static final String COL_ID = "id";
    public static final String COL_Domain = "domain";
    public static final String COL_ADDRESS = "address";
    public static final String COL_ACCOUNT = "account";
    public static final String COL_PASSWORD = "password";

    public EzDatabaseAccess(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_SETTING + "("
                + COL_ID + " INTEGER PRIMARY KEY,"
                + COL_Domain + " TEXT,"
                + COL_ADDRESS + " TEXT,"
                + COL_ACCOUNT + " TEXT,"
                + COL_PASSWORD + " TEXT"  + ")";
        sqLiteDatabase.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING);
        onCreate(sqLiteDatabase);
    }
    public EzServerInfo GetSettingInfo()
    {
        String query = "Select * FROM " + TABLE_SETTING ;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        EzServerInfo product = new EzServerInfo();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setDomain(cursor.getString(1));
            product.setAddress(cursor.getString(2));
            product.setAccount((cursor.getString(3)));
            product.setPassword((cursor.getString(4)));
            cursor.close();
        } else {
            product = null;
        }
        db.close();
        return product;
    }
    //Insert
    public void addAccount(EzServerInfo product) {

        ContentValues values = new ContentValues();
        values.put(COL_Domain, product.getDomain());
        values.put(COL_ADDRESS, product.getAddress());
        values.put(COL_ACCOUNT, product.getAccount());
        values.put(COL_PASSWORD, product.getPassword());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_SETTING, null, values);
        db.close();
    }
    //Select
    public EzServerInfo findAccount(String account) {
        String query = "Select * FROM " + TABLE_SETTING + " WHERE " + COL_ACCOUNT + " =  \"" + account + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        EzServerInfo product = new EzServerInfo();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setAddress(cursor.getString(1));
            product.setAccount((cursor.getString(2)));
            product.setPassword((cursor.getString(3)));
            cursor.close();
        } else {
            product = null;
        }
        db.close();
        return product;
    }
    //Delete
    public boolean deleteAccount(String productname) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_SETTING + " WHERE " + COL_ACCOUNT + " =  \"" + productname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        EzServerInfo product = new EzServerInfo();

        if (cursor.moveToFirst()) {
            product.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_SETTING, COL_ID + " = ?",
                    new String[] { String.valueOf(product.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
