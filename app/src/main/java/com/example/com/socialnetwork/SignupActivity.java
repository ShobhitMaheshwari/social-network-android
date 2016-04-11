package com.example.com.socialnetwork;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.com.socialnetwork.model.User;
import com.example.com.socialnetwork.ws.WebService;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

	public static String LOG_TAG = "My log tag";

	private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
			"[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
					"\\@" +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
					"(" +
					"\\." +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
					")+"
	);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
	}

    @Override
    protected void onResume(){
        super.onResume();

    }

	public void signup(View v) {

		hideKeyboard();

		final String email = ((EditText) findViewById(R.id.input_email)).getText().toString();
		final String password = ((EditText) findViewById(R.id.input_password)).getText().toString();
		final String name = ((EditText) findViewById(R.id.input_name)).getText().toString();

		if (isValid()) {
			final WebService webService = new WebService(getApplicationContext(), email, password);
			User user = new User();
			user.setName(name);
			user.setUsername(email);
			user.setPassword(password);
			Call<User> queryResponseCall =
					webService.userServiceRegister.postUser(user);
			//Call retrofit asynchronously
			queryResponseCall.enqueue(new Callback<User>() {
				@Override
				public void onResponse(Response<User> response) {
                    if (response.code() == 201) {
                        webService.saveCredentials();
                        //switch intent
                        switchActivity(MainActivity.class);
                        ApplicationData.getInstance().setCurrentUser(response.body());
                    }
                    else{
                        webService.clearCredentials();
                        EditText emailEditText = (EditText) findViewById(R.id.input_email);
                        emailEditText.setError("Email already exists");
                    }
                }

				@Override
				public void onFailure(Throwable t) {
					// Log error here since request failed
				}
			});
		}
	}

	private void switchActivity(Class<?> cls) {
		Intent i = new Intent(getApplicationContext(), cls);
		startActivity(i);
	}

	public void goLoginActivity(View v) {
		switchActivity(LoginActivity.class);
	}

	private boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	private void hideKeyboard(){
		InputMethodManager inputManager =
				(InputMethodManager) getApplicationContext().
						getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(
				this.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

	}

	private boolean isValid() {

		EditText emailEditText = (EditText) findViewById(R.id.input_email);
		EditText passwordEditText = (EditText) findViewById(R.id.input_password);
        EditText nameEditText = (EditText) findViewById(R.id.input_name);
		final String email = emailEditText.getText().toString();
		final String password = passwordEditText.getText().toString();
        final String name = nameEditText.getText().toString();
		if (email == null || email.isEmpty()) {
			emailEditText.setError("Email is empty");
			return false;
		} else if (!checkEmail(email)) {
			emailEditText.setError("Please give valid email");
			return false;
		} else if (email.length() > 30){
            emailEditText.setError("Email should be less than 30 characters");
            return false;
        } else if (password == null || password.isEmpty()) {
			passwordEditText.setError("Password is empty");
			return false;
		} else if(name == null || name.isEmpty()){
            nameEditText.setError("Name is empty");
            return false;
        } else if(name.length() > 30){
            nameEditText.setError("Name should be less than 30 characters");
            return false;
        }
		return  true;
	}
}