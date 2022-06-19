package com.dynasys.appdisoft.Login.DB.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "almacen")
public class AlmacenEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "ProductoId")
    int ProductoId;
    @ColumnInfo(name = "Producto")
    String Producto;
    @ColumnInfo(name = "fecha")
    Date fecha;
    @ColumnInfo(name = "inicial")
    double inicial;

    @ColumnInfo(name = "ingreso")
    double  ingreso;

    @ColumnInfo(name = "venta")
    double venta;
    @ColumnInfo(name = "saldo")
    double saldo;
    @ColumnInfo(name = "fisico")
    double fisico;
    @ColumnInfo(name = "diferencia")
    double diferencia;
    @ColumnInfo(name = "totalbs")
    double totalbs;

    public AlmacenEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductoId() {
        return ProductoId;
    }

    public void setProductoId(int productoId) {
        ProductoId = productoId;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String producto) {
        Producto = producto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getInicial() {
        return inicial;
    }

    public void setInicial(double inicial) {
        this.inicial = inicial;
    }

    public double getIngreso() {
        return ingreso;
    }

    public void setIngreso(double ingreso) {
        this.ingreso = ingreso;
    }

    public double getVenta() {
        return venta;
    }

    public void setVenta(double venta) {
        this.venta = venta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getFisico() {
        return fisico;
    }

    public void setFisico(double fisico) {
        this.fisico = fisico;
    }

    public double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

    public double getTotalbs() {
        return totalbs;
    }

    public void setTotalbs(double totalbs) {
        this.totalbs = totalbs;
    }
}
