package com.dynasys.appdisoft.Login.DB.Entity;

import java.io.Serializable;

public class PrecioCategoriaEntity implements Serializable {

    int id ;
    String nombre;

    public PrecioCategoriaEntity(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return nombre;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
