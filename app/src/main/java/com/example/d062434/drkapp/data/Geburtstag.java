package com.example.d062434.drkapp.data;

/**
 * Created by D062434 on 02.11.2015.
 */
public class Geburtstag {
    private String vname;
    private String nname;
    private String gebdat;
    private String alter;

    public Geburtstag(String vname, String nname, String gebdat, String alter) {
        this.vname = vname;
        this.nname = nname;
        this.gebdat = gebdat;
        this.alter = alter;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getNname() {
        return nname;
    }

    public void setNname(String nname) {
        this.nname = nname;
    }

    public String getGebdat() {
        return gebdat;
    }

    public void setGebdat(String gebdat) {
        this.gebdat = gebdat;
    }

    public String getAlter() {
        return alter;
    }

    public void setAlter(String alter) {
        this.alter = alter;
    }
}
