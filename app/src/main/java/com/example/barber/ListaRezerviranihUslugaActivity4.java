package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barber.model.Appointment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ListaRezerviranihUslugaActivity4 extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Appointment> listaRezervacija = new ArrayList<>();
    private CustomAdapter3 adapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rezerviranih_usluga4);

        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://172.20.10.3:7194/") // Osigurajte da je URL ispravan
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        apiService = retrofit.create(ApiService.class);


        listView = findViewById(R.id.listViewRezervacije);
        adapter = new CustomAdapter3(this, listaRezervacija, apiService);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        String ime = intent.getStringExtra("ime");
        String prezime = intent.getStringExtra("prezime");
        Log.d("LISTAREZERVIRANIHUSLUGAACTIVITY4   ", "USERNAME JE: "  + prezime);

        if (ime != null && prezime != null) {
            String frizerIme = ime + " " + prezime;
            fetchAppointments(frizerIme);
        } else {
            // Handle the case where ime or prezime is null if necessary
        }
    }

    public void fetchAppointments(String frizerIme) {
        apiService.getAppointmentsByFrizer(frizerIme).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Appointment> appointments = response.body();
                    listaRezervacija.clear();

                    // Filter appointments where JeOdraden is false
                    for (Appointment appointment : appointments) {
                        if (!appointment.isJeOdraden()) {
                            listaRezervacija.add(appointment);
                        }
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ListaRezerviranihUslugaActivity4.this, "Failed to fetch appointments", Toast.LENGTH_SHORT).show();
                    Log.e("API", "Response error: " + response.code() + ", " + response.message());
                    try {
                        Log.e("API", "Response body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("API", "Error reading response body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Toast.makeText(ListaRezerviranihUslugaActivity4.this, "API call failed", Toast.LENGTH_SHORT).show();
                Log.e("API", "Failure: " + t.getMessage());
            }
        });
    }
}