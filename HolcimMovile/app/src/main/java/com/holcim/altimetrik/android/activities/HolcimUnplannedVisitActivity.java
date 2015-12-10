package com.holcim.altimetrik.android.activities;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.model.Account;
import com.holcim.altimetrik.android.model.AccountDao;
import com.holcim.altimetrik.android.model.Prospect;
import com.holcim.altimetrik.android.model.ProspectDao;

public class HolcimUnplannedVisitActivity extends HolcimCustomActivity{

	private boolean teleSale = false;
	private String reason;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.create_visit_plan);
		super.onCreate(savedInstanceState);

		ListView listViewAccounts = (ListView)findViewById(R.id.list_view_accounts);
		List<Account> accountList = HolcimApp.daoSession.getAccountDao().queryBuilder()
				.where(AccountDao.Properties.Name.notEq(""))
				.orderAsc(AccountDao.Properties.Name)
				.list();
		HolcimCreateVisitPlanAccountAdapter adapterAccounts = new HolcimCreateVisitPlanAccountAdapter(accountList, this);
		listViewAccounts.setAdapter(adapterAccounts);

		ListView listViewProspects = (ListView)findViewById(R.id.list_view_prospects);
		List<Prospect> prospectList = HolcimApp.daoSession.getProspectDao().queryBuilder()
				.where(ProspectDao.Properties.Name.notEq(""))
				.orderAsc(ProspectDao.Properties.Name)
				.list();
		HolcimCreateVisitPlanProspectAdapter adapterProspects = new HolcimCreateVisitPlanProspectAdapter(prospectList, this);
		listViewProspects.setAdapter(adapterProspects);

		if (getIntent().getExtras() != null) {
			teleSale = getIntent().getExtras().getBoolean("tele_sale");
			reason = getIntent().getExtras().getString("reason");
		}	

		if (teleSale) {
			setCustomTitle(getResources().getString(R.string.tele_sale_title));
			listViewProspects.setVisibility(View.INVISIBLE);
			LinearLayout linearLayoutProspects = (LinearLayout)findViewById(R.id.linear_layout_header_prospects);
			linearLayoutProspects.setVisibility(View.INVISIBLE);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			//params.setMargins(45, 0, 45, 45);
			listViewAccounts.setLayoutParams(params);
		} else {
			setCustomTitle(getResources().getString(R.string.unplanned_visit));
		}
	}

	public class HolcimCreateVisitPlanAccountAdapter extends BaseAdapter {
		Context context;
		private final List<Account> fields;

		public HolcimCreateVisitPlanAccountAdapter(List<Account> fields, Context context) {
			this.context = context;
			this.fields = fields;
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.create_visit_plan_row, parent, false);				
			} else {
				convertView = (View)convertView;
			}

			final TextView textViewFieldName = (TextView)convertView.findViewById(R.id.text_view_field_name);
			textViewFieldName.setText(fields.get(position).getName());

			convertView.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					if (teleSale) {
						Intent intent = new Intent(HolcimUnplannedVisitActivity.this, HolcimCreatePreorderActivity.class);
						intent.putExtra("accountId", fields.get(position).getId());
						intent.putExtra("isAccount", true);
						intent.putExtra("tele_sale", true);
						startActivity(intent);
					} else {
						Intent intent = new Intent(HolcimUnplannedVisitActivity.this, HolcimSelectedRetailerActivity.class);
						intent.putExtra("accountId", fields.get(position).getId());
						intent.putExtra("isAccount", true);
						intent.putExtra("tele_sale", false);
						intent.putExtra("reason", reason);
						startActivity(intent);
					}
				}
			});

			return convertView; 
		}
	}

	public class HolcimCreateVisitPlanProspectAdapter extends BaseAdapter {
		Context context;
		private final List<Prospect> fields;

		public HolcimCreateVisitPlanProspectAdapter(List<Prospect> fields, Context context) {
			this.context = context;
			this.fields = fields;
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.create_visit_plan_row, parent, false);
			} else {
				convertView = (View)convertView;
			}

			final TextView textViewFieldName = (TextView)convertView.findViewById(R.id.text_view_field_name);
			textViewFieldName.setText(fields.get(position).getName());

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (teleSale) {
						Intent intent = new Intent(HolcimUnplannedVisitActivity.this, HolcimCreatePreorderActivity.class);
						intent.putExtra("prospectId", fields.get(position).getId());
						intent.putExtra("isAccount", false);
						intent.putExtra("tele_sale", true);
						startActivity(intent);
					} else {
						Intent intent = new Intent(HolcimUnplannedVisitActivity.this, HolcimSelectedRetailerActivity.class);
						intent.putExtra("prospectId", fields.get(position).getId());
						intent.putExtra("tele_sale", false);
						intent.putExtra("isAccount", false);
						intent.putExtra("reason", reason);
						startActivity(intent);
					}
				}
			});

			return convertView; 
		}
	}

	@Override
	public void onBackPressed() {
		if(!HolcimCustomActivity.blockback){
			HolcimCustomActivity.setOnback(true);
			if (teleSale) {
				Intent intent = new Intent(HolcimUnplannedVisitActivity.this, HolcimMainActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(HolcimUnplannedVisitActivity.this, HolcimMyVisitPlanListActivity.class);
				startActivity(intent);
			}
			super.onBackPressed();
		}
	}
}
