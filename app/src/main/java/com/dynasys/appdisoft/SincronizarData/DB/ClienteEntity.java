package com.dynasys.appdisoft.SincronizarData.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "clientes")
public class ClienteEntity  implements Comparable<ClienteEntity> {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "numi")
    int numi;
    @ColumnInfo(name = "codigo")
    String codigo;
    @ColumnInfo(name = "namecliente")
    String namecliente;
    @ColumnInfo(name = "nit")
    String nit;
    @ColumnInfo(name = "direccion")
    String direccion;
    @ColumnInfo(name = "telefono")
    String telefono;
    @ColumnInfo(name = "latitud")
    Double latitud;
    @ColumnInfo(name = "longitud")
    Double longitud;
    @ColumnInfo(name = "fechaingreso")
    Date fechaingreso;
    @ColumnInfo(name = "estado")
    int estado;
    @ColumnInfo(name = "codigogenerado")
    String codigogenerado;
    @ColumnInfo(name = "cccat")
    int cccat;
    @ColumnInfo(name = "cczona")
    int cczona;
    public ClienteEntity(){

    }

    public ClienteEntity(int id, int numi, String codigo, String namecliente, String nit, String direccion, String telefono, Double latitud, Double longitud, Date fechaingreso, int estado, String codigogenerado, int cccat, int cczona) {
        this.id = id;
        this.numi = numi;
        this.codigo = codigo;
        this.namecliente = namecliente;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaingreso = fechaingreso;
        this.estado = estado;
        this.codigogenerado = codigogenerado;
        this.cccat = cccat;
        this.cczona = cczona;
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

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getCodigogenerado() {
        return codigogenerado;
    }

    public void setCodigogenerado(String codigogenerado) {
        this.codigogenerado = codigogenerado;
    }

    public int getCccat() {
        return cccat;
    }

    public void setCccat(int cccat) {
        this.cccat = cccat;
    }

    public int getCczona() {
        return cczona;
    }

    public void setCczona(int cczona) {
        this.cczona = cczona;
    }

    @Override
    public int compareTo( ClienteEntity cliente) {
        String a=new String(String.valueOf(this.getNamecliente()));
        String b=new String(String.valueOf(cliente.getNamecliente()));
        return a.compareTo(b);
    }
}
