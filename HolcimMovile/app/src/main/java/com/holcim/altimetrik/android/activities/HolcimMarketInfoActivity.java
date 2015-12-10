package com.holcim.altimetrik.android.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.model.CompetitorMarketing;
import com.holcim.altimetrik.android.model.SaleExecution;
import com.holcim.altimetrik.android.utilities.HolcimConsts;

public class HolcimMarketInfoActivity extends HolcimCustomActivity {
	private CompetitorMarketing competitorMarketing = null;
	private ArrayList<CompetitorMarketing> competitorMarketings = null;
	private long saleExecutionId;
	private SaleExecution saleExecution;
	private int position = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.market_info);
		super.onCreate(savedInstanceState);

		competitorMarketing = (CompetitorMarketing) getIntent()
				.getSerializableExtra(HolcimConsts.OBJECT_SERIALIZABLE_KEY);
		competitorMarketings = (ArrayList<CompetitorMarketing>) getIntent()
				.getSerializableExtra(HolcimConsts.OBJECT_LIST_SERIALIZABLE_KEY);

		if (getIntent().getExtras() != null) {
			saleExecutionId = getIntent().getExtras()
					.getLong("saleExecutionId");
			saleExecution = HolcimApp.daoSession.getSaleExecutionDao().load(
					saleExecutionId);
			position = getIntent().getExtras().getInt("isEditing");
		}

		ImageView homePage = (ImageView) findViewById(R.id.imageButton_home);
		homePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				HolcimCustomActivity.setOnback(true);
				Intent intent = new Intent(HolcimMarketInfoActivity.this,
						HolcimMainActivity.class);
				startActivity(intent);
			}
		});

		Button buttonFinish = (Button) findViewById(R.id.button_finish);
		buttonFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent(HolcimMarketInfoActivity.this,
						HolcimSelectedRetailerActivity.class);
				HolcimCustomActivity.setOnback(true);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(HolcimConsts.OBJECT_SERIALIZABLE_KEY,
						competitorMarketing);
				returnIntent.putExtras(mBundle);
				returnIntent.putExtra("isEditing", position);
				setResult(HolcimConsts.ACTIVITY_RESULT_CODE_COMP_MARK_OK,
						returnIntent);
				finish();
			}
		});

		setCustomTitle(getString(R.string.title_create_competitor_marketing));

		TextView txtSupplierName = (TextView) findViewById(R.id.lbl_supplierName);
		TextView txtSupplierNameValue = (TextView) findViewById(R.id.lbl_supplierNameValue);
		TextView txtBuyingPrice = (TextView) findViewById(R.id.lbl_BuyingPrice);
		TextView txtBuyingPriceValue = (TextView) findViewById(R.id.lbl_BuyingPriceValue);
		TextView txtSellingPrice = (TextView) findViewById(R.id.lbl_SellingPrice);
		TextView txtSellingPriceValue = (TextView) findViewById(R.id.lbl_SellingPriceValue);
		TextView txtInventory = (TextView) findViewById(R.id.lbl_Inventory);
		TextView txtInventoryValue = (TextView) findViewById(R.id.lbl_InventoryValue);
		TextView txtLastMonthBuyingVolume = (TextView) findViewById(R.id.lbl_lastMonthBuyingVolume);
		TextView txtLastMonthBuyingVolumeValue = (TextView) findViewById(R.id.lbl_lastMonthBuyingVolumeValue);
		TextView txtLastMonthBuyingVolumeDate = (TextView) findViewById(R.id.lbl_lastMonthBuyingVolumeDate);
		DatePicker datepickerLastMonthBuyingVolumeDateValue = (DatePicker) findViewById(R.id.datepicker_lastMonthBuyingVolumeDateValue);
		TextView txtCompetitorMarginWithHIL = (TextView) findViewById(R.id.lbl_competitorMarginWithHIL);
		TextView txtCompetitorMarginWithHILValue = (TextView) findViewById(R.id.lbl_competitorMarginWithHILValue);
		TextView txtCompetitorMarketInfoTitle = (TextView) findViewById(R.id.lbl_competitorMarketInfoTitle);
		TextView txtPromotion = (TextView) findViewById(R.id.lbl_Promotion);
		TextView txtPromotionValue = (TextView) findViewById(R.id.lbl_PromotionValue);
		TextView txtPromotionStartDate = (TextView) findViewById(R.id.lbl_promotionStartDate);
		DatePicker datepickerPromotionStartDateValue = (DatePicker) findViewById(R.id.datepicker_promotionStartDateValue);
		TextView txtPromotionEndDate = (TextView) findViewById(R.id.lbl_promotionEndDate);
		DatePicker datepickerPromotionEndDateValue = (DatePicker) findViewById(R.id.datepicker_promotionEndDateValue);
		TextView txtProgram = (TextView) findViewById(R.id.lbl_Program);
		TextView txtProgramValue = (TextView) findViewById(R.id.lbl_ProgramValue);
		TextView txtProgramStartDate = (TextView) findViewById(R.id.lbl_programStartDate);
		DatePicker datepickerProgramStartDateValue = (DatePicker) findViewById(R.id.datepicker_programStartDateValue);
		TextView txtProgramEndDate = (TextView) findViewById(R.id.lbl_programEndDate);
		DatePicker datepickerProgramEndDateValue = (DatePicker) findViewById(R.id.datepicker_programEndDateValue);
		TextView txtIssue = (TextView) findViewById(R.id.lbl_issue);
		TextView txtIssueValue = (TextView) findViewById(R.id.lbl_issueValue);

		String[] values = getResources().getStringArray(
				R.array.competitor_marketing_fields);
		txtSupplierName.setText(values[1]);
		txtBuyingPrice.setText(values[2]);
		txtSellingPrice.setText(values[3]);
		txtInventory.setText(values[4]);
		txtLastMonthBuyingVolume.setText(values[5]);
		txtLastMonthBuyingVolumeDate.setText(values[6]);
		txtCompetitorMarketInfoTitle.setText(values[7]);
		txtCompetitorMarginWithHIL.setText(values[8]);
		txtPromotion.setText(values[9]);
		txtPromotionStartDate.setText(values[10]);
		txtPromotionEndDate.setText(values[11]);
		txtProgram.setText(values[12]);
		txtProgramStartDate.setText(values[13]);
		txtProgramEndDate.setText(values[14]);
		txtIssue.setText(values[15]);

		if (competitorMarketing != null) {
			if (competitorMarketing.getCompetitorName() != null) {
				txtSupplierNameValue.setText(competitorMarketing
						.getCompetitorName());
				if (saleExecution != null
						&& saleExecution.getLastBuyingVolumeDate() != null) {
					String[] date = saleExecution.getLastBuyingVolumeDate()
							.split("-");
					datepickerLastMonthBuyingVolumeDateValue.updateDate(
							Integer.parseInt(date[0]),
							(Integer.parseInt(date[1]) - 1),
							Integer.parseInt(date[2]));
				}
			}

			if (competitorMarketing.getBuyingPrice() != null) {
				txtBuyingPriceValue.setText(String.valueOf(competitorMarketing
						.getBuyingPrice()));
			}

			if (competitorMarketing.getSellingPrice() != null) {
				txtSellingPriceValue.setText(String.valueOf(competitorMarketing
						.getSellingPrice()));
			}

			if (competitorMarketing.getInventory() != null) {
				txtInventoryValue.setText(competitorMarketing.getInventory());
			}

			if (competitorMarketing.getLastMonthCompetitorBuyingVolume() != null) {
				txtLastMonthBuyingVolumeValue.setText(competitorMarketing
						.getLastMonthCompetitorBuyingVolume());
			}

			if (competitorMarketing.getCompetitorMarginHIL() != null) {
				txtCompetitorMarginWithHILValue.setText(String
						.valueOf(competitorMarketing.getCompetitorMarginHIL()));
			}

			if (competitorMarketing.getPromotion() != null) {
				txtPromotionValue.setText(competitorMarketing.getPromotion());
			}

			if (competitorMarketing.getProgram() != null) {
				txtProgramValue.setText(competitorMarketing.getProgram());
			}

			if (competitorMarketing.getIssue() != null) {
				txtIssueValue.setText(competitorMarketing.getIssue());
			}

			if (competitorMarketing.getPromotionStartDate() != null) {
				String[] date = saleExecution.getPromotionStartDate()
						.split("-");
				datepickerPromotionStartDateValue.updateDate(
						Integer.parseInt(date[0]),
						(Integer.parseInt(date[1]) - 1),
						Integer.parseInt(date[2]));
			}

			if (competitorMarketing.getPromotionEndDate() != null) {
				String[] date = saleExecution.getPromotionEndDate().split("-");
				datepickerPromotionEndDateValue.updateDate(
						Integer.parseInt(date[0]),
						(Integer.parseInt(date[1]) - 1),
						Integer.parseInt(date[2]));
			}

			if (competitorMarketing.getProgramStartDate() != null) {
				String[] date = saleExecution.getProgramStartDate().split("-");
				datepickerProgramStartDateValue.updateDate(
						Integer.parseInt(date[0]),
						(Integer.parseInt(date[1]) - 1),
						Integer.parseInt(date[2]));
			}

			if (competitorMarketing.getProgramEndDate() != null) {
				String[] date = saleExecution.getProgramEndDate().split("-");
				datepickerProgramEndDateValue.updateDate(
						Integer.parseInt(date[0]),
						(Integer.parseInt(date[1]) - 1),
						Integer.parseInt(date[2]));
			}
		}

		datepickerLastMonthBuyingVolumeDateValue.setEnabled(false);
		datepickerPromotionStartDateValue.setEnabled(false);
		datepickerPromotionEndDateValue.setEnabled(false);
		datepickerProgramStartDateValue.setEnabled(false);
		datepickerProgramEndDateValue.setEnabled(false);

	}

}
