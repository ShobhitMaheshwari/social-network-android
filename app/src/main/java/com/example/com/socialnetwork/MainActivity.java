package com.example.com.socialnetwork;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.example.com.socialnetwork.model.User;

import java.util.List;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

	public static String LOG_TAG = "My log tag";

	private Toolbar toolbar;
	private MenuItem mSearchAction;
	private boolean isSearchOpened = false;
	private EditText edtSeach;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(myToolbar);
	}

	@Override
	protected void onResume(){
		super.onResume();
		SharedPreferences prefs = getSharedPreferences("your_file_name", MODE_PRIVATE);
		if(!prefs.getBoolean("loggedin", false)){
            Log.i(LOG_TAG, "Going to logging activity default");
			switchActivity(LoginActivity.class);
		}

        Call<List<User>> queryResponseCall =
                webService.loginService.getUsers();
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
}