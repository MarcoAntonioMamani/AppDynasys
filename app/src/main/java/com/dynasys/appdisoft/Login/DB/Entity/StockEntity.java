package com.dynasys.appdisoft.Login.DB.Entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "stock")
public class StockEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "codigoProducto")
    int codigoProducto;
    @ColumnInfo(name = "cantidad")
    double cantidad;
    @ColumnInfo(name = "almacen")
    int almacen;

    public StockEntity(int id, int codigoProducto, double cantidad, int almacen) {
        this.id = id;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
        this.almacen = almacen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public int getAlmacen() {
        return almacen;
    }

    public void setAlmacen(int almacen) {
        this.almacen = almacen;
    }
}
