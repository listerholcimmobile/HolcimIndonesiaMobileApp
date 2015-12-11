/*
 * Copyright (C) 2011 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.holcim.altimetrik.android.application;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.DaoMaster;
import com.holcim.altimetrik.android.model.DaoSession;
import com.holcim.altimetrik.android.model.DaoMaster.DevOpenHelper;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimSecuredPreferences;
import com.holcim.altimetrik.android.utilities.OAuthTokens;
import com.holcim.altimetrik.android.utilities.TextProgressBar;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

/**
 * Application class for our application.
 */
public class HolcimApp extends Application {

	public static DevOpenHelper dbHelper;
	public static SQLiteDatabase database;
	public static DaoMaster daoMaster;
	public static DaoSession daoSession;

	private String securedPrefKey = "H0lc1m5urv3y";
	static HolcimSecuredPreferences mSecuredPreferences;
	SharedPreferences mSharedPreferences;
	private boolean mCameFromBackground = false;
	private boolean mForceNoPin = false;
	private static HolcimApp singleton = null;
	private static String profile;
	public static String DOWNLOADING_UNIT = "%";


	public static boolean isSyncPending = false;

	public static HolcimApp getInstance() {
		return singleton;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}



	public void setUserPin(String pPin) {
		mSecuredPreferences.put("userPin", pPin);
	}

	public static String getUserPin() {
		return mSecuredPreferences.getString("userPin");
	}

	public Editor getSharedPreferencesEditor() {
		return mSharedPreferences.edit();
	}

	public SharedPreferences getSharedPreferences() {
		return mSharedPreferences;
	}

	public void setCameFromBackground(boolean value) {
		mCameFromBackground = value;
	}

	public void setForceNoPin(boolean value) {
		mForceNoPin = value;
	}

	public boolean isForceNoPin() {
		return mForceNoPin;
	}

	public boolean isComingFromBackground() {
		return mCameFromBackground;
	}

	public void checkForceNoPin() {
		if (HolcimApp.getInstance().isComingFromBackground() && HolcimApp.getInstance().isForceNoPin()) {
			HolcimApp.getInstance().setCameFromBackground(false);
		}
	}

	public boolean checkUserPin(String pPin) {
		if (mSecuredPreferences.getString("userPin").equals(pPin)) {
			return true;
		}
		return false;
	}

	public static void publishProgress(Integer total, TextProgressBar textProgressBar){
		textProgressBar.setText(HolcimApp.getInstance().getResources().getString(R.string.progressbar_text) + " " + total + DOWNLOADING_UNIT);
		textProgressBar.setProgress(total);
	}

	@Override
	public void onCreate() {
		singleton = this;
		super.onCreate();
		dbHelper = new DaoMaster.DevOpenHelper(this, "holcim-mobile-db", null);
		database = dbHelper.getWritableDatabase();
		daoMaster = new DaoMaster(database);
		daoSession = daoMaster.newSession();
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mSecuredPreferences = new HolcimSecuredPreferences(this, securedPrefKey, false);

		//get profile from database
		if(daoSession.getUserDao().loadAll().size() != 0){
			profile = daoSession.getUserDao().loadAll().get(0).getProfile();
		}

		try {
			HolcimDataSource.CreateFilesDir(this);
			HolcimDataSource.deleteAllTemporalFiles(this);
		} catch (HolcimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkForceNoPin();
	}

	public static List<NameValuePair> GetTokenHeader() {
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Authorization", "Bearer " + accessTokens.get_access_token()));
		return headers;
	}

	public static String GetWebServicesURL() {
		return accessTokens.get_instance_url() + HolcimConsts.WEB_SERVICE_URL;
	}

	private static OAuthTokens accessTokens;

	/** Access Tokens**/
	public static OAuthTokens getAccessTokens() { return accessTokens; }
	public static void setAccessTokens(OAuthTokens accessTokens) { HolcimApp.accessTokens = accessTokens; }

}