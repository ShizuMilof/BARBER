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

import com.example.barber.model.Usluge;
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

public class UslugaRegisterActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;


    private ImageView imageView;

    private EditText editTextImeUsluge, editTextCijenaUsluge, editTextTrajanjeUsluge, editTextUsernameRadnik;
    private Button buttonDodajRadnika, buttonUploadImage, buttonChooseImage;


    private DatabaseReference radniciRef;
    private FirebaseAuth mAuth;
    private String imageUrl;
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());


    private StorageReference mStorageRef;
    private String imeUsluge;
    private String cijenaUsluge;
    private ListView trajanjeUsluge;
    private ImageView myimage;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usluge_registriraj);
        imageView = findViewById(R.id.imagePreview);
        mStorageRef  = FirebaseStorage.getInstance().getReference();
        editTextImeUsluge = findViewById(R.id.editTextImeUsluge);
        editTextCijenaUsluge = findViewById(R.id.editTextCijenaUsluge);
        editTextTrajanjeUsluge = findViewById(R.id.editTextTrajanjeUsluge);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        buttonDodajRadnika = findViewById(R.id.buttonDodajRadnika);
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
                String imeUsluge = editTextImeUsluge.getText().toString();
                String cijenaUsluge = editTextCijenaUsluge.getText().toString();
                String trajanjeUsluge = editTextTrajanjeUsluge.getText().toString();

                if (imeUsluge.isEmpty() || cijenaUsluge.isEmpty() || trajanjeUsluge.isEmpty()) {
                    Toast.makeText(UslugaRegisterActivity.this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
                } else {
                    // Validate TrajanjeUsluge
                    int trajanje;
                    try {
                        trajanje = Integer.parseInt(trajanjeUsluge);
                        if (trajanje > 20) {
                            Toast.makeText(UslugaRegisterActivity.this, "Trajanje usluge ne može biti veće od 20", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(UslugaRegisterActivity.this, "Unesite ispravan broj za trajanje usluge", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    addUserToDatabase(imeUsluge, cijenaUsluge, String.valueOf(trajanje), imageUrl);
                }
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        radniciRef = database.getReference("frizerskeusluge");
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
            // Get the captured image as a Bitmap
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                // Convert Bitmap to byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                // Display the image in the ImageView
                ImageView imagePreview = findViewById(R.id.imagePreview);
                imagePreview.setVisibility(View.VISIBLE);
                imagePreview.setImageBitmap(imageBitmap);

                // Upload the image to Firebase
                uploadToFirebase(imageData);
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            // Handle the image picked from the gallery
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                    // Convert Bitmap to byte array
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();

                    // Display the image in the ImageView
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

        String fileName =timeStamp +  "usluga.jpg";
        StorageReference imageRef = mStorageRef.child("images/" + fileName);


        imageRef.putBytes(bb)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(UslugaRegisterActivity.this, "Slika je uspješno spremljena.", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(UslugaRegisterActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void addUserToDatabase(String imeUsluge, String cijenaUsluge, String trajanjeUsluge, String imageUrl) {
        Log.d("Database", "Dodavanje usluge u bazu podataka...");
        DatabaseReference radniciRef = FirebaseDatabase.getInstance().getReference("frizerskeusluge");

        Usluge usluga = new Usluge(imeUsluge, cijenaUsluge,trajanjeUsluge, this.imageUrl);



        String radnikId = usluga.getImeUsluge();

        radniciRef.child(radnikId).setValue(usluga)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Database", "Usluga uspješno dodana u bazu podataka.");
                            Toast.makeText(UslugaRegisterActivity.this, "Uspješno ste registrirali radnika!", Toast.LENGTH_SHORT).show();
                            Intent loginIntent = new Intent(UslugaRegisterActivity.this, UslugaRegisterActivity.class);
                            startActivity(loginIntent);
                            recreate();
                        } else {
                            Log.d("Database", "Greška prilikom dodavanja radnika u bazu podataka.", task.getException());
                        }
                    }
                });

    }

}


