package es.uvigo.esei.dgss.exercises.domain;

import java.util.Date;


public class UserFriendshipDTO {
	
	private User user1;

	private User user2;

	private Date date;
	
	private boolean accepted;

	public UserFriendshipDTO() {
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

}
