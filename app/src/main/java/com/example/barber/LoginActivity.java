package com.example.barber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

	private EditText emailEditText;
	private EditText passwordEditText;
	private OkHttpClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitylogin);

		emailEditText = findViewById(R.id.editTextEmail);
		passwordEditText = findViewById(R.id.editTextPassword);
		Button loginButton = findViewById(R.id.buttonLogin);
		Button buttonOpenQrScanner = findViewById(R.id.buttonOpenQrScanner);

		loginButton.setOnClickListener(view -> attemptLogin());

		Button btnOpenRegisterActivity = findViewById(R.id.btnOpenRegisterActivity);
		btnOpenRegisterActivity.setOnClickListener(v -> {
			Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
		});
		buttonOpenQrScanner.setVisibility(View.GONE);


		buttonOpenQrScanner.setOnClickListener(v -> {
			Intent intent = new Intent(LoginActivity.this, QrCodeScannerActivity.class);
			startActivity(intent);
		});
	}

	@Override
	public void onBackPressed() {
		// Do nothing, or show a toast message
		Toast.makeText(this, "BACK BUTTON NIJE DOSTUPAN", Toast.LENGTH_SHORT).show();
	}

	private OkHttpClient getUnsafeOkHttpClient() {
		try {
			final TrustManager[] trustAllCerts = new TrustManager[] {
					new X509TrustManager() {
						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
						}

						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
						}

						@Override
						public java.security.cert.X509Certificate[] getAcceptedIssuers() {
							return new java.security.cert.X509Certificate[]{};
						}
					}
			};


			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
			builder.hostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			return builder.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void attemptLogin() {
		String email = emailEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();

		if (email.isEmpty() || password.isEmpty()) {
			Toast.makeText(this, "POLJA ZA EMAIL I LOZINKU MORAJU BITI ISPUNJENA", Toast.LENGTH_SHORT).show();
		} else {
			authenticateUser(email, password);
		}
	}

	private void authenticateUser(String email, String password) {
		String url = "https://172.20.10.3:7194/api/Barber/Authenticate";
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("email", email);
			jsonObject.put("lozinka", password);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestBody body = RequestBody.create(JSON, jsonObject.toString());
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.build();

		OkHttpClient client = getUnsafeOkHttpClient();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Prijava neuspješna: " + e.getMessage(), Toast.LENGTH_LONG).show());
				Log.e("LoginActivity", "Login failed: " + e.getMessage());
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()) {
					String responseData = response.body().string();
					try {
						JSONObject jsonResponse = new JSONObject(responseData);
						String email = jsonResponse.optString("email");
						String password = jsonResponse.optString("lozinka");
						String username = jsonResponse.optString("username");
						Integer ulogeId = jsonResponse.optInt("ulogeId");
						String urlSlike = jsonResponse.optString("urlSlike");
						String ime = jsonResponse.optString("ime");
						String prezime = jsonResponse.optString("prezime");
						boolean verificiran = jsonResponse.optBoolean("verificiran");


						SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putBoolean("isLoggedIn", true);
						editor.putString("email", email);
						editor.putString("username", username);
						editor.putString("urlSlike", urlSlike);
						editor.apply();

		//				runOnUiThread(() -> Toast.makeText(LoginActivity.this, "PRIJAVA USPJEŠNA", Toast.LENGTH_LONG).show());

						Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
						loginIntent.putExtra("email", email);
						loginIntent.putExtra("lozinka", password);
						loginIntent.putExtra("username", username);
						loginIntent.putExtra("ulogeId", ulogeId);
						loginIntent.putExtra("verificiran", verificiran);
						loginIntent.putExtra("urlSlike", urlSlike);
						loginIntent.putExtra("ime", ime);
						loginIntent.putExtra("prezime", prezime);

						startActivity(loginIntent);
						finish();
					} catch (JSONException e) {
						e.printStackTrace();
						runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Error parsing response", Toast.LENGTH_LONG).show());
					}
				} else {
					String errorMessage;
					if (response.code() == 401) {
						errorMessage = "Lozinka ili email nisu točni";
					} else {

						try {
							String errorBody = response.body().string();
							errorMessage = "Error: " + response.code() + " " + response.message() + "\n" + errorBody;
						} catch (IOException e) {
							errorMessage = "Unknown error: " + response.code() + " " + response.message();
						}
					}

					Log.e("LoginActivity", errorMessage);

					String finalErrorMessage = errorMessage; // Declare a final variable to use in runOnUiThread
					runOnUiThread(() -> Toast.makeText(LoginActivity.this, finalErrorMessage, Toast.LENGTH_LONG).show());
				}
			}

		});

	}
}
