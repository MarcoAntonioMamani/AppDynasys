package com.dynasys.appdisoft.Login.DB.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "descuentos")
public class DescuentosEntity {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    int id;
    @ColumnInfo(name = "idProducto")
    int idProducto;
    @ColumnInfo(name = "cantidad1")
    int cantidad1;
    @ColumnInfo(name = "cantidad2")
    int cantidad2;
    @ColumnInfo(name = "fechaInicio")
    Date fechaInicio;
    @ColumnInfo(name = "fechaFin")
    Date fechaFin;
    @ColumnInfo(name = "precio")
    Double precio;

    public DescuentosEntity(int id, int idProducto, int cantidad1, int cantidad2, Date fechaInicio, Date fechaFin, Double precio) {
        this.id = id;
        this.idProducto = idProducto;
        this.cantidad1 = cantidad1;
        this.cantidad2 = cantidad2;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad1() {
        return cantidad1;
    }

    public void setCantidad1(int cantidad1) {
        this.cantidad1 = cantidad1;
    }

    public int getCantidad2() {
        return cantidad2;
    }

    public void setCantidad2(int cantidad2) {
        this.cantidad2 = cantidad2;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
