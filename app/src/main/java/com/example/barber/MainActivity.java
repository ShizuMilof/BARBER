package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

	Button prijavaButton;

	Button postavkeButton;
	Button rezervirajButton;
	Button admin;

	private String loggedInUsername;

	private String loggedInUsernameUid;
	private String loggedInUsernameEmail;


	private BottomNavigationView bottomNavigationView;
	Button logoutButton;
	Button profilButton;

	Button buttonListaRezervacija;

	public DrawerLayout drawerLayout;
	public ActionBarDrawerToggle actionBarDrawerToggle;
	private String username;
	private String userUid;
	private String userEmail;
	
	
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

		Button logoutButton = findViewById(R.id.logoutButton);
		Button terminiButton = findViewById(R.id.terminiButton);
		Button terminifrizerButton = findViewById(R.id.terminifrizerButton);
		admin = findViewById(R.id.admin);
		admin.setVisibility(View.GONE);

		Button buttonListaRezervacija = findViewById(R.id.terminiButton);
		Button buttonListaRezervacijafrizer = findViewById(R.id.terminifrizerButton);


		terminiButton.setVisibility(View.GONE);
		postavkeButton = findViewById(R.id.PostavkeButton);
		profilButton = findViewById(R.id.profilButton);
		profilButton.setVisibility(View.GONE);
		prijavaButton = findViewById(R.id.prijava);
		rezervirajButton = findViewById(R.id.rezervirajButton);
		rezervirajButton.setVisibility(View.GONE);
		rezervirajButton.setEnabled(false);
		logoutButton.setVisibility(View.GONE);
		terminifrizerButton.setVisibility(View.GONE);
		Intent intent = getIntent();


		String enteredEmail = intent.getStringExtra("userEmail");
		String enteredPassword = intent.getStringExtra("enteredPassword");

















		if (intent != null && intent.hasExtra("userEmail")&& intent.hasExtra("userUid") ) {
			loggedInUsernameEmail = intent.getStringExtra("userEmail");
			loggedInUsernameUid = intent.getStringExtra("userUid");

			DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(loggedInUsernameUid);
			userRef.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					if (dataSnapshot.exists()) {
						loggedInUsername = dataSnapshot.child("username").getValue(String.class);
			//			Toast.makeText(MainActivity.this, "Dobrodošli, " + loggedInUsername + "!", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {
					Toast.makeText(MainActivity.this, "Error retrieving user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
				}
				private void openYourActivity() {
					// Ovdje otvorite željeni Activity, npr.:
					Intent intent = new Intent(MainActivity.this, AdminActivity.class);
					startActivity(intent);
				}
			});



			if (loggedInUsernameEmail.contains("radnici")) {

				admin = findViewById(R.id.admin);
				admin.setVisibility(View.GONE);
				terminifrizerButton.setVisibility(View.VISIBLE);
				terminiButton.setVisibility(View.GONE);
				rezervirajButton.setVisibility(View.GONE);
				prijavaButton.setVisibility(View.GONE);
				logoutButton.setVisibility(View.VISIBLE);



			} else if ("admin@mail.com".equals(loggedInUsernameEmail)) {
				// If the email is admin, perform actions for an admin user
				admin = findViewById(R.id.admin);
				admin.setVisibility(View.VISIBLE);
				terminiButton.setVisibility(View.GONE);
				rezervirajButton.setVisibility(View.GONE);
				prijavaButton.setVisibility(View.GONE);
				logoutButton.setVisibility(View.VISIBLE);

			} else {
				// Perform actions for a regular user
				terminiButton.setVisibility(View.VISIBLE);
				profilButton.setVisibility(View.VISIBLE);
				rezervirajButton.setVisibility(View.VISIBLE);
				rezervirajButton.setEnabled(true);
				logoutButton.setVisibility(View.VISIBLE);
				prijavaButton.setVisibility(View.GONE);
				prijavaButton.setEnabled(false);
				AppCompatImageButton button = findViewById(R.id.button);
				button.setVisibility(View.GONE);
				button.setEnabled(false);
			}
		} else {

		}



		profilButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, ProfilKorisnikaActivity.class);
				intent.putExtra("username", loggedInUsername);
				intent.putExtra("enteredPassword", enteredPassword);
				intent.putExtra("userEmail", enteredEmail);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});




		prijavaButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);

				startActivityForResult(intent, 1);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});


		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				handleLogout();
			}
		});


		postavkeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, PostavkeAplikacijeActivity.class);

				startActivityForResult(intent, 1);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});



		admin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, AdminActivity.class);
				startActivityForResult(intent, 1);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});



		buttonListaRezervacija.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, ListaRezerviranihUslugaActivity.class);

				intent.putExtra("username", loggedInUsername);
				startActivity(intent);
			}
		});



		buttonListaRezervacijafrizer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, RadniciActivity2.class);

				intent.putExtra("username", loggedInUsername);

				startActivity(intent);
			}
		});





         //da se mapa ne miče
		mapFragment.getView().setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						v.getParent().requestDisallowInterceptTouchEvent(true);
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						v.getParent().requestDisallowInterceptTouchEvent(false);
						break;
				}




				return false;
			}
		});




		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


		AppCompatImageButton button = findViewById(R.id.button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openLanguageSelectionActivity();
			}
		});

		rezervirajButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {



				openRadniciActivity();
			}
		});
		mapFragment.getMapAsync(this);




	}


	private void handleLogout() {
		// Perform any logout-related tasks here


		Intent intent = new Intent(MainActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {

		LatLng location = new LatLng(45.83325561013758, 17.388860417173003);
		googleMap.addMarker(new MarkerOptions().position(location).title("Frizerski salon Boris"));
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
	}

	private void openRadniciActivity() {
		Intent intent = new Intent(this, RadniciActivity.class);


		intent.putExtra("username", loggedInUsername);

		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	private void openLanguageSelectionActivity() {
		Intent intent = new Intent(this, LanguageActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
}
