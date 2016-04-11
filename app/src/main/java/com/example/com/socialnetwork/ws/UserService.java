package com.example.com.socialnetwork.ws;

import android.content.Context;

import com.example.com.socialnetwork.ApplicationData;
import com.example.com.socialnetwork.model.User;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

/**
 * Created by shobhit on 4/10/16.
 */
public class UserService {
	public static void getCurrentUser(final Context context){
		Runnable compute = new Runnable(){
			@Override
			public void run() {
				User user = ApplicationData.getInstance().getCurrentUser();
				if(user != null){
					EventBus.getDefault().post(user);
				}
				else {
					final WebService webService = new WebService(context);
					Call<User> queryResponseCall =
							webService.loginService.getUser();
					try {
						user = queryResponseCall.execute().body();
						ApplicationData.getInstance().setCurrentUser(user);
						EventBus.getDefault().post(user);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Thread backgroundThread = new Thread(compute);
		backgroundThread.start();
	}

	public static void getAllUsers(final Context context){
		Runnable compute = new Runnable(){
			@Override
			public void run() {

				final WebService webService = new WebService(context);
				Call <List<User>> queryResponseCall = webService.userService.getUsers();
					try {
						List<User> users = queryResponseCall.execute().body();
						for(int i = 0; i < users.size(); i++) {
							ApplicationData.getInstance().setUser(users.get(i));
						}
						EventBus.getDefault().post(users);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		};

		Thread backgroundThread = new Thread(compute);
		backgroundThread.start();
	}
}
