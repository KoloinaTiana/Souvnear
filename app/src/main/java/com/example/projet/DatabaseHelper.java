package com.example.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "accounts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "nom";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_LIST = "list";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE = "user";
    private static final String COLUMN_TITLE = "image";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT," + COLUMN_EMAIL + " TEXT," + COLUMN_PHONE + " NUMERIC," + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTable);
        String createList = "CREATE TABLE " + TABLE_LIST + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_IMAGE + " BLOB, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL)";
        db.execSQL(createList);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(String name, String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("OUHHHH", "BDD crée");
        ContentValues contentValues = new ContentValues();
        int id = 0;
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_PASSWORD, password);

        db.insert(TABLE_USERS, null, contentValues);
        db.close();
        id++;
    }

    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[] { "id", "email", "password", "nom" }, "email=? and password=?",
                new String[] { email, password }, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count != 0) {
            return true;
        }else {
            return false;
        }
    }

    public Cursor findUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[] { "id", "email", "password", "nom" }, "email=? and password=?",
                new String[] { email, password }, null, null, null);
        int count = cursor.getCount();
        if (count != 0) {
            return cursor;
        }else {
            return null;
        }
    }

    public long insertPhoto(String title, String description, Bitmap image, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE, getBytes(image));
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);

        long id = db.insert(TABLE_LIST, null, values);

        db.close();

        return id;
    }

    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}