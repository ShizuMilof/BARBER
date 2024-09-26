package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayScannedDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scanned_data);

        TextView tvScannedData = findViewById(R.id.tv_scanned_data);
        Button btnBack = findViewById(R.id.btn_back);
        TextView tvDetailsHeader = findViewById(R.id.tv_details_header);

        Intent intent = getIntent();
        String scannedData = intent.getStringExtra("scannedData");

        tvScannedData.setText(scannedData);

        btnBack.setOnClickListener(v -> finish());
    }
}
