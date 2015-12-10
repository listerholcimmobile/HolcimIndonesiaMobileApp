package com.holcim.altimetrik.android.utilities;

import java.io.File;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;

import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.holcim.altimetrik.android.activities.HolcimCameraActivity;
import com.holcim.altimetrik.android.exception.HolcimException;

public class HolcimSimpleCameraHost extends SimpleCameraHost {

	Context mContext;

	public HolcimSimpleCameraHost(Context _ctxt) {
		super(_ctxt);
		mContext = _ctxt;
	}

	//	@Override
	//	protected File getPhotoPath() {
	//		try {
	//			return HolcimDataSource.getTempPhotoFile(mContext);
	//		} catch (HolcimException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//			return null;
	//		}
	//	}

	//	@Override
	//	public void saveImage(PictureTransaction xact, Bitmap bitmap) {
	//		try {
	//			AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(mContext);
	//			fileHandler.WriteBitmapToFile(getPhotoPath().getAbsolutePath(), bitmap);
	//		} catch (AltimetrikException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//	}





	@Override
	public void saveImage(PictureTransaction arg0, final byte[] arg1) {
		try {
			if (mContext != null && mContext instanceof HolcimCameraActivity) {
				((HolcimCameraActivity)mContext).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						((HolcimCameraActivity)mContext).GoToPreview(arg1);
					}
				});
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

		@Override
		public boolean useSingleShotMode() {
			return true;
		}

	//	@Override
	//	public Camera.ShutterCallback getShutterCallback() {
	//		return new Camera.ShutterCallback() {			
	//			@Override
	//			public void onShutter() {
	//				if (mContext != null && mContext instanceof HolcimCameraActivity) {
	//					((HolcimCameraActivity)mContext).GoToPreview();
	//				}
	//			}
	//		};
	//	}

}
