package com.dynasys.appdisoft.Login.DB.Entity;

import java.util.Date;
import java.util.List;

public class PedidoDetallle {


    int id;

    String oanumi;

    Date oafdoc;

    String oahora;

    String oaccli;

    String cliente;

    int oarepa;

    int oaest;

    String oaobs;

    Double latitud;

    Double longitud;

    Double total;

    int tipocobro;

    Double totalcredito;

    int estado;

    String codigogenerado;

    int estadoUpdate;
    List<DetalleEntity> detalle;

    public PedidoDetallle(){

    }

    public List<DetalleEntity> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleEntity> detalle) {
        this.detalle = detalle;
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

}
