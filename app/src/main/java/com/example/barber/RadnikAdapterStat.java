package com.example.barber;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber.model.Radnik;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RadnikAdapterStat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_AGGREGATE = 0;
    private static final int VIEW_TYPE_INDIVIDUAL = 1;

    private List<Radnik> radnici;
    private List<Radnik> filteredRadnici;
    private int ukupnoRezervisanihTermina = 0;
    private int ukupnoOtkazanihTermina = 0;
    private int ukupnoOdradenihTermina = 0;

    public RadnikAdapterStat(List<Radnik> radnici) {
        this.radnici = radnici;
        this.filteredRadnici = new ArrayList<>(radnici); // Create a copy for filtering

        calculateTotals();
    }

    private void calculateTotals() {
        // Reset totals
        ukupnoRezervisanihTermina = 0;
        ukupnoOtkazanihTermina = 0;
        ukupnoOdradenihTermina = 0;

        for (Radnik radnik : filteredRadnici) {
            ukupnoRezervisanihTermina += radnik.getBrojacRezerviranihTermina();
            ukupnoOtkazanihTermina += radnik.getBrojacOtkazanihTermina();
            ukupnoOdradenihTermina += radnik.getBrojacOdradenihTermina();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_AGGREGATE;
        } else {
            return VIEW_TYPE_INDIVIDUAL;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_AGGREGATE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.radnik_stat_item, parent, false);
            return new AggregateViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.radnik_stat_item, parent, false);
            return new RadnikViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_AGGREGATE) {
            AggregateViewHolder aggregateHolder = (AggregateViewHolder) holder;
            aggregateHolder.textViewImePrezime.setText("Ukupno za sve korisnike");

            String statistika = "Ukupno rezerviranih termina: " + ukupnoRezervisanihTermina + "\n"
                    + "Ukupno otkazanih termina: " + ukupnoOtkazanihTermina + "\n"
                    + "Ukupno odrađenih termina: " + ukupnoOdradenihTermina;

            aggregateHolder.textViewStatistika.setText(statistika);

            // Set up the pie chart
            setupPieChart(aggregateHolder.pieChart, ukupnoRezervisanihTermina, ukupnoOtkazanihTermina, ukupnoOdradenihTermina);

            // Toggle visibility on click
            aggregateHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (aggregateHolder.layoutStatistika.getVisibility() == View.VISIBLE) {
                        aggregateHolder.layoutStatistika.setVisibility(View.GONE);
                    } else {
                        aggregateHolder.layoutStatistika.setVisibility(View.VISIBLE);
                    }
                }
            });

        } else {
            int actualPosition = position - 1; // Adjust for the aggregate view
            Radnik radnik = filteredRadnici.get(actualPosition);
            RadnikViewHolder radnikHolder = (RadnikViewHolder) holder;

            radnikHolder.textViewImePrezime.setText(radnik.getIme() + " " + radnik.getPrezime());

            String statistika = "Broj rezerviranih termina: " + radnik.getBrojacRezerviranihTermina() + "\n"
                    + "Broj otkazanih termina: " + radnik.getBrojacOtkazanihTermina() + "\n"
                    + "Broj odrađenih termina: " + radnik.getBrojacOdradenihTermina() + "\n"
                    + "Ukupno vrijeme rezerviranih i odrađenih usluga: " + radnik.getUkupnoVrijemeRada() + " minuta";

            radnikHolder.textViewStatistika.setText(statistika);

            // Set up the pie chart
            setupPieChart(radnikHolder.pieChart, radnik.getBrojacRezerviranihTermina(), radnik.getBrojacOtkazanihTermina(), radnik.getBrojacOdradenihTermina());

            // Toggle visibility on click
            radnikHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (radnikHolder.layoutStatistika.getVisibility() == View.VISIBLE) {
                        radnikHolder.layoutStatistika.setVisibility(View.GONE);
                    } else {
                        radnikHolder.layoutStatistika.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return filteredRadnici.size() + 1;  // Add 1 for aggregate data
    }

    // Method to filter data based on date range
    public void filterByDateRange(Date startDate, Date endDate) {
        filteredRadnici.clear();

        for (Radnik radnik : radnici) {
            // Filter by date range for each worker's appointments
            int rezervirani = radnik.getRezervisaniTermini() != null ?
                    (int) radnik.getRezervisaniTermini().stream()
                            .filter(date -> !date.before(startDate) && !date.after(endDate))
                            .count() : 0;

            int otkazani = radnik.getOtkazaniTermini() != null ?
                    (int) radnik.getOtkazaniTermini().stream()
                            .filter(date -> !date.before(startDate) && !date.after(endDate))
                            .count() : 0;

            int odradeni = radnik.getOdradeniTermini() != null ?
                    (int) radnik.getOdradeniTermini().stream()
                            .filter(date -> !date.before(startDate) && !date.after(endDate))
                            .count() : 0;

            // Update worker's counters
            radnik.setBrojacRezerviranihTermina(rezervirani);
            radnik.setBrojacOtkazanihTermina(otkazani);
            radnik.setBrojacOdradenihTermina(odradeni);

            filteredRadnici.add(radnik);
        }

        calculateTotals(); // Recalculate totals based on filtered data
        notifyDataSetChanged(); // Refresh the RecyclerView
    }

    // Helper method to set up the PieChart
    private void setupPieChart(PieChart pieChart, int rezervirani, int otkazani, int odradeni) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(rezervirani, "Rezervirani"));
        entries.add(new PieEntry(otkazani, "Otkazani"));
        entries.add(new PieEntry(odradeni, "Odrađeni"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate(); // refresh the chart
    }

    public static class RadnikViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewImePrezime;
        public TextView textViewStatistika;
        public LinearLayout layoutStatistika;
        public PieChart pieChart;

        public RadnikViewHolder(View itemView) {
            super(itemView);
            textViewImePrezime = itemView.findViewById(R.id.textViewImePrezime);
            textViewStatistika = itemView.findViewById(R.id.textViewStatistika);
            layoutStatistika = itemView.findViewById(R.id.layoutStatistika);
            pieChart = itemView.findViewById(R.id.pieChart);
        }
    }

    public static class AggregateViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewImePrezime;
        public TextView textViewStatistika;
        public LinearLayout layoutStatistika;
        public PieChart pieChart;

        public AggregateViewHolder(View itemView) {
            super(itemView);
            textViewImePrezime = itemView.findViewById(R.id.textViewImePrezime);
            textViewStatistika = itemView.findViewById(R.id.textViewStatistika);
            layoutStatistika = itemView.findViewById(R.id.layoutStatistika);
            pieChart = itemView.findViewById(R.id.pieChart);
        }
    }
}
