package com.dynasys.appdisoft.Pedidos.CreatePedidos;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DB.Entity.VisitaEntity;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoDetalle;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.StockListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.VisitaListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.ShareUtil.LocationGeo;
import com.dynasys.appdisoft.ShareUtil.ServiceSincronizacion;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.common.internal.Preconditions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePedidoPresenter implements CreatePedidoMvp.Presenter {
    private static final String TAG = "Tracking";
    private final CreatePedidoMvp.View mPedidoView;
    private final Context mContext;
    private ClientesListViewModel viewModelClientes;
    private ProductosListViewModel viewModelProductos;
    private PedidoListViewModel viewModelPedidos;
    private DetalleListViewModel viewModelDetalle;
    private StockListViewModel viewModelStock;
    private VisitaListViewModel viewModelVisita;
    private FragmentActivity activity;
    public CreatePedidoPresenter(CreatePedidoMvp.View  pedidosView, Context context, ClientesListViewModel viewModel,ProductosListViewModel viewModelProductos, FragmentActivity activity,
                                 PedidoListViewModel mviewPedidos,DetalleListViewModel mviewDetalle,StockListViewModel mviewStock,
                                 VisitaListViewModel viewModelVisita){
        mPedidoView = Preconditions.checkNotNull(pedidosView);
        mPedidoView.setPresenter(this);
        this.mContext=context;
        this.viewModelClientes=viewModel;
        this.viewModelPedidos=mviewPedidos;
        this.viewModelProductos=viewModelProductos;
        this.viewModelDetalle=mviewDetalle;
        this.viewModelStock=mviewStock;
        this.viewModelVisita=viewModelVisita;
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
    public void CargarProducto(int idCLiente,int VentaDirecta) {
        try {


           List<ProductoEntity>list;

           if (VentaDirecta==0){  //Quiere decir que es solo pedido normal y debe mostrar el stock general
               list= viewModelProductos.getProductoByClienteVentaDirecta(idCLiente);

           }else{
               list= viewModelProductos.getProductoByCliente(idCLiente);
           }


           mPedidoView.MostrarProductos(list);
        } catch (ExecutionException e) {
           // e.printStackTrace();
        } catch (InterruptedException e) {
           // e.printStackTrace();
        }
    }


    public void GuardarVisita(PedidoEntity ped,ClienteEntity cli){

        try {
            List<VisitaEntity> list= viewModelVisita.getVisitabyCliente(cli.getCodigogenerado());
            boolean banderaExists=false;
            for (int i = 0; i < list.size(); i++) {

                if (list.get(i).getFecha().before(ped.getOafdoc())){

                    VisitaEntity vi=list.get(i);
                    if (vi.getPedidoId().length()<=0){
                        vi.setPedidoId(ped.getOanumi());
                        viewModelVisita.updateVisita(vi);
                    }


                    banderaExists=true;
                }
            }
            if (banderaExists==false){  // quiere decir que no existe visita para el cliente
                VisitaEntity cliente=new VisitaEntity();
                int codigoRepartidor=  ped.getOarepa();
                //cliente.setCodigogenerado();
                DateFormat df = new SimpleDateFormat("dMMyyyy,HH:mm:ss");
                String code = df.format(Calendar.getInstance().getTime());
                Calendar c2 = Calendar.getInstance();
                final int hora = c2.get(Calendar.HOUR);
                final int minuto = c2.get(Calendar.MINUTE);
                code=""+codigoRepartidor+","+code;
                cliente.setIdSincronizacion(code+"VA1.1");
                cliente.setId(0);
                cliente.setFecha(Calendar.getInstance().getTime());
                cliente.setHora(""+hora+":"+minuto);

                cliente.setDescripcion("");
                cliente.setNombreCliente(cli.getNamecliente());

                cliente.setDireccion(cli.getDireccion());
                cliente.setTelefono(cli.getTelefono());


                cliente.setRepartidorId(codigoRepartidor);
                cliente.setPedidoId(""+ped.getCodigogenerado());
                cliente.setClienteId(cli.getCodigogenerado());
                cliente.setEstado(1);//solo para saber estado de la visita
                cliente.setSincronizado(0);
                cliente.setLatitud((LocationGeo.getLocationActual())==null? 0:LocationGeo.getLocationActual().getLatitude());
                cliente.setLongitud((LocationGeo.getLocationActual())==null? 0:LocationGeo.getLocationActual().getLongitude());

              viewModelVisita.insertVisita(cliente);

            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void GuardarDatos(final List<DetalleEntity> list, final PedidoEntity pedido,final ClienteEntity cli) {


        viewModelPedidos.insertPedido(pedido);
        for (int i = 0; i < list.size(); i++) {
            DetalleEntity item=list.get(i);
            viewModelDetalle.insertDetalle(item);



            try{
                List<StockEntity> listStock = viewModelStock.getMStockAllAsync();
                if (listStock.size()>0){
                    StockEntity  st = viewModelStock.getStock(item.getObcprod());
                    if (st!=null){
                        st.setCantidad(st.getCantidad()-item.getObpcant());
                        viewModelStock.updateStock(st);
                    }
                }

            }catch (Exception e){

                Log.d(TAG, "respuesta: "+e.getMessage());
            }




        }

        if (ShareMethods.IsServiceRunning(mContext, ServiceSincronizacion.class)){
            UtilShare.mActivity=activity;
            Intent intent = new Intent(mContext,ServiceSincronizacion.getInstance().getClass());
            //mContext.stopService(intent);
            ServiceSincronizacion.getInstance().onDestroy();
        }
        ApiManager apiManager=ApiManager.getInstance(mContext);

        final String CodeGenerado=pedido.getCodigogenerado();
        List<DetalleEntity> Detalle= null;
        try {
            Detalle = viewModelDetalle.getDetalle(CodeGenerado);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final PedidoDetalle p= new PedidoDetalle();
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
        p.setVentaDirecta(pedido.getVentaDirecta());
        p.setOaest(pedido.getOaest());
        p.setOaobs(pedido.getOaobs());
        p.setLatitud(pedido.getLatitud());
        p.setLongitud(pedido.getLongitud());
        p.setTotal(pedido.getTotal());
        p.setTipocobro(pedido.getTipocobro());
        p.setTotalcredito(pedido.getTotalcredito());
        p.setEstado(pedido.getEstado());
        p.setEstadoUpdate(pedido.getEstadoUpdate());
        p.setReclamo(pedido.getReclamo());
        apiManager.InsertPedido(p, new Callback<ResponseLogin>() {
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
                                mPedido .setEstadoStock(1);
                                mPedido.setCodigogenerado(responseUser.getToken());
                                GuardarVisita(mPedido,cli );
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
                                    mPedidoView.showSaveResultOption(1,""+responseUser.getToken(),"");
                                }else{
                                    if (!ShareMethods.IsServiceRunning(mContext,ServiceSincronizacion.class)){
                                        UtilShare.mActivity=activity;
                                        Intent intent = new Intent(mContext,ServiceSincronizacion.getInstance().getClass());
                                        mContext.startService(intent);
                                    }

                                    mPedidoView.showSaveResultOption(0,"","");
                                }


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
                    GuardarVisita(pedido,cli );
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
                GuardarVisita(pedido,cli );
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

}
