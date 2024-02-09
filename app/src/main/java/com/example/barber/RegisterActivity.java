package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barber.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// ...
public class RegisterActivity extends AppCompatActivity{

	private FirebaseAuth mAuth;
	private EditText editTextEmailReg2, editTextPasswordReg2, editTextUsernameReg2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activityregister);

		mAuth = FirebaseAuth.getInstance();
		FirebaseApp.initializeApp(this);


		editTextEmailReg2 = findViewById(R.id.editTextEmailReg2);
		editTextPasswordReg2 = findViewById(R.id.editTextPasswordReg2);
		editTextUsernameReg2 = findViewById(R.id.editTextUsernameReg2);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);











		Button buttonRegister = findViewById(R.id.buttonRegister2);
		buttonRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String emailReg = editTextEmailReg2.getText().toString();
				String passwordReg = editTextPasswordReg2.getText().toString();
				String usernameReg = editTextUsernameReg2.getText().toString();
				if (emailReg.isEmpty() || passwordReg.isEmpty() || usernameReg.isEmpty()) {
					Toast.makeText(RegisterActivity.this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
				} else {
					registerUser(emailReg, passwordReg, usernameReg);
				}

			}
		});
	}


	private void registerUser(final String emailReg, final String passwordReg, final String usernameReg) {
		mAuth.createUserWithEmailAndPassword(emailReg, passwordReg)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {

							Toast.makeText(RegisterActivity.this, "Uspješno ste se registrirali!", Toast.LENGTH_SHORT).show();

							FirebaseUser firebaseUser = mAuth.getCurrentUser();
							String userId = firebaseUser.getUid();
							addUserToDatabase(userId, emailReg, usernameReg);

							Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
							startActivity(loginIntent);
							finish();
						} else {
							// Neuspješna registracija
							Toast.makeText(RegisterActivity.this, "Registracija nije uspjela. Provjerite podatke.", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}



	private void addUserToDatabase(String userId, String email, String username) {
		Log.d("Database", "Dodavanje korisnika u bazu podataka...");
		DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
		User user = new User(email, username);
		usersRef.child(userId).setValue(user)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()) {
							Log.d("Database", "Korisnik uspješno dodan u bazu podataka.");
						} else {
							Log.d("Database", "Greška prilikom dodavanja korisnika u bazu podataka.", task.getException());
						}
					}
				});
	}



	}
