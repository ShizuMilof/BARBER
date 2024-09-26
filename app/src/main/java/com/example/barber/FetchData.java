package com.example.barber;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchData extends AsyncTask<String, Void, String> {
    private Context context; // Add this member variable

    private OkHttpClient client;
    private OnDataFetchedListener onDataFetchedListener;

    // Konstruktor klase FetchData
    public FetchData() {
        this.client = new OkHttpClient(); // Inicijalizacija OkHttpClient objekta
    }

    // Metoda koja se izvršava u pozadini kada se pokrene asinkroni zadatak
    @Override
    protected String doInBackground(String... urls) {
        String url = urls[0];
        String jsonData = null;

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            jsonData = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonData;
    }

    // Metoda koja se poziva nakon što je asinkroni zadatak dovršen
    // Unutar metode onPostExecute klase FetchData
    @Override
    protected void onPostExecute(String jsonData) {
        if (jsonData != null) {
            // Dodaj ispisivanje odgovora u konzolu
            Log.d("API_RESPONSE", jsonData);

            if (onDataFetchedListener != null) {
                onDataFetchedListener.onDataFetched(jsonData);
            }
        } else {
            Log.d("API_RESPONSE", "jsonData is null"); // Log a message indicating jsonData is null
        }
    }






    // Postavljanje slušača za dohvaćanje podataka
    public void setOnDataFetchedListener(OnDataFetchedListener listener) {
        this.onDataFetchedListener = listener;
    }

    // Sučelje za slušanje događaja dohvaćanja podataka
    public interface OnDataFetchedListener {
        void onDataFetched(String jsonData);
    }
}
