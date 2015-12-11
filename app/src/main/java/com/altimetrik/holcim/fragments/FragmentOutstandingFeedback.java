package com.altimetrik.holcim.fragments;

import java.util.ArrayList;
import java.util.List;

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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentOutstandingFeedback extends Fragment {

	private FragmentOutstandingFeedbackActions fragmentOutstandingFeedbackActions;
	private SaleExecution saleExecution;
	private ArrayList<CompetitorMarketing> comeptitorsMarketings;
	private ArrayList<ActionsLog> actionLogs;
	private ArrayList<PreOrder> preOrders;
	private FeedbackAdapter adapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			fragmentOutstandingFeedbackActions = (FragmentOutstandingFeedbackActions) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentOutstandingFeedbackActions");
		}
	}

	public interface FragmentOutstandingFeedbackActions {
		public void goBackFromOutstandingFeedback(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void goToOutstandingFeedbackDetail(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs,
				ArrayList<PreOrder> preOrders,
				OutstandingFeedback outstandingFeedback);

		public void deleteActionLogs();

		public void getLocation();

		public void deletePreOrders();

		public void deleteCompetitorMarketing();

		public void resetSaleExecution();
	}

	public FragmentOutstandingFeedback(SaleExecution saleExecution,
			ArrayList<CompetitorMarketing> comeptitorsMarketings,
			ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
		this.saleExecution = saleExecution;
		this.comeptitorsMarketings = comeptitorsMarketings;
		this.actionLogs = actionLogs;
		this.preOrders = preOrders;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.outstanding_feedback_list,
				container, false);

		TextView title = (TextView) rootView
				.findViewById(R.id.textView_fragment_title);
		title.setText(R.string.feedback_title);

		ImageButton back = (ImageButton) rootView
				.findViewById(R.id.imageButton_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentOutstandingFeedbackActions
						.goBackFromOutstandingFeedback(saleExecution,
								comeptitorsMarketings, actionLogs, preOrders);
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
										fragmentOutstandingFeedbackActions
												.deleteActionLogs();
										fragmentOutstandingFeedbackActions
												.deletePreOrders();
										fragmentOutstandingFeedbackActions
												.deleteCompetitorMarketing();
										fragmentOutstandingFeedbackActions
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

		ListView listviewVisitDetail = (ListView) rootView
				.findViewById(R.id.listView_fragment_list);

		if (saleExecution != null
				&& saleExecution.getAccount() != null
				&& saleExecution.getAccount().getOutstandingFeedbacks() != null
				&& saleExecution.getAccount().getOutstandingFeedbacks().size() > 0) {
			adapter = new FeedbackAdapter(saleExecution.getAccount()
					.getOutstandingFeedbacks(), getActivity()
					.getApplicationContext());
			listviewVisitDetail.setAdapter(adapter);
		} else if (saleExecution != null
				&& saleExecution.getProspect() != null
				&& saleExecution.getProspect().getOutstandingFeedbacks() != null
				&& saleExecution.getProspect().getOutstandingFeedbacks().size() > 0) {
			adapter = new FeedbackAdapter(saleExecution.getProspect()
					.getOutstandingFeedbacks(), getActivity()
					.getApplicationContext());
			listviewVisitDetail.setAdapter(adapter);
		}

		return rootView;
	}

	public class FeedbackAdapter extends BaseAdapter {

		Context context;
		private List<OutstandingFeedback> values;

		public void setNewAdapterList(ArrayList<OutstandingFeedback> actionsLogs) {
			this.values = actionsLogs;
			this.notifyDataSetChanged();
		}

		public FeedbackAdapter(List<OutstandingFeedback> valuesData,
				Context context) {
			this.context = context;
			this.values = valuesData;
		}

		@Override
		public int getCount() {
			return values.size();
		}

		@Override
		public Object getItem(int position) {
			return values.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.fragment_list_row,
						parent, false);
			}

			TextView textViewActionLogName = (TextView) convertView
					.findViewById(R.id.textView_field);
			TextView textViewFieldInfo = (TextView) convertView
					.findViewById(R.id.textView_field_info);

			textViewActionLogName
					.setText(values.get(position).getDescription());
			textViewFieldInfo.setVisibility(View.GONE);

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					fragmentOutstandingFeedbackActions
							.goToOutstandingFeedbackDetail(saleExecution,
									comeptitorsMarketings, actionLogs,
									preOrders, values.get(position));
				}
			});

			return convertView;
		}
	}
}
