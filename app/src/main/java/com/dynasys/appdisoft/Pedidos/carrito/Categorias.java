package com.dynasys.appdisoft.Pedidos.carrito;

public class Categorias implements Cloneable {

    int id;
    String NombreCategoria;
    public Categorias clone() throws CloneNotSupportedException {
        return (Categorias) super.clone();
    }


    public Categorias(int id, String nombreCategoria) {
        this.id = id;
        NombreCategoria = nombreCategoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCategoria() {
        return NombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        NombreCategoria = nombreCategoria;
    }


}
