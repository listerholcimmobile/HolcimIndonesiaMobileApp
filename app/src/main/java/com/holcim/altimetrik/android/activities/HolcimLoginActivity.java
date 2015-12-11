package com.holcim.altimetrik.android.activities;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.altimetrik.holcim.controller.HolcimController;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.utilities.HolcimUtility;
import com.holcim.altimetrik.android.utilities.OAuthTokens;
import com.holcim.altimetrik.android.utilities.OAuthUtil;
import com.holcim.altimetrik.android.utilities.SFUser;

/**
 * Main activity class
 * 
 * @author ajuarez@altimetrik
 * 
 */

@SuppressLint("SetJavaScriptEnabled")
public class HolcimLoginActivity extends HolcimCustomActivity {
	WebView webview;
	String callbackUrl;
	LinearLayout progress;
	ProgressBar swirl;
	static Boolean isProduction;

	PageFinishHandler pageFinishHandler;

	String reqUrl;
	private AsyncUserTransfer asyncUser;
	private android.view.View.OnClickListener mRefreshClickListener;

	public static void isProduction(Boolean isProd) {
		isProduction = isProd;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.login);
		super.onCreate(savedInstanceState);
		
		progress = (LinearLayout) findViewById(R.id.progress);
		swirl = (ProgressBar) findViewById(R.id.swirl);

		progress.setVisibility(View.VISIBLE);

