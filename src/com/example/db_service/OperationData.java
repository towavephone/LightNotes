package com.example.db_service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OperationData implements OperationInterface {
    private DBOpenHelper helper = null;
    private int DBVersion = 0;

    public OperationData(Context context) {
	helper = new DBOpenHelper(context);
    }

    public int getVersion() {
	return DBVersion;
    }

    @Override
    public boolean addData(String database_name, ContentValues values) {
	// TODO Auto-generated method stub
	boolean flag = false;
	SQLiteDatabase database = null;
	long id = -1;
	try {
	    database = helper.getWritableDatabase();
	    id = database.insert(database_name, null, values);
	    flag = (id != -1 ? true : false);
	} catch (Exception e) {
	    // TODO: handle exception
	} finally {
	    if (database != null) {
		database.close();
	    }
	}
	return flag;
    }

    @Override
    public boolean deleteData(String database_name, String whereClause,
	    String[] whereArgs) {
	// TODO Auto-generated method stub
	boolean flag = false;
	SQLiteDatabase database = null;
	int count = 0;
	try {
	    database = helper.getWritableDatabase();
	    count = database.delete(database_name, whereClause, whereArgs);
	    flag = (count > 0 ? true : false);
	} catch (Exception e) {
	    // TODO: handle exception
	} finally {
	    if (database != null) {
		database.close();
	    }
	}
	return flag;
    }

    @Override
    public boolean updateData(String database_name, ContentValues values,
	    String whereClause, String[] whereArgs) {
	boolean flag = false;
	SQLiteDatabase database = null;
	int count = 0;// 影响数据库的行数
	try {
	    database = helper.getWritableDatabase();
	    count = database.update(database_name, values, whereClause,
		    whereArgs);
	    flag = (count > 0 ? true : false);
	} catch (Exception e) {
	    // TODO: handle exception
	} finally {
	    if (database != null) {
		database.close();
	    }
	}
	return flag;
    }

    @Override
    public HashMap<String, Object> viewData(String database_name,
	    String selection, String[] selectionArgs) {
	// TODO Auto-generated method stub
	SQLiteDatabase database = null;
	Cursor cursor = null;
	HashMap<String, Object> map = new HashMap<String, Object>();
	try {
	    database = helper.getReadableDatabase();
	    cursor = database.query(true, database_name, null, selection,
		    selectionArgs, null, null, null, null);
	    int cols_len = cursor.getColumnCount();
	    while (cursor.moveToNext()) {
		for (int i = 0; i < cols_len; i++) {
		    String cols_name = cursor.getColumnName(i);
		    Object cols_value;
		    if (cols_name.equals("NoteContent")) {
			cols_value = cursor.getBlob(cursor
				.getColumnIndex(cols_name));
		    } else if (cols_name.equals("NoteClassification")) {
			cols_value = cursor.getInt(cursor
				.getColumnIndex(cols_name));
		    } else {
			cols_value = cursor.getString(cursor
				.getColumnIndex(cols_name));
		    }
		    if (cols_value == null) {
			cols_value = "";
		    }
		    map.put(cols_name, cols_value);
		}
	    }
	} catch (Exception e) {
	    // TODO: handle exception
	    e.printStackTrace();
	} finally {
	    if (database != null) {
		database.close();
	    }
	}
	return map;
    }

    @Override
    public ArrayList<HashMap<String, Object>> viewDatas(String database_name,
	    String selection, String[] selectionArgs) {
	// TODO Auto-generated method stub
	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	SQLiteDatabase database = null;
	Cursor cursor = null;
	try {
	    database = helper.getReadableDatabase();
	    cursor = database.query(false, database_name, null, selection,
		    selectionArgs, null, null, null, null);
	    int cols_len = cursor.getColumnCount();
	    while (cursor.moveToNext()) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < cols_len; i++) {
		    String cols_name = cursor.getColumnName(i);
		    Object cols_value;
		    if (cols_name.equals("NoteContent")) {
			cols_value = cursor.getBlob(cursor
				.getColumnIndex(cols_name));
		    } else if (cols_name.equals("NoteClassification")) {
			cols_value = cursor.getInt(cursor
				.getColumnIndex(cols_name));
		    } else {
			cols_value = cursor.getString(cursor
				.getColumnIndex(cols_name));
		    }
		    if (cols_value == null) {
			cols_value = "";
		    }
		    map.put(cols_name, cols_value);
		}
		list.add((HashMap<String, Object>) map);
	    }
	} catch (Exception e) {
	    // TODO: handle exception
	    e.printStackTrace();
	} finally {
	    if (database != null) {
		database.close();
	    }
	}
	return list;
    }
}
