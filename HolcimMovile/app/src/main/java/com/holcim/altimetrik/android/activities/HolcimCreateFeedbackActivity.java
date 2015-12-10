package com.holcim.altimetrik.android.activities;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.ActionsLog;
import com.holcim.altimetrik.android.utilities.AltimetrikFileHandler;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimUtility;

public class HolcimCreateFeedbackActivity extends HolcimCustomActivity {
	private ActionsLog actionLog;
	private ActionsLog actionLogOldValues;
	String category = "";
	String status = "";
	int position = -1;
	ImageButton camera;
	private Long saleExecutionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.action_log);
		super.onCreate(savedInstanceState);

		ImageView homePage = (ImageView) findViewById(R.id.imageButton_home);
		homePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.showWarning(getString(R.string.message_warning_go_home),
						getString(R.string.ok), getString(R.string.cancel),
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
								HolcimCustomActivity.setOnback(true);
								Intent intent = new Intent(
										HolcimCreateFeedbackActivity.this,
										HolcimMainActivity.class);
								startActivity(intent);
							}
						}, dialog.mDismissClickListener);
			}
		});

		if (getIntent().getExtras() != null) {
			actionLog = (ActionsLog) getIntent().getSerializableExtra(
					HolcimConsts.OBJECT_SERIALIZABLE_KEY);
			actionLogOldValues = new ActionsLog();
			position = getIntent().getExtras().getInt("isEditing");
			saleExecutionId = getIntent().getExtras()
					.getLong("saleExecutionId");
		}

		TextView textViewFieldNameActionLogNumber = (TextView) findViewById(R.id.textView_field_value_actionLogNumber);

		TextView textViewFieldNameDescription = (TextView) findViewById(R.id.textView_field_name_description);
		textViewFieldNameDescription.setText(getResources().getStringArray(
				R.array.feedback_fields)[3]);
		final EditText editTextDescription = (EditText) findViewById(R.id.editText_description);

		TextView textViewFieldNameComplaint = (TextView) findViewById(R.id.textView_field_name_complaint);
		textViewFieldNameComplaint.setText(getResources().getStringArray(
				R.array.feedback_fields)[0]);
		final CheckBox checkBoxComplaint = (CheckBox) findViewById(R.id.checkBox_complaint);

		TextView textViewFieldNameStatus = (TextView) findViewById(R.id.textView_field_name_status);
		textViewFieldNameStatus.setText(getResources().getStringArray(
				R.array.feedback_fields)[1]);
		final Spinner spinnerStatus = (Spinner) findViewById(R.id.spinner_status);
		final List<String> statusList = Arrays.asList(getResources()
				.getStringArray(R.array.feedback_status_values));
		ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner, statusList);
		spinnerStatus.setAdapter(statusAdapter);

		if (actionLog == null) {
			actionLog = new ActionsLog();
		}
		camera = (ImageButton) findViewById(R.id.imageButton_camera);
		refreshImage();
		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				saveActionLog();
				takePhoto();
			}
		});

		spinnerStatus.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spinnerStatus.getAdapter() != null
						&& spinnerStatus.getAdapter().getItem(position) != null
						&& !statusList.get(position).equals("")) {
					status = statusList.get(position);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		final EditText editTextPictureDescription = (EditText) findViewById(R.id.editText_picture_description);
		TextView textViewFieldNameCategory = (TextView) findViewById(R.id.textView_field_name_category);
		textViewFieldNameCategory.setText(getResources().getStringArray(
				R.array.feedback_fields)[2]);
		final Spinner spinnerCategory = (Spinner) findViewById(R.id.spinner_category);
		final List<String> categoryList = Arrays.asList(getResources()
				.getStringArray(R.array.feedback_category_values));
		ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner, categoryList);
		spinnerCategory.setAdapter(categoryAdapter);

		spinnerCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spinnerCategory.getAdapter() != null
						&& spinnerCategory.getAdapter().getItem(position) != null
						&& !categoryList.get(position).equals("")) {
					category = categoryList.get(position);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		if (actionLog != null) {
			if (actionLog.getDescription() != null) {
				editTextDescription.setText(actionLog.getDescription());
				actionLogOldValues.setDescription(actionLog.getDescription());
			}
			if (actionLog.getComplaint() != null) {
				checkBoxComplaint.setChecked(actionLog.getComplaint());
				actionLogOldValues.setComplaint(actionLog.getComplaint());
			}
			if (actionLog.getStatus() != null) {
				spinnerStatus.setSelection(statusList.indexOf(actionLog
						.getStatus()));
				actionLogOldValues.setStatus(actionLog.getStatus());
			}
			if (actionLog.getCategory() != null) {
				spinnerCategory.setSelection(categoryList.indexOf(actionLog
						.getCategory()));
				actionLogOldValues.setCategory(actionLog.getCategory());
			}
			if (actionLog.getActionLogNumber() != null) {
				textViewFieldNameActionLogNumber.setText(actionLog
						.getActionLogNumber());
			}
			if (actionLog.getPictureDescription() != null) {
				editTextPictureDescription.setText(actionLog
						.getPictureDescription());
				actionLogOldValues.setPictureDescription(actionLog
						.getPictureDescription());
			}
			if (actionLog.getPicture() != null) {
				actionLogOldValues.setPicture(actionLog.getPicture());
			}
		}

		Button buttonFinish = (Button) findViewById(R.id.button_finish);
		buttonFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setFields(String.valueOf(editTextDescription.getText()),
						checkBoxComplaint.isChecked(), status, category,
						String.valueOf(editTextPictureDescription.getText()));
				saveActionLog();
				saveImage();
				setIsActionLogEdited();

				HolcimCustomActivity.setOnback(true);
				Intent returnIntent = new Intent(
						HolcimCreateFeedbackActivity.this,
						HolcimSelectedRetailerActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(HolcimConsts.OBJECT_SERIALIZABLE_KEY,
						actionLog);
				returnIntent.putExtras(mBundle);
				returnIntent.putExtra("isEditing", position);
				setResult(HolcimConsts.ACTIVITY_RESULT_CODE_ACTION_LOG_OK,
						returnIntent);
				finish();

			}
		});

		if (spinnerStatus.getSelectedItem().toString().equals("Completed")) {
			editTextDescription.setKeyListener(null);
			editTextDescription.setFocusableInTouchMode(false);
			checkBoxComplaint.setKeyListener(null);
			spinnerStatus.setClickable(false);
			spinnerCategory.setClickable(false);
			editTextPictureDescription.setKeyListener(null);
			editTextPictureDescription.setFocusableInTouchMode(false);
			camera.setClickable(false);
			buttonFinish.setVisibility(View.GONE);

		}

		setCustomTitle(getString(R.string.feedback_title));

	}

	private void saveActionLog() {
		actionLog.setSaleExecutionId(saleExecutionId);
		if (actionLog.getId() == null || actionLog.getId() < 0) {
			actionLog.setIsEdited(true);
			HolcimApp.daoSession.insert(actionLog);
		} else {
			HolcimApp.daoSession.update(actionLog);
		}
	}

	private void setIsActionLogEdited() {
		if (actionLog.getIsEdited()
				|| (actionLog.getDescription() != null
						&& actionLogOldValues.getDescription() != null && !actionLog
						.getDescription().equals(
								actionLogOldValues.getDescription()))
				|| (!actionLog.getComplaint().equals(
						actionLogOldValues.getComplaint()))
				|| (actionLog.getStatus() != null
						&& actionLogOldValues.getStatus() != null && !actionLog
						.getStatus().equals(actionLogOldValues.getStatus()))
				|| (actionLog.getCategory() != null
						&& actionLogOldValues.getCategory() != null && !actionLog
						.getCategory().equals(actionLogOldValues.getCategory()))
				|| (actionLog.getPictureDescription() != null
						&& actionLogOldValues.getPictureDescription() != null && !actionLog
						.getPictureDescription().equals(
								actionLogOldValues.getPictureDescription()))
				|| (actionLog.getPicture() != null
						&& actionLogOldValues.getPicture() != null && !actionLog
						.getPicture().equals(actionLogOldValues.getPicture()))) {
			actionLog.setIsEdited(true);
			HolcimApp.daoSession.update(actionLog);
		}
	}

	private void saveImage() {
		try {
			if (AltimetrikFileHandler.isFileExist(actionLog
					.getTempActionLogPictureFilePath(this, actionLog.getId()))) {
				if (actionLog.isPictureFileExist(this)) {
					AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
							this);
					try {
						fileHandler
								.moveFile(
										actionLog
												.getTempActionLogPictureFilePath(
														this, actionLog.getId()),
										HolcimDataSource.getActionLogDir(this)
												+ File.separator
												+ actionLog
														.getTempActionLogPictureFileName(
																this,
																actionLog
																		.getId()));
						AltimetrikFileHandler.DeleteFile(actionLog
								.getPicturePath(this));
						AltimetrikFileHandler
								.renameFile(
										HolcimDataSource.getActionLogDir(this)
												+ File.separator
												+ actionLog
														.getTempActionLogPictureFileName(
																this,
																actionLog
																		.getId()),
										HolcimDataSource.getActionLogDir(this)
												+ File.separator
												+ actionLog
														.getPictureFileName());
					} catch (Exception e) {
						// TODO: handle exception
					}
				} else {
					try {
						AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
								this);
						fileHandler
								.moveFile(
										actionLog
												.getTempActionLogPictureFilePath(
														this, actionLog.getId()),
										HolcimDataSource.getActionLogDir(this)
												+ File.separator
												+ actionLog
														.getTempActionLogPictureFileName(
																this,
																actionLog
																		.getId()));
						AltimetrikFileHandler
								.renameFile(
										HolcimDataSource.getActionLogDir(this)
												+ File.separator
												+ actionLog
														.getTempActionLogPictureFileName(
																this,
																actionLog
																		.getId()),
										HolcimDataSource.getActionLogDir(this)
												+ File.separator
												+ actionLog
														.getPictureFileName());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				actionLog.setIsEdited(true);
			}
		} catch (HolcimException e) {
			e.printStackTrace();
		}
	}

	public void takePhoto() {
		Intent mIntent = new Intent(this, HolcimCameraActivity.class);
		mIntent.putExtra("isFeedback", true);
		mIntent.putExtra("actionLogId", actionLog.getId());
		startActivityForResult(mIntent,
				HolcimConsts.ACTIVITY_REQUEST_CAMERA_LOG);
	}

	public void refreshImage() {
		try {
			if (actionLog.getSalesforceId() != null
					&& !actionLog.getSalesforceId().equals("")
					&& (actionLog.getPicture() == null || actionLog
							.getPicture().equals(""))) {
				camera.setImageResource(R.drawable.camera);
			} else {
				if (actionLog.getId() != null
						&& actionLog.isActionLogPictureTempFileExist(this,
								actionLog.getId())) {
					camera.setImageBitmap(HolcimUtility
							.decodeSampledBitmapFromFile(actionLog
									.getTempActionLogPictureFilePath(this,
											actionLog.getId()), 200, 200, 0));
				} else if (actionLog.getId() != null
						&& actionLog.isPictureFileExist(this)) {
					camera.setImageBitmap(HolcimUtility
							.decodeSampledBitmapFromFile(
									actionLog.getPicturePath(this), 200, 200, 0));
				} else {
					camera.setImageResource(R.drawable.camera);
				}
			}
		} catch (HolcimException e) {
			e.printStackTrace();
		}
	}

	public void setFields(String description, Boolean complaint, String status,
			String category, String pictureDescription) {
		actionLog.setDescription(description);
		actionLog.setComplaint(complaint);
		actionLog.setStatus(status);
		actionLog.setCategory(category);
		actionLog.setPictureDescription(pictureDescription);
	}

	@Override
	public void onBackPressed() {
		if (!HolcimCustomActivity.blockback) {
			HolcimCustomActivity.setOnback(true);
			Intent returnIntent = new Intent(HolcimCreateFeedbackActivity.this,
					HolcimSelectedRetailerActivity.class);
			setResult(-1, returnIntent);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == HolcimConsts.ACTIVITY_REQUEST_CAMERA_LOG
				&& resultCode == HolcimConsts.ACTIVITY_RESULT_CODE_CAMERA_OK) {
			refreshImage();
		}
	}
}
