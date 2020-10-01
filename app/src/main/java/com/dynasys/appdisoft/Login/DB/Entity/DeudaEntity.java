package com.dynasys.appdisoft.Login.DB.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

import java.util.Date;

@Entity(tableName = "deuda")
public class DeudaEntity implements Comparable<DeudaEntity> {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "PedidoId")
    int PedidoId;
    @ColumnInfo(name = "ClienteId")
    int ClienteId;
    @ColumnInfo(name = "cliente")
    String cliente;
    @ColumnInfo(name = "direccion")
    String direccion;
    @ColumnInfo(name = "telefono")
    String telefono;
    @ColumnInfo(name = "limiteCliente")
    double limiteCliente;
    @ColumnInfo(name = "PersonalId")
    int PersonalId;
    @ColumnInfo(name = "vendedor")
    String vendedor;
    @ColumnInfo(name = "FechaPedido")
    Date  FechaPedido;
    @ColumnInfo(name = "totalfactura")
    double totalfactura;
    @ColumnInfo(name = "pendiente")
    double pendiente;
    @ColumnInfo(name = "estado")
    int estado;
    @Ignore
    double totalAPagar;

    public double getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(double totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public DeudaEntity() {
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

    public double getLimiteCliente() {
        return limiteCliente;
    }

    public void setLimiteCliente(double limiteCliente) {
        this.limiteCliente = limiteCliente;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getPersonalId() {
        return PersonalId;
    }

    public void setPersonalId(int personalId) {
        PersonalId = personalId;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public Date getFechaPedido() {
        return FechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        FechaPedido = fechaPedido;
    }

    public double getTotalfactura() {
        return totalfactura;
    }

    public void setTotalfactura(double totalfactura) {
        this.totalfactura = totalfactura;
    }

    public double getPendiente() {
        return pendiente;
    }

    public void setPendiente(double pendiente) {
        this.pendiente = pendiente;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public int compareTo( DeudaEntity cliente) {
        String a=new String(String.valueOf(this.getCliente()));
        String b=new String(String.valueOf(cliente.getCliente()));
        return a.compareTo(b);
    }
}
