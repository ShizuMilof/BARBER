package com.example.barber;

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

public class ListaRezerviranihUslugaActivity3 extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Appointment> listaRezervacija = new ArrayList<>();
    private CustomAdapter4 adapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rezerviranih_usluga3);

        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://172.20.10.3:7194/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        apiService = retrofit.create(ApiService.class);

        listView = findViewById(R.id.listViewRezervacije);
        adapter = new CustomAdapter4(this, listaRezervacija);
        listView.setAdapter(adapter);

        fetchAllAppointments();
    }

    public void fetchAllAppointments() {
        apiService.getAllAppointments().enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Appointment> appointments = response.body();
                    listaRezervacija.clear();
                    listaRezervacija.addAll(appointments);
                    adapter.notifyDataSetChanged();
                    Log.d("API_SUCCESS", "Appointments fetched: " + appointments.size());
                } else {
                    Toast.makeText(ListaRezerviranihUslugaActivity3.this, "Failed to fetch appointments", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Response error: " + response.code() + ", " + response.message());
                    try {
                        Log.e("API_ERROR_BODY", "Response body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("API_ERROR_BODY_READ", "Error reading response body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Toast.makeText(ListaRezerviranihUslugaActivity3.this, "API call failed", Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "API call failed", t);
            }
        });
    }
}
