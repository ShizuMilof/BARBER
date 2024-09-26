package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity  {

    Button shortcut1;
    Button shortcut2;
    Button shortcut3;
    Button shortcut4;
    Button shortcut5; // Add reference for the new button

    Button shortcut7; // Add reference for the new button


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        shortcut1 = findViewById(R.id.shortcut1);
        shortcut2 = findViewById(R.id.shortcut2);
      //  shortcut3 = findViewById(R.id.shortcut3);
       // shortcut4 = findViewById(R.id.shortcut4);
        shortcut5 = findViewById(R.id.shortcut5); // Initialize the new button
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        shortcut7 = findViewById(R.id.shortcut7); // Initialize the new button
        shortcut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, RadniciRegisterActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        shortcut2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, UslugaRegisterActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        Button btnPregledStatistike = findViewById(R.id.shortcut6);
        btnPregledStatistike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the action for viewing the frizers' statistics
                Intent intent = new Intent(AdminActivity.this, StatistikaActivity.class);
                startActivity(intent);
            }
        });

    //    shortcut3.setOnClickListener(new View.OnClickListener() {
      //      @Override
     //       public void onClick(View view) {
     //           Intent intent = new Intent(AdminActivity.this, BrisanjeUslugaActivity.class);
        //          startActivityForResult(intent, 1);
        //          overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        //       }
        //    });

        //     shortcut4.setOnClickListener(new View.OnClickListener() {
        //          @Override
        //        public void onClick(View view) {
        //             Intent intent = new Intent(AdminActivity.this, BrisanjeRadnikaActivity.class);
        //             startActivityForResult(intent, 1);
        //             overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        //          }
        //      });

        shortcut5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, VerifikacijaActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        shortcut7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, StatistikaActivity2.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}
