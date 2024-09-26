package com.example.barber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber.model.RezerviraniTerminModel;

import java.util.List;

// RezerviraniTerminAdapter.java
public class RezerviraniTerminAdapter extends RecyclerView.Adapter<RezerviraniTerminAdapter.ViewHolder> {

    private final List<RezerviraniTerminModel> rezerviraniTerminiList;

    public RezerviraniTerminAdapter(List<RezerviraniTerminModel> rezerviraniTerminiList) {
        this.rezerviraniTerminiList = rezerviraniTerminiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rezervirani_termin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RezerviraniTerminModel rezerviraniTermin = rezerviraniTerminiList.get(position);

        holder.textViewDatum.setText(rezerviraniTermin.getSelectedDate());
        holder.textViewTermin.setText(rezerviraniTermin.getSelectedTermin());
    }

    @Override
    public int getItemCount() {
        return rezerviraniTerminiList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDatum;
        TextView textViewTermin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDatum = itemView.findViewById(R.id.textViewDatum);
            textViewTermin = itemView.findViewById(R.id.textViewTermin);
        }
    }
}
