package com.example.barber;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.barber.model.RezerviraniTerminModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// CustomAdapter3.java
public class CustomAdapter3 extends ArrayAdapter<RezerviraniTerminModel> {
    private final Context context;
    private final ArrayList<RezerviraniTerminModel> rezervacijeList;

    public CustomAdapter3(Context context, ArrayList<RezerviraniTerminModel> rezervacijeList) {
        super(context, R.layout.row_layout_rezervacija2, rezervacijeList);
        this.context = context;
        this.rezervacijeList = rezervacijeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout_rezervacija2, parent, false);

        TextView selectedDateTextView = rowView.findViewById(R.id.selectedDateTextView);
        TextView formattedTerminTextView = rowView.findViewById(R.id.formattedTerminTextView);
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

        return rowView;
    }
}
