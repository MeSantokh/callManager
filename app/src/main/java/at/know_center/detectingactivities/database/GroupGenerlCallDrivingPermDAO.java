package at.know_center.detectingactivities.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.know_center.detectingactivities.model.GroupWithPermission;

/**
 * Created by santokh on 13.05.15.
 */
public class GroupGenerlCallDrivingPermDAO {

    static final String TABLE_GENERALDRIVINGCALLGROUPSPERM = "generalDrvingCallGroups";
    static final String COLUMN_GENERALDRIVINGCALLGROUPSPERM_GROUP_ID = "group_id";
    static final String COLUMN_GENERALDRIVINGCALLGROUPSPERM_PERMISSION = "permission";

    private static final String NAME = GroupGenerlCallDrivingPermDAO.class.getSimpleName();
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private Context context;
    private String[] allColumns = {
            DBHelper.COLUMN_GENERALDRIVINGCALLGROUPSPERM_GROUP_ID, // 0
            DBHelper.COLUMN_GENERALDRIVINGCALLGROUPSPERM_PERMISSION}; // 1

    public GroupGenerlCallDrivingPermDAO(Context context) {
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

    public GroupWithPermission createGroupWithPermission(int groupId, Boolean permission) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_GENERALDRIVINGCALLGROUPSPERM_GROUP_ID, groupId);
        values.put(DBHelper.COLUMN_GENERALDRIVINGCALLGROUPSPERM_PERMISSION, permission);
        long insertId = database.insert(DBHelper.TABLE_GENERALDRIVINGCALLGROUPSPERM, null, values);
        Cursor cursor = database.query(DBHelper.TABLE_GENERALDRIVINGCALLGROUPSPERM, allColumns,
                DBHelper.COLUMN_GENERALDRIVINGCALLGROUPSPERM_GROUP_ID + " = " + groupId, null, null,
                null, null);
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            Log.d(NAME, "Creating GroupGeneralCallDrivingPerm has failed for groupId " + groupId);
            return null;
        }
        cursor.moveToFirst();
        GroupWithPermission permObj = cursorConvertPermissionObj(cursor);
        cursor.close();
        return permObj;
    }

    public void deleteGroupWithPermission(GroupWithPermission groupCallDrivingPermission) {
        int id = groupCallDrivingPermission.getGroupId();
        database.delete(DBHelper.TABLE_GENERALDRIVINGCALLGROUPSPERM, DBHelper.COLUMN_GENERALDRIVINGCALLGROUPSPERM_GROUP_ID
                + " = " + id, null);
    }

    public List<GroupWithPermission> getAllGrouGeneralCallPermObjects() {
        List<GroupWithPermission> listPlaces = new ArrayList<GroupWithPermission>();

        Cursor cursor = database.query(DBHelper.TABLE_GENERALDRIVINGCALLGROUPSPERM, allColumns,
                null, null, null, null, null);
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            Log.d(NAME, "No GroupGeneralCallDrivingPerm objects found");
            return null;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GroupWithPermission permObj = cursorConvertPermissionObj(cursor);
            listPlaces.add(permObj);
            cursor.moveToNext();
        }
        cursor.close();

        return listPlaces;
    }

    public GroupWithPermission getGroupWithPermObj(Integer groupId) {
        Cursor cursor = database.query(DBHelper.SQL_CREATE_TABLE_PLACES, allColumns,
                DBHelper.COLUMN_GENERALDRIVINGCALLGROUPSPERM_GROUP_ID + " = ?",
                new String[]{String.valueOf(groupId)}, null, null, null);
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            Log.d(NAME, "No GroupGeneralCallDrivingPerm object found with groupId " + groupId);
            return null;
        }
        cursor.moveToFirst();

        GroupWithPermission permObj = cursorConvertPermissionObj(cursor);
        return permObj;
    }

    protected GroupWithPermission cursorConvertPermissionObj(Cursor cursor) {
        GroupWithPermission permObj = new GroupWithPermission();
        int index0 = cursor.getColumnIndex(DBHelper.COLUMN_GENERALDRIVINGCALLGROUPSPERM_GROUP_ID);
        int index1 = cursor.getColumnIndex(DBHelper.COLUMN_GENERALDRIVINGCALLGROUPSPERM_PERMISSION);
        permObj.setGroupId(cursor.getInt(index0));
        permObj.setPermission(cursor.getInt(index1) == 1);
        return permObj;
    }

}



