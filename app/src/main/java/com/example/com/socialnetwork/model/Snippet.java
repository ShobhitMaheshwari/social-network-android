package com.example.com.socialnetwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by shobhit on 4/6/16.
 */

public class Snippet extends BaseModel{

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("message")
	@Expose
	private String message;
	@SerializedName("created")
	@Expose
	private String created;

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
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return The message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message The message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return The created
	 */
	public String getCreated() {
		return created;
	}

	/**
	 * @param created The created
	 */
	public void setCreated(String created) {
		this.created = created;
	}

}