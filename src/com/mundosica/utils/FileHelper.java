package com.mundosica.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;


public class FileHelper {

	public static int BUFFER = 2048;
	public static int MAX_IMAGE_SIZE = 200 * 1024; // tamaño maximo 200kb
	public static int IMAGE_W = 800;
	public static int IMAGE_H = 600;
	
	
	public static boolean  zip(Context context,String[] _files, String zipFileName) {
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(zipFileName);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
			byte data[] = new byte[BUFFER];
			
			for (int i = 0; i < _files.length; i++) {
				FileInputStream fi = new FileInputStream(_files[i]);
				origin = new BufferedInputStream(fi, BUFFER);/*
				String nombreArchivo = "";
				switch (i) {
				case 0:nombreArchivo = "data.json";break;
				case 1:nombreArchivo = "previa.jpg";break;
				case 2:nombreArchivo = "instalacion.jpg";break;
				case 3:nombreArchivo = "post_instalacion.jpg";break;
				case 4:nombreArchivo = "firma_digital.jpg";
				}*/
				//_files[i].substring(_files[i].lastIndexOf("/") + 1)
				ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
				out.putNextEntry(entry);
				int count;

				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
				
			}
			out.finish();
			out.close();
			dest.close();
			return true;
		} catch(Exception e) {
			Helper.printError(context, e);
			return false;
		}
	}
	
	public static void writeFile(Context context,String name,String text) {
		try {
			File file  = new File(name);
		    OutputStreamWriter fout=
		        new OutputStreamWriter(
		        		new FileOutputStream(file));
		 
		    fout.write(text);
		    fout.close();
		}
		catch (Exception e) {
			Helper.mensaje(context, e.toString());
		}
	}
	
	public static String getPathFromUri(Activity activyti,Uri uri) {
		String path = null;
		try {
			Cursor cursor = activyti.getContentResolver().query(
					uri, null, null, null, null
			);
			cursor.moveToFirst();
			String document_id = cursor.getString(0);
			document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
			cursor.close();
			cursor = activyti.getContentResolver().query(
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					null,MediaStore.Images.Media._ID + " = ? ",new String[]{document_id},null
			);
			cursor.moveToFirst();
			path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
		} catch(Exception e) {
			Helper.printError(activyti, e);
		}
		return path;
	}
	public static void cargarImagenEnVista(Activity activity,Bitmap myBitmap,
			ImageView vistaImagen, String path) {
		
		if(path == null) {
			Helper.mensaje(activity, "path: "+path);
		}
		File imgFile = new  File(path);
		if(imgFile.exists()) {
		try {
			//if (myBitmap != null) {
				//Helper.mensaje(activity, "se reciclara el bitmap");
				//myBitmap.recycle();
			//}
			if(vistaImagen != null) {
				vistaImagen.setImageBitmap(null);
			}
			FileHelper.MAX_IMAGE_SIZE = 10 * 1024;
			FileHelper.IMAGE_H = 100;
			FileHelper.IMAGE_W = 200;
			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			vistaImagen.setImageBitmap(compressBitmap(myBitmap,imgFile));
			myBitmap.recycle();
		} catch(Exception e) {
			Helper.mensaje(activity, e.toString());
		}
		} else {
			Helper.mensaje(activity, path+": no existe fichero: "+imgFile.getAbsolutePath());
		}
	}
	public static void comprimir(
			Context context,String path_imagen_original, String path_imagen_destino) {
		try {
			File fileUri = new File(path_imagen_original);
			Bitmap bmpPic = BitmapFactory.decodeFile(fileUri.getPath());
			bmpPic = compressBitmap(bmpPic,fileUri);
			int compressQuality = 104;
			compressQuality = compressQuality(compressQuality,bmpPic);
			FileOutputStream bmpFile = new FileOutputStream(path_imagen_destino);
			bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
			bmpFile.flush();
			bmpFile.close();
		} catch (Exception e) {
			//Helper.mensaje(context,"error comprimir: "+e.toString() );
			Helper.printError(context, e);
		}
	}
	public static Bitmap compressBitmap(Bitmap myBitmap,File file) {
		
		if ((myBitmap.getWidth() >= IMAGE_W) && (myBitmap.getHeight() >= IMAGE_H)) {
			BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
			bmpOptions.inSampleSize = 1;
			while ((myBitmap.getWidth() >= IMAGE_W) && (myBitmap.getHeight() >= IMAGE_H)) {
				bmpOptions.inSampleSize++;
				myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmpOptions);
			}
		}
		//int compressQuality = 104; // quality decreasing by 5 every loop. (start from 99)
		//compressQuality(compressQuality,myBitmap);
		return myBitmap;
	}
	
	public static int compressQuality(int quality, Bitmap myBitmap) {
		int streamLength = MAX_IMAGE_SIZE;
		while (streamLength >= MAX_IMAGE_SIZE) {
			ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
			quality -= 5;
			myBitmap.compress(Bitmap.CompressFormat.JPEG, quality, bmpStream);
			byte[] bmpPicByteArray = bmpStream.toByteArray();
			streamLength = bmpPicByteArray.length;
		}
		return quality;
	}
	
	@SuppressWarnings("resource")
	public static void copyFile(Activity activity,String origen, String destino) {
	    try {
	        File sd = Environment.getExternalStorageDirectory();
	       // File data = Environment.getDataDirectory();

	        if (sd.canWrite()) {
	            String currentPath = origen;
	            String backupPath = destino;
	            //File currentDB = new File(data, currentDBPath);
	            //File backupDB = new File(sd, backupDBPath);
	            File currentDB = new File(currentPath);
	            File backupDB = new File(backupPath);

	            FileChannel src = new FileInputStream(currentDB).getChannel();
	            FileChannel dst = new FileOutputStream(backupDB).getChannel();
	            dst.transferFrom(src, 0, src.size());
	            src.close();
	            dst.close();

	        }
	    } catch (Exception e) {
	    	Helper.mensaje(activity, "copy file: "+e.toString());
	    	Helper.printError(activity, e);
	    }
	}
	
	public static void mkDir(Context activity,String path) {
		File folder = new File(path);
		boolean success = true;
		if (!folder.exists()) {
		    success = folder.mkdir();
		}
		if (success) {
		    // Do something on success
		} else {
			Helper.mensaje(activity, "error al crear: " +  path);
		}
	}
}
