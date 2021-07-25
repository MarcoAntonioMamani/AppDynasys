package com.dynasys.appdisoft.Login.DB.Entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "visita")
public class VisitaEntity {

    @PrimaryKey(autoGenerate = true)
    int codigo;
    @ColumnInfo(name = "id")
    int id;
    @ColumnInfo(name = "RepartidorId")
    int RepartidorId;
    @ColumnInfo(name = "PedidoId")
    int PedidoId;
    @ColumnInfo(name = "ClienteId")
    int ClienteId;
    @ColumnInfo(name = "NombreCliente")
    String NombreCliente;
    @ColumnInfo(name = "Direccion")
    String Direccion;
    @ColumnInfo(name = "Telefono")
    String Telefono;
    @ColumnInfo(name = "Descripcion")
    String Descripcion;
    @ColumnInfo(name = "Estado")
    int Estado;
    @ColumnInfo(name = "Latitud")
    double Latitud;
    @ColumnInfo(name = "Longitud")
    double Longitud;
    @ColumnInfo(name = "Sincronizado")
    int Sincronizado;
    @ColumnInfo(name = "IdSincronizacion")
    String IdSincronizacion;
    @ColumnInfo(name = "fecha")
    String fecha;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getSincronizado() {
        return Sincronizado;
    }

    public void setSincronizado(int sincronizado) {
        Sincronizado = sincronizado;
    }

    public String getIdSincronizacion() {
        return IdSincronizacion;
    }

    public void setIdSincronizacion(String idSincronizacion) {
        IdSincronizacion = idSincronizacion;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRepartidorId() {
        return RepartidorId;
    }

    public void setRepartidorId(int repartidorId) {
        RepartidorId = repartidorId;
    }

    public int getPedidoId() {
        return PedidoId;
    }

    public void setPedidoId(int pedidoId) {
        PedidoId = pedidoId;
    }

    public int getClienteId() {
        return ClienteId;
    }

    public void setClienteId(int clienteId) {
        ClienteId = clienteId;
    }

    public String getNombreCliente() {
        return NombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        NombreCliente = nombreCliente;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }
}
