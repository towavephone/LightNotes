package com.example.db_service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;

public interface OperationInterface {
    public boolean addData(String database_name, ContentValues values);

    public boolean deleteData(String database_name, String whereClause,
	    String[] whereArgs);

    public boolean updateData(String database_name, ContentValues values,
	    String whereClause, String[] whereArgs);

    public HashMap<String, Object> viewData(String database_name, String selection,
	    String[] selectionArgs);

    public ArrayList<HashMap<String, Object>> viewDatas(String database_name,
	    String selection, String[] selectionArgs);
}
