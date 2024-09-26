package com.example.barber;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

	private static final int PICK_IMAGE_REQUEST = 1;
	private EditText editTextEmail, editTextUsername, editTextPassword, editTextName, editTextSurname;
	private CircleImageView imageViewProfile;
	private Uri imageUri;
	private OkHttpClient client;
	private StorageReference storageReference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activityregister);

		editTextEmail = findViewById(R.id.editTextEmail);
		editTextUsername = findViewById(R.id.editTextUsername);
		editTextPassword = findViewById(R.id.editTextPassword);
		editTextName = findViewById(R.id.editTextName);
		editTextSurname = findViewById(R.id.editTextSurname);
		imageViewProfile = findViewById(R.id.imageViewProfile);

		storageReference = FirebaseStorage.getInstance().getReference("profile_images");

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		initializeUnsafeHttpClient();

		Button buttonRegister = findViewById(R.id.buttonRegister2);
		buttonRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String emailReg = editTextEmail.getText().toString();
				String usernameReg = editTextUsername.getText().toString();
				String passwordReg = editTextPassword.getText().toString();
				String firstName = editTextName.getText().toString();
				String lastName = editTextSurname.getText().toString();

				if (emailReg.isEmpty() || passwordReg.isEmpty() || usernameReg.isEmpty()) {
					Toast.makeText(RegisterActivity.this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
				} else if (imageUri == null) {
					Toast.makeText(RegisterActivity.this, "MOLIMO VAS ODABERITE SLIKU", Toast.LENGTH_SHORT).show();
				} else {
					uploadImageAndRegisterUser(emailReg, passwordReg, usernameReg, firstName, lastName, 3);
				}
			}
		});

		Button buttonChooseImage = findViewById(R.id.buttonChooseImage);
		buttonChooseImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openFileChooser();
			}
		});
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

	private void openFileChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, PICK_IMAGE_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			imageUri = data.getData();
			imageViewProfile.setImageURI(imageUri);
		}
	}

	private void uploadImageAndRegisterUser(final String emailReg, final String passwordReg, final String usernameReg, final String firstName, final String lastName, final int ulogaId) {
		if (imageUri != null) {
			StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
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
							Toast.makeText(RegisterActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
			jsonObject.put("ulogeId", 3); // Ensure this is always set to 3
			jsonObject.put("UrlSlike", imageUrl); // Change this line
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
					runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Uspješno ste se registrirali!", Toast.LENGTH_SHORT).show());
					startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
					finish();
				} else {
					runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Registracija nije uspjela: " + responseData, Toast.LENGTH_LONG).show());

					String errorMessage = "Registracija nije uspjela: " + responseData;
					Log.e("Registracija", errorMessage); // Log the error
					runOnUiThread(() -> Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show());
				}
			}
		});
	}
}
