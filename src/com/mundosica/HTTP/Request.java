package com.mundosica.HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONObject;


public class Request {

	public static HttpClient client;
	public static HttpResponse response;
	public static String resuesta;
	public static JSONObject json;
	public static JSONArray jsonArray;
	public static Vector<String> errors = new Vector<String>();
	public static Vector<String> log = new Vector<String>();
	public static Vector<StringBody> bodyStringParts;
	public static Vector<String> namesStrngParts;
	public static Vector<FileBody> bodyFileParts;
	public static Vector<String> namesFileParts;
	public static boolean ocupado;
	
	public static MultipartEntity getMultipart() {
		MultipartEntity multipart =  new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		for(int i = 0; i < Request.bodyFileParts.size(); i++) {
			multipart.addPart(namesFileParts.get(i),bodyFileParts.get(i));
		}
		
		for(int i = 0; i < Request.bodyStringParts.size(); i++) {
			multipart.addPart(namesStrngParts.get(i),bodyStringParts.get(i));
		}
		return multipart;
	}
	
	public static MultipartEntity getAndCleanMultipart() {
		MultipartEntity multipart =  getMultipart();
		initParts();
		return multipart;
	}
	
	public static boolean post(String url,MultipartEntity entity) {
		HttpPost post = new HttpPost(url);
		try {
			log.add("url: "+url );
			post.setEntity(entity);
			response = Request.client.execute(post);
			resuesta = respuestaServidor();
			log.add("\nrepuesta servidr post:\n"+resuesta);
			return true;
		} catch (Exception e) {
			errors.add("\nerror post:\n"+e.toString());
			return false;
		}
	}
	
	public static boolean postJObject(String url,MultipartEntity entity) {
		if(Request.post(url, entity)) {
			try {
				Request.json = new JSONObject(Request.resuesta);
				return true;
			} catch(Exception e) {
				Request.errors.add("Error al transformar en json\n" + e.toString());
			}
		}
		return false;
	}
	public static boolean postJArray(String url,MultipartEntity entity) {
		if(Request.post(url, entity)) {
			try {
				Request.jsonArray = new JSONArray(Request.resuesta);
				return true;
			} catch(Exception e) {
				Request.errors.add("Error al transformar en jArray\n" + e.toString());
			}
		}
		return false;
	}
	
	public static String respuestaServidor() {
		String respuesta = "";
		try {
			HttpEntity enti = response.getEntity();
			InputStream content = enti.getContent();
			BufferedReader bufer = new BufferedReader(new InputStreamReader (content));
			String line = "";
			while((line = bufer.readLine()) != null) {
				respuesta += line;
			}
		} catch (Exception e) {
			errors.add("respuesta servidor:\n"+e.toString());
		}
		return respuesta;
	}
	
	public static int statusResponse() {
		return Request.response.getStatusLine().getStatusCode();
	}
	public static void cleanLogs() {
		errors = new Vector<String>();
		log = new Vector<String>();
	}
	public static void addPart(String name, File fb) throws Exception {
		Request.namesFileParts.add(name);
		Request.bodyFileParts.add(new  FileBody(fb));
	}
	public static void addPart(String name, String sb) throws Exception {
		Request.namesStrngParts.add(name);
		Request.bodyStringParts.add(new StringBody(sb));
	}
	public static void initFileParts() {
		Request.namesFileParts = new Vector<String>();
		Request.bodyFileParts = new Vector<FileBody>();
	}
	public static void initStringParts() {
		Request.namesStrngParts = new Vector<String>();
		Request.bodyStringParts = new Vector<StringBody>();
	}
	public static void initParts() {
		initFileParts();
		initStringParts();
	}
}
