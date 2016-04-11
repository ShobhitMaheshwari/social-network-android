package com.example.com.socialnetwork;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.com.socialnetwork.model.Snippet;
import com.example.com.socialnetwork.model.User;
import com.example.com.socialnetwork.ws.UserService;
import com.example.com.socialnetwork.ws.UserServiceInterface;
import com.example.com.socialnetwork.ws.WebService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UserServiceInterface {

	public static String LOG_TAG = "My log tag";

	private Toolbar toolbar;
	private MenuItem mSearchAction;
	private boolean isSearchOpened = false;
	private EditText edtSeach;
	private MyAdapter aa;
	private ArrayList<Snippet> aList;

	private DrawerLayout mDrawerLayout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(myToolbar);

		final ActionBar ab = getSupportActionBar();
		ab.setHomeAsUpIndicator(R.drawable.ic_menu);
		ab.setDisplayHomeAsUpEnabled(true);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		if (navigationView != null) {
			setupDrawerContent(navigationView);
		}

	}

	@Override
	protected void onResume(){

		SharedPreferences prefs = getSharedPreferences("your_file_name", MODE_PRIVATE);
		if(!prefs.getBoolean("loggedin", false)){
			Log.i(LOG_TAG, "Going to logging activity default");
			switchActivity(LoginActivity.class);
		}
		aList = new ArrayList<Snippet>();
		aa = new MyAdapter(this, R.layout.list_element_snippet, aList);
		ListView myListView = (ListView) findViewById(R.id.listView);
		myListView.setAdapter(aa);
		aa.notifyDataSetChanged();
		UserService.getCurrentUser(getApplicationContext());
		UserService.getAllUsers(getApplicationContext());

		super.onResume();
	}

	private void getFeed(){
		WebService webService = new WebService(getApplicationContext());
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		switch (id){
			case R.id.action_logout:{
				SharedPreferences.Editor editor = getSharedPreferences("your_file_name", MODE_PRIVATE).edit();
				editor.putBoolean("loggedin", false);
				editor.remove("email");
				editor.remove("password");
				editor.commit();
				Log.i(LOG_TAG, "Changing Activity to main after logging");
				switchActivity(LoginActivity.class);
				return true;
			}
			case R.id.action_search:
				handleMenuSearch();
				return true;
			case android.R.id.home:
				mDrawerLayout.openDrawer(GravityCompat.START);
				return true;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		mSearchAction = menu.findItem(R.id.action_search);
		return super.onPrepareOptionsMenu(menu);
	}

	private void switchActivity(Class<?> cls){
		Intent i = new Intent(getApplicationContext(), cls);
		startActivity(i);
	}

	protected void handleMenuSearch(){
		ActionBar action = getSupportActionBar(); //get the actionbar

		if(isSearchOpened){ //test if the search is open

			action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
			action.setDisplayShowTitleEnabled(true); //show the title in the action bar

			//hides the keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

			//add the search icon in the action bar
			mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_open_search));

			isSearchOpened = false;
		} else { //open the search entry

			action.setDisplayShowCustomEnabled(true); //enable it to display a
			// custom view in the action bar.
			action.setCustomView(R.layout.search_bar);//add the custom view
			action.setDisplayShowTitleEnabled(false); //hide the title

			edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor

			//this is a listener to do a search when the user clicks on search button
			edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_SEARCH) {
						doSearch();
						return true;
					}
					return false;
				}
			});


			edtSeach.requestFocus();

			//open the keyboard focused in the edtSearch
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


			//add the close icon
			mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_search));

			isSearchOpened = true;
		}
	}

	@Override
	public void onBackPressed() {
		if(isSearchOpened) {
			handleMenuSearch();
			return;
		}
		super.onBackPressed();
	}

	private void doSearch() {

	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	@Override
	public void getCurrentUser(User user) {
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		View headerLayout = navigationView.getHeaderView(0);
		TextView textView = (TextView) headerLayout.findViewById(R.id.textViewName);
		textView.setText(ApplicationData.getInstance().getCurrentUser().getName());
	}

	@Override
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void getAllUsers(List<User> users) {
		getFeed();
	}

	@Override
	public void onStart() {
		super.onStart();
		// Register for messages.
		EventBus.getDefault().register(this);

	}

	@Override
	public void onStop() {
		EventBus.getDefault().unregister(this);

		super.onStop();
	}

	public void postMessage(View view){
		final EditText title = (EditText) findViewById(R.id.input_title);
		final EditText message = (EditText) findViewById(R.id.input_message);

		if((!title.getText().toString().isEmpty()) && (!message.getText().toString().isEmpty())){
			WebService webService = new WebService(getApplicationContext());
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
				}

				@Override
				public void onFailure(Throwable t) {

				}
			});
		}
	}

	private void setupDrawerContent(NavigationView navigationView) {
		navigationView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem) {
						menuItem.setChecked(true);
						mDrawerLayout.closeDrawers();
						return true;
					}
				});
	}
}