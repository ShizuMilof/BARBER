package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilKorisnikaActivity extends AppCompatActivity {

    private EditText editPassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_korisnika);

        Intent intent = getIntent();
        if (intent.hasExtra("username")) {
            String loggedInUsername = intent.getStringExtra("username");
            String enteredPasswordValue = intent.getStringExtra("enteredPassword");
            String userEmailValue = intent.getStringExtra("userEmail");

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            EditText prikazUsername = findViewById(R.id.prikazUsername);
            editPassword = findViewById(R.id.editPassword);
            EditText editTextEmail = findViewById(R.id.editTextEmail);

            prikazUsername.setText(loggedInUsername);
            editPassword.setText(enteredPasswordValue);
            editTextEmail.setText(userEmailValue);

            Button togglePasswordVisibilityButton = findViewById(R.id.togglePasswordVisibility);
            togglePasswordVisibilityButton.setOnClickListener(view -> togglePasswordVisibility());
        }
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
