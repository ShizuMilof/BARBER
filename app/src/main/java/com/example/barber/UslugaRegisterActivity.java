package com.example.barber;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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

public class UslugaRegisterActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private ImageView imageView;
    private EditText editTextImeUsluge, editTextCijenaUsluge, editTextTrajanjeUsluge;
    private Button buttonDodajUslugu, buttonChooseImage;
    private OkHttpClient client;

    private StorageReference mStorageRef;
    private String imageUrl;
    private Uri imageUri;
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usluge_registriraj);

        mStorageRef = FirebaseStorage.getInstance().getReference("usluge_images");
        client = new OkHttpClient();

        imageView = findViewById(R.id.imagePreview);
        editTextImeUsluge = findViewById(R.id.editTextImeUsluge);
        editTextCijenaUsluge = findViewById(R.id.editTextCijenaUsluge);
        editTextTrajanjeUsluge = findViewById(R.id.editTextTrajanjeUsluge);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        buttonDodajUslugu = findViewById(R.id.buttonDodajRadnika);
        initializeUnsafeHttpClient();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        buttonDodajUslugu.setOnClickListener(new View.OnClickListener() {
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

                    // Upload the image and register the service
                    if (imageUri != null) {
                        uploadImageAndRegisterService(imeUsluge, cijenaUsluge, trajanje);
                    } else {
                        Toast.makeText(UslugaRegisterActivity.this, "Odaberite sliku", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
    private void clearFields() {
        editTextImeUsluge.setText("");
        editTextCijenaUsluge.setText("");
        editTextTrajanjeUsluge.setText("");
        imageView.setImageResource(R.drawable.avatar); // Reset to default or clear the image view
        imageUri = null; // Reset image URI
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
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(imageBitmap);

                // Store image data
                uploadToFirebase(imageData);
            }
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
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
    private void uploadToFirebase(byte[] imageData) {
        String fileName = timeStamp + "_usluga_image.jpg";
        StorageReference imageRef = mStorageRef.child("images/" + fileName);

        imageRef.putBytes(imageData)
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

    private void uploadImageAndRegisterService(final String naziv, final String cijena, final int trajanje) {
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
                                    registerService(naziv, cijena, trajanje, imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UslugaRegisterActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerService(final String naziv, final String cijena, final int trajanje, final String imageUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("naziv", naziv);
            jsonObject.put("cijena", Integer.parseInt(cijena));
            jsonObject.put("trajanje", trajanje);
            jsonObject.put("urlSlike", imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("https://172.20.10.3:7194/api/Usluge") // Replace with your API URL
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(UslugaRegisterActivity.this, "Registracija nije uspjela: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(UslugaRegisterActivity.this, "Usluga uspješno registrirana!", Toast.LENGTH_SHORT).show();
                        clearFields(); // Clear all input fields and image view
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(UslugaRegisterActivity.this, "Registracija nije uspjela: " + responseData, Toast.LENGTH_LONG).show());
                }
            }

        });
    }
}
