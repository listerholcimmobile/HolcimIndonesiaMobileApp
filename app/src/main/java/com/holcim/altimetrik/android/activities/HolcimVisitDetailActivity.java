package com.holcim.altimetrik.android.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;

import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.ActionsLog;
import com.holcim.altimetrik.android.model.CompetitorMarketing;
import com.holcim.altimetrik.android.model.PreOrder;
import com.holcim.altimetrik.android.model.SaleExecution;
import com.holcim.altimetrik.android.model.SaleExecutionDao;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.IPredicate;
import com.holcim.altimetrik.android.utilities.Predicate;

public class HolcimVisitDetailActivity extends HolcimCustomActivity {
	private HolcimMyVisitPlanAdapter adapter;
	private String date;
	private boolean modalShowCancelReason;
	View modalView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.visit_detail);
		super.onCreate(savedInstanceState);

		final ListView listviewVisitDetail = (ListView) findViewById(R.id.listView_visit_detail);

		if (getIntent().getExtras() != null) {
			date = getIntent().getExtras().getString("date");
		}

		final List<SaleExecution> values = HolcimApp.daoSession
				.getSaleExecutionDao()
				.queryBuilder()
				.where(SaleExecutionDao.Properties.VisitDate.eq(date),
				// SaleExecutionDao.Properties.Status.notEq(HolcimConsts.SALEEXECUTION_STATUS_CANCELED),
				// SaleExecutionDao.Properties.Status.notEq(HolcimConsts.SALEEXECUTION_STATUS_COMPLETED),
						SaleExecutionDao.Properties.Status
								.notEq(HolcimConsts.SALEEXECUTION_STATUS_RESCHEDULED))
				.list();

		values.addAll(HolcimApp.daoSession
				.getSaleExecutionDao()
				.queryBuilder()
				.where(SaleExecutionDao.Properties.VisitDate.eq(date),
						SaleExecutionDao.Properties.Status.isNull()).list());

		adapter = new HolcimMyVisitPlanAdapter(values,
				HolcimVisitDetailActivity.this);
		listviewVisitDetail.setAdapter(adapter);

		ImageView homePage = (ImageView) findViewById(R.id.imageButton_home);
		homePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					HolcimDataSource
							.deleteAllTemporalFiles(HolcimVisitDetailActivity.this);
				} catch (HolcimException e) {
					e.printStackTrace();
				}
				HolcimCustomActivity.setOnback(true);
				Intent intent = new Intent(HolcimVisitDetailActivity.this,
						HolcimMainActivity.class);
				startActivity(intent);
			}
		});

		setCustomTitle(getResources().getString(R.string.visit_detail_title));

		final EditText etSearch = (EditText) findViewById(R.id.edittext_search);
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (etSearch.getText().length() != 0) {
					List<SaleExecution> list = filterVisitDetailList(values,
							etSearch.getText().toString().toLowerCase());
					adapter = new HolcimMyVisitPlanAdapter(list,
							HolcimVisitDetailActivity.this);

				} else {
					adapter = new HolcimMyVisitPlanAdapter(values,
							HolcimVisitDetailActivity.this);
				}
				listviewVisitDetail.setAdapter(adapter);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	public class HolcimMyVisitPlanAdapter extends BaseAdapter {

		Context context;
		private final List<SaleExecution> salesExcecutionList;

		public HolcimMyVisitPlanAdapter(List<SaleExecution> myVisitPlanData,
				Context context) {
			this.context = context;
			this.salesExcecutionList = myVisitPlanData;
		}

		@Override
		public int getCount() {
			return salesExcecutionList.size();
		}

		@Override
		public Object getItem(int position) {
			return salesExcecutionList.get(position);
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
				convertView = inflater.inflate(R.layout.visit_detail_list_row,
						parent, false);
			} else {
				convertView = (View) convertView;
			}

			final TextView textViewRetailer = (TextView) convertView
					.findViewById(R.id.textView_retailer);
			TextView textViewAddress = (TextView) convertView
					.findViewById(R.id.textView_Kecamatan);
			TextView textViewTierKLB = (TextView) convertView
					.findViewById(R.id.textView_tier_klb);
			ImageView imgViewRetailerIcon = (ImageView) convertView
					.findViewById(R.id.retailer_icon);

			if (salesExcecutionList != null
					&& salesExcecutionList.get(position).getAccount() != null) {

				// account logo
				imgViewRetailerIcon.setImageResource(R.drawable.account);

				// account name
				textViewRetailer.setText(salesExcecutionList.get(position)
						.getAccount().getName());
				// klb
				textViewTierKLB.setText(salesExcecutionList.get(position)
						.getTierKLAB());

			} else if (salesExcecutionList != null
					&& salesExcecutionList.get(position).getProspect() != null) {

				// prospect logo
				imgViewRetailerIcon.setImageResource(R.drawable.prospect);

				// prospect name
				textViewRetailer.setText(salesExcecutionList.get(position)
						.getProspect().getName());

				// klb
				textViewTierKLB.setText(salesExcecutionList.get(position)
						.getTierKLAB());
			}
			if (salesExcecutionList != null
					&& salesExcecutionList.get(position) != null) {
				// Kecamatan
				textViewAddress.setText(salesExcecutionList.get(position)
						.getKecamatan());
			}

			if (salesExcecutionList.get(position).getUnplannedVisitReason() != null
					|| (salesExcecutionList.get(position).getIsFinished() != null && salesExcecutionList
							.get(position).getIsFinished())) {
				textViewRetailer.setTextColor(getResources().getColor(
						R.color.holcim_red));
				textViewAddress.setTextColor(getResources().getColor(
						R.color.holcim_red));
				textViewTierKLB.setTextColor(getResources().getColor(
						R.color.holcim_red));
			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (salesExcecutionList.get(position).getIsFinished() == null
							|| !salesExcecutionList.get(position)
									.getIsFinished()) {
						showCancelReason(textViewRetailer.getText().toString(),
								salesExcecutionList.get(position).getId());
					} else {
						Intent nextAct = new Intent(
								HolcimVisitDetailActivity.this,
								HolcimSelectedRetailerActivity.class);
						nextAct.putExtra("accountOrProspectName",
								textViewRetailer.getText().toString());
						nextAct.putExtra("salesExcecutionId",
								salesExcecutionList.get(position).getId());
						nextAct.putExtra("date", date);
						startActivity(nextAct);
					}
				}
			});

			return convertView;
		}

	}

	String reason = null;

	private void showCancelReason(final String accountOrProspect,
			final long saleExecutionId) {
		modalShowCancelReason = true;

		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		modalView = layoutInflater.inflate(R.layout.modal_cancel_visit, null);

		final PopupWindow popUpReject = new PopupWindow(view,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, false);
		popUpReject.setOutsideTouchable(false);

		Spinner spnReason = (Spinner) modalView
				.findViewById(R.id.spinner_cancel_visit_reason);
		Button btnOk = (Button) modalView.findViewById(R.id.btn_yes);
		Button btnCancel = (Button) modalView.findViewById(R.id.btn_no);
		final TextView tvError = (TextView) modalView
				.findViewById(R.id.tv_unplanned_visit_error);

		final List<String> reasonList = Arrays
				.asList(getResources().getStringArray(
						R.array.sales_execution_unplanned_visit_reason));
		ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner, reasonList);
		spnReason.setAdapter(reasonAdapter);
		spnReason.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				reason = reasonList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (reason == null || reason.equals("")) {
					tvError.setVisibility(View.VISIBLE);
				} else {
					SaleExecution saleExecution = HolcimApp.daoSession
							.getSaleExecutionDao().load(saleExecutionId);
					if (saleExecution != null) {
						saleExecution.setReasonForCancelling(reason);
						saleExecution
								.setStatus(HolcimConsts.SALEEXECUTION_STATUS_CANCELED);
						try {
							HolcimDataSource.saveSaleExecution(
									HolcimVisitDetailActivity.this,
									saleExecution,
									new ArrayList<CompetitorMarketing>(),
									new ArrayList<ActionsLog>(),
									new ArrayList<PreOrder>());
						} catch (HolcimException e) {
							e.printStackTrace();
						}
						Intent nextAct = new Intent(
								HolcimVisitDetailActivity.this,
								HolcimMyVisitPlanListActivity.class);
						startActivity(nextAct);
					}

				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ((ViewGroup)modalView.getParent()).removeView(modalView);
				Intent nextAct = new Intent(HolcimVisitDetailActivity.this,
						HolcimSelectedRetailerActivity.class);
				nextAct.putExtra("accountOrProspectName", accountOrProspect);
				nextAct.putExtra("salesExcecutionId", saleExecutionId);
				nextAct.putExtra("date", date);
				startActivity(nextAct);
			}
		});

		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		this.addContentView(modalView, layoutParams);
	}

	public static List<SaleExecution> filterVisitDetailList(
			List<SaleExecution> list, final String filter) {
		List<SaleExecution> filteredList = (ArrayList<SaleExecution>) Predicate
				.filter(list, new IPredicate<SaleExecution>() {
					public boolean apply(SaleExecution detail) {
						Predicate.predicateParams = detail;
						boolean ret = false;
						if (detail.getAccountName() != null) {
							ret = detail.getAccountName().toLowerCase()
									.contains(filter.toLowerCase())
									|| (detail.getTierKLAB() != null && detail
											.getTierKLAB().toLowerCase()
											.contains(filter.toLowerCase()))
									|| (detail.getAccount() != null
											&& detail.getAccount()
													.getMobileAddress() != null && detail
											.getAccount().getMobileAddress()
											.toLowerCase()
											.contains(filter.toLowerCase()));
						} else {
							ret = detail.getProspect().getName().toLowerCase()
									.contains(filter.toLowerCase())
									|| (detail.getTierKLAB() != null && detail
											.getTierKLAB().toLowerCase()
											.contains(filter.toLowerCase()))
									|| (detail.getProspect() != null
											&& detail.getProspect()
													.getMobileAddress() != null && detail
											.getAccount().getMobileAddress()
											.toLowerCase()
											.contains(filter.toLowerCase()));
						}
						return ret;
					}
				});
		if (filteredList == null) {
			filteredList = new ArrayList<SaleExecution>();
		}
		return filteredList;
	}

	@Override
	public void onBackPressed() {
		if (!HolcimCustomActivity.blockback) {
			if (modalShowCancelReason) {
				((ViewGroup) modalView.getParent()).removeView(modalView);
				modalShowCancelReason = false;
			} else {
				HolcimCustomActivity.setOnback(true);
				Intent nextAct = new Intent(HolcimVisitDetailActivity.this,
						HolcimMyVisitPlanListActivity.class);
				startActivity(nextAct);
			}
		}
	}

}
