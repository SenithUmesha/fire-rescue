package com.blackeyedghoul.firefighters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {

    DatabaseHelper helper;
    SQLiteDatabase db;
    List<Contacts> contactsList = new ArrayList<>();

    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public List<Contacts> getAllContacts() {
        String[] columns = {DatabaseHelper.KEY_ID, DatabaseHelper.KEY_NAME, DatabaseHelper.KEY_POSITION, DatabaseHelper.KEY_STATION, DatabaseHelper.KEY_PHONE_NUMBER, DatabaseHelper.KEY_EMAIL};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        while (cursor.moveToNext()) {

            int index1 = cursor.getColumnIndex(DatabaseHelper.KEY_ID);
            int id = cursor.getInt(index1);
            int index2 = cursor.getColumnIndex(DatabaseHelper.KEY_NAME);
            String name = cursor.getString(index2);
            int index3 = cursor.getColumnIndex(DatabaseHelper.KEY_POSITION);
            String position = cursor.getString(index3);
            int index4 = cursor.getColumnIndex(DatabaseHelper.KEY_STATION);
            String station = cursor.getString(index4);
            int index5 = cursor.getColumnIndex(DatabaseHelper.KEY_PHONE_NUMBER);
            String phone_number = cursor.getString(index5);
            int index6 = cursor.getColumnIndex(DatabaseHelper.KEY_EMAIL);
            String email = cursor.getString(index6);

            Contacts contact = new Contacts(id, name, position, station, phone_number, email);
            contactsList.add(contact);
        }
        return contactsList;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "Firefighters.db";
        private static final String TABLE_NAME = "contact_list";
        private static final int DATABASE_VERSION = 2;
        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "name";
        private static final String KEY_POSITION = "position";
        private static final String KEY_STATION = "station";
        private static final String KEY_PHONE_NUMBER = "phone_number";
        private static final String KEY_EMAIL = "email";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
