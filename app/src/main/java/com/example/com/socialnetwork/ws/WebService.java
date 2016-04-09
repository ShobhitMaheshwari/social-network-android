package com.example.com.socialnetwork.ws;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.example.com.socialnetwork.LoginActivity;
import com.example.com.socialnetwork.model.Friendship;
import com.example.com.socialnetwork.model.Snippet;
import com.example.com.socialnetwork.model.User;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by shobhit on 4/7/16.
 */
public class WebService {

	private String username;
	private String password;
	private Context context;

	public static String LOG_TAG = "My log tag";

	HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

	OkHttpClient httpClient = new OkHttpClient.Builder()

			.addInterceptor(new Interceptor() {
				@Override
				public Response intercept(Chain chain) throws IOException {

					Request request = chain.request().newBuilder()
							.header("Content-Type", "application/json")
							.header("Accept", "*/*")
							.header("Accept-Encoding", "gzip, deflate")
							.header("Accept-Language", "en-US,en;q=0.8")
							.header("Connection", "keep-alive")
							.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
							.header("Host", "192.168.0.26:5000")
							.header("X-CSRFToken", "VIAMlO5RfIqRgdxxTvqwAu9asFBEMv9h")
							.header("Cookie", "csrftoken=VIAMlO5RfIqRgdxxTvqwAu9asFBEMv9h")
							.header("Authorization", "Basic " +
									Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.DEFAULT).trim())
							.build();
					return chain.proceed(request);
				}
			})
			.addInterceptor(logging)
			.build();
	OkHttpClient registerClient = new OkHttpClient.Builder()

			.addInterceptor(new Interceptor() {
				@Override
				public Response intercept(Chain chain) throws IOException {

					Request request = chain.request().newBuilder()
							.header("Content-Type", "application/json")
							.header("Accept", "*/*")
							.header("Accept-Encoding", "gzip, deflate")
							.header("Accept-Language", "en-US,en;q=0.8")
							.header("Connection", "keep-alive")
							.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
							.header("Host", "192.168.0.26:5000")
							.header("X-CSRFToken", "VIAMlO5RfIqRgdxxTvqwAu9asFBEMv9h")
							.header("Cookie", "csrftoken=VIAMlO5RfIqRgdxxTvqwAu9asFBEMv9h")
							.build();
					return chain.proceed(request);
				}
			})
			.addInterceptor(logging)
			.build();

	Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("http://192.168.0.26:5000/snippetapp/")
//            .baseUrl("https://serene-reef-32806.herokuapp.com/snippetapp/")
			.addConverterFactory(GsonConverterFactory.create())
			.client(httpClient)
			.build();
	Retrofit registerRetrofit = new Retrofit.Builder()
			.baseUrl("http://192.168.0.26:5000/snippetapp/")
//            .baseUrl("https://serene-reef-32806.herokuapp.com/snippetapp/")
			.addConverterFactory(GsonConverterFactory.create())
			.client(registerClient)
			.build();

	public SnippetServiceInterface snippetService = retrofit.create(SnippetServiceInterface.class);
	public UserServiceInterface userService = retrofit.create(UserServiceInterface.class);
	public UserServiceInterface userServiceRegister = registerRetrofit.create(UserServiceInterface.class);
	public FriendServiceInterface friendService = retrofit.create(FriendServiceInterface.class);
	public LoginServiceInterface loginService = retrofit.create(LoginServiceInterface.class);

	public WebService(Context context){
		this.context = context;
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		SharedPreferences prefs = this.context.getSharedPreferences("your_file_name", context.MODE_PRIVATE);
		this.username = prefs.getString("username", "");
		this.password = prefs.getString("password", "");
	}

	public WebService(Context context, String username, String password){
		this.context = context;
		this.username = username;
		this.password = password;
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
	}

	public interface SnippetServiceInterface {
		@GET("snippets/")
		Call<List<Snippet>> getSnippets();
		@POST("snippets/")
		Call<Snippet> postSnippet(@Field("title") String title, @Field("message") String message);
	}

	public interface UserServiceInterface {
		@GET("users/")
		Call<List<User>> getUsers();

		@POST("users/")
		Call<User> postUser(@Body User user);
	}

	public interface FriendServiceInterface{
		@POST("friends/")
		Call<Friendship> postFriendRequest(@Field("friend") String friend);
	}

	public interface LoginServiceInterface{
		@GET("login/")
		Call<List<User>> getUsers();
	}

    public void saveCredentials(){

    }
}