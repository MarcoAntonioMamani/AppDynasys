package com.dynasys.appdisoft.Login.DB.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "cobranza")
public class CobranzaEntity {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "tenumi")
    int tenumi;
    @ColumnInfo(name = "fecha")
    Date fecha;
    @ColumnInfo(name = "IdPersonal")
    int IdPersonal;
    @ColumnInfo(name = "observacion")
    String observacion;
    @ColumnInfo(name = "estado")
    int estado;
    public CobranzaEntity() {
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTenumi() {
        return tenumi;
    }

    public void setTenumi(int tenumi) {
        this.tenumi = tenumi;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdPersonal() {
        return IdPersonal;
    }

    public void setIdPersonal(int idPersonal) {
        IdPersonal = idPersonal;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
