package com.dynasys.appdisoft.Login.DB.Entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "precio")
public class PrecioEntity {
    @PrimaryKey()
    @ColumnInfo(name = "chnumi")
    int chnumi;
    @ColumnInfo(name = "chcprod")
    int chcprod;
    @ColumnInfo(name = "chcatcl")
    int chcatcl;
    @ColumnInfo(name = "chprecio")
    double chprecio;

    public PrecioEntity(int chnumi, int chcprod, int chcatcl, double chprecio) {
        this.chnumi = chnumi;
        this.chcprod = chcprod;
        this.chcatcl = chcatcl;
        this.chprecio = chprecio;
    }

    public int getChnumi() {
        return chnumi;
    }

    public void setChnumi(int chnumi) {
        this.chnumi = chnumi;
    }

    public int getChcprod() {
        return chcprod;
    }

    public void setChcprod(int chcprod) {
        this.chcprod = chcprod;
    }

    public int getChcatcl() {
        return chcatcl;
    }

    public void setChcatcl(int chcatcl) {
        this.chcatcl = chcatcl;
    }

    public double getChprecio() {
        return chprecio;
    }

    public void setChprecio(double chprecio) {
        this.chprecio = chprecio;
    }
}
