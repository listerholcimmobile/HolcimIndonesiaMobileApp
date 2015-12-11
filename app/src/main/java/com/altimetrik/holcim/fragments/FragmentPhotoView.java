package com.altimetrik.holcim.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.holcim.hsea.R;
import com.holcim.altimetrik.android.utilities.HolcimUtility;

public class FragmentPhotoView extends Fragment {

	private FragmentPhotoViewActions fragmentPhotoViewActions;

	public interface FragmentPhotoViewActions {
		public void CancelPhotoTake();

		public void SavePhotoTake(byte[] bitmap);
	}

	private byte[] bitmap;

	public FragmentPhotoView(byte[] photo) {
		this.bitmap = photo;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			fragmentPhotoViewActions = (FragmentPhotoViewActions) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentPhotoViewActions");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.photo_fragment, container,
				false);

		ImageView photo = (ImageView) rootView.findViewById(R.id.imv_photo);
		ImageButton save = (ImageButton) rootView.findViewById(R.id.imb_save);
		ImageButton cancel = (ImageButton) rootView
				.findViewById(R.id.imb_cancel);

		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentPhotoViewActions.SavePhotoTake(bitmap);
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentPhotoViewActions.CancelPhotoTake();
			}
		});

		// File imageFile = HolcimDataSource.getTempPhotoFile(getActivity());
		// if (imageFile != null && imageFile.exists()) {
		// photo.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(imageFile.getAbsolutePath(),
		// 400, 400, 0));
		// } else {
		// photo.setImageBitmap(null);
		// }
		photo.setImageBitmap(HolcimUtility.decodeSampledBitmap(bitmap, 400,
				400, 0));
		// photo.setImageBitmap(BitmapFactory.decodeByteArray(bitmap, 0,
		// bitmap.length));

		return rootView;
	}

}
