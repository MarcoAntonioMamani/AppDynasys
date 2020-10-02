package com.dynasys.appdisoft.ListarDeudas.Pagos;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.ListViewModel.DeudaListaViewModel;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.google.android.gms.common.internal.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class PagosPresenter implements PagosMvp.Presenter  {

    private final PagosMvp.View mPagosView;
    private final Context mContext;
    private FragmentActivity activity;
    private DeudaListaViewModel viewModelDeuda;

    public PagosPresenter(PagosMvp.View  pagosView, Context context,DeudaListaViewModel mDeuda,  FragmentActivity activity){
        mPagosView = Preconditions.checkNotNull(pagosView);
        mPagosView.setPresenter(this);
        this.mContext=context;
        this.viewModelDeuda =mDeuda;
        this.activity=activity;
    }



    @Override
    public void CargarDeudas() {

       List<DeudaEntity> list=new ArrayList<>();
        list=FiltrarDeudas(viewModelDeuda.getMDeudaAllAsync());
        mPagosView.MostrarListadoPagos(list);
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
