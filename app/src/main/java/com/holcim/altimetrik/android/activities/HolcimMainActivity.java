package com.holcim.altimetrik.android.activities;

import java.util.Arrays;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.altimetrik.holcim.controller.HolcimController;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimUtility;

public class HolcimMainActivity extends HolcimCustomActivity {
	private Boolean isProduction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.home);
		super.onCreate(savedInstanceState);

		HolcimApp.getInstance().setForceNoPin(false);

		Button btnSync = (Button) findViewById(R.id.button_sync);
		Button btnViewPlan = (Button) findViewById(R.id.button_view_plan);
		Button btnViewContacts = (Button) findViewById(R.id.button_view_contacts);
		Button btnTeleSale = (Button) findViewById(R.id.button_tele_sale);

		// Version:
		TextView lblVersion = (TextView) findViewById(R.id.lblversion);
		String versionName = null;
		try {
			versionName = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		lblVersion.setText(versionName);
		// ------------------------------

		// btnTeleSale
		btnTeleSale.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(HolcimMainActivity.this,
						HolcimUnplannedVisitActivity.class);
				i.putExtra("tele_sale", true);
				startActivity(i);
			}
		});

		// telesale condition
		if (HolcimApp.getInstance().getProfile() != null
				&& HolcimApp.getInstance().getProfile()
						.equalsIgnoreCase(HolcimConsts.TSO)) {
			btnTeleSale.setVisibility(View.GONE);
		} else {
			btnTeleSale.setVisibility(View.VISIBLE);
		}

		// btnViewPlan
		btnViewPlan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(HolcimMainActivity.this,
						HolcimMyVisitPlanListActivity.class);
				startActivity(i);
			}
		});

		// btnViewContact
		btnViewContacts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HolcimMainActivity.this,
						HolcimContactActivity.class);
				startActivity(intent);
			}
		});

		// btnSync
		btnSync.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (HolcimUtility.isOnline(HolcimMainActivity.this)) {
					showSelectOrgModal();
				} else {
					dialog.showNoInternetRefreshDialog(dialog.mDismissClickListener);
				}
			}
		});

		setCustomTitle(getResources().getString(R.string.home));

		if (!isOnback()) {
			showPINAndKeyboard();
		}
	}

	@Override
	public void onBackPressed() {
		// when click back
	}

	String item;

	private void showSelectOrgModal() {

		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View modalView = layoutInflater.inflate(
				R.layout.modal_is_production_or_test, null);

		final PopupWindow popUpReject = new PopupWindow(modalView,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, false);
		// popUpReject.setContentView(modalView);
		popUpReject.setOutsideTouchable(false);

		Spinner spnOrgs = (Spinner) modalView.findViewById(R.id.spinner_org);
		Button btnOk = (Button) modalView.findViewById(R.id.btn_yes);
		Button btnCancel = (Button) modalView.findViewById(R.id.btn_no);

		final List<String> items = Arrays.asList(getResources().getStringArray(
				R.array.select_an_org_items));
		ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner, items);
		spnOrgs.setAdapter(reasonAdapter);
		spnOrgs.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					isProduction = true;
				} else {
					isProduction = false;
				}
				item = items.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncCheckLoginNecessary asyncLogin = new AsyncCheckLoginNecessary(
						HolcimMainActivity.this);
				asyncLogin.execute();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((ViewGroup) modalView.getParent()).removeView(modalView);
			}
		});

		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		this.addContentView(modalView, layoutParams);
	}

	// -------------------------------------------------------------

	private void onAsyncCheckLoginNecessaryFinish(Object response) {
		dialog.dismiss();
		if (response instanceof Boolean) {
			if (!(Boolean) response) {
				Intent i = new Intent(HolcimMainActivity.this,
						HolcimLoginActivity.class);
				HolcimLoginActivity.isProduction(isProduction);
				startActivity(i);
			} else {
				Intent i = new Intent(HolcimMainActivity.this,
						HolcimSyncActivity.class);
				startActivity(i);
			}
		}
	}

	public class AsyncCheckLoginNecessary extends AsyncTask<Void, Void, Object> {
		// Maintain attached activity for states change propose
		private HolcimMainActivity activity;
		// Keep the response of the database query
		private Object _response;
		// Flag that keep async task completed status
		private boolean completed;

		// Constructor
		private AsyncCheckLoginNecessary(HolcimMainActivity activity) {
			this.activity = activity;
		}

		// Pre execution actions
		@Override
		protected void onPreExecute() {
			// Start the splash screen dialog
			dialog.showProgress();
		}

		// Execution of the async task
		protected Object doInBackground(Void... params) {
			try {
				if (HolcimApp.getAccessTokens().get_access_token() == null
						|| HolcimApp.getAccessTokens().get_access_token()
								.equals("")) {
					// is necessary to call login
					return false;
				} else {
					// call check WS
					return HolcimController.checkLogin(activity); // TODO
				}
			} catch (Exception e) {
				return false;
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
				activity.onAsyncCheckLoginNecessaryFinish(_response);
			}
		}

		// Sets the current activity to the async task
		public void setActivity(HolcimMainActivity activity) {
			this.activity = activity;
			if (completed) {
				notifyActivityTaskCompleted();
			}
		}

	}

}
