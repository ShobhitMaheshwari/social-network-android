package com.example.com.socialnetwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shobhit on 4/6/16.
 */
public class User extends BaseModel{

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("username")
	@Expose
	private String username;
    @SerializedName("first_name")
    @Expose
    private String name;
	@SerializedName("password")
	@Expose
	private String password;
	@SerializedName("snippets")
	@Expose
	private List<Integer> snippets = new ArrayList<Integer>();
	@SerializedName("friends")
	@Expose
	private List<Integer> friends = new ArrayList<Integer>();

	/**
	 * @return The id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return The username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username The username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

	/**
	 * @return The password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password The password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return The snippets
	 */
	public List<Integer> getSnippets() {
		return snippets;
	}

	/**
	 * @param snippets The snippets
	 */
	public void setSnippets(List<Integer> snippets) {
		this.snippets = snippets;
	}

	/**
	 * @return The friends
	 */
	public List<Integer> getFriends() {
		return friends;
	}

	/**
	 * @param friends The friends
	 */
	public void setFriends(List<Integer> friends) {
		this.friends = friends;
	}

}
