package com.dynasys.appdisoft.Pedidos.carrito;

import android.widget.EditText;
import android.widget.TextView;


import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;

import java.util.List;

public interface ProductorMvp {

    interface View {
        void recyclerViewListClicked(android.view.View v, ProductoEntity empresa);
        void recyclerViewListClickedCategoria(android.view.View v, Categorias empresa, TextView tvCategoria) throws CloneNotSupportedException;
        void recyclerViewListClickedProducto(android.view.View v, ProductoEntity producto) ;
        void setPresenter(Presenter presenter);
        void ShowMessageResult(String message);
        void ModifyItemCart( String value, ProductoEntity item, EditText eCantidad,EditText eCatidadCajas);
        void ModifyItemCajaCart( String value, ProductoEntity item, EditText eCantidad,EditText eCatidadCajas);
        void MostrarDatos(List<ProductoEntity> listEmpresa);
        void ShowSyncroMgs(String message);

    }
    interface Presenter{
        void GetDatos();
    }
}
