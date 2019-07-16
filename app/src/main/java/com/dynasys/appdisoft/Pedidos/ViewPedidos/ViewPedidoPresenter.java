package com.dynasys.appdisoft.Pedidos.ViewPedidos;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.dynasys.appdisoft.Login.DB.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.Pedidos.CreatePedidos.CreatePedidoMvp;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.common.internal.Preconditions;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewPedidoPresenter implements ViewPedidoMvp.Presenter {

    private final ViewPedidoMvp.View mDetailView;
    private final Context mContext;
    private DetalleListViewModel viewModelDetalle;
    private FragmentActivity activity;

    public ViewPedidoPresenter(ViewPedidoMvp.View  pedidosView, Context context,  DetalleListViewModel viewModelDetalle, FragmentActivity activity){
        mDetailView = Preconditions.checkNotNull(pedidosView);
        mDetailView.setPresenter(this);
        this.mContext=context;
        this.viewModelDetalle=viewModelDetalle;
        this.activity=activity;
    }
    @Override
    public void getDetailOrder(String numiOrder) {
        try {
         List<DetalleEntity> listDetalle= viewModelDetalle.getDetalle(numiOrder);
         if (listDetalle.size()>0){
             mDetailView.showDataDetail(listDetalle);
         }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
