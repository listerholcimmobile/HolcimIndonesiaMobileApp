package com.altimetrik.holcim.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.plurals;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;

import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.activities.HolcimCustomActivity;
import com.holcim.altimetrik.android.activities.HolcimMainActivity;
import com.holcim.altimetrik.android.activities.HolcimMyVisitPlanListActivity;
import com.holcim.altimetrik.android.activities.HolcimSelectedRetailerActivity;
import com.holcim.altimetrik.android.activities.HolcimVisitDetailActivity;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.ActionsLog;
import com.holcim.altimetrik.android.model.CompetitorMarketing;
import com.holcim.altimetrik.android.model.PreOrder;
import com.holcim.altimetrik.android.model.SaleExecution;
import com.holcim.altimetrik.android.utilities.HolcimConsts;

public class FragmentPreorder extends Fragment {

	private FragmentPreorderActions fragmentPreorderActions;
	private SaleExecution saleExecution;
	private ArrayList<CompetitorMarketing> comeptitorsMarketings;
	private ArrayList<ActionsLog> actionLogs;
	private ArrayList<PreOrder> preOrders;
	private ListView listviewVisitDetail;

	HolcimPreorderAdapter adapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			fragmentPreorderActions = (FragmentPreorderActions) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentPreorderActions");
		}
	}

	public interface FragmentPreorderActions {
		public void goBackFromPreorder(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void finishFlow(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void callCreateEditPreOrder(PreOrder preOrder, int position);

		public void deleteActionLogs();

		public void getLocation();

		public void deletePreOrders();

		public void deleteCompetitorMarketing();

		public void resetSaleExecution();
	}

	public FragmentPreorder(SaleExecution saleExecution,
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
		title.setText(R.string.preorder_title);

		ImageButton back = (ImageButton) rootView
				.findViewById(R.id.imageButton_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentPreorderActions.goBackFromPreorder(saleExecution,
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
										fragmentPreorderActions
												.deleteActionLogs();
										fragmentPreorderActions
												.deletePreOrders();
										fragmentPreorderActions
												.deleteCompetitorMarketing();
										fragmentPreorderActions
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
		next.setVisibility(View.GONE);

		Button buttonCreatePreorder = (Button) rootView
				.findViewById(R.id.button_create_new_contact);
		buttonCreatePreorder.setText(getString(R.string.title_create_preorder));
		buttonCreatePreorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fragmentPreorderActions.callCreateEditPreOrder(null, -1);
			}
		});
		if (!((HolcimSelectedRetailerActivity) getActivity()).isCanEditByDate()) {
			buttonCreatePreorder.setVisibility(View.INVISIBLE);
		}

		Button finish = (Button) rootView.findViewById(R.id.button_finish);
		finish.setVisibility(View.VISIBLE);
		finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (preOrders.size() != 0) {
					fragmentPreorderActions.finishFlow(saleExecution,
							comeptitorsMarketings, actionLogs, preOrders);
				} else {
					((HolcimSelectedRetailerActivity) getActivity())
							.showReasonForNotOrderingModal();
				}
			}
		});
		if (!((HolcimSelectedRetailerActivity) getActivity()).isCanEditByDate()) {
			finish.setVisibility(View.INVISIBLE);
		}

		listviewVisitDetail = (ListView) rootView
				.findViewById(R.id.listView_fragment_list);
		ArrayList<PreOrder> pList = new ArrayList<PreOrder>();
		pList.addAll(preOrders);
		if (preOrders.size() > 1) {
			for (PreOrder p : preOrders) {
				if (p.getReasonForNotOrdering() != null
						&& !p.getReasonForNotOrdering().equals("")) {
					pList.remove(p);
				}
			}
		}
		adapter = new HolcimPreorderAdapter(pList, getActivity()
				.getApplicationContext());
		listviewVisitDetail.setAdapter(adapter);

		return rootView;
	}

	public void refreshAdapter(ArrayList<PreOrder> pList) {
		if (adapter != null) {
			adapter = new HolcimPreorderAdapter(pList, getActivity()
					.getApplicationContext());
			listviewVisitDetail.setAdapter(adapter);
		}
	}

	public class HolcimPreorderAdapter extends BaseAdapter {

		Context context;
		private List<PreOrder> fields;

		public HolcimPreorderAdapter(List<PreOrder> fields, Context context) {
			this.context = context;
			this.fields = fields;
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
			TextView textViewFieldValue = (TextView) convertView
					.findViewById(R.id.textView_field_info);
			textViewFieldValue.setVisibility(View.GONE);

			if (preOrders.size() == 1
					&& preOrders.get(0).getReasonForNotOrdering() != null
					&& !preOrders.get(0).getReasonForNotOrdering().equals("")) {
				LinearLayout linearLayoutReasonForNotOrdering = (LinearLayout) convertView
						.findViewById(R.id.linearLayoutReasonForNotOrdering);
				TextView textViewReasonForNotOrdering = (TextView) convertView
						.findViewById(R.id.textViewReasonForNotOrdering);
				TextView textViewReasonForNotOrderingValue = (TextView) convertView
						.findViewById(R.id.textViewReasonForNotOrderingValue);

				linearLayoutReasonForNotOrdering.setVisibility(View.VISIBLE);

				textViewReasonForNotOrdering
						.setText(getString(R.string.po_reasonForNotOrdering));
				textViewReasonForNotOrderingValue.setText(preOrders.get(0)
						.getReasonForNotOrdering());
				textViewField.setVisibility(View.GONE);
				textViewFieldValue.setVisibility(View.GONE);
			} else {
				textViewField.setText(fields.get(position).getPreOrderDate());

				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						fragmentPreorderActions.callCreateEditPreOrder(
								fields.get(position), (position + 1));
					}
				});
			}

			return convertView;
		}
	}

}
