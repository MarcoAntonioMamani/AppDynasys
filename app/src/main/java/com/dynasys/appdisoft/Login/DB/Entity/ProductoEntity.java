package com.dynasys.appdisoft.Login.DB.Entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "producto")
public class ProductoEntity  implements Cloneable{
    @PrimaryKey()
    @ColumnInfo(name = "numi")
    int numi;
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
    @ColumnInfo(name = "precio")
    double precio;
    @ColumnInfo(name = "stock")
    double stock;
    @ColumnInfo(name = "familia")
    int familia;
    @ColumnInfo(name = "PrecioMinimo")
    double PrecioMinimo;
    @ColumnInfo(name = "PrecioMaximo")
    double PrecioMaximo;
public ProductoEntity(){

}
    public ProductoEntity(int numi, String cod, String producto, String desccorta, int idcategoria, String categoria, double precio, double stock, int familia, double precioMinimo, double precioMaximo) {
        this.numi = numi;
        this.cod = cod;
        this.producto = producto;
        this.desccorta = desccorta;
        this.idcategoria = idcategoria;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.familia = familia;
        PrecioMinimo = precioMinimo;
        PrecioMaximo = precioMaximo;
    }

    public int getNumi() {
        return numi;
    }

    public void setNumi(int numi) {
        this.numi = numi;
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

    public int getFamilia() {
        return familia;
    }

    public void setFamilia(int familia) {
        this.familia = familia;
    }

    public double getPrecioMinimo() {
        return PrecioMinimo;
    }

    public void setPrecioMinimo(double precioMinimo) {
        PrecioMinimo = precioMinimo;
    }

    public double getPrecioMaximo() {
        return PrecioMaximo;
    }

    public void setPrecioMaximo(double precioMaximo) {
        PrecioMaximo = precioMaximo;
    }

    public ProductoEntity clone() throws CloneNotSupportedException {
        return (ProductoEntity) super.clone();
    }
}
