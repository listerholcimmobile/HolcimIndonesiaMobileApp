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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.google.android.gms.maps.model.LatLng;
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

public class FragmentAccountInfo extends Fragment {

	// Declaration
	private SaleExecution saleExecution;
	private ArrayList<CompetitorMarketing> comeptitorsMarketings;
	private ArrayList<ActionsLog> actionLogs;
	private ArrayList<PreOrder> preOrders;
	private ImageButton camera;
	EditText bankAccountBranch1;
	EditText bankAccountBranch2;
	EditText bankAccountBranch3;
	EditText bankAccountName1;
	EditText bankAccountName2;
	EditText bankAccountName3;
	EditText bankAccountNumber1;
	EditText bankAccountNumber2;
	EditText bankAccountNumber3;
	EditText deliveryRemark;
	EditText eightTTruckArmada;
	EditText email1;
	EditText extension1;
	EditText fax1;
    TextView acclatitude;
    TextView acclongitude;
	EditText latitude;
	EditText longitude;
	EditText mobile1;
	EditText mobile2;
	EditText numberOfPermanentEmployees;
	EditText phone1;
	EditText phone2;
	EditText pickUp2TArmada;
	EditText postalCode;
	EditText retailerCreditLimit;
	EditText startingYear;
	EditText streetAddress;
	EditText twentyFourTTruckArmada;
	EditText landmarkDescription;
	EditText ownerContactNumber;
	EditText firstName;
	EditText lastName;
	TextView title;
	EditText pro_title;
	EditText edtReasonForUnsatisfiedExperience;
	EditText editTextBirthdate;

	private FragmentAccountInfoActions fragmentAccountInfoActions;

	public interface FragmentAccountInfoActions {
		public void goBackFromAccountInfo();

		public void goToContactInfo(SaleExecution saleExecution,
				ArrayList<CompetitorMarketing> comeptitorsMarketings,
				ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders);

		public void getLocation();

