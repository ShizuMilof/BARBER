package com.example.barber.model;

public class User {
	private String email;

	private String username;



	public User() {

	}
	public User(String email,String username) {
		this.email = email;

		this.username = username;


	}

	public String getEmail() {
		return email;
	}



	public String getUsername() {
		return username;
	}



}
