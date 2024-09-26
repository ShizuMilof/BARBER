package com.example.barber.model;

import com.google.gson.annotations.SerializedName;

public class Appointment {

    @SerializedName("datum")
    private String datum;

    @SerializedName("vrijemeUsluge")
    private String vrijemeUsluge;

    @SerializedName("korisnik")
    private String korisnik;

    @SerializedName("frizer")
    private String frizer;

    @SerializedName("usluge")
    private String usluge;

    @SerializedName("trajanjeUsluge")
    private String trajanjeUsluge;

    @SerializedName("urlSlike")
    private String urlSlike;

    @SerializedName("JeOdraden")

    private boolean JeOdraden;
    public boolean isJeOdraden() {
        return JeOdraden;
    }


    @SerializedName("JeOtkazan")

    private boolean JeOtkazan;
    public boolean isJeOtkazan() {
        return JeOtkazan;
    }
    private int id; // Add this field if not already present

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    // Getters and Setters

    public void setJeOdraden(boolean JeOdraden) {
        this.JeOdraden = JeOdraden;
    }
    public void setJeOtkazan(boolean JeOtkazan) {
        this.JeOtkazan = JeOtkazan;
    }

    public boolean JeOdraden() {
        return JeOdraden;
    }

    public boolean JeOtkazan() {
        return JeOtkazan;
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

    public String getUsluge() {
        return usluge;
    }

    public void setUsluge(String usluge) {
        this.usluge = usluge;
    }

    public String getTrajanjeUsluge() {
        return trajanjeUsluge;
    }

    public void setTrajanjeUsluge(String trajanjeUsluge) {
        this.trajanjeUsluge = trajanjeUsluge;
    }

    public String getUrlSlike() {
        return urlSlike;
    }

    public void setUrlSlike(String urlSlike) {
        this.urlSlike = urlSlike;
    }


}
