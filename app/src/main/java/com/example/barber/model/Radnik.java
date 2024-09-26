package com.example.barber.model;

import java.util.Date;
import java.util.List;

public class Radnik {
	private int id;
	private String username;
	private String ime;
	private String prezime;
	private String email;
	private String lozinka;
	private int ulogeId;
	private boolean verificiran;
	private String urlSlike;
	private int brojacRezerviranihTermina;
	private int brojacOtkazanihTermina;
	private int brojacOdradenihTermina;
	private int ukupnoVrijemeRada;
	private String datum;  // New field for date

	private List<Date> rezervisaniTermini; // New field
	private List<Date> otkazaniTermini;    // New field
	private List<Date> odradeniTermini;    // New field

	private boolean isExpanded;

	public Radnik() {
		this.isExpanded = false;
	}

	// Getteri
	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getIme() {
		return ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public String getEmail() {
		return email;
	}

	public String getLozinka() {
		return lozinka;
	}

	public String getUrlSlike() {
		return urlSlike;
	}

	public int getUlogeId() {
		return ulogeId;
	}

	public boolean isVerificiran() {
		return verificiran;
	}

	public int getBrojacRezerviranihTermina() {
		return brojacRezerviranihTermina;
	}

	public int getBrojacOtkazanihTermina() {
		return brojacOtkazanihTermina;
	}

	public int getBrojacOdradenihTermina() {
		return brojacOdradenihTermina;
	}

	public int getUkupnoVrijemeRada() {
		return ukupnoVrijemeRada;
	}

	// Setteri
	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public void setUrlSlike(String urlSlike) {
		this.urlSlike = urlSlike;
	}

	public void setUlogeId(int ulogeId) {
		this.ulogeId = ulogeId;
	}

	public void setVerificiran(boolean verificiran) {
		this.verificiran = verificiran;
	}

	public void setBrojacRezerviranihTermina(int brojacRezerviranihTermina) {
		this.brojacRezerviranihTermina = brojacRezerviranihTermina;
	}

	public void setBrojacOtkazanihTermina(int brojacOtkazanihTermina) {
		this.brojacOtkazanihTermina = brojacOtkazanihTermina;
	}

	public void setBrojacOdradenihTermina(int brojacOdradenihTermina) {
		this.brojacOdradenihTermina = brojacOdradenihTermina;
	}

	public void setUkupnoVrijemeRada(int ukupnoVrijemeRada) {
		this.ukupnoVrijemeRada = ukupnoVrijemeRada;
	}

	public List<Date> getRezervisaniTermini() {
		return rezervisaniTermini;
	}

	public void setRezervisaniTermini(List<Date> rezervisaniTermini) {
		this.rezervisaniTermini = rezervisaniTermini;
	}
	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}
	public List<Date> getOtkazaniTermini() {
		return otkazaniTermini;
	}

	public void setOtkazaniTermini(List<Date> otkazaniTermini) {
		this.otkazaniTermini = otkazaniTermini;
	}

	public List<Date> getOdradeniTermini() {
		return odradeniTermini;
	}

	public void setOdradeniTermini(List<Date> odradeniTermini) {
		this.odradeniTermini = odradeniTermini;
	}

	// Other getters and setters...

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean expanded) {
		isExpanded = expanded;
	}

	@Override
	public String toString() {
		return "Radnik{" +
				"id=" + id +
				", username='" + username + '\'' +
				", ime='" + ime + '\'' +
				", prezime='" + prezime + '\'' +
				", email='" + email + '\'' +
				", lozinka='" + lozinka + '\'' +
				", ulogeId=" + ulogeId +
				", verificiran=" + verificiran +
				", urlSlike='" + urlSlike + '\'' +
				", brojacRezerviranihTermina=" + brojacRezerviranihTermina +
				", brojacOtkazanihTermina=" + brojacOtkazanihTermina +
				", brojacOdradenihTermina=" + brojacOdradenihTermina +
				", ukupnoVrijemeRada=" + ukupnoVrijemeRada +
				", datum='" + datum + '\'' +
				", isExpanded=" + isExpanded +
				'}';
	}
}
