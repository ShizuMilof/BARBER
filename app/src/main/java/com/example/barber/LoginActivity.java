package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// ...
public class LoginActivity extends AppCompatActivity {


	private String enteredEmail;
	private String enteredPassword;
	private FirebaseAuth mAuth;

	private EditText editTextEmail, editTextPassword, editTextEmailReg, editTextPasswordReg, editTextUsernameReg2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitylogin);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mAuth = FirebaseAuth.getInstance();
		FirebaseApp.initializeApp(this);
		editTextEmail = findViewById(R.id.editTextEmail);
		editTextPassword = findViewById(R.id.editTextPassword);


		Button btnOpenRegisterActivity = findViewById(R.id.btnOpenRegisterActivity);

		btnOpenRegisterActivity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});


		Button buttonLogin = findViewById(R.id.buttonLogin);
		buttonLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String email = editTextEmail.getText().toString();
				String password = editTextPassword.getText().toString();
				if (email.isEmpty() || password.isEmpty()) {
					Toast.makeText(LoginActivity.this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
				} else {


					// Store the entered email and password
					enteredEmail = email;
					enteredPassword = password;
					loginUser(email, password);
				}
			}
		});
	}


	private void loginUser(String email, String password) {
		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
							String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
							String enteredPassword = editTextPassword.getText().toString();
							String enteredEmail = editTextEmail.getText().toString();


							if (userEmail != null && userEmail.contains("radnik")) {

								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
								intent.putExtra("userUid", userUid);
								intent.putExtra("userEmail", enteredEmail);
								intent.putExtra("enteredPassword", enteredPassword);
								startActivity(intent);
							} else {
								// Redirect to the default MainActivity
								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
								intent.putExtra("userUid", userUid);
								intent.putExtra("userEmail", enteredEmail);
								intent.putExtra("enteredPassword", enteredPassword);
								startActivity(intent);
							}

							// Finish the LoginActivity
							finish();
						} else {
							Toast.makeText(LoginActivity.this, "Prijava nije uspjela. Provjerite podatke.", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

}
