package com.example.com.socialnetwork.ws;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.com.socialnetwork.R;
import com.example.com.socialnetwork.model.BaseModel;
import com.example.com.socialnetwork.model.Friendship;
import com.example.com.socialnetwork.model.Snippet;
import com.example.com.socialnetwork.model.User;

import java.util.List;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by shobhit on 4/7/16.
 */
public class WebService {

	HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

	OkHttpClient httpClient = new OkHttpClient.Builder()
			.addInterceptor(logging)
			.build();

	Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("https://localhost:5000/snippetapp/")
			.addConverterFactory(GsonConverterFactory.create())
			.client(httpClient)
			.build();

    public SnippetServiceInterface snippetService = retrofit.create(SnippetServiceInterface.class);
    public UserServiceInterface userService = retrofit.create(UserServiceInterface.class);
    public FriendServiceInterface friendService = retrofit.create(FriendServiceInterface.class);
    public LoginServiceInterface loginService = retrofit.create(LoginServiceInterface.class);

	public WebService(){
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
        Call<User> postUser(@Field("username") String username, @Field("password") String password);
    }

    public interface FriendServiceInterface{
        @POST("friends/")
        Call<Friendship> postFriendRequest(@Field("friend") String friend);
    }

    public interface LoginServiceInterface{
        @GET("login/")
        Call<List<User>> getUsers();
    }
}