		if (HolcimUtility.isOnline(this)) {
			// new AsyncLoginFix().execute("");
			startActivity();
		} else {
			final Context context = this;
			// Set refresh button action
			mRefreshClickListener = new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (HolcimUtility.isOnline(context)) {
						hideRefreshNoInternetDialog();
						startActivity();
					}
				}
			};
			showRefreshNoInternetDialog();
		}
		setCustomTitle(getResources().getString(R.string.login));
	}

	private void loginHotFix() {

		HttpClient client = new DefaultHttpClient();

		HttpPost postRequest = new HttpPost(
				"https://test.salesforce.com/services/oauth2/token");

		try {

			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters
					.add(new BasicNameValuePair("grant_type", "password"));
			postParameters
					.add(new BasicNameValuePair(
							"client_id",
							"3MVG9e2mBbZnmM6lk8YA8cOgMyxHxysRqKg4sP5Sry5bhGkZpZXCb7Ez0KGMGdDFZe9CchCu.Q20Ws71SzRPn"));
			postParameters.add(new BasicNameValuePair("client_secret",
					"4430680915156717853"));
			postParameters.add(new BasicNameValuePair("username",
					"mbustamante@altimetrik.com.hildev08"));
			postParameters
					.add(new BasicNameValuePair("password", "Indonesia1!"));
			postParameters.add(new BasicNameValuePair("redirect_uri",
					"https://cs6.salesforce.com/services/oauth2/success"));

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			postRequest.setEntity(formEntity);

			// Execute HTTP Post Request
			HttpResponse response = client.execute(postRequest);
			if (response != null) {
				startActivity();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}

	private class AsyncLoginFix extends AsyncTask<String, Integer, Double> {

		@Override
		protected Double doInBackground(String... params) {
			loginHotFix();
			return null;
		}

		protected void onPostExecute(Double result) {
			Toast.makeText(getApplicationContext(), "correcto",
					Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * Method to start the activity with the webview
	 * 
	 */
	private void startActivity() {
		/*
		 * As per the OAuth 2.0 User-Agent Flow supported by Salesforce, we pass
		 * along the Client Id (aka Consumer Key) as a GET parameter. We also
		 * pass along a special String as the redirect URI so that we can verify
		 * it when Salesforce redirects the user back to the mobile device
		 */
		// String consumerKey =
		// Decryption.decryptKey(this.getResources().getString(R.string.consumer_key).toString());
		String consumerKey = this.getResources()
				.getString(R.string.consumer_key).toString();
		String url;

		if (isProduction) {
			url = this.getResources().getString(R.string.oAuthUrlProduction)
					.toString();
			callbackUrl = this.getResources()
					.getString(R.string.callbackUrlProduction).toString();
			Toast.makeText(getApplicationContext(),"Log into Production ORG",Toast.LENGTH_SHORT).show();
		} else {
			url = this.getResources().getString(R.string.oAuthUrlTest)
					.toString();
			callbackUrl = this.getResources()
					.getString(R.string.callbackUrlTest).toString();
			Toast.makeText(getApplicationContext(),"Log into Pre-prod ORG",Toast.LENGTH_SHORT).show();
		}

		// the url to load into the web view -- this will bring up the SFDC
		// login page
		reqUrl = url + consumerKey + "&redirect_uri=" + callbackUrl;

		pageFinishHandler = new PageFinishHandler();

		// find the web view
		webview = (WebView) findViewById(R.id.webview);

		webview.setWebViewClient(new LoginWebViewClient());

		webview.getSettings().setJavaScriptEnabled(true);

		webview.loadUrl(reqUrl);

		// We don't need to keep the cookies since we store the tokens in oauth
		// class.
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		// cookieManager.removeAllCookie();
	}

	private void showRefreshNoInternetDialog() {
		dialog.showNoInternetRefreshDialog(mRefreshClickListener);
	}

	private void hideRefreshNoInternetDialog() {
		dialog.hideNoInternetRefreshDialog();
	}

	private void showProgress() {
		webview.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
	}

	private void hideProgress() {
		progress.setVisibility(View.GONE);
		webview.setVisibility(View.VISIBLE);
	}

	private void showMessageScreen() {
		swirl.setVisibility(View.VISIBLE);
	}

	private void hideMessageScreen() {
		swirl.setVisibility(View.GONE);
	}

	/**
	 * Extend WebViewClient so we can monitor page loads and redirect when we
	 * get the callback url back from SFDC
	 */
	private class LoginWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			showProgress();
			pageFinishHandler
					.removeMessages(PageFinishHandler.MESSAGE_PAGE_FINISHED);
		}

		@Override
		public void onPageFinished(WebView view, String url) {

			// Log.d("TG:", "Redirect URL: " + url);

			// check if the redirect URL starts with the callbackUrl
			// if it does, we're done with the web view, and need to parse the
			// tokens we got back from SFDC
			if (url.startsWith(callbackUrl)) {
				if (!url.contains("error")) {

					// Log.d("TG","Redirecting to Main View");

					// parse the access tokens from the callbackUrl redirect
					OAuthTokens accessTokens = OAuthUtil.parseToken(url);

					// OAuthUtil needs to know if is org or test production
					OAuthUtil.setIsProduction(isProduction);

					// save the access tokens to disk for next time
					OAuthUtil.Save(accessTokens, getApplicationContext());

					// keep track of the access tokens in the model
					HolcimApp.setAccessTokens(accessTokens);
					// startAsyncUserTrasnfer(HolcimLoginActivity.this);
					Intent i = new Intent(HolcimLoginActivity.this,
							HolcimSyncActivity.class);
					startActivity(i);
				} else if (!HolcimUtility.isOnline(HolcimLoginActivity.this)) {
					showMessageScreen();
					dialog.showError(
							getString(R.string.message_no_internet_connection),
							getString(R.string.message_button_ok),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									hideMessageScreen();
									webview.loadUrl(reqUrl);
								}
							});
				} else {
					Intent i = new Intent(HolcimLoginActivity.this,
							HolcimMainActivity.class);
					startActivity(i);
				}
			} else {
				Message msg = Message.obtain(pageFinishHandler,
						PageFinishHandler.MESSAGE_PAGE_FINISHED, "");
				pageFinishHandler.sendMessageDelayed(msg,
						PageFinishHandler.DELAY_PAGE_FINISHED);
			}

		}
	}

	/**
	 * Override method for go back button to do nothing.
	 */
	@Override
	public void onBackPressed() {
		if (!HolcimCustomActivity.blockback) {
			HolcimCustomActivity.setOnback(true);
			Intent i = new Intent(HolcimLoginActivity.this,
					HolcimMainActivity.class);
			startActivity(i);
		}
	}

	public void onAsyncUserTransferCompleted(Object _response) {
		android.os.Debug.waitForDebugger();
		try {
			if (_response instanceof SFUser) {
				OAuthUtil.SaveUser((SFUser) _response, this);
				HolcimApp.getAccessTokens().set_user((SFUser) _response);
				// redirect to the main view controller
				Intent i = new Intent(this, HolcimSyncActivity.class);
				startActivity(i);
			} else {
				showMessageScreen();
				dialog.showError(
						getString(R.string.message_no_internet_connection),
						getString(R.string.message_button_ok),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								hideMessageScreen();
								webview.loadUrl(reqUrl);
							}
						});
			}
		} catch (Exception e) {
			showMessageScreen();
			dialog.showError(
					getString(R.string.message_no_internet_connection),
					getString(R.string.message_button_ok),
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							hideMessageScreen();
							webview.loadUrl(reqUrl);
						}
					});
		}

	}

	public class AsyncUserTransfer extends AsyncTask<Void, Void, Object> {
		// Maintain attached activity for states change propose
		private HolcimLoginActivity activity;
		// Keep the response
		private Object _response;
		// Flag that keep async task completed status
		private boolean completed;

		// Constructor
		private AsyncUserTransfer(HolcimLoginActivity activity) {
			this.activity = activity;
		}

		// Pre execution actions
		@Override
		protected void onPreExecute() {
		}

		protected Object doInBackground(Void... arg0) {
			try {
				android.os.Debug.waitForDebugger();
				return HolcimController.getUser();
			} catch (HolcimException e) {
				return e.getHolcimError();
			}
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
				activity.onAsyncUserTransferCompleted(_response);
			}
		}

		// Sets the current activity to the async task
		public void setActivity(HolcimLoginActivity activity) {
			this.activity = activity;
			if (completed) {
				notifyActivityTaskCompleted();
			}
		}
	}

	public void startAsyncUserTrasnfer(HolcimLoginActivity lensAct) {
		asyncUser = new AsyncUserTransfer(lensAct);
		asyncUser.execute();
	}

	public class PageFinishHandler extends Handler {
		public static final int MESSAGE_PAGE_FINISHED = 0;
		public static final int DELAY_PAGE_FINISHED = 100;

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MESSAGE_PAGE_FINISHED) {
				hideProgress();
			}
		}
	}

}
