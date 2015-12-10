package com.holcim.altimetrik.android.activities;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.altimetrik.holcim.fragments.FragmentPhotoView;
import com.altimetrik.holcim.fragments.FragmentPhotoView.FragmentPhotoViewActions;
import com.commonsware.cwac.camera.CameraFragment;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.utilities.AltimetrikException;
import com.holcim.altimetrik.android.utilities.AltimetrikFileHandler;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimSimpleCameraHost;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class HolcimCameraActivity extends HolcimCustomActivity implements
		FragmentPhotoViewActions {

	private ImageButton btnTakePict;
	private boolean isFeedback;
	private boolean isLandmarkPicture;
	private boolean isContactPicture;
	private Long actionLogId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);

		// Create the CameraFragment and add it to the layout
		CameraFragment f = new CameraFragment();
		// Set the CameraHost
		f.setHost(new HolcimSimpleCameraHost(this));
		getFragmentManager().beginTransaction()
				.add(R.id.frame_container, f, HolcimConsts.FRAGMENT_CAMERA_TAG)
				.commit();

		if (getIntent().getExtras() != null) {
			isFeedback = getIntent().getExtras().getBoolean("isFeedback");
			isContactPicture = getIntent().getExtras().getBoolean(
					"isContactPicture");
			isLandmarkPicture = getIntent().getExtras().getBoolean(
					"isLandmarkPicture");
			actionLogId = getIntent().getExtras().getLong("actionLogId");
		}

		// Set an onClickListener for a shutter button
		btnTakePict = (ImageButton) findViewById(R.id.imb_take_picture);
		btnTakePict.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takePicture();
			}
		});
		btnTakePict.setVisibility(View.VISIBLE);

		setCustomTitle(getString(R.string.camera_activity_title));
	}

	/**
	 * Checks that the CameraFragment exists and is visible to the user, then
	 * takes a picture.
	 */
	private void takePicture() {
		CameraFragment f = (CameraFragment) getFragmentManager()
				.findFragmentByTag(HolcimConsts.FRAGMENT_CAMERA_TAG);
		if (f != null && f.isVisible()) {
			f.takePicture();
		}
	}

	@Override
	public void CancelPhotoTake() {
		try {
			HolcimDataSource.deleteAllTemporalFiles(this);
		} catch (HolcimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CameraFragment f = new CameraFragment();
		// Set the CameraHost
		f.setHost(new HolcimSimpleCameraHost(this));
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.frame_container, f,
						HolcimConsts.FRAGMENT_CAMERA_TAG).commit();
		btnTakePict.setVisibility(View.VISIBLE);
	}

	byte[] resizeImage(byte[] input) {
		Bitmap original = BitmapFactory.decodeByteArray(input, 0, input.length);

		// resize keep ratio
		Matrix m = new Matrix();
		m.setRectToRect(
				new RectF(0, 0, original.getWidth(), original.getHeight()),
				new RectF(0, 0, 1024, 1024), Matrix.ScaleToFit.CENTER);
		Bitmap resized = Bitmap.createBitmap(original, 0, 0,
				original.getWidth(), original.getHeight(), m, true);

		ByteArrayOutputStream blob = new ByteArrayOutputStream();
		resized.compress(Bitmap.CompressFormat.JPEG, 100, blob);

		return blob.toByteArray();
	}

	@Override
	public void SavePhotoTake(byte[] bitmap) {
		try {
			byte[] resized = resizeImage(bitmap);

			// if (!isFeedback) {
			// if (!isLandmarkPicture) {
			// AltimetrikFileHandler.WriteBytesTodFile(HolcimDataSource
			// .getTempPhotoFile(this).getAbsolutePath(), resized);
			// } else {
			// AltimetrikFileHandler.WriteBytesTodFile(HolcimDataSource
			// .getTempLandmarkPhotoFile(this).getAbsolutePath(),
			// resized);
			// }
			// } else {
			// AltimetrikFileHandler.WriteBytesTodFile(HolcimDataSource
			// .getTempActionLogPhotoFile(this, actionLogId)
			// .getAbsolutePath(), resized);
			// }

			if (isFeedback) {
				AltimetrikFileHandler.WriteBytesTodFile(HolcimDataSource
						.getTempActionLogPhotoFile(this, actionLogId)
						.getAbsolutePath(), resized);
			} else if (isLandmarkPicture) {
				AltimetrikFileHandler.WriteBytesTodFile(HolcimDataSource
						.getTempLandmarkPhotoFile(this).getAbsolutePath(),
						resized);
			} else if (isContactPicture) {
				AltimetrikFileHandler.WriteBytesTodFile(HolcimDataSource
						.getTempContactPhotoFile(this).getAbsolutePath(),
						resized);
			} else {
				AltimetrikFileHandler.WriteBytesTodFile(HolcimDataSource
						.getTempPhotoFile(this).getAbsolutePath(), resized);
			}
		} catch (AltimetrikException e) {
			e.printStackTrace();
		} catch (HolcimException e) {
			e.printStackTrace();
		}
		HolcimCustomActivity.setOnback(true);
		Intent returnIntent = new Intent(HolcimCameraActivity.this,
				HolcimSelectedRetailerActivity.class);
		setResult(HolcimConsts.ACTIVITY_RESULT_CODE_CAMERA_OK, returnIntent);
		finish();
	}

	public void GoToPreview(byte[] photo) {
		btnTakePict.setVisibility(View.GONE);
		FragmentPhotoView f = new FragmentPhotoView(photo);
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.frame_container, f,
						HolcimConsts.FRAGMENT_CAMERA_PREVIEW_TAG).commit();
	}

	@Override
	public void onBackPressed() {

		if (!HolcimCustomActivity.blockback) {
			HolcimCustomActivity.setOnback(true);
			super.onBackPressed();
		}

	}
}
