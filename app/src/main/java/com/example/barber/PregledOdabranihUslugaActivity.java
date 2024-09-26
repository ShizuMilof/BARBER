package com.example.barber;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barber.model.Usluge;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PregledOdabranihUslugaActivity extends AppCompatActivity {

    private List<Usluge> selectedServices;
    private String selectedDateRange;
    private String selectedTimeSlot;
    private String username;
    private String frizer_ime;
    private TextView appointmentDetailsTextView;
    private TextView usernameTextView;
    private TextView frizerTextView;
    private TextView dateRangeTextView;
    private TextView timeSlotTextView;
    private TextView servicesTextView;
    private TextView servicesTextView22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregled_odabranih_usluga);

        // Initialize the TextView objects
        appointmentDetailsTextView = findViewById(R.id.appointmentDetailsTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        frizerTextView = findViewById(R.id.frizerTextView);
        timeSlotTextView = findViewById(R.id.timeSlotTextView);
        servicesTextView = findViewById(R.id.servicesTextView);

        // Retrieve data from Intent
        username = getIntent().getStringExtra("username");
        frizer_ime = getIntent().getStringExtra("frizer_ime");
        selectedServices = getIntent().getParcelableArrayListExtra("selectedServices");
        selectedDateRange = getIntent().getStringExtra("selectedDateRange");
        selectedTimeSlot = getIntent().getStringExtra("selectedTimeSlot");
        String bookedDate = getIntent().getStringExtra("bookedDate");
        String bookedTime = getIntent().getStringExtra("bookedTime");
        String bookedHairdresser = getIntent().getStringExtra("bookedHairdresser");
        String qrCodeUrl = getIntent().getStringExtra("qrCodeUrl");

        if (appointmentDetailsTextView != null) {
            String appointmentDetails = "Vrijeme termina: " + bookedTime ;
            appointmentDetailsTextView.setText(appointmentDetails);
        }

        ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView);
        new Thread(() -> {
            try {
                URL url = new URL(qrCodeUrl);
                Bitmap qrCodeBitmap = BitmapFactory.decodeStream(url.openStream());
                runOnUiThread(() -> qrCodeImageView.setImageBitmap(qrCodeBitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        if (username != null && usernameTextView != null) {
            usernameTextView.setText("Korisničko ime: " + username);
        }

        if (frizer_ime != null && frizerTextView != null) {
            frizerTextView.setText("Frizer: " + frizer_ime);
        }



        if (selectedTimeSlot != null && timeSlotTextView != null) {
            timeSlotTextView.setText("Odabrani dio dana: " + selectedTimeSlot.toLowerCase());
        }

        if (selectedServices != null && !selectedServices.isEmpty() && servicesTextView != null) {
            StringBuilder servicesDetails = new StringBuilder();
            for (int i = 0; i < selectedServices.size(); i++) {
                if (i > 0) {
                    servicesDetails.append(", ");
                }
                servicesDetails.append(selectedServices.get(i).getNaziv());
            }
            servicesTextView.setText("Odabrane usluge: " + servicesDetails.toString().toLowerCase());
        } else if (servicesTextView != null) {
            servicesTextView.setText("No services selected.");
        }


        if (selectedTimeSlot != null && servicesTextView22 != null) {
            servicesTextView22.setText("Odabrani dio dana: " + selectedTimeSlot);
        }

        // Log the received data
        Log.d("PregledOdabranihUsluga", "Username: " + username);
        Log.d("PregledOdabranihUsluga", "Frizer: " + frizer_ime);
        Log.d("PregledOdabranihUsluga", "Selected Date Range: " + selectedDateRange);
        Log.d("PregledOdabranihUsluga", "Selected Time Slot: " + selectedTimeSlot);
        Log.d("PregledOdabranihUsluga", "vrijeme termin: " + bookedTime);

        if (selectedServices != null) {
            for (Usluge service : selectedServices) {
                Log.d("PregledOdabranihUsluga", "Selected Service: " + service.getNaziv() +
                        ", Price: " + service.getCijena() + ", Duration: " + service.getTrajanje());
            }
        }

        // Add the button and set its click listener
        Button buttonGoToMain = findViewById(R.id.buttonGoToMain);
        buttonGoToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PregledOdabranihUslugaActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
