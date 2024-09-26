package com.example.barber;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class CustomAdapter4 extends ArrayAdapter<Appointment> {
    private final Context context;
    private final ArrayList<Appointment> rezervacijeList;

    public CustomAdapter4(Context context, ArrayList<Appointment> rezervacijeList) {
        super(context, R.layout.row_layout_rezervacija2, rezervacijeList);
        this.context = context;
        this.rezervacijeList = rezervacijeList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_layout_rezervacija2, parent, false);
            holder = new ViewHolder();
            holder.selectedDateTextView = convertView.findViewById(R.id.selectedDateTextView);
            holder.formattedTerminTextView = convertView.findViewById(R.id.formattedTerminTextView);
            holder.imeUslugeTextView = convertView.findViewById(R.id.imeUslugeTextView);
            holder.usernameTextView = convertView.findViewById(R.id.usernameTextView);
            holder.frizerTextView = convertView.findViewById(R.id.frizerTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Appointment rezervacija = rezervacijeList.get(position);

        // Parse and format the date
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss";
        String outputPattern = "dd.MM.yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());

        try {
            Date date = inputFormat.parse(rezervacija.getDatum());
            String formattedDate = outputFormat.format(date);
            holder.selectedDateTextView.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.selectedDateTextView.setText(rezervacija.getDatum()); // Fallback to original date string in case of error
        }

        holder.formattedTerminTextView.setText(rezervacija.getTrajanjeUsluge());
        holder.imeUslugeTextView.setText(rezervacija.getUsluge());
        holder.usernameTextView.setText(rezervacija.getKorisnik());
        holder.frizerTextView.setText(rezervacija.getFrizer());

        // Set click listener for the list item
        convertView.setOnClickListener(v -> {
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

        return convertView;
    }

    static class ViewHolder {
        TextView selectedDateTextView;
        TextView formattedTerminTextView;
        TextView imeUslugeTextView;
        TextView usernameTextView;
        TextView frizerTextView;
    }
}
