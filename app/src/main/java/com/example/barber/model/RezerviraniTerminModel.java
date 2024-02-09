package com.example.barber.model;

public class RezerviraniTerminModel {
    private String selectedDate;
    private String selectedTermin;

    private String imeUsluge;
    private String loggedInUsername;
    private String ime;


    private String key;

    public RezerviraniTerminModel() {
    }

    public RezerviraniTerminModel(String selectedDate, String selectedTermin,String imeUsluge,String loggedInUsername,String ime) {
        this.selectedDate = selectedDate;
        this.selectedTermin = selectedTermin;
        this.imeUsluge = imeUsluge;
        this.loggedInUsername = loggedInUsername;
        this.ime = ime;

    }

    public String getSelectedDate() {
        return selectedDate;
    }
    public String getImeUsluge() {
        return imeUsluge;
    }

    public String getloggedInUsername() {
        return loggedInUsername;
    }
    public String getSelectedTermin() {
        return selectedTermin;
    }


    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
