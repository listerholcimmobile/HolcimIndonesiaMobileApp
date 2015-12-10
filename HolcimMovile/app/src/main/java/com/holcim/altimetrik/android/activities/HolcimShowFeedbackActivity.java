package com.holcim.altimetrik.android.activities;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.holcim.hsea.R;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.ActionsLog;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimUtility;

public class HolcimShowFeedbackActivity extends HolcimCustomActivity{
	private ActionsLog actionLog;
	//private ActionsLog actionLogOldValues;
	String category = "";
	String status = "";
	int position = -1;
	ImageButton camera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.feedback_detail);
		super.onCreate(savedInstanceState);

		ImageView homePage = (ImageView)findViewById(R.id.imageButton_home);
		homePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				HolcimCustomActivity.setOnback(true);
				Intent intent = new Intent(HolcimShowFeedbackActivity.this, HolcimMainActivity.class);
				startActivity(intent);
			}

		});

		if (getIntent().getExtras() != null) {
			actionLog = (ActionsLog) getIntent().getSerializableExtra(HolcimConsts.OBJECT_SERIALIZABLE_KEY);
			position = getIntent().getExtras().getInt("isEditing");
		}

		TextView textViewFieldNameActionLogNumber = (TextView)findViewById(R.id.textView_field_value_actionLogNumber);

		TextView textViewFieldNameDescription = (TextView)findViewById(R.id.textView_field_name_description);
		textViewFieldNameDescription.setText(getResources().getStringArray(R.array.feedback_fields)[3]);
		final TextView textViewDescription = (TextView)findViewById(R.id.txt_description);

		TextView textViewFieldNameComplaint = (TextView)findViewById(R.id.textView_field_name_complaint);
		textViewFieldNameComplaint.setText(getResources().getStringArray(R.array.feedback_fields)[0]);
		final CheckBox checkBoxComplaint = (CheckBox)findViewById(R.id.checkBox_complaint);

		TextView textViewFieldNameStatus = (TextView)findViewById(R.id.textView_field_name_status);
		textViewFieldNameStatus.setText(getResources().getStringArray(R.array.feedback_fields)[1]);
		final TextView textViewStatus = (TextView)findViewById(R.id.txt_status);
		
		if(actionLog == null){
			actionLog = new ActionsLog();
		}
		camera = (ImageButton)findViewById(R.id.imageButton_camera);
		refreshImage();	

		final TextView textViewPictureDescription = (TextView)findViewById(R.id.textView_picture_description);
		TextView textViewFieldNameCategory = (TextView) findViewById(R.id.textView_field_name_category);
		textViewFieldNameCategory.setText(getResources().getStringArray(R.array.feedback_fields)[2]);
		final TextView textViewCategory = (TextView) findViewById(R.id.txt_category);

		if(actionLog != null){
			if(actionLog.getDescription() != null){
				textViewDescription.setText(actionLog.getDescription());
			}
			if(actionLog.getComplaint() != null){
				checkBoxComplaint.setChecked(actionLog.getComplaint());
			}
			if(actionLog.getStatus() != null){
				textViewStatus.setText(actionLog.getStatus());
			}
			if(actionLog.getCategory() != null){
				textViewCategory.setText(actionLog.getCategory());
			}
			if(actionLog.getActionLogNumber() != null){
				textViewFieldNameActionLogNumber.setText(actionLog.getActionLogNumber());
			}
			if(actionLog.getPictureDescription() != null){
				textViewPictureDescription.setText(actionLog.getPictureDescription());
			}
		}

		Button buttonFinish = (Button)findViewById(R.id.button_finish);
		buttonFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				HolcimCustomActivity.setOnback(true);
				Intent returnIntent = new Intent(HolcimShowFeedbackActivity.this, HolcimSelectedRetailerActivity.class);
				Bundle mBundle = new Bundle(); 
				mBundle.putSerializable(HolcimConsts.OBJECT_SERIALIZABLE_KEY, actionLog); 
				returnIntent.putExtras(mBundle); 
				returnIntent.putExtra("isEditing", position);
				setResult(HolcimConsts.ACTIVITY_RESULT_CODE_ACTION_LOG_OK, returnIntent);
				finish();

			}
		});

		textViewDescription.setKeyListener(null);
		textViewDescription.setFocusableInTouchMode(false);
		checkBoxComplaint.setKeyListener(null);
		textViewStatus.setClickable(false);
		textViewCategory.setClickable(false);
		textViewPictureDescription.setKeyListener(null);
		textViewPictureDescription.setFocusableInTouchMode(false);
		camera.setClickable(false);
		buttonFinish.setVisibility(View.GONE);
		
		setCustomTitle(getString(R.string.feedback_title));

	}

	public void refreshImage() {
		try {
			if(actionLog.getSalesforceId()!=null && !actionLog.getSalesforceId().equals("") && (actionLog.getPicture() == null || actionLog.getPicture().equals(""))){
				camera.setImageResource(R.drawable.camera);
			}else{
				if (actionLog.getId() != null && actionLog.isActionLogPictureTempFileExist(this, actionLog.getId())) {
					camera.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(actionLog.getTempActionLogPictureFilePath(this, actionLog.getId()), 200, 200, 0));
				}else if(actionLog.getId() != null && actionLog.isPictureFileExist(this)) {
					camera.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(actionLog.getPicturePath(this), 200, 200, 0));
				}else {
					camera.setImageResource(R.drawable.camera);
				}
			}
		} catch (HolcimException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		if(!HolcimCustomActivity.blockback){
			HolcimCustomActivity.setOnback(true);
			Intent returnIntent = new Intent(HolcimShowFeedbackActivity.this, HolcimSelectedRetailerActivity.class);
			setResult(-1, returnIntent);
			finish();
		}	
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == HolcimConsts.ACTIVITY_REQUEST_CAMERA_LOG && resultCode == HolcimConsts.ACTIVITY_RESULT_CODE_CAMERA_OK) {
			refreshImage();
		}
	}
}
