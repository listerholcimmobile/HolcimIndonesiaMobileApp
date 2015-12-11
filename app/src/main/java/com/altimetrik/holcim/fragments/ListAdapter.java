package com.altimetrik.holcim.fragments;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.holcim.hsea.R;

public class ListAdapter extends BaseAdapter {

	Context context;
	@SuppressWarnings("rawtypes")
	private final ArrayList values;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ListAdapter(Map<String, String> valuesData, Context context) {
		this.context = context;
		this.values = new ArrayList();
		this.values.addAll(valuesData.entrySet());
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getItem(int position) {
		return (Map.Entry) values.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View valuesView;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			valuesView = inflater.inflate(R.layout.fragment_list_row, parent,
					false);
		} else {
			valuesView = (View) convertView;
		}

		TextView textViewField = (TextView) valuesView
				.findViewById(R.id.textView_field);
		TextView textViewFieldInfo = (TextView) valuesView
				.findViewById(R.id.textView_field_info);

		@SuppressWarnings("unchecked")
		Map.Entry<String, String> item = (Entry<String, String>) getItem(position);
		textViewField.setText(item.getKey());
		textViewFieldInfo.setText(item.getValue());

		return valuesView;
	}

}