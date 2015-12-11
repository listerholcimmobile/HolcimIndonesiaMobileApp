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

public class FragmentSalesExec extends Fragment {

	private FragmentSalesExecActions fragmentSalesExecActions;
	private SaleExecution saleExecution;
	private ArrayList<CompetitorMarketing> comeptitorsMarketings;
	private ArrayList<ActionsLog> actionLogs;
	private ArrayList<PreOrder> preOrders;
	HolcimSaleExcecutionAdapter adapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			fragmentSalesExecActions = (FragmentSalesExecActions) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentSalesExecActions");
		}
	}

	public interface FragmentSalesExecActions {
		public void goBackFromSalesExec(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void goToFeedback(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void callCreateEditCompetitorMarketing(
				CompetitorMarketing competitorMarketing,
				ArrayList<CompetitorMarketing> competitorMarketings,
				int posotion);

		public void deleteActionLogs();

		public void getLocation();

		public void deletePreOrders();

		public void deleteCompetitorMarketing();

		public void resetSaleExecution();
	}

	public FragmentSalesExec(SaleExecution saleExecution,
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

		View rootView = inflater.inflate(R.layout.contact_info, container,
				false);

		TextView title = (TextView) rootView
				.findViewById(R.id.textView_fragment_title);
		title.setText(R.string.sales_title);

		ImageButton back = (ImageButton) rootView
				.findViewById(R.id.imageButton_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentSalesExecActions.goBackFromSalesExec(saleExecution,
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
										fragmentSalesExecActions
												.deleteActionLogs();
										fragmentSalesExecActions
												.deleteCompetitorMarketing();
										fragmentSalesExecActions
												.deletePreOrders();
										fragmentSalesExecActions
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
				fragmentSalesExecActions.goToFeedback(saleExecution,
						comeptitorsMarketings, actionLogs, preOrders);
			}
		});

		Button btnChekin = (Button) rootView.findViewById(R.id.btn_checkin);
		btnChekin.setVisibility(View.VISIBLE);
		btnChekin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentSalesExecActions.getLocation();
			}
		});
		if (!((HolcimSelectedRetailerActivity) getActivity()).isCanEditByDate()) {
			btnChekin.setVisibility(View.INVISIBLE);
		}

		Button buttonCreateCompetitorMarketing = (Button) rootView
				.findViewById(R.id.button_create_new_contact);
		buttonCreateCompetitorMarketing
				.setText(getString(R.string.title_create_competitor_marketing));
		buttonCreateCompetitorMarketing
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						fragmentSalesExecActions
								.callCreateEditCompetitorMarketing(null,
										comeptitorsMarketings, -1);
					}
				});
		if (!((HolcimSelectedRetailerActivity) getActivity()).isCanEditByDate()) {
			buttonCreateCompetitorMarketing.setVisibility(View.INVISIBLE);
		}

		ListView listviewVisitDetail = (ListView) rootView
				.findViewById(R.id.listView_fragment_list);

		adapter = new HolcimSaleExcecutionAdapter(comeptitorsMarketings,
				getActivity().getApplicationContext());
		listviewVisitDetail.setAdapter(adapter);
		return rootView;
	}

	public void refreshAdapter(ArrayList<CompetitorMarketing> compMark) {
		if (adapter != null) {
			adapter.setNewAdapterList(compMark);
		}
	}

	public class HolcimSaleExcecutionAdapter extends BaseAdapter {

		Context context;
		private List<CompetitorMarketing> fields;

		public HolcimSaleExcecutionAdapter(List<CompetitorMarketing> fields,
				Context context) {
			this.context = context;
			this.fields = fields;
		}

		public void setNewAdapterList(ArrayList<CompetitorMarketing> compMark) {
			this.fields = compMark;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return fields.size();
		}

		@Override
		public Object getItem(int position) {
			return fields.get(position);
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

			TextView textViewField = (TextView) convertView
					.findViewById(R.id.textView_field);
			textViewField.setText(fields.get(position).getCompetitorName());

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					fragmentSalesExecActions.callCreateEditCompetitorMarketing(
							fields.get(position), comeptitorsMarketings,
							position);
				}
			});

			return convertView;
		}
	}
}
