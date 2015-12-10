package com.holcim.altimetrik.android.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.model.Contact;
import com.holcim.altimetrik.android.utilities.IPredicate;
import com.holcim.altimetrik.android.utilities.Predicate;

public class HolcimContactActivity extends HolcimCustomActivity {
	private HolcimContactsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestCustomTitle();
		setContentView(R.layout.contact_list);
		super.onCreate(savedInstanceState);

		Button btnCreateNewContact = (Button) findViewById(R.id.button_create_new_contact);
		btnCreateNewContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HolcimContactActivity.this,
						HolcimCreateNewContactActivity.class);
				startActivity(intent);
			}
		});

		final ListView lvContacts = (ListView) findViewById(R.id.list_view_contacts);
		final List<Contact> contactList = HolcimApp.daoSession.getContactDao()
				.loadAll();
		adapter = new HolcimContactsAdapter(contactList, this);
		lvContacts.setAdapter(adapter);

		ImageView homePage = (ImageView) findViewById(R.id.imageButton_home);
		homePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				HolcimCustomActivity.setOnback(true);
				Intent intent = new Intent(HolcimContactActivity.this,
						HolcimMainActivity.class);
				startActivity(intent);
			}
		});

		final EditText etSearch = (EditText) findViewById(R.id.edittext_search);
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (etSearch.getText().length() != 0) {
					List<Contact> list = filterContactList(contactList,
							etSearch.getText().toString().toLowerCase());
					adapter = new HolcimContactsAdapter(list,
							HolcimContactActivity.this);

				} else {
					adapter = new HolcimContactsAdapter(contactList,
							HolcimContactActivity.this);
				}
				lvContacts.setAdapter(adapter);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		setCustomTitle(getResources().getString(R.string.contacts));
	}

	public class HolcimContactsAdapter extends BaseAdapter {
		Context context;
		private final List<Contact> contactList;

		public HolcimContactsAdapter(List<Contact> contacts, Context context) {
			this.context = context;
			this.contactList = contacts;
		}

		@Override
		public int getCount() {
			return contactList.size();
		}

		@Override
		public Object getItem(int position) {
			return contactList.get(position);
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
				convertView = inflater.inflate(R.layout.contact_list_row,
						parent, false);
			} else {
				convertView = (View) convertView;
			}

			final TextView textViewContactName = (TextView) convertView
					.findViewById(R.id.textView_contact_name);
			String toShow = "";
			if (contactList.get(position).getFirstName() != null) {
				toShow += contactList.get(position).getFirstName();
			}
			if (contactList.get(position).getLastName() != null) {
				if (toShow.equals("")) {
					toShow += contactList.get(position).getLastName();
				} else {
					toShow += " " + contactList.get(position).getLastName();
				}
			}
			if (contactList.get(position).getAccountName() != null) {
				toShow += " - Account: "
						+ contactList.get(position).getAccountName();
			}
			textViewContactName.setText(toShow);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(HolcimContactActivity.this,
							HolcimShowContactInfo.class);
					intent.putExtra("contactId", contactList.get(position)
							.getId());
					startActivity(intent);
				}
			});

			return convertView;
		}

	}

	public static List<Contact> filterContactList(List<Contact> list,
			final String filter) {
		List<Contact> filteredList = (ArrayList<Contact>) Predicate.filter(
				list, new IPredicate<Contact>() {
					public boolean apply(Contact detail) {
						Predicate.predicateParams = detail;
						boolean ret = false;
						ret = (detail.getFirstName() != null && detail
								.getFirstName().toLowerCase()
								.contains(filter.toLowerCase()))
								|| (detail.getLastName() != null && detail
										.getLastName().toLowerCase()
										.contains(filter.toLowerCase()));
						return ret;
					}
				});
		if (filteredList == null) {
			filteredList = new ArrayList<Contact>();
		}
		return filteredList;
	}

	@Override
	public void onBackPressed() {
		if (!HolcimCustomActivity.blockback) {
			HolcimCustomActivity.setOnback(true);
			Intent intent = new Intent(HolcimContactActivity.this,
					HolcimMainActivity.class);
			startActivity(intent);
		}
	}
}
