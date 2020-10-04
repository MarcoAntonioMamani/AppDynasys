package com.dynasys.appdisoft.ListarDeudas.Pagos;

import com.dynasys.appdisoft.Login.DB.Entity.CobranzaDetalleEntity;

import java.util.Date;
import java.util.List;

public class CobranzaRequest {
    int id;
    String tenumi;
    Date fecha;
    int IdPersonal;
    String observacion;
    int estado;
    List<CobranzaDetalleEntity> listDetalle;

    public CobranzaRequest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenumi() {
        return tenumi;
    }

    public void setTenumi(String tenumi) {
        this.tenumi = tenumi;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdPersonal() {
        return IdPersonal;
    }

    public void setIdPersonal(int idPersonal) {
        IdPersonal = idPersonal;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public List<CobranzaDetalleEntity> getListDetalle() {
        return listDetalle;
    }

    public void setListDetalle(List<CobranzaDetalleEntity> listDetalle) {
        this.listDetalle = listDetalle;
    }
}
