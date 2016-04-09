package com.example.com.socialnetwork;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.com.socialnetwork.model.User;
import com.example.com.socialnetwork.ws.WebService;

import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

	public static String LOG_TAG = "My log tag";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences prefs = getSharedPreferences("your_file_name", MODE_PRIVATE);
		if (prefs.getBoolean("loggedin", false)) {
			Log.i(LOG_TAG, "Changing Activity to main default");
			switchActivity(MainActivity.class);
		}

        EditText et = (EditText)findViewById(R.id.input_email);
        EditText et2 = (EditText)findViewById(R.id.input_password);
        final TextView textView = (TextView) findViewById(R.id.textView2);
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                textView.setVisibility(View.GONE);
            }
        };

        et.addTextChangedListener(textWatcher);
        et2.addTextChangedListener(textWatcher);
	}

	private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
			"[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
					"\\@" +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
					"(" +
					"\\." +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
					")+"
	);

	public void login(View v) {

        hideKeyboard();

		final String email = ((EditText) findViewById(R.id.input_email)).getText().toString();
		final String password = ((EditText) findViewById(R.id.input_password)).getText().toString();

		if (isValid()) {
			WebService webService = new WebService(getApplicationContext(), email, password);
			User user = new User();
			user.setUsername(email);
			user.setPassword(password);
			Call<List<User>> queryResponseCall =
					webService.loginService.getUsers();
			//Call retrofit asynchronously
			queryResponseCall.enqueue(new Callback<List<User>>() {
				@Override
				public void onResponse(Response<List<User>> response) {
					if (response.code() == 200) {

						SharedPreferences.Editor editor = getSharedPreferences("your_file_name", MODE_PRIVATE).edit();
						editor.putBoolean("loggedin", true);
						editor.putString("username", email);
						editor.putString("password", password);
						editor.commit();
						//switch intent
						Log.i(LOG_TAG, "Changing Activity to main after logging");
						switchActivity(MainActivity.class);
					}
                    else{
                        TextView textView = (TextView) findViewById(R.id.textView2);
                        textView.setVisibility(View.VISIBLE);
                    }
				}

				@Override
				public void onFailure(Throwable t) {
					// Log error here since request failed
					Log.i(LOG_TAG, "Network error");
				}
			});
		}
	}

	private void switchActivity(Class<?> cls) {
		Intent i = new Intent(getApplicationContext(), cls);
		startActivity(i);
	}

	private boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public void goSignupActivity(View v) {
		switchActivity(SignupActivity.class);
	}

	private boolean isValid() {

		EditText emailEditText = (EditText) findViewById(R.id.input_email);
		EditText passwordEditText = (EditText) findViewById(R.id.input_password);
		final String email = emailEditText.getText().toString();
		final String password = passwordEditText.getText().toString();
		if (email == null || email.isEmpty()) {
			Log.i(LOG_TAG, "email empty");
			emailEditText.setError("Email is empty");
			return false;
		} else if (!checkEmail(email)) {
			emailEditText.setError("Please give valid email");
			return false;
		} else if (password == null || password.isEmpty()) {
			passwordEditText.setError("Password is empty");
			return false;
		}
		return  true;
	}

    private void hideKeyboard(){
        InputMethodManager inputManager =
                (InputMethodManager) getApplicationContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }
}