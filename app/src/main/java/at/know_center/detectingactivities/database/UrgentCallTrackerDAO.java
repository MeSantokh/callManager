package at.know_center.detectingactivities.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.know_center.detectingactivities.model.Place;
import at.know_center.detectingactivities.model.UrgentCallTracker;

/**
 * Created by santokh on 13.05.15.
 */
public class UrgentCallTrackerDAO {


    static final String TABLE_URGENTCALLTRACKER = "urgentCallsTracker";
    static final String COLUMN_URGENTCALLTRACKER_PHONE_NR = "phone_nr";
    static final String COLUMN_URGENTCALLTRACKER_CALLS_COUNTER = "calls_counter";
    static final String COLUMN_URGENTCALLTRACKER_FIRST_TIMESTAMP = "first_timestamp";
    static final String COLUMN_URGENTCALLTRACKER_LAST_TIMESTAMP = "last_timestamp";
    static final String SQL_CREATE_TABLE_URGENTCALLTRACKER = "CREATE TABLE " + TABLE_URGENTCALLTRACKER + "("
            + COLUMN_URGENTCALLTRACKER_PHONE_NR + " STRING PRIMARY KEY, "
            + COLUMN_URGENTCALLTRACKER_CALLS_COUNTER + " INTEGER NOT NULL, "
            + COLUMN_URGENTCALLTRACKER_FIRST_TIMESTAMP + " TEXT NOT NULL, "
            + COLUMN_URGENTCALLTRACKER_LAST_TIMESTAMP + " TEXT NOT NULL " + ")";


    private static final String NAME = UrgentCallTrackerDAO.class.getSimpleName();
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private Context context;
    private String[] allColumns = {
            DBHelper.COLUMN_URGENTCALLTRACKER_PHONE_NR, // 0
            DBHelper.COLUMN_URGENTCALLTRACKER_CALLS_COUNTER, // 1
            DBHelper.COLUMN_URGENTCALLTRACKER_FIRST_TIMESTAMP, // 2
            DBHelper.COLUMN_URGENTCALLTRACKER_LAST_TIMESTAMP}; // 3

    public UrgentCallTrackerDAO(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        try {
            openDatabase();
        } catch (SQLException e) {

        }
    }

    public void openDatabase() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void closeDatabase() {
        dbHelper.close();
    }

