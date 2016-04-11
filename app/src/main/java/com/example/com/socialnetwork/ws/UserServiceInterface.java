package com.example.com.socialnetwork.ws;

import com.example.com.socialnetwork.model.User;

import java.util.List;

/**
 * Created by shobhit on 4/10/16.
 */
public interface UserServiceInterface {
	public void getCurrentUser(User user);
	public void getAllUsers(List<User> users);
}
