package com.example.barber;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Registracija {
    @SerializedName("datum")
    private String datum;

    @SerializedName("vrijemeUsluge")
    private String vrijemeUsluge;

    @SerializedName("korisnik")
    private String korisnik;

    @SerializedName("frizer")
    private String frizer;

    @SerializedName("usluge")
    private List<String> usluge;

    @SerializedName("trajanjeUsluge")
    private String trajanjeUsluge;

    // Konstruktor, getteri i setteri
    public Registracija() {
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getVrijemeUsluge() {
        return vrijemeUsluge;
    }

    public void setVrijemeUsluge(String vrijemeUsluge) {
        this.vrijemeUsluge = vrijemeUsluge;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getFrizer() {
        return frizer;
    }

    public void setFrizer(String frizer) {
        this.frizer = frizer;
    }

    public List<String> getUsluge() {
        return usluge;
    }

    public void setUsluge(List<String> usluge) {
        this.usluge = usluge;
    }

    public String getTrajanjeUsluge() {
        return trajanjeUsluge;
    }

    public void setTrajanjeUsluge(String trajanjeUsluge) {
        this.trajanjeUsluge = trajanjeUsluge;
    }
}
