package com.mundosica.model;

public class ColumnMetadata {

	public int cid; //columna id
	public String name;
	public String type;
	public boolean isNull;
	public boolean isPrimary;
	
	public ColumnMetadata(int cid,String name, String type,String isNull,String primary) {
		this.cid = cid;
		this.name = name;
		this.type = type;
		this.isNull = isNull.equals("1");
		this.isPrimary = primary.equals("1");
	}
}

