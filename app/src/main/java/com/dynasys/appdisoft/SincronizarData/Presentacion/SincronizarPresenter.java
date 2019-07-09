package com.dynasys.appdisoft.SincronizarData.Presentacion;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;

import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.Bodylogin;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.common.internal.Preconditions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SincronizarPresenter implements SincronizarMvp.Presenter {
    private final SincronizarMvp.View mSincronizarview;
    private final Context mContext;
    private final ClientesListViewModel viewModel;
    private final Activity activity;
    public SincronizarPresenter(SincronizarMvp.View sincronizarView,Context context,ClientesListViewModel viewModel,Activity activity){
        mSincronizarview = Preconditions.checkNotNull(sincronizarView);
        mSincronizarview.setPresenter(this);
        this.viewModel=viewModel;
        this.mContext=context;
        this.activity=activity;
    }
    @Override
    public void GuadarDatos() {
        ApiManager apiManager=ApiManager.getInstance();
        apiManager.ObtenerClientes( new Callback<List<ClienteEntity>>() {
            @Override
            public void onResponse(Call<List<ClienteEntity>> call, Response<List<ClienteEntity>> response) {
                final List<ClienteEntity> responseUser = (List<ClienteEntity>) response.body();
                if (response.code()==404){
                    mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {

                    viewModel.getClientes().observe((LifecycleOwner) activity, new Observer<List<ClienteEntity>>() {
                        @Override
                        public void onChanged(@Nullable List<ClienteEntity> notes) {
                            List<ClienteEntity> listCliente = notes;
                            if (listCliente.size() <= 0) {
                                for (int i = 0; i < responseUser.size(); i++) {
                                    ClienteEntity cliente = responseUser.get(i);
                                    viewModel.insertCliente(cliente);
                                }

                                mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Clientes");
                            }else{
                                mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Clientes");
                            }
                        }  });



                } else {
                    mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor");
                }
            }

            @Override
            public void onFailure(Call<List<ClienteEntity>> call, Throwable t) {
                mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio.");
            }
        });
    }
}
