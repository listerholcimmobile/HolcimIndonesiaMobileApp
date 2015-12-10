package com.altimetrik.holcim.fragments;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.DatePicker.OnDateChangedListener;

import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.activities.HolcimCustomActivity;
import com.holcim.altimetrik.android.activities.HolcimMainActivity;
import com.holcim.altimetrik.android.activities.HolcimSelectedRetailerActivity;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.ActionsLog;
import com.holcim.altimetrik.android.model.CompetitorMarketing;
import com.holcim.altimetrik.android.model.PreOrder;
import com.holcim.altimetrik.android.model.SaleExecution;

public class FragmentPreviousSales extends Fragment {

	private FragmentPreviousSalesActions fragmentPreviousSalesActions;
	private SaleExecution saleExecution;
	private ArrayList<CompetitorMarketing> comeptitorsMarketings;
	private ArrayList<ActionsLog> actionLogs;
	private ArrayList<PreOrder> preOrders;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			fragmentPreviousSalesActions = (FragmentPreviousSalesActions) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentPreviousSalesActions");
		}
	}

	public interface FragmentPreviousSalesActions {
		public void goBackFromPreviousSales(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void goToLastCompetitorMarketinfo(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void deleteActionLogs();

		public void deletePreOrders();

		public void deleteCompetitorMarketing();

		public void resetSaleExecution();
	}

	public FragmentPreviousSales(SaleExecution saleExecution,
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

		View rootView = inflater.inflate(R.layout.previous_sales_exec,
				container, false);

		TextView title = (TextView) rootView
				.findViewById(R.id.textView_fragment_title);
		title.setText(R.string.previous_sales_title);

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
										fragmentPreviousSalesActions
												.deleteActionLogs();
										fragmentPreviousSalesActions
												.deleteCompetitorMarketing();
										fragmentPreviousSalesActions
												.deletePreOrders();
										fragmentPreviousSalesActions
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

		DatePicker datepickerBpPriceDate = (DatePicker) rootView
				.findViewById(R.id.datepicker_pse_bp_price_date);
		// EditText edtBpCompetitorName = (EditText)
		// rootView.findViewById(R.id.edt_bp_competitor_name);
		// EditText edtBpCompetitorBP = (EditText)
		// rootView.findViewById(R.id.edt_pse_bp_competitor_bp);
		final TextView edtBpHILPrice = (TextView) rootView
				.findViewById(R.id.edt_bp_hil_price);
		// EditText edtBpCompetitorBPDate = (EditText)
		// rootView.findViewById(R.id.edt_bp_competitor_bp_date);
		DatePicker datepickerSpHILSpDate = (DatePicker) rootView
				.findViewById(R.id.datepicker_pse_sp_hil_sp_date);
		final TextView edtSpHILSp = (TextView) rootView
				.findViewById(R.id.edt_pse_sp_hil_sp);
		// EditText edtSpCompetitorName = (EditText)
		// rootView.findViewById(R.id.edt_sp_competitor_name);
		// EditText edtSpCompSpDate = (EditText)
		// rootView.findViewById(R.id.edt_pse_sp_comp_sp_date);
		// EditText edtSpCompSP = (EditText)
		// rootView.findViewById(R.id.edt_pse_sp_comp_sp);
		DatePicker datepickerLiHILInvDate = (DatePicker) rootView
				.findViewById(R.id.datepicker_pse_li_hil_inv_date);
		final TextView edtLiHILInv = (TextView) rootView
				.findViewById(R.id.edt_pse_li_hil_inv);
		// EditText edtLiCompetitorName = (EditText)
		// rootView.findViewById(R.id.edt_li_competitor_name);
		// EditText edtLiCompInv = (EditText)
		// rootView.findViewById(R.id.edt_pse_li_comp_inv);
		// EditText edtLiCompInvDate = (EditText)
		// rootView.findViewById(R.id.edt_pse_li_comp_inv_date);
		final TextView edtBvHILV = (TextView) rootView
				.findViewById(R.id.edt_pse_bv_hil_v);
		// EditText edtBvCompV = (EditText)
		// rootView.findViewById(R.id.edt_pse_bv_comp_v);
		// EditText edtBvCompetitorName = (EditText)
		// rootView.findViewById(R.id.edt_bv_competitor_name);
		// EditText edtBvCompVDate = (EditText)
		// rootView.findViewById(R.id.edt_pse_bv_comp_v_date);

		ImageButton back = (ImageButton) rootView
				.findViewById(R.id.imageButton_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentPreviousSalesActions.goBackFromPreviousSales(
						saleExecution, comeptitorsMarketings, actionLogs,
						preOrders);
			}
		});

		ImageButton next = (ImageButton) rootView
				.findViewById(R.id.imageButton_next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentPreviousSalesActions.goToLastCompetitorMarketinfo(
						saleExecution, comeptitorsMarketings, actionLogs,
						preOrders);
			}
		});

		String hilBuyingPrice = (saleExecution.getHilBuyingPrice() != null) ? saleExecution
				.getHilBuyingPrice().toString() : "";
		String hilSp = (saleExecution.getHilBuyingSellingPrice() != null) ? saleExecution
				.getHilBuyingSellingPrice().toString() : "";
		String hilInv = (saleExecution.getHilBuyingInventoryPrice() != null) ? saleExecution
				.getHilBuyingInventoryPrice().toString() : "";
		String hilBuyVol = (saleExecution.getHilBuyingVolume() != null) ? saleExecution
				.getHilBuyingVolume().toString() : "";

		if (saleExecution.getHilBuyingPriceDate() != null) {
			String[] separated = saleExecution.getHilBuyingPriceDate().split(
					"-");
			datepickerBpPriceDate.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setHilBuyingPriceDate(year + "-"
									+ (monthOfYear + 1) + "-" + dayOfMonth);
						}
					});
		} else {
			datepickerBpPriceDate.setVisibility(View.INVISIBLE);
//			Calendar c = Calendar.getInstance();
//			datepickerBpPriceDate.init(c.get(Calendar.YEAR),
//					c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
//					new OnDateChangedListener() {
//
//						@Override
//						public void onDateChanged(DatePicker view, int year,
//								int monthOfYear, int dayOfMonth) {
//							saleExecution.setHilBuyingPriceDate(year + "-"
//									+ (monthOfYear + 1) + "-" + dayOfMonth);
//						}
//					});
		}

		edtBpHILPrice.setText(hilBuyingPrice, TextView.BufferType.EDITABLE);

		if (saleExecution.getHilBuyingSellingPriceDate() != null) {
			String[] separated = saleExecution.getHilBuyingSellingPriceDate()
					.split("-");
			datepickerSpHILSpDate.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setHilBuyingSellingPriceDate(year
									+ "-" + (monthOfYear + 1) + "-"
									+ dayOfMonth);
						}
					});
		} else {
			datepickerSpHILSpDate.setVisibility(View.INVISIBLE);
//			Calendar c = Calendar.getInstance();
//			datepickerSpHILSpDate.init(c.get(Calendar.YEAR),
//					c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
//					new OnDateChangedListener() {
//
//						@Override
//						public void onDateChanged(DatePicker view, int year,
//								int monthOfYear, int dayOfMonth) {
//							saleExecution.setHilBuyingSellingPriceDate(year
//									+ "-" + (monthOfYear + 1) + "-"
//									+ dayOfMonth);
//						}
//					});
		}

		edtSpHILSp.setText(hilSp, TextView.BufferType.EDITABLE);

		if (saleExecution.getHilBuyingInventoryPriceDate() != null) {
			String[] separated = saleExecution.getHilBuyingInventoryPriceDate()
					.split("-");
			datepickerLiHILInvDate.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setHilBuyingInventoryPriceDate(year
									+ "-" + (monthOfYear + 1) + "-"
									+ dayOfMonth);
						}
					});
		} else {
			datepickerLiHILInvDate.setVisibility(View.INVISIBLE);
//			Calendar c = Calendar.getInstance();
//			datepickerLiHILInvDate.init(c.get(Calendar.YEAR),
//					c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
//					new OnDateChangedListener() {
//
//						@Override
//						public void onDateChanged(DatePicker view, int year,
//								int monthOfYear, int dayOfMonth) {
//							saleExecution.setHilBuyingInventoryPriceDate(year
//									+ "-" + (monthOfYear + 1) + "-"
//									+ dayOfMonth);
//						}
//					});
		}

		edtLiHILInv.setText(hilInv, TextView.BufferType.EDITABLE);
		edtBvHILV.setText(hilBuyVol, TextView.BufferType.EDITABLE);

		datepickerBpPriceDate.setEnabled(false);
		datepickerSpHILSpDate.setEnabled(false);
		datepickerLiHILInvDate.setEnabled(false);

		return rootView;
	}
}
