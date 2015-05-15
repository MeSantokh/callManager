package at.know_center.detectingactivities.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.know_center.detectingactivities.model.Place;
import at.know_center.detectingactivities.model.UrgentCallTracker;

/**
 * Created by santokh on 13.05.15.
 */
public class PlacesDAO {

    private static final String NAME = PlacesDAO.class.getSimpleName();
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private Context context;
    private String[] allColumns = {
            DBHelper.COLUMN_PLACES_ID, // 0
            DBHelper.COLUMN_PLACES_NAME, // 1
            DBHelper.COLUMN_PLACES_STREET_NAME, // 2
            DBHelper.COLUMN_PLACES_STREET_NUMBER, // 3
            DBHelper.COLUMN_PLACES_CITY_NAME, // 4
            DBHelper.COLUMN_PLACES_PERMITTED_GROUPS_IDS}; // 5

    public PlacesDAO(Context context) {
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

    public Place createPlace(String name, String street, int streetNumber, String cityName,
                             ArrayList<Integer> groupPermissions) {
        String permissions = TextUtils.join(",", groupPermissions);
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_PLACES_NAME, name);
        values.put(DBHelper.COLUMN_PLACES_STREET_NAME, street);
        values.put(DBHelper.COLUMN_PLACES_STREET_NUMBER, streetNumber);
        values.put(DBHelper.COLUMN_PLACES_CITY_NAME, cityName);
        values.put(DBHelper.COLUMN_PLACES_PERMITTED_GROUPS_IDS, permissions);
        long insertId = database.insert(DBHelper.TABLE_PLACES, null, values);
        Cursor cursor = database.query(DBHelper.TABLE_PLACES, allColumns,
                DBHelper.COLUMN_PLACES_ID + " = " + insertId, null, null,
                null, null);
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            Log.d(NAME, "insertion of place failed");
            return null;
        }
        cursor.moveToFirst();
        Place place = cursorConvertToPlace(cursor);

        Log.d(NAME, "placeName " + place.getName()
                + " steetName" + place.getStreetName()
                + " StreetNr" + place.getStreetNumber()
                + " cityName" + place.getCityName()
                + " permSize" + place.getGroupPermissions().size());


        cursor.close();
        return place;
    }

    public void deletePlace(Place place) {
        long id = place.getId();
        database.delete(DBHelper.TABLE_PLACES, DBHelper.COLUMN_PLACES_ID
                + " = " + id, null);
       Log.d(NAME, "the deleted place has the placeName: " + place);
    }

    public void updatePlace(Place oldPlace, Place updatePlace) {

        String permissions = TextUtils.join(",", updatePlace.getGroupPermissions());
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_PLACES_NAME, updatePlace.getName());
        values.put(DBHelper.COLUMN_PLACES_STREET_NAME, updatePlace.getStreetName());
        values.put(DBHelper.COLUMN_PLACES_STREET_NUMBER, updatePlace.getStreetNumber());
        values.put(DBHelper.COLUMN_PLACES_CITY_NAME, updatePlace.getCityName());
        values.put(DBHelper.COLUMN_PLACES_PERMITTED_GROUPS_IDS, permissions);
        database.update(DBHelper.TABLE_PLACES, values, DBHelper.COLUMN_PLACES_ID
                + " = " + oldPlace.getId(), null);

    }

    public List<Place> getAllPlaces() {
        List<Place> listPlaces = new ArrayList<Place>();

        Cursor cursor = database.query(DBHelper.TABLE_PLACES, allColumns,
                null, null, null, null, null);
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            Log.d(NAME, "No places found");
            return null;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Place place = cursorConvertToPlace(cursor);
            listPlaces.add(place);
            cursor.moveToNext();
        }
        cursor.close();

        return listPlaces;
    }

    public Place getPlaceByName(String placeName) {
        Cursor cursor = database.query(DBHelper.TABLE_PLACES, allColumns,
                DBHelper.COLUMN_PLACES_NAME + " = ?",
                new String[]{placeName}, null, null, null);
        if (cursor != null) {
            Log.d(NAME, "geting place failed, placeNam " + placeName);
            cursor.moveToFirst();
        }
        Place place = cursorConvertToPlace(cursor);

        Log.d(NAME, "placeName " + place.getName()
                + " steetName" + place.getStreetName()
                + " StreetNr" + place.getStreetNumber()
                + " cityName" + place.getCityName()
                + " permSize" + place.getGroupPermissions().size());

        return place;
    }

    protected Place cursorConvertToPlace(Cursor cursor) {
        Place place = new Place();
        int index0 = cursor.getColumnIndex(DBHelper.COLUMN_PLACES_ID);
        int index1 = cursor.getColumnIndex(DBHelper.COLUMN_PLACES_NAME);
        int index2 = cursor.getColumnIndex(DBHelper.COLUMN_PLACES_STREET_NAME);
        int index3 = cursor.getColumnIndex(DBHelper.COLUMN_PLACES_STREET_NUMBER);
        int index4 = cursor.getColumnIndex(DBHelper.COLUMN_PLACES_CITY_NAME);
        int index5 = cursor.getColumnIndex(DBHelper.COLUMN_PLACES_PERMITTED_GROUPS_IDS);
        place.setId(cursor.getLong(index0));
        place.setName(cursor.getString(index1));
        place.setStreetName(cursor.getString(index2));
        place.setStreetNumber(cursor.getInt(index3));
        place.setCityName(cursor.getString(index4));
        String permission = cursor.getString(index5);
        ArrayList<Integer> groupPermissions = new ArrayList<Integer>();
        for (String s : permission.split(",")) {
            groupPermissions.add(Integer.parseInt(s.trim()));
        }
        place.setGroupPermissions(groupPermissions);
        return place;
    }

}



