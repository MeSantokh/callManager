package at.know_center.detectingactivities.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by santokh on 13.05.15.
 */
public class DBHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "callsManagerDB";
    static final int DATABASE_VERSION = 0;

    static final String TABLE_PLACES = "places";
    static final String COLUMN_PLACES_ID = "id";
    static final String COLUMN_PLACES_NAME = "name";
    static final String COLUMN_PLACES_STREET_NAME = "street_name";
    static final String COLUMN_PLACES_STREET_NUMBER = "street_number";
    static final String COLUMN_PLACES_CITY_NAME = "city_name";
    static final String COLUMN_PLACES_PERMITTED_GROUPS_IDS = "permitted_group_ids";
    static final String SQL_CREATE_TABLE_PLACES =  "CREATE TABLE " + TABLE_PLACES + "("
            + COLUMN_PLACES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PLACES_NAME + " TEXT NOT NULL, "
            + COLUMN_PLACES_STREET_NAME + " TEXT NOT NULL, "
            + COLUMN_PLACES_STREET_NUMBER + " INTEGER NOT NULL, "
            + COLUMN_PLACES_CITY_NAME + " TEXT NOT NULL, "
            + COLUMN_PLACES_PERMITTED_GROUPS_IDS + " TEXT NOT NULL " + ")";


    static final String TABLE_PLACESPERM = "placesPermissions";
    static final String COLUMN_PLACESPERM_PLACE_ID = "place_id";
    static final String COLUMN_PLACESPERM_GROUP_ID = "group_id";
    static final String SQL_CREATE_TABLE_PLACESPERM =  "CREATE TABLE " + TABLE_PLACESPERM + "("
            + COLUMN_PLACESPERM_PLACE_ID + " INTEGER NOT NULL, "
            + COLUMN_PLACESPERM_GROUP_ID + " INTEGER NOT NULL " + ")";



    static final String TABLE_URGENTCALLTRACKER = "urgentCallsTracker";
    static final String COLUMN_URGENTCALLTRACKER_PHONE_NR = "phone_nr";
    static final String COLUMN_URGENTCALLTRACKER_CALLS_COUNTER = "calls_counter";
    static final String COLUMN_URGENTCALLTRACKER_FIRST_TIMESTAMP = "first_timestamp";
    static final String COLUMN_URGENTCALLTRACKER_LAST_TIMESTAMP = "last_timestamp";
    static final String SQL_CREATE_TABLE_URGENTCALLTRACKER =  "CREATE TABLE " + TABLE_URGENTCALLTRACKER + "("
            + COLUMN_URGENTCALLTRACKER_PHONE_NR + " STRING PRIMARY KEY, "
            + COLUMN_URGENTCALLTRACKER_CALLS_COUNTER + " INTEGER NOT NULL, "
            + COLUMN_URGENTCALLTRACKER_FIRST_TIMESTAMP + " TEXT NOT NULL, "
            + COLUMN_URGENTCALLTRACKER_LAST_TIMESTAMP + " TEXT NOT NULL " + ")";


    static final String TABLE_URGENTDRIVINGCALLGROUPSPERM = "urgentDrvingCallGroups";
    static final String COLUMN_URGENTDRIVINGCALLGROUPSPERM_GROUP_ID = "group_id";
    static final String COLUMN_URGENTDRIVINGCALLGROUPSPERM_PERMISSION = "permission";
    static final String SQL_CREATE_TABLE_URGENTDRIVINGCALLGROUPSPERM =  "CREATE TABLE " + TABLE_URGENTDRIVINGCALLGROUPSPERM + "("
            + COLUMN_URGENTDRIVINGCALLGROUPSPERM_GROUP_ID + " INTEGER NOT NULL, "
            + COLUMN_URGENTDRIVINGCALLGROUPSPERM_PERMISSION + " BOOLEAN NOT NULL " + ")";


    static final String TABLE_GENERALDRIVINGCALLGROUPSPERM = "generalDrvingCallGroups";
    static final String COLUMN_GENERALDRIVINGCALLGROUPSPERM_GROUP_ID = "group_id";
    static final String COLUMN_GENERALDRIVINGCALLGROUPSPERM_PERMISSION = "permission";
    static final String SQL_CREATE_TABLE_GENERALDRIVINGCALLGROUPSPERM =  "CREATE TABLE " + TABLE_GENERALDRIVINGCALLGROUPSPERM + "("
            + COLUMN_GENERALDRIVINGCALLGROUPSPERM_GROUP_ID + " INTEGER NOT NULL, "
            + COLUMN_GENERALDRIVINGCALLGROUPSPERM_PERMISSION + " BOOLEAN NOT NULL " + ")";

    private Context context;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PLACES);
        db.execSQL(SQL_CREATE_TABLE_PLACESPERM);
        db.execSQL(SQL_CREATE_TABLE_URGENTCALLTRACKER);
        db.execSQL(SQL_CREATE_TABLE_GENERALDRIVINGCALLGROUPSPERM);
        db.execSQL(SQL_CREATE_TABLE_URGENTDRIVINGCALLGROUPSPERM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String dropTableQuery = "DROP TABLE IF EXISTS";
        db.execSQL(dropTableQuery + " " + TABLE_PLACES);
        db.execSQL(dropTableQuery + " " + TABLE_PLACESPERM);
        db.execSQL(dropTableQuery + " " + TABLE_URGENTCALLTRACKER);
        db.execSQL(dropTableQuery + " " + TABLE_GENERALDRIVINGCALLGROUPSPERM);
        db.execSQL(dropTableQuery + " " + TABLE_URGENTDRIVINGCALLGROUPSPERM);
        onCreate(db);
    }
}