    public UrgentCallTracker createUrgentCallTracker(String phoneNumber, Long timestamp) {
        UrgentCallTracker tempTracker = getUrgentCallTrackerByPhoneNumber(phoneNumber);
        if (tempTracker != null) {
            updateCallCounter(tempTracker, timestamp);
        }
        int callCounter = 1;
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_URGENTCALLTRACKER_PHONE_NR, phoneNumber);
        values.put(DBHelper.COLUMN_URGENTCALLTRACKER_CALLS_COUNTER, callCounter);
        // values.put(DBHelper.COLUMN_URGENTCALLTRACKER_FIRST_TIMESTAMP, String.valueOf(firstTimeStamp));
        // values.put(DBHelper.COLUMN_URGENTCALLTRACKER_LAST_TIMESTAMP, String.valueOf(lastTimeStamp));
        values.put(DBHelper.COLUMN_URGENTCALLTRACKER_FIRST_TIMESTAMP, Long.toString(timestamp));
        values.put(DBHelper.COLUMN_URGENTCALLTRACKER_LAST_TIMESTAMP, Long.toString(timestamp));
        long insertId = database.insert(DBHelper.TABLE_URGENTCALLTRACKER, null, values);
        Cursor cursor = database.query(DBHelper.TABLE_URGENTCALLTRACKER, allColumns,
                DBHelper.COLUMN_URGENTCALLTRACKER_PHONE_NR + " = " + phoneNumber, null, null,
                null, null);
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            Log.d(NAME, "insertion of UrgentCallTracker failed");
            return null;
        }
        cursor.moveToFirst();
        UrgentCallTracker tracker = cursorConvertToUrgentCallTracker(cursor);
        cursor.close();

        Log.d(NAME, "phoneNumber " + tracker.getPhoneNumber()
                + " callCounter" + tracker.getCallCounter()
                + " firstTimestamp" + tracker.getFirstTimestamp()
                + " secondTimestamp" + tracker.getLastTimestamp());


        return tracker;


    }

    public void deleteUrgentCallTracker(UrgentCallTracker urgentCallTracker) {
        String phoneNumber = urgentCallTracker.getPhoneNumber();
        database.delete(DBHelper.TABLE_URGENTCALLTRACKER, DBHelper.COLUMN_URGENTCALLTRACKER_PHONE_NR
                + " = " + phoneNumber, null);

    }

    public void updateCallCounter(UrgentCallTracker urgentCallTracker, Long newTimestamp) {
        ContentValues values = new ContentValues();
        int callsCounter = 1;
        Long firstTimeStamp = newTimestamp;
        Long lastTimeStamp = newTimestamp;
        if (urgentCallTracker.getCallCounter() == 2) {
            callsCounter = 2;
            firstTimeStamp = urgentCallTracker.getLastTimestamp();
        } else if (urgentCallTracker.getCallCounter() == 1) {
            firstTimeStamp = urgentCallTracker.getFirstTimestamp();
            callsCounter++;
        }


        values.put(DBHelper.COLUMN_URGENTCALLTRACKER_CALLS_COUNTER, callsCounter);
        values.put(DBHelper.COLUMN_URGENTCALLTRACKER_FIRST_TIMESTAMP, Long.toString(firstTimeStamp));
        values.put(DBHelper.COLUMN_URGENTCALLTRACKER_LAST_TIMESTAMP, Long.toString(lastTimeStamp));
        String phoneNumber = urgentCallTracker.getPhoneNumber();
        database.update(DBHelper.TABLE_URGENTCALLTRACKER, values, DBHelper.COLUMN_URGENTCALLTRACKER_PHONE_NR
                + " = " + phoneNumber, null);

    }


    public List<UrgentCallTracker> getAllUrgentCallTrackers() {
        List<UrgentCallTracker> urgentCallTrackers = new ArrayList<UrgentCallTracker>();

        Cursor cursor = database.query(DBHelper.TABLE_URGENTCALLTRACKER, allColumns,
                null, null, null, null, null);
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            UrgentCallTracker urgentCallTracker = cursorConvertToUrgentCallTracker(cursor);
            urgentCallTrackers.add(urgentCallTracker);
            cursor.moveToNext();
        }
        cursor.close();

        return urgentCallTrackers;
    }

    public UrgentCallTracker getUrgentCallTrackerByPhoneNumber(String phoneNumber) {
        Cursor cursor = database.query(DBHelper.TABLE_URGENTCALLTRACKER, allColumns,
                DBHelper.COLUMN_URGENTCALLTRACKER_PHONE_NR + " = ?",
                new String[]{phoneNumber}, null, null, null);
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            Log.d(NAME, "No UrgentCallTracker found with phoneNumber " + phoneNumber);
            return null;
        }
        cursor.moveToFirst();
        UrgentCallTracker urgentCallTracker = cursorConvertToUrgentCallTracker(cursor);

        Log.d(NAME, "phoneNumber " + urgentCallTracker.getPhoneNumber()
                + " callCounter" + urgentCallTracker.getCallCounter()
                + " firstTimestamp" + urgentCallTracker.getFirstTimestamp()
                + " secondTimestamp" + urgentCallTracker.getLastTimestamp());


        return urgentCallTracker;

    }


    protected UrgentCallTracker cursorConvertToUrgentCallTracker(Cursor cursor) {
        UrgentCallTracker urgentCallTracker = new UrgentCallTracker();
        int index0 = cursor.getColumnIndex(DBHelper.COLUMN_URGENTCALLTRACKER_PHONE_NR);
        int index1 = cursor.getColumnIndex(DBHelper.COLUMN_URGENTCALLTRACKER_CALLS_COUNTER);
        int index2 = cursor.getColumnIndex(DBHelper.COLUMN_URGENTCALLTRACKER_FIRST_TIMESTAMP);
        int index3 = cursor.getColumnIndex(DBHelper.COLUMN_URGENTCALLTRACKER_LAST_TIMESTAMP);
        urgentCallTracker.setPhoneNumber(cursor.getString(index0));
        urgentCallTracker.setCallCounter(cursor.getInt(index1));
        urgentCallTracker.setFirstTimestamp(Long.valueOf(cursor.getString(index2)));
        urgentCallTracker.setLastTimestamp(Long.valueOf(cursor.getString(index3)));


        return urgentCallTracker;

    }

}



