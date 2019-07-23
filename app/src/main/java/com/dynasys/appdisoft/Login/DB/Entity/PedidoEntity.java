package com.dynasys.appdisoft.Login.DB.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
@Entity(tableName = "pedido")
public class PedidoEntity {
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
    @ColumnInfo(name = "estado")
    int estado;
    @ColumnInfo(name = "codigogenerado")
    String codigogenerado;
public PedidoEntity(){

}

    public PedidoEntity(int id, String oanumi, Date oafdoc, String oahora, String oaccli, String cliente, int oarepa, int oaest, String oaobs, Double latitud, Double longitud, Double total, int tipocobro, int estado, String codigogenerado) {
        this.id = id;
        this.oanumi = oanumi;
        this.oafdoc = oafdoc;
        this.oahora = oahora;
        this.oaccli = oaccli;
        this.cliente = cliente;
        this.oarepa = oarepa;
        this.oaest = oaest;
        this.oaobs = oaobs;
        this.latitud = latitud;
        this.longitud = longitud;
        this.total = total;
        this.tipocobro = tipocobro;
        this.estado = estado;
        this.codigogenerado = codigogenerado;
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
}
