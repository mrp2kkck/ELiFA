package com.mundosica.utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class Helper {

	private static String rootPath = null;
	private static String memoryPath = Environment.getExternalStorageDirectory().getPath();
	
	public static String rootPath() {
		if(rootPath == null) {
			return memoryPath+"/libEA";
		}
		return memoryPath + "/" + rootPath;
	}
	public static void rootPath(Context context,String path) {
		File f = new File( memoryPath + path);
		//si no existe la carpeta principal de la app, se creara
		if(!f.isDirectory()) {
			FileHelper.mkDir(context, memoryPath + path);
		} 
		rootPath = path;
	}
	public static boolean isNumeric(Object valor) {
		try {
			Integer.parseInt(valor + "");
			return true;
		} catch (Exception es) {
			return isReal(valor);
		}
	}
	public static boolean isReal(Object valor) {
		try {
			Double.parseDouble(valor + "");
			return true;
		} catch (Exception es) {
			return false;
		}
	}
	
	public static String IMEI(Context activity) {
		TelephonyManager tm = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
		return  tm.getDeviceId();
	}
	//40caracteres alfanumericos 0-F
	public static String cifrado(String algoritmo,String texto) {
		try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance(algoritmo);
	        byte[] array = md.digest(texto.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	       }
	        return sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
	    	//error action
	    }
	    return null;
	}
	
	public static String sha1(String txt) {
		return cifrado("SHA1",txt);
	}
	
	public static String md5(String txt) {
		return cifrado("MD5",txt);
	}
	
	public static boolean validarIFE(String ife) {
		ife=ife.toUpperCase().trim();
		return ife.matches("[A-Za-z]{6}[0-9]{8}[H,M][0-9]{3}");
	}
	
	public static boolean validarCurp(String curp) {
		curp=curp.toUpperCase().trim();
		return curp.matches( 
			"[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}" + 
			"(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])" + 
			"[HM]{1}" + 
			"(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)" + 
			"[B-DF-HJ-NP-TV-Z]{3}" + 
			"[0-9A-Z]{1}[0-9]{1}$"
		); 
	}
	
	public static void printError(Context context, Exception e) {
		Writer writer = new StringWriter();
		PrintWriter print = new PrintWriter(writer);
		e.printStackTrace(print);
		Helper.printError(context, writer.toString());
	}
	
	public static void printError(Context context, String e) {
		String nombre = Fecha.fechaHoraMili();
		nombre = nombre.replaceAll(":", "-");
		String path = rootPath();
		File f = new File(path);
		//si no existe la carpeta principal de la app, se creara
		if(!f.isDirectory()) {
			FileHelper.mkDir(context, path);
		} 
		path += "/Errors";
		f = new File(path);
		//si no exixste la subcarpeta de errores de la app, se creara
		if(!f.isDirectory()) {
			FileHelper.mkDir(context, path);
		} 
		FileHelper.writeFile(context, path + "/error_" + nombre + ".txt",e);
	}
	public static boolean isNetworkAvailable(Activity activity) {
	    ConnectivityManager connectivityManager = 
	    		(ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static void mensaje(Context context,String mensaje) {
		Toast.makeText(context,mensaje, Toast.LENGTH_LONG).show();
	}
}
