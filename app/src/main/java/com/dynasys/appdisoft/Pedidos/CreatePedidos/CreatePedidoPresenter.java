package com.dynasys.appdisoft.Pedidos.CreatePedidos;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.Pedidos.Presentacion.PedidosMvp;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.common.internal.Preconditions;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CreatePedidoPresenter implements CreatePedidoMvp.Presenter {

    private final CreatePedidoMvp.View mPedidoView;
    private final Context mContext;
    private ClientesListViewModel viewModelClientes;
    private ProductosListViewModel viewModelProductos;
    private FragmentActivity activity;
    public CreatePedidoPresenter(CreatePedidoMvp.View  pedidosView, Context context, ClientesListViewModel viewModel,ProductosListViewModel viewModelProductos, FragmentActivity activity){
        mPedidoView = Preconditions.checkNotNull(pedidosView);
        mPedidoView.setPresenter(this);
        this.mContext=context;
        this.viewModelClientes=viewModel;
        this.viewModelProductos=viewModelProductos;
        this.activity=activity;
    }
    @Override
    public void CargarClientes() {
        try {
            List<ClienteEntity> listCliente=viewModelClientes.getMAllCliente(1);
            if (listCliente.size()>0){
                mPedidoView.MostrarClientes(listCliente);
            }
        } catch (ExecutionException e) {
           // e.printStackTrace();
        } catch (InterruptedException e) {
          //  e.printStackTrace();
        }

    }

    @Override
    public void CargarProducto(int idCLiente) {
        try {
           List<ProductoEntity>list= viewModelProductos.getProductoByCliente(idCLiente);
            mPedidoView.MostrarProductos(list);
        } catch (ExecutionException e) {
           // e.printStackTrace();
        } catch (InterruptedException e) {
           // e.printStackTrace();
        }
    }
}
