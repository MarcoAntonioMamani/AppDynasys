package com.dynasys.appdisoft.SincronizarData.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "clientes")
public class ClienteEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "numi")
    int numi;
    @ColumnInfo(name = "codigo")
    String codigo;
    @ColumnInfo(name = "namecliente")
    String namecliente;
    @ColumnInfo(name = "direccion")
    String direccion;
    @ColumnInfo(name = "latitud")
    Double latitud;
    @ColumnInfo(name = "longitud")
    Double longitud;
    @ColumnInfo(name = "fechaingreso")
    Date fechaingreso;
    @ColumnInfo(name = "estado")
    boolean estado;
    @ColumnInfo(name = "codigogenerado")
    String codigogenerado;
    public ClienteEntity(){

    }
    public ClienteEntity(int id, int numi, String codigo, String namecliente, String direccion, Double latitud, Double longitud, Date fechaingreso, boolean estado, String codigogenerado) {
        this.id = id;
        this.numi = numi;
        this.codigo = codigo;
        this.namecliente = namecliente;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaingreso = fechaingreso;
        this.estado = estado;
        this.codigogenerado = codigogenerado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumi() {
        return numi;
    }

    public void setNumi(int numi) {
        this.numi = numi;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNamecliente() {
        return namecliente;
    }

    public void setNamecliente(String namecliente) {
        this.namecliente = namecliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Date getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Date fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getCodigogenerado() {
        return codigogenerado;
    }

    public void setCodigogenerado(String codigogenerado) {
        this.codigogenerado = codigogenerado;
    }
}
