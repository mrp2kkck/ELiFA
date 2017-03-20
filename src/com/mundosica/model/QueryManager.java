package com.mundosica.model;

import java.util.HashMap;
import java.util.Vector;

import com.mundosica.database.DataBase;
import com.mundosica.utils.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QueryManager {
	
	static DataBase database = null;
	static SQLiteDatabase db;
	
	public static void closeDatabase() {
		if(database != null) {
			database.close();
		}
		if(db != null) {
			db.close();
		}
	}
	
	public static Cursor select(Context context, String tabla) {
		String query = "SELECT * FROM "+tabla+";";
		return selectByQuery(context,query);
	}
	
	public static Cursor selectByQuery(Context context, String query) {
		try {
			if(database == null) {
				database = DataBase.init(context);
			}
			db = database.getWritableDatabase();
			return db.rawQuery(query,null);
		} catch (Exception e ) {
			Helper.printError(context, e);
		}
		return null;
	}
	
	public static Cursor selectByID(Context context, String tabla,String id) {
		String primary = TableInfo.primaryKey(context, tabla);
		String query = "SELECT * FROM "+tabla+" WHERE " + primary +" = "+id;
		return selectByQuery(context,query);
	}

	public static boolean executeQuery(Context context,String query) {
		try {
			if(database == null) {
				database = DataBase.init(context);
			}
			db = database.getWritableDatabase();
			Helper.printError(context, query);
			db.execSQL(query);
			db.close();
			return true;
		} catch (Exception e) {
			Helper.printError(context,e);
			return false;
		}
	}
	public static boolean insert(Context context,String tabla,Vector<String> field
			,HashMap<String,String> value) {
		String values = "";
		String fields = "";
		int ultimo = field.size() - 1;
		if(ultimo <= -1) {
			Helper.printError(context, "error en QueryManager insert: la lista de campos de"
					+ " la tabla esta vacia: " + tabla);
			return false;
		}
		for(int i = 0; i < ultimo; i++) {
			fields += field.get(i) + ",";
			String valor = value.get(field.get(i));
			if(Helper.isNumeric(valor)) {
				values += valor + ",";
			} else {
				values += "'" + escaparApostrofes(valor) + "',";
			}
		}
		fields += field.get(ultimo);
		String valor = value.get(field.get(ultimo));
		if(Helper.isNumeric(valor)) {
			values += valor;
		} else {
			values += "'" + escaparApostrofes(valor) + "'";
		}
		String query = "INSERT INTO " + tabla + " (" + fields + " ) VALUES (" + values + ");";
		return executeQuery(context,query);
	}
	public static boolean delete(Context context,String tabla,String id) {
		String primary = TableInfo.primaryKey(context, tabla);
		String query = "DELETE FROM "+ tabla +" WHERE " + primary + " = " + id;
		return executeQuery(context,query);
	}
	public static boolean deleteByField(Context context,String tabla,String field,String value) {
		String query = "DELETE FROM "+ tabla +" WHERE "+field+" = " + value;
		return executeQuery(context,query);
	}
	
	public static boolean update(Context context,String tabla,Vector<String> field,
			HashMap<String,String> value,String id) {
		String set = stringSetUpdate(context,tabla,field,value);
		String primary = TableInfo.primaryKey(context, tabla);
		String query = "UPDATE " + tabla + " SET " + set + " WHERE " + primary + " = " + id;
		return executeQuery(context,query);
	}
	
	public static boolean updateByField(Context context,String tabla,Vector<String> field
			,HashMap<String,String> value,String campo,String valor) {
		String set = stringSetUpdate(context,tabla,field,value);
		String query = "UPDATE " + tabla + " SET " + set + " WHERE "+campo+" = " + valor;
		return executeQuery(context,query);
	}
	public static String stringSetUpdate(Context context,String tabla,Vector<String> field,
			HashMap<String,String> value) {
		String set = "";
		int ultimo = field.size() - 1;
		for(int i = 0; i < ultimo; i++) {
			if(Helper.isNumeric(value.get(field.get(i)))) {
				set += field.get(i) + "=" + value.get(field.get(i)) + "," ;
			} else {
				set += field.get(i) + "= '" + escaparApostrofes(value.get(field.get(i))) + "'," ;
			}
		}
		if(Helper.isNumeric(value.get(field.get(ultimo)))) {
			set += field.get(ultimo) + "=" + value.get(field.get(ultimo));
		} else {
			set += field.get(ultimo) + "= '" + escaparApostrofes(value.get(field.get(ultimo))) + "'";
		}
		return set;
	}
	
	public static String escaparApostrofes(String value) {
		if(value == null || value.length() <= 0) {
			return value;
		}
		return value.replace("'", "''");
	}
}
