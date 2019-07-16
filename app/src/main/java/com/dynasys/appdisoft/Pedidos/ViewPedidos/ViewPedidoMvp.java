package com.dynasys.appdisoft.Pedidos.ViewPedidos;

import android.view.View;

import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;

import java.util.List;

public interface ViewPedidoMvp {

    interface View{
        void setPresenter(Presenter presenter);
        void showDataDetail(List<DetalleEntity> listDetalle) ;
        void recyclerViewListClicked(View v, int position);
    }
    interface Presenter{
        void getDetailOrder(String numiOrder);
    }
}
