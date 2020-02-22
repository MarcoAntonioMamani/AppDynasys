package com.dynasys.appdisoft.Pedidos.CreatePedidos;

import android.widget.EditText;
import android.widget.TextView;

import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Pedidos.Presentacion.PedidosMvp;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

import java.util.List;

public interface CreatePedidoMvp {

    interface View {

        void MostrarClientes(List<ClienteEntity> clientes);
        void MostrarProductos(List<ProductoEntity> productos);
        void setPresenter(Presenter presenter);
        void ModifyItem(int pos, String value, DetalleEntity item, TextView tvsubtotal, EditText eCantidad);
        void ModifyItemPrecio(int pos, String value, DetalleEntity item, TextView tvsubtotal, EditText ePrecio);
        void DeleteAndModifyDetailOrder(DetalleEntity item,int pos);
        void ShowMessageResult(String message);
        void showSaveResultOption(int codigo, String id, String mensaje);
        void showDataDetail(List<DetalleEntity> listDetalle) ;

    }
    interface Presenter{
        void CargarClientes();
        void CargarProducto(int idCLiente);
        void GuardarDatos(List<DetalleEntity> list,PedidoEntity pedido);
        void getDetailOrder(String numiOrder);
    }
}
