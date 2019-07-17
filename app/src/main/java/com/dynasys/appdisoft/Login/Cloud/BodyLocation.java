package com.dynasys.appdisoft.Login.Cloud;

import java.util.Date;

public class BodyLocation {
    int ldchof;
    Date ldfec;
    String ldhora;
    Double lblat;
    Double lblongi;

    public BodyLocation(){

    }
    public BodyLocation(int ldchof, Date ldfec, String ldhora, Double lblat, Double lblongi) {
        this.ldchof = ldchof;
        this.ldfec = ldfec;
        this.ldhora = ldhora;
        this.lblat = lblat;
        this.lblongi = lblongi;
    }

    public int getLdchof() {
        return ldchof;
    }

    public void setLdchof(int ldchof) {
        this.ldchof = ldchof;
    }

    public Date getLdfec() {
        return ldfec;
    }

    public void setLdfec(Date ldfec) {
        this.ldfec = ldfec;
    }

    public String getLdhora() {
        return ldhora;
    }

    public void setLdhora(String ldhora) {
        this.ldhora = ldhora;
    }

    public Double getLblat() {
        return lblat;
    }

    public void setLblat(Double lblat) {
        this.lblat = lblat;
    }

    public Double getLblongi() {
        return lblongi;
    }

    public void setLblongi(Double lblongi) {
        this.lblongi = lblongi;
    }
}
