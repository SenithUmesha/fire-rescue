package com.blackeyedghoul.firefighters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {

    DatabaseHelper helper;
    SQLiteDatabase db;
    List<Contacts> contactsList = new ArrayList<>();
    List<Station> stationsList = new ArrayList<>();
    List<Tip> tipsList = new ArrayList<>();

    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
        db = helper.getReadableDatabase();
    }

    public List<Contacts> getAllContacts() {
        String[] columns = {DatabaseHelper.KEY_NAME, DatabaseHelper.KEY_POSITION, DatabaseHelper.KEY_STATION, DatabaseHelper.KEY_PHONE_NUMBER, DatabaseHelper.KEY_EMAIL};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME1, columns, null, null, null, null, null, null);
        while (cursor.moveToNext()) {

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

            Contacts contact = new Contacts(name, position, station, phone_number, email);
            contactsList.add(contact);
        }

        cursor.close();
        return contactsList;
    }

    public List<Station> getAllStations() {
        String[] columns = {DatabaseHelper.KEY_S_NAME, DatabaseHelper.KEY_S_ADDRESS, DatabaseHelper.KEY_S_PHONE_NUMBER, DatabaseHelper.KEY_S_TOTAL_FIGHTERS, DatabaseHelper.KEY_S_TOTAL_VEHICLES, DatabaseHelper.KEY_S_LAT, DatabaseHelper.KEY_S_LON};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME2, columns, null, null, null, null, null, null);
        while (cursor.moveToNext()) {

            int index1 = cursor.getColumnIndex(DatabaseHelper.KEY_S_NAME);
            String name = cursor.getString(index1);
            int index2 = cursor.getColumnIndex(DatabaseHelper.KEY_S_ADDRESS);
            String address = cursor.getString(index2);
            int index3 = cursor.getColumnIndex(DatabaseHelper.KEY_S_PHONE_NUMBER);
            String phone_number = cursor.getString(index3);
            int index4 = cursor.getColumnIndex(DatabaseHelper.KEY_S_TOTAL_FIGHTERS);
            int total_f = cursor.getInt(index4);
            int index5 = cursor.getColumnIndex(DatabaseHelper.KEY_S_TOTAL_VEHICLES);
            int total_v = cursor.getInt(index5);
            int index6 = cursor.getColumnIndex(DatabaseHelper.KEY_S_LAT);
            String lat = cursor.getString(index6);
            int index7 = cursor.getColumnIndex(DatabaseHelper.KEY_S_LON);
            String lon = cursor.getString(index7);

            Station station = new Station(name, address, phone_number, total_f, total_v, lat, lon);
            stationsList.add(station);
        }
        cursor.close();
        return stationsList;
    }

    public List<Tip> getAllTips() {
        String[] columns = {DatabaseHelper.KEY_TIP_TOPIC, DatabaseHelper.KEY_TIP_SUB_TOPIC};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME3, columns, null, null, null, null, null, null);
        while (cursor.moveToNext()) {

            int index2 = cursor.getColumnIndex(DatabaseHelper.KEY_TIP_TOPIC);
            String topic = cursor.getString(index2);
            int index3 = cursor.getColumnIndex(DatabaseHelper.KEY_TIP_SUB_TOPIC);
            String sub_topic = cursor.getString(index3);

            Tip tip = new Tip(topic, sub_topic);
            tipsList.add(tip);
        }
        cursor.close();
        return tipsList;
    }

    private static class DatabaseHelper extends SQLiteAssetHelper {

        private static final String DATABASE_NAME = "Firefighters.db";
        private static final String TABLE_NAME1 = "contact_list";
        private static final String TABLE_NAME2 = "station_list";
        private static final String TABLE_NAME3 = "tips";

        private static final int DATABASE_VERSION = 2;

        private static final String KEY_NAME = "name";
        private static final String KEY_POSITION = "position";
        private static final String KEY_STATION = "station";
        private static final String KEY_PHONE_NUMBER = "phone_number";
        private static final String KEY_EMAIL = "email";

        private static final String KEY_S_NAME = "name";
        private static final String KEY_S_ADDRESS = "address";
        private static final String KEY_S_PHONE_NUMBER = "phone_number";
        private static final String KEY_S_TOTAL_FIGHTERS = "total_fighters";
        private static final String KEY_S_TOTAL_VEHICLES = "total_vehicles";
        private static final String KEY_S_LAT = "lat";
        private static final String KEY_S_LON = "long";

        private static final String KEY_TIP_TOPIC = "tip_topic";
        private static final String KEY_TIP_SUB_TOPIC = "tip_sub_topic";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
    }
}
