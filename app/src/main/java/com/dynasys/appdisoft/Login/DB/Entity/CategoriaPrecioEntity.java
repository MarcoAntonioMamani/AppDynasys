package com.dynasys.appdisoft.Login.DB.Entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "CategoriaPrecio")
public class CategoriaPrecioEntity  implements Serializable {

    @PrimaryKey()
    int Id;
    @ColumnInfo(name = "NombreCategoria")
    String NombreCategoria;

    public CategoriaPrecioEntity() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombreCategoria() {
        return NombreCategoria;
    }
    @Override
    public String toString() {
        return NombreCategoria;
    }
    public void setNombreCategoria(String nombreCategoria) {
        NombreCategoria = nombreCategoria;
    }
}
