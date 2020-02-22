package com.dynasys.appdisoft.Login.DB.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


import java.util.Date;
@Entity(tableName = "pedido")
public class PedidoEntity implements Comparable<PedidoEntity> {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "oanumi")
    String oanumi;
    @ColumnInfo(name = "oafdoc")
    Date oafdoc;
    @ColumnInfo(name = "oahora")
    String oahora;
    @ColumnInfo(name = "oaccli")
    String oaccli;
    @ColumnInfo(name = "cliente")
    String cliente;
    @ColumnInfo(name = "oarepa")
    int oarepa;
    @ColumnInfo(name = "oaest")
    int oaest;
    @ColumnInfo(name = "oaobs")
    String oaobs;
    @ColumnInfo(name = "latitud")
    Double latitud;
    @ColumnInfo(name = "longitud")
    Double longitud;
    @ColumnInfo(name = "total")
    Double total;
    @ColumnInfo(name = "tipocobro")
    int tipocobro;
    @ColumnInfo(name = "totalcredito")
    Double totalcredito;
    @ColumnInfo(name = "estado")
    int estado;
    @ColumnInfo(name = "codigogenerado")
    String codigogenerado;
    @ColumnInfo(name = "estadoupdate")
    int estadoUpdate;
    
public PedidoEntity(){

}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOanumi() {
        return oanumi;
    }

    public void setOanumi(String oanumi) {
        this.oanumi = oanumi;
    }

    public Date getOafdoc() {
        return oafdoc;
    }

    public void setOafdoc(Date oafdoc) {
        this.oafdoc = oafdoc;
    }

    public String getOahora() {
        return oahora;
    }

    public void setOahora(String oahora) {
        this.oahora = oahora;
    }

    public String getOaccli() {
        return oaccli;
    }

    public void setOaccli(String oaccli) {
        this.oaccli = oaccli;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getOarepa() {
        return oarepa;
    }

    public void setOarepa(int oarepa) {
        this.oarepa = oarepa;
    }

    public int getOaest() {
        return oaest;
    }

    public void setOaest(int oaest) {
        this.oaest = oaest;
    }

    public String getOaobs() {
        return oaobs;
    }

    public void setOaobs(String oaobs) {
        this.oaobs = oaobs;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getTipocobro() {
        return tipocobro;
    }

    public void setTipocobro(int tipocobro) {
        this.tipocobro = tipocobro;
    }

    public Double getTotalcredito() {
        return totalcredito;
    }

    public void setTotalcredito(Double totalcredito) {
        this.totalcredito = totalcredito;
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

    public int getEstadoUpdate() {
        return estadoUpdate;
    }

    public void setEstadoUpdate(int estadoUpdate) {
        this.estadoUpdate = estadoUpdate;
    }

    @Override
    public int compareTo(PedidoEntity pedidoEntity) {
        int thisVal = this.getId();
        int anotherVal = pedidoEntity.getId();
        return (thisVal>anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
    }
}
