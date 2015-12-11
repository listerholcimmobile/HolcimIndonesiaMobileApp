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
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

public class FragmentLastCompetitorMarketInfo extends Fragment {

	private FragmentLastCompetitorMarketInfoActions fragmentLastCompetitorMarketInfoActions;
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
			fragmentLastCompetitorMarketInfoActions = (FragmentLastCompetitorMarketInfoActions) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentLastCompetitorMarketInfoActions");
		}
	}

	public FragmentLastCompetitorMarketInfo(SaleExecution saleExecution,
			ArrayList<CompetitorMarketing> comeptitorsMarketings,
			ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
		this.saleExecution = saleExecution;
		this.comeptitorsMarketings = comeptitorsMarketings;
		this.actionLogs = actionLogs;
		this.preOrders = preOrders;
	}

	public interface FragmentLastCompetitorMarketInfoActions {
		public void goBackFromLastCompetitorMarketInfo(
				SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void goToShopSign(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

        public void goToSaleExecution(SaleExecution saleExecution,
                                      ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                      ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);


        public void deleteActionLogs();

		public void getLocation();

		public void deletePreOrders();

		public void deleteCompetitorMarketing();

		public void resetSaleExecution();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.last_competitor_market_info,
				container, false);

		TextView txtSupplierName = (TextView) rootView
				.findViewById(R.id.edt_supplierName);
		TextView txtLastBuyingPrice = (TextView) rootView
				.findViewById(R.id.edt_lastBuyingPrice);
		TextView txtLastSellingPrice = (TextView) rootView
				.findViewById(R.id.edt_lastSellingPrice);
		TextView txtLast_inventory = (TextView) rootView
				.findViewById(R.id.edt_last_inventory);
		TextView txtLastBuyingVolume = (TextView) rootView
				.findViewById(R.id.edt_lastBuyingVolume);
		TextView txtPromotion = (TextView) rootView
				.findViewById(R.id.edt_promotion);
		TextView txtProgram = (TextView) rootView
				.findViewById(R.id.edt_program);
		final DatePicker datePickerLastBuyingPriceDate = (DatePicker) rootView
				.findViewById(R.id.datePicker_lastBuyingPriceDate);
		final DatePicker datePickerLastSellingPriceDate = (DatePicker) rootView
				.findViewById(R.id.datePicker_lastSellingPriceDate);
		final DatePicker datePickerLastInventoryDate = (DatePicker) rootView
				.findViewById(R.id.datePicker_lastInventoryDate);
		final DatePicker datePickerLastBuyingVolumeDate = (DatePicker) rootView
				.findViewById(R.id.datePicker_lastBuyingVolumeDate);
		final DatePicker datePickerPromotionStartDate = (DatePicker) rootView
				.findViewById(R.id.datePicker_promotionStartDate);
		final DatePicker datePickerPromotionEndDate = (DatePicker) rootView
				.findViewById(R.id.datePicker_promotionEndDate);
		final DatePicker datePickerProgramStartDate = (DatePicker) rootView
				.findViewById(R.id.datePicker_programStartDate);
		final DatePicker datePickerProgramEndDate = (DatePicker) rootView
				.findViewById(R.id.datePicker_programEndDate);

		if (this.saleExecution.getSupplierName() != null) {
			txtSupplierName.setText(this.saleExecution.getSupplierName());
		}

		if (this.saleExecution.getLastBuyingPrice() != null) {
			txtLastBuyingPrice.setText(String.valueOf(this.saleExecution
					.getLastBuyingPrice()));
		}

		if (this.saleExecution.getLastSellingPrice() != null) {
			txtLastSellingPrice.setText(String.valueOf(this.saleExecution
					.getLastSellingPrice()));
		}

		if (this.saleExecution.getLastInventory() != null) {
			txtLast_inventory.setText(String.valueOf(this.saleExecution
					.getLastInventory()));
		}

		if (this.saleExecution.getLastBuyingVolume() != null) {
			txtLastBuyingVolume.setText(String.valueOf(this.saleExecution
					.getLastBuyingVolume()));
		}

		if (this.saleExecution.getPromotion() != null) {
			txtPromotion.setText(String.valueOf(this.saleExecution
					.getPromotion()));
		}

		if (this.saleExecution.getProgram() != null) {
			txtProgram.setText(String.valueOf(this.saleExecution.getProgram()));
		}

		if (saleExecution.getLastBuyingPriceDate() != null) {
			String date = saleExecution.getLastBuyingPriceDate();
			String[] separated = date.split("-");
			datePickerLastBuyingPriceDate.updateDate(
					Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]));
			datePickerLastBuyingPriceDate.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setLastBuyingPriceDate(datePickerLastBuyingPriceDate
									.getYear()
									+ "-"
									+ datePickerLastBuyingPriceDate.getMonth()
									+ "-"
									+ datePickerLastBuyingPriceDate
											.getDayOfMonth());
						}
					});
		} else {
			datePickerLastBuyingPriceDate.setVisibility(View.INVISIBLE);
		}

		if (saleExecution.getLastSellingPriceDate() != null) {
			String date = saleExecution.getLastSellingPriceDate();
			String[] separated = date.split("-");
			datePickerLastSellingPriceDate.updateDate(
					Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]));
			datePickerLastSellingPriceDate.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setLastSellingPriceDate(datePickerLastSellingPriceDate
									.getYear()
									+ "-"
									+ datePickerLastSellingPriceDate.getMonth()
									+ "-"
									+ datePickerLastSellingPriceDate
											.getDayOfMonth());
						}
					});
		} else {
			datePickerLastSellingPriceDate.setVisibility(View.INVISIBLE);
		}

		if (saleExecution.getLastInventoryDate() != null) {
			String date = saleExecution.getLastInventoryDate();
			String[] separated = date.split("-");
			datePickerLastInventoryDate.updateDate(
					Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]));
			datePickerLastInventoryDate.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setLastInventoryDate(datePickerLastInventoryDate
									.getYear()
									+ "-"
									+ datePickerLastInventoryDate.getMonth()
									+ "-"
									+ datePickerLastInventoryDate
											.getDayOfMonth());
						}
					});
		} else {
			datePickerLastInventoryDate.setVisibility(View.INVISIBLE);
		}

		if (saleExecution.getLastBuyingVolumeDate() != null) {
			String date = saleExecution.getLastBuyingVolumeDate();
			String[] separated = date.split("-");
			datePickerLastBuyingVolumeDate.updateDate(
					Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]));
			datePickerLastBuyingVolumeDate.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setLastBuyingVolumeDate(datePickerLastBuyingVolumeDate
									.getYear()
									+ "-"
									+ datePickerLastBuyingVolumeDate.getMonth()
									+ "-"
									+ datePickerLastBuyingVolumeDate
											.getDayOfMonth());
						}
					});
		} else {
			datePickerLastBuyingVolumeDate.setVisibility(View.INVISIBLE);
//			Calendar c = Calendar.getInstance();
//			datePickerLastBuyingVolumeDate.init(c.get(Calendar.YEAR),
//					c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
//					new OnDateChangedListener() {
//
//						@Override
//						public void onDateChanged(DatePicker view, int year,
//								int monthOfYear, int dayOfMonth) {
//							saleExecution.setLastBuyingVolumeDate(datePickerLastBuyingVolumeDate
//									.getYear()
//									+ "-"
//									+ datePickerLastBuyingVolumeDate.getMonth()
//									+ "-"
//									+ datePickerLastBuyingVolumeDate
//											.getDayOfMonth());
//						}
//					});
		}

		if (saleExecution.getPromotionStartDate() != null) {
			String date = saleExecution.getPromotionStartDate();
			String[] separated = date.split("-");
			datePickerPromotionStartDate.updateDate(
					Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]));
			datePickerPromotionStartDate.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setPromotionStartDate(datePickerPromotionStartDate
									.getYear()
									+ "-"
									+ datePickerPromotionStartDate.getMonth()
									+ "-"
									+ datePickerPromotionStartDate
											.getDayOfMonth());
						}
					});
		} else {
			datePickerPromotionStartDate.setVisibility(View.INVISIBLE);
//			Calendar c = Calendar.getInstance();
//			datePickerPromotionStartDate.init(c.get(Calendar.YEAR),
//					c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
//					new OnDateChangedListener() {
//
//						@Override
//						public void onDateChanged(DatePicker view, int year,
//								int monthOfYear, int dayOfMonth) {
//							saleExecution.setPromotionStartDate(datePickerPromotionStartDate
//									.getYear()
//									+ "-"
//									+ datePickerPromotionStartDate.getMonth()
//									+ "-"
//									+ datePickerPromotionStartDate
//											.getDayOfMonth());
//						}
//					});
		}

		if (saleExecution.getPromotionEndDate() != null) {
			String date = saleExecution.getPromotionEndDate();
			String[] separated = date.split("-");
			datePickerPromotionEndDate.updateDate(
					Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]));
			datePickerPromotionEndDate.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setPromotionEndDate(datePickerPromotionEndDate
									.getYear()
									+ "-"
									+ datePickerPromotionEndDate.getMonth()
									+ "-"
									+ datePickerPromotionEndDate
											.getDayOfMonth());
						}
					});
		} else {
			datePickerPromotionEndDate.setVisibility(View.INVISIBLE);
//			Calendar c = Calendar.getInstance();
//			datePickerPromotionEndDate.init(c.get(Calendar.YEAR),
//					c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
//					new OnDateChangedListener() {
//
//						@Override
//						public void onDateChanged(DatePicker view, int year,
//								int monthOfYear, int dayOfMonth) {
//							saleExecution.setPromotionEndDate(datePickerPromotionEndDate
//									.getYear()
//									+ "-"
//									+ datePickerPromotionEndDate.getMonth()
//									+ "-"
//									+ datePickerPromotionEndDate
//											.getDayOfMonth());
//						}
//					});
		}

		if (saleExecution.getProgramStartDate() != null) {
			String date = saleExecution.getProgramStartDate();
			String[] separated = date.split("-");
			datePickerProgramStartDate.updateDate(
					Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]));
			datePickerProgramStartDate.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setProgramStartDate(datePickerProgramStartDate
									.getYear()
									+ "-"
									+ datePickerProgramStartDate.getMonth()
									+ "-"
									+ datePickerProgramStartDate
											.getDayOfMonth());
						}
					});
		} else {
			datePickerProgramStartDate.setVisibility(View.INVISIBLE);
//			Calendar c = Calendar.getInstance();
//			datePickerProgramStartDate.init(c.get(Calendar.YEAR),
//					c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
//					new OnDateChangedListener() {
//
//						@Override
//						public void onDateChanged(DatePicker view, int year,
//								int monthOfYear, int dayOfMonth) {
//							saleExecution.setProgramStartDate(datePickerProgramStartDate
//									.getYear()
//									+ "-"
//									+ datePickerProgramStartDate.getMonth()
//									+ "-"
//									+ datePickerProgramStartDate
//											.getDayOfMonth());
//						}
//					});
		}

		if (saleExecution.getProgramEndDate() != null) {
			String date = saleExecution.getProgramEndDate();
			String[] separated = date.split("-");
			datePickerProgramEndDate.updateDate(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]));
			datePickerProgramEndDate.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution
									.setProgramEndDate(datePickerProgramEndDate
											.getYear()
											+ "-"
											+ datePickerProgramEndDate
													.getMonth()
											+ "-"
											+ datePickerProgramEndDate
													.getDayOfMonth());
						}
					});
		} else {
			datePickerProgramEndDate.setVisibility(View.INVISIBLE);
//			Calendar c = Calendar.getInstance();
//			datePickerProgramEndDate.init(c.get(Calendar.YEAR),
//					c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
//					new OnDateChangedListener() {
//
//						@Override
//						public void onDateChanged(DatePicker view, int year,
//								int monthOfYear, int dayOfMonth) {
//							saleExecution
//									.setProgramEndDate(datePickerProgramEndDate
//											.getYear()
//											+ "-"
//											+ datePickerProgramEndDate
//													.getMonth()
//											+ "-"
//											+ datePickerProgramEndDate
//													.getDayOfMonth());
//						}
//					});
		}

		datePickerLastBuyingPriceDate.setEnabled(false);
		datePickerLastSellingPriceDate.setEnabled(false);
		datePickerLastInventoryDate.setEnabled(false);
		datePickerLastBuyingVolumeDate.setEnabled(false);
		datePickerPromotionStartDate.setEnabled(false);
		datePickerPromotionEndDate.setEnabled(false);
		datePickerProgramStartDate.setEnabled(false);
		datePickerProgramEndDate.setEnabled(false);

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
										fragmentLastCompetitorMarketInfoActions
												.deleteActionLogs();
										fragmentLastCompetitorMarketInfoActions
												.deletePreOrders();
										fragmentLastCompetitorMarketInfoActions
												.deleteCompetitorMarketing();
										fragmentLastCompetitorMarketInfoActions
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

		ImageButton back = (ImageButton) rootView
				.findViewById(R.id.imageButton_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentLastCompetitorMarketInfoActions
						.goBackFromLastCompetitorMarketInfo(saleExecution,
								comeptitorsMarketings, actionLogs, preOrders);
			}
		});

		ImageButton next = (ImageButton) rootView
				.findViewById(R.id.imageButton_next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if(saleExecution.getUnplannedVisitReason()!=null){
                    fragmentLastCompetitorMarketInfoActions.goToSaleExecution(saleExecution, comeptitorsMarketings, actionLogs,
                            preOrders);
                }
                else
                {
                    fragmentLastCompetitorMarketInfoActions.goToShopSign(
                            saleExecution, comeptitorsMarketings, actionLogs,
                            preOrders);
                }

			}
		});

		return rootView;
	}
}
