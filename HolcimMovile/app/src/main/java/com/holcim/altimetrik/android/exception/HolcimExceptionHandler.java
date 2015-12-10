package com.holcim.altimetrik.android.exception;
import android.content.Context;


public class HolcimExceptionHandler {

	Context mContext;
	HolcimDialogHandler mDialog;



	public HolcimExceptionHandler(Context context) {
		mContext = context;
		mDialog = new HolcimDialogHandler(mContext);
	}

}
