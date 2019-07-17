package com.dynasys.appdisoft.Pedidos.CreatePedidos;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DB.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.Pedidos.Presentacion.PedidosMvp;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.common.internal.Preconditions;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePedidoPresenter implements CreatePedidoMvp.Presenter {

    private final CreatePedidoMvp.View mPedidoView;
    private final Context mContext;
    private ClientesListViewModel viewModelClientes;
    private ProductosListViewModel viewModelProductos;
    private PedidoListViewModel viewModelPedidos;
    private DetalleListViewModel viewModelDetalle;
    private FragmentActivity activity;
    public CreatePedidoPresenter(CreatePedidoMvp.View  pedidosView, Context context, ClientesListViewModel viewModel,ProductosListViewModel viewModelProductos, FragmentActivity activity,
                                 PedidoListViewModel mviewPedidos,DetalleListViewModel mviewDetalle){
        mPedidoView = Preconditions.checkNotNull(pedidosView);
        mPedidoView.setPresenter(this);
        this.mContext=context;
        this.viewModelClientes=viewModel;
        this.viewModelPedidos=mviewPedidos;
        this.viewModelProductos=viewModelProductos;
        this.viewModelDetalle=mviewDetalle;
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

    @Override
    public void GuardarDatos(final List<DetalleEntity> list, final PedidoEntity pedido) {


        viewModelPedidos.insertPedido(pedido);
        for (int i = 0; i < list.size(); i++) {
            DetalleEntity item=list.get(i);
            viewModelDetalle.insertDetalle(item);
        }
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.InsertPedido(pedido, new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin responseUser = response.body();
                if (response.code()==404){
                   mPedidoView.showSaveResultOption(0,"","");
                    return;
                }
                try{
                    if (responseUser!=null){
                        if (responseUser.getCode()==0){
                            PedidoEntity mPedido= viewModelPedidos.getPedido(pedido.getOanumi());
                            if (mPedido!=null){
                                mPedido.setOanumi(responseUser.getToken());
                                mPedido.setEstado(true);
                                mPedido.setCodigogenerado(responseUser.getToken());
                                viewModelPedidos.updatePedido(mPedido);
                                InsertarDetalleServicio(responseUser.getToken(),list,pedido);
                                //showSaveResultOption(1,""+mcliente.getNumi(),"");
                                return;
                            }
                        }
                    }
                }catch (Exception e){
                    mPedidoView.showSaveResultOption(0,"","");
                    return;
                }

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                mPedidoView.showSaveResultOption(0,"","");
                return;
                //ShowMessageResult("Error al guardar el pedido");
            }
        });
    }

    public void InsertarDetalleServicio(final String Oanumi, List<DetalleEntity> list, final PedidoEntity pedido){
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.InsertDetalle(list,Oanumi, new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin responseUser = response.body();
                if (response.code()==404){
                    mPedidoView.showSaveResultOption(0,"","");
                    return;
                }
                try{
                    if (responseUser!=null){
                        if (responseUser.getCode()==1){
                            List<DetalleEntity> listDetalle= viewModelDetalle.getDetalle(pedido.getCodigogenerado());
                            if (listDetalle!=null){
                                for (int i = 0; i < listDetalle.size(); i++) {
                                    DetalleEntity item=listDetalle.get(i);
                                    item.setObnumi(Oanumi);
                                    item.setEstado(true);
                                    viewModelDetalle.updateDetalle(item);
                                }
                                mPedidoView.showSaveResultOption(1,""+Oanumi,"");
                                return;
                            }
                        }
                    }
                }catch (Exception e){
                    mPedidoView.showSaveResultOption(0,"","");
                    return;
                }

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                mPedidoView.showSaveResultOption(0,"","");
                //ShowMessageResult("Error al guardar el pedido");
            }
        });
    }
}
