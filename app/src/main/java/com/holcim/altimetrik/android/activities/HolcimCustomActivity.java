package com.holcim.altimetrik.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.utilities.HolcimBroadcastReciever;
import com.holcim.altimetrik.android.utilities.HolcimDialogHandler;
import com.holcim.altimetrik.android.utilities.HolcimPasswordTransformationMethod;
import com.holcim.altimetrik.android.utilities.MemoryBoss;

public class HolcimCustomActivity extends Activity {

	MemoryBoss mMemoryBoss;
	HolcimBroadcastReciever broadcastReciever;

	private static EditText edtPin;
	private TextView tvErrorMessage;
	private static boolean isModalVisible;
	View view;
	public HolcimDialogHandler dialog;
	private boolean showBack = true;
	private static boolean onback;
	public static boolean blockback;

	private String profile;

	// Indicates if is current week, next week or next two weeks
	private static String period;

	public static boolean getIsModalVisible() {
		return isModalVisible;
	}

	public static String getPeriod() {
		return period;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String p) {
		this.profile = p;
	}

	public static void setPeriod(String newPeriod) {
		period = newPeriod;
	}

	public static boolean isOnback() {
		return onback;
	}

	public static void setOnback(boolean isOnback) {
		onback = isOnback;
	}

	/**
	 * Change the focus to edtPin1 it is needed when the user is filling a
	 * survey the ODK change focus to the question
	 */
	public static void changeFocus() {
		if (edtPin != null) {
			edtPin.requestFocus();
		}
	}

	public boolean getShowBack() {
		return this.showBack;
	}

	public void setShowBack(boolean value) {
		this.showBack = value;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			mMemoryBoss = new MemoryBoss();
			registerComponentCallbacks(mMemoryBoss);
		}
		broadcastReciever = new HolcimBroadcastReciever();
		registerReceiver(broadcastReciever, new IntentFilter(
				Intent.ACTION_SCREEN_ON));
		this.dialog = new HolcimDialogHandler(this);
		HolcimApp.getInstance().setCameFromBackground(false);

	}

	@Override
	protected void onStop() {
		super.onStop();
		if (!onback) {
			HolcimApp.getInstance().setCameFromBackground(true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (this instanceof HolcimSignIn) {
			HolcimApp.getInstance().setForceNoPin(true);
		}
		HolcimApp.getInstance().checkForceNoPin();
		if (HolcimApp.getInstance().isComingFromBackground() && !isModalVisible
				&& !onback) {
			showPINAndKeyboard();
		}
		onback = false;
	}

	public void showPINAndKeyboard() {
		blockback = true;
		if (dialog.isShowing()) {
			dialog.hideDialog();
		}
		PINModal();

		edtPin = (EditText) view.findViewById(R.id.edt_pin);
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// only will trigger it if no physical keyboard is open
		inputMethodManager.showSoftInput(edtPin,
				InputMethodManager.SHOW_IMPLICIT);

	}

	public void PINModal() {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.modal_pin_request, null);

		final PopupWindow popUpReject = new PopupWindow(view,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, false);
		popUpReject.setOutsideTouchable(false);

		edtPin = (EditText) view.findViewById(R.id.edt_pin);

		// do the PIN secret
		edtPin.setTransformationMethod(new HolcimPasswordTransformationMethod());

		tvErrorMessage = (TextView) view.findViewById(R.id.tv_pin_error);
		Button btnContinue = (Button) view
				.findViewById(R.id.btn_confrim_pin_check);
		btnContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validatePin()
						&& HolcimApp.getInstance().checkUserPin(getPin())) {
					blockback = false;
					((ViewGroup) view.getParent()).removeView(view);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					System.gc();
					isModalVisible = false;
					HolcimApp.getInstance().setCameFromBackground(false);
					if (dialog.isHidden()) {
						dialog.showDialog();
					}
				} else if (!validatePINEmpty()) {
					tvErrorMessage
							.setText(getString(R.string.empty_sign_in_pin_modal_error_message));
					showError();
				} else {
					tvErrorMessage
							.setText(getString(R.string.modal_pin_check_wrong_pin_error));
					showError();
				}
			}
		});

		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		this.addContentView(view, layoutParams);
		edtPin.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(edtPin, 0);
		isModalVisible = true;
	}

	private boolean validatePINEmpty() {
		if (edtPin.getText().toString().equals("")) {
			return false;
		}
		return true;
	}

	private void showError() {
		edtPin.setText("");
		tvErrorMessage.setVisibility(View.VISIBLE);
		edtPin.requestFocus();
	}

	private boolean validatePin() {
		if (edtPin.getText().toString().equals("")) {
			return false;
		}
		return true;
	}

	private String getPin() {
		return edtPin.getText().toString();
	}

	protected void requestCustomTitle() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	}

	protected void setCustomTitle(String msg) {
		// set custom title bar
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_titlebar);

		TextView tv = (TextView) getWindow()
				.findViewById(R.id.txv_topbar_title);
		tv.setText(msg);
	}
}
