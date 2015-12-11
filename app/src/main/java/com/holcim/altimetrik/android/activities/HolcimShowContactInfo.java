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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker.OnDateChangedListener;

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

public class HolcimShowContactInfo extends HolcimCustomActivity{
	private Contact contact;
	ImageButton camera;
	private Boolean canEdit;
	EditText editTextBirthdate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.show_contact_info);
		super.onCreate(savedInstanceState);
		editTextBirthdate = (EditText) findViewById(R.id.edit_birthdate);
		ImageView homePage = (ImageView)findViewById(R.id.imageButton_home);
		homePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(contact != null && contact.getSalesforceId() == null){
					dialog.showWarning(getString(R.string.message_warning_go_home), getString(R.string.ok), getString(R.string.cancel), new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							HolcimCustomActivity.setOnback(true);
							Intent intent = new Intent(HolcimShowContactInfo.this, HolcimMainActivity.class);
							startActivity(intent);
						}
					}, dialog.mDismissClickListener);
				}else{
					HolcimCustomActivity.setOnback(true);
					Intent intent = new Intent(HolcimShowContactInfo.this, HolcimMainActivity.class);
					startActivity(intent);
				}
			}
		});

		if(getIntent().getExtras() != null){
			contact = HolcimApp.daoSession.getContactDao().load(getIntent().getExtras().getLong("contactId"));
		}

		

		final DatePicker datepickerBirthdate = (DatePicker) findViewById(R.id.datePicker_birthdate);
		if(contact != null && contact.getBirthDate() != null){
			editTextBirthdate.setVisibility(View.GONE);
			datepickerBirthdate.setVisibility(View.VISIBLE);	
			String date = contact.getBirthDate();
			String[] separated = date.split("-");
			datepickerBirthdate.updateDate(Integer.parseInt(separated[0]), Integer.parseInt(separated[1]) - 1, Integer.parseInt(separated[2]));	
			datepickerBirthdate.init(Integer.parseInt(separated[0]), (Integer.parseInt(separated[1]) - 1), Integer.parseInt(separated[2]), new OnDateChangedListener() {

				@Override
				public void onDateChanged(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					contact.setBirthDate(datepickerBirthdate.getYear() 
							+ "-" + (datepickerBirthdate.getMonth() + 1) + "-" + datepickerBirthdate.getDayOfMonth());
				}
			});
		}else{
			datepickerBirthdate.setVisibility(View.GONE);
			editTextBirthdate.setVisibility(View.VISIBLE);
			editTextBirthdate.setFocusable(false);
			editTextBirthdate.setOnClickListener(new OnClickListener() {			
				
				@Override
				public void onClick(View v) {
					String date = contact.getBirthDate();
					dialog.showDatePickerModal(date, getString(R.string.ok), getString(R.string.cancel), new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							final DatePicker datePickerBirthdate2 = (DatePicker) dialog.findViewById(R.id.datePicker_birthdate_modal);
							dialog.dismiss();
							editTextBirthdate.setText((datePickerBirthdate2.getMonth() + 1) + "/" + datePickerBirthdate2.getDayOfMonth() + "/" + datePickerBirthdate2.getYear());
							contact.setBirthDate(datePickerBirthdate2.getYear() + "-" + (datePickerBirthdate2.getMonth() + 1) + "-" + datePickerBirthdate2.getDayOfMonth());
						}
					}, dialog.mDismissClickListener);	
				}
			});
		}
		
		canEdit = true;
		if(contact.getSalesforceId() != null){
			datepickerBirthdate.setEnabled(false);
			datepickerBirthdate.setFocusable(false);
			datepickerBirthdate.setClickable(false);
		}

		TextView textViewFirstName = (TextView) findViewById(R.id.textView_first_name_value);
		TextView textViewLastName = (TextView) findViewById(R.id.textView_last_name_value);
		TextView textViewGender = (TextView) findViewById(R.id.textView_gender_value);
		TextView textViewPhone = (TextView) findViewById(R.id.textView_phone_value);
		TextView textViewMobile = (TextView) findViewById(R.id.textView_mobile_value);
		TextView textViewExtension = (TextView) findViewById(R.id.textView_extension_value);
		TextView textViewFax = (TextView) findViewById(R.id.textView_fax_value);
		TextView textViewEmail = (TextView) findViewById(R.id.textView_email_value);
		TextView textViewMailingStreet = (TextView) findViewById(R.id.textView_mailing_street_value);
		TextView textViewMailingCity = (TextView) findViewById(R.id.textView_mailing_city_value);
		TextView textViewMailingStateProvince = (TextView) findViewById(R.id.textView_mailing_state_province_value);
		TextView textViewMailingPostalCode = (TextView) findViewById(R.id.textView_mailing_postal_code_value);
		TextView textViewMailingCountry = (TextView) findViewById(R.id.textView_mailing_country_value);
		TextView textViewNationality = (TextView) findViewById(R.id.textView_nationality_value);
		final TextView textViewAccountId = (TextView) findViewById(R.id.textView_account_id_value);
		TextView textViewAccountName = (TextView) findViewById(R.id.textView_account_name_value);
		TextView textViewBirthdateValue = (TextView) findViewById(R.id.textView_birthdate_value);

		if(contact != null && contact.getRetailerID() != null){
			textViewAccountId.setText(contact.getRetailerID());
		}

		if(!canEdit){
			if(contact != null){
				if(contact.getFirstName() != null){
					textViewFirstName.setText(contact.getFirstName());
				}

				if(contact.getLastName() != null){
					textViewLastName.setText(contact.getLastName());
				}

				if(contact.getGender() != null){
					textViewGender.setText(contact.getGender());
				}

				if(contact.getPhone() != null){
					textViewPhone.setText(contact.getPhone());
				}

				if(contact.getMobile() != null){
					textViewMobile.setText(contact.getMobile());
				}

				if(contact.getExtension() != null){
					textViewExtension.setText(contact.getExtension());
				}

				if(contact.getFax() != null){
					textViewFax.setText(contact.getFax());
				}

				if(contact.getEmail() != null){
					textViewEmail.setText(contact.getEmail());
				}

				if(contact.getMailingStreet() != null){
					textViewMailingStreet.setText(contact.getMailingStreet());
				}

				if(contact.getMailingCity() != null){
					textViewMailingCity.setText(contact.getMailingCity());
				}

				if(contact.getMailingStateProvince() != null){
					textViewMailingStateProvince.setText(contact.getMailingStateProvince());
				}

				if(contact.getMailingPostalCode() != null){
					textViewMailingPostalCode.setText(contact.getMailingPostalCode());
				}
				if(contact.getMailingCountry() != null){
					textViewMailingCountry.setText(contact.getMailingCountry());
				}

				if(contact.getNationality() != null){
					textViewNationality.setText(contact.getNationality());
				}

				if(contact.getAccountName() != null){
					textViewAccountName.setText(contact.getAccountName());
				}
				
				if(contact.getBirthDate() != null){
					editTextBirthdate.setVisibility(View.GONE);
					datepickerBirthdate.setVisibility(View.VISIBLE);
					datepickerBirthdate.setEnabled(false);
				}else{
					datepickerBirthdate.setVisibility(View.GONE);
					editTextBirthdate.setVisibility(View.GONE);
					textViewBirthdateValue.setVisibility(View.VISIBLE);
				}				
				
			}
		}else{
			textViewFirstName.setVisibility(View.GONE);
			textViewLastName.setVisibility(View.GONE);
			textViewGender.setVisibility(View.GONE);
			textViewPhone.setVisibility(View.GONE);
			textViewMobile.setVisibility(View.GONE);
			textViewExtension.setVisibility(View.GONE);
			textViewFax.setVisibility(View.GONE);
			textViewEmail.setVisibility(View.GONE);
			textViewMailingStreet.setVisibility(View.GONE);
			textViewMailingCity.setVisibility(View.GONE);
			textViewMailingStateProvince.setVisibility(View.GONE);
			textViewMailingPostalCode.setVisibility(View.GONE);
			textViewMailingCountry.setVisibility(View.GONE);
			textViewNationality.setVisibility(View.GONE);
			textViewAccountName.setVisibility(View.GONE);

			final EditText editTextFirstName = (EditText) findViewById(R.id.edt_first_name_value);
			final EditText editTextLastName = (EditText) findViewById(R.id.edt_last_name_value);
			final EditText editTextPhone = (EditText) findViewById(R.id.edt_phone_value);
			final EditText editTextMobile = (EditText) findViewById(R.id.edt_mobile_value);
			final EditText editTextExtension = (EditText) findViewById(R.id.edt_extension_value);
			final EditText editTextFax = (EditText) findViewById(R.id.edt_fax_value);
			final EditText editTextEmail = (EditText) findViewById(R.id.edt_email_value);
			final EditText editTextMailingStreet = (EditText) findViewById(R.id.edt_mailing_street_value);
			final EditText editTextMailingCity = (EditText) findViewById(R.id.edt_mailing_city_value);
			final EditText editTextMailingStateProvince = (EditText) findViewById(R.id.edt_mailing_province_value);
			final EditText editTextMailingPostalCode = (EditText) findViewById(R.id.edt_mailing_postal_code_value);
			final EditText editTextMailingCountry = (EditText) findViewById(R.id.edt_mailing_country_value);
			final Spinner spinnerGender = (Spinner) findViewById(R.id.spinner_gender_value);
			final Spinner spinnerAccountName = (Spinner)findViewById(R.id.spinner_account_name);
			final Spinner spinnerNationality = (Spinner)findViewById(R.id.spinner_nationality);
			Button saveButton = (Button)findViewById(R.id.button_save);

			editTextFirstName.setVisibility(View.VISIBLE);
			editTextLastName.setVisibility(View.VISIBLE);
			spinnerGender.setVisibility(View.VISIBLE);
			editTextPhone.setVisibility(View.VISIBLE);
			editTextMobile.setVisibility(View.VISIBLE);
			editTextExtension.setVisibility(View.VISIBLE);
			editTextFax.setVisibility(View.VISIBLE);
			editTextEmail.setVisibility(View.VISIBLE);
			editTextMailingStreet.setVisibility(View.VISIBLE);
			editTextMailingCity.setVisibility(View.VISIBLE);
			editTextMailingStateProvince.setVisibility(View.VISIBLE);
			editTextMailingPostalCode.setVisibility(View.VISIBLE);
			editTextMailingCountry.setVisibility(View.VISIBLE);
			spinnerAccountName.setVisibility(View.VISIBLE);
			spinnerNationality.setVisibility(View.VISIBLE);
			saveButton.setVisibility(View.VISIBLE);

			saveButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(contact.getAccountId() == null || contact.getAccountId().isEmpty()){
						dialog.showError(getString(R.string.warning_account_contact), getString(R.string.ok), new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
					}else if(!HolcimUtility.isValidEmail(editTextEmail.getText())){
						dialog.showError(getString(R.string.warning_email_contact), getString(R.string.ok), new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});					
					}else {
                        if (editTextLastName.getText() != null && !String.valueOf(editTextLastName.getText()).equals("")) {
                            contact.setLastName(String.valueOf(editTextLastName.getText()));

                            if (editTextFirstName.getText() != null) {
                                contact.setFirstName(String.valueOf(editTextFirstName.getText()));
                            }

                            if (editTextPhone.getText() != null) {
                                contact.setPhone(String.valueOf(editTextPhone.getText()));
                            }

                            if (editTextMobile.getText() != null) {
                                contact.setMobile(String.valueOf(editTextMobile.getText()));
                            }

                            if (editTextEmail.getText() != null) {
                                contact.setEmail(String.valueOf(editTextEmail.getText()));
                            }

                            if (editTextExtension.getText() != null) {
                                contact.setExtension(String.valueOf(editTextExtension.getText()));
                            }

                            if (editTextFax != null) {
                                contact.setFax(String.valueOf(editTextFax.getText()));
                            }

                            if (editTextMailingStreet != null) {
                                contact.setMailingStreet(String.valueOf(editTextMailingStreet.getText()));
                            }

                            if (editTextMailingCity != null) {
                                contact.setMailingCity(String.valueOf(editTextMailingCity.getText()));
                            }

                            if (editTextMailingStateProvince != null) {
                                contact.setMailingStateProvince(String.valueOf(editTextMailingStateProvince.getText()));
                            }

                            if (editTextMailingPostalCode != null) {
                                contact.setMailingPostalCode(String.valueOf(editTextMailingPostalCode.getText()));
                            }

                            if (editTextMailingCountry != null) {
                                contact.setMailingCountry(String.valueOf(editTextMailingCountry.getText()));
                            }

                            try {
                                if (AltimetrikFileHandler.isFileExist(contact.getTempPictureFilePath(HolcimShowContactInfo.this))) {
                                    if (contact.isPictureFileExist(HolcimShowContactInfo.this)) {
                                        AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(HolcimShowContactInfo.this);
                                        try {
                                            fileHandler.moveFile(contact.getTempPictureFilePath(HolcimShowContactInfo.this), HolcimDataSource.getContactDir(HolcimShowContactInfo.this) + File.separator + contact.getPictureFileName());
                                            contact.setPicturemd5(HolcimDataSource.getContactDir(HolcimShowContactInfo.this) + File.separator + contact.getTempPictureFileName(HolcimShowContactInfo.this));
                                        } catch (Exception e) {
                                        }
                                    } else {
                                        try {
                                            AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(HolcimShowContactInfo.this);
                                            fileHandler.moveFile(contact.getTempPictureFilePath(HolcimShowContactInfo.this), HolcimDataSource.getContactDir(HolcimShowContactInfo.this) + File.separator + contact.getPictureFileName());
                                            AltimetrikFileHandler.renameFile(HolcimDataSource.getContactDir(HolcimShowContactInfo.this) + File.separator + contact.getTempPictureFileName(HolcimShowContactInfo.this), HolcimDataSource.getContactDir(HolcimShowContactInfo.this) + File.separator + contact.getPictureFileName());
                                            contact.setPicturemd5(HolcimDataSource.getContactDir(HolcimShowContactInfo.this) + File.separator + contact.getTempPictureFileName(HolcimShowContactInfo.this));
                                        } catch (Exception e) {
                                        }
                                    }
                                } else {
                                    contact.setPicturemd5(contact.getPicturePath(HolcimShowContactInfo.this));
                                }
                            } catch (HolcimException e) {
                                e.printStackTrace();
                            }
                            contact.setIsEdited(true);
                            HolcimApp.daoSession.update(contact);
                            Intent intent = new Intent(HolcimShowContactInfo.this, HolcimContactActivity.class);
                            startActivity(intent);

                        } else {
                            dialog.showError(getString(R.string.warning_last_name_contact), getString(R.string.ok), new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }

				}
			});

			final List<String> accountList = new ArrayList<String>();
			accountList.add("");
			for(Account account: HolcimApp.daoSession.getAccountDao().loadAll()){
				accountList.add(account.getName());
			}

			Collections.sort(accountList, String.CASE_INSENSITIVE_ORDER);
			ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(this, R.layout.spinner, accountList);
			spinnerAccountName.setAdapter(accountAdapter);
			spinnerAccountName.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					if(spinnerAccountName.getAdapter() != null  && spinnerAccountName.getAdapter().getItem(position) != null
							&& !accountList.get(position).equals("")){
						List<Account> acc = HolcimApp.daoSession.getAccountDao().queryBuilder()
								.where(AccountDao.Properties.Name.eq(accountList.get(position))).list();
						contact.setAccountName(accountList.get(position));
						contact.setAccountId(acc.get(0).getSalesforceId());

					}else if(accountList.get(position).equals("")){
						contact.setAccountName("");
						contact.setAccountId("");
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			final List<String> nationallityList = Arrays.asList(getResources().getStringArray(R.array.nationality));
			ArrayAdapter<String> NationallityAdapter = new ArrayAdapter<String>(this, R.layout.spinner, nationallityList);
			spinnerNationality.setAdapter(NationallityAdapter);
			spinnerNationality.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					if(spinnerNationality.getAdapter() != null && spinnerNationality.getAdapter().getItem(position) != null
							&& !nationallityList.get(position).equals("")){
						contact.setNationality(nationallityList.get(position));
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			final List<String> genderList = Arrays.asList(getResources().getStringArray(R.array.gender));
			ArrayAdapter<String> GenderAdapter = new ArrayAdapter<String>(this, R.layout.spinner, genderList);
			spinnerGender.setAdapter(GenderAdapter);
			spinnerGender.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					if(spinnerGender.getAdapter() != null && spinnerGender.getAdapter().getItem(position) != null
							&& !genderList.get(position).equals("")){
						contact.setGender(genderList.get(position));
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});			
			
			if(contact != null){
				if(contact.getFirstName() != null){
					editTextFirstName.setText(contact.getFirstName());
				}

				if(contact.getLastName() != null){
					editTextLastName.setText(contact.getLastName());
				}

				if(contact.getPhone() != null){
					editTextPhone.setText(contact.getPhone());
				}

				if(contact.getMobile() != null){
					editTextMobile.setText(contact.getMobile());
				}

				if(contact.getExtension() != null){
					editTextExtension.setText(contact.getExtension());
				}

				if(contact.getFax() != null){
					editTextFax.setText(contact.getFax());
				}

				if(contact.getEmail() != null){
					editTextEmail.setText(contact.getEmail());
				}

				if(contact.getMailingStreet() != null){
					editTextMailingStreet.setText(contact.getMailingStreet());
				}

				if(contact.getMailingCity() != null){
					editTextMailingCity.setText(contact.getMailingCity());
				}

				if(contact.getMailingStateProvince() != null){
					editTextMailingStateProvince.setText(contact.getMailingStateProvince());
				}

				if(contact.getMailingPostalCode() != null){
					editTextMailingPostalCode.setText(contact.getMailingPostalCode());
				}
				if(contact.getMailingCountry() != null){
					editTextMailingCountry.setText(contact.getMailingCountry());
				}

				if(contact.getAccountName() != null){
					spinnerAccountName.setSelection(accountList.indexOf(contact.getAccountName()));
				}
				
				if(contact.getGender() != null){
					spinnerGender.setSelection(genderList.indexOf(contact.getGender()));
				}
				
				if(contact.getNationality() != null){
					spinnerNationality.setSelection(nationallityList.indexOf(contact.getNationality()));
				}else{
					spinnerNationality.setSelection(nationallityList.indexOf(HolcimConsts.DEFAULT_NATIONALITY));
				}
			}
		}
		ImageButton back = (ImageButton) findViewById(R.id.imageButton_back);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(contact != null && contact.getSalesforceId() == null){
					dialog.showWarning(getString(R.string.message_warning_go_home), getString(R.string.ok), getString(R.string.cancel), new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							Intent intent = new Intent(HolcimShowContactInfo.this, HolcimContactActivity.class);
							startActivity(intent);
						}
					}, dialog.mDismissClickListener);
				}else{
					Intent intent = new Intent(HolcimShowContactInfo.this, HolcimContactActivity.class);
					startActivity(intent);
				}
			}
		});

		camera				= (ImageButton)findViewById(R.id.imageButton_camera);
		refreshImage();

		setCustomTitle(contact.getFirstName() + " " + contact.getLastName());

		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				takePhoto();
			}
		});
	}

	public void refreshImage() {
		try {
			if (contact.isPictureTempFileExist(this)) {
				camera.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(contact.getTempPictureFilePath(this), 200, 200, 0));
			} else if (contact.getId() != null && contact.isPictureFileExist(this)) {
				camera.setImageBitmap(HolcimUtility.decodeSampledBitmapFromFile(contact.getPicturePath(this), 200, 200, 0));
			} else if(contact.getSalesforceId() != null){
				camera.setImageResource(R.drawable.contact_picture);
			}else{
				camera.setImageResource(R.drawable.camera);
			}
		} catch (HolcimException e) {
			e.printStackTrace();
		}
	}

	public void takePhoto() {
		Intent mIntent = new Intent(this, HolcimCameraActivity.class);
        mIntent.putExtra("isContactPicture", true);
		startActivityForResult(mIntent, HolcimConsts.ACTIVITY_REQUEST_CAMERA_LOG);
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
		if(!HolcimCustomActivity.blockback){
			if(contact != null && contact.getSalesforceId() == null){
				dialog.showWarning(getString(R.string.message_warning_go_home), getString(R.string.ok), getString(R.string.cancel), new OnClickListener() {
	
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						Intent intent = new Intent(HolcimShowContactInfo.this, HolcimContactActivity.class);
						startActivity(intent);
						HolcimCustomActivity.setOnback(true);
						try {
							AltimetrikFileHandler.DeleteFile(contact.getTempPictureFilePath(HolcimShowContactInfo.this));
						} catch (AltimetrikException e) {
							e.printStackTrace();
						} catch (HolcimException e) {
							e.printStackTrace();
						}
					}
				}, dialog.mDismissClickListener);
			}else{
				super.onBackPressed();
				HolcimCustomActivity.setOnback(true);
				try {
					AltimetrikFileHandler.DeleteFile(contact.getTempPictureFilePath(this));
				} catch (AltimetrikException e) {
					e.printStackTrace();
				} catch (HolcimException e) {
					e.printStackTrace();
				}
			}
		}	
	}
}
