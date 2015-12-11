package com.holcim.altimetrik.android.activities;

import java.util.ArrayList;
import java.util.Calendar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.holcim.altimetrik.android.model.Account;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.model.Competitor;
import com.holcim.altimetrik.android.model.CompetitorDao;
import com.holcim.altimetrik.android.model.CompetitorMarketing;
import com.holcim.altimetrik.android.model.SaleExecution;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimUtility;

public class HolcimCreateCompetitorMarketing extends HolcimCustomActivity {
	private CompetitorMarketing competitorMarketing = null;
	private ArrayList<CompetitorMarketing> competitorMarketings = null;
	private long saleExecutionId;
	private SaleExecution saleExecution;
    private Account account;
	private int position = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.create_new_contact);
		super.onCreate(savedInstanceState);

		TextView textViewFragmentTitle = (TextView) findViewById(R.id.textView_fragment_title);
		ListView listviewFields = (ListView) findViewById(R.id.list_view_contact_fields);
		Button buttonFinish = (Button) findViewById(R.id.button_finish);

		textViewFragmentTitle
				.setText(getString(R.string.title_competitor_marketing));

		competitorMarketing = (CompetitorMarketing) getIntent()
				.getSerializableExtra(HolcimConsts.OBJECT_SERIALIZABLE_KEY);
		competitorMarketings = (ArrayList<CompetitorMarketing>) getIntent()
				.getSerializableExtra(HolcimConsts.OBJECT_LIST_SERIALIZABLE_KEY);

		if (getIntent().getExtras() != null) {
			saleExecutionId = getIntent().getExtras()
					.getLong("saleExecutionId");
			saleExecution = HolcimApp.daoSession.getSaleExecutionDao().load(
					saleExecutionId);
            account =  HolcimApp.daoSession.getAccountDao().load(
                    getIntent().getExtras().getLong("accountId"));
			position = getIntent().getExtras().getInt("isEditing");
		}

		final HolcimCreateCompetitorMarketingAdapter adapter = new HolcimCreateCompetitorMarketingAdapter(
				getResources().getStringArray(
						R.array.competitor_marketing_fields), this,
				competitorMarketing);
		listviewFields.setAdapter(adapter);

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
										HolcimCreateCompetitorMarketing.this,
										HolcimMainActivity.class);
								startActivity(intent);
							}
						}, dialog.mDismissClickListener);
			}
		});

		buttonFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Competitor competitor;
				if (competitorMarketing == null) {
					competitor = createCompetitorMarketing(adapter);
				} else {
					competitor = createOrUpdateCompetitorMarketing(adapter);
				}
				if (competitor != null) {
					if (!existCompetitorMarketingWithCompetitorName(competitor
							.getName())
							&& HolcimUtility.validDates(
									competitorMarketing.getProgramStartDate(),
									competitorMarketing.getProgramEndDate())
							&& HolcimUtility.validDates(
									competitorMarketing.getPromotionStartDate(),
									competitorMarketing.getPromotionEndDate())) {

						competitorMarketing.setCompetitor(competitor);
						competitorMarketing.setCompetitorName(competitor
								.getName());

						Intent returnIntent = new Intent(
								HolcimCreateCompetitorMarketing.this,
								HolcimSelectedRetailerActivity.class);
						HolcimCustomActivity.setOnback(true);
						Bundle mBundle = new Bundle();
						mBundle.putSerializable(
								HolcimConsts.OBJECT_SERIALIZABLE_KEY,
								competitorMarketing);
						returnIntent.putExtras(mBundle);
						returnIntent.putExtra("isEditing", position);
						setResult(
								HolcimConsts.ACTIVITY_RESULT_CODE_COMP_MARK_OK,
								returnIntent);
						finish();
					} else if (!HolcimUtility.validDates(
							competitorMarketing.getProgramStartDate(),
							competitorMarketing.getProgramEndDate())) {
						dialog.showError(
								getString(R.string.error_invalid_program_date),
								getString(R.string.ok), new OnClickListener() {

									@Override
									public void onClick(View v) {
										adapter.resetCompetitor();
										adapter.notifyDataSetChanged();
										dialog.dismiss();
									}
								});
					} else if (!HolcimUtility.validDates(
							competitorMarketing.getPromotionStartDate(),
							competitorMarketing.getPromotionEndDate())) {
						dialog.showError(
								getString(R.string.error_invalid_promotion_date),
								getString(R.string.ok), new OnClickListener() {

									@Override
									public void onClick(View v) {
										adapter.resetCompetitor();
										adapter.notifyDataSetChanged();
										dialog.dismiss();
									}
								});
					} else {
						dialog.showError(
								getString(R.string.error_competitormarketing_competitor_name),
								getString(R.string.ok), new OnClickListener() {

									@Override
									public void onClick(View v) {
										adapter.resetCompetitor();
										adapter.notifyDataSetChanged();
										dialog.dismiss();
									}
								});
					}
				} else {
					dialog.showError(
							getString(R.string.error_competitormarketing_competitor_name_empty),
							getString(R.string.ok), new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				}
			}
		});

		setCustomTitle(getString(R.string.title_create_competitor_marketing));
	}

	private Competitor createCompetitorMarketing(
			HolcimCreateCompetitorMarketingAdapter adapter) {
		competitorMarketing = new CompetitorMarketing();
		return createOrUpdateCompetitorMarketing(adapter);
	}

	private Competitor createOrUpdateCompetitorMarketing(
			HolcimCreateCompetitorMarketingAdapter adapter) {
		Competitor competitor = null;
		if (!adapter.getCompetitorMarketing().get(1).equals("")) {
			competitor = HolcimApp.daoSession
					.getCompetitorDao()
					.queryBuilder()
					.where(CompetitorDao.Properties.Name.eq(adapter
							.getCompetitorMarketing().get(1))).list().get(0);
		}
		if (!adapter.getCompetitorMarketing().get(15).equals("")) {
			competitorMarketing.setIssue(adapter.getCompetitorMarketing().get(
					15));
		}
		if (!adapter.getCompetitorMarketing().get(2).equals("")) {
			competitorMarketing.setBuyingPrice(Double.valueOf(adapter
					.getCompetitorMarketing().get(2)));
		}
		if (!adapter.getCompetitorMarketing().get(3).equals("")) {
			competitorMarketing.setSellingPrice(Double.valueOf(adapter
					.getCompetitorMarketing().get(3)));
		}
		if (!adapter.getCompetitorMarketing().get(8).equals("")) {
			competitorMarketing.setCompetitorMarginHIL(Double.valueOf(adapter
					.getCompetitorMarketing().get(8)));
		}
		if (!adapter.getCompetitorMarketing().get(4).equals("")) {
			competitorMarketing.setInventory(Integer.parseInt(adapter
					.getCompetitorMarketing().get(4)));
		}
		if (!adapter.getCompetitorMarketing().get(5).equals("")) {
			competitorMarketing.setLastMonthCompetitorBuyingVolume(Double.parseDouble(adapter.getCompetitorMarketing().get(5)));
		}
		if (!adapter.getCompetitorMarketing().get(9).equals("")) {
			competitorMarketing.setPromotion(adapter.getCompetitorMarketing()
					.get(9));
		}
		if (!adapter.getCompetitorMarketing().get(10).equals("")) {
			competitorMarketing.setPromotionStartDate(adapter
					.getCompetitorMarketing().get(10));
		} else if (competitorMarketing.getPromotionStartDate() == null) {
			competitorMarketing.setPromotionStartDate(getTodayDate());
		}
		if (!adapter.getCompetitorMarketing().get(11).equals("")) {
			competitorMarketing.setPromotionEndDate(adapter
					.getCompetitorMarketing().get(11));
		} else if (competitorMarketing.getPromotionEndDate() == null) {
			competitorMarketing.setPromotionEndDate(getTodayDate());
		}
		if (!adapter.getCompetitorMarketing().get(12).equals("")) {
			competitorMarketing.setProgram(adapter.getCompetitorMarketing()
					.get(12));
		}
		if (!adapter.getCompetitorMarketing().get(13).equals("")) {
			competitorMarketing.setProgramStartDate(adapter
					.getCompetitorMarketing().get(13));
		} else if (competitorMarketing.getProgramStartDate() == null) {
			competitorMarketing.setProgramStartDate(getTodayDate());
		}
		if (!adapter.getCompetitorMarketing().get(14).equals("")) {
			competitorMarketing.setProgramEndDate(adapter
					.getCompetitorMarketing().get(14));
		} else if (competitorMarketing.getProgramEndDate() == null) {
			competitorMarketing.setProgramEndDate(getTodayDate());
		}
		return competitor;
	}

	private String getTodayDate() {
		Calendar c = Calendar.getInstance();
		return ("" + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1)
				+ "-" + c.get(Calendar.DAY_OF_MONTH));

	}

	private boolean existCompetitorMarketingWithCompetitorName(
			String competitorName) {
		for (int i = 0; i < competitorMarketings.size(); i++) {
			if (competitorMarketings.get(i).getCompetitorName()
					.equals(competitorName)
					&& (i != position)) {
				return true;
			}
		}
		return false;
	}

	public class HolcimCreateCompetitorMarketingAdapter extends BaseAdapter {

		Context context;
		private final ArrayList<String> fields;
		private final ArrayList<String> fieldsValues;
		ViewHolder viewHolder;
		private CompetitorMarketing competitorMarketing;

		public HolcimCreateCompetitorMarketingAdapter(String[] fields,
				Context context, CompetitorMarketing competitorMarketing) {
			this.context = context;
			this.fields = new ArrayList<String>();
			this.fieldsValues = new ArrayList<String>();
			this.competitorMarketing = competitorMarketing;

			for (String s : fields) {
				this.fields.add(s);
				this.fieldsValues.add("");
			}
		}

		public ArrayList<String> getCompetitorMarketing() {
			return this.fieldsValues;
		}

		public void resetCompetitor() {
			fieldsValues.set(5, "");
			viewHolder.spinnerCompetitor.setSelection(0);
		}

		@Override
		public int getCount() {
			return fields.size();
		}

		@Override
		public Object getItem(int position) {
			return fields.get(position);
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
				convertView = inflater.inflate(
						R.layout.create_competitor_marketing, parent, false);
				viewHolder = new ViewHolder();

				// cache the views
				viewHolder.spinnerCompetitor = (Spinner) convertView
						.findViewById(R.id.spinner_competitor);
				viewHolder.editTextText = (EditText) convertView
						.findViewById(R.id.editText_field_value_text);
				viewHolder.editTextNumber = (EditText) convertView
						.findViewById(R.id.editText_field_value_number);
				viewHolder.editTextDecimal = (EditText) convertView
						.findViewById(R.id.editText_field_value_decimal);
				viewHolder.datePicker = (DatePicker) convertView
						.findViewById(R.id.datePicker_field_value);
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.textView_field_value);

				final ArrayList<String> competitorList = new ArrayList<String>();
				competitorList.add("");
				for (Competitor competitor : HolcimApp.daoSession
						.getCompetitorDao().loadAll()) {
					competitorList.add(competitor.getName());
				}
				ArrayAdapter<String> adapterCompetitor = new ArrayAdapter<String>(
						context, R.layout.spinner, competitorList);
				viewHolder.spinnerCompetitor.setAdapter(adapterCompetitor);

				viewHolder.spinnerCompetitor
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								if (viewHolder.spinnerCompetitor.getAdapter() != null
										&& viewHolder.spinnerCompetitor
												.getAdapter().getItem(position) != null) {
									if (!competitorList.get(position)
											.equals("")) {
										fieldsValues.set(1,
												competitorList.get(position));
									}
									if (fieldsValues.get(1).equalsIgnoreCase(
											HolcimConsts.HOLCIM) && account.getLmbuyingvol()!= null
											) {
                                        fieldsValues.set(
                                                5,
                                                String.valueOf(account.getLmbuyingvol()));
									} else {
										fieldsValues.set(
												5,
												"");
									}
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
							}
						});

				// link the cached views to the convertview
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final TextView textViewFieldName = (TextView) convertView
					.findViewById(R.id.text_view_field_name);
			textViewFieldName.setText(fields.get(position));

			if (textViewFieldName.getText().equals(fields.get(9))
					|| textViewFieldName.getText().equals(fields.get(12))) {
				viewHolder.editTextText.setText(fieldsValues.get(fields
						.indexOf(textViewFieldName.getText())));
				viewHolder.editTextDecimal.setVisibility(View.GONE);
				viewHolder.editTextNumber.setVisibility(View.GONE);
				viewHolder.editTextText.setVisibility(View.VISIBLE);
			} else if (textViewFieldName.getText().equals(fields.get(4))
					|| textViewFieldName.getText().equals(fields.get(5))
					|| textViewFieldName.getText().equals(fields.get(15))) {
				viewHolder.editTextNumber.setText(fieldsValues.get(fields
						.indexOf(textViewFieldName.getText())));
				viewHolder.editTextText.setVisibility(View.GONE);
				viewHolder.editTextDecimal.setVisibility(View.GONE);
				viewHolder.editTextNumber.setVisibility(View.VISIBLE);
			} else if (textViewFieldName.getText().equals(fields.get(2))
					|| textViewFieldName.getText().equals(fields.get(3))
					|| textViewFieldName.getText().equals(fields.get(8))) {
				viewHolder.editTextDecimal.setText(fieldsValues.get(fields
						.indexOf(textViewFieldName.getText())));
				viewHolder.editTextText.setVisibility(View.GONE);
				viewHolder.editTextNumber.setVisibility(View.GONE);
				viewHolder.editTextDecimal.setVisibility(View.VISIBLE);
			}

			if (textViewFieldName.getText().equals(fields.get(0))
					|| textViewFieldName.getText().equals(fields.get(7))) {
				textViewFieldName.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						new Float("60"));
				textViewFieldName.setTextColor(getResources().getColor(
						R.color.holcim_red));
			} else {
				textViewFieldName.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						new Float("40"));
				textViewFieldName.setTextColor(getResources().getColor(
						R.color.black));
			}

			if (textViewFieldName.getText().equals(fields.get(0))
					|| textViewFieldName.getText().equals(fields.get(7))) {
				viewHolder.datePicker.setVisibility(View.GONE);
				viewHolder.editTextDecimal.setVisibility(View.GONE);
				viewHolder.editTextNumber.setVisibility(View.GONE);
				viewHolder.editTextText.setVisibility(View.GONE);
				viewHolder.spinnerCompetitor.setVisibility(View.GONE);
				viewHolder.textView.setVisibility(View.GONE);
			}

			if (textViewFieldName.getText().equals(fields.get(1))) {
				ArrayList<String> data = new ArrayList<String>();
				data.add("");
				for (Competitor competitor : HolcimApp.daoSession
						.getCompetitorDao().loadAll()) {
					data.add(competitor.getName());
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						context, R.layout.spinner, data);
				viewHolder.spinnerCompetitor.setAdapter(adapter);
				if (competitorMarketing != null) {
					if (!fieldsValues.get(1).equals("")) {
						viewHolder.spinnerCompetitor.setSelection(data
								.indexOf(fieldsValues.get(1)));
					} else if (competitorMarketing.getCompetitorName() != null) {
						viewHolder.spinnerCompetitor.setSelection(data
								.indexOf(competitorMarketing
										.getCompetitorName()));
						fieldsValues.set(1, data.get(data
								.indexOf(competitorMarketing
										.getCompetitorName())));
					}
				} else {
					viewHolder.spinnerCompetitor.setSelection(data
							.indexOf(fieldsValues.get(1)));
				}
				showSpinnerCompetitor();
			} else if (textViewFieldName.getText().equals(fields.get(10))) {
				if (competitorMarketing != null
						&& competitorMarketing.getPromotionStartDate() != null
						&& fieldsValues.get(10).equals("")) {
					String date = competitorMarketing.getPromotionStartDate();
					String[] separated = date.split("-");
					viewHolder.datePicker.updateDate(
							Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]));
					viewHolder.datePicker.init(Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]),
							new OnDateChangedListener() {

								@Override
								public void onDateChanged(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									fieldsValues.set(fields
											.indexOf(textViewFieldName
													.getText()), "" + year
											+ "-" + (monthOfYear + 1) + "-"
											+ dayOfMonth);
								}
							});
				} else if (fieldsValues.get(10).equals("")) {
					Calendar c = Calendar.getInstance();
					viewHolder.datePicker.init(c.get(Calendar.YEAR),
							c.get(Calendar.MONTH),
							c.get(Calendar.DAY_OF_MONTH),
							new OnDateChangedListener() {

								@Override
								public void onDateChanged(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									fieldsValues.set(fields
											.indexOf(textViewFieldName
													.getText()), "" + year
											+ "-" + (monthOfYear + 1) + "-"
											+ dayOfMonth);
								}
							});
				} else {
					String[] separated = fieldsValues.get(10).split("-");
					fieldsValues.set(
							fields.indexOf(textViewFieldName.getText()), ""
									+ Integer.parseInt(separated[0]) + "-"
									+ (Integer.parseInt(separated[1]) - 1)
									+ "-" + Integer.parseInt(separated[2]));
					viewHolder.datePicker.init(Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]),
							new OnDateChangedListener() {

								@Override
								public void onDateChanged(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									fieldsValues.set(fields
											.indexOf(textViewFieldName
													.getText()), "" + year
											+ "-" + (monthOfYear + 1) + "-"
											+ dayOfMonth);
								}
							});
				}
				showDatePicker();
			} else if (textViewFieldName.getText().equals(fields.get(14))) {
				if (competitorMarketing != null
						&& competitorMarketing.getProgramEndDate() != null
						&& fieldsValues.get(14).equals("")) {
					String date = competitorMarketing.getProgramEndDate();
					String[] separated = date.split("-");
					viewHolder.datePicker.updateDate(
							Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]));
					viewHolder.datePicker.init(Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]),
							new OnDateChangedListener() {

								@Override
								public void onDateChanged(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									fieldsValues.set(fields
											.indexOf(textViewFieldName
													.getText()), "" + year
											+ "-" + (monthOfYear + 1) + "-"
											+ dayOfMonth);
								}
							});
				} else if (fieldsValues.get(14).equals("")) {
					Calendar c = Calendar.getInstance();
					viewHolder.datePicker.init(c.get(Calendar.YEAR),
							c.get(Calendar.MONTH),
							c.get(Calendar.DAY_OF_MONTH),
							new OnDateChangedListener() {

								@Override
								public void onDateChanged(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									fieldsValues.set(fields
											.indexOf(textViewFieldName
													.getText()), "" + year
											+ "-" + (monthOfYear + 1) + "-"
											+ dayOfMonth);
								}
							});
				} else {
					String[] separated = fieldsValues.get(14).split("-");
					viewHolder.datePicker.init(Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]),
							new OnDateChangedListener() {

								@Override
								public void onDateChanged(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									fieldsValues.set(fields
											.indexOf(textViewFieldName
													.getText()), "" + year
											+ "-" + (monthOfYear + 1) + "-"
											+ dayOfMonth);
								}
							});
				}
				showDatePicker();

			} else if (textViewFieldName.getText().equals(fields.get(6))) {
				if (saleExecution != null
						&& saleExecution.getHilBuyingVolumeDate() != null) {
					String date = saleExecution.getHilBuyingVolumeDate();
					String[] separated = date.split("-");
					viewHolder.datePicker.updateDate(
							Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]));
				}
				showDatePicker();
				viewHolder.datePicker.setEnabled(false);
			} else if (textViewFieldName.getText().equals(fields.get(13))) {
				if (competitorMarketing != null
						&& competitorMarketing.getProgramStartDate() != null
						&& fieldsValues.get(13).equals("")) {
					String date = competitorMarketing.getProgramStartDate();
					String[] separated = date.split("-");
					viewHolder.datePicker.updateDate(
							Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]));
					viewHolder.datePicker.init(Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]),
							new OnDateChangedListener() {

								@Override
								public void onDateChanged(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									fieldsValues.set(fields
											.indexOf(textViewFieldName
													.getText()), "" + year
											+ "-" + (monthOfYear + 1) + "-"
											+ dayOfMonth);
								}
							});
				} else if (fieldsValues.get(13).equals("")) {
					Calendar c = Calendar.getInstance();
					viewHolder.datePicker.init(c.get(Calendar.YEAR),
							c.get(Calendar.MONTH),
							c.get(Calendar.DAY_OF_MONTH),
							new OnDateChangedListener() {

								@Override
								public void onDateChanged(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									fieldsValues.set(fields
											.indexOf(textViewFieldName
													.getText()), "" + year
											+ "-" + (monthOfYear + 1) + "-"
											+ dayOfMonth);
								}
							});
				} else {
					String[] separated = fieldsValues.get(13).split("-");
					viewHolder.datePicker.init(Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]),
							new OnDateChangedListener() {

								@Override
								public void onDateChanged(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									fieldsValues.set(fields
											.indexOf(textViewFieldName
													.getText()), "" + year
											+ "-" + (monthOfYear + 1) + "-"
											+ dayOfMonth);
								}
							});
				}
				showDatePicker();
			} else if (textViewFieldName.getText().equals(fields.get(11))) {
				if (competitorMarketing != null
						&& competitorMarketing.getPromotionEndDate() != null
						&& fieldsValues.get(11).equals("")) {
					String date = competitorMarketing.getPromotionEndDate();
					String[] separated = date.split("-");
					viewHolder.datePicker.updateDate(
							Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]));
					viewHolder.datePicker.init(Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]),
							new OnDateChangedListener() {

								@Override
								public void onDateChanged(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									fieldsValues.set(fields
											.indexOf(textViewFieldName
													.getText()), "" + year
											+ "-" + (monthOfYear + 1) + "-"
											+ dayOfMonth);
								}
							});
				} else if (fieldsValues.get(11).equals("")) {
					Calendar c = Calendar.getInstance();
					viewHolder.datePicker.init(c.get(Calendar.YEAR),
							c.get(Calendar.MONTH),
							c.get(Calendar.DAY_OF_MONTH),
							new OnDateChangedListener() {

								@Override
								public void onDateChanged(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									fieldsValues.set(fields
											.indexOf(textViewFieldName
													.getText()), "" + year
											+ "-" + (monthOfYear + 1) + "-"
											+ dayOfMonth);
								}
							});
				} else {
					String[] separated = fieldsValues.get(11).split("-");
					viewHolder.datePicker.init(Integer.parseInt(separated[0]),
							Integer.parseInt(separated[1]) - 1,
							Integer.parseInt(separated[2]),
							new OnDateChangedListener() {

								@Override
								public void onDateChanged(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									fieldsValues.set(fields
											.indexOf(textViewFieldName
													.getText()), "" + year
											+ "-" + (monthOfYear + 1) + "-"
											+ dayOfMonth);
								}
							});
				}
				showDatePicker();
			} else if (textViewFieldName.getText().equals(fields.get(2))) {
				if (competitorMarketing != null
						&& competitorMarketing.getBuyingPrice() != null
						&& fieldsValues.get(
								fields.indexOf(textViewFieldName.getText()))
								.equals("")) {
					viewHolder.editTextDecimal.setText(String
							.valueOf(competitorMarketing.getBuyingPrice()));
					fieldsValues.set(2, String.valueOf(competitorMarketing
							.getBuyingPrice()));
				}
				showEditTextDecimal();
			} else if (textViewFieldName.getText().equals(fields.get(3))) {
				if (competitorMarketing != null
						&& competitorMarketing.getSellingPrice() != null
						&& fieldsValues.get(
								fields.indexOf(textViewFieldName.getText()))
								.equals("")) {
					viewHolder.editTextDecimal.setText(String
							.valueOf(competitorMarketing.getSellingPrice()));
					fieldsValues.set(3, String.valueOf(competitorMarketing
							.getSellingPrice()));
				}
				showEditTextDecimal();
			} else if (textViewFieldName.getText().equals(fields.get(8))) {
				if (competitorMarketing != null
						&& competitorMarketing.getCompetitorMarginHIL() != null
						&& fieldsValues.get(
								fields.indexOf(textViewFieldName.getText()))
								.equals("")) {
					viewHolder.editTextDecimal.setText(String
							.valueOf(competitorMarketing
									.getCompetitorMarginHIL()));
					fieldsValues.set(8, String.valueOf(competitorMarketing
							.getCompetitorMarginHIL()));
				}
				showEditTextDecimal();
			} else if (textViewFieldName.getText().equals(fields.get(4))) {
				if (competitorMarketing != null
						&& competitorMarketing.getInventory() != null
						&& fieldsValues.get(
								fields.indexOf(textViewFieldName.getText()))
								.equals("")) {
					fieldsValues.set(4,
							String.valueOf(competitorMarketing.getInventory()));
					viewHolder.editTextNumber.setText(String
							.valueOf(competitorMarketing.getInventory()));
				}
				showEditTextNumber();
			} else if (textViewFieldName.getText().equals(fields.get(5))) {
				if (fieldsValues.get(1).equalsIgnoreCase(HolcimConsts.HOLCIM)
						&& saleExecution != null
						&& saleExecution.getHilBuyingVolume() != null) {
					viewHolder.editTextNumber.setText(String
							.valueOf(saleExecution.getHilBuyingVolume()));
				} else if (competitorMarketing != null
						&& competitorMarketing
								.getLastMonthCompetitorBuyingVolume() != null
						&& fieldsValues.get(
								fields.indexOf(textViewFieldName.getText()))
								.equals("")) {
					viewHolder.editTextNumber.setText(String
							.valueOf(competitorMarketing
									.getLastMonthCompetitorBuyingVolume()));
					fieldsValues.set(5, String.valueOf(competitorMarketing
							.getLastMonthCompetitorBuyingVolume()));
					showEditTextNumber();
				}
				showEditTextNumber();
			} else if (textViewFieldName.getText().equals(fields.get(9))) {
				if (competitorMarketing != null
						&& competitorMarketing.getPromotion() != null
						&& fieldsValues.get(
								fields.indexOf(textViewFieldName.getText()))
								.equals("")) {
					viewHolder.editTextText.setText(competitorMarketing
							.getPromotion());
					fieldsValues.set(9,
							String.valueOf(competitorMarketing.getPromotion()));
				}
				showEditTextText();
			} else if (textViewFieldName.getText().equals(fields.get(12))) {
				if (competitorMarketing != null
						&& competitorMarketing.getProgram() != null
						&& fieldsValues.get(
								fields.indexOf(textViewFieldName.getText()))
								.equals("")) {
					viewHolder.editTextText.setText(competitorMarketing
							.getProgram());
					fieldsValues.set(12,
							String.valueOf(competitorMarketing.getProgram()));
				}
				showEditTextText();
			} else if (textViewFieldName.getText().equals(fields.get(15))) {
				if (competitorMarketing != null
						&& competitorMarketing.getIssue() != null
						&& fieldsValues.get(
								fields.indexOf(textViewFieldName.getText()))
								.equals("")) {
					viewHolder.editTextText.setText(competitorMarketing
							.getIssue());
					fieldsValues.set(15,
							String.valueOf(competitorMarketing.getIssue()));
				} else if (!fieldsValues.get(
						fields.indexOf(textViewFieldName.getText())).equals("")) {
					viewHolder.editTextText.setText(fieldsValues.get(fields
							.indexOf(textViewFieldName.getText())));
				} else if (fieldsValues.get(
						fields.indexOf(textViewFieldName.getText())).equals("")) {
					viewHolder.editTextText.setText("");
				}
				showEditTextText();
			}

			viewHolder.editTextText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (s.length() > 0) {
						fieldsValues.set(
								fields.indexOf(textViewFieldName.getText()),
								s.toString());
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});

			viewHolder.editTextNumber.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (s.length() > 0) {
						fieldsValues.set(
								fields.indexOf(textViewFieldName.getText()),
								s.toString());
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});

			viewHolder.editTextDecimal
					.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							if (s.length() > 0) {
								fieldsValues.set(fields
										.indexOf(textViewFieldName.getText()),
										s.toString());
							}
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});

			return convertView;
		}

		// class for caching the views in a row
		private class ViewHolder {
			Spinner spinnerCompetitor;
			EditText editTextText;
			EditText editTextNumber;
			EditText editTextDecimal;
			DatePicker datePicker;
			TextView textView;
		}

		public void showSpinnerExperianceWithLeadCompetitors() {
			viewHolder.spinnerCompetitor.setVisibility(View.GONE);
			viewHolder.editTextText.setVisibility(View.GONE);
			viewHolder.editTextNumber.setVisibility(View.GONE);
			viewHolder.editTextDecimal.setVisibility(View.GONE);
			viewHolder.datePicker.setVisibility(View.GONE);
			viewHolder.textView.setVisibility(View.GONE);
		}

		public void showSpinnerReasonForBuyingFromLeadCompetitors() {
			viewHolder.spinnerCompetitor.setVisibility(View.GONE);
			viewHolder.editTextText.setVisibility(View.GONE);
			viewHolder.editTextNumber.setVisibility(View.GONE);
			viewHolder.editTextDecimal.setVisibility(View.GONE);
			viewHolder.datePicker.setVisibility(View.GONE);
			viewHolder.textView.setVisibility(View.GONE);
		}

		public void showSpinnerCompetitor() {
			viewHolder.spinnerCompetitor.setVisibility(View.VISIBLE);
			viewHolder.editTextText.setVisibility(View.GONE);
			viewHolder.editTextNumber.setVisibility(View.GONE);
			viewHolder.editTextDecimal.setVisibility(View.GONE);
			viewHolder.datePicker.setVisibility(View.GONE);
			viewHolder.textView.setVisibility(View.GONE);
		}

		public void showEditTextText() {
			viewHolder.spinnerCompetitor.setVisibility(View.GONE);
			viewHolder.editTextText.setVisibility(View.VISIBLE);
			viewHolder.editTextNumber.setVisibility(View.GONE);
			viewHolder.editTextDecimal.setVisibility(View.GONE);
			viewHolder.datePicker.setVisibility(View.GONE);
			viewHolder.textView.setVisibility(View.GONE);
		}

		public void showEditTextNumber() {
			viewHolder.spinnerCompetitor.setVisibility(View.GONE);
			viewHolder.editTextText.setVisibility(View.GONE);
			viewHolder.editTextDecimal.setVisibility(View.GONE);
			viewHolder.datePicker.setVisibility(View.GONE);
			viewHolder.textView.setVisibility(View.GONE);
			viewHolder.editTextNumber.setVisibility(View.VISIBLE);
		}

		public void showEditTextDecimal() {
			viewHolder.spinnerCompetitor.setVisibility(View.GONE);
			viewHolder.editTextText.setVisibility(View.GONE);
			viewHolder.editTextNumber.setVisibility(View.GONE);
			viewHolder.editTextDecimal.setVisibility(View.VISIBLE);
			viewHolder.datePicker.setVisibility(View.GONE);
			viewHolder.textView.setVisibility(View.GONE);
		}

		public void showDatePicker() {
			viewHolder.spinnerCompetitor.setVisibility(View.GONE);
			viewHolder.editTextText.setVisibility(View.GONE);
			viewHolder.editTextNumber.setVisibility(View.GONE);
			viewHolder.editTextDecimal.setVisibility(View.GONE);
			viewHolder.datePicker.setVisibility(View.VISIBLE);
			viewHolder.textView.setVisibility(View.GONE);
			viewHolder.datePicker.setEnabled(true);
		}

		public void showTextView() {
			viewHolder.spinnerCompetitor.setVisibility(View.GONE);
			viewHolder.editTextText.setVisibility(View.GONE);
			viewHolder.editTextNumber.setVisibility(View.GONE);
			viewHolder.editTextDecimal.setVisibility(View.GONE);
			viewHolder.datePicker.setVisibility(View.GONE);
			viewHolder.textView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onBackPressed() {
		if (!HolcimCustomActivity.blockback) {
			HolcimCustomActivity.setOnback(true);
			Intent returnIntent = new Intent(
					HolcimCreateCompetitorMarketing.this,
					HolcimSelectedRetailerActivity.class);
			setResult(-1, returnIntent);
			finish();
		}
	}

}
