package com.dynasys.appdisoft.Mapas;

import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

public class ClientePedidos {

    ClienteEntity cliente;
    int EstadoPedido ;
    int Anulado ;

    public ClientePedidos(ClienteEntity cliente, int estadoPedido, int anulado) {
        this.cliente = cliente;
        EstadoPedido = estadoPedido;
        Anulado = anulado;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public int getEstadoPedido() {
        return EstadoPedido;
    }

    public void setEstadoPedido(int estadoPedido) {
        EstadoPedido = estadoPedido;
    }

    public int getAnulado() {
        return Anulado;
    }

    public void setAnulado(int anulado) {
        Anulado = anulado;
    }
}
