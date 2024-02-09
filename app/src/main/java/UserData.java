// Create a new class named UserData.java
public class UserData {
	private String email;
	private String username;
	private String password;

	// Required default constructor for Firebase
	public UserData() {}

	public UserData(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}

	// Getter methods
	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
