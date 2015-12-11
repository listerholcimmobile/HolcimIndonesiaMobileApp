package com.holcim.altimetrik.android.activities;

import android.content.Intent;
import android.os.*;
import android.os.AsyncTask.Status;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.altimetrik.holcim.controller.HolcimController;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.utilities.HolcimUtility;
import com.holcim.altimetrik.android.utilities.TextProgressBar;

public class HolcimSyncActivity extends HolcimCustomActivity {

    AsyncSynchronize asyncSync = null;
    String error = null;

    static TextProgressBar textProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestCustomTitle();
        setContentView(R.layout.sync);
        super.onCreate(savedInstanceState);

        onInitvar();
    }

    private void onInitvar() {
        Button btnSync = (Button) findViewById(R.id.btn_sync);

        btnSync.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HolcimUtility.isOnline(HolcimSyncActivity.this)) {
                    setContentView(R.layout.progressbar);
                    textProgressBar = (TextProgressBar) findViewById(R.id.progressBarWithText);
                    asyncSync = new AsyncSynchronize(HolcimSyncActivity.this,
                            textProgressBar);
                    asyncSync.execute();

                } else {
                    dialog.showNoInternetRefreshDialog(dialog.mDismissClickListener);
                }
            }
        });

        setCustomTitle(getResources().getString(R.string.sync_title));
    }

    private void onAsyncSynchronizeFinish(Object response) {
        dialog.dismiss();
        asyncSync = null;
        if (error == null && HolcimController.mSynComplete) {
            dialog.showNotification(getString(R.string.sync_info_complete),
                    getString(R.string.ok), new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            HolcimCustomActivity.setOnback(true);
                            Intent i = new Intent(HolcimSyncActivity.this,
                                    HolcimMainActivity.class);
                            startActivity(i);
                        }
                    });
        } else if(error!=null){
            dialog.showError(error, getString(R.string.ok),
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

//                            requestCustomTitle();
                            setContentView(R.layout.sync);
                            onInitvar();
                            //here we provide the alert like connection problem please sync again

//                            Intent i = new Intent(HolcimSyncActivity.this,
//                                    HolcimMainActivity.class);
//                            startActivity(i);
                        }
                    });
        }else{
            dialog.showError(getString(R.string.message_no_internet_connection), getString(R.string.ok),
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

//                            requestCustomTitle();
                            setContentView(R.layout.sync);
                            onInitvar();
                            //here we provide the alert like connection problem please sync again

//                            Intent i = new Intent(HolcimSyncActivity.this,
//                                    HolcimMainActivity.class);
//                            startActivity(i);
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        if (!HolcimCustomActivity.blockback) {
            HolcimCustomActivity.setOnback(true);
            if (!(asyncSync != null && !asyncSync.getStatus().equals(
                    Status.FINISHED))) {
                Intent i = new Intent(HolcimSyncActivity.this,
                        HolcimMainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Please wait for sync . . .", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public class AsyncSynchronize extends AsyncTask<Void, Void, Object> {
        // Maintain attached activity for states change propose
        private HolcimSyncActivity activity;
        // Keep the response of the database query
        private Object _response;
        // Flag that keep async task completed status
        private boolean completed;
        TextProgressBar progressBar;

        // Constructor
        private AsyncSynchronize(HolcimSyncActivity activity,
                                 TextProgressBar progressBar) {
            this.activity = activity;
            this.progressBar = progressBar;
        }

        // Pre execution actions
        @Override
        protected void onPreExecute() {
            // Start the splash screen dialog
            textProgressBar.setProgress(1);
            textProgressBar.setTextSize(34);

        }

        // Execution of the async task
        protected Object doInBackground(Void... params) {
            try {
//                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND+Process.THREAD_PRIORITY_MORE_FAVORABLE);
                error = HolcimController.synchronize(activity, progressBar);
                return error;
            } catch (Exception e) {
                return false;
            }
        }

        // Post execution actions
        @Override
        protected void onPostExecute(Object response) {
            // Set task completed and notify the activity
            completed = true;
            _response = response;
            notifyActivityTaskCompleted();
        }

        // Notify activity of async task complete
        private void notifyActivityTaskCompleted() {
            if (null != activity) {
                activity.onAsyncSynchronizeFinish(_response);
            }
        }

        // Sets the current activity to the async task
        public void setActivity(HolcimSyncActivity activity) {
            this.activity = activity;
            if (completed) {
                notifyActivityTaskCompleted();
            }
        }

    }
}
