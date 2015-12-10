package com.altimetrik.holcim.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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
import android.widget.RelativeLayout;
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
import com.holcim.altimetrik.android.model.CompetitorMarketing;
import com.holcim.altimetrik.android.model.PreOrder;
import com.holcim.altimetrik.android.model.SaleExecution;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimUtility;

public class FragmentContactInfo extends Fragment {

	private FragmentContactInfoActions fragmentContactInfoActions;
	private SaleExecution saleExecution;
	private ArrayList<CompetitorMarketing> comeptitorsMarketings;
	private ArrayList<ActionsLog> actionLogs;
	private ArrayList<PreOrder> preOrders;

	// Camera
	ImageButton camera;

	boolean[] firstTime;
	EditText editTextFirstName;
	EditText editTextLastName;
	EditText editTextPhone;
	EditText editTextMobile1;
	EditText editTextMobile2;
	EditText editTextEmail;
	EditText editTextExtension;
	EditText editTextFax;
	EditText editTextMailingAddress;
	EditText editTextPreferredName;
	EditText editTextNationalityId;
	Spinner spnReligion;
	Spinner spnContact;
	EditText editTextCallNote;
	Spinner spnTitle;
	EditText editTextFavouriteFood;
	EditText editTextNotFavouriteFood;
	EditText editTextFavouriteDrink;
	EditText editTextNotFavouriteDrink;
	EditText editTextHobbies;
	EditText editTextFavouriteActivities;
	EditText editTextBirthdate;

	public interface FragmentContactInfoActions {
		public void goBackFromContactInfo(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void goToPreviousSale(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void deleteActionLogs();

		public void getLocation();

		public void takePhoto();

		public void deletePreOrders();

		public void deleteCompetitorMarketing();

		public void resetSaleExecution();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			fragmentContactInfoActions = (FragmentContactInfoActions) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentContactInfoActions");
		}
	}

