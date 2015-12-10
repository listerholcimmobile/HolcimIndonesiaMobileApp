package com.holcim.altimetrik.android.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.holcim.altimetrik.android.activities.HolcimCustomActivity;
import com.holcim.altimetrik.android.application.HolcimApp;

public class HolcimBroadcastReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            HolcimApp.getInstance().setCameFromBackground(true);
        }
	}

}
