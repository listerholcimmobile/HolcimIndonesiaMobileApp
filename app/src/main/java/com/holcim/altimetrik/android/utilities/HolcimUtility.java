package com.holcim.altimetrik.android.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;

public class HolcimUtility {
	/**
	 * Check Internet connectivity
	 * @param cntx - Instance of the context where the method is called from
	 * @return If there is Internet connection or not
	 */
	public static boolean isOnline(Context cntx) {
		ConnectivityManager cm = (ConnectivityManager)cntx.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Get the numbers of days between two dates
	 * @param startDate - Start date
	 * @param endDate - End date
	 * @return Number of days between the dates
	 */
	public static long daysBetween(Calendar startDate, Calendar endDate) 
	{
		Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	/**
	 * Get the string of today date formated by parameter
	 * @param pDateFormat - Date format to build string. Ex:
	 * 				     yyyy-MM-dd 1969-12-31
	                     yyyy-MM-dd 1970-01-01
	               yyyy-MM-dd HH:mm 1969-12-31 16:00
	               yyyy-MM-dd HH:mm 1970-01-01 00:00
	              yyyy-MM-dd HH:mmZ 1969-12-31 16:00-0800
	              yyyy-MM-dd HH:mmZ 1970-01-01 00:00+0000
	       yyyy-MM-dd HH:mm:ss.SSSZ 1969-12-31 16:00:00.000-0800
	       yyyy-MM-dd HH:mm:ss.SSSZ 1970-01-01 00:00:00.000+0000
	     yyyy-MM-dd'T'HH:mm:ss.SSSZ 1969-12-31T16:00:00.000-0800
	     yyyy-MM-dd'T'HH:mm:ss.SSSZ 1970-01-01T00:00:00.000+0000
	 * @return String with today date formated 
	 */
	public static String getTodayFormated(String pDateFormat)
	{
		SimpleDateFormat todayFormater = new SimpleDateFormat(pDateFormat);
		Calendar calendar = Calendar.getInstance();
		return todayFormater.format(calendar.getTime());
	}
	
	public static Calendar getDateAddDaysFromToday(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar;
	}

	public static String getDateStringFormated(Calendar pCalendar, String pFormat) {
		SimpleDateFormat todayFormater = new SimpleDateFormat(pFormat);
		return todayFormater.format(pCalendar.getTime());
	}

	/**
	 * Return date from string
	 * @param pDate - String with the date to parse
	 * @return Calendar type with the parsed date
	 */
	public static Calendar getDateFromString(String pDate)
	{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		try {
			calendar.setTime(formater.parse(pDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}

	public static Calendar getDateFromString(String pDate, String pFormat)
	{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat(pFormat);
		try {
			calendar.setTime(formater.parse(pDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}
	
	public static String getStringDateFormatted(int year, int month, int day, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		return HolcimUtility.getDateStringFormated(calendar, format);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, int currentWidth, int currentHeight) {
		// Raw height and width of image
		final int height = currentHeight;
		final int width = currentWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			inSampleSize *= 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight, int orientation) {

		try {
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			// Calculate inSampleSize
			if (orientation == 90 || orientation == 270) {
				options.inSampleSize = HolcimUtility.calculateInSampleSize(options, reqWidth, reqHeight, options.outHeight, options.outWidth);
			} else {
				options.inSampleSize = HolcimUtility.calculateInSampleSize(options, reqWidth, reqHeight, options.outWidth, options.outHeight);
			}

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap outBitmap = BitmapFactory.decodeFile(path, options);

			/*
			 * if the orientation is not 0 (or -1, which means we don't know), we
			 * have to do a rotation.
			 */
			if (orientation > 0) {
				Matrix matrix = new Matrix();
				matrix.postRotate(orientation);

				outBitmap = Bitmap.createBitmap(outBitmap, 0, 0, outBitmap.getWidth(), outBitmap.getHeight(), matrix, true);
			}

			return outBitmap;

		} catch (Exception e) {
			return null;
		}


	}
	
	public static Bitmap decodeSampledBitmap(byte[] bitmap, int reqWidth, int reqHeight, int orientation) {

		try {
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length, options);

			// Calculate inSampleSize
			if (orientation == 90 || orientation == 270) {
				options.inSampleSize = HolcimUtility.calculateInSampleSize(options, reqWidth, reqHeight, options.outHeight, options.outWidth);
			} else {
				options.inSampleSize = HolcimUtility.calculateInSampleSize(options, reqWidth, reqHeight, options.outWidth, options.outHeight);
			}

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap outBitmap = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length, options);

			/*
			 * if the orientation is not 0 (or -1, which means we don't know), we
			 * have to do a rotation.
			 */
			if (orientation > 0) {
				Matrix matrix = new Matrix();
				matrix.postRotate(orientation);

				outBitmap = Bitmap.createBitmap(outBitmap, 0, 0, outBitmap.getWidth(), outBitmap.getHeight(), matrix, true);
			}

			return outBitmap;

		} catch (Exception e) {
			return null;
		}


	}


	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

		try {
			// First decode with inJustDecodeBounds=true to check dimensions
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(res, resId, options);
			// Calculate inSampleSize
			int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight, options.outWidth, options.outHeight);
			options.inSampleSize = inSampleSize;

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeResource(res, resId, options);
		} catch (OutOfMemoryError e) {
			return HolcimUtility.decodeSampledBitmapFromResourceFixedSampleSize(res, resId, 2);
		}		
	}
	
	public static Bitmap decodeSampledBitmapFromResourceFixedSampleSize(Resources res, int resId, int fixedSampleSize) {
		try {
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = fixedSampleSize;
			return BitmapFactory.decodeResource(res, resId, options);
		} catch (OutOfMemoryError e) {
			return HolcimUtility.decodeSampledBitmapFromResourceFixedSampleSize(res, resId, fixedSampleSize + 1);
		}		
	}

	public static int getOrientation(Context context, Uri photoUri) {
		try {
			/* it's on the external media. */
			Cursor cursor = context.getContentResolver().query(photoUri, new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

			if (cursor == null || cursor.getCount() != 1) {
				return -1;
			}

			cursor.moveToFirst();
			return cursor.getInt(0);
		} catch (Exception e) {
			return 0;
		}
		
	}
	
	//validate if email is well formed
	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null || target.toString().isEmpty()) {
	        return true;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
	
	//validate if an end date is after or equal than a start date
	public final static boolean validDates(String startDate, String endDate){
		Calendar sdate = getDateFromString(startDate);
		Calendar edate = getDateFromString(endDate);
		if(sdate.after(edate)){
			return false;
		}
		return true;
	}
}
