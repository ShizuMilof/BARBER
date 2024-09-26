package com.example.barber;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

	private Button prijavaButton;
	private Button postavkeButton;
	private Button rezervirajButton;
	private Button adminButton;
	private Button logoutButton;
	private Button profilButton;
	private Button terminiButton;
	private Button terminifrizerButton;

	private Button terminifrizerButton2;

	private Button buttonListaRezervacija;
	private Button buttonListaRezervacijafrizer;
	private Button buttonListaRezervacijafrizer2;

	private AppCompatImageButton languageButton;

	private BottomNavigationView bottomNavigationView;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private GoogleMap googleMap;
	private Button buttonOpenQrScanner;
	private String loggedInUsername;
	private String loggedInUsernameUid;
	private String loggedInUsernameEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

		logoutButton = findViewById(R.id.logoutButton);
		terminiButton = findViewById(R.id.terminiButton);
		terminifrizerButton = findViewById(R.id.terminifrizerButton);
		terminifrizerButton2 = findViewById(R.id.terminifrizerButton2);
		adminButton = findViewById(R.id.admin);
		buttonListaRezervacija = findViewById(R.id.terminiButton);
		buttonListaRezervacijafrizer = findViewById(R.id.terminifrizerButton);
		buttonListaRezervacijafrizer2 = findViewById(R.id.terminifrizerButton2);
		postavkeButton = findViewById(R.id.PostavkeButton);
		profilButton = findViewById(R.id.profilButton);
		prijavaButton = findViewById(R.id.prijava);
		rezervirajButton = findViewById(R.id.rezervirajButton);
		buttonOpenQrScanner = findViewById(R.id.buttonOpenQrScanner);















		checkLoginState();
		buttonOpenQrScanner.setOnClickListener(v -> {
			Intent intent = new Intent(MainActivity.this, QrCodeScannerActivity.class);
			startActivity(intent);
		});
		// Get intent data
		Intent intent = getIntent();
		String email = intent.getStringExtra("email");
		String password = intent.getStringExtra("lozinka");
		String ime = intent.getStringExtra("ime");
		String prezime = intent.getStringExtra("prezime");

		String username = intent.getStringExtra("username");
		String urlSlike = intent.getStringExtra("urlSlike");
		int ulogaId = intent.getIntExtra("ulogeId", 0); // Default value is 1
		boolean verificiran = intent.getBooleanExtra("verificiran", false); // Assuming verificiran is passed as a boolean
		String enteredEmail = intent.getStringExtra("email");


		// Display UI based on the role
		displayUIBasedOnRole(ulogaId);

		// Set onClick listeners for buttons
		setButtonOnClickListeners(username, password, enteredEmail, urlSlike, ime, prezime, verificiran);

		// Set touch listener for map fragment
		if (mapFragment != null) {
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

			mapFragment.getMapAsync(this);
		}

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);





	}

	private void logout() {
		SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear(); // or editor.remove("isLoggedIn");
		editor.apply();

		// Redirect to the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		// Do nothing, or show a toast message
		Toast.makeText(this, "BACK BUTTON JE ZABRANJEN, KORISTITE ODJAVI SE ", Toast.LENGTH_SHORT).show();
	}



	private void displayUIBasedOnRole(int ulogaId) {
		switch (ulogaId) {
			case 1:
				adminButton.setVisibility(View.GONE);
				terminifrizerButton.setVisibility(View.VISIBLE);
				terminiButton.setVisibility(View.GONE);
				rezervirajButton.setVisibility(View.GONE);
				prijavaButton.setVisibility(View.GONE);
				logoutButton.setVisibility(View.VISIBLE);
				terminifrizerButton2.setVisibility(View.GONE);
				postavkeButton.setVisibility(View.GONE);
				buttonOpenQrScanner.setVisibility(View.VISIBLE);
				break;
			case 2:
				adminButton.setVisibility(View.VISIBLE);
				terminiButton.setVisibility(View.GONE);
				terminifrizerButton.setVisibility(View.GONE);
				terminifrizerButton2.setVisibility(View.VISIBLE);

				rezervirajButton.setVisibility(View.GONE);
				prijavaButton.setVisibility(View.GONE);
				logoutButton.setVisibility(View.VISIBLE);
				buttonOpenQrScanner.setVisibility(View.VISIBLE);
				break;
			case 3:
				terminiButton.setVisibility(View.VISIBLE);
				profilButton.setVisibility(View.VISIBLE);
				rezervirajButton.setVisibility(View.VISIBLE);
				rezervirajButton.setEnabled(true);
				logoutButton.setVisibility(View.VISIBLE);
				prijavaButton.setVisibility(View.GONE);
				adminButton.setVisibility(View.GONE);
				terminifrizerButton.setVisibility(View.GONE);
				terminifrizerButton2.setVisibility(View.GONE);

				buttonOpenQrScanner.setVisibility(View.GONE);




				break;
			default: // ulogaId 1 or any other value
				terminiButton.setVisibility(View.VISIBLE);
				profilButton.setVisibility(View.VISIBLE);
				rezervirajButton.setVisibility(View.VISIBLE);
				rezervirajButton.setEnabled(true);
				logoutButton.setVisibility(View.VISIBLE);
				prijavaButton.setVisibility(View.GONE);
				adminButton.setVisibility(View.GONE);

				prijavaButton.setEnabled(false);
				break;
		}
	}

	private void setButtonOnClickListeners(String username, String password, String enteredEmail, String urlSlike,String ime, String prezime, boolean verificiran) {
		profilButton.setOnClickListener(view -> {
			Intent intent = new Intent(MainActivity.this, ProfilKorisnikaActivity.class);
			intent.putExtra("username", username);
			intent.putExtra("lozinka", password);
			intent.putExtra("userEmail", enteredEmail);
			intent.putExtra("urlSlike", urlSlike);
			intent.putExtra("ime", ime);
			intent.putExtra("prezime", prezime);
			intent.putExtra("verificiran", verificiran);


			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});

		prijavaButton.setOnClickListener(view -> {
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});

		logoutButton.setOnClickListener(view -> handleLogout());

		postavkeButton.setOnClickListener(view -> {
			Intent intent = new Intent(MainActivity.this, PostavkeAplikacijeActivity.class);
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});

		adminButton.setOnClickListener(view -> {
			Intent intent = new Intent(MainActivity.this, AdminActivity.class);
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});

		buttonListaRezervacija.setOnClickListener(v -> {
			Intent intent = new Intent(MainActivity.this, ListaRezerviranihUslugaActivity.class);
			intent.putExtra("username", username); // Ensure this key matches
			startActivity(intent);
		});


		buttonListaRezervacijafrizer.setOnClickListener(v -> {
			Intent intent = new Intent(MainActivity.this, ListaRezerviranihUslugaActivity4.class);

			intent.putExtra("ime", ime); // Ensure this key matches
			intent.putExtra("prezime", prezime); // Ensure this key matches

			intent.putExtra("username", username); // Ensure this key matches
			startActivity(intent);
		});


		buttonListaRezervacijafrizer2.setOnClickListener(v -> {
			Intent intent = new Intent(MainActivity.this, ListaRezerviranihUslugaActivity3.class);

			intent.putExtra("ime", ime); // Ensure this key matches
			intent.putExtra("prezime", prezime); // Ensure this key matches

			intent.putExtra("username", username); // Ensure this key matches
			startActivity(intent);
		});


		rezervirajButton.setOnClickListener(view -> {
			if (verificiran) {
				Intent intent = new Intent(MainActivity.this, RadniciActivity.class);
				intent.putExtra("username", username); // Ensure this key is consistent
				intent.putExtra("urlSlike", urlSlike); // Ensure this key is consistent

				startActivity(intent);

				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			} else {
				rezervirajButton.setText("račun nije verificiran");
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkLoginState();
	}

	private void checkLoginState() {
		SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
		boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
		if (!isLoggedIn) {
			// Redirect to the login activity
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void handleLogout() {
		logout();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		LatLng location = new LatLng(45.83325561013758, 17.388860417173003);
		googleMap.addMarker(new MarkerOptions().position(location).title("Frizerski salon Boris"));
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
	}
}
