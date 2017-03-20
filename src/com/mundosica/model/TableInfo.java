package com.mundosica.model;

import java.util.Vector;

import com.mundosica.utils.Helper;

import android.content.Context;
import android.database.Cursor;

public class TableInfo {

	public static Vector<ColumnMetadata> info(Context context,String tabla) {
		Vector<ColumnMetadata> columns = new Vector<ColumnMetadata>();
		Cursor c = QueryManager.selectByQuery(context, "PRAGMA table_info ('"+tabla+"');");
		try {
			if(c.moveToFirst()) {
				do {
					ColumnMetadata cm = new ColumnMetadata(
						c.getInt(0),
						c.getString(1),
						c.getString(2),
						c.getString(3),
						c.getString(5)
					);
					columns.add(cm);
				} while(c.moveToNext());
			}
		} catch(Exception e) {
			Helper.printError(context, e);
		}
		c.close();
		QueryManager.closeDatabase();
		return columns;
	}
	
	public static String primaryKey(Context context,String tabla) {
		 Vector<ColumnMetadata> info = info(context, tabla);
		 for (int i = 0; i < info.size(); i++) {
			 if(info.get(i).isPrimary) {
				 return info.get(i).name;
			 }
		 }
		return "id";
	}
}
