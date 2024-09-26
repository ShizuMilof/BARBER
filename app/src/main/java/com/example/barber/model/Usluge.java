package com.example.barber.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Usluge implements Parcelable {
    private String naziv;
    private String cijena;
    private String trajanje;
    private boolean isSelected;
    private String urlSlike;

    // Constructor
    public Usluge(String naziv, String cijena, boolean isSelected, String imageUrl, String trajanje) {
        this.naziv = naziv;
        this.cijena = cijena;
        this.isSelected = isSelected;
        this.urlSlike = imageUrl;
        this.trajanje = trajanje;
    }

    // Protected constructor that reads from a Parcel
    protected Usluge(Parcel in) {
        naziv = in.readString();
        cijena = in.readString();
        isSelected = in.readByte() != 0;  // isSelected will be true if byte != 0
        urlSlike = in.readString();
        trajanje = in.readString(); // Read trajanje from the Parcel
    }

    // Parcelable.Creator
    public static final Creator<Usluge> CREATOR = new Creator<Usluge>() {
        @Override
        public Usluge createFromParcel(Parcel in) {
            return new Usluge(in);
        }

        @Override
        public Usluge[] newArray(int size) {
            return new Usluge[size];
        }
    };

    // Implementing Parcelable interface methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(naziv);
        dest.writeString(cijena);
        dest.writeByte((byte) (isSelected ? 1 : 0));  // Write isSelected as a byte
        dest.writeString(urlSlike);
        dest.writeString(trajanje); // Ensure trajanje is written to the Parcel
    }

    // Getter and setter methods
    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getCijena() {
        return cijena;
    }

    public void setCijena(String cijena) {
        this.cijena = cijena;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSlika() {
        return urlSlike;
    }

    public void setImageUrl(String imageUrl) {
        this.urlSlike = imageUrl;
    }

    public String getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }
}
