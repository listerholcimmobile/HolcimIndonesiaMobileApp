package com.holcim.altimetrik.android.utilities;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.InputStreamEntity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

public class AltimetrikFileHandler {
	Context mContext;
	
	public AltimetrikFileHandler(Context context) {
		this.mContext = context;
	}
	
	public String getDataDir() throws AltimetrikException 
    {
		try {
			return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).applicationInfo.dataDir;
		} catch (Exception e) {
			throw new AltimetrikException(e);
		}       
    }
	
	public static String getDataDir(Context context) throws AltimetrikException 
    {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir;
		} catch (Exception e) {
			throw new AltimetrikException(e);
		}       
    }
	
	public void createDirectoryOnAppDirectory(String directoryName) throws AltimetrikException {
		try {
			File newFolder = new File(getDataDir() + File.separator + directoryName);
			newFolder.mkdir();
		} catch (Exception e) {
			throw new AltimetrikException(e);
		}
	}
	
	public boolean isFileExistOnAppDirectory(String filePath) throws AltimetrikException {
		File file;
		try {
			file = new File(getDataDir() + File.separator + filePath);
			return file.exists();
		} catch (AltimetrikException e) {
			throw new AltimetrikException(e);
		}		
	}
	
	public boolean isDirectoryExistOnAppDirectory(String filePath) throws AltimetrikException {
		File file;
		try {
			file = new File(getDataDir() + File.separator + filePath);
			return file.exists() && file.isDirectory();
		} catch (AltimetrikException e) {
			throw new AltimetrikException(e);
		}		
	}
	
	/**
	 * Write a HTTP response to a file
	 * @param filename - Path to save the file
	 * @param response - HTTP response with file content
	 * @return file path
	 * @throws LensException
	 */
	private String WriteHTTPResponseToFile(File file, HttpResponse response) throws AltimetrikException {
		FileOutputStream fos = null;
		try {
			HttpEntity entity = response.getEntity();
			fos = new FileOutputStream(file);	        
			InputStream is = entity.getContent();	     
			byte[] buffer = new byte[1024]; 
			int len;
			while ((len = is.read(buffer, 0, 1024)) > 0) {
				fos.write(buffer, 0, len);
			}
			return file.getPath();
		} catch (IOException e){
			throw new AltimetrikException(e);
		} catch (Exception e) {
			throw new AltimetrikException(e);
		} finally{
			if (fos!=null) {
				try{
					fos.close();
				} catch (IOException e){
					throw new AltimetrikException(e);
				}
			}
		}
	} 

	@SuppressWarnings("unused")
	private String WriteInputStreamFile(File file, InputStream input) throws AltimetrikException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024]; 
			int len;
			while ((len = input.read(buffer, 0, 1024)) > 0) {
				fos.write(buffer, 0, len);
			}
			return file.getPath();
		} catch (IOException e){
			throw new AltimetrikException(e);
		} catch (Exception e) {
			throw new AltimetrikException(e);
		} finally{
			if (fos!=null) {
				try{
					fos.close();
				} catch (IOException e){
					throw new AltimetrikException(e);
				}
			}
		}
	}

	@SuppressLint("WorldReadableFiles")
	private String WriteInputStreamFile(String filename, InputStream input) throws AltimetrikException {
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(filename));
			byte[] buffer = new byte[1024]; 
			int len;
			while ((len = input.read(buffer, 0, 1024)) > 0) {
				fos.write(buffer, 0, len);
			}
			return filename;
		} catch (IOException e){
			throw new AltimetrikException(e);
		} catch (Exception e) {
			throw new AltimetrikException(e);
		} finally{
			if (fos!=null) {
				try{
					fos.close();
				} catch (IOException e){
					throw new AltimetrikException(e);
				}
			}
		}
	}
	
	public static void WriteBytesTodFile(String filename, byte[] bytes) throws AltimetrikException {
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(filename));
			fos.write(bytes);
		} catch (IOException e){
			throw new AltimetrikException(e);
		} catch (Exception e) {
			throw new AltimetrikException(e);
		} finally{
			if (fos!=null) {
				try{
					fos.close();
				} catch (IOException e){
					throw new AltimetrikException(e);
				}
			}
		}
	}
	
	@SuppressLint("WorldReadableFiles")
	public String WriteBitmapToFile(String filename, Bitmap bitmap) throws AltimetrikException {
		OutputStream fos = null;
		try {
			File image = new File(filename);
			fos = new FileOutputStream(image);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
			fos.flush();

			return filename;
		} catch (IOException e){
			throw new AltimetrikException(e);
		} catch (Exception e) {
			throw new AltimetrikException(e);
		} finally{
			if (fos!=null) {
				try{
					fos.close();
				} catch (IOException e){
					throw new AltimetrikException(e);
				}
			}
		}
	} 

	/**
	 * Save a file to a given filename from a http response and return the checksum md5
	 * @param filename - path to save the file
	 * @param response - http response with the file content
	 * @return MD5 checksum of the saved file
	 * @throws LensException
	 */
	public String SaveFileFromHTTPResponse(String filename, HttpResponse response) throws AltimetrikException {
		try {
			File file = new File(mContext.getFilesDir(), filename);
			return WriteHTTPResponseToFile(file, response);
		} catch (AltimetrikException e) {
			throw e;
		} catch (Exception e) {
			throw new AltimetrikException(e);
		}
	}
	
	

	/**
	 * Save a file to a given filename from a http response and return the checksum md5
	 * @param filename - path to save the file
	 * @param response - http response with the file content
	 * @return MD5 checksum of the saved file
	 * @throws LensException
	 */
	public String SaveFileFromInputStream(String filename, InputStream input) throws AltimetrikException {
		try {
			return WriteInputStreamFile(filename, input);
		} catch (AltimetrikException e) {
			throw e;
		} catch (Exception e) {
			throw new AltimetrikException(e);
		}
	}

	public String GetChecksum(String filePath) throws AltimetrikException {
		try {
			return getMD5Checksum(filePath);
		} catch (AltimetrikException e) {
			throw e;
		} catch (Exception e) {
			throw new AltimetrikException(e);
		}
	}

	public InputStreamEntity GetInputStreamFromFile(String filePath) throws AltimetrikException {
		try {
			File file = new File(filePath);
			InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
			return reqEntity;
		}
		catch(Exception e) {
			throw new AltimetrikException(e);
		}
	}

	public static boolean DeleteFile(String filePath) throws AltimetrikException {
		try {
			File file = new File(filePath);
			return file.delete();
		}
		catch(Exception e) {
			throw new AltimetrikException(e);
		}
	}

	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	
	public static boolean isDirectoryExist(String filePath) {
		File file = new File(filePath);
		return file.exists() && file.isDirectory();
	}

	public static int GetMediaFileDuration(String path) {
		FileInputStream fs = null;
		try {
			File attachFile = new File(path);
			MediaPlayer mp = new MediaPlayer();			
			FileDescriptor fd;
			fs = new FileInputStream(attachFile);
			fd = fs.getFD();
			mp.setDataSource(fd);
			mp.prepare();
			int length = mp.getDuration();
			mp.release();
			return length;
		} catch (Exception e) {
			return 0;
		} finally{
			if (fs!=null) {
				try{
					fs.close();
				} catch (IOException e){
					return 0;
				}
			}
		}	
	}
	
	public static boolean renameFile(String path, String newPath) {
			if (isFileExist(path)) {
				File file = new File(path);
				return file.renameTo(new File(newPath));
			}
			return false;		
	}
	
	@SuppressLint("WorldReadableFiles")
	public String copyFile(String src, String dst) throws IOException
	{
		File inFile = new File(src);
		InputStream in = new FileInputStream(inFile);
	    OutputStream out = new FileOutputStream(new File(dst));
	    try
	    {
	    	byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	        	out.write(buf, 0, len);
	        }
	        return mContext.getFilesDir() + File.separator + dst;
	    }
	    finally
	    {
	        if (in != null)
	        	in.close();
	        if (out != null)
	        	out.close();
	    }
	}
	
	public void moveFile(String src, String dst) throws AltimetrikException {
		try {
			copyFile(src, dst);
			DeleteFile(src);
		} catch (Exception e) {
			throw new AltimetrikException(e);
		}
	}
	
	/**
	 * Get the byte array from file to get the checksum
	 * @param filename
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] createChecksum(String filename) throws Exception {
		InputStream fis =  new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	/**
	 * get the checksum string representation
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static String getMD5Checksum(String filename) throws Exception {
		byte[] b = createChecksum(filename);
		String result = "";

		for (int i=0; i < b.length; i++) {
			result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
	
	public static void deleteAllFilesInDirectory(Context context, String dirPath) throws AltimetrikException {
		try {
			File dir = new File(dirPath);
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            new File(dir, children[i]).delete();
	        }
		}
		} catch (Exception e) {
			throw new AltimetrikException(e);
		}
		
	}

}
