package com.dynasys.appdisoft.Pedidos.carrito;

public class Categorias implements Cloneable {

    int id;
    String NombreCategoria;
    int Estado;
    public Categorias clone() throws CloneNotSupportedException {
        return (Categorias) super.clone();
    }
    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public Categorias(int id, String nombreCategoria, int estado) {
        this.id = id;
        NombreCategoria = nombreCategoria;
        Estado = estado;
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
