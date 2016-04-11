package com.example.com.socialnetwork;

import com.example.com.socialnetwork.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shobhit on 4/9/16.
 */
public class ApplicationData {
    private ApplicationData(){}

    private User user;
	private Map<Integer, User> users = new HashMap<>();

    private static ApplicationData instance = null;

    public static ApplicationData getInstance(){
        if(instance == null) {
            instance = new ApplicationData();
        }
        return instance;
    }

    public void setCurrentUser(User user){
        this.user = user;
    }
    public User getCurrentUser(){
        return this.user;
    }

	public void setUser(User user){
		users.put(user.getId(), user);
	}

	public User getUser(int id){
		return users.get(id);
	}

	public User getUser(String email){
		for (Map.Entry<Integer, User> entry:users.entrySet()) {
			if(entry.getValue().getUsername().equals(email))
				return entry.getValue();
		}
		return null;
	}
}
