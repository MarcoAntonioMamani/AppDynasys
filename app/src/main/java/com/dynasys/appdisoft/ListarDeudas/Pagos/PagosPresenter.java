package com.dynasys.appdisoft.ListarDeudas.Pagos;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaDetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.ListViewModel.CobranzaDetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewModel.CobranzaListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewModel.DeudaListaViewModel;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.ShareUtil.ServiceSincronizacion;
import com.google.android.gms.common.internal.Preconditions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosPresenter implements PagosMvp.Presenter  {

    private final PagosMvp.View mPagosView;
    private final Context mContext;
    private FragmentActivity activity;
    private DeudaListaViewModel viewModelDeuda;
    private CobranzaListViewModel viewModelCobranza;
    private CobranzaDetalleListViewModel viewModelCobranzaDetalle;
    public PagosPresenter(PagosMvp.View  pagosView, Context context,DeudaListaViewModel mDeuda,  FragmentActivity activity,
                          CobranzaListViewModel cobranza,CobranzaDetalleListViewModel detalle){
        mPagosView = Preconditions.checkNotNull(pagosView);
        mPagosView.setPresenter(this);
        this.mContext=context;
        this.viewModelDeuda =mDeuda;
        this.viewModelCobranza=cobranza;
        this.viewModelCobranzaDetalle=detalle;
        this.activity=activity;
    }



    @Override
    public void CargarDeudas() {

       List<DeudaEntity> list=new ArrayList<>();
        list=FiltrarDeudas(viewModelDeuda.getMDeudaAllAsync());
        mPagosView.MostrarListadoPagos(list);
    }

    DeudaEntity ObtenerDeuda(int IdPedido ,List<DeudaEntity> list){

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPedidoId()==IdPedido){
                return list.get(i);
            }
        }
        return null;
    }

    @Override
    public void GuardarCobranza(final CobranzaRequest cobranza,List<DeudaEntity> listDeuda) {
        CobranzaEntity cob=new CobranzaEntity();
        cob.setEstado(cobranza.getEstado());
        cob.setFecha(cobranza.getFecha());
        cob.setIdPersonal(cobranza.getIdPersonal());
        cob.setObservacion(cobranza.getObservacion());
        cob.setTenumi(cobranza.getTenumi());
        viewModelCobranza.insertCobranza(cob);
        List<CobranzaDetalleEntity> list=cobranza.getListDetalle();

        for (int i = 0; i < list.size(); i++) {
            CobranzaDetalleEntity detalle=list.get(i);
            viewModelCobranzaDetalle.insertCobranzaDetalle(detalle);

        }

        for (int i = 0; i < list.size(); i++) {
            CobranzaDetalleEntity detalle=list.get(i);
            DeudaEntity mDeuda=ObtenerDeuda(detalle.getPedidoId(),listDeuda);
            if (mDeuda!=null){
                mDeuda.setTotalAPagar(0);
                mDeuda.setPendiente(mDeuda.getPendiente()-detalle.getMontoAPagar());
                viewModelDeuda.updateDeuda(mDeuda);
            }
        }


        if (ShareMethods.IsServiceRunning(mContext, ServiceSincronizacion.class)){
            UtilShare.mActivity=activity;
            Intent intent = new Intent(mContext,ServiceSincronizacion.getInstance().getClass());
            //mContext.stopService(intent);
            ServiceSincronizacion.getInstance().onDestroy();
        }
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.InsertCobranza(cobranza, new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin responseUser = response.body();
                if (response.code()==404 || response.code()==500){
                    mPagosView.showSaveResultOption(0,"","");
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
                            CobranzaEntity mPedido= viewModelCobranza.getCobranza(cobranza.getTenumi());
                            if (mPedido!=null){
                                mPedido.setTenumi(responseUser.getToken());
                                mPedido.setEstado(1);
                                mPedido.setObservacion(responseUser.getToken());
                                List<CobranzaDetalleEntity> listDetalle= viewModelCobranzaDetalle.getCobranzaDetalle(cobranza.getTenumi());
                                if (listDetalle!=null) {
                                    for (int i = 0; i < listDetalle.size(); i++) {
                                        CobranzaDetalleEntity item = listDetalle.get(i);
                                        item.setCobranzaId(responseUser.getToken());
                                        item.setEstado(1);
                                        viewModelCobranzaDetalle.updateCobranzaDetalle(item);

                                    }
                                    viewModelCobranza.updateCobranza(mPedido);
                                    mPagosView.showSaveResultOption(1,""+responseUser.getToken(),"");
                                }else{

                                    mPagosView.showSaveResultOption(0,"","");
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

                            mPagosView.showSaveResultOption(0,"",responseUser.getMessage());
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
                    mPagosView.showSaveResultOption(0,"","");
                    return;
                }

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                mPagosView.showSaveResultOption(0,"","");
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

    public List<DeudaEntity> FiltrarDeudas(List<DeudaEntity> lis){
        List<DeudaEntity> listNew=new ArrayList<>();
        int idCliente= UtilShare.IdCLiente;
        for (int i = 0; i < lis.size(); i++) {
            if (lis.get(i).getClienteId()==idCliente){
                listNew.add(lis.get(i));
            }

        }
        return listNew;

    }



}
