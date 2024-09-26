package com.example.barber;

import java.time.LocalDateTime;
import java.time.Duration;

public class Termini {
    private int id;
    private LocalDateTime datum;
    private Duration vrijemeUsluge;
    private String korisnik;
    private String frizer;
    private String usluge; // Comma-separated string of services
    private String trajanjeUsluge;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public Duration getVrijemeUsluge() {
        return vrijemeUsluge;
    }

    public void setVrijemeUsluge(Duration vrijemeUsluge) {
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
}
