package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.barber.model.Usluge;
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

public class FrizerskeUslugeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomAdapter1 adapter;
    private String username;
    private OkHttpClient okHttpClient;
    private List<Usluge> usluge; // Define a list of services
    private List<Usluge> selectedServices; // Define a list to store selected services
    private EditText editTextSearchUsluge; // Add search EditText
    private List<Usluge> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frizerske_usluge); // Ispravljeno ime layouta

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spaceHeight = getResources().getDimensionPixelSize(R.dimen.space_height);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spaceHeight));

        editTextSearchUsluge = findViewById(R.id.editTextSearchUsluge); // Initialize search EditText

        String username = getIntent().getStringExtra("username");
        String frizer_ime = getIntent().getStringExtra("frizer_ime");

        usluge = new ArrayList<>();
        selectedServices = new ArrayList<>();

        // Initialize adapter
        adapter = new CustomAdapter1(usluge);
        recyclerView.setAdapter(adapter);

        // Initialize unsafe HTTP client
        initializeUnsafeHttpClient();

        // Fetch items from API
        fetchItems();

        // Set up next button click listener
        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Usluge> selectedServices = adapter.getSelectedServices();
                if (selectedServices.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "MORATE ODABRATI NAJMANJE 1 USLUGU", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed with these selected services
                    Intent intent = new Intent(FrizerskeUslugeActivity.this, VrijemeDatumUslugeActivity.class);
                    intent.putParcelableArrayListExtra("selectedServices", new ArrayList<Parcelable>(selectedServices));
                    intent.putExtra("username", username); // Pass the username
                    intent.putExtra("frizer_ime", frizer_ime); // Pass the frizer_ime
                    startActivity(intent);
                }
            }
        });

        // Set up search functionality
        editTextSearchUsluge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterUslugeList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
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

        MyApi2 apiService = retrofit.create(MyApi2.class);
        Call<List<Usluge>> call = apiService.getYourData();  // Ispravljeno na Usluge

        call.enqueue(new Callback<List<Usluge>>() {
            @Override
            public void onResponse(Call<List<Usluge>> call, Response<List<Usluge>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    usluge = response.body();
                    filteredList.clear();
                    filteredList.addAll(usluge); // Initially, filtered list is the same as the original list
                    adapter.updateData(filteredList); // Update adapter with the filtered list
                } else {
                    Log.e("FrizerskeUsluge", "Error fetching data");
                }
            }

            @Override
            public void onFailure(Call<List<Usluge>> call, Throwable t) {
                Log.e("FrizerskeUsluge", "Error fetching usluge data: " + t.getMessage(), t);
            }
        });
    }

    private void filterUslugeList(String searchText) {
        filteredList.clear();
        for (Usluge usluge : usluge) {
            if (usluge.getNaziv().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(usluge);
            }
        }
        adapter.updateData(filteredList); // Update adapter with the filtered list
    }
}
