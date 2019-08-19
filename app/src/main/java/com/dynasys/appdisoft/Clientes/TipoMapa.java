package com.dynasys.appdisoft.Clientes;

import java.io.Serializable;

public class TipoMapa implements Serializable {

    int id;
    String title;

    public TipoMapa(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title;
    }
}