	public FragmentContactInfo(SaleExecution saleExecution,
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

		firstTime = new boolean[4];
		Arrays.fill(firstTime, Boolean.TRUE);

		View rootView = inflater.inflate(R.layout.create_new_contact_row,
				container, false);

		TextView title = (TextView) rootView
				.findViewById(R.id.textView_fragment_title);
		title.setText(R.string.contact_info_title);

		Button btnChekin = (Button) rootView.findViewById(R.id.btn_checkin);
		btnChekin.setVisibility(View.VISIBLE);
		btnChekin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentContactInfoActions.getLocation();
			}
		});
		if (!((HolcimSelectedRetailerActivity) getActivity()).isCanEditByDate()) {
			btnChekin.setVisibility(View.INVISIBLE);
		}

		// BtnCamera
		camera = (ImageButton) rootView.findViewById(R.id.imageButton_camera);

		refreshImage();
		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				fragmentContactInfoActions.takePhoto();
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
										fragmentContactInfoActions
												.deleteActionLogs();
										fragmentContactInfoActions
												.deleteCompetitorMarketing();
										fragmentContactInfoActions
												.deletePreOrders();
										fragmentContactInfoActions
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

		String[] contactFields = getResources().getStringArray(
				R.array.contact_fields);

		TextView textViewFirstName = (TextView) rootView
				.findViewById(R.id.text_view_first_name);
		TextView textViewContactIdValue = (TextView) rootView
				.findViewById(R.id.text_view_contact_id_value);
		TextView textViewGender = (TextView) rootView
				.findViewById(R.id.text_view_gender);
		TextView textViewNationality = (TextView) rootView
				.findViewById(R.id.text_view_nationality);
		TextView textViewEmail = (TextView) rootView
				.findViewById(R.id.text_view_email);
		TextView textViewLastName = (TextView) rootView
				.findViewById(R.id.text_view_last_name);
		TextView textViewBirthdate = (TextView) rootView
				.findViewById(R.id.text_view_birthdate);
		TextView textViewPhone = (TextView) rootView
				.findViewById(R.id.text_view_phone);
		TextView textViewExtension = (TextView) rootView
				.findViewById(R.id.text_view_extension);
		TextView textViewFax = (TextView) rootView
				.findViewById(R.id.text_view_fax);
		TextView textViewMailingStreet = (TextView) rootView
				.findViewById(R.id.text_view_mailing_street);
		TextView textViewMailingCity = (TextView) rootView
				.findViewById(R.id.text_view_mailing_city);
		TextView textViewMailingStateProvince = (TextView) rootView
				.findViewById(R.id.text_view_mailing_state_province);
		TextView textViewMailingPostalCode = (TextView) rootView
				.findViewById(R.id.text_view_mailing_postal_code);
		TextView textViewCountry = (TextView) rootView
				.findViewById(R.id.text_view_mailing_country);
		TextView textViewMailingAddress = (TextView) rootView
				.findViewById(R.id.text_view_mailing_address);
		TextView textViewAccountId = (TextView) rootView
				.findViewById(R.id.text_view_account_id);
		TextView textViewAccountName = (TextView) rootView
				.findViewById(R.id.text_view_account_name);
		TextView textViewAccountNameValue = (TextView) rootView
				.findViewById(R.id.text_view_account_name_value);
		TextView editTextAccountId = (TextView) rootView
				.findViewById(R.id.edit_text_account_id);

		textViewFirstName.setText(contactFields[0]);
		textViewLastName.setText(contactFields[1]);
		textViewGender.setText(contactFields[2]);
		textViewBirthdate.setText(contactFields[3]);
		textViewNationality.setText(contactFields[4]);
		textViewPhone.setText(contactFields[5]);
		textViewEmail.setText(contactFields[7]);
		textViewExtension.setText(contactFields[8]);
		textViewFax.setText(contactFields[9]);
		textViewMailingAddress.setText(contactFields[15]);
		textViewAccountId.setText(contactFields[16]);
		textViewAccountName.setText(contactFields[17]);

		TextView txtTitle = (TextView) rootView
				.findViewById(R.id.txt_view_title);
		TextView txtFirstName = (TextView) rootView
				.findViewById(R.id.txt_view_first_name);
		TextView txtLastName = (TextView) rootView
				.findViewById(R.id.txt_view_last_name);
		TextView txtPreferredName = (TextView) rootView
				.findViewById(R.id.txt_view_preferred_name);
		TextView txtNationalId = (TextView) rootView
				.findViewById(R.id.txt_view_national_id);
		TextView txtGender = (TextView) rootView
				.findViewById(R.id.txt_view_gender);
		TextView txtMaritalStatus = (TextView) rootView
				.findViewById(R.id.txt_view_marital_status);
		TextView txtNationaly = (TextView) rootView
				.findViewById(R.id.txt_view_nationality);
		TextView txtReligion = (TextView) rootView
				.findViewById(R.id.txt_view_religion);
		TextView txtPreferredContactMethod = (TextView) rootView
				.findViewById(R.id.txt_view_preferred_Contact_method);
		TextView txtMailingAddress = (TextView) rootView
				.findViewById(R.id.txt_view_mailing_address);
		TextView txtMobile1 = (TextView) rootView
				.findViewById(R.id.txt_view_mobile_1);
		TextView txtMobile2 = (TextView) rootView
				.findViewById(R.id.txt_view_mobile_2);
		TextView txtCallNote = (TextView) rootView
				.findViewById(R.id.txt_view_call_note);
		TextView txtEmail1 = (TextView) rootView
				.findViewById(R.id.txt_view_email);
		TextView txtFavouriteFood = (TextView) rootView
				.findViewById(R.id.txt_view_favourite_food);
		TextView txtNotFavouriteFood = (TextView) rootView
				.findViewById(R.id.txt_view_not_favourite_food);
		TextView txtFavouriteDrink = (TextView) rootView
				.findViewById(R.id.txt_view_favourite_drink);
		TextView txtNotFavouriteDrink = (TextView) rootView
				.findViewById(R.id.txt_view_not_favourite_drink);
		TextView txtHobbies = (TextView) rootView
				.findViewById(R.id.txt_view_hobbies);
		TextView txtFavouriteActivities = (TextView) rootView
				.findViewById(R.id.txt_view_favourite_activities);
		TextView txtFavouriteSports = (TextView) rootView
				.findViewById(R.id.txt_view_favourite_sports);

		if (saleExecution.getRetailerId() != null) {
			editTextAccountId.setText(String.valueOf(saleExecution
					.getRetailerId()));
		}

		if (saleExecution.getContactId() != null) {
			textViewContactIdValue.setText(saleExecution.getContactId());
		}

		if (saleExecution.getMaritalStatus() != null) {
			txtMaritalStatus.setText(saleExecution.getMaritalStatus());
		}

		if (saleExecution.getNationality() != null) {
			txtNationaly.setText(saleExecution.getNationality());
		}

		if (saleExecution.getFavouriteSports() != null) {
			txtFavouriteSports.setText(saleExecution.getFavouriteSports());
		}

		textViewMailingStreet.setVisibility(View.GONE);
		textViewMailingCity.setVisibility(View.GONE);
		textViewMailingStateProvince.setVisibility(View.GONE);
		textViewMailingPostalCode.setVisibility(View.GONE);
		textViewCountry.setVisibility(View.GONE);
		textViewPhone.setVisibility(View.GONE);
		textViewExtension.setVisibility(View.GONE);
		textViewFax.setVisibility(View.GONE);

		final Spinner spinnerGender = (Spinner) rootView
				.findViewById(R.id.spinner_gender);
		final Spinner spinnerNationality = (Spinner) rootView
				.findViewById(R.id.spinner_nationality);
		final Spinner spinnerAccountName = (Spinner) rootView
				.findViewById(R.id.spinner_account_name);
		final Spinner spinnerMaritalStatus = (Spinner) rootView
				.findViewById(R.id.spinner_marital_status);
		final Spinner spinnerFavouriteSports = (Spinner) rootView
				.findViewById(R.id.spinner_favourite_sports);
		spnReligion = (Spinner) rootView.findViewById(R.id.spinner_religion);
		spnTitle = (Spinner) rootView.findViewById(R.id.spinner_title);
		spnContact = (Spinner) rootView.findViewById(R.id.spinner_contact);

		spinnerAccountName.setVisibility(View.GONE);

		// Religion
		final List<String> arrReligion = Arrays.asList((getResources()
				.getStringArray(R.array.pl_religion)));
		ArrayAdapter<String> adapterReligion = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, arrReligion);
		spnReligion.setAdapter(adapterReligion);
		if (saleExecution != null && saleExecution.getReligion() != null) {
			spnReligion.setSelection(arrReligion.indexOf(saleExecution
					.getReligion()));
			txtReligion.setText(saleExecution.getReligion());
		}

		spnReligion.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spnReligion.getAdapter() != null
						&& spnReligion.getAdapter().getItem(position) != null
						&& !arrReligion.get(position).equals("")) {
					saleExecution.setReligion(arrReligion.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// Title
		final List<String> arrTitle = Arrays.asList((getResources()
				.getStringArray(R.array.pl_title)));
		ArrayAdapter<String> adapterTitle = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, arrTitle);
		spnTitle.setAdapter(adapterTitle);
		if (saleExecution != null && saleExecution.getTitle() != null) {
			spnTitle.setSelection(arrTitle.indexOf(saleExecution.getTitle()));
			txtTitle.setText(saleExecution.getTitle());
		}

		spnTitle.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spnTitle.getAdapter() != null
						&& spnTitle.getAdapter().getItem(position) != null
						&& !arrTitle.get(position).equals("")) {
					saleExecution.setTitle(arrTitle.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// Contact
		final List<String> arrContact = Arrays.asList((getResources()
				.getStringArray(R.array.pl_contact)));
		ArrayAdapter<String> adapterContact = new ArrayAdapter<String>(
				this.getActivity(), R.layout.spinner, arrContact);
		spnContact.setAdapter(adapterContact);
		if (saleExecution != null
				&& saleExecution.getPreferredContactMethod() != null) {
			spnContact.setSelection(arrContact.indexOf(saleExecution
					.getPreferredContactMethod()));
			txtPreferredContactMethod.setText(saleExecution
					.getPreferredContactMethod());
		}

		spnContact.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spnContact.getAdapter() != null
						&& spnContact.getAdapter().getItem(position) != null
						&& !arrContact.get(position).equals("")) {
					saleExecution.setPreferredContactMethod(arrContact
							.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		final List<String> genderList = Arrays.asList(getResources()
				.getStringArray(R.array.gender));
		ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(
				getActivity().getApplicationContext(), R.layout.spinner,
				genderList);
		spinnerGender.setAdapter(genderAdapter);
		spinnerGender.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spinnerGender.getAdapter() != null
						&& spinnerGender.getAdapter().getItem(position) != null) {
					if (firstTime[0]) {
						if (saleExecution != null
								&& saleExecution.getGender() != null) {
							spinnerGender.setSelection(genderList
									.indexOf(saleExecution.getGender()));
						}
						firstTime[0] = false;
					} else if (!genderList.get(position).equals("")) {
						saleExecution.setGender(genderList.get(position));
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		final List<String> nationallityList = Arrays.asList(getResources()
				.getStringArray(R.array.nationality));
		ArrayAdapter<String> NationallityAdapter = new ArrayAdapter<String>(
				getActivity().getApplicationContext(), R.layout.spinner,
				nationallityList);
		spinnerNationality.setAdapter(NationallityAdapter);
		spinnerNationality
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (spinnerNationality.getAdapter() != null
								&& spinnerNationality.getAdapter().getItem(
										position) != null) {
							if (firstTime[1]) {
								if (saleExecution != null
										&& saleExecution.getNationality() != null) {
									spinnerNationality
											.setSelection(nationallityList
													.indexOf(saleExecution
															.getNationality()));
								} else {
									spinnerNationality.setSelection(nationallityList
											.indexOf(HolcimConsts.DEFAULT_NATIONALITY));
								}
								firstTime[1] = false;
							} else if (!nationallityList.get(position).equals(
									"")) {
								saleExecution.setNationality(nationallityList
										.get(position));
							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		final List<String> maritalStatusList = Arrays.asList(getResources()
				.getStringArray(R.array.sales_execution_marital_status));
		ArrayAdapter<String> maritalStatusAdapter = new ArrayAdapter<String>(
				getActivity().getApplicationContext(), R.layout.spinner,
				maritalStatusList);
		spinnerMaritalStatus.setAdapter(maritalStatusAdapter);
		spinnerMaritalStatus
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (spinnerMaritalStatus.getAdapter() != null
								&& spinnerMaritalStatus.getAdapter().getItem(
										position) != null) {
							if (firstTime[2]) {
								if (saleExecution != null
										&& saleExecution.getMaritalStatus() != null) {
									spinnerMaritalStatus.setSelection(maritalStatusList
											.indexOf(saleExecution
													.getMaritalStatus()));
								} else {
									spinnerMaritalStatus
											.setSelection(maritalStatusList
													.indexOf(0));
								}
								firstTime[2] = false;
							} else if (!maritalStatusList.get(position).equals(
									"")) {
								saleExecution
										.setMaritalStatus(maritalStatusList
												.get(position));
							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		final List<String> sportsList = Arrays.asList(getResources()
				.getStringArray(R.array.sales_execution_favourite_sports));
		ArrayAdapter<String> favouriteSportsAdapter = new ArrayAdapter<String>(
				getActivity().getApplicationContext(), R.layout.spinner,
				sportsList);
		spinnerFavouriteSports.setAdapter(favouriteSportsAdapter);
		spinnerFavouriteSports
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (spinnerFavouriteSports.getAdapter() != null
								&& spinnerFavouriteSports.getAdapter().getItem(
										position) != null) {
							if (firstTime[3]) {
								if (saleExecution != null
										&& saleExecution.getFavouriteSports() != null) {
									spinnerFavouriteSports.setSelection(sportsList
											.indexOf(saleExecution
													.getFavouriteSports()));
								} else {
									spinnerFavouriteSports
											.setSelection(sportsList.indexOf(0));
								}
								firstTime[3] = false;
							} else if (!sportsList.get(position).equals("")) {
								saleExecution.setFavouriteSports(sportsList
										.get(position));
							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		editTextFirstName = (EditText) rootView
				.findViewById(R.id.edit_text_first_name);
		editTextMobile1 = (EditText) rootView
				.findViewById(R.id.edit_text_mobile_1);
		editTextMobile2 = (EditText) rootView
				.findViewById(R.id.edit_text_mobile_2);
		editTextEmail = (EditText) rootView.findViewById(R.id.edit_text_email);
		editTextLastName = (EditText) rootView
				.findViewById(R.id.edit_text_last_name);
		editTextPhone = (EditText) rootView.findViewById(R.id.edit_text_phone);
		editTextExtension = (EditText) rootView
				.findViewById(R.id.edit_text_extension);
		editTextFax = (EditText) rootView.findViewById(R.id.edit_text_fax);
		EditText editTextMailingStreet = (EditText) rootView
				.findViewById(R.id.edit_text_mailing_street);
		EditText editTextMailingCity = (EditText) rootView
				.findViewById(R.id.edit_text_mailing_city);
		EditText editTextMailingStateProvince = (EditText) rootView
				.findViewById(R.id.edit_text_mailing_state_province);
		EditText editTextMailingPostalCode = (EditText) rootView
				.findViewById(R.id.edit_text_mailing_postal_code);
		EditText editTextCountry = (EditText) rootView
				.findViewById(R.id.edit_text_mailing_country);
		editTextMailingAddress = (EditText) rootView
				.findViewById(R.id.edit_text_mailing_address);
		editTextPreferredName = (EditText) rootView
				.findViewById(R.id.edit_text_preferred_name);
		editTextNationalityId = (EditText) rootView
				.findViewById(R.id.edit_text_national_id);
		editTextCallNote = (EditText) rootView
				.findViewById(R.id.edit_text_call_note);
		editTextFavouriteFood = (EditText) rootView
				.findViewById(R.id.edit_favourite_food);
		editTextNotFavouriteFood = (EditText) rootView
				.findViewById(R.id.edit_not_favourite_food);
		editTextFavouriteDrink = (EditText) rootView
				.findViewById(R.id.edit_favourite_drink);
		editTextNotFavouriteDrink = (EditText) rootView
				.findViewById(R.id.edit_not_favourite_drink);
		editTextHobbies = (EditText) rootView.findViewById(R.id.edit_hobbies);
		editTextFavouriteActivities = (EditText) rootView
				.findViewById(R.id.edit_favourite_activities);
		editTextBirthdate = (EditText) rootView
				.findViewById(R.id.edit_birthdate);

		if (saleExecution.getPreferredName() != null) {
			editTextPreferredName.setText(saleExecution.getPreferredName());
			txtPreferredName.setText(saleExecution.getPreferredName());
		}

		if (saleExecution.getNationalId() != null) {
			editTextNationalityId.setText(saleExecution.getNationalId());
			txtNationalId.setText(saleExecution.getNationalId());
		}

		if (saleExecution.getCallNote() != null) {
			editTextCallNote.setText(saleExecution.getCallNote());
			txtCallNote.setText(saleExecution.getCallNote());
		}

		if (saleExecution.getFavouriteFood() != null) {
			editTextFavouriteFood.setText(saleExecution.getFavouriteFood());
			txtFavouriteFood.setText(saleExecution.getFavouriteFood());
		}

		if (saleExecution.getNotFavouriteFood() != null) {
			editTextNotFavouriteFood.setText(saleExecution
					.getNotFavouriteFood());
			txtNotFavouriteFood.setText(saleExecution.getNotFavouriteFood());
		}

		if (saleExecution.getFavouriteDrink() != null) {
			editTextFavouriteDrink.setText(saleExecution.getFavouriteDrink());
			txtFavouriteDrink.setText(saleExecution.getFavouriteDrink());
		}

		if (saleExecution.getNotFavouriteDrink() != null) {
			editTextNotFavouriteDrink.setText(saleExecution
					.getNotFavouriteDrink());
			txtNotFavouriteDrink.setText(saleExecution.getNotFavouriteDrink());
		}

		if (saleExecution.getHobbies() != null) {
			editTextHobbies.setText(saleExecution.getHobbies());
			txtHobbies.setText(saleExecution.getHobbies());
		}

		if (saleExecution.getFavouriteActivities() != null) {
			editTextFavouriteActivities.setText(saleExecution
					.getFavouriteActivities());
			txtFavouriteActivities.setText(saleExecution
					.getFavouriteActivities());
		}

		editTextMailingStreet.setVisibility(View.GONE);
		editTextMailingCity.setVisibility(View.GONE);
		editTextMailingStateProvince.setVisibility(View.GONE);
		editTextMailingPostalCode.setVisibility(View.GONE);
		editTextCountry.setVisibility(View.GONE);
		editTextPhone.setVisibility(View.GONE);
		editTextExtension.setVisibility(View.GONE);
		editTextFax.setVisibility(View.GONE);

		final DatePicker datePickerBirthdate = (DatePicker) rootView
				.findViewById(R.id.datePicker_birthdate);
		Calendar c = Calendar.getInstance();
		if (saleExecution.getBirthDate() != null) {
			editTextBirthdate.setVisibility(View.GONE);
			datePickerBirthdate.setVisibility(View.VISIBLE);
			String date = saleExecution.getBirthDate();
			String[] separated = date.split("-");
			datePickerBirthdate.updateDate(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]));
			datePickerBirthdate.init(Integer.parseInt(separated[0]),
					Integer.parseInt(separated[1]) - 1,
					Integer.parseInt(separated[2]),
					new OnDateChangedListener() {
						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							saleExecution.setBirthDate(datePickerBirthdate
									.getYear()
									+ "-"
									+ (datePickerBirthdate.getMonth() + 1)
									+ "-" + datePickerBirthdate.getDayOfMonth());
						}
					});
		} else {
			datePickerBirthdate.setVisibility(View.GONE);
			editTextBirthdate.setVisibility(View.VISIBLE);
			editTextBirthdate.setFocusable(false);
			editTextBirthdate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					final HolcimSelectedRetailerActivity activity = (HolcimSelectedRetailerActivity) getActivity();
					String date = saleExecution.getBirthDate();
					activity.dialog.showDatePickerModal(date,
							getString(R.string.ok), getString(R.string.cancel),
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									final DatePicker datePickerBirthdate2 = (DatePicker) activity.dialog
											.findViewById(R.id.datePicker_birthdate_modal);
									activity.dialog.dismiss();
									editTextBirthdate
											.setText((datePickerBirthdate2
													.getMonth() + 1)
													+ "/"
													+ datePickerBirthdate2
															.getDayOfMonth()
													+ "/"
													+ datePickerBirthdate2
															.getYear());
									saleExecution
											.setBirthDate(datePickerBirthdate2
													.getYear()
													+ "-"
													+ (datePickerBirthdate2
															.getMonth() + 1)
													+ "-"
													+ datePickerBirthdate2
															.getDayOfMonth());
								}
							}, activity.dialog.mDismissClickListener);
				}
			});

		}

		if (saleExecution.getFirstName() != null) {
			editTextFirstName.setText(saleExecution.getFirstName());
			txtFirstName.setText(saleExecution.getFirstName());
		}

		if (saleExecution.getLastName() != null) {
			editTextLastName.setText(saleExecution.getLastName());
			txtLastName.setText(saleExecution.getLastName());
		}

		if (saleExecution.getGender() != null) {
			spinnerGender.setSelection(genderList.indexOf(saleExecution
					.getGender()));
			txtGender.setText(saleExecution.getGender());
		}

		if (saleExecution.getMailingAddress() != null) {
			editTextMailingAddress.setText(saleExecution.getMailingAddress());
			txtMailingAddress.setText(saleExecution.getMailingAddress());
		}

		if (saleExecution.getContactEmail1() != null) {
			editTextEmail.setText(saleExecution.getContactEmail1());
			txtEmail1.setText(saleExecution.getContactEmail1());
		}

		if (saleExecution.getContactMobile1() != null
				&& !saleExecution.getContactMobile1().equals("")) {
			editTextMobile1.setText(saleExecution.getContactMobile1());
			txtMobile1.setText(saleExecution.getContactMobile1());
		}

		if (saleExecution.getContactMobile2() != null
				&& !saleExecution.getContactMobile1().equals("")) {
			editTextMobile2.setText(saleExecution.getContactMobile2());
			txtMobile2.setText(saleExecution.getContactMobile2());
		}

		if (saleExecution.getPhone1() != null) {
			editTextPhone.setText(saleExecution.getPhone1());
		}

		if (saleExecution.getExtension1() != null) {
			editTextExtension.setText(saleExecution.getExtension1());
		}

		if (saleExecution.getFax1() != null) {
			editTextFax.setText(saleExecution.getFax1());
		}

		if (saleExecution.getAccountName() != null) {
			textViewAccountNameValue.setText(saleExecution.getAccountName());
		}

		Button createNewContact = (Button) rootView
				.findViewById(R.id.button_finish);
		createNewContact.setVisibility(View.INVISIBLE);

		ImageButton next = (ImageButton) rootView
				.findViewById(R.id.imageButton_next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((HolcimSelectedRetailerActivity) getActivity())
						.isCanEditByDate()) {
					saveChanges();
					fragmentContactInfoActions.goToPreviousSale(saleExecution,
							comeptitorsMarketings, actionLogs, preOrders);
				} else {
					fragmentContactInfoActions.goToPreviousSale(saleExecution,
							comeptitorsMarketings, actionLogs, preOrders);
				}
			}
		});

		ImageButton back = (ImageButton) rootView
				.findViewById(R.id.imageButton_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((HolcimSelectedRetailerActivity) getActivity())
						.isCanEditByDate()) {
					saveChanges();
					fragmentContactInfoActions.goBackFromContactInfo(
							saleExecution, comeptitorsMarketings, actionLogs,
							preOrders);
				} else {
					fragmentContactInfoActions.goBackFromContactInfo(
							saleExecution, comeptitorsMarketings, actionLogs,
							preOrders);
				}
			}
		});
		if (!((HolcimSelectedRetailerActivity) getActivity()).isCanEditByDate()) {
			// Remove editText and Spinner to the user cannot change fields if
			// is not today's date
			spnTitle.setVisibility(View.GONE);
			editTextFirstName.setVisibility(View.GONE);
			editTextLastName.setVisibility(View.GONE);
			editTextPreferredName.setVisibility(View.GONE);
			editTextNationalityId.setVisibility(View.GONE);
			spinnerGender.setVisibility(View.GONE);
			spinnerMaritalStatus.setVisibility(View.GONE);
			spinnerNationality.setVisibility(View.GONE);
			spnReligion.setVisibility(View.GONE);
			spnContact.setVisibility(View.GONE);
			editTextMailingAddress.setVisibility(View.GONE);
			editTextMobile1.setVisibility(View.GONE);
			editTextMobile2.setVisibility(View.GONE);
			editTextCallNote.setVisibility(View.GONE);
			editTextEmail.setVisibility(View.GONE);
			editTextFavouriteFood.setVisibility(View.GONE);
			editTextNotFavouriteFood.setVisibility(View.GONE);
			editTextFavouriteDrink.setVisibility(View.GONE);
			editTextNotFavouriteDrink.setVisibility(View.GONE);
			editTextHobbies.setVisibility(View.GONE);
			editTextFavouriteActivities.setVisibility(View.GONE);
			spinnerFavouriteSports.setVisibility(View.GONE);

			// visibility of the datepicker
			if (saleExecution.getBirthDate() != null) {
				editTextBirthdate.setVisibility(View.GONE);
				datePickerBirthdate.setVisibility(View.VISIBLE);
				datePickerBirthdate.setEnabled(false);
			} else {
				datePickerBirthdate.setVisibility(View.GONE);
				editTextBirthdate.setVisibility(View.INVISIBLE);
			}

			// Add textView to the user can see all fields values
			txtTitle.setVisibility(View.VISIBLE);
			txtFirstName.setVisibility(View.VISIBLE);
			txtLastName.setVisibility(View.VISIBLE);
			txtPreferredName.setVisibility(View.VISIBLE);
			txtNationalId.setVisibility(View.VISIBLE);
			txtGender.setVisibility(View.VISIBLE);
			txtMaritalStatus.setVisibility(View.VISIBLE);
			txtNationaly.setVisibility(View.VISIBLE);
			txtReligion.setVisibility(View.VISIBLE);
			txtPreferredContactMethod.setVisibility(View.VISIBLE);
			txtMailingAddress.setVisibility(View.VISIBLE);
			txtMobile1.setVisibility(View.VISIBLE);
			txtMobile2.setVisibility(View.VISIBLE);
			txtCallNote.setVisibility(View.VISIBLE);
			txtEmail1.setVisibility(View.VISIBLE);
			txtFavouriteFood.setVisibility(View.VISIBLE);
			txtNotFavouriteFood.setVisibility(View.VISIBLE);
			txtFavouriteDrink.setVisibility(View.VISIBLE);
			txtNotFavouriteDrink.setVisibility(View.VISIBLE);
			txtHobbies.setVisibility(View.VISIBLE);
			txtFavouriteActivities.setVisibility(View.VISIBLE);
			txtFavouriteSports.setVisibility(View.VISIBLE);
		}
		return rootView;
	}

	private void warningTelephoneMobile() {
		final HolcimSelectedRetailerActivity activity = (HolcimSelectedRetailerActivity) getActivity();

		activity.dialog.showError(getString(R.string.warning_telephone_mobile),
				getString(R.string.ok), new OnClickListener() {

					@Override
					public void onClick(View v) {
						activity.dialog.dismiss();
					}
				});
	}

	public void refreshImage() {
		try {
			if (saleExecution.isContactPictureTempFileExist(getActivity())) {
				camera.setImageBitmap(HolcimUtility
						.decodeSampledBitmapFromFile(saleExecution
								.getTempContactPictureFilePath(getActivity()),
								200, 200, 0));
			} else if (saleExecution.getId() != null
					&& saleExecution
							.isContactPictureTempFileExist(getActivity())) {
				camera.setImageBitmap(HolcimUtility
						.decodeSampledBitmapFromFile(saleExecution
								.getContactSaleExPicturePath(getActivity()),
								200, 200, 0));
			} else {
				camera.setImageResource(R.drawable.camera);
			}
		} catch (HolcimException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == HolcimConsts.ACTIVITY_REQUEST_CAMERA_LOG) {
			if (resultCode == HolcimConsts.ACTIVITY_RESULT_CODE_CAMERA_OK) {
				refreshImage();
			}
		}
	}

	public void saveChanges() {

		if (editTextFirstName.getText() != null) {
			saleExecution.setFirstName(String.valueOf(editTextFirstName
					.getText()));
		}

		if (editTextLastName.getText() != null) {
			saleExecution
					.setLastName(String.valueOf(editTextLastName.getText()));
		}

		if (editTextPhone.getText() != null) {
			saleExecution.setPhone1(String.valueOf(editTextPhone.getText()));
		}

		if (editTextMobile1.getText() != null) {
			saleExecution.setContactMobile1(String.valueOf(editTextMobile1
					.getText()));
		}

		if (editTextMobile2.getText() != null) {
			saleExecution.setContactMobile2(String.valueOf(editTextMobile2
					.getText()));
		}

		if (editTextEmail.getText() != null) {
			saleExecution.setContactEmail1(String.valueOf(editTextEmail
					.getText()));
		}

		if (editTextExtension.getText() != null) {
			saleExecution.setExtension1(String.valueOf(editTextExtension
					.getText()));
		}

		if (editTextFax.getText() != null) {
			saleExecution.setFax1(String.valueOf(editTextFax.getText()));
		}

		if (editTextMailingAddress.getText() != null) {
			saleExecution.setMailingAddress(String
					.valueOf(editTextMailingAddress.getText()));
		}

		if (editTextPreferredName.getText() != null) {
			saleExecution.setPreferredName(String.valueOf(editTextPreferredName
					.getText()));
		}

		if (editTextNationalityId.getText() != null) {
			saleExecution.setNationalId(String.valueOf(editTextNationalityId
					.getText()));
		}

		if (spnReligion.getSelectedItem() != null) {
			saleExecution.setReligion(String.valueOf(spnReligion
					.getSelectedItem()));
		}

		if (spnContact.getSelectedItem() != null) {
			saleExecution.setPreferredContactMethod(String.valueOf(spnContact
					.getSelectedItem()));
		}

		if (editTextCallNote.getText() != null) {
			saleExecution
					.setCallNote(String.valueOf(editTextCallNote.getText()));
		}

		if (spnTitle.getSelectedItem() != null) {
			saleExecution.setTitle(String.valueOf(spnTitle.getSelectedItem()));
		}

		if (editTextFavouriteFood.getText() != null) {
			saleExecution.setFavouriteFood(String.valueOf(editTextFavouriteFood
					.getText()));
		}

		if (editTextNotFavouriteFood.getText() != null) {
			saleExecution.setNotFavouriteFood(String
					.valueOf(editTextNotFavouriteFood.getText()));
		}

		if (editTextFavouriteDrink.getText() != null) {
			saleExecution.setFavouriteDrink(String
					.valueOf(editTextFavouriteDrink.getText()));
		}

		if (editTextNotFavouriteDrink.getText() != null) {
			saleExecution.setNotFavouriteDrink(String
					.valueOf(editTextNotFavouriteDrink.getText()));
		}

		if (editTextHobbies.getText() != null) {
			saleExecution.setHobbies(String.valueOf(editTextHobbies.getText()));
		}

		if (editTextFavouriteActivities.getText() != null) {
			saleExecution.setFavouriteActivities(String
					.valueOf(editTextFavouriteActivities.getText()));
		}
	}
}
