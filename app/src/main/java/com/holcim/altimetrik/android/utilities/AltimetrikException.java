package com.holcim.altimetrik.android.utilities;


public class AltimetrikException extends Exception {

	/**
	 * Generated Serial UID
	 */
	private static final long serialVersionUID = 8096948155985985606L;

	//Exceptions type to handle
	public static final int WEB_SERVICE_EXCEPTION = 0;
	public static final int DATABASE_EXCEPTION = 1;
	public static final int CONNECTION_EXCEPTION = 2;
	public static final int GENERAL_EXCEPTION = 3;
	public static final int SESSION_EXCEPTION = 4;
	public static final int PARSE_EXCEPTION = 5;
	public static final int FILE_EXCEPTION = 6;
	public static final int WEB_SERVICE_BAD_CODE_EXCEPTION = 7;
	
	private Exception originalException;
	
	public AltimetrikException(Exception originalException) {
		this.originalException = originalException;
	}

}
