package com.example.barber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

	private static final String PREFS_NAME = "MyPrefsFile";
	private static final String SELECTED_LANGUAGE = "SelectedLanguage";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



		//slike jezika
		List<Pair<String, Integer>> languagesWithFlags = new ArrayList<>();
		languagesWithFlags.add(new Pair<>("EN", R.drawable.eng));
		languagesWithFlags.add(new Pair<>("CRO", R.drawable.hrv));
		languagesWithFlags.add(new Pair<>("DE", R.drawable.de));

		ListView languageListView = findViewById(R.id.languageListView);

		LanguageAdapter adapter = new LanguageAdapter(this, languagesWithFlags);
		languageListView.setAdapter(adapter);

		languageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String selectedLanguage = languagesWithFlags.get(position).first;

				SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
				editor.putString(SELECTED_LANGUAGE, selectedLanguage);
				editor.apply();

				setLocale(selectedLanguage);

				Intent resultIntent = new Intent();
				resultIntent.putExtra("selectedLanguage", selectedLanguage);
				setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
	}

	private void setLocale(String language) {
		Locale locale = new Locale(language);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getResources().updateConfiguration(config, getResources().getDisplayMetrics());

		Intent refresh = new Intent(this, MainActivity.class);
		startActivity(refresh);
		finish();
	}
}
