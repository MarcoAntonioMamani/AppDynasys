package com.dynasys.appdisoft.RevisarEfectivo;

import java.util.Date;

public class Efectivo {
    String idPedido;
    String idCliente;
    String Cliente;
    double MontoContado;
    double MontoCredito;
    int Tipo;
    Date Fecha;
    String Observacion;
    public Efectivo() {
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }


    public double getMontoContado() {
        return MontoContado;
    }

    public void setMontoContado(double montoContado) {
        MontoContado = montoContado;
    }

    public double getMontoCredito() {
        return MontoCredito;
    }

    public void setMontoCredito(double montoCredito) {
        MontoCredito = montoCredito;
    }

    public int getTipo() {
        return Tipo;
    }

    public void setTipo(int tipo) {
        Tipo = tipo;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }
}
