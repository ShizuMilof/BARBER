package com.example.barber;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber.model.Usluge;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class CustomAdapter1 extends RecyclerView.Adapter<CustomAdapter1.UslugeViewHolder> {
    private List<Usluge> usluge;
    private OnItemClickListener listener;

    private List<Usluge> selectedServices; // Dodajte polje za praćenje odabranih usluga

    public CustomAdapter1(List<Usluge> usluge) {
        this.usluge = usluge;

        this.selectedServices = new ArrayList<>(); // Inicijalizirajte listu odabranih usluga

    }

    public interface OnItemClickListener {
        void onItemClick(Usluge usluga);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Usluge> newUsluge) {
        this.usluge = newUsluge;
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    @NonNull
    @Override
    public UslugeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_item1, parent, false);
        return new UslugeViewHolder(view);
    }

    public List<Usluge> getSelectedServices() {
        return selectedServices;
    }


    @Override
    public void onBindViewHolder(@NonNull UslugeViewHolder holder, int position) {
        Usluge usluga = usluge.get(position);

        // Postavljanje teksta na TextView-ove
        holder.name.setText(usluga.getNaziv());
        holder.durationTextView.setText(usluga.getTrajanje() + "min"); // Assuming there's a getTrajanje() method
        holder.name1.setText(usluga.getCijena() + "€");

        // Postavljanje stanja checkbox-a i definiranje slušača
        holder.checkBox.setOnCheckedChangeListener(null); // Onemogućite okidač dok ažurirate
        holder.checkBox.setChecked(selectedServices.contains(usluga)); // Provjerite je li usluga odabrana
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!selectedServices.contains(usluga)) {
                        selectedServices.add(usluga);
                    }
                } else {

                    selectedServices.remove(usluga);
                }
                // Ažurirajte prikaz na temelju novog stanja odabranih usluga
                notifyDataSetChanged();
            }
        });



    String imageUrl = usluga.getSlika();
        Log.d("CustomAdapter1", "Loading image from URL: " + imageUrl);
        Picasso.get()
                .load(usluga.getSlika())
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(holder.profileImageView);
    }


    @Override
    public int getItemCount() {
        return usluge.size();
    }

    public static class UslugeViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView name1;

        TextView durationTextView;
        CircleImageView profileImageView;
        CheckBox checkBox;

        public UslugeViewHolder(View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            name = itemView.findViewById(android.R.id.text1);
            name1 = itemView.findViewById(android.R.id.text2);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
