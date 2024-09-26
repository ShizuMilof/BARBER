package com.example.barber;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RadniciRegisterActivity extends AppCompatActivity {

	private static final int REQUEST_CAMERA_PERMISSION = 100;
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_IMAGE_PICK = 2;

	private ImageView imageView;

	private EditText editTextImeRadnik, editTextPrezimeRadnik, editTextEmailRadnik, editTextUsernameRadnik, editTextPasswordRadnik;
	private Button buttonDodajRadnika, buttonChooseImage;
	private Uri imageUri;
	private StorageReference mStorageRef;
	private OkHttpClient client;
	private String timeStamp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radnici_registriraj);

		mStorageRef = FirebaseStorage.getInstance().getReference("profile_images");
		client = new OkHttpClient();

		editTextImeRadnik = findViewById(R.id.editTextImeRadnik);
		editTextPrezimeRadnik = findViewById(R.id.editTextPrezimeRadnik);
		editTextEmailRadnik = findViewById(R.id.editTextEmailRadnik);
		editTextUsernameRadnik = findViewById(R.id.editTextUsernameRadnik);
		editTextPasswordRadnik = findViewById(R.id.editTextPasswordRadnik);
		imageView = findViewById(R.id.imagePreview);
		buttonDodajRadnika = findViewById(R.id.buttonRegister);
		buttonChooseImage = findViewById(R.id.buttonChooseImage);
		timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

		buttonChooseImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openFileChooser();
			}
		});
		initializeUnsafeHttpClient();

		buttonDodajRadnika.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String imeRadnik = editTextImeRadnik.getText().toString();
				String prezimeRadnik = editTextPrezimeRadnik.getText().toString();
				String emailRadnik = editTextEmailRadnik.getText().toString();
				String usernameRadnik = editTextUsernameRadnik.getText().toString();
				String passwordRadnik = editTextPasswordRadnik.getText().toString();

				if (imeRadnik.isEmpty() || prezimeRadnik.isEmpty() || emailRadnik.isEmpty() || usernameRadnik.isEmpty() || passwordRadnik.isEmpty()) {
					Toast.makeText(RadniciRegisterActivity.this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
				} else if (imageUri == null) {
					Toast.makeText(RadniciRegisterActivity.this, "Molimo vas odaberite sliku", Toast.LENGTH_SHORT).show();
				} else {
					uploadImageAndRegisterUser(emailRadnik, passwordRadnik, usernameRadnik, imeRadnik, prezimeRadnik, 2);
				}
			}
		});

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	private void openFileChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_IMAGE_PICK);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
			imageUri = data.getData();
			imageView.setImageURI(imageUri);
		}
	}
	private void initializeUnsafeHttpClient() {
		final TrustManager[] trustAllCerts = new TrustManager[]{
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

		try {
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			client = new OkHttpClient.Builder()
					.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
					.hostnameVerifier((hostname, session) -> true)
					.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	private void uploadImageAndRegisterUser(final String emailReg, final String passwordReg, final String usernameReg, final String firstName, final String lastName, final int ulogaId) {
		if (imageUri != null) {
			StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + ".jpg");
			fileReference.putFile(imageUri)
					.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
						@Override
						public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
							fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
								@Override
								public void onSuccess(Uri uri) {
									String imageUrl = uri.toString();
									Log.d("FirebaseStorage", "Image URL: " + imageUrl); // Log the URL
									registerUser(emailReg, passwordReg, usernameReg, firstName, lastName, ulogaId, imageUrl);
								}
							});
						}
					})
					.addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							Toast.makeText(RadniciRegisterActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
		} else {
			Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
		}
	}

	private void registerUser(final String emailReg, final String passwordReg, final String usernameReg, final String firstName, final String lastName, final int ulogaId, final String imageUrl) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("username", usernameReg);
			jsonObject.put("ime", firstName);
			jsonObject.put("prezime", lastName);
			jsonObject.put("email", emailReg);
			jsonObject.put("lozinka", passwordReg);
			jsonObject.put("ulogeId", 1); // Set ulogeId to 2 for the worker role
			jsonObject.put("UrlSlike", imageUrl);
			Log.d("RegisterActivity", "JSON Request: " + jsonObject.toString()); // Log the JSON
		} catch (JSONException e) {
			e.printStackTrace();
		}

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		RequestBody body = RequestBody.create(JSON, jsonObject.toString());
		Request request = new Request.Builder()
				.url("https://172.20.10.3:7194/api/Register")
				.post(body)
				.build();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.e("Registracija", "Registracija nije uspjela: " + e.getMessage()); // Log the error
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String responseData = response.body().string();

				if (response.isSuccessful()) {
					runOnUiThread(() -> Toast.makeText(RadniciRegisterActivity.this, "Radnik uspješno registriran!", Toast.LENGTH_SHORT).show());
					startActivity(new Intent(RadniciRegisterActivity.this, LoginActivity.class));
					finish();
				} else {
					runOnUiThread(() -> Toast.makeText(RadniciRegisterActivity.this, "Registracija nije uspjela: " + responseData, Toast.LENGTH_LONG).show());

					String errorMessage = "Registracija nije uspjela: " + responseData;
					Log.e("Registracija", errorMessage); // Log the error
					runOnUiThread(() -> Toast.makeText(RadniciRegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show());
				}
			}
		});
	}
}
