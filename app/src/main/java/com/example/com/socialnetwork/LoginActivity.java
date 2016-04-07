package com.example.com.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

    @Override
    protected void onResume (){
        super.onResume();
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if(prefs.getBoolean("loggedin", false)){
            switchActivity(MainActivity.class);
        }
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

	public void login(View v){
		final String email = ((EditText) findViewById(R.id.input_email)).getText().toString();
		final String password = ((EditText) findViewById(R.id.input_password)).getText().toString();

		if(email != null && password != null && checkEmail(email)){
			WebService webService = new WebService();
			User user = new User();
			user.setUsername(email);
			user.setPassword(password);
			Call<List<User>> queryResponseCall =
					webService.loginService.getUsers();
			//Call retrofit asynchronously
			queryResponseCall.enqueue(new Callback<List<User>>() {
				@Override
				public void onResponse(Response<List<User>> response) {
                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                    editor.putBoolean("loggedin", true);
                    editor.putString("username", email);
                    editor.putString("password", password);
                    editor.commit();
					//switch intent
                    switchActivity(MainActivity.class);
				}

				@Override
				public void onFailure(Throwable t) {
					// Log error here since request failed
				}
			});
		}
	}

    private void switchActivity(Class<?> cls){
        Intent i = new Intent(getApplicationContext(), cls);
        startActivity(i);
    }

	private boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public void goSignupActivity(View v){
		switchActivity(SignupActivity.class);
	}
}