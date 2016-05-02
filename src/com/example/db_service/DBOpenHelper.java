package com.example.db_service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static String name = "LightNote.db";// 表示数据库的名称
    private static int version = 1;// 表示数据库的版本号码

    public DBOpenHelper(Context context) {
	super(context, name, null, version);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	// TODO Auto-generated method stub
	db.execSQL("create table if not exists tb_notes(_id integer primary key autoincrement,NoteClassification integer default 0,NoteTypeID varchar(5),NoteTitle varchar(300),NoteContent blob,CreateTime varchar(30),ImagePath varchar(100))");
//	db.execSQL("create table if not exists tb_notedatas(_id integer primary key ,NoteClassification integer,NoteContent blob)");
//	db.execSQL("create table if not exists tb_note_datas(_id integer primary key autoincrement,NoteID integer,NoteDataID integer)");
	db.execSQL("create table if not exists tb_notetags(_id integer primary key autoincrement,NoteTagname varchar(100),CreateTime varchar(30))");
	db.execSQL("create table if not exists tb_note_tags(_id integer primary key autoincrement,NoteID varchar(5),NoteTagID varchar(5))");
	db.execSQL("create table if not exists tb_notetypes(_id integer primary key autoincrement,NoteTypename varchar(100),CreateTime varchar(30))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// TODO Auto-generated method stub
	db.execSQL("drop table if exists tb_notes");
//	db.execSQL("drop table if exists tb_notedatas");
//	db.execSQL("drop table if exists tb_note_datas");
	db.execSQL("drop table if exists tb_notetags");
	db.execSQL("drop table if exists tb_note_tags");
	db.execSQL("drop table if exists tb_notetypes");
	onCreate(db);
    }
}
