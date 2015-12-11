package com.holcim.altimetrik.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.utilities.HolcimPasswordTransformationMethod;

public class HolcimSignIn extends HolcimCustomActivity {

	private EditText edtPin;
	private EditText edtPinConfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.pin_signin);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pin_signin);

		edtPin = (EditText)findViewById(R.id.edt_pin);
		edtPinConfirm = (EditText)findViewById(R.id.edt_confirm_pin);

		//do the PIN secret
		edtPin.setTransformationMethod(new HolcimPasswordTransformationMethod());
		edtPinConfirm.setTransformationMethod(new HolcimPasswordTransformationMethod());

		edtPin.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(edtPin.getText().length() == 4){
					edtPinConfirm.requestFocus();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		Button btnContinue = (Button)findViewById(R.id.btn_confrim_pin_signin);

		btnContinue.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (!validatePINEmpty()) {
					dialog.showError(getString(R.string.empty_sign_in_pin_modal_error_message), getString(R.string.ok), new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							resetPINFields();
						}
					});
				} else if(!validatePINMatch()){
					dialog.showError(getString(R.string.sign_in_pin_modal_error_message), getString(R.string.ok), new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							resetPINFields();
						}
					});
				}else if(edtPin.length() != 4){
					dialog.showError(getString(R.string.sign_in_pin_modal_length_error_message), getString(R.string.ok), new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							resetPINFields();
						}
					});
				}else{					
					HolcimApp.getInstance().setUserPin(getPin());
					HolcimCustomActivity.setOnback(true);
					Intent mainIntent = new Intent(HolcimSignIn.this, HolcimMainActivity.class);
					HolcimSignIn.this.startActivity(mainIntent);
				}
			}
		});
		
		setCustomTitle(getResources().getString(R.string.signin_activity_title));
		edtPin.requestFocus();
	}

	private void resetPINFields(){
		edtPin.setText("");
		edtPinConfirm.setText("");
		edtPin.requestFocus();
	}
	private boolean validatePINEmpty() {
		if (edtPin.getText().toString().equals("") || edtPinConfirm.getText().toString().equals("")){
			return false;
		}
		return true;
	}

	private boolean validatePINMatch(){
		if (!edtPin.getText().toString().equals(edtPinConfirm.getText().toString())) {
			return false;
		}
		return true;
	}

	private String getPin() {
		return edtPin.getText().toString();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
