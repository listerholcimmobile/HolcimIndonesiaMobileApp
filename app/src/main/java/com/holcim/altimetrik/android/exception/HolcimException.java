package com.holcim.altimetrik.android.exception;

public class HolcimException extends Exception {

		/**
		 * Generated Serial UID
		 */
		private static final long serialVersionUID = 6472505629428142136L;

		//Exceptions type to handle
		public static final int WEB_SERVICE_EXCEPTION = 0;
		public static final int DATABASE_EXCEPTION = 1;
		public static final int CONNECTION_EXCEPTION = 2;
		public static final int GENERAL_EXCEPTION = 3;
		public static final int SESSION_EXCEPTION = 4;
		public static final int PARSE_EXCEPTION = 5;
		public static final int FILE_EXCEPTION = 6;
		public static final int WEB_SERVICE_BAD_CODE_EXCEPTION = 7;

		//Statics status
		public static final String ERROR_STATUS = "ERROR";

		private HolcimError holcimError;
		private int type;

		public HolcimException(int pType, HolcimError pHolcimError) {
			super();
			this.setType(pType);
			this.setHolcimError(pHolcimError);
		}

		public HolcimException(int pType, String pStatus, String pMessage) {
			super();
			this.setType(pType);
			this.setHolcimError(new HolcimError(pStatus, pMessage));
		}

		public HolcimError getHolcimError() {
			return holcimError;
		}

		public void setHolcimError(HolcimError holicmError) {
			this.holcimError = holcimError;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public static String GetResponseCodeMessage(int code) {
			switch (code) {
			case 400: 
				return "recordId is not a valid Salesforce Id.";
			case 404: 
				return "Attachment doesn't exists";
			case 406: 
				return "Attachment.MediaChecksum__c doesn't match the checksum of the media being uploaded";
			case 500: 
				return "If any of the DML Operations over the database fails it throws this status code (e.g. fails to create the ContentVersion record)";
			default: 
				return "recordId is not a valid Salesforce Id.";

			}
		}
	}

