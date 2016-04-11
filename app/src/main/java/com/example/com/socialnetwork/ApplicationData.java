package com.example.com.socialnetwork;

import com.example.com.socialnetwork.model.Friendship;
import com.example.com.socialnetwork.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by shobhit on 4/9/16.
 */
public class ApplicationData {
    private ApplicationData(){}

    private User user;
	private Map<Integer, User> users = new HashMap<>();

	private List<Friendship> friendshipList = new ArrayList<>();

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

	public List<User> getUsers(){
		List<User> tmp = new ArrayList<>();
		for(Map.Entry<Integer, User> entry : users.entrySet()){
			tmp.add(entry.getValue());
		}
		return tmp;
	}

	public User getUser(String email){
		for (Map.Entry<Integer, User> entry:users.entrySet()) {
			if(entry.getValue().getUsername().equals(email))
				return entry.getValue();
		}
		return null;
	}

	public List<User> getFriends(){
		Integer id = getCurrentUser().getId();

		List<Integer> friends = new ArrayList<>();

		for(Friendship friendship: friendshipList){
			if(friendship.getCreator().equals(getCurrentUser().getUsername()) && friendship.getApproved())friends.add(friendship.getFriend());
		}
		List<User>tmp = new ArrayList<>();
		for(Integer i: friends){
			tmp.add(users.get(i));
		}
		return tmp;
	}

	public List<User> friendRequestRecieved(){
		Integer id = getCurrentUser().getId();

		List<String> friends = new ArrayList<>();

		for(Friendship friendship: friendshipList){
			if(friendship.getFriend() == id && !friendship.getApproved())friends.add(friendship.getCreator());
		}
		List<User>tmp = new ArrayList<>();
		for(String i: friends){
			tmp.add(getUser(i));
		}
		return tmp;
	}

	public List<User> friendRequestSent(){
		Integer id = getCurrentUser().getId();

		List<Integer> friends = new ArrayList<>();

		for(Friendship friendship: friendshipList){
			if(friendship.getCreator().equals(getCurrentUser().getUsername()) && !friendship.getApproved())friends.add(friendship.getFriend());
		}
		List<User>tmp = new ArrayList<>();
		for(Integer i: friends){
			tmp.add(users.get(i));
		}
		return tmp;
	}

	public void setFriendshipList(List<Friendship> friendshipList){
		this.friendshipList = friendshipList;
	}

	public List<User> getUnknown(){
		List<User> possibleUnknown = getUsers();
		for(Iterator<User> iter = possibleUnknown.listIterator(); iter.hasNext(); ){
			User u = iter.next();
			for (Friendship friendship: friendshipList){
				if(friendship.getCreator().equals(u.getUsername()) || friendship.getFriend() == u.getId()){
					iter.remove();
					break;
				}
			}
		}
		for(Iterator<User> iter = possibleUnknown.listIterator(); iter.hasNext(); ){
			User u = iter.next();
			if(u.getId() == getCurrentUser().getId())
				iter.remove();

		}
		return  possibleUnknown;
	}
}
