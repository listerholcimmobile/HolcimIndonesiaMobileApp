package com.holcim.altimetrik.android.activities;

import com.holcim.hsea.R;

import android.content.Intent;
import android.os.Bundle;

public class HolcimMobileActivity extends HolcimCustomActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.main);
    	super.onCreate(savedInstanceState);
        
    }
    
    @Override
	public void onBackPressed() {
    	if(!HolcimCustomActivity.blockback){
			if(!super.getIsModalVisible()){
				Intent homeIntent = new Intent(Intent.ACTION_MAIN);
				homeIntent.addCategory( Intent.CATEGORY_HOME );
				homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(homeIntent);
			}
    	}	
	}
}