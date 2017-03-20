package com.mundosica.model;

import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mundosica.utils.Helper;

import android.content.Context;
import android.database.Cursor;

public class Model {

	public Context context;
	public String table_name;
	HashMap <String,String> values = new HashMap<String,String>();
	public String primaryKey = null;
	public Model(Context context) {
		this.context = context;
	}
	public void tableName (String tn) {
		this.table_name = tn;
	}
	public Vector<ColumnMetadata> columnasMetadata() {
		return TableInfo.info(context, this.table_name);
	}
	public Vector<String> nombreColumnas() {
		Vector<ColumnMetadata> info = this.columnasMetadata();
		int size = info.size();
		Vector<String> field = new Vector<String> ();
		for (int i = 0; i < size; i ++) {
			field.add( info.get(i).name);
		}
		return field;
	}
	public Vector<String> nombreColumnasSinId() {
		Vector<String> field = nombreColumnas();
		field.remove(primaryKey);
		return  field;
	}
	private boolean obtenerValorPorTipo(Vector<ColumnMetadata> data,Cursor c) {
		boolean exito = false;
		if(c.moveToFirst()) {
			for (int i = 0; i < data.size(); i++) {
				String field = data.get(i).name;
				if(data.get(i).type.toLowerCase().equals("integer")) {
					values.put(field, c.getInt(data.get(i).cid) + "");
				}
				if(data.get(i).type.toLowerCase().equals("text")) {
					values.put(field, c.getString(data.get(i).cid));
				}
				if(data.get(i).type.toLowerCase().equals("real")) {
					values.put(field, c.getFloat(data.get(i).cid) + "");
				}
			}
			exito =  true;
		} 
		c.close();
		QueryManager.closeDatabase();
		return exito;
	}
	public boolean load(String id) {
		Vector<ColumnMetadata> data = columnasMetadata();
		this.primaryKey = TableInfo.primaryKey(context, this.table_name);
		Cursor c = QueryManager.selectByID(this.context, this.table_name, id);
		return obtenerValorPorTipo(data,c);
	}
	
	public boolean loadByQuery(String query) {
		Vector<ColumnMetadata> data = TableInfo.info(context, this.table_name);
		Cursor c = QueryManager.selectByQuery(this.context, query);
		return obtenerValorPorTipo(data,c);
	}
	
	public String get(String field) {
		return values.get(field);
	}
	
	public void set(String... namesAndValues) {
		if (namesAndValues.length % 2 == 1) {
			throw new IllegalArgumentException("The number of arguments must be pair.");
		}
		for (int i = 0; i < namesAndValues.length - 1; i += 2) {
			if (namesAndValues[i] != null && namesAndValues[i + 1] != null) {
				this.values.put(namesAndValues[i].toString(), namesAndValues[i + 1]);
			}
		}
	}
	
	public boolean save() {
		if(this.yaExiste()) {
			return this.update();
		}
		if(this.get(primaryKey) == null || this.get(primaryKey).equals("-1") 
		|| this.get(primaryKey).equals("NULL") || this.get(primaryKey).equals("null")) {
			return this.insert();
		}
		return this.insertConId();
	}
	
	public boolean insertConId() {
		boolean exito = false;
		if(beforeInsert()) {
			exito = QueryManager.insert(
					this.context, this.table_name, 
					this.nombreColumnas(), this.values
			);
		}
		if(exito) {
			return afterInsert();
		}
		return false;
	}
	
	public boolean insert() {
		boolean exito = false;
		if(beforeInsert()) {
			exito = QueryManager.insert(
					this.context, this.table_name, 
					this.nombreColumnasSinId(), this.values
			);
		}
		if(exito) {
			return afterInsert();
		}
		return false;
	}
	
	public boolean update() {
		boolean exito = false;
		if(beforeInsert()) {
			if(!this.yaExiste()) {
				return false;
			}
			String id = this.get(primaryKey);
			if(!Helper.isNumeric(id)) {
				id = "'"+id+"'";
			}
			exito =  QueryManager.update(
					this.context, this.table_name, 
					this.nombreColumnasSinId(), this.values,id);
		}
		if(exito) {
			return afterInsert();
		}
		return false;
	}
	
	public boolean updateByField(String campo, String valor) {
		return QueryManager.updateByField(this.context, this.table_name, 
				this.nombreColumnasSinId(), this.values, campo, valor
		);
	}
	public boolean delete() {
		return QueryManager.delete(context, table_name, this.get(primaryKey));
	}
	public boolean deleteByField(String field,String value) {
		return QueryManager.deleteByField(context, table_name, field, value);
	}
	public boolean yaExiste() {
		String id = this.get(primaryKey);
		if(!Helper.isNumeric(id)) {
			id = "'"+id+"'";
		}
		return yaExiste(id);
	}
	public boolean yaExiste(String id) {
		Cursor c = QueryManager.selectByID(this.context, this.table_name, id);
		if(c == null) {
			return false;
		}
		boolean first = c.moveToFirst();
		c.close();
		QueryManager.closeDatabase();
		return first;
	}
	public String ultimaInsercion() {
		String query = "SELECT "+ primaryKey +" FROM "+ this.table_name;
		Cursor c = QueryManager.selectByQuery(this.context, query);
		if(c.moveToLast()) {
			String ultimaInser = c.getString(0);
			c.close();
			QueryManager.closeDatabase();
			return ultimaInser;
		} 
		return null;
	}
	
	public void saveByJsonArray(JSONArray jarray) throws Exception {
		for(int i = 0; i < jarray.length(); i++) {
			this.saveByJson(jarray.getJSONObject(i));
		}
	}
	
	public boolean saveByJson(JSONObject json) throws Exception {
		Vector<String> columnas = this.nombreColumnas();
		for (int i = 0;i < columnas.size(); i++) {
			String columna = columnas.get(i);
			String valor = json.get(columna).toString();
			valor = valor.replaceAll("false", "0");
			valor = valor.replaceAll("true", "1");
			this.set(columna,valor);
		}
		if(this.yaExiste()) {
			return this.update();
		}
		return this.insertConId();
	}
	public String fieldsJson() {
		String json = "{";
		Vector<String> columnas = this.nombreColumnas();
		int ultimo = columnas.size() -1;
		for (int i = 0; i < ultimo; i ++) {
			String field = columnas.get(i);
			String value = this.get(field);
			if(value == null) {
				continue;
			}
			if(Helper.isNumeric(value) ) {
				json += "\"" + field + "\":"+value+",";
			} else {
				json += "\"" + field + "\":\""+value+"\",";
			}
		}
		String field = columnas.get(ultimo);
		String value = this.get(field);
		if(value != null) {
			if(Helper.isNumeric(value)) {
				json += "\"" + field + "\":"+value;
			} else {
				json += "\"" + field + "\":\""+value+"\"";
			}
		}		
		json += "}";
		return json;
	}
	
	public boolean beforeInsert() {
		return true;
	}
	
	public boolean afterInsert() {
		return true;
	}
	
	public boolean beforeUpdate() {
		return true;
	}
	
	public boolean afterUpdate() {
		return true;
	}
}
