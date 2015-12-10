package com.holcim.altimetrik.android.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.SaleExecutionDao;
import com.holcim.altimetrik.android.utilities.HolcimConsts;

public class HolcimMyVisitPlanListActivity extends HolcimCustomActivity {
	private boolean modalShowUnplannedReason = false;
	private View modalView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.my_visit_plan_list);
		super.onCreate(savedInstanceState);

		ListView listviewMyVisitPlan = (ListView) findViewById(R.id.listView_my_visit_plan);

		// create new unplan visit button
		Button btnCreateUnplannedVisit = (Button) findViewById(R.id.button_create_unplanned_visit);
		btnCreateUnplannedVisit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showUnplannedReason();
			}
		});

		ImageView homePage = (ImageView) findViewById(R.id.imageButton_home);
		homePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					HolcimDataSource
							.deleteAllTemporalFiles(HolcimMyVisitPlanListActivity.this);
				} catch (HolcimException e) {
					e.printStackTrace();
				}
				HolcimCustomActivity.setOnback(true);
				Intent intent = new Intent(HolcimMyVisitPlanListActivity.this,
						HolcimMainActivity.class);
				startActivity(intent);
			}

		});

		ImageButton btnNextWeek = (ImageButton) findViewById(R.id.image_button_next_week);
		btnNextWeek.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(HolcimMyVisitPlanListActivity.this,
						HolcimMyVisitPlanListActivity.class);
				if (HolcimCustomActivity.getPeriod().equalsIgnoreCase(
						getString(R.string.current_week))) {
					HolcimCustomActivity
							.setPeriod(getString(R.string.next_week));
				}
				startActivity(i);
			}
		});

		ImageButton btnback = (ImageButton) findViewById(R.id.image_button_back);
		btnback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(HolcimMyVisitPlanListActivity.this,
						HolcimMyVisitPlanListActivity.class);
				if (HolcimCustomActivity.getPeriod().equalsIgnoreCase(
						getString(R.string.next_week))) {
					HolcimCustomActivity
							.setPeriod(getString(R.string.current_week));
				}
				startActivity(i);
			}
		});

		TextView textTitle = (TextView) findViewById(R.id.textView_title_listview);
		if (HolcimCustomActivity.getPeriod() != null) {
			textTitle.setText(HolcimCustomActivity.getPeriod());
		} else {
			textTitle.setText(getString(R.string.current_week));
		}

		DateFormat format = new SimpleDateFormat("dd MM yyyy");
		DateFormat formatToCompare = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		String[] days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> valuesToCompare = new ArrayList<String>();

		// Check if is current week, next week or next two weeks
		if (HolcimCustomActivity.getPeriod() != null
				&& HolcimCustomActivity.getPeriod().equalsIgnoreCase(
						getString(R.string.next_week))) {
			calendar.add(Calendar.DAY_OF_MONTH, 7);
			for (int i = 0; i < 7; i++) {
				values.add(format.format(calendar.getTime()));
				valuesToCompare.add(formatToCompare.format(calendar.getTime()));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			btnback.setVisibility(View.VISIBLE);
			btnNextWeek.setVisibility(View.INVISIBLE);

		} else {
			HolcimCustomActivity.setPeriod(getString(R.string.current_week));
			for (int i = 0; i < 7; i++) {
				values.add(format.format(calendar.getTime()));
				valuesToCompare.add(formatToCompare.format(calendar.getTime()));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			btnback.setVisibility(View.INVISIBLE);
			btnNextWeek.setVisibility(View.VISIBLE);
		}

		Boolean[] hasSaleExcecution = new Boolean[values.size()];
		Arrays.fill(hasSaleExcecution, Boolean.FALSE);
		// change FALSE to TRUE to show all detail

		for (String date : valuesToCompare) {
			long hasVisitDate = HolcimApp.daoSession
					.getSaleExecutionDao()
					.queryBuilder()
					.where(SaleExecutionDao.Properties.VisitDate.eq(date),
							SaleExecutionDao.Properties.Status
									.notEq(HolcimConsts.SALEEXECUTION_STATUS_RESCHEDULED))
					.count();
			// show all data on my visit plan list

			// .where(SaleExecutionDao.Properties.VisitDate.eq(date),
			// SaleExecutionDao.Properties.Status
			// .notEq(HolcimConsts.SALEEXECUTION_STATUS_CANCELED),
			// SaleExecutionDao.Properties.Status
			// .notEq(HolcimConsts.SALEEXECUTION_STATUS_COMPLETED),

			if (hasVisitDate == 0) {
				hasVisitDate = HolcimApp.daoSession
						.getSaleExecutionDao()
						.queryBuilder()
						.where(SaleExecutionDao.Properties.VisitDate.eq(date),
								SaleExecutionDao.Properties.Status.isNull())
						.count();
			}

			if (hasVisitDate > 0) {
				hasSaleExcecution[valuesToCompare.indexOf(date)] = true;
			}
		}

		HolcimMyVisitPlanAdapter adapter = new HolcimMyVisitPlanAdapter(values,
				valuesToCompare, hasSaleExcecution, days,
				HolcimMyVisitPlanListActivity.this);
		listviewMyVisitPlan.setAdapter(adapter);

		setCustomTitle(getResources().getString(
				R.string.my_visit_plan_activity_title));
	}

	String reason = null;

	private void showUnplannedReason() {
		modalShowUnplannedReason = true;
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		modalView = layoutInflater.inflate(R.layout.modal_unplanned_reason,
				null);

		final PopupWindow popUpReject = new PopupWindow(view,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, false);
		popUpReject.setOutsideTouchable(false);

		Spinner spnReason = (Spinner) modalView
				.findViewById(R.id.spinner_reason_for_not_ordering);
		Button btnOk = (Button) modalView.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) modalView.findViewById(R.id.btn_cancel);
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
					Intent intent = new Intent(
							HolcimMyVisitPlanListActivity.this,
							HolcimUnplannedVisitActivity.class);
					intent.putExtra("reason", reason);
					startActivity(intent);
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((ViewGroup) modalView.getParent()).removeView(modalView);
			}
		});

		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		this.addContentView(modalView, layoutParams);
	}

	public class HolcimMyVisitPlanAdapter extends BaseAdapter {

		Context context;
		private final ArrayList<String> myVisitPlan;
		private final ArrayList<String> valuesToCompare;
		private final String[] days;
		private Boolean[] hasSaleExcecution;

		public HolcimMyVisitPlanAdapter(ArrayList<String> dates,
				ArrayList<String> valuesToCompare,
				Boolean[] hasSaleExcecutionData, String[] days, Context context) {
			this.context = context;
			this.myVisitPlan = new ArrayList<String>();
			this.myVisitPlan.addAll(dates);
			this.days = days;
			this.hasSaleExcecution = hasSaleExcecutionData;
			this.valuesToCompare = valuesToCompare;
		}

		@Override
		public int getCount() {
			return myVisitPlan.size();
		}

		@Override
		public Object getItem(int position) {
			return myVisitPlan.get(position);
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
				convertView = inflater.inflate(R.layout.my_visit_plan_list_row,
						parent, false);
			} else {
				convertView = (View) convertView;
			}

			final TextView textViewDayOfWeek = (TextView) convertView
					.findViewById(R.id.textView_day_of_week);
			TextView textViewDate = (TextView) convertView
					.findViewById(R.id.textView_date);

			textViewDayOfWeek.setText(days[position]);
			textViewDate.setText(myVisitPlan.get(position));

			if (hasSaleExcecution[position]) {
				textViewDayOfWeek.setTextColor(getResources().getColor(
						R.color.holcim_red));
				textViewDate.setTextColor(getResources().getColor(
						R.color.holcim_red));
			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// Show all day
					// if (textViewDayOfWeek.getCurrentTextColor() ==
					// getResources()
					// .getColor(R.color.holcim_red)) {
					Intent nextAct = new Intent(
							HolcimMyVisitPlanListActivity.this,
							HolcimVisitDetailActivity.class);
					nextAct.putExtra("date", valuesToCompare.get(position));
					startActivity(nextAct);
					// }
				}
			});

			return convertView;
		}

	}

	@Override
	public void onBackPressed() {
		try {
			if (!HolcimCustomActivity.blockback) {
				if (modalShowUnplannedReason) {
					modalShowUnplannedReason = false;
					((ViewGroup) modalView.getParent()).removeView(modalView);
				} else {
					HolcimCustomActivity.setOnback(true);
					Intent intent = new Intent(
							HolcimMyVisitPlanListActivity.this,
							HolcimMainActivity.class);
					startActivity(intent);
				}
			}
		} catch (Exception ex) {
			HolcimCustomActivity.setOnback(true);
			Intent intent = new Intent(HolcimMyVisitPlanListActivity.this,
					HolcimMainActivity.class);
			startActivity(intent);
		}

	}

}
