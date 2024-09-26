package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barber.model.Appointment;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaRezerviranihUslugaActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Appointment> listaRezervacija = new ArrayList<>();
    private CustomAdapter2 adapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rezerviranih_usluga);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://172.20.10.3:7194/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient())
                .build();
        apiService = retrofit.create(ApiService.class);

        listView = findViewById(R.id.listViewRezervacije);
        adapter = new CustomAdapter2(this, listaRezervacija, apiService);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        Log.d("LISTAREZERVIRANIHUSLUGAACTIVITY", "USERNAME JE a ovo je lista rez usluga activiti bez broja " + username);

        if (username != null) {
            fetchAppointments(username);
        } else {
            Toast.makeText(this, "Username is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAppointments(String username) {
        Call<List<Appointment>> call = apiService.getAppointmentsByUser(username);
        call.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Appointment>> call, @NonNull Response<List<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_SUCCESS", "Appointments fetched: " + response.body().size());
                    listaRezervacija.clear(); // Clear the existing data
                    listaRezervacija.addAll(response.body());
                    adapter.notifyDataSetChanged(); // Notify the adapter
                } else {
                    Log.d("API_ERROR", "Failed to fetch appointments: " + response.code());
                    Toast.makeText(ListaRezerviranihUslugaActivity.this, "Failed to fetch appointments", Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("API_ERROR_BODY", "Response body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("API_ERROR_BODY_READ", "Error reading response body", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Appointment>> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", "API call failed", t);
                Toast.makeText(ListaRezerviranihUslugaActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
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

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
