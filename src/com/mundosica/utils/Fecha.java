package com.mundosica.utils;

import java.util.Calendar;

public class Fecha {
	public static Calendar fecha;
	static int anho;
	static int mes;
	static int dia;
	static int hra;
	static int min;
	static int seg;
	static int mili;
	static void fechaAhora() {
		fecha = Calendar.getInstance();
		anho = fecha.get(Calendar.YEAR);
		mes = fecha.get(Calendar.MONTH) + 1;
		dia = fecha.get(Calendar.DATE);
		hra = fecha.get(Calendar.HOUR_OF_DAY);
		min = fecha.get(Calendar.MINUTE);
		seg = fecha.get(Calendar.SECOND);
		mili = fecha.get(Calendar.MILLISECOND);
	}
	
	public static String fechaActual() {
		fechaAhora();
		return anho + "-" + mes + "-" + dia;
	}
	public static String fechaHoraActual() {
		fechaAhora();
		return anho + "-" + mes + "-" + dia + " " + hra + ":" + min + ":" + seg;
	}
	public static String fechaHoraMili() {
		fechaAhora();
		return anho + "-" + mes + "-" + dia + " " + hra + ":" + min + ":" + seg + ":" + mili;
	}
}
