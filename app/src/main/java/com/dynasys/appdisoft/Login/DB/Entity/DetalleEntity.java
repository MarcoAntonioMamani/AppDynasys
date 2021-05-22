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
    @ColumnInfo(name = "descuento")
    double descuento;
    @ColumnInfo(name = "total")
    double total;
    @ColumnInfo(name = "familia")
    int familia;
    @ColumnInfo(name = "estado")
    boolean estado;

    @ColumnInfo(name = "obupdate")
    int obupdate;

    @ColumnInfo(name = "stock")
    double stock;

    //cajas
    @ColumnInfo(name = "cajas")
    double cajas;
    @ColumnInfo(name = "conversion")
    double conversion;
    public DetalleEntity(){

    }

    public DetalleEntity(int id, String obnumi, int obcprod, String cadesc, double obpcant, double obpbase, double obptot, double descuento, double total, int familia, boolean estado, int obupdate, double stock, double cajas, double conversion) {
        this.id = id;
        this.obnumi = obnumi;
        this.obcprod = obcprod;
        this.cadesc = cadesc;
        this.obpcant = obpcant;
        this.obpbase = obpbase;
        this.obptot = obptot;
        this.descuento = descuento;
        this.total = total;
        this.familia = familia;
        this.estado = estado;
        this.obupdate = obupdate;
        this.stock = stock;
        this.cajas = cajas;
        this.conversion = conversion;
    }

    public double getConversion() {
        return conversion;
    }

    public void setConversion(double conversion) {
        this.conversion = conversion;
    }

    public double getCajas() {
        return cajas;
    }

    public void setCajas(double cajas) {
        this.cajas = cajas;
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

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getFamilia() {
        return familia;
    }

    public void setFamilia(int familia) {
        this.familia = familia;
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

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public DetalleEntity clone() throws CloneNotSupportedException {
        return (DetalleEntity) super.clone();
    }
}

