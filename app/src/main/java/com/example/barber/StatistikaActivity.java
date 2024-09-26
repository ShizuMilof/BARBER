package com.example.barber;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber.model.Radnik;

import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.Date;
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

public class StatistikaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRadnici;
    private RadnikAdapterStat radnikAdapterStat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistika);

        recyclerViewRadnici = findViewById(R.id.recyclerViewRadnici);
        recyclerViewRadnici.setLayoutManager(new LinearLayoutManager(this));




        // Your existing Retrofit setup and API call

        OkHttpClient client = getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://172.20.10.3:7194/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Radnik>> call = apiService.getRadniciWithUlogeId3();

        call.enqueue(new Callback<List<Radnik>>() {
            @Override
            public void onResponse(Call<List<Radnik>> call, Response<List<Radnik>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(StatistikaActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                List<Radnik> radnici = response.body();
                radnikAdapterStat = new RadnikAdapterStat(radnici);
                recyclerViewRadnici.setAdapter(radnikAdapterStat);
            }

            @Override
            public void onFailure(Call<List<Radnik>> call, Throwable t) {
                Log.e("RetrofitError", "Request failed", t);
                Toast.makeText(StatistikaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterThisMonth() {
        Calendar calendar = Calendar.getInstance();

        // Set the calendar to the first day of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        // Set the calendar to the last day of the current month
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = calendar.getTime();

        // Apply the filter to the adapter
        if (radnikAdapterStat != null) {
            radnikAdapterStat.filterByDateRange(startDate, endDate);
        } else {
            Toast.makeText(this, "Data not yet loaded", Toast.LENGTH_LONG).show();
        }
    }

    private OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

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
