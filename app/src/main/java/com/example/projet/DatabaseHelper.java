package com.example.projet;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.example.projet.ui.list.ListFragment;
import com.example.projet.ui.list.MyData;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

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
    private static final String COLUMN_USERID = "userId";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_TITLE = "titre";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT," + COLUMN_EMAIL + " TEXT," + COLUMN_PHONE + " NUMERIC," + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTable);
        String createList = "CREATE TABLE " + TABLE_LIST + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERID + " INTEGER, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_IMAGE + " BLOB, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL, " +
                COLUMN_DATE + " TEXT)";
        db.execSQL(createList);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(String name, String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_PASSWORD, password);

        db.insert(TABLE_USERS, null, contentValues);
        db.close();
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

    public Cursor findUserById(int uid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[] { "id", "email", "password", "nom", "phone" }, "id=?",
                new String[] {String.valueOf(uid)}, null, null, null);
        int count = cursor.getCount();
        if (count != 0) {
            return cursor;
        }else {
            return null;
        }
    }

    public long insertPhoto(int userId, String title, String description, Bitmap image, double latitude, double longitude, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERID, userId);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE, getBytes(image));
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_DATE, String.valueOf(date));

        long id = db.insert(TABLE_LIST, null, values);

        db.close();

        return id;
    }

    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public ArrayList<MyData> getListData(int uid) {
        ArrayList<MyData> data = new ArrayList<>();

        // Récupérez les données de la base de données
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LIST, new String[] {COLUMN_USERID,COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_IMAGE, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_DATE}, "userId=?", new String[] {String.valueOf(uid)}, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String userid = cursor.getString(cursor.getColumnIndex(COLUMN_USERID));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
            @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
            @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));

            MyData item = new MyData(title, description,date, latitude, longitude, image);
            data.add(item);
        }
        cursor.close();

        return data;
    }

    public void deleteData(int userId, String titre, byte[] img, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM list " +
                "WHERE userId = ?" +
                "AND titre = ? " +
                "AND image = ?" +
                "AND date = ?";
        Object[] args = {userId, titre, img, date};
        db.execSQL(query, args);
    }

    public void uptdateUser(int uid, String nom, String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("email", email);
        values.put("phone", phone);
        values.put("password", password);

        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(uid) };

        db.update("users", values, whereClause, whereArgs);
        db.close();
    }
}