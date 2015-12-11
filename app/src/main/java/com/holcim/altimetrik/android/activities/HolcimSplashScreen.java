package com.holcim.altimetrik.android.activities;

import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.utilities.HolcimConsts;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class HolcimSplashScreen extends Activity {
	private static final int mSplashTimeout = 1300; // milliseconds
	private boolean firstRun = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		TextView lblVersion = (TextView) findViewById(R.id.lblversion);
		String versionName = null;
		try {
			versionName = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		lblVersion.setText(versionName);

		firstRun = HolcimApp.getInstance().getSharedPreferences()
				.getBoolean(HolcimConsts.KEY_FIRST_RUN, true);

		if (firstRun || HolcimApp.getUserPin() == null) {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					HolcimApp.getInstance().getSharedPreferencesEditor()
							.putBoolean(HolcimConsts.KEY_FIRST_RUN, false)
							.commit();
					startActivity(new Intent(HolcimSplashScreen.this,
							HolcimSignIn.class));
					finish();
				}
			}, mSplashTimeout);
		} else {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					Intent i = new Intent(HolcimSplashScreen.this,
							HolcimMainActivity.class);
					i.putExtra("isStart", true);
					startActivity(i);

					finish();
				}
			}, mSplashTimeout);
		}
	}
}