		public void deleteActionLogs();

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
			fragmentAccountInfoActions = (FragmentAccountInfoActions) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement fragmentAccountInfoActions");
		}
	}

	public FragmentAccountInfo(SaleExecution saleExecution,
			ArrayList<CompetitorMarketing> comeptitorsMarketings,
			ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
		this.saleExecution = saleExecution;
		this.comeptitorsMarketings = comeptitorsMarketings;
		this.actionLogs = actionLogs;
		this.preOrders = preOrders;
	}

	public FragmentAccountInfo() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = null;

		if (this.saleExecution.getProspectId() == 0) {

			rootView = inflater
					.inflate(R.layout.account_info, container, false);

			title = (TextView) rootView
					.findViewById(R.id.textView_fragment_title);
			title.setText(R.string.account_info_title);

			camera = (ImageButton) rootView
					.findViewById(R.id.imageButton_landmark_picture);

			landmarkDescription = (EditText) rootView
					.findViewById(R.id.edt_landmark_description);
			final TextView accountType = (TextView) rootView
					.findViewById(R.id.edt_accountType);
			bankAccountBranch1 = (EditText) rootView
					.findViewById(R.id.edt_bankAccountBranch1);
			bankAccountBranch2 = (EditText) rootView
					.findViewById(R.id.edt_bankAccountBranch2);
			bankAccountBranch3 = (EditText) rootView
					.findViewById(R.id.edt_bankAccountBranch3);
			bankAccountName1 = (EditText) rootView
					.findViewById(R.id.edt_bankAccountName1);
			bankAccountName2 = (EditText) rootView
					.findViewById(R.id.edt_bankAccountName2);
			bankAccountName3 = (EditText) rootView
					.findViewById(R.id.edt_bankAccountName3);
			bankAccountNumber1 = (EditText) rootView
					.findViewById(R.id.edt_bankAccountNumber1);
			bankAccountNumber2 = (EditText) rootView
					.findViewById(R.id.edt_bankAccountNumber2);
			bankAccountNumber3 = (EditText) rootView
					.findViewById(R.id.edt_bankAccountNumber3);
			deliveryRemark = (EditText) rootView
					.findViewById(R.id.edt_deliveryRemark);
			final TextView district = (TextView) rootView
					.findViewById(R.id.edt_district);
			eightTTruckArmada = (EditText) rootView
					.findViewById(R.id.edt_eightTTruckArmada);
			email1 = (EditText) rootView.findViewById(R.id.edt_email1);
			extension1 = (EditText) rootView.findViewById(R.id.edt_extension1);
			fax1 = (EditText) rootView.findViewById(R.id.edt_fax1);
			final TextView holcimeterBalance = (TextView) rootView
					.findViewById(R.id.edt_holcimeterBalance);
			final TextView holcimeterBonus = (TextView) rootView
					.findViewById(R.id.edt_holcimeterBonus);
			final TextView holcimeterTotal = (TextView) rootView
					.findViewById(R.id.edt_holcimeterTotal);

			// Checkbox
			final CheckBox jelajahHolcimMembership = (CheckBox) rootView
					.findViewById(R.id.checkBox_jelajahHolcimMembership);
			final CheckBox badDebt = (CheckBox) rootView
					.findViewById(R.id.checkBox_bad_debt);
			final TextView kecamatan = (TextView) rootView
					.findViewById(R.id.edt_kecamatan);
			final CheckBox keyRetailer = (CheckBox) rootView
					.findViewById(R.id.checkBox_keyRetailer);
			final TextView kota = (TextView) rootView
					.findViewById(R.id.edt_kota);
			// ------------------

			final Spinner landStatus = (Spinner) rootView
					.findViewById(R.id.spinner_landStatus);
			final TextView lastRewardRedeemed = (TextView) rootView
					.findViewById(R.id.edt_lastRewardRedeemed);
			latitude = (EditText) rootView.findViewById(R.id.edt_latitude);
			longitude = (EditText) rootView.findViewById(R.id.edt_longitude);
            acclatitude = (TextView) rootView.findViewById(R.id.edt_accountLatitude);
            acclongitude = (TextView) rootView.findViewById(R.id.edt_accountLongitude);
			mobile1 = (EditText) rootView.findViewById(R.id.edt_mobile1);
			mobile2 = (EditText) rootView.findViewById(R.id.edt_mobile2);
			numberOfPermanentEmployees = (EditText) rootView
					.findViewById(R.id.edt_numberOfPermanentEmployees);
			phone1 = (EditText) rootView.findViewById(R.id.edt_phone1);
			phone2 = (EditText) rootView.findViewById(R.id.edt_phone2);
			pickUp2TArmada = (EditText) rootView
					.findViewById(R.id.edt_pickUp2TArmada);
			postalCode = (EditText) rootView.findViewById(R.id.edt_postalCode);
			final TextView province = (TextView) rootView
					.findViewById(R.id.edt_province);
			retailerCreditLimit = (EditText) rootView
					.findViewById(R.id.edt_retailerCreditLimit);
			final TextView retailerId = (TextView) rootView
					.findViewById(R.id.edt_retailerId);
			final TextView retailerStatus = (TextView) rootView
					.findViewById(R.id.spinner_retailerStatus);
			final Spinner retailerTermOfPayment = (Spinner) rootView
					.findViewById(R.id.spinner_retailerTermOfPayment);
			startingYear = (EditText) rootView
					.findViewById(R.id.edt_startingYear);
			streetAddress = (EditText) rootView
					.findViewById(R.id.edt_streetAddress);
			final TextView tierKLAB = (TextView) rootView
					.findViewById(R.id.edt_tierKLAB);
			final TextView tierKLABHistory = (TextView) rootView
					.findViewById(R.id.edt_tierKLABHistory);
			twentyFourTTruckArmada = (EditText) rootView
					.findViewById(R.id.edt_twentyFourTTruckArmada);
			final TextView x1stDesiredReward = (TextView) rootView
					.findViewById(R.id.edt_x1stDesiredReward);
			final TextView x1stDesiredRewardPoints = (TextView) rootView
					.findViewById(R.id.edt_x1stDesiredRewardPoints);

			final TextView salesTargCurr = (TextView) rootView
					.findViewById(R.id.edt_sales_target_curr);
			final TextView salesTarg = (TextView) rootView
					.findViewById(R.id.edt_sales_target);
			final TextView salesCap = (TextView) rootView
					.findViewById(R.id.edt_sales_cap);
			final TextView salesSow = (TextView) rootView
					.findViewById(R.id.edt_sales_sow);
			final TextView salesActual = (TextView) rootView
					.findViewById(R.id.edt_sales_actual);
			final TextView salesOfficer = (TextView) rootView
					.findViewById(R.id.edt_salesOfficer);
			final TextView salesManInCharge = (TextView) rootView
					.findViewById(R.id.edt_salesManInCharge);
            final TextView totalDispatchVolLastMonth = (TextView) rootView
                    .findViewById(R.id.edt_totalDispatchVolLastMonth);


			final TextView retailerName = (TextView) rootView
					.findViewById(R.id.edt_retailerName);
			final TextView accountRecordType = (TextView) rootView
					.findViewById(R.id.edt_accountRecordType);
			final TextView jelajahHolcimID = (TextView) rootView
					.findViewById(R.id.edt_JelajahHolcimID);

			TextView txtStreetAddress = (TextView) rootView
					.findViewById(R.id.txt_streetAddress);
			TextView txtKelurahan = (TextView) rootView
					.findViewById(R.id.txt_kelurahan);
			TextView txtPostalCode = (TextView) rootView
					.findViewById(R.id.txt_postalCode);
			final TextView txtLatitude = (TextView) rootView
					.findViewById(R.id.txt_latitude);
			final TextView txtLongitude = (TextView) rootView
					.findViewById(R.id.txt_longitude);
			TextView txtLandmarkDescription = (TextView) rootView
					.findViewById(R.id.txt_landmark_description);
			TextView txtPhone1 = (TextView) rootView
					.findViewById(R.id.txt_phone1);
			TextView txtPhone2 = (TextView) rootView
					.findViewById(R.id.txt_phone2);
			TextView txtExtension1 = (TextView) rootView
					.findViewById(R.id.txt_extension1);
			TextView txtMobile1 = (TextView) rootView
					.findViewById(R.id.txt_mobile1);
			TextView txtMobile2 = (TextView) rootView
					.findViewById(R.id.txt_mobile2);
			TextView txtFax1 = (TextView) rootView.findViewById(R.id.txt_fax1);
			TextView txtEmail1 = (TextView) rootView
					.findViewById(R.id.txt_email1);
			TextView txtBankAccountName1 = (TextView) rootView
					.findViewById(R.id.txt_bankAccountName1);
			TextView txtBankAccountBranch1 = (TextView) rootView
					.findViewById(R.id.txt_bankAccountBranch1);
			TextView txtBankAccountNumber1 = (TextView) rootView
					.findViewById(R.id.txt_bankAccountNumber1);
			TextView txtBankAccountName2 = (TextView) rootView
					.findViewById(R.id.txt_bankAccountName2);
			TextView txtBankAccountBranch2 = (TextView) rootView
					.findViewById(R.id.txt_bankAccountBranch2);
			TextView txtBankAccountNumber2 = (TextView) rootView
					.findViewById(R.id.txt_bankAccountNumber2);
			TextView txtBankAccountName3 = (TextView) rootView
					.findViewById(R.id.txt_bankAccountName3);
			TextView txtBankAccountBranch3 = (TextView) rootView
					.findViewById(R.id.txt_bankAccountBranch3);
			TextView txtBankAccountNumber3 = (TextView) rootView
					.findViewById(R.id.txt_bankAccountNumber3);
			TextView txtRetailerTermsOfPayment = (TextView) rootView
					.findViewById(R.id.txt_retailerTermOfPayment);
			TextView txtRetailerCreditLimit = (TextView) rootView
					.findViewById(R.id.txt_retailerCreditLimit);
			TextView txtStartingYear = (TextView) rootView
					.findViewById(R.id.txt_startingYear);
			TextView txtNumberOfPermanentEmployees = (TextView) rootView
					.findViewById(R.id.txt_numberOfPermanentEmployees);
			TextView txtLandStatus = (TextView) rootView
					.findViewById(R.id.txt_landStatus);
			TextView txtEigthTruckArmada = (TextView) rootView
					.findViewById(R.id.txt_eightTTruckArmada);
			TextView txtPickUpTwoTArmada = (TextView) rootView
					.findViewById(R.id.txt_pickUp2TArmada);
			TextView txtTwenyFourTTruckArmada = (TextView) rootView
					.findViewById(R.id.txt_twentyFourTTruckArmada);
			TextView txtDeliveryRemark = (TextView) rootView
					.findViewById(R.id.txt_deliveryRemark);

			// setText
			if (this.saleExecution.getRetailerName() != null) {
				retailerName.setText(this.saleExecution.getRetailerName());
			}
			if (this.saleExecution.getAccountRecordType() != null) {
				accountRecordType.setText(this.saleExecution
						.getAccountRecordType());
			}
			if (this.saleExecution.getJelajahHolcimID() != null) {
				jelajahHolcimID
						.setText(this.saleExecution.getJelajahHolcimID());
			}
			if (this.saleExecution.getLandmarkDescription() != null) {
				landmarkDescription.setText(this.saleExecution
						.getLandmarkDescription());
				txtLandmarkDescription.setText(this.saleExecution
						.getLandmarkDescription());
			}

			// setText SaleTarget
			String sSalesTargCurr = (this.saleExecution
					.getSalesTargetCurrentMonth() != null) ? String
					.valueOf(this.saleExecution.getSalesTargetCurrentMonth())
					: "";
			salesTargCurr.setText(sSalesTargCurr);

			String sSalesTarg = (this.saleExecution.getSalesTargetMTD() != null) ? String
					.valueOf(this.saleExecution.getSalesTargetMTD()) : "";
			salesTarg.setText(sSalesTarg);

			String sSalesCap = (this.saleExecution.getCapacityCurrentMonth() != null) ? String
					.valueOf(this.saleExecution.getCapacityCurrentMonth()) : "";
			salesCap.setText(sSalesCap);

			String sSalesSow = (this.saleExecution.getHilSoWCurrentMonth() != null) ? String
					.valueOf(this.saleExecution.getHilSoWCurrentMonth()) : "";
			salesSow.setText(sSalesSow);

			String sSalesActual = (this.saleExecution.getSalesActualMTD() != null) ? String
					.valueOf(this.saleExecution.getSalesActualMTD()) : "";
			salesActual.setText(sSalesActual);
			// ------------------------

			String saccountType = (this.saleExecution.getAccountType() != null) ? this.saleExecution
					.getAccountType() : "";
			accountType.setText(saccountType);
			String sbankAccountBranch1 = (this.saleExecution
					.getBankAccountBranch1() != null) ? this.saleExecution
					.getBankAccountBranch1() : "";
			bankAccountBranch1.setText(sbankAccountBranch1);
			txtBankAccountBranch1.setText(sbankAccountBranch1);

			String sbankAccountBranch2 = (this.saleExecution
					.getBankAccountBranch2() != null) ? this.saleExecution
					.getBankAccountBranch2() : "";
			bankAccountBranch2.setText(sbankAccountBranch2);
			txtBankAccountBranch2.setText(sbankAccountBranch2);

			String sbankAccountBranch3 = (this.saleExecution
					.getBankAccountBranch3() != null) ? this.saleExecution
					.getBankAccountBranch3() : "";
			bankAccountBranch3.setText(sbankAccountBranch3);
			txtBankAccountBranch3.setText(sbankAccountBranch3);
			
			String sbankAccountName1 = (this.saleExecution
					.getBankAccountName1() != null) ? this.saleExecution
					.getBankAccountName1() : "";
			bankAccountName1.setText(sbankAccountName1);
			txtBankAccountName1.setText(sbankAccountName1);
			
			String sbankAccountName2 = (this.saleExecution
					.getBankAccountName2() != null) ? this.saleExecution
					.getBankAccountName2() : "";
			bankAccountName2.setText(sbankAccountName2);
			txtBankAccountName2.setText(sbankAccountName2);
			
			String sbankAccountName3 = (this.saleExecution
					.getBankAccountName3() != null) ? this.saleExecution
					.getBankAccountName3() : "";
			bankAccountName3.setText(sbankAccountName3);
			txtBankAccountName3.setText(sbankAccountName3);
			
			String sbankAccountNumber1 = (this.saleExecution
					.getBankAccountNumber1() != null) ? this.saleExecution
					.getBankAccountNumber1() : "";
			bankAccountNumber1.setText(sbankAccountNumber1);
			txtBankAccountNumber1.setText(sbankAccountNumber1);
			
			String sbankAccountNumber2 = (this.saleExecution
					.getBankAccountNumber2() != null) ? this.saleExecution
					.getBankAccountNumber2() : "";
			bankAccountNumber2.setText(sbankAccountNumber2);
			txtBankAccountNumber2.setText(sbankAccountNumber2);
			
			String sbankAccountNumber3 = (this.saleExecution
					.getBankAccountNumber3() != null) ? this.saleExecution
					.getBankAccountNumber3() : "";
			bankAccountNumber3.setText(sbankAccountNumber3);
			txtBankAccountNumber3.setText(sbankAccountNumber3);
			
			String sdeliveryRemark = (this.saleExecution.getDeliveryRemark() != null) ? this.saleExecution
					.getDeliveryRemark() : "";
			deliveryRemark.setText(sdeliveryRemark);
			txtDeliveryRemark.setText(sdeliveryRemark);
			
			String sdistrict = (this.saleExecution.getDistrict() != null) ? this.saleExecution
					.getDistrict() : "";
			district.setText(sdistrict);
			
			String seightTTruckArmada = (this.saleExecution
					.getEightTTruckArmada() != null) ? this.saleExecution
					.getEightTTruckArmada() : "";
			eightTTruckArmada.setText(seightTTruckArmada);
			txtEigthTruckArmada.setText(seightTTruckArmada);
			
			String semail1 = (this.saleExecution.getEmail1() != null) ? this.saleExecution
					.getEmail1() : "";
			email1.setText(semail1);
			txtEmail1.setText(semail1);
			
			String sextension1 = (this.saleExecution.getExtension1() != null) ? this.saleExecution
					.getExtension1() : "";
			extension1.setText(sextension1);
			txtExtension1.setText(sextension1);
			
			String sfax1 = (this.saleExecution.getFax1() != null) ? this.saleExecution
					.getFax1() : "";
			fax1.setText(sfax1);
			txtFax1.setText(sfax1);
			
			String sholcimeterBalance = (this.saleExecution
					.getHolcimeterBalance() != null) ? this.saleExecution
					.getHolcimeterBalance().toString() : "";
			holcimeterBalance.setText(sholcimeterBalance);
			
			String sholcimeterBonus = (this.saleExecution.getHolcimeterBonus() != null) ? this.saleExecution
					.getHolcimeterBonus().toString() : "";
			holcimeterBonus.setText(sholcimeterBonus);
			
			String sholcimeterTotal = (this.saleExecution.getHolcimeterTotal() != null) ? this.saleExecution
					.getHolcimeterTotal().toString() : "";
			holcimeterTotal.setText(sholcimeterTotal);
			
			String skecamatan = (this.saleExecution.getKecamatan() != null) ? this.saleExecution
					.getKecamatan() : "";
			kecamatan.setText(skecamatan);
			
			String skelurahan = (this.saleExecution.getKelurahan() != null) ? this.saleExecution
					.getKelurahan() : "";
			txtKelurahan.setText(skelurahan);

			//setCheckBox
			if (this.saleExecution.getKeyRetailer() != null) {
				keyRetailer.setChecked(this.saleExecution.getKeyRetailer());
			}
			keyRetailer.setEnabled(false);

			if (this.saleExecution.getJelajahHolcimMembership() != null) {
				jelajahHolcimMembership.setChecked(this.saleExecution
						.getJelajahHolcimMembership());
			}
			jelajahHolcimMembership.setEnabled(false);

			if (this.saleExecution.getBadDebt() != null) {
				badDebt.setChecked(this.saleExecution.getBadDebt());
			}
			badDebt.setEnabled(false);
			//---------------
			
			if (this.saleExecution.getSalesOfficer() != null) {
				salesOfficer.setText(this.saleExecution.getSalesOfficer());
			}
            this.saleExecution.getAccount();
			if (this.saleExecution.getSalesmanInCharge() != null) {
				salesManInCharge.setText(this.saleExecution
						.getSalesmanInCharge());
			}

            if (this.saleExecution.getAccount() != null && this.saleExecution
                    .getAccount().getLmbuyingvol() != null) {
                totalDispatchVolLastMonth.setText(String.valueOf(this.saleExecution
                        .getAccount().getLmbuyingvol()));
            }


            String skota = (this.saleExecution.getKota() != null) ? this.saleExecution
					.getKota() : "";
			kota.setText(skota);

			//setDropdown
			final List<String> landStatusList = Arrays.asList(getResources()
					.getStringArray(R.array.prospect_land_status));
			ArrayAdapter<String> adapterLandStatus = new ArrayAdapter<String>(
					getActivity(), R.layout.spinner, landStatusList);
			landStatus.setAdapter(adapterLandStatus);
			landStatus.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					if (landStatus.getAdapter() != null
							&& landStatus.getAdapter().getItem(position) != null) {
						saleExecution.setLandStatus(landStatusList
								.get(position));
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			if (this.saleExecution.getLandStatus() != null) {
				landStatus.setSelection(landStatusList
						.indexOf(this.saleExecution.getLandStatus()));
				txtLandStatus.setText(this.saleExecution.getLandStatus());
			}

			String slastRewardRedeemed = (this.saleExecution
					.getLastRewardRedeemed() != null) ? this.saleExecution
					.getLastRewardRedeemed() : "";
			lastRewardRedeemed.setText(slastRewardRedeemed);
			String slatitude = (this.saleExecution.getLatitude() != null) ? this.saleExecution
					.getLatitude() : "";
			latitude.setText(slatitude);
			txtLatitude.setText(slatitude);
			String slongitude = (this.saleExecution.getLongitude() != null) ? this.saleExecution
					.getLongitude() : "";
			longitude.setText(slongitude);
			txtLongitude.setText(slongitude);
			String smobile1 = (this.saleExecution.getMobile1() != null) ? this.saleExecution
					.getMobile1() : "";
			mobile1.setText(smobile1);
			txtMobile1.setText(smobile1);
			String smobile2 = (this.saleExecution.getMobile2() != null) ? this.saleExecution
					.getMobile2() : "";
			mobile2.setText(smobile2);
			txtMobile2.setText(smobile2);
			String snumberOfPermanentEmployees = (this.saleExecution
					.getNumberOfPermanentEmployees() != null) ? this.saleExecution
					.getNumberOfPermanentEmployees().toString() : "";
			numberOfPermanentEmployees.setText(snumberOfPermanentEmployees);
			txtNumberOfPermanentEmployees.setText(snumberOfPermanentEmployees);
			String sphone1 = (this.saleExecution.getPhone1() != null) ? this.saleExecution
					.getPhone1() : "";
			phone1.setText(sphone1);
			txtPhone1.setText(sphone1);
			String sphone2 = (this.saleExecution.getPhone2() != null) ? this.saleExecution
					.getPhone2() : "";
			phone2.setText(sphone2);
			txtPhone2.setText(sphone2);
			String spickUp2TArmada = (this.saleExecution.getPickUp2TArmada() != null) ? this.saleExecution
					.getPickUp2TArmada() : "";
			pickUp2TArmada.setText(spickUp2TArmada);
			txtPickUpTwoTArmada.setText(spickUp2TArmada);
			String spostalCode = (this.saleExecution.getPostalCode() != null) ? this.saleExecution
					.getPostalCode() : "";
			postalCode.setText(spostalCode);
			txtPostalCode.setText(spostalCode);
			String sprovince = (this.saleExecution.getProvince() != null) ? this.saleExecution
					.getProvince() : "";
			province.setText(sprovince);
			String sretailerCreditLimit = (this.saleExecution
					.getRetailerCreditLimit() != null) ? this.saleExecution
					.getRetailerCreditLimit().toString() : "";
			retailerCreditLimit.setText(sretailerCreditLimit);
			txtRetailerCreditLimit.setText(sretailerCreditLimit);
			String sretailerId = (this.saleExecution.getRetailerId() != null) ? this.saleExecution
					.getRetailerId() : "";
			retailerId.setText(sretailerId);
            String accLat = (this.saleExecution.getAcclatitude() != null) ?this.saleExecution.getAcclatitude() : "";
            acclatitude.setText(accLat);
            String accLong = (this.saleExecution.getAcclongitude() != null) ?this.saleExecution.getAcclongitude() : "";
            acclongitude.setText(accLong);

			if (this.saleExecution.getRetailerStatus() != null) {
				retailerStatus.setText(this.saleExecution.getRetailerStatus());
			}

			final List<String> retailerTermOfPaymentList = Arrays
					.asList(getResources().getStringArray(
							R.array.prospect_retailer_terms_of_payment));
			ArrayAdapter<String> retailerTermOfPaymentAdapter = new ArrayAdapter<String>(
					getActivity(), R.layout.spinner, retailerTermOfPaymentList);
			retailerTermOfPayment.setAdapter(retailerTermOfPaymentAdapter);

			retailerTermOfPayment
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							if (retailerTermOfPayment.getAdapter() != null
									&& retailerTermOfPayment.getAdapter()
											.getItem(position) != null) {
								saleExecution
										.setRetailerTermOfPayment(retailerTermOfPaymentList
												.get(position));
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						}
					});

			if (this.saleExecution.getRetailerTermOfPayment() != null) {
				retailerTermOfPayment
						.setSelection(retailerTermOfPaymentList
								.indexOf(this.saleExecution
										.getRetailerTermOfPayment()));
				txtRetailerTermsOfPayment.setText(this.saleExecution
						.getRetailerTermOfPayment());
			}

			String sstartingYear = (this.saleExecution.getStartingYear() != null) ? this.saleExecution
					.getStartingYear().toString() : "";
			startingYear.setText(sstartingYear);
			txtStartingYear.setText(sstartingYear);
			String sstreetAddress = (this.saleExecution.getStreetAddress() != null) ? this.saleExecution
					.getStreetAddress() : "";
			streetAddress.setText(sstreetAddress);
			txtStreetAddress.setText(sstartingYear);
			String stierKLAB = (this.saleExecution.getTierKLAB() != null) ? this.saleExecution
					.getTierKLAB() : "";
			tierKLAB.setText(stierKLAB);
			String stierKLABHistory = (this.saleExecution.getTierKLABHistory() != null) ? this.saleExecution
					.getTierKLABHistory() : "";
			tierKLABHistory.setText(stierKLABHistory);
			String stwentyFourTTruckArmada = (this.saleExecution
					.getTwentyFourTTruckArmada() != null) ? this.saleExecution
					.getTwentyFourTTruckArmada() : "";
			twentyFourTTruckArmada.setText(stwentyFourTTruckArmada);
			txtTwenyFourTTruckArmada.setText(stwentyFourTTruckArmada);
			String sx1stDesiredReward = (this.saleExecution
					.getX1stDesiredReward() != null) ? this.saleExecution
					.getX1stDesiredReward() : "";
			x1stDesiredReward.setText(sx1stDesiredReward);
			String sx1stDesiredRewardPoints = (this.saleExecution
					.getX1stDesiredRewardPoints() != null) ? this.saleExecution
					.getX1stDesiredRewardPoints().toString() : "";
			x1stDesiredRewardPoints.setText(sx1stDesiredRewardPoints);

			Button btnChekin = (Button) rootView.findViewById(R.id.btn_checkin);
			btnChekin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					fragmentAccountInfoActions.getLocation();
					LatLng currentLocation = ((HolcimSelectedRetailerActivity) getActivity())
							.getCurrentLocation();
                    if(currentLocation != null) {
                        latitude.setText(String.valueOf(currentLocation.latitude));
                        txtLatitude.setText(String
                                .valueOf(currentLocation.latitude));
                        longitude.setText(String.valueOf(currentLocation.longitude));
                        txtLongitude.setText(String
                                .valueOf(currentLocation.longitude));
                    }
				}
			});

			if (!((HolcimSelectedRetailerActivity) getActivity())
					.isCanEditByDate()) {
				btnChekin.setVisibility(View.INVISIBLE);
			}

			final DatePicker datepickerLastDispatchDate = (DatePicker) rootView
					.findViewById(R.id.datePicker_lastDispatchDate);
			datepickerLastDispatchDate.setEnabled(false);
			if (saleExecution.getLastDispatchDate() != null) {
				String date = saleExecution.getLastDispatchDate();
				String[] separated = date.split("-");
				datepickerLastDispatchDate.updateDate(
						Integer.parseInt(separated[0]),
						Integer.parseInt(separated[1]) - 1,
						Integer.parseInt(separated[2]));
				datepickerLastDispatchDate.init(Integer.parseInt(separated[0]),
						Integer.parseInt(separated[1]) - 1,
						Integer.parseInt(separated[2]),
						new OnDateChangedListener() {

							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								saleExecution.setLastDispatchDate(datepickerLastDispatchDate
										.getYear()
										+ "-"
										+ datepickerLastDispatchDate.getMonth()
										+ "-"
										+ datepickerLastDispatchDate
												.getDayOfMonth());
							}
						});
			} else {
				Calendar c = Calendar.getInstance();
				datepickerLastDispatchDate.init(c.get(Calendar.YEAR),
						c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
						new OnDateChangedListener() {

							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								saleExecution.setLastDispatchDate(datepickerLastDispatchDate
										.getYear()
										+ "-"
										+ datepickerLastDispatchDate.getMonth()
										+ "-"
										+ datepickerLastDispatchDate
												.getDayOfMonth());
							}
						});
			}

			final DatePicker datePickerLastPlannedVisitDate = (DatePicker) rootView
					.findViewById(R.id.datePicker_lastPlannedVisitDate);
			datePickerLastPlannedVisitDate.setEnabled(false);
			if (saleExecution.getLastPlannedVisitDate() != null) {
				String date = saleExecution.getLastPlannedVisitDate();
				String[] separated = date.split("-");
				datePickerLastPlannedVisitDate.updateDate(
						Integer.parseInt(separated[0]),
						Integer.parseInt(separated[1]) - 1,
						Integer.parseInt(separated[2]));
				datePickerLastPlannedVisitDate.init(
						Integer.parseInt(separated[0]),
						Integer.parseInt(separated[1]) - 1,
						Integer.parseInt(separated[2]),
						new OnDateChangedListener() {

							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								saleExecution.setLastPlannedVisitDate(datePickerLastPlannedVisitDate
										.getYear()
										+ "-"
										+ datePickerLastPlannedVisitDate
												.getMonth()
										+ "-"
										+ datePickerLastPlannedVisitDate
												.getDayOfMonth());
							}
						});
			} else {
				Calendar c = Calendar.getInstance();
				datePickerLastPlannedVisitDate.init(c.get(Calendar.YEAR),
						c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
						new OnDateChangedListener() {

							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								saleExecution.setLastPlannedVisitDate(datePickerLastPlannedVisitDate
										.getYear()
										+ "-"
										+ datePickerLastPlannedVisitDate
												.getMonth()
										+ "-"
										+ datePickerLastPlannedVisitDate
												.getDayOfMonth());
							}
						});
			}

			final DatePicker datePickerLastActualVisitDate = (DatePicker) rootView
					.findViewById(R.id.datePicker_lastActualVisitDate);
			datePickerLastActualVisitDate.setEnabled(false);
			if (saleExecution.getLastActualVisitDate() != null) {
				String date = saleExecution.getLastActualVisitDate();
				String[] separated = date.split("-");
				datePickerLastActualVisitDate.updateDate(
						Integer.parseInt(separated[0]),
						Integer.parseInt(separated[1]) - 1,
						Integer.parseInt(separated[2]));
				datePickerLastActualVisitDate.init(
						Integer.parseInt(separated[0]),
						Integer.parseInt(separated[1]) - 1,
						Integer.parseInt(separated[2]),
						new OnDateChangedListener() {

							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								saleExecution.setLastActualVisitDate(datePickerLastActualVisitDate
										.getYear()
										+ "-"
										+ datePickerLastActualVisitDate
												.getMonth()
										+ "-"
										+ datePickerLastActualVisitDate
												.getDayOfMonth());
							}
						});
			} else {
				Calendar c = Calendar.getInstance();
				datePickerLastActualVisitDate.init(c.get(Calendar.YEAR),
						c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
						new OnDateChangedListener() {

							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								saleExecution.setLastPlannedVisitDate(datePickerLastActualVisitDate
										.getYear()
										+ "-"
										+ datePickerLastActualVisitDate
												.getMonth()
										+ "-"
										+ datePickerLastActualVisitDate
												.getDayOfMonth());
							}
						});
			}

			final DatePicker datePickerNextPlannedVisitDate = (DatePicker) rootView
					.findViewById(R.id.datePicker_nextPlannedVisitDate);
			datePickerNextPlannedVisitDate.setEnabled(false);
			if (saleExecution.getNextPlannedVisitDate() != null) {
				String date = saleExecution.getNextPlannedVisitDate();
				String[] separated = date.split("-");
				datePickerNextPlannedVisitDate.updateDate(
						Integer.parseInt(separated[0]),
						Integer.parseInt(separated[1]) - 1,
						Integer.parseInt(separated[2]));
				datePickerNextPlannedVisitDate.init(
						Integer.parseInt(separated[0]),
						Integer.parseInt(separated[1]) - 1,
						Integer.parseInt(separated[2]),
						new OnDateChangedListener() {

							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								saleExecution.setNextPlannedVisitDate(datePickerNextPlannedVisitDate
										.getYear()
										+ "-"
										+ datePickerNextPlannedVisitDate
												.getMonth()
										+ "-"
										+ datePickerNextPlannedVisitDate
												.getDayOfMonth());
							}
						});
			} else {
				Calendar c = Calendar.getInstance();
				datePickerNextPlannedVisitDate.init(c.get(Calendar.YEAR),
						c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
						new OnDateChangedListener() {

							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								saleExecution.setNextPlannedVisitDate(datePickerNextPlannedVisitDate
										.getYear()
										+ "-"
										+ datePickerNextPlannedVisitDate
												.getMonth()
										+ "-"
										+ datePickerNextPlannedVisitDate
												.getDayOfMonth());
							}
						});
			}
			datePickerNextPlannedVisitDate.setEnabled(false);

			ImageButton next = (ImageButton) rootView
					.findViewById(R.id.imageButton_next);
			next.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!((HolcimSelectedRetailerActivity) getActivity())
							.isCanEditByDate()) {
						fragmentAccountInfoActions.goToContactInfo(
								saleExecution, comeptitorsMarketings,
								actionLogs, preOrders);
					} else {
						if (setFieldsAccount()) {
							fragmentAccountInfoActions.goToContactInfo(
									saleExecution, comeptitorsMarketings,
									actionLogs, preOrders);
						}
					}
				}
			});

			//CanEditbyDate
			if (!((HolcimSelectedRetailerActivity) getActivity())
					.isCanEditByDate()) {

				// Remove editText and Spinner to the user cannot change fields
				// if is not today's date
				streetAddress.setVisibility(View.GONE);
				postalCode.setVisibility(View.GONE);
				latitude.setVisibility(View.GONE);
				longitude.setVisibility(View.GONE);
				landmarkDescription.setVisibility(View.GONE);
				phone1.setVisibility(View.GONE);
				phone2.setVisibility(View.GONE);
				extension1.setVisibility(View.GONE);
				mobile1.setVisibility(View.GONE);
				mobile2.setVisibility(View.GONE);
				fax1.setVisibility(View.GONE);
				email1.setVisibility(View.GONE);
				bankAccountName1.setVisibility(View.GONE);
				bankAccountBranch1.setVisibility(View.GONE);
				bankAccountNumber1.setVisibility(View.GONE);
				bankAccountName2.setVisibility(View.GONE);
				bankAccountBranch2.setVisibility(View.GONE);
				bankAccountNumber2.setVisibility(View.GONE);
				bankAccountName3.setVisibility(View.GONE);
				bankAccountBranch3.setVisibility(View.GONE);
				bankAccountNumber3.setVisibility(View.GONE);
				retailerTermOfPayment.setVisibility(View.GONE);
				retailerCreditLimit.setVisibility(View.GONE);
				startingYear.setVisibility(View.GONE);
				numberOfPermanentEmployees.setVisibility(View.GONE);
				landStatus.setVisibility(View.GONE);
				eightTTruckArmada.setVisibility(View.GONE);
				pickUp2TArmada.setVisibility(View.GONE);
				twentyFourTTruckArmada.setVisibility(View.GONE);
				deliveryRemark.setVisibility(View.GONE);
				camera.setEnabled(false);

				// Add textView to the user can see all fields values
				txtStreetAddress.setVisibility(View.VISIBLE);
				txtPostalCode.setVisibility(View.VISIBLE);
				txtLatitude.setVisibility(View.VISIBLE);
				txtLongitude.setVisibility(View.VISIBLE);
				txtLandmarkDescription.setVisibility(View.VISIBLE);
				txtPhone1.setVisibility(View.VISIBLE);
				txtPhone2.setVisibility(View.VISIBLE);
				txtExtension1.setVisibility(View.VISIBLE);
				txtMobile1.setVisibility(View.VISIBLE);
				txtMobile2.setVisibility(View.VISIBLE);
				txtFax1.setVisibility(View.VISIBLE);
				txtEmail1.setVisibility(View.VISIBLE);
				txtBankAccountName1.setVisibility(View.VISIBLE);
				txtBankAccountBranch1.setVisibility(View.VISIBLE);
				txtBankAccountNumber1.setVisibility(View.VISIBLE);
				txtBankAccountName2.setVisibility(View.VISIBLE);
				txtBankAccountBranch2.setVisibility(View.VISIBLE);
				txtBankAccountNumber2.setVisibility(View.VISIBLE);
				txtBankAccountName3.setVisibility(View.VISIBLE);
				txtBankAccountBranch3.setVisibility(View.VISIBLE);
				txtBankAccountNumber3.setVisibility(View.VISIBLE);
				txtRetailerTermsOfPayment.setVisibility(View.VISIBLE);
				txtRetailerCreditLimit.setVisibility(View.VISIBLE);
				txtStartingYear.setVisibility(View.VISIBLE);
				txtNumberOfPermanentEmployees.setVisibility(View.VISIBLE);
				txtLandStatus.setVisibility(View.VISIBLE);
				txtEigthTruckArmada.setVisibility(View.VISIBLE);
				txtPickUpTwoTArmada.setVisibility(View.VISIBLE);
				txtTwenyFourTTruckArmada.setVisibility(View.VISIBLE);
				txtDeliveryRemark.setVisibility(View.VISIBLE);
			}
		} else {

			rootView = inflater.inflate(R.layout.prospect, container, false);

			title = (TextView) rootView
					.findViewById(R.id.textView_fragment_title);
			title.setText(R.string.prospect_info_title);

			camera = (ImageButton) rootView
					.findViewById(R.id.imageButton_landmark_picture);

			landmarkDescription = (EditText) rootView
					.findViewById(R.id.edt_landmark_description);
			final TextView pro_accountName = (TextView) rootView
					.findViewById(R.id.edt_accountName);
			final TextView pro_district = (TextView) rootView
					.findViewById(R.id.edt_district);
			firstName = (EditText) rootView.findViewById(R.id.edt_firstName);
			final TextView pro_kecamatan = (TextView) rootView
					.findViewById(R.id.edt_kecamatan);
			final TextView kelurahan = (TextView) rootView
					.findViewById(R.id.txt_kelurahan);
			final TextView pro_kota = (TextView) rootView
					.findViewById(R.id.edt_kota);
			final TextView pro_accountType = (TextView) rootView
					.findViewById(R.id.edt_accountType);
			lastName = (EditText) rootView.findViewById(R.id.edt_lastName);
			latitude = (EditText) rootView.findViewById(R.id.edt_latitude);
			longitude = (EditText) rootView.findViewById(R.id.edt_longitude);
			mobile1 = (EditText) rootView.findViewById(R.id.edt_mobile1);
			phone1 = (EditText) rootView.findViewById(R.id.edt_phone1);
			postalCode = (EditText) rootView.findViewById(R.id.edt_postalCode);
			final TextView pro_prospectId = (TextView) rootView
					.findViewById(R.id.edt_prospectId);
			final TextView pro_province = (TextView) rootView
					.findViewById(R.id.edt_province);
			pro_title = (EditText) rootView.findViewById(R.id.edt_title);
			streetAddress = (EditText) rootView
					.findViewById(R.id.edt_streetAddress);
			editTextBirthdate = (EditText) rootView
					.findViewById(R.id.edit_birthdate);

			final TextView salesTargCurr = (TextView) rootView
					.findViewById(R.id.edt_sales_target_curr);
			final TextView salesTarg = (TextView) rootView
					.findViewById(R.id.edt_sales_target);
			final TextView salesCap = (TextView) rootView
					.findViewById(R.id.edt_sales_cap);
			final TextView salesSow = (TextView) rootView
					.findViewById(R.id.edt_sales_sow);

			final TextView accountRecordType = (TextView) rootView
					.findViewById(R.id.edt_accountRecordType);
			final TextView salesmanInCharge = (TextView) rootView
					.findViewById(R.id.edt_salesmanInCharge);
			final TextView salesOfficer = (TextView) rootView
					.findViewById(R.id.edt_salesOfficer);
			ownerContactNumber = (EditText) rootView
					.findViewById(R.id.edt_ownerContactNumber);

			TextView txtBusinessEntityStatus = (TextView) rootView
					.findViewById(R.id.txt_BusinessEntityStatus);
			TextView txtStreetAddress = (TextView) rootView
					.findViewById(R.id.txt_streetAddress);
			TextView txtKelurahan = (TextView) rootView
					.findViewById(R.id.txt_kelurahan);
			TextView txtPostalCode = (TextView) rootView
					.findViewById(R.id.txt_postalCode);
			TextView txtLatitude = (TextView) rootView
					.findViewById(R.id.txt_latitude);
			TextView txtLongitude = (TextView) rootView
					.findViewById(R.id.txt_longitude);
			TextView txtLandmarkDescription = (TextView) rootView
					.findViewById(R.id.txt_landmark_description);
			TextView txtPhone1 = (TextView) rootView
					.findViewById(R.id.txt_phone1);
			TextView txtMobile1 = (TextView) rootView
					.findViewById(R.id.txt_mobile1);
			TextView txtOwnerTitle = (TextView) rootView
					.findViewById(R.id.txt_ownerTitle);
			TextView txtOwnerFirstName = (TextView) rootView
					.findViewById(R.id.txt_ownerFirstName);
			TextView txtOwnerLastName = (TextView) rootView
					.findViewById(R.id.txt_ownerLastName);
			TextView txtOwnerContactNumber = (TextView) rootView
					.findViewById(R.id.txt_ownerContactNumber);

			TextView txtExperienceWithLeadCompetitors = (TextView) rootView
					.findViewById(R.id.txt_experience_with_lead_competitors);
			TextView txtReasonForBuyingFromLeadCompetitors = (TextView) rootView
					.findViewById(R.id.txt_reason_for_buying_from_lead_competitors);
			TextView txtReasonForUnsatisfiedExperience = (TextView) rootView
					.findViewById(R.id.txt_Reason_for_unsatisfied_experience);

			final Spinner spinnerReasonForBuyingFromLeadCompetitors = (Spinner) rootView
					.findViewById(R.id.spinner_reason_for_buying_from_lead_competitors);
			final Spinner spinnerExperienceWithLeadCompetitors = (Spinner) rootView
					.findViewById(R.id.spinner_experience_with_lead_competitors);
			edtReasonForUnsatisfiedExperience = (EditText) rootView
					.findViewById(R.id.edt_Reason_for_unsatisfied_experience);

			final List<String> experianceWithLeadCompetitors = Arrays
					.asList(getResources().getStringArray(
							R.array.experience_with_lead_competitors_values));
			ArrayAdapter<String> adapterExperianceWithLeadCompetitors = new ArrayAdapter<String>(
					getActivity(), R.layout.spinner,
					experianceWithLeadCompetitors);
			spinnerExperienceWithLeadCompetitors
					.setAdapter(adapterExperianceWithLeadCompetitors);

			spinnerExperienceWithLeadCompetitors
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							if (spinnerExperienceWithLeadCompetitors
									.getAdapter() != null
									&& spinnerExperienceWithLeadCompetitors
											.getAdapter().getItem(position) != null) {
								saleExecution
										.setExperienceWithLeadCompetitor(experianceWithLeadCompetitors
												.get(position));
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						}
					});

			if (this.saleExecution.getExperienceWithLeadCompetitor() != null) {
				spinnerExperienceWithLeadCompetitors
						.setSelection(experianceWithLeadCompetitors
								.indexOf(this.saleExecution
										.getExperienceWithLeadCompetitor()));
				txtExperienceWithLeadCompetitors.setText(this.saleExecution
						.getExperienceWithLeadCompetitor());
			}

			final List<String> reasonForBuyingFromLeadCompetitorsList = Arrays
					.asList(getResources()
							.getStringArray(
									R.array.reason_for_buying_from_lead_competitors_values));
			ArrayAdapter<String> adapterReasonForBuyingFromLeadCompetitors = new ArrayAdapter<String>(
					getActivity(), R.layout.spinner,
					reasonForBuyingFromLeadCompetitorsList);
			spinnerReasonForBuyingFromLeadCompetitors
					.setAdapter(adapterReasonForBuyingFromLeadCompetitors);

			spinnerReasonForBuyingFromLeadCompetitors
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							if (spinnerReasonForBuyingFromLeadCompetitors
									.getAdapter() != null
									&& spinnerReasonForBuyingFromLeadCompetitors
											.getAdapter().getItem(position) != null) {
								saleExecution
										.setReasonForBuyingFromLeadCompetitor(reasonForBuyingFromLeadCompetitorsList
												.get(position));
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						}
					});

			if (this.saleExecution.getReasonForBuyingFromLeadCompetitor() != null) {
				spinnerReasonForBuyingFromLeadCompetitors
						.setSelection(reasonForBuyingFromLeadCompetitorsList.indexOf(this.saleExecution
								.getReasonForBuyingFromLeadCompetitor()));
				txtReasonForBuyingFromLeadCompetitors
						.setText(this.saleExecution
								.getReasonForBuyingFromLeadCompetitor());
			}

			if (this.saleExecution.getReasonForUnsatisfiedExperience() != null) {
				edtReasonForUnsatisfiedExperience.setText(this.saleExecution
						.getReasonForUnsatisfiedExperience());
				txtReasonForUnsatisfiedExperience.setText(this.saleExecution
						.getReasonForUnsatisfiedExperience());
			}

			if (this.saleExecution.getAccountRecordType() != null) {
				accountRecordType.setText(this.saleExecution
						.getAccountRecordType());
			}

			if (this.saleExecution.getSalesOfficer() != null) {
				salesOfficer.setText(this.saleExecution.getSalesOfficer());
			}

			if (this.saleExecution.getSalesmanInCharge() != null) {
				salesmanInCharge.setText(this.saleExecution
						.getSalesmanInCharge());
			}

			if (this.saleExecution.getOwnerContactNumber() != null) {
				ownerContactNumber.setText(String.valueOf(this.saleExecution
						.getOwnerContactNumber()));
				txtOwnerContactNumber.setText(String.valueOf(this.saleExecution
						.getOwnerContactNumber()));
			}

			if (this.saleExecution.getLandmarkDescription() != null) {
				landmarkDescription.setText(this.saleExecution
						.getLandmarkDescription());
				txtLandmarkDescription.setText(this.saleExecution
						.getLandmarkDescription());
			}

			String sSalesTargCurr = (this.saleExecution
					.getSalesTargetCurrentMonth() != null) ? String
					.valueOf(this.saleExecution.getSalesTargetCurrentMonth())
					: "";
			salesTargCurr.setText(sSalesTargCurr);

			String sSalesTarg = (this.saleExecution.getSalesTargetMTD() != null) ? String
					.valueOf(this.saleExecution.getSalesTargetMTD()) : "";
			salesTarg.setText(sSalesTarg);

			String sSalesCap = (this.saleExecution.getCapacityCurrentMonth() != null) ? String
					.valueOf(this.saleExecution.getCapacityCurrentMonth()) : "";
			salesCap.setText(sSalesCap);

			String sSalesSow = (this.saleExecution.getHilSoWCurrentMonth() != null) ? String
					.valueOf(this.saleExecution.getHilSoWCurrentMonth()) : "";
			salesSow.setText(sSalesSow);

			String saccountType = (this.saleExecution.getAccountType() != null) ? this.saleExecution
					.getAccountType() : "";
			pro_accountType.setText(saccountType);

			if (this.saleExecution.getStreetAddress() != null) {
				streetAddress.setText(this.saleExecution.getStreetAddress());
				txtStreetAddress.setText(this.saleExecution.getStreetAddress());
			}
			String spro_accountName = (this.saleExecution.getAccountName() != null) ? this.saleExecution
					.getAccountName() : "";
			pro_accountName.setText(spro_accountName);
			String spro_district = (this.saleExecution.getDistrict() != null) ? this.saleExecution
					.getDistrict() : "";
			pro_district.setText(spro_district);
			if (this.saleExecution.getFirstName() != null) {
				firstName.setText(this.saleExecution.getFirstName());
				txtOwnerFirstName.setText(this.saleExecution.getFirstName());
			}
			String spro_kecamatan = (this.saleExecution.getKecamatan() != null) ? this.saleExecution
					.getKecamatan() : "";
			pro_kecamatan.setText(spro_kecamatan);
			if (this.saleExecution.getKelurahan() != null) {
				kelurahan.setText(this.saleExecution.getKelurahan());
				txtKelurahan.setText(this.saleExecution.getKelurahan());
			}
			String spro_kota = (this.saleExecution.getKota() != null) ? this.saleExecution
					.getKota() : "";
			pro_kota.setText(spro_kota);
			if (this.saleExecution.getLastName() != null) {
				lastName.setText(this.saleExecution.getLastName());
				txtOwnerLastName.setText(this.saleExecution.getLastName());
			}
			if (this.saleExecution.getLatitude() != null) {
				latitude.setText(this.saleExecution.getLatitude());
				txtLatitude.setText(this.saleExecution.getLatitude());
			}
			if (this.saleExecution.getLongitude() != null) {
				longitude.setText(this.saleExecution.getLongitude());
				txtLongitude.setText(this.saleExecution.getLongitude());
			}
			if (this.saleExecution.getMobile1() != null) {
				mobile1.setText(this.saleExecution.getMobile1());
				txtMobile1.setText(this.saleExecution.getMobile1());
			}
			if (this.saleExecution.getPhone1() != null) {
				phone1.setText(this.saleExecution.getPhone1());
				txtPhone1.setText(this.saleExecution.getPhone1());
			}
			if (this.saleExecution.getPostalCode() != null) {
				postalCode.setText(this.saleExecution.getPostalCode());
				txtPostalCode.setText(this.saleExecution.getPostalCode());
			}
			String spro_prospectId = (this.saleExecution.getRetailerId() != null) ? String
					.valueOf(this.saleExecution.getRetailerId()) : "";
			pro_prospectId.setText(spro_prospectId);
			String spro_province = (this.saleExecution.getProvince() != null) ? this.saleExecution
					.getProvince() : "";
			pro_province.setText(spro_province);
			if (this.saleExecution.getTitle() != null) {
				pro_title.setText(this.saleExecution.getTitle());
				txtOwnerTitle.setText(this.saleExecution.getTitle());
			}

			Button btnChekin = (Button) rootView.findViewById(R.id.btn_checkin);
			btnChekin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					fragmentAccountInfoActions.getLocation();
					LatLng currentLocation = ((HolcimSelectedRetailerActivity) getActivity())
							.getCurrentLocation();
                    if(currentLocation!= null) {
                        latitude.setText(String.valueOf(currentLocation.latitude));
                        longitude.setText(String.valueOf(currentLocation.longitude));
                    }
				}
			});
			if (!((HolcimSelectedRetailerActivity) getActivity())
					.isCanEditByDate()) {
				btnChekin.setVisibility(View.INVISIBLE);
			}

			final DatePicker datepickerBirthDate = (DatePicker) rootView
					.findViewById(R.id.datePicker_birthdate);

			if (this.saleExecution.getBirthDate() != null) {
				editTextBirthdate.setVisibility(View.GONE);
				datepickerBirthDate.setVisibility(View.VISIBLE);
				String date = this.saleExecution.getBirthDate();
				String[] separated = date.split("-");
				datepickerBirthDate.updateDate(Integer.parseInt(separated[0]),
						Integer.parseInt(separated[1]) - 1,
						Integer.parseInt(separated[2]));
				datepickerBirthDate.init(Integer.parseInt(separated[0]),
						Integer.parseInt(separated[1]) - 1,
						Integer.parseInt(separated[2]),
						new OnDateChangedListener() {

							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								saleExecution.setBirthDate(datepickerBirthDate
										.getYear()
										+ "-"
										+ (datepickerBirthDate.getMonth() + 1)
										+ "-"
										+ datepickerBirthDate.getDayOfMonth());
							}
						});
			} else {
				datepickerBirthDate.setVisibility(View.GONE);
				editTextBirthdate.setVisibility(View.VISIBLE);
				editTextBirthdate.setFocusable(false);
				editTextBirthdate.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final HolcimSelectedRetailerActivity activity = (HolcimSelectedRetailerActivity) getActivity();
						String date = saleExecution.getBirthDate();
						activity.dialog.showDatePickerModal(date,
								getString(R.string.ok),
								getString(R.string.cancel),
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										final DatePicker datePickerBirthdate2 = (DatePicker) activity.dialog
												.findViewById(R.id.datePicker_birthdate_modal);
										activity.dialog.dismiss();
										editTextBirthdate.setText((datePickerBirthdate2
												.getMonth() + 1)
												+ "/"
												+ datePickerBirthdate2
														.getDayOfMonth()
												+ "/"
												+ datePickerBirthdate2
														.getYear());
										saleExecution.setBirthDate(datePickerBirthdate2
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

			final Spinner spinnerBusinessEntityStatus = (Spinner) rootView
					.findViewById(R.id.spinner_BusinessEntityStatus);
			final List<String> businessEntityStatusList = Arrays
					.asList(getResources().getStringArray(
							R.array.prospect_business_entity_status));
			ArrayAdapter<String> adapterBusinessEntityStatus = new ArrayAdapter<String>(
					getActivity(), R.layout.spinner, businessEntityStatusList);
			spinnerBusinessEntityStatus.setAdapter(adapterBusinessEntityStatus);

			spinnerBusinessEntityStatus
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							if (spinnerBusinessEntityStatus.getAdapter() != null
									&& spinnerBusinessEntityStatus.getAdapter()
											.getItem(position) != null) {
								saleExecution
										.setBusinessEntityStatus(businessEntityStatusList
												.get(position));
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						}
					});

			if (this.saleExecution.getBusinessEntityStatus() != null) {
				spinnerBusinessEntityStatus
						.setSelection(businessEntityStatusList
								.indexOf(this.saleExecution
										.getBusinessEntityStatus()));
				txtBusinessEntityStatus.setText(this.saleExecution
						.getBusinessEntityStatus());
			}

			ImageButton next = (ImageButton) rootView
					.findViewById(R.id.imageButton_next);
			next.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (((HolcimSelectedRetailerActivity) getActivity())
							.isCanEditByDate()) {
						if (String.valueOf(mobile1.getText()).isEmpty()
								&& String.valueOf(phone1.getText()).isEmpty()) {
							warningPhoneMobile();
							phone1.requestFocus();
						} else {
							setFieldsProspect();
							fragmentAccountInfoActions.goToContactInfo(
									saleExecution, comeptitorsMarketings,
									actionLogs, preOrders);
						}
					} else {
						fragmentAccountInfoActions.goToContactInfo(
								saleExecution, comeptitorsMarketings,
								actionLogs, preOrders);
					}
				}
			});

			if (!((HolcimSelectedRetailerActivity) getActivity())
					.isCanEditByDate()) {

				// Remove editText and Spinner to the user cannot change fields
				// if is not today's date
				spinnerBusinessEntityStatus.setVisibility(View.GONE);
				streetAddress.setVisibility(View.GONE);
				kelurahan.setVisibility(View.GONE);
				postalCode.setVisibility(View.GONE);
				latitude.setVisibility(View.GONE);
				longitude.setVisibility(View.GONE);
				landmarkDescription.setVisibility(View.GONE);
				phone1.setVisibility(View.GONE);
				mobile1.setVisibility(View.GONE);
				pro_title.setVisibility(View.GONE);
				firstName.setVisibility(View.GONE);
				lastName.setVisibility(View.GONE);
				ownerContactNumber.setVisibility(View.GONE);
				camera.setEnabled(false);
				edtReasonForUnsatisfiedExperience.setVisibility(View.GONE);
				spinnerExperienceWithLeadCompetitors.setVisibility(View.GONE);
				spinnerReasonForBuyingFromLeadCompetitors
						.setVisibility(View.GONE);

				// visibility of the datepicker
				if (saleExecution.getBirthDate() != null) {
					editTextBirthdate.setVisibility(View.GONE);
					datepickerBirthDate.setVisibility(View.VISIBLE);
					datepickerBirthDate.setEnabled(false);
				} else {
					datepickerBirthDate.setVisibility(View.GONE);
					editTextBirthdate.setVisibility(View.INVISIBLE);
				}

				// Add textView to the user can see all fields values
				txtBusinessEntityStatus.setVisibility(View.VISIBLE);
				txtStreetAddress.setVisibility(View.VISIBLE);
				txtKelurahan.setVisibility(View.VISIBLE);
				txtPostalCode.setVisibility(View.VISIBLE);
				txtLatitude.setVisibility(View.VISIBLE);
				txtLongitude.setVisibility(View.VISIBLE);
				txtLandmarkDescription.setVisibility(View.VISIBLE);
				txtPhone1.setVisibility(View.VISIBLE);
				txtMobile1.setVisibility(View.VISIBLE);
				txtOwnerTitle.setVisibility(View.VISIBLE);
				txtOwnerFirstName.setVisibility(View.VISIBLE);
				txtOwnerLastName.setVisibility(View.VISIBLE);
				txtOwnerContactNumber.setVisibility(View.VISIBLE);
				txtExperienceWithLeadCompetitors.setVisibility(View.VISIBLE);
				txtReasonForBuyingFromLeadCompetitors
						.setVisibility(View.VISIBLE);
				txtReasonForUnsatisfiedExperience.setVisibility(View.VISIBLE);
			}

		}
		
		//Backbutton
		ImageButton back = (ImageButton) rootView
				.findViewById(R.id.imageButton_back);
		back.setVisibility(View.INVISIBLE);

		//Homebutton
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
										fragmentAccountInfoActions
												.deleteActionLogs();
										fragmentAccountInfoActions
												.deleteCompetitorMarketing();
										fragmentAccountInfoActions
												.deletePreOrders();
										fragmentAccountInfoActions
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

		//Camera
		refreshImage();
		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				fragmentAccountInfoActions.takePhoto();
			}
		});

		return rootView;
	}

	//Reload image to show 
	public void refreshImage() {
		try {
			if (saleExecution.isLandmarkPictureTempFileExist(getActivity())) {
				camera.setImageBitmap(HolcimUtility
						.decodeSampledBitmapFromFile(saleExecution
								.getTempLandmarkPictureFilePath(getActivity()),
								200, 200, 0));
			} else if (saleExecution.getId() != null
					&& saleExecution.isLandmarkPictureFileExist(getActivity())) {
				camera.setImageBitmap(HolcimUtility
						.decodeSampledBitmapFromFile(saleExecution
								.getLandmarkPicturePath(getActivity()), 200,
								200, 0));
			} else {
				camera.setImageResource(R.drawable.camera);
			}
		} catch (HolcimException e) {
			e.printStackTrace();
		}
	}

	public Boolean setFieldsAccount() {
		if (((HolcimSelectedRetailerActivity) getActivity()) != null
				&& ((HolcimSelectedRetailerActivity) getActivity())
						.isCanEditByDate()) {
			if (String.valueOf(mobile1.getText()).isEmpty()
					&& String.valueOf(phone1.getText()).isEmpty()) {
				warningPhoneMobile();
				phone1.requestFocus();
				return false;
			} else {
				this.saleExecution.setBankAccountBranch1(String
						.valueOf(bankAccountBranch1.getText()));
				this.saleExecution.setBankAccountBranch2(String
						.valueOf(bankAccountBranch2.getText()));
				this.saleExecution.setBankAccountBranch3(String
						.valueOf(bankAccountBranch3.getText()));
				this.saleExecution.setBankAccountName1(String
						.valueOf(bankAccountName1.getText()));
				this.saleExecution.setBankAccountName2(String
						.valueOf(bankAccountName2.getText()));
				this.saleExecution.setBankAccountName3(String
						.valueOf(bankAccountName3.getText()));
				this.saleExecution.setBankAccountNumber1(String
						.valueOf(bankAccountNumber1.getText()));
				this.saleExecution.setBankAccountNumber2(String
						.valueOf(bankAccountNumber2.getText()));
				this.saleExecution.setBankAccountNumber3(String
						.valueOf(bankAccountNumber3.getText()));
				this.saleExecution.setDeliveryRemark(String
						.valueOf(deliveryRemark.getText()));
				this.saleExecution.setEightTTruckArmada(String
						.valueOf(eightTTruckArmada.getText()));
				this.saleExecution.setEmail1(String.valueOf(email1.getText()));
				this.saleExecution.setExtension1(String.valueOf(extension1
						.getText()));
				this.saleExecution.setFax1(String.valueOf(fax1.getText()));
				this.saleExecution.setLatitude(String.valueOf(latitude
						.getText()));
				this.saleExecution.setLongitude(String.valueOf(longitude
						.getText()));
				this.saleExecution
						.setMobile1(String.valueOf(mobile1.getText()));
				this.saleExecution
						.setMobile2(String.valueOf(mobile2.getText()));
				this.saleExecution.setNumberOfPermanentEmployees(String
						.valueOf(numberOfPermanentEmployees.getText()).equals(
								"") ? 0 : Double.parseDouble(String
						.valueOf(numberOfPermanentEmployees.getText())));
				this.saleExecution.setPhone1(String.valueOf(phone1.getText()));
				this.saleExecution.setPhone2(String.valueOf(phone2.getText()));
				this.saleExecution.setPickUp2TArmada(String
						.valueOf(pickUp2TArmada.getText()));
				this.saleExecution.setPostalCode(String.valueOf(postalCode
						.getText()));
				this.saleExecution.setRetailerCreditLimit((String
						.valueOf(retailerCreditLimit.getText()).equals("")) ? 0
						: Double.parseDouble(String.valueOf(retailerCreditLimit
								.getText())));
				this.saleExecution.setStartingYear((String.valueOf(startingYear
						.getText()).equals("")) ? 0 : Integer.parseInt(String
						.valueOf(startingYear.getText())));
				this.saleExecution.setStreetAddress(String
						.valueOf(streetAddress.getText()));
				this.saleExecution.setTwentyFourTTruckArmada(String
						.valueOf(twentyFourTTruckArmada.getText()));
				this.saleExecution.setLandmarkDescription(String
						.valueOf(landmarkDescription.getText()));
			}
		}
		return true;
	}

	public Boolean setFieldsProspect() {
		if (String.valueOf(mobile1.getText()).isEmpty()
				&& String.valueOf(phone1.getText()).isEmpty()) {
			warningPhoneMobile();
			phone1.requestFocus();
			return false;
		} else {
			if (!String.valueOf(ownerContactNumber.getText()).equals("")) {
				this.saleExecution.setOwnerContactNumber(String
						.valueOf(ownerContactNumber.getText()));
			}
			this.saleExecution.setStreetAddress(String.valueOf(streetAddress
					.getText()));
			this.saleExecution
					.setFirstName(String.valueOf(firstName.getText()));
			this.saleExecution.setLastName(String.valueOf(lastName.getText()));
			this.saleExecution.setLatitude(String.valueOf(latitude.getText()));
			this.saleExecution
					.setLongitude(String.valueOf(longitude.getText()));
			this.saleExecution.setMobile1(String.valueOf(mobile1.getText()));
			this.saleExecution.setPhone1(String.valueOf(phone1.getText()));
			this.saleExecution.setPostalCode(String.valueOf(postalCode
					.getText()));
			this.saleExecution.setTitle(String.valueOf(pro_title.getText()));
			this.saleExecution.setLandmarkDescription(String
					.valueOf(landmarkDescription.getText()));
			this.saleExecution.setReasonForUnsatisfiedExperience(String
					.valueOf(edtReasonForUnsatisfiedExperience.getText()));
		}
		return true;
	}

	public String getLatitude() {
		return String.valueOf(latitude.getText());
	}

	public void setLatitude(String latitude) {
		this.latitude.setText(latitude);
	}

	public String getLongitude() {
		return String.valueOf(longitude.getText());
	}

	public void setLongitude(String longitude) {
		this.longitude.setText(longitude);
	}

	private void warningPhoneMobile() {
		final HolcimSelectedRetailerActivity activity = (HolcimSelectedRetailerActivity) getActivity();

		activity.dialog.showError(getString(R.string.warning_phone_mobile),
				getString(R.string.ok), new OnClickListener() {

					@Override
					public void onClick(View v) {
						activity.dialog.dismiss();
					}
				});
	}
}