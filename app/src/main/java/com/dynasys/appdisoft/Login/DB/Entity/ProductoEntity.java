package com.dynasys.appdisoft.Login.DB.Entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "producto")
public class ProductoEntity {
    @PrimaryKey()
    @ColumnInfo(name = "numi")
    int cbnumi;
    @ColumnInfo(name = "cod")
    String cod;
    @ColumnInfo(name = "producto")
    String producto;
    @ColumnInfo(name = "desccorta")
    String desccorta;
    @ColumnInfo(name = "idcategoria")
    int idcategoria;
    @ColumnInfo(name = "categoria")
    String categoria;

    public ProductoEntity(int cbnumi, String cod, String producto, String desccorta, int idcategoria, String categoria) {
        this.cbnumi = cbnumi;
        this.cod = cod;
        this.producto = producto;
        this.desccorta = desccorta;
        this.idcategoria = idcategoria;
        this.categoria = categoria;
    }

    public int getCbnumi() {
        return cbnumi;
    }

    public void setCbnumi(int cbnumi) {
        this.cbnumi = cbnumi;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getDesccorta() {
        return desccorta;
    }

    public void setDesccorta(String desccorta) {
        this.desccorta = desccorta;
    }

    public int getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(int idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
