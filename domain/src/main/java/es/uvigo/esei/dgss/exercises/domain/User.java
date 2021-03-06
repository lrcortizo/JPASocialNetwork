package es.uvigo.esei.dgss.exercises.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class User {

	@Id
	private String login;
	
	private String name;
	private String password;
	private String role = "user";
	private String email;

	private byte[] picture;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user1", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value="user1")
	private Collection<UserFriendship> friends = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user2", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value="user2")
	private Collection<UserFriendship> friendOf = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value="user-post")
	private List<Post> posts = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value="user-comment")
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	@JsonManagedReference(value="user-like")
	private Collection<Like> likes = new ArrayList<>();

	public User() {
	}

	public User(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getPicture() {
		return picture;
	}
	
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<UserFriendship> getFriends() {
		return friends;
	}

	public void setFriends(Collection<UserFriendship> friends) {
		this.friends = friends;
	}

	public Collection<UserFriendship> getFriendOf() {
		return friendOf;
	}

	public void setFriendOf(Collection<UserFriendship> friendOf) {
		this.friendOf = friendOf;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Collection<Like> getLikes() {
		return likes;
	}

	public void setLikes(Collection<Like> likes) {
		this.likes = likes;
	}
	
	public void addLike(Like like){
		this.likes.add(like);
		like.setUser(this);
	}
}
