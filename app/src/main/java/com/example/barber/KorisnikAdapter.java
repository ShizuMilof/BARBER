package com.example.barber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber.model.Radnik;

import java.util.List;

public class KorisnikAdapter extends RecyclerView.Adapter<KorisnikAdapter.KorisnikViewHolder> {

    private List<Radnik> korisnici;

    public KorisnikAdapter(List<Radnik> korisnici) {
        this.korisnici = korisnici;
    }

    @NonNull
    @Override
    public KorisnikViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_korisnik, parent, false);
        return new KorisnikViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KorisnikViewHolder holder, int position) {
        Radnik korisnik = korisnici.get(position);
        holder.imePrezime.setText(korisnik.getIme() + " " + korisnik.getPrezime());
        holder.rezerviraniTermini.setText("Rezervirani termini: " + korisnik.getBrojacRezerviranihTermina());
        holder.otkazaniTermini.setText("Otkazani termini: " + korisnik.getBrojacOtkazanihTermina());
        holder.odradjeniTermini.setText("Odrađeni termini: " + korisnik.getBrojacOdradenihTermina());
        holder.ukupnoVrijemeRada.setText("Ukupno vrijeme rada: " + korisnik.getUkupnoVrijemeRada() + " min");
    }

    @Override
    public int getItemCount() {
        return korisnici.size();
    }

    public static class KorisnikViewHolder extends RecyclerView.ViewHolder {
        TextView imePrezime;
        TextView rezerviraniTermini;
        TextView otkazaniTermini;
        TextView odradjeniTermini;
        TextView ukupnoVrijemeRada;

        public KorisnikViewHolder(@NonNull View itemView) {
            super(itemView);
            imePrezime = itemView.findViewById(R.id.tvImePrezime);
            rezerviraniTermini = itemView.findViewById(R.id.tvRezerviraniTermini);
            otkazaniTermini = itemView.findViewById(R.id.tvOtkazaniTermini);
            odradjeniTermini = itemView.findViewById(R.id.tvOdradjeniTermini);
            ukupnoVrijemeRada = itemView.findViewById(R.id.tvUkupnoVrijemeRada);
        }
    }
}
