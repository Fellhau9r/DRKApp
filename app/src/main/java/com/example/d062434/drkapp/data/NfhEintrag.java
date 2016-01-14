package com.example.d062434.drkapp.data;

import java.util.Calendar;

/**
 * Created by D062434 on 09.11.2015.
 */
public class NfhEintrag {
    private String mitgliedId;
    private Calendar datum;
    private String status;

    public NfhEintrag(String mitgliedId, Calendar datum, String status) {
        this.mitgliedId = mitgliedId;
        this.datum = datum;
        this.status = status;
    }

    public String getMitgliedId() {
        return mitgliedId;
    }

    public void setMitgliedId(String mitgliedId) {
        this.mitgliedId = mitgliedId;
    }

    public Calendar getDatum() {
        return datum;
    }

    public void setDatum(Calendar datum) {
        this.datum = datum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
