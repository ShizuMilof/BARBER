package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber.model.Radnik;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RadniciActivity extends AppCompatActivity {
	private RecyclerView recyclerView;
	private RadnikAdapter adapter;
	private OkHttpClient okHttpClient;
	private String username; // Declare username as a class-level variable
	private List<Radnik> radnici; // List to store all Radnik items
	private List<Radnik> filteredList; // List to store filtered Radnik items
	private EditText editTextSearch; // Search EditText

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radnici);

		// Retrieve the username from the Intent
		Intent intent = getIntent();
		if (intent != null && intent.hasExtra("username")) {
			username = intent.getStringExtra("username");
		} else {
			Log.e("RadniciActivity", "Username not found in Intent extras");
			username = "DefaultUsername"; // or handle it appropriately
		}

		// Log the retrieved username
		Log.d("RadniciActivity", "Retrieved username: " + username);

		// Initialize RecyclerView
		recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		int spaceHeight = getResources().getDimensionPixelSize(R.dimen.space_height);
		recyclerView.addItemDecoration(new SpaceItemDecoration(spaceHeight));

		// Initialize HTTP client
		initializeUnsafeHttpClient();

		// Initialize adapter
		radnici = new ArrayList<>();
		filteredList = new ArrayList<>();
		adapter = new RadnikAdapter(filteredList);
		recyclerView.setAdapter(adapter);

		// Initialize search EditText
		editTextSearch = findViewById(R.id.editTextSearch);
		editTextSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				filterRadniciList(charSequence.toString());
			}

			@Override
			public void afterTextChanged(Editable editable) {}
		});

		adapter.setOnItemClickListener(new RadnikAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(Radnik radnik) {
				Intent intent = new Intent(RadniciActivity.this, FrizerskeUslugeActivity.class);
				intent.putExtra("frizer_ime", radnik.getIme() + " " + radnik.getPrezime());
				intent.putExtra("username", username);
				startActivity(intent);
			}
		});

		// Make the activity full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Fetch data for RecyclerView
		fetchItems();
	}

	private void initializeUnsafeHttpClient() {
		final TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					@Override
					public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

					@Override
					public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return new java.security.cert.X509Certificate[]{};
					}
				}
		};

		try {
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			okHttpClient = new OkHttpClient.Builder()
					.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
					.hostnameVerifier((hostname, session) -> true)
					.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void fetchItems() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://172.20.10.3:7194/api/")
				.addConverterFactory(GsonConverterFactory.create())
				.client(okHttpClient)
				.build();

		MyApi apiService = retrofit.create(MyApi.class);
		Call<List<Radnik>> call = apiService.getYourData();

		call.enqueue(new Callback<List<Radnik>>() {
			@Override
			public void onResponse(Call<List<Radnik>> call, Response<List<Radnik>> response) {
				if (response.isSuccessful() && response.body() != null) {
					radnici = response.body();
					filteredList.clear();
					filteredList.addAll(radnici); // Initially, filtered list is the same as the original list
					adapter.updateData(filteredList); // Update adapter with the filtered list
				} else {
					Toast.makeText(RadniciActivity.this, "Response unsuccessful or empty.", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<List<Radnik>> call, Throwable t) {
				Log.e("RadniciActivity", "Error fetching Radnik data: " + t.getMessage(), t);
			}
		});
	}

	private void filterRadniciList(String searchText) {
		filteredList.clear();
		for (Radnik radnik : radnici) {
			if (radnik.getIme().toLowerCase().contains(searchText.toLowerCase()) ||
					radnik.getPrezime().toLowerCase().contains(searchText.toLowerCase())) {
				filteredList.add(radnik);
			}
		}
		adapter.updateData(filteredList); // Update adapter with the filtered list
	}
}
