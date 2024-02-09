package com.example.barber;

public class TerminFirebaseModel {
    private String selectedDate;
    private String selectedTermin;
    private String imeUsluge;
    private String loggedInUsername;
    private String imePrezime;
    private boolean rezerviran;

    public TerminFirebaseModel() {
    }

    public TerminFirebaseModel(String selectedDate, String selectedTermin, String imeUsluge, String loggedInUsername, String imePrezime) {
        this.selectedDate = selectedDate;
        this.selectedTermin = selectedTermin;
        this.loggedInUsername = loggedInUsername;
        this.imeUsluge = imeUsluge;
        this.imePrezime = imePrezime;
        this.rezerviran = false;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public String getImeUsluge() {
        return imeUsluge;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedTermin() {
        return selectedTermin;
    }

    public void setSelectedTermin(String selectedTermin) {
        this.selectedTermin = selectedTermin;
    }

    public String getIme() {
        return imePrezime;
    }

    public void setIme(String ime) {
        this.imePrezime = ime;
    }

    public boolean isRezerviran() {
        return rezerviran;
    }

    public void setRezerviran(boolean rezerviran) {
        this.rezerviran = rezerviran;
    }
}
