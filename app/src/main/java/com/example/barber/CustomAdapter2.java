package com.example.barber;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barber.model.RezerviraniTerminModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// CustomAdapter2.java
public class CustomAdapter2 extends ArrayAdapter<RezerviraniTerminModel> {
    private final Context context;
    private final ArrayList<RezerviraniTerminModel> rezervacijeList;

    public CustomAdapter2(Context context, ArrayList<RezerviraniTerminModel> rezervacijeList) {
        super(context, R.layout.row_layout_rezervacija, rezervacijeList);
        this.context = context;
        this.rezervacijeList = rezervacijeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout_rezervacija, parent, false);

        TextView selectedDateTextView = rowView.findViewById(R.id.selectedDateTextView);
        TextView formattedTerminTextView = rowView.findViewById(R.id.formattedTerminTextView);
        Button deleteButton = rowView.findViewById(R.id.deleteButton);

        TextView imeUslugeTextView = rowView.findViewById(R.id.imeUslugeTextView);
        TextView usernameTextView = rowView.findViewById(R.id.usernameTextView);

        RezerviraniTerminModel rezervacija = rezervacijeList.get(position);

        // Format the date
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd_MM_yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = inputFormat.parse(rezervacija.getSelectedDate());
            String formattedDate = outputFormat.format(date);
            selectedDateTextView.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        formattedTerminTextView.setText(rezervacija.getSelectedTermin());
        imeUslugeTextView.setText("   " + rezervacija.getImeUsluge());
        usernameTextView.setText("   " + rezervacija.getloggedInUsername());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the clicked item
                RezerviraniTerminModel selectedItem = getItem(position);
                // Display "ime" and "prezime" in a toast
                String imePrezime = selectedItem.getIme();

                // Call a method to delete the item from the database
                deleteItemFromDatabase(selectedItem);

                // Remove the item from the list and notify the adapter
                rezervacijeList.remove(selectedItem);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }

    private void deleteItemFromDatabase(RezerviraniTerminModel item) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("termini");

        // Get the key of the item to be deleted
        String itemKey = generateTerminKey(item);

        if (itemKey != null && !itemKey.isEmpty()) {
            Toast.makeText(context, "Deleting item with key: " + itemKey, Toast.LENGTH_SHORT).show();
            DatabaseReference itemReference = databaseReference.child(itemKey);
            itemReference.removeValue();
        } else {
            // Handle the case when the key is null or empty
        }
    }

    // Function to generate a key based on the item's properties
    private String generateTerminKey(RezerviraniTerminModel item) {
        String key = String.format("korisnik:%s_frizer:%s_termin:%s__%s",
                item.getloggedInUsername(), item.getIme(), item.getSelectedTermin(), item.getSelectedDate());

        // Log the key and other values
        Log.d("KeyGeneration", "Key: " + key +
                ", Username: " + item.getloggedInUsername() +
                ", ImePrezime: " + item.getIme() +
                ", SelectedTermin: " + item.getSelectedTermin() +
                ", SelectedDate: " + item.getSelectedDate());

        return key;
    }

}
