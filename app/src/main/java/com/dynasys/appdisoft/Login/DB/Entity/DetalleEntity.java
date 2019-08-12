package com.dynasys.appdisoft.Login.DB.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "detalle")
public class DetalleEntity  implements Cloneable{
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "obnumi")
    String obnumi;
    @ColumnInfo(name = "obcprod")
    int obcprod;
    @ColumnInfo(name = "cadesc")
    String cadesc;
    @ColumnInfo(name = "obpcant")
    double obpcant;
    @ColumnInfo(name = "obpbase")
    double obpbase;
    @ColumnInfo(name = "obptot")
    double obptot;
    @ColumnInfo(name = "estado")
    boolean estado;

    @ColumnInfo(name = "obupdate")
    int obupdate;
    public DetalleEntity(){

    }

    public DetalleEntity(int id, String obnumi, int obcprod, String cadesc, double obpcant, double obpbase, double obptot, boolean estado, int obupdate) {
        this.id = id;
        this.obnumi = obnumi;
        this.obcprod = obcprod;
        this.cadesc = cadesc;
        this.obpcant = obpcant;
        this.obpbase = obpbase;
        this.obptot = obptot;
        this.estado = estado;
        this.obupdate = obupdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObnumi() {
        return obnumi;
    }

    public void setObnumi(String obnumi) {
        this.obnumi = obnumi;
    }

    public int getObcprod() {
        return obcprod;
    }

    public void setObcprod(int obcprod) {
        this.obcprod = obcprod;
    }

    public String getCadesc() {
        return cadesc;
    }

    public void setCadesc(String cadesc) {
        this.cadesc = cadesc;
    }

    public double getObpcant() {
        return obpcant;
    }

    public void setObpcant(double obpcant) {
        this.obpcant = obpcant;
    }

    public double getObpbase() {
        return obpbase;
    }

    public void setObpbase(double obpbase) {
        this.obpbase = obpbase;
    }

    public double getObptot() {
        return obptot;
    }

    public void setObptot(double obptot) {
        this.obptot = obptot;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getObupdate() {
        return obupdate;
    }

    public void setObupdate(int obupdate) {
        this.obupdate = obupdate;
    }

    public DetalleEntity clone() throws CloneNotSupportedException {
        return (DetalleEntity) super.clone();
    }
}

