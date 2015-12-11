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
	ImageButton camera0,camera1,camera2,camera3,camera4;
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
		camera0 = (ImageButton) findViewById(R.id.imageButton_camera);
        camera1 = (ImageButton) findViewById(R.id.imageButton_camera1);
        camera2 = (ImageButton) findViewById(R.id.imageButton_camera2);
        camera3 = (ImageButton) findViewById(R.id.imageButton_camera3);
        camera4 = (ImageButton) findViewById(R.id.imageButton_camera4);
        final EditText editTextPictureDescription = (EditText) findViewById(R.id.editText_picture_description);
        final EditText editTextPictureDescription1 = (EditText) findViewById(R.id.editText_picture_description1);
        final EditText editTextPictureDescription2 = (EditText) findViewById(R.id.editText_picture_description2);
        final EditText editTextPictureDescription3 = (EditText) findViewById(R.id.editText_picture_description3);
        final EditText editTextPictureDescription4 = (EditText) findViewById(R.id.editText_picture_description4);
        editTextPictureDescription.setEnabled(false);
        editTextPictureDescription1.setEnabled(false);
        editTextPictureDescription2.setEnabled(false);
        editTextPictureDescription3.setEnabled(false);
        editTextPictureDescription4.setEnabled(false);
        refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_FIELD_NAME, 0);
        refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_1_FIELD_NAME, 1);
        refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_2_FIELD_NAME, 2);
        refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_3_FIELD_NAME, 3);
        refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_4_FIELD_NAME, 4);

		camera0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
                editTextPictureDescription.setEnabled(true);
				saveActionLog();
				takePhoto(0);
			}
		});
        camera1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPictureDescription1.setEnabled(true);
                saveActionLog();
                takePhoto(1);
            }
        });
        camera2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPictureDescription2.setEnabled(true);
                saveActionLog();
                takePhoto(2);
            }
        });
        camera3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPictureDescription3.setEnabled(true);
                saveActionLog();
                takePhoto(3);
            }
        });
        camera4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPictureDescription4.setEnabled(true);
                saveActionLog();
                takePhoto(4);
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
			if (actionLog.getPictureDescription1() != null) {
				editTextPictureDescription1.setText(actionLog
						.getPictureDescription1());
				actionLogOldValues.setPictureDescription1(actionLog
						.getPictureDescription1());
			}
            if (actionLog.getPictureDescription2() != null) {
                editTextPictureDescription2.setText(actionLog
                        .getPictureDescription2());
                actionLogOldValues.setPictureDescription2(actionLog
                        .getPictureDescription2());
            }
            if (actionLog.getPictureDescription3() != null) {
                editTextPictureDescription3.setText(actionLog
                        .getPictureDescription3());
                actionLogOldValues.setPictureDescription3(actionLog
                        .getPictureDescription3());
            }
            if (actionLog.getPictureDescription4() != null) {
                editTextPictureDescription4.setText(actionLog
                        .getPictureDescription4());
                actionLogOldValues.setPictureDescription4(actionLog
                        .getPictureDescription4());
            }
            if (actionLog.getPicture() != null) {
                actionLogOldValues.setPicture(actionLog.getPicture());
            }
			if (actionLog.getPicture1() != null) {
				actionLogOldValues.setPicture1(actionLog.getPicture1());
			}
            if (actionLog.getPicture2() != null) {
                actionLogOldValues.setPicture2(actionLog.getPicture2());
            }
            if (actionLog.getPicture3() != null) {
                actionLogOldValues.setPicture3(actionLog.getPicture3());
            }
            if (actionLog.getPicture4() != null) {
                actionLogOldValues.setPicture4(actionLog.getPicture4());
            }
		}

		Button buttonFinish = (Button) findViewById(R.id.button_finish);
		buttonFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setFields(String.valueOf(editTextDescription.getText()),
						checkBoxComplaint.isChecked(), status, category,
						String.valueOf(editTextPictureDescription.getText()),
                        String.valueOf(editTextPictureDescription1.getText()),
                        String.valueOf(editTextPictureDescription2.getText()),
                        String.valueOf(editTextPictureDescription3.getText()),
                        String.valueOf(editTextPictureDescription4.getText()));
				saveActionLog();
				saveImage(HolcimConsts.ACTIONLOG_SF_IMAGE_FIELD_NAME, 0);
                saveImage(HolcimConsts.ACTIONLOG_SF_IMAGE_1_FIELD_NAME, 1);
                saveImage(HolcimConsts.ACTIONLOG_SF_IMAGE_2_FIELD_NAME, 2);
                saveImage(HolcimConsts.ACTIONLOG_SF_IMAGE_3_FIELD_NAME, 3);
                saveImage(HolcimConsts.ACTIONLOG_SF_IMAGE_4_FIELD_NAME, 4);
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
			editTextPictureDescription1.setKeyListener(null);
            editTextPictureDescription2.setKeyListener(null);
            editTextPictureDescription3.setKeyListener(null);
            editTextPictureDescription4.setKeyListener(null);
            editTextPictureDescription.setFocusableInTouchMode(false);
			editTextPictureDescription1.setFocusableInTouchMode(false);
            editTextPictureDescription2.setFocusableInTouchMode(false);
            editTextPictureDescription3.setFocusableInTouchMode(false);
            editTextPictureDescription4.setFocusableInTouchMode(false);
            camera0.setClickable(false);
			camera1.setClickable(false);
            camera2.setClickable(false);
            camera3.setClickable(false);
            camera4.setClickable(false);
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
				|| (actionLog.getPictureDescription1() != null
						&& actionLogOldValues.getPictureDescription1() != null && !actionLog
						.getPictureDescription1().equals(
								actionLogOldValues.getPictureDescription1()))
                || (actionLog.getPictureDescription2() != null
                && actionLogOldValues.getPictureDescription2() != null && !actionLog
                .getPictureDescription2().equals(
                        actionLogOldValues.getPictureDescription2()))
                || (actionLog.getPictureDescription3() != null
                && actionLogOldValues.getPictureDescription3() != null && !actionLog
                .getPictureDescription3().equals(
                        actionLogOldValues.getPictureDescription3()))
                || (actionLog.getPictureDescription4() != null
                && actionLogOldValues.getPictureDescription4() != null && !actionLog
                .getPictureDescription4().equals(
                        actionLogOldValues.getPictureDescription4()))
				|| (actionLog.getPicture() != null
						&& actionLogOldValues.getPicture() != null && !actionLog
						.getPicture().equals(actionLogOldValues.getPicture()))
                || (actionLog.getPicture1() != null
                        && actionLogOldValues.getPicture1() != null && !actionLog
                        .getPicture1().equals(actionLogOldValues.getPicture1()))
                || (actionLog.getPicture2() != null
                        && actionLogOldValues.getPicture2() != null && !actionLog
                        .getPicture2().equals(actionLogOldValues.getPicture2()))
                || (actionLog.getPicture3() != null
                        && actionLogOldValues.getPicture3() != null && !actionLog
                        .getPicture3().equals(actionLogOldValues.getPicture3()))
                || (actionLog.getPicture4() != null
                        && actionLogOldValues.getPicture4() != null && !actionLog
                        .getPicture4().equals(actionLogOldValues.getPicture4()))) {
			actionLog.setIsEdited(true);
			HolcimApp.daoSession.update(actionLog);
		}
	}

	private void saveImage(String field, int photoNumber) {
		try {
			if (AltimetrikFileHandler.isFileExist(actionLog
					.getTempActionLogPictureFilePath(this, actionLog.getId(), photoNumber))) {
				if (actionLog.isPictureFileExist(this, field, photoNumber)) {
					AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
							this);
					try {
						fileHandler.moveFile(actionLog.getTempActionLogPictureFilePath(
                                this, actionLog.getId(), photoNumber),
                                HolcimDataSource.getActionLogDir(this) + File.separator	+ actionLog.getTempActionLogPictureFileName(this,actionLog.getId(), photoNumber));
						AltimetrikFileHandler.DeleteFile(actionLog.getPicturePath(this, field, photoNumber));
						AltimetrikFileHandler.renameFile(HolcimDataSource.getActionLogDir(this)	+ File.separator + actionLog.getTempActionLogPictureFileName(this, actionLog.getId(), photoNumber),
										HolcimDataSource.getActionLogDir(this) + File.separator	+ actionLog.getPictureFileName(photoNumber));
					} catch (Exception e) {
						// TODO: handle exception
					}
				} else {
					try {
						AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
								this);
						fileHandler.moveFile(actionLog.getTempActionLogPictureFilePath(this, actionLog.getId(), photoNumber),
										HolcimDataSource.getActionLogDir(this) + File.separator	+ actionLog.getTempActionLogPictureFileName(this, actionLog.getId(), photoNumber));
						AltimetrikFileHandler.renameFile(HolcimDataSource.getActionLogDir(this)	+ File.separator + actionLog.getTempActionLogPictureFileName(this, actionLog.getId(), photoNumber),
										HolcimDataSource.getActionLogDir(this) + File.separator	+ actionLog.getPictureFileName(photoNumber));
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

	public void takePhoto(int photoNumber) {
		Intent mIntent = new Intent(this, HolcimCameraActivity.class);
		mIntent.putExtra("isFeedback", true);
        mIntent.putExtra("photoNumber", photoNumber);
		mIntent.putExtra("actionLogId", actionLog.getId());
		startActivityForResult(mIntent,
				HolcimConsts.ACTIVITY_REQUEST_CAMERA_LOG);
	}

	public void refreshImage(String field, int photoNumber) {
		try {
            if (actionLog.getId() != null && actionLog.isActionLogPictureTempFileExist(this,actionLog.getId(), photoNumber)) {
                if(photoNumber == 0){
                    camera0.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(
                            actionLog.getTempActionLogPictureFilePath(this, actionLog.getId(), photoNumber), 200, 200, 0));
                    ((EditText) findViewById(R.id.editText_picture_description)).setEnabled(true);
                }
                else if(photoNumber == 1){
                    camera1.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(
                            actionLog.getTempActionLogPictureFilePath(this, actionLog.getId(), photoNumber), 200, 200, 0));
                    ((EditText) findViewById(R.id.editText_picture_description1)).setEnabled(true);
                }
                else if(photoNumber == 2){
                    camera2.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(
                            actionLog.getTempActionLogPictureFilePath(this, actionLog.getId(), photoNumber), 200, 200, 0));
                    ((EditText) findViewById(R.id.editText_picture_description2)).setEnabled(true);
                }
                else if(photoNumber == 3){
                    camera3.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(
                            actionLog.getTempActionLogPictureFilePath(this, actionLog.getId(), photoNumber), 200, 200, 0));
                    ((EditText) findViewById(R.id.editText_picture_description3)).setEnabled(true);
                }
                else if(photoNumber == 4){
                    camera4.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(
                            actionLog.getTempActionLogPictureFilePath(this, actionLog.getId(), photoNumber), 200, 200, 0));
                    ((EditText) findViewById(R.id.editText_picture_description4)).setEnabled(true);
                }
            } else if (actionLog.getId() != null
                    && actionLog.isPictureFileExist(this, field, photoNumber)) {
                if(photoNumber == 0){
                    camera0.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(actionLog.getPicturePath(this, field, photoNumber), 200, 200, 0));
                    ((EditText) findViewById(R.id.editText_picture_description)).setEnabled(true);
                }
                else if(photoNumber == 1){
                    camera1.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(actionLog.getPicturePath(this, field, photoNumber), 200, 200, 0));
                    ((EditText) findViewById(R.id.editText_picture_description1)).setEnabled(true);
                }
                else if(photoNumber == 2){
                    camera2.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(actionLog.getPicturePath(this, field, photoNumber), 200, 200, 0));
                    ((EditText) findViewById(R.id.editText_picture_description2)).setEnabled(true);
                }
                else if(photoNumber == 3){
                    camera3.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(actionLog.getPicturePath(this, field, photoNumber), 200, 200, 0));
                    ((EditText) findViewById(R.id.editText_picture_description3)).setEnabled(true);
                }
                else if(photoNumber == 4){
                    camera4.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(actionLog.getPicturePath(this, field, photoNumber), 200, 200, 0));
                    ((EditText) findViewById(R.id.editText_picture_description4)).setEnabled(true);
                }
            } else {
                if(photoNumber == 0){
                    camera0.setImageResource(R.drawable.camera);
                }
                else if(photoNumber == 1){
                    camera1.setImageResource(R.drawable.camera);
                }
                else if(photoNumber == 2){
                    camera2.setImageResource(R.drawable.camera);
                }
                else if(photoNumber == 3){
                    camera3.setImageResource(R.drawable.camera);
                }
                else if(photoNumber == 4){
                    camera4.setImageResource(R.drawable.camera);
                }
            }
		} catch (HolcimException e) {
			e.printStackTrace();
		}
	}

	public void setFields(String description, Boolean complaint, String status,
			String category, String pictureDescription, String pictureDescription1, String pictureDescription2,
            String pictureDescription3 , String pictureDescription4){
		actionLog.setDescription(description);
		actionLog.setComplaint(complaint);
		actionLog.setStatus(status);
		actionLog.setCategory(category);
        actionLog.setPictureDescription(pictureDescription);
		actionLog.setPictureDescription1(pictureDescription1);
        actionLog.setPictureDescription2(pictureDescription2);
        actionLog.setPictureDescription3(pictureDescription3);
        actionLog.setPictureDescription4(pictureDescription4);
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
            refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_FIELD_NAME, 0);
            refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_1_FIELD_NAME, 1);
            refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_2_FIELD_NAME, 2);
            refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_3_FIELD_NAME, 3);
            refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_4_FIELD_NAME, 4);
		}
	}
}
