package com.example.com.socialnetwork;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.socialnetwork.model.Friendship;
import com.example.com.socialnetwork.model.Snippet;
import com.example.com.socialnetwork.model.User;
import com.example.com.socialnetwork.view.DurationTextView;
import com.example.com.socialnetwork.ws.WebService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shobhit on 4/10/16.
 */
public class FriendFragment extends Fragment {

	private static class FriendList{
		public User user;
		public Status status;
		public enum Status{
			Friend, Sent, Received, Unknown
		}
		public FriendList(User user, Status status){
			this.user = user;
			this.status = status;
		}
	}

	public static String LOG_TAG = "My log tag";

	private MyAdapter aa;
	private ArrayList<FriendList> aList;
	private ViewGroup container;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.container = container;
		View v = inflater.inflate(R.layout.friend_list, container, false);
//		Button button = (Button)v.findViewById(R.id.btn_post_message);
//		button.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				postMessage(v);
//			}
//		});
		return v;
	}

	@Override
	public void onResume(){
		super.onResume();
		aList = new ArrayList<>();
		aa = new MyAdapter(getActivity(), R.layout.list_element_friend, aList);
		ListView myListView = (ListView) container.findViewById(R.id.listViewFriend);
		myListView.setAdapter(aa);
		aa.notifyDataSetChanged();
		getFriendship();
	}

	private void getFriendship(){

		WebService webService = new WebService(getActivity());
		Call<List<Friendship>> listCall = webService.friendService.getFriendRequests();
		listCall.enqueue(new Callback<List<Friendship>>() {
			@Override
			public void onResponse(Response<List<Friendship>> response) {
				ApplicationData.getInstance().setFriendshipList(response.body());
				aList.clear();

				List<User> friends = ApplicationData.getInstance().getFriends();
				List<User> friend_sent = ApplicationData.getInstance().friendRequestSent();
				List<User> friends_received = ApplicationData.getInstance().friendRequestRecieved();
				List<User> unknown = ApplicationData.getInstance().getUnknown();
				for (int i = 0; i < friends.size(); i++) {
					aList.add(new FriendList(friends.get(i), FriendList.Status.Friend));
				}
				for (int i = 0; i < friend_sent.size(); i++) {
					aList.add(new FriendList(friend_sent.get(i), FriendList.Status.Sent));
				}
				for (int i = 0; i < friends_received.size(); i++) {
					aList.add(new FriendList(friends_received.get(i), FriendList.Status.Received));
				}
				for (int i = 0; i < unknown.size(); i++) {
					aList.add(new FriendList(unknown.get(i), FriendList.Status.Unknown));
				}
				// We notify the ArrayList adapter that the underlying list has changed,
				// triggering a re-rendering of the list.
				aa.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Throwable t) {

			}
		});


	}

	public void sendRequest(View view){
		Button button = (Button) view;
		WebService webService = new WebService(getActivity());

		Friendship friendship = new Friendship();
		friendship.setFriend((Integer) button.getTag());

		switch (button.getText().toString()){
			case "Send Request":
			case "Approve":{
				Call<Friendship> call = webService.friendService.postFriendRequest(friendship);
				call.enqueue(new Callback<Friendship>() {
					@Override
					public void onResponse(Response<Friendship> response) {
						getFriendship();
					}

					@Override
					public void onFailure(Throwable t) {

					}
				});
				break;
			}
			case "Unfriend":
			case "Withdraw":
			default:
				Toast.makeText(getActivity(), "Functionality not implmented", Toast.LENGTH_SHORT).show();
		}
	}

	public class MyAdapter extends ArrayAdapter<FriendList> {

		int resource;
		Context context;

		public MyAdapter(Context _context, int _resource, List<FriendList> items) {
			super(_context, _resource, items);
			resource = _resource;
			context = _context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout newView;

			FriendList w = getItem(position);

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
			TextView nameTV = (TextView) newView.findViewById(R.id.item_name);
			nameTV.setText(w.user.getName());
			TextView statusTV = (TextView) newView.findViewById(R.id.item_status);
			statusTV.setText(w.status.name());

			Button button = (Button) newView.findViewById(R.id.button);
			button.setTag(w.user.getId());
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					sendRequest(v);
				}
			});
			switch (w.status){
				case Unknown:
//					button.setEnabled(true);
					button.setText("Send Request");
					break;
				case Friend:
//					button.setEnabled(false);
					button.setText("Unfriend");
//					button.setVisibility(View.GONE);
					break;
				case Received:
//					button.setEnabled(true);
					button.setText("Approve");
					break;
				case Sent:
					button.setText("Withdraw");
					break;
				default:
					button.setEnabled(false);
			}
			return newView;
		}
	}
}