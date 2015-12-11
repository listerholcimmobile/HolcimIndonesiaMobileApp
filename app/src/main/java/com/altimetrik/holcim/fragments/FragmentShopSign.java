package com.altimetrik.holcim.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.activities.HolcimCustomActivity;
import com.holcim.altimetrik.android.activities.HolcimMainActivity;
import com.holcim.altimetrik.android.activities.HolcimSelectedRetailerActivity;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.ActionsLog;
import com.holcim.altimetrik.android.model.Competitor;
import com.holcim.altimetrik.android.model.CompetitorMarketing;
import com.holcim.altimetrik.android.model.PreOrder;
import com.holcim.altimetrik.android.model.SaleExecution;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimUtility;

public class FragmentShopSign extends Fragment {

	private View rootView;
	private FragmentShopSignActions fragmentShopSignSalesActions;
	private SaleExecution saleExecution;
	private ArrayList<CompetitorMarketing> comeptitorsMarketings;
	private ArrayList<ActionsLog> actionLogs;
	private ArrayList<PreOrder> preOrders;
	EditText edtRemark;
	ImageButton camera;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			fragmentShopSignSalesActions = (FragmentShopSignActions) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentShopSignActions");
		}
	}

	public interface FragmentShopSignActions {
		public void goBackFromShopSign(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void goToSaleExecution(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void getLocation();

		public void takePhoto();

		public void deleteActionLogs();

		public void deletePreOrders();

		public void deleteCompetitorMarketing();

		public void resetSaleExecution();
	}

	public FragmentShopSign(SaleExecution saleExecution,
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

		rootView = inflater.inflate(R.layout.fragmentshop_sign, container,
				false);

		TextView title = (TextView) rootView
				.findViewById(R.id.textView_fragment_title);
		title.setText(R.string.shop_sign_title);

		Button btnChekin = (Button) rootView.findViewById(R.id.btn_checkin);
		btnChekin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentShopSignSalesActions.getLocation();
			}
		});
		if (!((HolcimSelectedRetailerActivity) getActivity()).isCanEditByDate()) {
			btnChekin.setVisibility(View.INVISIBLE);
		}

		ImageButton back = (ImageButton) rootView
				.findViewById(R.id.imageButton_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveChanges();
				fragmentShopSignSalesActions.goBackFromShopSign(saleExecution,
						comeptitorsMarketings, actionLogs, preOrders);
			}
		});

		HolcimSelectedRetailerActivity activity = (HolcimSelectedRetailerActivity) getActivity();
		if (!activity.getShowBack()) {
			back.setVisibility(View.INVISIBLE);
		}

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
										fragmentShopSignSalesActions
												.deleteActionLogs();
										fragmentShopSignSalesActions
												.deleteCompetitorMarketing();
										fragmentShopSignSalesActions
												.deletePreOrders();
										fragmentShopSignSalesActions
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

		camera = (ImageButton) rootView.findViewById(R.id.imageButton_camera);

		final DatePicker datePicker = (DatePicker) rootView
				.findViewById(R.id.dp_inspection);
		datePicker.setEnabled(false);

		edtRemark = (EditText) rootView.findViewById(R.id.edt_remark);

		final Spinner spnVisibility = (Spinner) rootView
				.findViewById(R.id.spinner_visibility_hil);
		final Spinner spnCompetitorName = (Spinner) rootView
				.findViewById(R.id.spinner_competitor);
		final Spinner spnCondition = (Spinner) rootView
				.findViewById(R.id.spinner_condition);
		final Spinner spnColorCondition = (Spinner) rootView
				.findViewById(R.id.spinner_color_condition);
		final Spinner spnPriority = (Spinner) rootView
				.findViewById(R.id.spinner_priority);
		final Spinner spnVisibilityCompetitor = (Spinner) rootView
				.findViewById(R.id.spinner_visibility_competitor);
		final Spinner spnAvailability = (Spinner) rootView
				.findViewById(R.id.spinner_availability_hil);
		final Spinner spnType = (Spinner) rootView
				.findViewById(R.id.spinner_type);
		final Spinner spnCleanness = (Spinner) rootView
				.findViewById(R.id.spinner_cleanness);
		final Spinner spnPhysical = (Spinner) rootView
				.findViewById(R.id.spinner_physical);
		final Spinner spnAction = (Spinner) rootView
				.findViewById(R.id.spinner_action);
		final Spinner spnActionStatus = (Spinner) rootView
				.findViewById(R.id.spinner_action_status);

		TextView txtAvailability = (TextView) rootView
				.findViewById(R.id.txt_availability_hil);
		TextView txtVisibility = (TextView) rootView
				.findViewById(R.id.txt_visibility_hil);
		TextView txtType = (TextView) rootView.findViewById(R.id.txt_type);
		TextView txtCondition = (TextView) rootView
				.findViewById(R.id.txt_condition);
		TextView txtCleanness = (TextView) rootView
				.findViewById(R.id.txt_cleanness);
		TextView txtColorCondition = (TextView) rootView
				.findViewById(R.id.txt_color_condition);
		TextView txtPhysical = (TextView) rootView
				.findViewById(R.id.txt_physical);
		TextView txtRemark = (TextView) rootView.findViewById(R.id.txt_remark);
		TextView txtAction = (TextView) rootView.findViewById(R.id.txt_action);
		TextView txtPriority = (TextView) rootView
				.findViewById(R.id.txt_priority);
		TextView txtActionStatus = (TextView) rootView
				.findViewById(R.id.txt_action_status);
		TextView txtVisibilityCompetitor = (TextView) rootView
				.findViewById(R.id.txt_visibility_competitor);
		TextView txtCompetitorName = (TextView) rootView
				.findViewById(R.id.txt_competitor_name);

		// Remark
		if (saleExecution.getShopSignRemark() != null) {
			edtRemark.setText(saleExecution.getShopSignRemark());
			txtRemark.setText(saleExecution.getShopSignRemark());
		}

		if (this.saleExecution.getSsCompetitorName() != null
				&& !this.saleExecution.getSsCompetitorName().equals("")) {
			txtCompetitorName.setText(this.saleExecution.getSsCompetitorName());
		}

		ImageButton next = (ImageButton) rootView
				.findViewById(R.id.imageButton_next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveChanges();
				fragmentShopSignSalesActions.goToSaleExecution(saleExecution,
						comeptitorsMarketings, actionLogs, preOrders);
			}
		});

		// Date
		if (saleExecution.getInspectionDate() != null) {
			String[] separated = saleExecution.getInspectionDate().split("-");
			separated[1] = "" + (Integer.parseInt(separated[1]) - 1);
			datePicker.updateDate(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]),
					Integer.parseInt(separated[2]));
			datePicker.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]),
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setInspectionDate(datePicker
									.getYear()
									+ "-"
									+ (monthOfYear + 1)
									+ "-"
									+ datePicker.getDayOfMonth());
						}
					});
		} else {
			Calendar c = Calendar.getInstance();
			datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setInspectionDate(datePicker
									.getYear()
									+ "-"
									+ (monthOfYear + 1)
									+ "-"
									+ datePicker.getDayOfMonth());
						}
					});
			saleExecution.setInspectionDate("" + c.get(Calendar.YEAR) + "-"
					+ (c.get(Calendar.MONTH) + 1) + "-"
					+ c.get(Calendar.DAY_OF_MONTH));
		}

		// Visibility
		final ArrayList<String> visibility = new ArrayList<String>(
				Arrays.asList((getResources().getStringArray(R.array.confirm))));
		ArrayAdapter<String> adapterVisibility = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, visibility);
		spnVisibility.setAdapter(adapterVisibility);
		if (saleExecution != null
				&& saleExecution.getVisibilityOfHILShopSign() != null) {
			spnVisibility.setSelection(visibility.indexOf(saleExecution
					.getVisibilityOfHILShopSign()));
			txtVisibility.setText(saleExecution.getVisibilityOfHILShopSign());
		}

		spnVisibility.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spnVisibility.getAdapter() != null
						&& spnVisibility.getAdapter().getItem(position) != null) {
					saleExecution.setVisibilityOfHILShopSign(visibility
							.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		final ArrayList<String> competitorList = new ArrayList<String>();
		competitorList.add("");
		for (Competitor competitor : HolcimApp.daoSession.getCompetitorDao()
				.loadAll()) {
			competitorList.add(competitor.getName());
		}
		ArrayAdapter<String> adapterCompetitor = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner, competitorList);
		spnCompetitorName.setAdapter(adapterCompetitor);
		if (saleExecution != null
				&& saleExecution.getVisibilityOfHILShopSign() != null) {
			spnCompetitorName.setSelection(competitorList.indexOf(saleExecution
					.getSsCompetitorName()));
			txtCompetitorName.setText(saleExecution.getSsCompetitorName());
		}

		spnCompetitorName
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (spnCompetitorName.getAdapter() != null
								&& spnCompetitorName.getAdapter().getItem(
										position) != null) {
							saleExecution.setSsCompetitorName(competitorList
									.get(position));
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		// Condition
		final ArrayList<String> condition = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.sale_execution_condition_shop_sign)));
		ArrayAdapter<String> adapterCondition = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, condition);
		spnCondition.setAdapter(adapterCondition);
		if (saleExecution != null
				&& saleExecution.getConditionOfShopSign() != null) {
			spnCondition.setSelection(condition.indexOf(saleExecution
					.getConditionOfShopSign()));
			txtCondition.setText(saleExecution.getConditionOfShopSign());
		}

		spnCondition.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spnCondition.getAdapter() != null
						&& spnCondition.getAdapter().getItem(position) != null) {
					saleExecution.setConditionOfShopSign(condition
							.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// Color Condition
		final ArrayList<String> colorCondition = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.sale_execution_color_condition)));
		ArrayAdapter<String> adapterColorCondition = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, colorCondition);
		spnColorCondition.setAdapter(adapterColorCondition);
		if (saleExecution != null && saleExecution.getColorCondition() != null) {
			spnColorCondition.setSelection(colorCondition.indexOf(saleExecution
					.getColorCondition()));
			txtColorCondition.setText(saleExecution.getColorCondition());
		}

		spnColorCondition
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (spnColorCondition.getAdapter() != null
								&& spnColorCondition.getAdapter().getItem(
										position) != null) {
							saleExecution.setColorCondition(colorCondition
									.get(position));
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		// Priority
		final ArrayList<String> priority = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.sale_execution_priority)));
		ArrayAdapter<String> adapterPriority = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, priority);
		spnPriority.setAdapter(adapterPriority);
		if (saleExecution != null && saleExecution.getPriority() != null) {
			spnPriority.setSelection(priority.indexOf(saleExecution
					.getPriority()));
			txtPriority.setText(saleExecution.getPriority());
		}

		spnPriority.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spnPriority.getAdapter() != null
						&& spnPriority.getAdapter().getItem(position) != null) {
					saleExecution.setPriority(priority.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// Competitor Visibility
		final ArrayList<String> visCompet = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(R.array.confirm)));
		ArrayAdapter<String> adapterVisibilityCompetitor = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, visCompet);
		spnVisibilityCompetitor.setAdapter(adapterVisibilityCompetitor);
		if (saleExecution != null
				&& saleExecution.getVisibilityOfCompetitorShopSign() != null) {
			spnVisibilityCompetitor
					.setSelection(visCompet.indexOf(saleExecution
							.getVisibilityOfCompetitorShopSign()));
			txtVisibilityCompetitor.setText(saleExecution
					.getVisibilityOfCompetitorShopSign());
		}

		spnVisibilityCompetitor
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (spnVisibilityCompetitor.getAdapter() != null
								&& spnVisibilityCompetitor.getAdapter()
										.getItem(position) != null) {
							saleExecution
									.setVisibilityOfCompetitorShopSign(visCompet
											.get(position));
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		// Availability
		final ArrayList<String> availability = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(R.array.confirm)));
		ArrayAdapter<String> adapterAvailability = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, availability);
		spnAvailability.setAdapter(adapterAvailability);
		if (saleExecution != null
				&& saleExecution.getAvailabilityOfHILShopSign() != null) {
			spnAvailability.setSelection(availability.indexOf(saleExecution
					.getAvailabilityOfHILShopSign()));
			txtAvailability.setText(saleExecution
					.getAvailabilityOfHILShopSign());
		}

		spnAvailability.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spnAvailability.getAdapter() != null
						&& spnAvailability.getAdapter().getItem(position) != null) {
					saleExecution.setAvailabilityOfHILShopSign(availability
							.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// Type
		final ArrayList<String> type = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.sales_execution_type_shop_sign)));
		ArrayAdapter<String> adapterType = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, type);
		spnType.setAdapter(adapterType);
		if (saleExecution != null && saleExecution.getTypeOfShopSign() != null) {
			spnType.setSelection(type.indexOf(saleExecution.getTypeOfShopSign()));
			txtType.setText(saleExecution.getTypeOfShopSign());
		}

		spnType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spnType.getAdapter() != null
						&& spnType.getAdapter().getItem(position) != null) {
					saleExecution.setTypeOfShopSign(type.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// Cleanness
		final ArrayList<String> cleanness = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.sale_execution_cleanness_condition)));
		ArrayAdapter<String> adapterCleanness = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, cleanness);
		spnCleanness.setAdapter(adapterCleanness);
		if (saleExecution != null
				&& saleExecution.getCleanessCondition() != null) {
			spnCleanness.setSelection(cleanness.indexOf(saleExecution
					.getCleanessCondition()));
			txtCleanness.setText(saleExecution.getCleanessCondition());
		}

		spnCleanness.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spnCleanness.getAdapter() != null
						&& spnCleanness.getAdapter().getItem(position) != null) {
					saleExecution.setCleanessCondition(cleanness.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// Physical
		final ArrayList<String> physical = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.sale_execution_physical_condition)));
		ArrayAdapter<String> adapterPhysical = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, physical);
		spnPhysical.setAdapter(adapterPhysical);
		if (saleExecution != null
				&& saleExecution.getPhysicalCondition() != null) {
			spnPhysical.setSelection(physical.indexOf(saleExecution
					.getPhysicalCondition()));
			txtPhysical.setText(saleExecution.getPhysicalCondition());
		}

		spnPhysical.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spnPhysical.getAdapter() != null
						&& spnPhysical.getAdapter().getItem(position) != null) {
					saleExecution.setPhysicalCondition(physical.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// Action
		final ArrayList<String> action = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.sale_execution_action)));
		ArrayAdapter<String> adapterAction = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, action);
		spnAction.setAdapter(adapterAction);
		if (saleExecution != null && saleExecution.getAction() != null) {
			spnAction.setSelection(action.indexOf(saleExecution.getAction()));
			txtAction.setText(saleExecution.getAction());
		}

		spnAction.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spnAction.getAdapter() != null
						&& spnAction.getAdapter().getItem(position) != null) {
					saleExecution.setAction(action.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// ActionStatus
		final ArrayList<String> actionStatus = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.sale_execution_action_status)));
		ArrayAdapter<String> adapterActionStatus = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, actionStatus);
		spnActionStatus.setAdapter(adapterActionStatus);
		if (saleExecution != null && saleExecution.getActionStatus() != null) {
			spnActionStatus.setSelection(actionStatus.indexOf(saleExecution
					.getActionStatus()));
			txtActionStatus.setText(saleExecution.getActionStatus());
		}

		spnActionStatus.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spnActionStatus.getAdapter() != null
						&& spnActionStatus.getAdapter().getItem(position) != null) {
					saleExecution.setActionStatus(actionStatus.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		refreshImage();

		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				fragmentShopSignSalesActions.takePhoto();
			}
		});

		if (!((HolcimSelectedRetailerActivity) getActivity()).isCanEditByDate()) {
			// Remove editText and Spinner to the user cannot change fields if
			// is not today's date
			camera.setEnabled(false);
			spnAvailability.setVisibility(View.GONE);
			spnVisibility.setVisibility(View.GONE);
			spnType.setVisibility(View.GONE);
			spnCondition.setVisibility(View.GONE);
			spnCleanness.setVisibility(View.GONE);
			spnColorCondition.setVisibility(View.GONE);
			spnPhysical.setVisibility(View.GONE);
			edtRemark.setVisibility(View.GONE);
			spnAction.setVisibility(View.GONE);
			spnPriority.setVisibility(View.GONE);
			spnActionStatus.setVisibility(View.GONE);
			spnVisibilityCompetitor.setVisibility(View.GONE);
			spnCompetitorName.setVisibility(View.GONE);

			// Add textView to the user can see all fields values
			txtAvailability.setVisibility(View.VISIBLE);
			txtVisibility.setVisibility(View.VISIBLE);
			txtType.setVisibility(View.VISIBLE);
			txtCondition.setVisibility(View.VISIBLE);
			txtCleanness.setVisibility(View.VISIBLE);
			txtColorCondition.setVisibility(View.VISIBLE);
			txtPhysical.setVisibility(View.VISIBLE);
			txtRemark.setVisibility(View.VISIBLE);
			txtAction.setVisibility(View.VISIBLE);
			txtPriority.setVisibility(View.VISIBLE);
			txtActionStatus.setVisibility(View.VISIBLE);
			txtVisibilityCompetitor.setVisibility(View.VISIBLE);
			txtCompetitorName.setVisibility(View.VISIBLE);

		}

		return rootView;
	}

	public void refreshImage() {
		try {
			if (saleExecution.isSSPictureTempFileExist(getActivity())) {
				camera.setImageBitmap(HolcimUtility
						.decodeSampledBitmapFromFile(saleExecution
								.getTempSSPictureFilePath(getActivity()), 200,
								200, 0));
			} else if (saleExecution.getId() != null
					&& saleExecution.isSSPictureFileExist(getActivity())) {
				camera.setImageBitmap(HolcimUtility
						.decodeSampledBitmapFromFile(
								saleExecution.getSSPicturePath(getActivity()),
								200, 200, 0));
			} else {
				camera.setImageResource(R.drawable.camera);
			}
		} catch (HolcimException e) {
			e.printStackTrace();
		}
	}

	public void saveChanges() {
		if (edtRemark.getText() != null) {
			saleExecution
					.setShopSignRemark(String.valueOf(edtRemark.getText()));
		}
	}

	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if( requestCode == 1888 && data != null && data.getExtras() != null) {
	// Bitmap photo = (Bitmap) data.getExtras().get("data");
	// ((ImageView)rootView.findViewById(R.id.imageButton_camera)).setImageBitmap(photo);
	// AltimetrikFileHandler fileHandler = new
	// AltimetrikFileHandler(getActivity());
	// try {
	// fileHandler.WriteBitmapToFile(saleExecution.getTempSSPictureFilePath(getActivity()),
	// photo);
	// } catch (AltimetrikException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (HolcimException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }

	@Override
	public void onResume() {
		super.onResume();
		HolcimApp.getInstance().setForceNoPin(false);
	}

}
