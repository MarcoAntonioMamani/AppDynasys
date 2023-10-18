package com.dynasys.appdisoft.Login.DB.Entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
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
    @ColumnInfo(name = "idProveedor")
    int idProveedor;
    @ColumnInfo(name = "Proveedor")
    String Proveedor;
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

    @ColumnInfo(name = "conversion")
    double conversion;

    @Ignore
    double totalUnitario;
    @Ignore
    double caja;
    public ProductoEntity(){

    }

    public ProductoEntity(int numi, String cod, String producto, int idProveedor, String proveedor, int idcategoria, String categoria, double precio, double stock, int familia, double conversion, double totalUnitario, double caja) {
        this.numi = numi;
        this.cod = cod;
        this.producto = producto;
        this.idProveedor = idProveedor;
        Proveedor = proveedor;
        this.idcategoria = idcategoria;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.familia = familia;
        this.conversion = conversion;
        this.totalUnitario = totalUnitario;
        this.caja = caja;
    }

    public double getTotalUnitario() {
        return totalUnitario;
    }

    public void setTotalUnitario(double totalUnitario) {
        this.totalUnitario = totalUnitario;
    }

    public double getCaja() {
        return caja;
    }

    public void setCaja(double caja) {
        this.caja = caja;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getProveedor() {
        return Proveedor;
    }

    public void setProveedor(String proveedor) {
        Proveedor = proveedor;
    }

    public int getFamilia() {
        return familia;
    }

    public void setFamilia(int familia) {
        this.familia = familia;
    }

    public int getNumi() {
        return numi;
    }

    public double getConversion() {
        return conversion;
    }

    public void setConversion(double conversion) {
        this.conversion = conversion;
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

    public ProductoEntity clone()  {
        try {
            return (ProductoEntity) super.clone();
        } catch (CloneNotSupportedException e) {
           return null;
        }
    }
}
