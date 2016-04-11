package com.example.com.socialnetwork.model;

/**
 * Created by shobhit on 4/6/16.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Friendship extends BaseModel{

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("created")
	@Expose
	private String created;
	@SerializedName("friend")
	@Expose
	private Integer friend;
	@SerializedName("creator")
	@Expose
	private String creator;
	@SerializedName("approved")
	@Expose
	private Boolean approved;

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

	/**
	 * @return The creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator The friend
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return The friend
	 */
	public Integer getFriend() {
		return friend;
	}

	/**
	 * @param friend The friend
	 */
	public void setFriend(Integer friend) {
		this.friend = friend;
	}

	/**
	 * @return The approved
	 */
	public Boolean getApproved() {
		return approved;
	}

	/**
	 * @param approved The approved
	 */
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

}
