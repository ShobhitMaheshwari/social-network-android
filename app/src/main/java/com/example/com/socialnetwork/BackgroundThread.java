package com.example.com.socialnetwork;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.com.socialnetwork.model.User;
import com.example.com.socialnetwork.ws.WebService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

/**
 * Created by shobhit on 4/10/16.
 */
public class BackgroundThread extends Thread{

	Handler handler;
	Context context;

	public BackgroundThread(Context context){
		this.context = context;
	}
	@Override
	public void run() {

		handler = new Handler();
		handler.removeCallbacks(compute);
		handler.post(compute);
	}

	Runnable compute = new Runnable(){
		@Override
		public void run() {
			final WebService webService = new WebService(context);
			Call<User> queryResponseCall =
					webService.loginService.getUser();
			try{
				User user = queryResponseCall.execute().body();
				ApplicationData.getInstance().setCurrentUser(user);

				Call<List<User>> queryResponseCall1 = webService.userService.getUsers();
				List<User> users = queryResponseCall1.execute().body();
				for (int i = 0; i < users.size(); i++) {
					ApplicationData.getInstance().setUser(users.get(i));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
//			EventBus.getDefault().post();
		}
	};
}
