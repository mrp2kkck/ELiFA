package com.mundosica.model;

import android.content.Context;
import android.database.Cursor;

public class Config {

	public static void customField(Context vista,String  field, String value) {
		String query = "";
		String valor = customField(vista,field);
		if (valor == null) {
			query = "INSERT INTO configuraciones (nombre,valor) "
					+ "VALUES ('"+field+"','"+value+"')";
		} else {
			query = "UPDATE configuraciones SET valor = '"+value + "' WHERE nombre = '"+field+"'";
		}
		QueryManager.executeQuery(vista, query);
	}
	public static String customField(Context vista,String  field) {
		String valor = null;
		String query = "SELECT valor FROM configuraciones WHERE nombre = '"+field+"'";
		Cursor c = QueryManager.selectByQuery(vista, query);
		if(c.moveToFirst()) {
			valor =  c.getString(0);
		}
		c.close();
		QueryManager.closeDatabase();
		return valor;
	}
}
