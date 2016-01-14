package com.example.d062434.drkapp.data;

import java.util.Calendar;

/**
 * Created by D062434 on 19.10.2015.
 */
public class Mitglied {

    /* Persönliche Daten */
    private int anrede; //Enum verfügbar
    private int titel; //Enum verfügbar
    private String vorname;
    private String nachname;
    private Calendar gebDatum;
    private String straße;
    private String plz;
    private String ort;
    private String id;

    /* Vereinsdaten */
    private int vereinsposition; //Enum verfügbar
    private int vereinsstatus; //Enum verfügbar
    private boolean notfallhilfe;
    private Calendar eintrittsdatum;
    private Calendar austrittsdatum;
    private boolean mitgliedsstatus;

    /* Kommunikationsdaten */
    private String mailPrivat;
    private String mailGeschaeft;
    private String telePrivat;
    private String teleGeschaeft;
    private String teleMobil;
    private String faxPrivat;
    private String faxGeschaeft;

    /* Temporäre Termineinsatzdaten */
    private Calendar begZeit;
    private Calendar endZeit;

    public Mitglied(int anrede, int titel, String vorname, String nachname, Calendar gebDatum, String straße, String plz, String ort, int vereinsposition, int vereinsstatus, boolean notfallhilfe, Calendar eintrittsdatum, Calendar austrittsdatum, boolean mitgliedsstatus, String mailPrivat, String mailGeschaeft, String telePrivat, String teleGeschaeft, String teleMobil, String faxPrivat, String faxGeschaeft) {
        this.anrede = anrede;
        this.titel = titel;
        this.vorname = vorname;
        this.nachname = nachname;
        this.gebDatum = gebDatum;
        this.straße = straße;
        this.plz = plz;
        this.ort = ort;
        this.vereinsposition = vereinsposition;
        this.vereinsstatus = vereinsstatus;
        this.notfallhilfe = notfallhilfe;
        this.eintrittsdatum = eintrittsdatum;
        this.austrittsdatum = austrittsdatum;
        this.mitgliedsstatus = mitgliedsstatus;
        this.mailPrivat = mailPrivat;
        this.mailGeschaeft = mailGeschaeft;
        this.telePrivat = telePrivat;
        this.teleGeschaeft = teleGeschaeft;
        this.teleMobil = teleMobil;
        this.faxPrivat = faxPrivat;
        this.faxGeschaeft = faxGeschaeft;
    }

    public Mitglied(String id, String vorname, String nachname, Calendar begZeit, Calendar endZeit){
        this.vorname = vorname;
        this.nachname = nachname;
        this.begZeit = begZeit;
        this.endZeit = endZeit;
        this.id = id;
    }

    public Mitglied(String vorname, String nachname, String id){
        this.vorname = vorname;
        this.nachname = nachname;
        this.id = id;
    }

    public Mitglied() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Calendar getBegZeit() {
        return begZeit;
    }

    public void setBegZeit(Calendar begZeit) {
        this.begZeit = begZeit;
    }

    public Calendar getEndZeit() {
        return endZeit;
    }

    public void setEndZeit(Calendar endZeit) {
        this.endZeit = endZeit;
    }

    public int getAnrede() {
        return anrede;
    }

    public void setAnrede(int anrede) {
        this.anrede = anrede;
    }

    public int getTitel() {
        return titel;
    }

    public void setTitel(int titel) {
        this.titel = titel;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public Calendar getGebDatum() {
        return gebDatum;
    }

    public void setGebDatum(Calendar gebDatum) {
        this.gebDatum = gebDatum;
    }

    public String getStraße() {
        return straße;
    }

    public void setStraße(String straße) {
        this.straße = straße;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public int getVereinsposition() {
        return vereinsposition;
    }

    public void setVereinsposition(int vereinsposition) {
        this.vereinsposition = vereinsposition;
    }

    public int getVereinsstatus() {
        return vereinsstatus;
    }

    public void setVereinsstatus(int vereinsstatus) {
        this.vereinsstatus = vereinsstatus;
    }

    public boolean isNotfallhilfe() {
        return notfallhilfe;
    }

    public void setNotfallhilfe(boolean notfallhilfe) {
        this.notfallhilfe = notfallhilfe;
    }

    public Calendar getEintrittsdatum() {
        return eintrittsdatum;
    }

    public void setEintrittsdatum(Calendar eintrittsdatum) {
        this.eintrittsdatum = eintrittsdatum;
    }

    public Calendar getAustrittsdatum() {
        return austrittsdatum;
    }

    public void setAustrittsdatum(Calendar austrittsdatum) {
        this.austrittsdatum = austrittsdatum;
    }

    public boolean isMitgliedsstatus() {
        return mitgliedsstatus;
    }

    public void setMitgliedsstatus(boolean mitgliedsstatus) {
        this.mitgliedsstatus = mitgliedsstatus;
    }

    public String getMailPrivat() {
        return mailPrivat;
    }

    public void setMailPrivat(String mailPrivat) {
        this.mailPrivat = mailPrivat;
    }

    public String getMailGeschaeft() {
        return mailGeschaeft;
    }

    public void setMailGeschaeft(String mailGeschaeft) {
        this.mailGeschaeft = mailGeschaeft;
    }

    public String getTelePrivat() {
        return telePrivat;
    }

    public void setTelePrivat(String telePrivat) {
        this.telePrivat = telePrivat;
    }

    public String getTeleGeschaeft() {
        return teleGeschaeft;
    }

    public void setTeleGeschaeft(String teleGeschaeft) {
        this.teleGeschaeft = teleGeschaeft;
    }

    public String getTeleMobil() {
        return teleMobil;
    }

    public void setTeleMobil(String teleMobil) {
        this.teleMobil = teleMobil;
    }

    public String getFaxPrivat() {
        return faxPrivat;
    }

    public void setFaxPrivat(String faxPrivat) {
        this.faxPrivat = faxPrivat;
    }

    public String getFaxGeschaeft() {
        return faxGeschaeft;
    }

    public void setFaxGeschaeft(String faxGeschaeft) {
        this.faxGeschaeft = faxGeschaeft;
    }
}
