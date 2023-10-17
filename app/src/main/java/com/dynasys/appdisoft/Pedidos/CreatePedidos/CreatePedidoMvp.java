package com.dynasys.appdisoft.Pedidos.CreatePedidos;

import android.widget.EditText;
import android.widget.TextView;

import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClienteEntity;

import java.util.List;

public interface CreatePedidoMvp {

    interface View {

        void MostrarClientes(List<ClienteEntity> clientes);
        void MostrarProductos(List<ProductoEntity> productos);
        void setPresenter(Presenter presenter);
        void ModifyItem(int pos, String value, DetalleEntity item, TextView tvsubtotal, EditText eCantidad,EditText eCatidadCajas);
        void ModifyItemCaja(int pos, String value, DetalleEntity item, TextView tvsubtotal, EditText eCantidad,EditText eCatidadCajas);
        void ModifyItemPrecio(int pos, String value, DetalleEntity item, TextView tvsubtotal, EditText ePrecio);
        void ModifyItemDescuento(int pos, String value, DetalleEntity item, TextView tvsubtotal, EditText eDescuento);
        void DeleteAndModifyDetailOrder(DetalleEntity item,int pos);
        void ShowMessageResult(String message);
        void showSaveResultOption(int codigo, String id, String mensaje);
        void showDataDetail(List<DetalleEntity> listDetalle) ;

    }
    interface Presenter{
        void CargarClientes();
        void CargarProducto(int idCLiente,int VentaDirecta);
        void GuardarDatos(List<DetalleEntity> list,PedidoEntity pedido,ClienteEntity cli);
        void getDetailOrder(String numiOrder);
    }
}
