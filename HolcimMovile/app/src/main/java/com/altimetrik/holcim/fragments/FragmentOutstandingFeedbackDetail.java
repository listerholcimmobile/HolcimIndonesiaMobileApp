package com.altimetrik.holcim.fragments;

import java.util.ArrayList;

import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.activities.HolcimCustomActivity;
import com.holcim.altimetrik.android.activities.HolcimMainActivity;
import com.holcim.altimetrik.android.activities.HolcimSelectedRetailerActivity;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.ActionsLog;
import com.holcim.altimetrik.android.model.CompetitorMarketing;
import com.holcim.altimetrik.android.model.OutstandingFeedback;
import com.holcim.altimetrik.android.model.PreOrder;
import com.holcim.altimetrik.android.model.SaleExecution;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentOutstandingFeedbackDetail extends Fragment {
	private FragmentOutstandingFeedbackDetailActions fragmentOutstandingFeedbackDetailActions;
	private SaleExecution saleExecution;
	private ArrayList<CompetitorMarketing> comeptitorsMarketings;
	private ArrayList<ActionsLog> actionLogs;
	private ArrayList<PreOrder> preOrders;
	private OutstandingFeedback outstandingFeedback;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			fragmentOutstandingFeedbackDetailActions = (FragmentOutstandingFeedbackDetailActions) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					activity.toString()
							+ " must implement FragmentOutstandingFeedbackDetailActions");
		}
	}

	public interface FragmentOutstandingFeedbackDetailActions {
		public void goBackFromFragmentOutstandingFeedbackDetail(
				SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void deleteActionLogs();

		public void getLocation();

		public void deletePreOrders();

		public void deleteCompetitorMarketing();

		public void resetSaleExecution();
	}

	public FragmentOutstandingFeedbackDetail(SaleExecution saleExecution,
			ArrayList<CompetitorMarketing> comeptitorsMarketings,
			ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders,
			OutstandingFeedback outstandingFeedback) {
		this.saleExecution = saleExecution;
		this.comeptitorsMarketings = comeptitorsMarketings;
		this.actionLogs = actionLogs;
		this.preOrders = preOrders;
		this.outstandingFeedback = outstandingFeedback;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.outstanding_feedback_detail,
				container, false);

		TextView description = (TextView) rootView
				.findViewById(R.id.textView_field_value_description);
		if (outstandingFeedback.getDescription() != null
				&& !outstandingFeedback.getDescription().equals("")) {
			description.setText(outstandingFeedback.getDescription());
		}

		TextView isComplaint = (TextView) rootView
				.findViewById(R.id.textView_field_value_complaint);
		if (outstandingFeedback.getComplaint() != null
				&& outstandingFeedback.getComplaint()) {
			isComplaint.setText("True");
		} else {
			isComplaint.setText("False");
		}
		TextView status = (TextView) rootView
				.findViewById(R.id.textView_field_value_status);
		if (outstandingFeedback.getStatus() != null
				&& !outstandingFeedback.getStatus().equals("")) {
			status.setText(outstandingFeedback.getStatus());
		}
		TextView category = (TextView) rootView
				.findViewById(R.id.textView_field_value_category);
		if (outstandingFeedback.getCategory() != null
				&& !outstandingFeedback.getCategory().equals("")) {
			category.setText(outstandingFeedback.getCategory());
		}

		Button finish = (Button) rootView.findViewById(R.id.button_finish);
		finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentOutstandingFeedbackDetailActions
						.goBackFromFragmentOutstandingFeedbackDetail(
								saleExecution, comeptitorsMarketings,
								actionLogs, preOrders);
			}
		});

		ImageView homePage = (ImageView) rootView
				.findViewById(R.id.imageButton_home);
		homePage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!((HolcimSelectedRetailerActivity) getActivity())
						.isCanEditByDate()) {
					HolcimCustomActivity.setOnback(true);
					Intent intent = new Intent(getActivity()
							.getApplicationContext(), HolcimMainActivity.class);
					startActivity(intent);
				} else {
					final HolcimSelectedRetailerActivity activity = (HolcimSelectedRetailerActivity) getActivity();
					activity.dialog.showWarning(
							getString(R.string.message_warning_go_home),
							getString(R.string.ok), getString(R.string.cancel),
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									activity.dialog.dismiss();
									try {
										HolcimDataSource
												.deleteAllTemporalFiles(getActivity());
										fragmentOutstandingFeedbackDetailActions
												.deleteActionLogs();
										fragmentOutstandingFeedbackDetailActions
												.deletePreOrders();
										fragmentOutstandingFeedbackDetailActions
												.deleteCompetitorMarketing();
										fragmentOutstandingFeedbackDetailActions
												.resetSaleExecution();
									} catch (HolcimException e) {
										e.printStackTrace();
									}
									HolcimCustomActivity.setOnback(true);
									Intent intent = new Intent(getActivity()
											.getApplicationContext(),
											HolcimMainActivity.class);
									startActivity(intent);
								}
							}, activity.dialog.mDismissClickListener);
				}
			}
		});

		return rootView;
	}
}
