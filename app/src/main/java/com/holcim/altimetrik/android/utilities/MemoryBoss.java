package com.holcim.altimetrik.android.utilities;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

import com.holcim.altimetrik.android.application.HolcimApp;

public class MemoryBoss implements ComponentCallbacks2 {

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            HolcimApp.getInstance().setCameFromBackground(true);
        }
        // you might as well implement some memory cleanup here and be a nice Android dev.
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		
	}
}
