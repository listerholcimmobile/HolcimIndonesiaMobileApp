package com.holcim.altimetrik.android.exception;

public class HolcimError {
		
		private String status;
		private String message;
		
		public HolcimError() {
			
		}
		
		public HolcimError(String pStatus, String pMessage) {
			this.setStatus(pStatus);
			this.setMessage(pMessage);
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
			
	}

