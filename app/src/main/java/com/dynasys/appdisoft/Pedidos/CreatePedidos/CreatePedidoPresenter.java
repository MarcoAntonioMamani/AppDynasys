package com.dynasys.appdisoft.Pedidos.CreatePedidos;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DB.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoDetallle;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.ShareUtil.ServiceSincronizacion;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.common.internal.Preconditions;

import java.util.ArrayList;
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
            //List<ClienteEntity> listCliente=FiltarByZona(viewModelClientes.getMAllCliente(1));
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
    public List<ClienteEntity> FiltarByZona(List<ClienteEntity> list){

        int idzona= DataPreferences.getPrefInt("zona",mContext);
        List<ClienteEntity> listClie=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ClienteEntity cliente=list.get(i);
            if (cliente.getCczona() ==idzona){
                listClie.add(cliente);
            }
        }
        return listClie;
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

        if (ShareMethods.IsServiceRunning(mContext, ServiceSincronizacion.class)){
            UtilShare.mActivity=activity;
            Intent intent = new Intent(mContext,ServiceSincronizacion.getInstance().getClass());
            //mContext.stopService(intent);
            ServiceSincronizacion.getInstance().onDestroy();
        }

        final String CodeGenerado=pedido.getCodigogenerado();
        List<DetalleEntity> Detalle= null;
        try {
            Detalle = viewModelDetalle.getDetalle(CodeGenerado);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final PedidoDetallle p= new PedidoDetallle();
        p.setCliente(pedido.getCliente());
        p.setCodigogenerado(pedido.getCodigogenerado());
        p.setDetalle(Detalle);
        p.setEstado(pedido.getEstado());
        p.setId(pedido.getId());
        p.setOanumi(pedido.getOanumi());
        p.setOafdoc(pedido.getOafdoc());
        p.setOahora(pedido.getOahora());
        p.setOaccli(pedido.getOaccli());
        p.setOarepa(pedido.getOarepa());
        p.setOaest(pedido.getOaest());
        p.setOaobs(pedido.getOaobs());
        p.setLatitud(pedido.getLatitud());
        p.setLongitud(pedido.getLongitud());
        p.setTotal(pedido.getTotal());
        p.setTipocobro(pedido.getTipocobro());
        p.setTotalcredito(pedido.getTotalcredito());
        p.setEstado(pedido.getEstado());
        p.setEstadoUpdate(pedido.getEstadoUpdate());
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.InsertPedidoConDetalle(p, new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin responseUser = response.body();
                if (response.code()==404 || response.code()==500){
                   mPedidoView.showSaveResultOption(0,"","");
                    if (!ShareMethods.IsServiceRunning(mContext,ServiceSincronizacion.class)){
                        UtilShare.mActivity=activity;
                        Intent intent = new Intent(mContext,ServiceSincronizacion.getInstance().getClass());

                        mContext.startService(intent);
                    }
                    return;
                }
                try{
                    if (responseUser!=null){
                        if (responseUser.getCode()==0){
                            PedidoEntity mPedido= viewModelPedidos.getPedido(pedido.getOanumi());
                            if (mPedido!=null){
                                mPedido.setOanumi(responseUser.getToken());
                                mPedido.setEstado(1);
                                mPedido.setEstadoUpdate(1);
                                mPedido.setCodigogenerado(responseUser.getToken());
                                List<DetalleEntity> listDetalle= viewModelDetalle.getDetalle(CodeGenerado);
                                if (listDetalle!=null) {
                                    for (int i = 0; i < listDetalle.size(); i++) {
                                        DetalleEntity item = listDetalle.get(i);
                                        item.setObnumi(responseUser.getToken());
                                        item.setEstado(true);
                                        item.setObupdate(1);
                                        viewModelDetalle.updateDetalle(item);

                                    }
                                    viewModelPedidos.updatePedido(mPedido);
                                }
                                viewModelPedidos.updatePedido(mPedido);
                                mPedidoView.showSaveResultOption(1,""+responseUser.getToken(),"");
                                if (!ShareMethods.IsServiceRunning(mContext,ServiceSincronizacion.class)){
                                    UtilShare.mActivity=activity;
                                    Intent intent = new Intent(mContext,ServiceSincronizacion.getInstance().getClass());
                                    mContext.startService(intent);
                                }
                                //showSaveResultOption(1,""+mcliente.getNumi(),"");
                                return;
                            }
                        }else{

                            mPedidoView.showSaveResultOption(0,"",responseUser.getMessage());
                            if (!ShareMethods.IsServiceRunning(mContext,ServiceSincronizacion.class)){
                                UtilShare.mActivity=activity;
                                Intent intent = new Intent(mContext,ServiceSincronizacion.getInstance().getClass());
                                mContext.startService(intent);
                            }
                            //showSaveResultOption(1,""+mcliente.getNumi(),"");
                            return;
                        }
                    }
                }catch (Exception e){
                    if (!ShareMethods.IsServiceRunning(mContext,ServiceSincronizacion.class)){
                        UtilShare.mActivity=activity;
                        Intent intent = new Intent(mContext,ServiceSincronizacion.getInstance().getClass());
                        mContext.startService(intent);
                    }
                    mPedidoView.showSaveResultOption(0,"","");
                    return;
                }

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                mPedidoView.showSaveResultOption(0,"","");
                if (!ShareMethods.IsServiceRunning(mContext,ServiceSincronizacion.class)){
                    UtilShare.mActivity=activity;
                    Intent intent = new Intent(mContext,ServiceSincronizacion.getInstance().getClass());
                    mContext.startService(intent);
                }
                return;
                //ShowMessageResult("Error al guardar el pedido");
            }
        });
    }

    @Override
    public void getDetailOrder(String numiOrder) {
        try {
            List<DetalleEntity> listDetalle= viewModelDetalle.getDetalle(numiOrder);
            if (listDetalle.size()>0){
                mPedidoView.showDataDetail(listDetalle);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void InsertarDetalleServicio(final String Oanumi, List<DetalleEntity> list, final PedidoEntity pedido){
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.InsertDetalle(list,Oanumi, new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin responseUser = response.body();
                if (response.code()==404 || response.code()==500){
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
                                    item.setObupdate(1);
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
