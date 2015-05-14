package at.know_center.detectingactivities.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.know_center.detectingactivities.model.GroupWithPermission;

/**
 * Created by santokh on 13.05.15.
 */
public class GroupUrgentCallDrivingPermDAO {

    private static final String NAME = GroupUrgentCallDrivingPermDAO.class.getSimpleName();
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private Context context;
    private String[] allColumns = {
            DBHelper.COLUMN_URGENTDRIVINGCALLGROUPSPERM_GROUP_ID, // 0
            DBHelper.COLUMN_URGENTDRIVINGCALLGROUPSPERM_PERMISSION }; // 1

    public GroupUrgentCallDrivingPermDAO(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        try {
            openDatabase();
        } catch (SQLException e) {

        }
    }

    public void openDatabase() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void closeDatabase() {
        dbHelper.close();
    }

    public GroupWithPermission createCompany(int groupId, Boolean permission) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_URGENTDRIVINGCALLGROUPSPERM_GROUP_ID, groupId);
        values.put(DBHelper.COLUMN_URGENTDRIVINGCALLGROUPSPERM_PERMISSION, permission);
        long insertId = database.insert(DBHelper.TABLE_URGENTDRIVINGCALLGROUPSPERM, null, values);
        Cursor cursor = database.query(DBHelper.TABLE_URGENTDRIVINGCALLGROUPSPERM, allColumns,
                DBHelper.COLUMN_URGENTDRIVINGCALLGROUPSPERM_GROUP_ID + " = " + groupId, null, null,
                null, null);
        cursor.moveToFirst();
        GroupWithPermission permObj = cursorConvertPermissionObj(cursor);
        cursor.close();
        return permObj;
    }

    public void deleteCompany(GroupWithPermission groupCallDrivingPermission) {
        int id = groupCallDrivingPermission.getGroupId();
        database.delete(DBHelper.TABLE_URGENTDRIVINGCALLGROUPSPERM, DBHelper.COLUMN_URGENTDRIVINGCALLGROUPSPERM_GROUP_ID
                + " = " + id, null);
    }

    public List<GroupWithPermission> getAllGroupGeneralCallPermObjects() {
        List<GroupWithPermission> listPlaces = new ArrayList<GroupWithPermission>();

        Cursor cursor = database.query(DBHelper.TABLE_URGENTDRIVINGCALLGROUPSPERM, allColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                GroupWithPermission permObj = cursorConvertPermissionObj(cursor);
                listPlaces.add(permObj);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listPlaces;
    }

    public GroupWithPermission getPlaceByName(String placeName) {
        Cursor cursor = database.query(DBHelper.SQL_CREATE_TABLE_PLACES, allColumns,
                DBHelper.COLUMN_PLACES_NAME + " = ?",
                new String[] { placeName }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        GroupWithPermission permObj = cursorConvertPermissionObj(cursor);
        return permObj;
    }

    protected GroupWithPermission cursorConvertPermissionObj(Cursor cursor) {
        GroupWithPermission permObj = new GroupWithPermission();
        int index0 = cursor.getColumnIndex( DBHelper.COLUMN_URGENTDRIVINGCALLGROUPSPERM_GROUP_ID);
        int index1 = cursor.getColumnIndex( DBHelper.COLUMN_URGENTDRIVINGCALLGROUPSPERM_PERMISSION);
        permObj.setGroupId(cursor.getInt(index0));
        permObj.setPermission(cursor.getInt(index1) == 1);
        return permObj;
    }

}



