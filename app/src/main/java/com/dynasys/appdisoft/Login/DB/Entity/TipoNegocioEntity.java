package com.dynasys.appdisoft.Login.DB.Entity;

import java.io.Serializable;

public class TipoNegocioEntity implements Serializable {

    int Id;
    String Descripcion;

    public TipoNegocioEntity(int id, String descripcion) {
        Id = id;
        Descripcion = descripcion;
    }

    public TipoNegocioEntity() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
    @Override
    public String toString() {
        return Descripcion;
    }
}
