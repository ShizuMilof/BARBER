package com.example.barber;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LanguageAdapter extends ArrayAdapter<Pair<String, Integer>> {

	public LanguageAdapter(@NonNull Context context, @NonNull List<Pair<String, Integer>> languagesWithFlags) {
		super(context, 0, languagesWithFlags);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.language_list_item, parent, false);
		}

		Pair<String, Integer> languageWithFlag = getItem(position);
		if (languageWithFlag != null) {
			String language = languageWithFlag.first;
			int flagResource = languageWithFlag.second;

			TextView textView = convertView.findViewById(R.id.textViewLanguage);
			ImageView imageView = convertView.findViewById(R.id.imageViewFlag);

			textView.setText(language);
			imageView.setImageResource(flagResource);
		}

		return convertView;
	}
}
