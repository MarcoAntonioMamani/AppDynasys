package com.dynasys.appdisoft.Login.DB.Entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ProductoView")
public class ProductoViewEntity {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "ProductoId")
    int productoId;

    @ColumnInfo(name = "nombreProducto")
    String nombreProducto;

    @ColumnInfo(name = "cantInicial")
    double cantInicial;
    @ColumnInfo(name = "cantFinal")
    double cantFinal;
    @ColumnInfo(name = "precio")
    double precio;

    @ColumnInfo(name = "stock")
    double stock;

    @ColumnInfo(name = "entrada")
    double entrada;

    @ColumnInfo(name = "xentrada")
    double xentrada;

    @ColumnInfo(name = "rebote")
    double rebote;

    @ColumnInfo(name = "aut")
    double aut;

    @ColumnInfo(name = "saldo")
    double saldo;

    public ProductoViewEntity(int id, int productoId, String nombreProducto, double cantInicial, double cantFinal, double precio, double stock, double entrada, double xentrada, double rebote, double aut, double saldo) {
        this.id = id;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantInicial = cantInicial;
        this.cantFinal = cantFinal;
        this.precio = precio;
        this.stock = stock;
        this.entrada = entrada;
        this.xentrada = xentrada;
        this.rebote = rebote;
        this.aut = aut;
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getCantInicial() {
        return cantInicial;
    }

    public void setCantInicial(double cantInicial) {
        this.cantInicial = cantInicial;
    }

    public double getCantFinal() {
        return cantFinal;
    }

    public void setCantFinal(double cantFinal) {
        this.cantFinal = cantFinal;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getEntrada() {
        return entrada;
    }

    public void setEntrada(double entrada) {
        this.entrada = entrada;
    }

    public double getXentrada() {
        return xentrada;
    }

    public void setXentrada(double xentrada) {
        this.xentrada = xentrada;
    }

    public double getRebote() {
        return rebote;
    }

    public void setRebote(double rebote) {
        this.rebote = rebote;
    }

    public double getAut() {
        return aut;
    }

    public void setAut(double aut) {
        this.aut = aut;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
