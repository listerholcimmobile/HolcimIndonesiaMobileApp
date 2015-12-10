package com.altimetrik.holcim.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.activities.HolcimCustomActivity;
import com.holcim.altimetrik.android.activities.HolcimMainActivity;
import com.holcim.altimetrik.android.activities.HolcimSelectedRetailerActivity;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.ActionsLog;
import com.holcim.altimetrik.android.model.CompetitorMarketing;
import com.holcim.altimetrik.android.model.PreOrder;
import com.holcim.altimetrik.android.model.SaleExecution;
import com.holcim.altimetrik.android.utilities.HolcimConsts;

public class FragmentFeedback extends Fragment {

	private FragmentFeedbackActions fragmentFeedbackActions;
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
			fragmentFeedbackActions = (FragmentFeedbackActions) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentFeedbackActions");
		}
	}

	public interface FragmentFeedbackActions {
		public void goBackFromFeedback(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void goToPreOrder(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void callCreateEditActionLog(ActionsLog actionLog, int position);

		public void goToOutstandingFeedback(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public boolean isEditing();

		public void deleteActionLogs();

		public void addActionLogsToDelete(ActionsLog actionLog);

		public void getLocation();

		public void deletePreOrders();

		public void deleteCompetitorMarketing();

		public void resetSaleExecution();

		public void finishFlow(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);
	}

	public FragmentFeedback(SaleExecution saleExecution,
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

		View rootView = inflater.inflate(R.layout.feedback_list, container,
				false);

		TextView title = (TextView) rootView
				.findViewById(R.id.textView_fragment_title);
		title.setText(R.string.feedback_title);

		ImageButton back = (ImageButton) rootView
				.findViewById(R.id.imageButton_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentFeedbackActions.goBackFromFeedback(saleExecution,
						comeptitorsMarketings, actionLogs, preOrders);
			}
		});

		Button btnChekin = (Button) rootView.findViewById(R.id.btn_checkin);
		btnChekin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentFeedbackActions.getLocation();
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
										fragmentFeedbackActions
												.deleteActionLogs();
										fragmentFeedbackActions
												.deletePreOrders();
										fragmentFeedbackActions
												.deleteCompetitorMarketing();
										fragmentFeedbackActions
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

		ImageButton next = (ImageButton) rootView
				.findViewById(R.id.imageButton_next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentFeedbackActions.goToPreOrder(saleExecution,
						comeptitorsMarketings, actionLogs, preOrders);
			}
		});

		Button buttonFinish = (Button) rootView
				.findViewById(R.id.button_finish);
		buttonFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fragmentFeedbackActions.finishFlow(saleExecution,
						comeptitorsMarketings, actionLogs, preOrders);
			}
		});

		if (((!((HolcimSelectedRetailerActivity) getActivity()).isAccount()) && (((HolcimSelectedRetailerActivity) getActivity())
				.isCanEditByDate()))
				|| (HolcimApp.getInstance().getProfile() != null && HolcimApp
						.getInstance().getProfile()
						.equalsIgnoreCase(HolcimConsts.TSO))) {
			buttonFinish.setVisibility(View.VISIBLE);
			next.setVisibility(View.GONE);
			btnChekin.setVisibility(View.GONE);
		}

		if ((!((HolcimSelectedRetailerActivity) getActivity()).isAccount())
				&& !((HolcimSelectedRetailerActivity) getActivity())
						.isCanEditByDate()) {
			btnChekin.setVisibility(View.INVISIBLE);
			next.setVisibility(View.GONE);
		} else if (!((HolcimSelectedRetailerActivity) getActivity())
				.isCanEditByDate()) {
			btnChekin.setVisibility(View.INVISIBLE);
		}

		if (HolcimApp.getInstance().getProfile() != null
				&& HolcimApp.getInstance().getProfile()
						.equalsIgnoreCase(HolcimConsts.TSO)) {
			btnChekin.setVisibility(View.GONE);
		}

		Button buttonCreateActionLog = (Button) rootView
				.findViewById(R.id.button_create_new_contact);
		buttonCreateActionLog
				.setText(getString(R.string.title_create_feedback));
		buttonCreateActionLog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentFeedbackActions.callCreateEditActionLog(null, -1);
			}
		});

		if (!((HolcimSelectedRetailerActivity) getActivity()).isCanEditByDate()) {
			buttonCreateActionLog.setVisibility(View.INVISIBLE);
		}

		Button btnOutstandingFeedback = (Button) rootView
				.findViewById(R.id.button_outstanding_feedback);
		if (fragmentFeedbackActions.isEditing()
				&& saleExecution != null
				&& saleExecution.getAccount() != null
				&& saleExecution.getAccount().getOutstandingFeedbacks() != null
				&& saleExecution.getAccount().getOutstandingFeedbacks().size() > 0) {
			btnOutstandingFeedback.setVisibility(View.VISIBLE);
			btnOutstandingFeedback.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					fragmentFeedbackActions.goToOutstandingFeedback(
							saleExecution, comeptitorsMarketings, actionLogs,
							preOrders);
				}
			});
		} else if (fragmentFeedbackActions.isEditing()
				&& saleExecution != null
				&& saleExecution.getProspect() != null
				&& saleExecution.getProspect().getOutstandingFeedbacks() != null
				&& saleExecution.getProspect().getOutstandingFeedbacks().size() > 0) {
			btnOutstandingFeedback.setVisibility(View.VISIBLE);
			btnOutstandingFeedback.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					fragmentFeedbackActions.goToOutstandingFeedback(
							saleExecution, comeptitorsMarketings, actionLogs,
							preOrders);
				}
			});
		} else {
			btnOutstandingFeedback.setVisibility(View.GONE);
		}

		ListView listviewVisitDetail = (ListView) rootView
				.findViewById(R.id.listView_fragment_list);

		if (actionLogs != null) {
			adapter = new FeedbackAdapter(actionLogs, getActivity()
					.getApplicationContext());
			listviewVisitDetail.setAdapter(adapter);
		}

		return rootView;
	}

	public void refreshAdapter(ArrayList<ActionsLog> actionsLogs) {
		if (adapter != null) {
			adapter.setNewAdapterList(actionsLogs);
		}
	}

	public class FeedbackAdapter extends BaseAdapter {

		Context context;
		private List<ActionsLog> values;

		public void setNewAdapterList(ArrayList<ActionsLog> actionsLogs) {
			this.values = actionsLogs;
			this.notifyDataSetChanged();
		}

		public FeedbackAdapter(List<ActionsLog> valuesData, Context context) {
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
					fragmentFeedbackActions.callCreateEditActionLog(
							values.get(position), position);
				}
			});

			return convertView;
		}
	}
}
