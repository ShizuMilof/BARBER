package com.example.barber.model;

import android.graphics.Color;

public class TerminModel {
    private String vrijeme;
    private String selectedDate;
    private boolean isReserved;
    private int backgroundColor; // Add this line
    private int boja;

    public TerminModel(String vrijeme, String selectedDate) {
        this.vrijeme = vrijeme;
        this.selectedDate = selectedDate;
        this.isReserved = false; // Default to not reserved
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public int getBoja() {
        return boja;
    }

    public void setBoja(int boja) {
        this.boja = boja;
    }


    public void updateReservationStatus() {
        if (isReserved) {
            setBackgroundColor(Color.RED);
        } else {
            setBackgroundColor(Color.GREEN);
        }
    }

}
