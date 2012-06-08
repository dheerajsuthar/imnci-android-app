package org.dheeraj.imnci;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class UpdatesAdapter extends ArrayAdapter<Updates> {
	List<Updates> updates;
	int rowViewId;
	Context context;

	public UpdatesAdapter(Context context, int textViewResourceId,
			List<Updates> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		rowViewId = textViewResourceId;
		updates = objects;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return updates.size();
	}

	@Override
	public Updates getItem(int position) {
		// TODO Auto-generated method stub
		return updates.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			View row = new View(context);
			LayoutInflater lInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = lInflater.inflate(rowViewId, parent, false);
			TextView tvTitle = (TextView) row
					.findViewById(R.id.textUpdateTitle);
			tvTitle.setText(updates.get(position).getTitle());
			TextView tvMessage = (TextView) row
					.findViewById(R.id.textUpdateMessage);
			tvMessage.setText(updates.get(position).getMessage());
			return row;

		} else {
			return convertView;
		}
	}

}