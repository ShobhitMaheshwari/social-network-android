package com.example.com.socialnetwork;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.com.socialnetwork.model.Snippet;
import com.example.com.socialnetwork.ws.WebService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shobhit on 4/10/16.
 */
public class FeedFragment extends Fragment {

	private MyAdapter aa;
	private ArrayList<Snippet> aList;
	private ViewGroup container;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.container = container;
		View v = inflater.inflate(R.layout.feed, container, false);
		Button button = (Button)v.findViewById(R.id.btn_post_message);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				postMessage(v);
			}
		});
		return v;
	}

	@Override
	public void onResume(){
		super.onResume();
		aList = new ArrayList<>();
		aa = new MyAdapter(getActivity(), R.layout.list_element_snippet, aList);
		ListView myListView = (ListView) container.findViewById(R.id.listView);
		myListView.setAdapter(aa);
		aa.notifyDataSetChanged();
		getFeed();
	}

	private void getFeed(){
		WebService webService = new WebService(getActivity());
		Call<List<Snippet>> queryResponseCall = webService.snippetService.getSnippets();
		queryResponseCall.enqueue(new Callback<List<Snippet>>() {
			@Override
			public void onResponse(Response<List<Snippet>> response) {
				List<Snippet> snippets = response.body();

				Collections.sort(snippets, new Comparator<Snippet>() {
					public int compare(Snippet o1, Snippet o2) {
						return o2.getStarttime().compareTo(o1.getStarttime());
					}
				});

				aList.clear();
				for (int i = 0; i < snippets.size(); i++) {
					aList.add(snippets.get(i));
				}

				// We notify the ArrayList adapter that the underlying list has changed,
				// triggering a re-rendering of the list.
				aa.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Throwable t) {
				// Log error here since request failed
			}
		});
	}

	public void postMessage(View view){
		final EditText title = (EditText) this.container.findViewById(R.id.input_title);
		final EditText message = (EditText) this.container.findViewById(R.id.input_message);

		if((!title.getText().toString().isEmpty()) && (!message.getText().toString().isEmpty())){
			WebService webService = new WebService(getActivity());
			Snippet snippet = new Snippet();
			snippet.setMessage(message.getText().toString());
			snippet.setTitle(title.getText().toString());
			Call<Snippet> call = webService.snippetService.postSnippet(snippet);
			call.enqueue(new Callback<Snippet>() {
				@Override
				public void onResponse(Response<Snippet> response) {
					Snippet snippet1 = response.body();
					aList.add(snippet1);
					Collections.sort(aList, new Comparator<Snippet>() {
						public int compare(Snippet o1, Snippet o2) {
							return o2.getStarttime().compareTo(o1.getStarttime());
						}
					});
					aa.notifyDataSetChanged();
					title.setText("");
					message.setText("");
				}

				@Override
				public void onFailure(Throwable t) {

				}
			});
		}
	}
}
