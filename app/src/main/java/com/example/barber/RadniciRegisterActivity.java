package com.example.barber;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.barber.model.Radnik;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RadniciRegisterActivity extends AppCompatActivity {

	private static final int REQUEST_CAMERA_PERMISSION = 100;
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_IMAGE_PICK = 2;


	private ImageView imageView;

	private EditText editTextImeRadnik, editTextPrezimeRadnik, editTextEmailRadnik, editTextUsernameRadnik;
	private Button buttonDodajRadnika, buttonUploadImage, buttonChooseImage;


	private DatabaseReference radniciRef;
	private FirebaseAuth mAuth;
	private String imageUrl;
	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());


	private StorageReference mStorageRef;
	private String imePrezimeReg;

	private ListView listView;
    private ImageView myimage;



	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radnici_registriraj);
		imageView = findViewById(R.id.imagePreview);
		mStorageRef  = FirebaseStorage.getInstance().getReference();
		editTextImeRadnik = findViewById(R.id.editTextImeRadnik);
		buttonDodajRadnika = findViewById(R.id.buttonRegister);
		buttonChooseImage = findViewById(R.id.buttonChooseImage);
		ImageView imagePreview = findViewById(R.id.imagePreview);
		imagePreview.setImageResource(R.drawable.avatar);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



		buttonChooseImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				chooseImage();
			}
		});



		buttonDodajRadnika.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String imePrezimeReg = editTextImeRadnik.getText().toString();



				if ( imePrezimeReg.isEmpty() ) {
					Toast.makeText(RadniciRegisterActivity.this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
				} else
				{

					addUserToDatabase(imePrezimeReg, imageUrl);

				}
			}
		});

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		radniciRef = database.getReference("radnici");
	}

	public void chooseImage() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, REQUEST_IMAGE_PICK);
	}


	public void captureImage(View view) {

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
		} else {

			startCamera();
		}
	}

	private void startCamera() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			// Snimanje slike s kamere
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap imageBitmap = (Bitmap) extras.get("data");

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] imageData = baos.toByteArray();

				ImageView imagePreview = findViewById(R.id.imagePreview);
				imagePreview.setVisibility(View.VISIBLE);
				imagePreview.setImageBitmap(imageBitmap);

				// Upload the image to Firebase
				uploadToFirebase(imageData);
			}
		} else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
			// Odabir slike iz galerije
			if (data != null && data.getData() != null) {
				Uri selectedImageUri = data.getData();
				try {
					Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					byte[] imageData = baos.toByteArray();

					ImageView imagePreview = findViewById(R.id.imagePreview);
					imagePreview.setVisibility(View.VISIBLE);
					imagePreview.setImageBitmap(imageBitmap);

					// Upload the image to Firebase
					uploadToFirebase(imageData);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == REQUEST_CAMERA_PERMISSION) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

				startCamera();

			} else {

				Toast.makeText(this, "Dozvola za kameru nije odobrena.", Toast.LENGTH_SHORT).show();
			}
		}
	}



	private void uploadToFirebase(byte[] bb) {

		String fileName =timeStamp +  "_radnik_image.jpg";
		StorageReference imageRef = mStorageRef.child("images/" + fileName);


		imageRef.putBytes(bb)
				.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
						Toast.makeText(RadniciRegisterActivity.this, "Slika je uspješno spremljena.", Toast.LENGTH_SHORT).show();
						imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
							@Override
							public void onSuccess(Uri downloadUri) {
								imageUrl = downloadUri.toString();
							}
						});
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {

						Toast.makeText(RadniciRegisterActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
					}
				});
	}



	private void addUserToDatabase(String imePrezime, String imageUrl) {
		Log.d("Database", "Dodavanje radnika u bazu podataka...");
		DatabaseReference radniciRef = FirebaseDatabase.getInstance().getReference("radnici");


		Radnik radnik = new Radnik(imePrezime, this.imageUrl);



		String radnikId = radnik.getImePrezime();

// Provjerite je li radnikId različit od null i prazan string prije nego što pristupite bazi podataka
		if (radnikId != null && !radnikId.isEmpty()) {
			radniciRef.child(radnikId).setValue(radnik)
					.addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if (task.isSuccessful()) {
								Log.d("Database", "Radnik uspješno dodan u bazu podataka.");
								Toast.makeText(RadniciRegisterActivity.this, "Uspješno ste registrirali radnika!", Toast.LENGTH_SHORT).show();
								Intent loginIntent = new Intent(RadniciRegisterActivity.this, RadniciRegisterActivity.class);
								startActivity(loginIntent);
								finish();
							} else {
								Log.d("Database", "Greška prilikom dodavanja radnika u bazu podataka.", task.getException());
							}
						}
					});
		} else {
			// Ako je radnikId null ili prazan, obavijestite korisnika ili dodajte odgovarajuće rukovanje
			Log.d("Database", "RadnikId je null ili prazan.");
			Toast.makeText(RadniciRegisterActivity.this, "Greška prilikom registracije radnika. Molimo pokušajte ponovno.", Toast.LENGTH_SHORT).show();
		}

	}

}


