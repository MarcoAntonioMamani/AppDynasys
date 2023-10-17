package com.dynasys.appdisoft.Login.Cloud;

public class ResponseLogin {
   int code;
   String message;
   String token;
   int id;
    int zona;
    int mapa;
    int pedido;
    int update_cliente;
    int categoria;
    int stock;
    int view_credito;
    int cantidad_producto;
    int ValidarZona;
    int precio;
    int idConciliacion;
    String TipoNegocio;
    String CategoriasProducts;

    public ResponseLogin(int code, String message, String token, int id, int zona, int mapa, int pedido, int update_cliente, int categoria, int stock, int view_credito, int cantidad_producto, int validarZona, int precio,int idConciliacion,String TipoNegocio,String categorias) {
        this.code = code;
        this.message = message;
        this.token = token;
        this.id = id;
        this.zona = zona;
        this.mapa = mapa;
        this.pedido = pedido;
        this.update_cliente = update_cliente;
        this.categoria = categoria;
        this.stock = stock;
        this.view_credito = view_credito;
        this.cantidad_producto = cantidad_producto;
        ValidarZona = validarZona;
        this.precio = precio;
        this.idConciliacion=idConciliacion;
        this.TipoNegocio=TipoNegocio;
        this.CategoriasProducts=categorias;
    }

    public String getCategoriasProducts() {
        return CategoriasProducts;
    }

    public void setCategoriasProducts(String categoriasProducts) {
        CategoriasProducts = categoriasProducts;
    }

    public String getTipoNegocio() {
        return TipoNegocio;
    }

    public void setTipoNegocio(String tipoNegocio) {
        TipoNegocio = tipoNegocio;
    }

    public int getIdConciliacion() {
        return idConciliacion;
    }

    public void setIdConciliacion(int idConciliacion) {
        this.idConciliacion = idConciliacion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getValidarZona() {
        return ValidarZona;
    }

    public void setValidarZona(int validarZona) {
        ValidarZona = validarZona;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public int getMapa() {
        return mapa;
    }

    public void setMapa(int mapa) {
        this.mapa = mapa;
    }

    public int getPedido() {
        return pedido;
    }

    public void setPedido(int pedido) {
        this.pedido = pedido;
    }

    public int getUpdate_cliente() {
        return update_cliente;
    }

    public void setUpdate_cliente(int update_cliente) {
        this.update_cliente = update_cliente;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getView_credito() {
        return view_credito;
    }

    public void setView_credito(int view_credito) {
        this.view_credito = view_credito;
    }

    public int getCantidad_producto() {
        return cantidad_producto;
    }

    public void setCantidad_producto(int cantidad_producto) {
        this.cantidad_producto = cantidad_producto;
    }
}
