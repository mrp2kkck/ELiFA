package com.mundosica.database;

import java.util.Vector;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public  class DataBase extends SQLiteOpenHelper {
	
	private static String name = "database.db";
	private static String version = "1";
	private static Vector<String> tablas = new Vector<String>();
	private static Vector<String> inserts = new Vector<String>();
	private static DataBase db = null;
	
	private DataBase(Context context) {
		//super(context, name, null, Integer.parseInt(version));
		super(context, name, null, Integer.parseInt(version));
	}
	
	public static DataBase init(Context context) {
		if(db == null) {
			db =  new DataBase(context);
		}
		return db;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		///*
		createTable(
			"configuraciones",
			"id INTEGER primary key autoincrement,nombre TEXT,valor TEXT"
		);
		int numTablas = tablas.size();
		for (int i = 0; i < numTablas; i++ ) {
			db.execSQL(tablas.get(i));
		}
		
		int numInserts = inserts.size();
		for (int i = 0; i < numInserts; i++ ) {
			db.execSQL(inserts.get(i));
		}
		////*/
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	public static void createTable(String nombre, String values) {
		tablas.add("CREATE TABLE " + nombre + "(" + values + ");");
	}
	
	public static  void insertInto(String tabla, String fields,String values) {
		inserts.add("INSERT INTO " + tabla + "(" + fields + ") VALUES " + "(" + values + ");");
	}

}
