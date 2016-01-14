package com.example.d062434.drkapp.data;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by D062434 on 19.10.2015.
 */
public class Termin {

    private Terminkategorie kategorie;
    private String bezeichnung;
    private String id;
    private String ort;
    private boolean statistikrelevanz;
    private String beschreibung;
    private Calendar beginn;
    private Calendar ende;
    private int minPers;
    private int maxPers;
    private ArrayList<Mitglied> teilnehmer = new ArrayList<>();
    private int currentPers;
    private boolean persUnbegrenzt;

    public Termin(String id, Terminkategorie kategorie, String bezeichnung, String ort, boolean statistikrelevanz, String beschreibung, Calendar beginn, Calendar ende, int minPers, int maxPers, boolean persUnbegrenzt) {

        this.kategorie = kategorie;
        this.bezeichnung = bezeichnung;
        this.ort = ort;
        this.statistikrelevanz = statistikrelevanz;
        this.beschreibung = beschreibung;
        this.beginn = beginn;
        this.ende = ende;
        this.minPers = minPers;
        this.maxPers = maxPers;
        this.persUnbegrenzt = persUnbegrenzt;
        this.id = id;
    }

    public Termin(String terminName, String terminDatum, String terminID, int currentPers, int minPers, int maxPers) {
        this.bezeichnung = terminName;

        int day = Integer.parseInt(terminDatum.substring(0,2));
        int month = Integer.parseInt(terminDatum.substring(3,5));
        int year = Integer.parseInt("20".concat(terminDatum.substring(6,8)));
        this.beginn = Calendar.getInstance();
        this.beginn.set(Calendar.DAY_OF_MONTH, day);
        this.beginn.set(Calendar.MONTH, month-1);
        this.beginn.set(Calendar.YEAR, year);
        if(terminDatum.length() > 8){
            day = Integer.parseInt(terminDatum.substring(11,13));
            month = Integer.parseInt(terminDatum.substring(14,16));
            year = Integer.parseInt("20".concat(terminDatum.substring(17, 19)));
            this.ende = Calendar.getInstance();
            this.ende.set(Calendar.DAY_OF_MONTH, day);
            this.ende.set(Calendar.MONTH, month-1);
            this.ende.set(Calendar.YEAR, year);
        }
        else{
            this.ende = (Calendar) this.beginn.clone();
        }

        this.currentPers = currentPers;
        this.id = terminID;
        this.minPers = minPers;
        this.maxPers = maxPers;

    }

    public Termin() {

    }

    public Termin(String terName, Calendar beginn, String terId) {
        this.id = terId;
        this.bezeichnung = terName;
        this.beginn = beginn;
    }

    public Termin(String terName, String terId) {
        this.bezeichnung = terName;
        this.id = terId;
    }

    public String getKategorie() {
        String rueck = "";
        switch(kategorie){
            case AUSBILDUNG: rueck = "Aus- und Weiterbildung"; break;
            case BEREITSCHAFT: rueck = "Bereitschaft"; break;
            case BESPRECHUNG: rueck = "Besprechung/Sitzung"; break;
            case BLUTSPENDEN: rueck = "Blutspendetermin"; break;
            case FESTVERANSTALTUNG: rueck = "Festveranstaltung"; break;
            case LEHRGANG: rueck = "Lehrgang"; break;
            case SANDIENST: rueck = "Sanitätsdienst"; break;
            case TURNIERTEILNAHME: rueck = "Turnierteilnahme"; break;
            case UEBUNGSEINHEIT: rueck = "Übungseinheit"; break;
            default: rueck = "Sonstiges"; break;
        }

        return rueck;
    }

    public void setKategorie(Terminkategorie kategorie) {
        this.kategorie = kategorie;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public boolean isStatistikrelevanz() {
        return statistikrelevanz;
    }

    public void setStatistikrelevanz(boolean statistikrelevanz) {
        this.statistikrelevanz = statistikrelevanz;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Calendar getBeginn() {
        return beginn;
    }

    public void setBeginn(Calendar beginn) {
        this.beginn = beginn;
    }

    public Calendar getEnde() {
        return ende;
    }

    public void setEnde(Calendar ende) {
        this.ende = ende;
    }

    public int getMinPers() {
        return minPers;
    }

    public void setMinPers(int minPers) {
        this.minPers = minPers;
    }

    public int getMaxPers() {
        return maxPers;
    }

    public void setMaxPers(int maxPers) {
        this.maxPers = maxPers;
    }

    public boolean isPersUnbegrenzt() {
        return persUnbegrenzt;
    }

    public void setPersUnbegrenzt(boolean persUnbegrenzt) {
        this.persUnbegrenzt = persUnbegrenzt;
    }

    public int getMonthDay(){
        return beginn.get(Calendar.DAY_OF_MONTH);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Mitglied> getTeilnehmer() {
        return teilnehmer;
    }

    public void setTeilnehmer(ArrayList<Mitglied> teilnehmer) {
        this.teilnehmer = teilnehmer;
    }

    public int getCurrentPers() {
        return currentPers;
    }

    public void setCurrentPers(int currentPers) {
        this.currentPers = currentPers;
    }

    public Terminteilnahme getTeilnahme(){
        if(this.getCurrentPers() == 0){
            return Terminteilnahme.NIEMAND;
        }
        else{
            if(this.currentPers < minPers){
                return Terminteilnahme.WENIGE;
            }
            else{
                return Terminteilnahme.VOLL;
            }
        }
    }
}
