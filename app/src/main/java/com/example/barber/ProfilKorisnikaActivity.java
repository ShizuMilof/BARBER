package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProfilKorisnikaActivity extends AppCompatActivity {

    private EditText editPassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_korisnika);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String enteredPasswordValue = intent.getStringExtra("lozinka");
        String userEmailValue = intent.getStringExtra("userEmail");
        String urlSlike = intent.getStringExtra("urlSlike");
        String ime = intent.getStringExtra("ime");
        String prezime = intent.getStringExtra("prezime");
        boolean verificiran = intent.getBooleanExtra("verificiran", false);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        EditText editTextIme = findViewById(R.id.editTextIme);
        EditText editTextPrezime = findViewById(R.id.editTextPrezime);
        EditText prikazUsername = findViewById(R.id.prikazUsername);
        editPassword = findViewById(R.id.editPassword);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextVerificiran = findViewById(R.id.editTextVerificiran);

        editTextIme.setText(ime);
        editTextPrezime.setText(prezime);
        prikazUsername.setText(username);
        editPassword.setText(enteredPasswordValue);
        editTextEmail.setText(userEmailValue);
        editTextVerificiran.setText(verificiran ? "Verificiran" : "Nije Verificiran");

        if (urlSlike != null && !urlSlike.isEmpty()) {
            ImageView imageViewProfile = findViewById(R.id.imageViewProfile);
            Glide.with(this).load(urlSlike).into(imageViewProfile);
        }

        Button togglePasswordVisibilityButton = findViewById(R.id.togglePasswordVisibility);
        togglePasswordVisibilityButton.setOnClickListener(view -> togglePasswordVisibility());
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        int newInputType;
        if (isPasswordVisible) {
            // Password should be visible
            newInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        } else {
            // Password should be hidden
            newInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        }

        editPassword.setInputType(newInputType);

        // Move the cursor to the end of the text
        editPassword.setSelection(editPassword.getText().length());
    }
}
