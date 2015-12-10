package com.holcim.altimetrik.android.activities;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.model.Account;
import com.holcim.altimetrik.android.model.PreOrder;
import com.holcim.altimetrik.android.model.Prospect;
import com.holcim.altimetrik.android.model.TeleSale;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimUtility;

public class HolcimCreatePreorderActivity extends HolcimCustomActivity {
	private PreOrder preOrder = null;
	private long saleExecutionId = -1;
	private boolean tele_sale = false;
	private boolean isAccount = true;
	private long accountId = -1;
	private long prospectId = -1;

	DatePicker poDate;
	Spinner product;
	Spinner unit;
	EditText poVolume;
	String accountOrProspectName;

	int position = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.preorder);
		super.onCreate(savedInstanceState);

		TextView textViewFragmentTitle = (TextView) findViewById(R.id.textView_fragment_title);
		poDate = (DatePicker) findViewById(R.id.dp_po_date);
		poVolume = (EditText) findViewById(R.id.edt_po_quantity);
		product = (Spinner) findViewById(R.id.spinner_product);
		unit = (Spinner) findViewById(R.id.spinner_unit);
		Button buttonFinish = (Button) findViewById(R.id.button_finish);
		ImageButton next = (ImageButton) findViewById(R.id.imageButton_next);
		ImageButton back = (ImageButton) findViewById(R.id.imageButton_back);
		final TextView productLabel = (TextView) findViewById(R.id.lbl_po_product);

		poDate.setEnabled(false);

		preOrder = (PreOrder) getIntent().getSerializableExtra(
				HolcimConsts.OBJECT_SERIALIZABLE_KEY);
		if (getIntent().getExtras() != null) {
			tele_sale = getIntent().getExtras().getBoolean("tele_sale");
			if (tele_sale) {
				isAccount = getIntent().getExtras().getBoolean("isAccount");
				if (isAccount) {
					accountId = getIntent().getExtras().getLong("accountId");
				} else {
					prospectId = getIntent().getExtras().getLong("prospectId");
				}
			} else {
				accountOrProspectName = getIntent().getExtras().getString(
						"accountOrProspectName");
				saleExecutionId = getIntent().getExtras().getLong(
						"saleExecutionId");
				position = getIntent().getExtras().getInt("isEditing");
			}

			if (tele_sale) {
				textViewFragmentTitle
						.setText(getString(R.string.tele_sale_title));
			} else {
				textViewFragmentTitle
						.setText(getString(R.string.preorder_title));
			}

		}

		next.setVisibility(View.INVISIBLE);
		back.setVisibility(View.INVISIBLE);
		buttonFinish.setVisibility(View.VISIBLE);

		// Product
		ArrayList<String> poProduct = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.pre_order_product)));
		ArrayAdapter<String> adapterProduct = new ArrayAdapter<String>(this,
				R.layout.spinner, poProduct);
		product.setAdapter(adapterProduct);
		if (preOrder != null && preOrder.getProduct() != null
				&& !poProduct.get(position).equals(""))
			product.setSelection(poProduct.indexOf(preOrder.getProduct()));
		// -------

		// Unit
		final ArrayList<String> poUnit = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.pre_order_unit)));
		ArrayAdapter<String> adapterUnit = new ArrayAdapter<String>(this,
				R.layout.spinner, poUnit);
		unit.setAdapter(adapterUnit);
		if (preOrder != null && preOrder.getUnit() != null
				&& !poUnit.get(position).equals(""))
			product.setSelection(poProduct.indexOf(preOrder.getUnit()));
		// -------

		if (preOrder != null) {
			// Date
			if (preOrder.getPreOrderDate() != null) {
				String date = preOrder.getPreOrderDate();
				String[] separated = date.split("-");
				poDate.updateDate(Integer.parseInt(separated[0]),
						Integer.parseInt(separated[1]) - 1,
						Integer.parseInt(separated[2]));
			}

			// Product
			if (preOrder.getProduct() != null) {
				product.setSelection(poProduct.indexOf(preOrder.getProduct()));
			}

			// Unit
			if (preOrder.getUnit() != null) {
				unit.setSelection(poUnit.indexOf(preOrder.getUnit()));
			}

			// Volume
			if (preOrder.getPreOrderQuantity() != null) {
				poVolume.setText(String.valueOf(preOrder.getPreOrderQuantity()));
			}
		}

		buttonFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (preOrder == null) {
					preOrder = new PreOrder();
				}

				if (!tele_sale
						&& (String.valueOf(poVolume.getText()).equals("")
								|| unit.getSelectedItem().toString().equals("") || (!unit
								.getSelectedItem()
								.equals(HolcimConsts.PREORDER_UNIT_NOT_NEED_TO_KNOW_PRODUCT) && product
								.getSelectedItem().toString().equals("")))) {
					dialog.showError(
							getString(R.string.pre_order_quantity_error),
							getString(R.string.ok), new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				} else if (tele_sale
						&& (String.valueOf(poVolume.getText()).equals("")
								|| unit.getSelectedItem().toString().equals("") || (!unit
								.getSelectedItem()
								.equals(HolcimConsts.PREORDER_UNIT_NOT_NEED_TO_KNOW_PRODUCT) && product
								.getSelectedItem().toString().equals("")))) {
					dialog.showError(
							getString(R.string.pre_order_quantity_error),
							getString(R.string.ok), new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				} else {
					collectData(preOrder);

					if (tele_sale) {
						HolcimCustomActivity.setOnback(true);
						Intent returnIntent = new Intent(
								HolcimCreatePreorderActivity.this,
								HolcimMainActivity.class);
						startActivity(returnIntent);
					} else {
						HolcimCustomActivity.setOnback(true);
						Intent returnIntent = new Intent(
								HolcimCreatePreorderActivity.this,
								HolcimSelectedRetailerActivity.class);
						Bundle mBundle = new Bundle();
						mBundle.putSerializable(
								HolcimConsts.OBJECT_SERIALIZABLE_KEY, preOrder);
						returnIntent.putExtras(mBundle);
						returnIntent.putExtra("isEditing", position);
						setResult(
								HolcimConsts.ACTIVITY_RESULT_CODE_PRE_ORDER_OK,
								returnIntent);
						finish();
					}
				}

			}
		});

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
										HolcimCreatePreorderActivity.this,
										HolcimMainActivity.class);
								startActivity(intent);
							}
						}, dialog.mDismissClickListener);
			}
		});

		unit.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (unit.getAdapter() != null
						&& product.getAdapter().getItem(position) != null) {
					if (!poUnit
							.get(position)
							.equalsIgnoreCase(
									HolcimConsts.PREORDER_UNIT_NOT_NEED_TO_KNOW_PRODUCT)
							&& !poUnit.get(position).equals("")) {
						product.setVisibility(View.VISIBLE);
						productLabel.setVisibility(View.VISIBLE);
					} else {
						product.setSelection(0);
						product.setVisibility(View.GONE);
						productLabel.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		if (!tele_sale) {
			setCustomTitle(getString(R.string.title_create_preorder));
		} else {
			setAccountOrProspectNameTelesale();
		}
	}

	private void setAccountOrProspectNameTelesale() {
		if (isAccount && accountId >= 0) {
			Account account = HolcimApp.daoSession.getAccountDao().load(
					accountId);
			if (account != null) {
				setCustomTitle(account.getName());
			}
		} else if (prospectId >= 0) {
			Prospect prospect = HolcimApp.daoSession.getProspectDao().load(
					prospectId);
			if (prospect != null) {
				setCustomTitle(prospect.getName());
			}
		}
	}

	void collectData(PreOrder po) {
		if (tele_sale) {
			TeleSale teleSale = new TeleSale();
			if (isAccount && accountId >= 0) {
				teleSale.setAccountId(accountId);
			} else if (prospectId >= 0) {
				teleSale.setProspectId(prospectId);
			}
			teleSale.setPreOrderDate(HolcimUtility.getStringDateFormatted(
					poDate.getYear(), poDate.getMonth(),
					poDate.getDayOfMonth(), "yyyy-MM-dd"));
			teleSale.setProduct(product.getSelectedItem().toString());
			if (!poVolume.getText().toString().equals("")) {
				try {
					teleSale.setPreOrderQuantity(Double.parseDouble(poVolume
							.getText().toString()));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			teleSale.setUnit(unit.getSelectedItem().toString());
			HolcimApp.daoSession.insert(teleSale);
		} else {
			if (preOrder != null) {
				preOrder.setPreOrderDate(HolcimUtility.getStringDateFormatted(
						poDate.getYear(), poDate.getMonth(),
						poDate.getDayOfMonth(), "yyyy-MM-dd"));
				preOrder.setProduct(product.getSelectedItem().toString());
				if (!poVolume.getText().toString().equals("")) {
					try {
						preOrder.setPreOrderQuantity(Double
								.parseDouble(poVolume.getText().toString()));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				preOrder.setUnit(unit.getSelectedItem().toString());
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (!HolcimCustomActivity.blockback) {
			HolcimCustomActivity.setOnback(true);
			super.onBackPressed();
		}
	}
}