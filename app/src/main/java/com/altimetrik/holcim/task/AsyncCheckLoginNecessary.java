package com.altimetrik.holcim.task;

import android.app.Activity;
import android.os.AsyncTask;

import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.utilities.HolcimDialogHandler;

public class AsyncCheckLoginNecessary extends AsyncTask<Void, Void, Object> {
	// Maintain attached activity for states change propose
	private Activity activity;
	// Keep the response of the database query
	private Object _response;
	// Flag that keep async task completed status
	private boolean completed;

	// Dialog
	private HolcimDialogHandler dialog;

	private int redirectActivity;

	// Constructor
	private AsyncCheckLoginNecessary(Activity activity, int redirectActivity) {
		this.activity = activity;
		this.redirectActivity = redirectActivity;
	}

	// Pre execution actions
	@Override
	protected void onPreExecute() {
		// Start the splash screen dialog
		dialog.showProgress();
	}

	// Execution of the async task
	protected Object doInBackground(Void... params) {
		// try {
		if (HolcimApp.getAccessTokens().get_access_token() == null
				|| HolcimApp.getAccessTokens().get_access_token().equals("")) {
			// is necessary to call login
			return false;
		} else {
			// call check WS
			// TODO: delete return false and call ws to know if the user is
			// authorized
			return false;
		}
		// } catch (HolcimException hx) {
		// return false;
		// } catch (Exception e) {
		// return false;
		// }
		// TODO:uncomment when ws exist
	}

	// Post execution actions
	@Override
	protected void onPostExecute(Object response) {
		// Set task completed and notify the activity
		completed = true;
		_response = response;
		notifyActivityTaskCompleted();
	}

	// Notify activity of async task complete
	private void notifyActivityTaskCompleted() {
		if (null != activity) {
			// TODO: uncomment and implement onAsyncCheckLoginNecessaryFinish in
			// activity
			// activity.onAsyncCheckLoginNecessaryFinish(_response,
			// redirectActivity);
		}
	}

	// Sets the current activity to the async task
	public void setActivity(Activity activity) {
		this.activity = activity;
		if (completed) {
			notifyActivityTaskCompleted();
		}
	}

}
