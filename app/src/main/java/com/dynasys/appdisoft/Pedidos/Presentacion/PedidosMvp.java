package com.dynasys.appdisoft.Pedidos.Presentacion;

import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.LoginMvp;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

import java.util.List;

public interface PedidosMvp {
    interface View {
        void recyclerViewListClicked(android.view.View v, PedidoEntity pedido);
        void MostrarPedidos(List<PedidoEntity> clientes);
        void setPresenter(Presenter presenter);

    }
    interface Presenter{
        void CargarPedidos();
    }
}
