package com.dynasys.appdisoft.Pedidos.carrito;

import android.widget.TextView;


import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;

import java.util.List;

public interface ProductorMvp {

    interface View {
        void recyclerViewListClicked(android.view.View v, ProductoEntity empresa);
        void recyclerViewListClickedCategoria(android.view.View v, Categorias empresa, TextView tvCategoria) throws CloneNotSupportedException;
        void setPresenter(Presenter presenter);
        void ShowMessageResult(String message);
        void MostrarDatos(List<ProductoEntity> listEmpresa);
        void ShowSyncroMgs(String message);

    }
    interface Presenter{
        void GetDatos();
    }
}
