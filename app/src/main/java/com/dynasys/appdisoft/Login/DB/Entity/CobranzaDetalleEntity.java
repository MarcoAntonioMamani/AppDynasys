package com.dynasys.appdisoft.Login.DB.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "cobranzaDetalle")
public class CobranzaDetalleEntity {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "tdnumi")
    int tdnumi;
    @ColumnInfo(name = "pedidoId")
    int PedidoId;
    @ColumnInfo(name = "cobranzaId")
    String cobranzaId;
    @ColumnInfo(name = "fechaPago")
    Date fechaPago;
    @ColumnInfo(name = "montoAPagar")
    double montoAPagar;
    @ColumnInfo(name = "estado")
    int estado;

    public CobranzaDetalleEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTdnumi() {
        return tdnumi;
    }

    public void setTdnumi(int tdnumi) {
        this.tdnumi = tdnumi;
    }

    public int getPedidoId() {
        return PedidoId;
    }

    public void setPedidoId(int pedidoId) {
        PedidoId = pedidoId;
    }

    public String getCobranzaId() {
        return cobranzaId;
    }

    public void setCobranzaId(String cobranzaId) {
        this.cobranzaId = cobranzaId;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getMontoAPagar() {
        return montoAPagar;
    }

    public void setMontoAPagar(double montoAPagar) {
        this.montoAPagar = montoAPagar;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
