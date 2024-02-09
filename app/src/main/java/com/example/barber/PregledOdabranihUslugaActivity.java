// PregledOdabranihUslugaActivity.java
package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PregledOdabranihUslugaActivity extends AppCompatActivity {

   Button buttonPotvrdi;

    @Override
    public void onBackPressed() {
        // Disable the back button
        // Custom behavior (if any)
        // Comment: This method intentionally left blank to disable the back button

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregled_odabranih_usluga);

        buttonPotvrdi = findViewById(R.id.button2);

        String loggedInUsername = "username"; // Zamijenite s vašim stvarnim korisničkim imenom
        String imePrezime = "ime"; // Zamijenite s vašim stvarnim korisničkim imenom
        String prezime = "prezime"; // Zamijenite s vašim stvarnim korisničkim imenom




        Intent intent = getIntent();
        if (intent != null) {
            imePrezime = intent.getStringExtra("imePrezime");

            loggedInUsername = intent.getStringExtra("username");
            String imeUsluge = intent.getStringExtra("imeUsluge");
            String cijenaUsluge = intent.getStringExtra("cijenaUsluge");
            String trajanjeUsluge = intent.getStringExtra("trajanjeUsluge");
            String profileImageUrl = intent.getStringExtra("profileImageUrl");


            TextView imeTextView = findViewById(R.id.imeTextView);
            imeTextView.setText("Ime i prezime frizera: " + imePrezime );



            TextView imeUslugeTextView = findViewById(R.id.imeUslugeTextView);
            imeUslugeTextView.setText("Ime usluge: " + imeUsluge);

            TextView cijenaUslugeTextView = findViewById(R.id.cijenaUslugeTextView);

            cijenaUslugeTextView.setText("Cijena usluge: " + cijenaUsluge+ "€");

            TextView trajanjeUslugeTextView = findViewById(R.id.trajanjeUslugeTextView);
            trajanjeUslugeTextView.setText("Trajanje usluge: " + trajanjeUsluge+"min");

            ImageView slikaImageView = findViewById(R.id.slikaImageView);
            Glide.with(this).load(profileImageUrl).into(slikaImageView);
        }


        String loggedInUsername2 = loggedInUsername;
        String ime2 = imePrezime;
        String prezime2 = prezime;
        buttonPotvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PregledOdabranihUslugaActivity.this, ListaRezerviranihUslugaActivity.class);

                intent.putExtra("username", loggedInUsername2);
                intent.putExtra("imePrezime", ime2);


                startActivity(intent);
            }







        });









    }
}
