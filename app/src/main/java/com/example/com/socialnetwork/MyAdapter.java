package com.example.com.socialnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.com.socialnetwork.model.Snippet;
import com.example.com.socialnetwork.view.DurationTextView;

import java.util.List;

/**
 * Created by shobhit on 4/9/16.
 */
public class MyAdapter extends ArrayAdapter<Snippet> {

	int resource;
	Context context;

	public MyAdapter(Context _context, int _resource, List<Snippet> items) {
		super(_context, _resource, items);
		resource = _resource;
		context = _context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout newView;

		Snippet w = getItem(position);

		// Inflate a new view if necessary.
		if (convertView == null) {
			newView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource,  newView, true);
		} else {
			newView = (LinearLayout) convertView;
		}

		// Fills in the view.

		TextView messageTV = (TextView) newView.findViewById(R.id.itemMessage);
		TextView nameTV = (TextView) newView.findViewById(R.id.itemName);
		DurationTextView dateTV = (DurationTextView) newView.findViewById(R.id.itemDate);
		TextView titleTV = (TextView) newView.findViewById(R.id.itemTitle);
		messageTV.setText(w.getMessage());
		nameTV.setText(ApplicationData.getInstance().getUser(w.getOwner()).getName());
		dateTV.setDuration(w.getStarttime());
		titleTV.setText(w.getTitle());

		return newView;
	}
}