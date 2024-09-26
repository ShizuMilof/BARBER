package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
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

public class
VerifikacijaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RadnikAdapter1 adapter;
    private OkHttpClient okHttpClient;
    private String username; // Declare username as a class-level variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikacija);

        // Retrieve the username from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("username")) {
            username = intent.getStringExtra("username");
        } else {
            Log.e("VerifikacijaActivity", "Username not found in Intent extras");
            username = "DefaultUsername"; // or handle it appropriately
        }

        // Log the retrieved username
        Log.d("VerifikacijaActivity", "Retrieved username: " + username);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spaceHeight = getResources().getDimensionPixelSize(R.dimen.space_height);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spaceHeight));


        initializeUnsafeHttpClient();

        // Show Toast with username


        // Initialize adapter
        adapter = new RadnikAdapter1(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

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

        MyApi3 apiService = retrofit.create(MyApi3.class);
        Call<List<Radnik>> call = apiService.getYourData();

        call.enqueue(new Callback<List<Radnik>>() {
            @Override
            public void onResponse(Call<List<Radnik>> call, Response<List<Radnik>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Radnik> radnici = response.body();
                    adapter.updateData(radnici);
                } else {
                    Toast.makeText(VerifikacijaActivity.this, "Response unsuccessful or empty.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Radnik>> call, Throwable t) {
                Log.e("VerifikacijaActivity", "Error fetching Radnik data: " + t.getMessage(), t);
                Toast.makeText(VerifikacijaActivity.this, "Error fetching data: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
