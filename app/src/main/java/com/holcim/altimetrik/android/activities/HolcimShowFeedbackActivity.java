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
	ImageButton camera0,camera1,camera2,camera3,camera4;

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

        camera0 = (ImageButton) findViewById(R.id.imageButton_camera);
        camera1 = (ImageButton) findViewById(R.id.imageButton_camera1);
        camera2 = (ImageButton) findViewById(R.id.imageButton_camera2);
        camera3 = (ImageButton) findViewById(R.id.imageButton_camera3);
        camera4 = (ImageButton) findViewById(R.id.imageButton_camera4);
        refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_FIELD_NAME, 0);
        refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_1_FIELD_NAME, 1);
        refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_2_FIELD_NAME, 2);
        refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_3_FIELD_NAME, 3);
        refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_4_FIELD_NAME, 4);

        final TextView textViewPictureDescription = (TextView)findViewById(R.id.textView_picture_description);
		final TextView textViewPictureDescription1 = (TextView)findViewById(R.id.textView_picture_description1);
        final TextView textViewPictureDescription2 = (TextView)findViewById(R.id.textView_picture_description2);
        final TextView textViewPictureDescription3 = (TextView)findViewById(R.id.textView_picture_description3);
        final TextView textViewPictureDescription4 = (TextView)findViewById(R.id.textView_picture_description4);
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
			if(actionLog.getPictureDescription1() != null){
				textViewPictureDescription1.setText(actionLog.getPictureDescription1());
			}
            if(actionLog.getPictureDescription2() != null){
                textViewPictureDescription2.setText(actionLog.getPictureDescription2());
            }
            if(actionLog.getPictureDescription3() != null){
                textViewPictureDescription3.setText(actionLog.getPictureDescription3());
            }
            if(actionLog.getPictureDescription4() != null){
                textViewPictureDescription4.setText(actionLog.getPictureDescription4());
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
		textViewPictureDescription1.setKeyListener(null);
        textViewPictureDescription2.setKeyListener(null);
        textViewPictureDescription3.setKeyListener(null);
        textViewPictureDescription4.setKeyListener(null);
        textViewPictureDescription.setFocusableInTouchMode(false);
		textViewPictureDescription1.setFocusableInTouchMode(false);
        textViewPictureDescription2.setFocusableInTouchMode(false);
        textViewPictureDescription3.setFocusableInTouchMode(false);
        textViewPictureDescription4.setFocusableInTouchMode(false);
        camera0.setClickable(false);
		camera1.setClickable(false);
        camera2.setClickable(false);
        camera3.setClickable(false);
        camera4.setClickable(false);
		buttonFinish.setVisibility(View.GONE);
		
		setCustomTitle(getString(R.string.feedback_title));

	}

    public void refreshImage(String field, int photoNumber) {
        try {
            if (actionLog.getId() != null && actionLog.isActionLogPictureTempFileExist(this,actionLog.getId(), photoNumber)) {
                if(photoNumber == 0){
                    camera0.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(
                            actionLog.getTempActionLogPictureFilePath(this, actionLog.getId(), photoNumber), 200, 200, 0));
                }
                else if(photoNumber == 1){
                    camera1.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(
                            actionLog.getTempActionLogPictureFilePath(this, actionLog.getId(), photoNumber), 200, 200, 0));
                }
                else if(photoNumber == 2){
                    camera2.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(
                            actionLog.getTempActionLogPictureFilePath(this, actionLog.getId(), photoNumber), 200, 200, 0));
                }
                else if(photoNumber == 3){
                    camera3.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(
                            actionLog.getTempActionLogPictureFilePath(this, actionLog.getId(), photoNumber), 200, 200, 0));
                }
                else if(photoNumber == 4){
                    camera4.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(
                            actionLog.getTempActionLogPictureFilePath(this, actionLog.getId(), photoNumber), 200, 200, 0));
                }
            } else if (actionLog.getId() != null
                    && actionLog.isPictureFileExist(this, field, photoNumber)) {
                if(photoNumber == 0){
                    camera0.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(actionLog.getPicturePath(this, field, photoNumber), 200, 200, 0));
                }
                else if(photoNumber == 1){
                    camera1.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(actionLog.getPicturePath(this, field, photoNumber), 200, 200, 0));
                }
                else if(photoNumber == 2){
                    camera2.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(actionLog.getPicturePath(this, field, photoNumber), 200, 200, 0));
                }
                else if(photoNumber == 3){
                    camera3.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(actionLog.getPicturePath(this, field, photoNumber), 200, 200, 0));
                }
                else if(photoNumber == 4){
                    camera4.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(actionLog.getPicturePath(this, field, photoNumber), 200, 200, 0));
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
            refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_FIELD_NAME, 0);
            refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_1_FIELD_NAME, 1);
            refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_2_FIELD_NAME, 2);
            refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_3_FIELD_NAME, 3);
            refreshImage(HolcimConsts.ACTIONLOG_SF_IMAGE_4_FIELD_NAME, 4);
		}
	}
}
