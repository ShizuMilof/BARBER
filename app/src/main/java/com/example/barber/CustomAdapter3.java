package com.example.barber;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.barber.model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CustomAdapter3 extends ArrayAdapter<Appointment> {
    private final Context context;
    private final ArrayList<Appointment> rezervacijeList;
    private final ApiService apiService;

    public CustomAdapter3(Context context, ArrayList<Appointment> rezervacijeList, ApiService apiService) {
        super(context, R.layout.row_layout_rezervacija, rezervacijeList);
        this.context = context;
        this.rezervacijeList = rezervacijeList;
        this.apiService = apiService;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout_rezervacija, parent, false);

        TextView selectedDateTextView = rowView.findViewById(R.id.selectedDateTextView);
        TextView formattedTerminTextView = rowView.findViewById(R.id.formattedTerminTextView);
        TextView imeUslugeTextView = rowView.findViewById(R.id.imeUslugeTextView);
        TextView usernameTextView = rowView.findViewById(R.id.usernameTextView);
        Button approveButton = rowView.findViewById(R.id.approveButton);
        Button approveButton2 = rowView.findViewById(R.id.approveButton2);

        Appointment rezervacija = rezervacijeList.get(position);

        // Parse and format the date
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss";
        String outputPattern = "dd.MM.yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());

        try {
            Date date = inputFormat.parse(rezervacija.getDatum());
            String formattedDate = outputFormat.format(date);
            selectedDateTextView.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            selectedDateTextView.setText(rezervacija.getDatum()); // Fallback to original date string in case of error
        }

        formattedTerminTextView.setText(rezervacija.getTrajanjeUsluge());
        imeUslugeTextView.setText(rezervacija.getUsluge());
        usernameTextView.setText("         " + rezervacija.getKorisnik());

        // Set click listener for the list item
        rowView.setOnClickListener(v -> {
            // Create a dialog to show the QR code
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_enlarged_image);
            ImageView enlargedImageView = dialog.findViewById(R.id.enlargedImageView);

            // Load the image from the URL
            new Thread(() -> {
                try {
                    URL url = new URL(rezervacija.getUrlSlike());
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                    new Handler(Looper.getMainLooper()).post(() -> enlargedImageView.setImageBitmap(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show());
                }
            }).start();

            dialog.show();
        });

        // Set click listener for the approve button
        approveButton.setOnClickListener(v -> approveAppointment(rezervacija, position));

        // Set click listener for the cancel button
        approveButton2.setOnClickListener(v -> cancelAppointment(rezervacija, position));

        return rowView;
    }

    private void approveAppointment(Appointment rezervacija, int position) {
        String url = "https://172.20.10.3:7194/api/Termini/ApproveAppointment"; // Replace with your actual API endpoint
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = String.valueOf(rezervacija.getId()); // Sending the appointment ID as a plain number
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(); // Use unsafe OkHttpClient

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("CustomAdapter3", "Approval failed: " + e.getMessage());

                // Show toast on the main UI thread
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Approval failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Update the Appointment object and the UI
                    rezervacija.setJeOdraden(true);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        // Refresh the activity
                        if (context instanceof ListaRezerviranihUslugaActivity4) {
                            ((ListaRezerviranihUslugaActivity4) context).fetchAppointments(rezervacija.getFrizer());
                        }
                        Toast.makeText(context, "TERMIN USPJEŠNO ODRAĐEN!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Log.e("CustomAdapter3", "Approval failed: " + response.message());
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Approval failed: " + response.message(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void cancelAppointment(Appointment rezervacija, int position) {
        String url = "https://172.20.10.3:7194/api/Termini/CancelAppointment"; // Replace with your actual API endpoint
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = String.valueOf(rezervacija.getId()); // Sending the appointment ID as a plain number
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(); // Use unsafe OkHttpClient

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("CustomAdapter3", "Cancellation failed: " + e.getMessage());

                // Show toast on the main UI thread
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Cancellation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Update the Appointment object and the UI
                    rezervacija.setJeOdraden(false);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        // Refresh the activity
                        if (context instanceof ListaRezerviranihUslugaActivity4) {
                            ((ListaRezerviranihUslugaActivity4) context).fetchAppointments(rezervacija.getFrizer());
                        }
                        Toast.makeText(context, "TERMIN USPJEŠNO OTKAZAN!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Log.e("CustomAdapter3", "Cancellation failed: " + response.message());
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Cancellation failed: " + response.message(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
