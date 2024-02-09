package com.example.barber;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class PostavkeAplikacijeActivity extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postavke_aplikacije);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ImageView slikaImageView = findViewById(R.id.slikaImageView);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.martin);
        slikaImageView.setImageDrawable(drawable);


        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvPoint1 = findViewById(R.id.tvPoint1);
        TextView tvPoint2 = findViewById(R.id.tvPoint2);
        TextView tvPoint3 = findViewById(R.id.tvPoint3);
        TextView tvPoint4 = findViewById(R.id.tvPoint4);
        TextView tvPoint5 = findViewById(R.id.tvPoint5);

        tvTitle.setText("PREDNOSTI DIGITALIZACIJE VAŠEG POSLOVANJA");
        tvPoint1.setText("• BEZ PORUKA");
        tvPoint2.setText("• BEZ POZIVA");
        tvPoint3.setText("• NEOMETAN RAD");
        tvPoint4.setText("• DOSTUPNOST 24/7");
        tvPoint5.setText("• REZERVACIJE 24/7");



    }
}
