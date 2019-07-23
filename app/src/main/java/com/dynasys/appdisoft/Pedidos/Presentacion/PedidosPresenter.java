package com.dynasys.appdisoft.Pedidos.Presentacion;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.common.internal.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PedidosPresenter implements PedidosMvp.Presenter {

    private final PedidosMvp.View mPedidosView;
    private final Context mContext;
    private PedidoListViewModel viewModel;
    private FragmentActivity activity;
    int Estado;
    public PedidosPresenter(PedidosMvp.View pedidosView, Context context, PedidoListViewModel viewModel, FragmentActivity activity,int Estado){
        mPedidosView = Preconditions.checkNotNull(pedidosView);
        mPedidosView.setPresenter(this);
        this.mContext=context;
        this.Estado=Estado;
        this.viewModel=viewModel;
        this.activity=activity;
    }
    @Override
    public void CargarPedidos() {
        viewModel = ViewModelProviders.of(activity).get(PedidoListViewModel.class);

            viewModel.getPedidos().observe((LifecycleOwner) activity, new Observer<List<PedidoEntity>>() {
                @Override
                public void onChanged(@Nullable List<PedidoEntity> notes) {
                    try{
                        if (notes.size()>0){

                            mPedidosView.MostrarPedidos(FiltarByZona(notes));
                        }
                    }catch(Exception e){

                    }



                }  });


    }
    public List<PedidoEntity> FiltarByZona(List<PedidoEntity> list){

        int idRepartidor= DataPreferences.getPrefInt("idrepartidor",mContext);
        List<PedidoEntity> listClie=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            PedidoEntity cliente=list.get(i);
            if (cliente.getOarepa() ==idRepartidor && cliente.getOaest()==Estado && Estado==3){
                listClie.add(cliente);
            }else{
                if ( (cliente.getOarepa() ==idRepartidor && cliente.getOaest()!=3 && Estado!=3)){
                    listClie.add(cliente);
                }

            }
        }
        return listClie;
    }
}
