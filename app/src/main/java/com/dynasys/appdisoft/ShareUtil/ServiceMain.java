package com.dynasys.appdisoft.ShareUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClientesListViewModel;

public class ServiceMain extends BroadcastReceiver {
    private Context mContext;
    private  ClientesListViewModel viewModelClientes;
    public static ServiceSincronizacion mInstance;
    FragmentActivity activity;

    public ServiceMain(ClientesListViewModel mviewCliente, FragmentActivity f){
        this.viewModelClientes=mviewCliente;
        activity=f;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context ,"Inicio Servico exportacion...", Toast.LENGTH_LONG).show();
        try {
            boolean bolService = ShareMethods.IsServiceRunning(context,ServiceSincronizacion.class);
            if (bolService){
                context.stopService(new Intent(context,new ServiceSincronizacion(viewModelClientes,activity).getClass()));
                context.startService(new Intent(context,new ServiceSincronizacion(viewModelClientes,activity).getClass()));
            }else{
                context.startService(new Intent(context,new ServiceSincronizacion(viewModelClientes,activity).getClass()));
            }

        }catch (Exception e){
            Toast.makeText(context ,"Error Inicio Servicio:"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
