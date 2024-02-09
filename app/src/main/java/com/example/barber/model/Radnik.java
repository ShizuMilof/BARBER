package com.example.barber.model;

public class Radnik {

	private String imePrezime;

	private String profileImageUrl;



	public Radnik() { }

	public Radnik( String imePrezime,String profileImageUrl) {

		this.imePrezime = imePrezime;

		this.profileImageUrl = profileImageUrl;
	}



	public String getImePrezime() {
		return imePrezime;
	}
	public void setImePrezime(String imePrezime) {
		this.imePrezime = imePrezime;
	}




	public String getProfileImageUrl() {
		return profileImageUrl;
	}


	public void setImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}


}
