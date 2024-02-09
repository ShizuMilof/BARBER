package com.example.barber.model;

public class Usluge {
    private String imeUsluge;

    private String cijenaUsluge;

    private String trajanjeUsluge;

    private String profileImageUrl;


    public Usluge() {

    }

    public Usluge(String imeUsluge,String cijenaUsluge,String trajanjeUsluge,String profileImageUrl) {

      this.imeUsluge = imeUsluge;
      this.cijenaUsluge = cijenaUsluge;
      this.trajanjeUsluge = trajanjeUsluge;
      this.profileImageUrl=profileImageUrl;

    }

    public String getCijenaUsluge() {
        return cijenaUsluge;
    }

    public String getProfileImageUrl() {
      return profileImageUrl;
    }

    public String getImeUsluge() {
        return imeUsluge;
    }

    public String getTrajanjeUsluge() {
        return trajanjeUsluge;
    }

}
