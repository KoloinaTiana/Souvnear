package com.example.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "accounts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "nom";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_LISTS = "lists";
    private static final String COLUMN_USER = "user";
    private static final String COLUMN_TITLE = "title";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT," + COLUMN_EMAIL + " TEXT," + COLUMN_PHONE + " NUMERIC," + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTable);
        String createList = "CREATE TABLE " + TABLE_LISTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER + " TEXT," + COLUMN_TITLE + " TEXT," + COLUMN_PHONE + " NUMERIC," + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTable);
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
        Cursor cursor = db.query("users", new String[] { "id", "email", "password" }, "email=? and password=?",
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
        Cursor cursor = db.query("users", new String[] { "id", "email", "password" }, "email=? and password=?",
                new String[] { email, password }, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count != 0) {
            return cursor;
        }else {
            return null;
        }
    }

}