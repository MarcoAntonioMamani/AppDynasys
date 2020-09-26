package com.dynasys.appdisoft.Login.DB.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "zona")
public class ZonasEntity   implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "lanumi")
    int lanumi;
    @ColumnInfo(name = "zona")
    String zona;
    @ColumnInfo(name = "idRepartidor")
    int idRepartidor;

    public ZonasEntity() {
    }



    public int getLanumi() {
        return lanumi;
    }

    public void setLanumi(int lanumi) {
        this.lanumi = lanumi;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }
    @Override
    public String toString() {
        return zona;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
