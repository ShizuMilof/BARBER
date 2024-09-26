// Appointment.java
package com.example.barber.model;

public class AppointmentResponse {
    private int id;
    private String datum;
    private VrijemeUsluge vrijemeUsluge;
    private String korisnik;
    private String frizer;
    private String usluge;
    private String trajanjeUsluge;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public VrijemeUsluge getVrijemeUsluge() {
        return vrijemeUsluge;
    }

    public void setVrijemeUsluge(VrijemeUsluge vrijemeUsluge) {
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


    public static class VrijemeUsluge {
        private long ticks;

        public long getTicks() {
            return ticks;
        }

        public void setTicks(long ticks) {
            this.ticks = ticks;
        }
    }
}
