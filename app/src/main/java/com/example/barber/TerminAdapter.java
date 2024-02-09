package com.example.barber;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber.model.TerminModel;

import java.util.ArrayList;
import java.util.List;

public class TerminAdapter extends RecyclerView.Adapter<TerminAdapter.TerminViewHolder> {


    private List<String> zauzetiTermini; // Declare the zauzetiTermini list
    private final List<TerminModel> terminiList;
    private OnItemClickListener onItemClickListener;

    public TerminModel[] getTerminiList() {
        return terminiList.toArray(new TerminModel[0]);
    }

    public interface OnItemClickListener {
        void onItemClick(String selectedTermin);

        int postaviBojuTermina(String vrijeme);
    }



    public TerminAdapter(List<TerminModel> terminiList) {
        this.terminiList = terminiList;
        this.zauzetiTermini = new ArrayList<>(); // Initialize zauzetiTermini
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void setZauzetiTermini(List<String> zauzetiTermini) {
        this.zauzetiTermini = zauzetiTermini;

        // Create a new list for the available time slots
        List<TerminModel> availableTermini = new ArrayList<>();

        // Iterate over the terminiList
        for (TerminModel termin : terminiList) {
            // If the current time slot is not in zauzetiTermini, add it to the availableTermini
            if (!zauzetiTermini.contains(termin.getVrijeme())) {
                availableTermini.add(termin);
            }
        }

        // Clear the terminiList and add all elements from availableTermini to it
        terminiList.clear();
        terminiList.addAll(availableTermini);

        // Notify the adapter that the data set has changed
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TerminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_termin, parent, false);
        return new TerminViewHolder(view);
    }

  @Override
public void onBindViewHolder(@NonNull TerminViewHolder holder, int position) {
    TerminModel termin = terminiList.get(position);
    holder.textViewVrijeme.setText(termin.getVrijeme());

    // Check if the current time slot is in the zauzetiTermini list
    if (zauzetiTermini.contains(termin.getVrijeme())) {
        // If it is, disable the item view
        holder.itemView.setEnabled(false);
        holder.itemView.setAlpha(0.5f); // Optional: make the item view semi-transparent
    } else {
        // If it's not, enable the item view
        holder.itemView.setEnabled(true);
        holder.itemView.setAlpha(1.0f); // Optional: make the item view fully opaque
    }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(termin.getVrijeme());
            }
        }
    });
}



    @Override
    public int getItemCount() {
        return terminiList.size();
    }

    public void promijeniBojuTermina(String rezerviranoVrijeme, int green) {
        for (int i = 0; i < terminiList.size(); i++) {
            if (terminiList.get(i).getVrijeme().equals(rezerviranoVrijeme)) {
                notifyItemChanged(i);
                break;
            }
        }
    }
    public String getItemAtPosition(int position) {
        return terminiList.get(position).getVrijeme();
    }
    public void clearReservations() {
        for (TerminModel termin : terminiList) {
            termin.setReserved(false);
        }
        notifyDataSetChanged();
    }

    public void updateAvailability(int position, boolean isAvailable) {
        terminiList.get(position).setReserved(!isAvailable);
        notifyItemChanged(position);
    }
    public void clearColors() {
        for (TerminModel termin : terminiList) {
            termin.setReserved(false);
        }
        notifyDataSetChanged(); // Notify the adapter that the dataset has changed
    }
    static class TerminViewHolder extends RecyclerView.ViewHolder {
        TextView textViewVrijeme; // Declare the textViewTermin

        public TerminViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVrijeme = itemView.findViewById(R.id.textViewVrijeme); // Initialize the textViewTermin
        }
    }
}