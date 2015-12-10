package com.holcim.altimetrik.android.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.Account;
import com.holcim.altimetrik.android.model.AccountDao;
import com.holcim.altimetrik.android.model.Contact;
import com.holcim.altimetrik.android.utilities.AltimetrikException;
import com.holcim.altimetrik.android.utilities.AltimetrikFileHandler;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimUtility;

public class HolcimCreateNewContactActivity extends HolcimCustomActivity {
	private Contact contact;
	ImageButton camera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.create_new_contact_row);
		super.onCreate(savedInstanceState);

		String[] contactFields = getResources().getStringArray(
				R.array.contact_fields);
		Button buttonFinish = (Button) findViewById(R.id.button_finish);
		buttonFinish.setVisibility(View.VISIBLE);

		TextView textViewTitleContactInfo = (TextView) findViewById(R.id.textView_fragment_title);
		textViewTitleContactInfo.setVisibility(View.GONE);

		ImageView homePage = (ImageView) findViewById(R.id.imageButton_home);
		homePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.showWarning(getString(R.string.message_warning_go_home),
						getString(R.string.ok), getString(R.string.cancel),
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
								HolcimCustomActivity.setOnback(true);
								Intent intent = new Intent(
										HolcimCreateNewContactActivity.this,
										HolcimMainActivity.class);
								startActivity(intent);
							}
						}, dialog.mDismissClickListener);
			}
		});

		TextView textViewFirstName = (TextView) findViewById(R.id.text_view_first_name);
		TextView textViewGender = (TextView) findViewById(R.id.text_view_gender);
		TextView textViewNationality = (TextView) findViewById(R.id.text_view_nationality);
		TextView textViewMobile1 = (TextView) findViewById(R.id.text_view_mobile_1);
		TextView textViewMobile2 = (TextView) findViewById(R.id.text_view_mobile_2);
		TextView textViewEmail = (TextView) findViewById(R.id.text_view_email);
		TextView textViewLastName = (TextView) findViewById(R.id.text_view_last_name);
		TextView textViewBirthdate = (TextView) findViewById(R.id.text_view_birthdate);
		TextView textViewPhone = (TextView) findViewById(R.id.text_view_phone);
		TextView textViewExtension = (TextView) findViewById(R.id.text_view_extension);
		TextView textViewFax = (TextView) findViewById(R.id.text_view_fax);
		TextView textViewMailingStreet = (TextView) findViewById(R.id.text_view_mailing_street);
		TextView textViewMailingCity = (TextView) findViewById(R.id.text_view_mailing_city);
		TextView textViewMailingStateProvince = (TextView) findViewById(R.id.text_view_mailing_state_province);
		TextView textViewMailingPostalCode = (TextView) findViewById(R.id.text_view_mailing_postal_code);
		TextView textViewCountry = (TextView) findViewById(R.id.text_view_mailing_country);
		TextView textViewAddress = (TextView) findViewById(R.id.text_view_mailing_address);
		TextView textViewAccountId = (TextView) findViewById(R.id.text_view_account_id);
		TextView textViewAccountName = (TextView) findViewById(R.id.text_view_account_name);
		TextView editTextAccountId = (TextView) findViewById(R.id.edit_text_account_id);
		TextView textViewAccountNameValue = (TextView) findViewById(R.id.text_view_account_name_value);
		TextView textViewContactIdValue = (TextView) findViewById(R.id.text_view_contact_id_value);
		TextView textViewContactId = (TextView) findViewById(R.id.text_view_contact_id);
		TextView textViewTitle = (TextView) findViewById(R.id.text_view_title);
		TextView textViewPreferredName = (TextView) findViewById(R.id.text_view_preferred_name);
		TextView textViewNationalId = (TextView) findViewById(R.id.text_view_national_id);
		TextView textViewMaritalStatus = (TextView) findViewById(R.id.text_view_marital_status);
		TextView textViewReligion = (TextView) findViewById(R.id.text_view_religion);
		TextView textViewPreferredContactMethod = (TextView) findViewById(R.id.text_view_preferred_Contact_method);
		TextView textViewCallNote = (TextView) findViewById(R.id.text_view_call_note);
		TextView textViewFavouriteFood = (TextView) findViewById(R.id.text_favourite_food);
		TextView textViewFavouriteSports = (TextView) findViewById(R.id.text_favourite_sports);
		TextView textViewNotFavouriteFood = (TextView) findViewById(R.id.text_not_favourite_food);
		TextView textViewFavouriteDrink = (TextView) findViewById(R.id.text_favourite_drink);
		TextView textViewNotFavouriteDrink = (TextView) findViewById(R.id.text_not_favourite_drink);
		TextView textViewFavouriteActivities = (TextView) findViewById(R.id.text_favourite_activities);
		TextView textViewHobbies = (TextView) findViewById(R.id.text_hobbies);

		textViewFirstName.setText(contactFields[0]);
		textViewLastName.setText(contactFields[1]);
		textViewGender.setText(contactFields[2]);
		textViewBirthdate.setText(contactFields[3]);
		textViewNationality.setText(contactFields[4]);
		textViewPhone.setText(contactFields[5]);
		textViewMobile1.setText(contactFields[6]);
		textViewEmail.setText(contactFields[7]);
		textViewExtension.setText(contactFields[8]);
		textViewFax.setText(contactFields[9]);
		textViewMailingStreet.setText(contactFields[10]);
		textViewMailingCity.setText(contactFields[11]);
		textViewMailingStateProvince.setText(contactFields[12]);
		textViewMailingPostalCode.setText(contactFields[13]);
		textViewCountry.setText(contactFields[14]);
		textViewAccountName.setText(contactFields[17]);

		textViewAddress.setVisibility(View.GONE);
		textViewAccountId.setVisibility(View.GONE);
		editTextAccountId.setVisibility(View.GONE);
		textViewAccountNameValue.setVisibility(View.GONE);
		textViewMobile2.setVisibility(View.GONE);
		textViewContactIdValue.setVisibility(View.GONE);
		textViewContactId.setVisibility(View.GONE);
		textViewTitle.setVisibility(View.GONE);
		textViewPreferredName.setVisibility(View.GONE);
		textViewNationalId.setVisibility(View.GONE);
		textViewReligion.setVisibility(View.GONE);
		textViewPreferredContactMethod.setVisibility(View.GONE);
		textViewCallNote.setVisibility(View.GONE);
		textViewFavouriteFood.setVisibility(View.GONE);
		textViewNotFavouriteFood.setVisibility(View.GONE);
		textViewFavouriteDrink.setVisibility(View.GONE);
		textViewNotFavouriteDrink.setVisibility(View.GONE);
		textViewFavouriteActivities.setVisibility(View.GONE);
		textViewHobbies.setVisibility(View.GONE);
		textViewMaritalStatus.setVisibility(View.GONE);
		textViewFavouriteSports.setVisibility(View.GONE);

		final Spinner spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
		final Spinner spinnerNationality = (Spinner) findViewById(R.id.spinner_nationality);
		final Spinner spinnerAccountName = (Spinner) findViewById(R.id.spinner_account_name);
		final Spinner spinnerMaritalStatus = (Spinner) findViewById(R.id.spinner_marital_status);
		final Spinner spinnerFavouriteSports = (Spinner) findViewById(R.id.spinner_favourite_sports);

		spinnerMaritalStatus.setVisibility(View.GONE);
		spinnerFavouriteSports.setVisibility(View.GONE);

		contact = new Contact();

		final List<String> genderList = Arrays.asList(getResources()
				.getStringArray(R.array.gender));
		ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner, genderList);
		spinnerGender.setAdapter(genderAdapter);
		spinnerGender.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (spinnerGender.getAdapter() != null
						&& spinnerGender.getAdapter().getItem(position) != null
						&& !genderList.get(position).equals("")) {
					contact.setGender(genderList.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		final List<String> nationallityList = Arrays.asList(getResources()
				.getStringArray(R.array.nationality));
		ArrayAdapter<String> NationallityAdapter = new ArrayAdapter<String>(
				this, R.layout.spinner, nationallityList);
		spinnerNationality.setAdapter(NationallityAdapter);
		spinnerNationality.setSelection(nationallityList
				.indexOf(HolcimConsts.DEFAULT_NATIONALITY));
		spinnerNationality
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (spinnerNationality.getAdapter() != null
								&& spinnerNationality.getAdapter().getItem(
										position) != null
								&& !nationallityList.get(position).equals("")) {
							contact.setNationality(nationallityList
									.get(position));
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		final List<String> accountList = new ArrayList<String>();
		accountList.add("");
		for (Account account : HolcimApp.daoSession.getAccountDao().loadAll()) {
			accountList.add(account.getName());
		}

		Collections.sort(accountList, String.CASE_INSENSITIVE_ORDER);

		ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner, accountList);
		spinnerAccountName.setAdapter(accountAdapter);
		spinnerAccountName
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (spinnerAccountName.getAdapter() != null
								&& spinnerAccountName.getAdapter().getItem(
										position) != null
								&& !accountList.get(position).equals("")) {
							List<Account> acc = HolcimApp.daoSession
									.getAccountDao()
									.queryBuilder()
									.where(AccountDao.Properties.Name
											.eq(accountList.get(position)))
									.list();
							contact.setAccountName(acc.get(0).getName());
							contact.setAccountId(acc.get(0).getSalesforceId());

						} else if (accountList.get(position).equals("")) {

							contact.setAccountName("");
							contact.setAccountId("");
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		final EditText editTextFirstName = (EditText) findViewById(R.id.edit_text_first_name);
		final EditText editTextMobile1 = (EditText) findViewById(R.id.edit_text_mobile_1);
		final EditText editTextMobile2 = (EditText) findViewById(R.id.edit_text_mobile_2);
		final EditText editTextEmail = (EditText) findViewById(R.id.edit_text_email);
		final EditText editTextLastName = (EditText) findViewById(R.id.edit_text_last_name);
		final EditText editTextPhone = (EditText) findViewById(R.id.edit_text_phone);
		final EditText editTextExtension = (EditText) findViewById(R.id.edit_text_extension);
		final EditText editTextFax = (EditText) findViewById(R.id.edit_text_fax);
		final EditText editTextMailingStreet = (EditText) findViewById(R.id.edit_text_mailing_street);
		final EditText editTextMailingCity = (EditText) findViewById(R.id.edit_text_mailing_city);
		final EditText editTextMailingStateProvince = (EditText) findViewById(R.id.edit_text_mailing_state_province);
		final EditText editTextMailingPostalCode = (EditText) findViewById(R.id.edit_text_mailing_postal_code);
		final EditText editTextMailingCountry = (EditText) findViewById(R.id.edit_text_mailing_country);
		final EditText editTextMailingAddress = (EditText) findViewById(R.id.edit_text_mailing_address);
		final EditText editTextPreferredName = (EditText) findViewById(R.id.edit_text_preferred_name);
		final EditText editTextNationalId = (EditText) findViewById(R.id.edit_text_national_id);
		final EditText editTextBirthdate = (EditText) findViewById(R.id.edit_birthdate);

		final Spinner spnReligion = (Spinner) findViewById(R.id.spinner_religion);
		final Spinner spnTitle = (Spinner) findViewById(R.id.spinner_title);
		final Spinner spnContact = (Spinner) findViewById(R.id.spinner_contact);

		EditText editTextCallNote = (EditText) findViewById(R.id.edit_text_call_note);
		EditText editTextFavouriteFood = (EditText) findViewById(R.id.edit_favourite_food);
		EditText editTextNotFavouriteFood = (EditText) findViewById(R.id.edit_not_favourite_food);
		EditText editTextFavouriteDrink = (EditText) findViewById(R.id.edit_favourite_drink);
		EditText editTextNotFavouriteDrink = (EditText) findViewById(R.id.edit_not_favourite_drink);
		EditText editTextFavouriteActivities = (EditText) findViewById(R.id.edit_favourite_activities);
		EditText editTextHobbies = (EditText) findViewById(R.id.edit_hobbies);

		editTextMailingAddress.setVisibility(View.GONE);
		editTextMobile2.setVisibility(View.GONE);
		spnTitle.setVisibility(View.GONE);
		editTextPreferredName.setVisibility(View.GONE);
		editTextNationalId.setVisibility(View.GONE);
		spnReligion.setVisibility(View.GONE);
		spnContact.setVisibility(View.GONE);
		editTextCallNote.setVisibility(View.GONE);
		editTextFavouriteFood.setVisibility(View.GONE);
		editTextNotFavouriteFood.setVisibility(View.GONE);
		editTextFavouriteDrink.setVisibility(View.GONE);
		editTextNotFavouriteDrink.setVisibility(View.GONE);
		editTextFavouriteActivities.setVisibility(View.GONE);
		editTextHobbies.setVisibility(View.GONE);

		final DatePicker datePickerBirthdate = (DatePicker) findViewById(R.id.datePicker_birthdate);
		datePickerBirthdate.setVisibility(View.GONE);
		editTextBirthdate.setVisibility(View.VISIBLE);
		editTextBirthdate.setFocusable(false);
		editTextBirthdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String date = contact.getBirthDate();
				dialog.showDatePickerModal(date, getString(R.string.ok),
						getString(R.string.cancel), new OnClickListener() {

							@Override
							public void onClick(View v) {
								final DatePicker datePickerBirthdate2 = (DatePicker) dialog
										.findViewById(R.id.datePicker_birthdate_modal);
								dialog.dismiss();
								editTextBirthdate.setText((datePickerBirthdate2
										.getMonth() + 1)
										+ "/"
										+ datePickerBirthdate2.getDayOfMonth()
										+ "/" + datePickerBirthdate2.getYear());
								contact.setBirthDate(datePickerBirthdate2
										.getYear()
										+ "-"
										+ (datePickerBirthdate2.getMonth() + 1)
										+ "-"
										+ datePickerBirthdate2.getDayOfMonth());
							}
						}, dialog.mDismissClickListener);
			}
		});

		camera = (ImageButton) findViewById(R.id.imageButton_camera);
		refreshImage();

		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				takePhoto();
			}
		});

		ImageButton next = (ImageButton) findViewById(R.id.imageButton_next);
		ImageButton back = (ImageButton) findViewById(R.id.imageButton_back);

		next.setVisibility(View.INVISIBLE);
		back.setVisibility(View.INVISIBLE);

		buttonFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (contact.getAccountId() == null
						|| contact.getAccountId().isEmpty()) {
					dialog.showError(
							getString(R.string.warning_account_contact),
							getString(R.string.ok), new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				} else if (!HolcimUtility.isValidEmail(editTextEmail.getText())) {
					dialog.showError(getString(R.string.warning_email_contact),
							getString(R.string.ok), new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				} else if (editTextLastName.getText() != null
						&& !String.valueOf(editTextLastName.getText()).equals(
								"")) {
					contact.setLastName(String.valueOf(editTextLastName
							.getText()));

					if (editTextFirstName.getText() != null) {
						contact.setFirstName(String.valueOf(editTextFirstName
								.getText()));
					}

					if (editTextPhone.getText() != null) {
						contact.setPhone(String.valueOf(editTextPhone.getText()));
					}

					if (editTextMobile1.getText() != null) {
						contact.setMobile(String.valueOf(editTextMobile1
								.getText()));
					}

					if (editTextEmail.getText() != null) {
						contact.setEmail(String.valueOf(editTextEmail.getText()));
					}

					if (editTextExtension.getText() != null) {
						contact.setExtension(String.valueOf(editTextExtension
								.getText()));
					}

					if (editTextFax != null) {
						contact.setFax(String.valueOf(editTextFax.getText()));
					}

					if (editTextMailingStreet != null) {
						contact.setMailingStreet(String
								.valueOf(editTextMailingStreet.getText()));
					}

					if (editTextMailingCity != null) {
						contact.setMailingCity(String
								.valueOf(editTextMailingCity.getText()));
					}

					if (editTextMailingStateProvince != null) {
						contact.setMailingStateProvince(String
								.valueOf(editTextMailingStateProvince.getText()));
					}

					if (editTextMailingPostalCode != null) {
						contact.setMailingPostalCode(String
								.valueOf(editTextMailingPostalCode.getText()));
					}

					if (editTextMailingCountry != null) {
						contact.setMailingCountry(String
								.valueOf(editTextMailingCountry.getText()));
					}

					HolcimApp.daoSession.insert(contact);

					try {
						if (AltimetrikFileHandler.isFileExist(contact
								.getTempPictureFilePath(HolcimCreateNewContactActivity.this))) {
							if (contact
									.isPictureFileExist(HolcimCreateNewContactActivity.this)) {
								AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
										HolcimCreateNewContactActivity.this);
								try {
									fileHandler.moveFile(
											contact.getTempPictureFilePath(HolcimCreateNewContactActivity.this),
											HolcimDataSource
													.getContactDir(HolcimCreateNewContactActivity.this)
													+ File.separator
													+ contact
															.getPictureFileName());
									AltimetrikFileHandler.DeleteFile(contact
											.getPicturePath(HolcimCreateNewContactActivity.this));
									AltimetrikFileHandler.renameFile(
											HolcimDataSource
													.getContactDir(HolcimCreateNewContactActivity.this)
													+ File.separator
													+ contact
															.getTempPictureFileName(HolcimCreateNewContactActivity.this),
											HolcimDataSource
													.getContactDir(HolcimCreateNewContactActivity.this)
													+ File.separator
													+ contact
															.getPictureFileName());
								} catch (Exception e) {
								}
							} else {
								try {
									AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
											HolcimCreateNewContactActivity.this);
									fileHandler.moveFile(
											contact.getTempPictureFilePath(HolcimCreateNewContactActivity.this),
											HolcimDataSource
													.getContactDir(HolcimCreateNewContactActivity.this)
													+ File.separator
													+ contact
															.getPictureFileName());
									AltimetrikFileHandler.renameFile(
											HolcimDataSource
													.getContactDir(HolcimCreateNewContactActivity.this)
													+ File.separator
													+ contact
															.getTempPictureFileName(HolcimCreateNewContactActivity.this),
											HolcimDataSource
													.getContactDir(HolcimCreateNewContactActivity.this)
													+ File.separator
													+ contact
															.getPictureFileName());
								} catch (Exception e) {
								}
							}
						}
					} catch (HolcimException e) {
						e.printStackTrace();
					}

					Intent intent = new Intent(
							HolcimCreateNewContactActivity.this,
							HolcimContactActivity.class);
					startActivity(intent);

				} else {
					dialog.showError(
							getString(R.string.warning_last_name_contact),
							getString(R.string.ok), new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				}
			}
		});

		setCustomTitle(getResources().getString(R.string.new_contact));
	}

	public void refreshImage() {
		try {
			if (contact.isPictureTempFileExist(this)) {
				camera.setImageBitmap(HolcimUtility
						.decodeSampledBitmapFromFile(
								contact.getTempPictureFilePath(this), 200, 200,
								0));
			} else if (contact.getId() != null
					&& contact.isPictureFileExist(this)) {
				camera.setImageBitmap(HolcimUtility
						.decodeSampledBitmapFromFile(
								contact.getPicturePath(this), 200, 200, 0));
			} else {
				camera.setImageResource(R.drawable.camera);
			}
		} catch (HolcimException e) {
			e.printStackTrace();
		}
	}

	public void takePhoto() {
		Intent mIntent = new Intent(this, HolcimCameraActivity.class);
		startActivityForResult(mIntent,
				HolcimConsts.ACTIVITY_REQUEST_CAMERA_LOG);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == HolcimConsts.ACTIVITY_REQUEST_CAMERA_LOG) {
			if (resultCode == HolcimConsts.ACTIVITY_RESULT_CODE_CAMERA_OK) {
				refreshImage();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (!HolcimCustomActivity.blockback) {
			super.onBackPressed();
			HolcimCustomActivity.setOnback(true);
			try {
				AltimetrikFileHandler.DeleteFile(contact
						.getTempPictureFilePath(this));
			} catch (AltimetrikException e) {
				e.printStackTrace();
			} catch (HolcimException e) {
				e.printStackTrace();
			}
		}
	}
}
