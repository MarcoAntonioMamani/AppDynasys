package com.dynasys.appdisoft.ShareUtil;

import android.app.NotificationManager;
import android.app.Service;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceSincronizacion extends Service {
    int counter = 0;
    static final int UPDATE_INTERVAL = 120000;//120000;//3 min
    private Timer timer = new Timer();
    private static final String TAG = "ServiceMov->";
    NotificationManager notManager;
    static final int ID_NOTIFICACION=2905;
    private Stopwatch stopwatch = Stopwatch.createStarted();
    private Context mContext;
    private  ClientesListViewModel viewModelClientes;
    public static ServiceSincronizacion mInstance;
    public Runnable runnable = null;
    FragmentActivity activity;
    public ServiceSincronizacion(){

    }
    public ServiceSincronizacion(ClientesListViewModel mviewCliente,FragmentActivity f){
        this.viewModelClientes=mviewCliente;
        activity=f;

    }
    public static ServiceSincronizacion getInstance() {
        return mInstance;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
       return  null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = getApplicationContext();
      //  iRepositoryMovimiento = RepositoryMovimiento.getInstance(mContext,
        mInstance = this;
        this.mContext = getApplicationContext();
if (UtilShare.mActivity!=null){

    viewModelClientes = ViewModelProviders.of(UtilShare.mActivity).get(ClientesListViewModel.class);
}

    }
    private  boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("Service-->", "Antes Online");
            if (isOnline()){
                Log.i("Service-->", "Inicio de servicio Sincronizacion Datos");
                exportarMovimiento();
            }

            }
        };
        handler.postDelayed(runnable,1 * 1000);
        return START_STICKY;
    }

    private void exportarMovimiento(){
//        Looper.prepare();
        try {
            if (viewModelClientes==null){
                return;
            }
            List<ClienteEntity> listCliente = viewModelClientes.getMAllStateCliente(1);
            if (listCliente==null){
                return;
            }
            if (listCliente.size()>0){

                for (int i = 0; i < listCliente.size(); i++) {

                    ClienteEntity cliente=listCliente.get(i);
                    final String CodeGenerado=cliente.getCodigogenerado();
                    ApiManager apiManager=ApiManager.getInstance(this);
                    apiManager.InsertUser(cliente, new Callback<ResponseLogin>() {
                        @Override
                        public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                            ResponseLogin responseUser = response.body();
                            if (response.code()==404){

                                return;
                            }
                            try{
                                if (responseUser!=null){
                                    if (responseUser.getCode()==0){
                                        ClienteEntity mcliente=   viewModelClientes.getClientebycode(CodeGenerado);
                                        if (mcliente!=null){
                                            mcliente.setNumi(Integer.parseInt(responseUser.getToken()));
                                            mcliente.setEstado(true);
                                            viewModelClientes.updateCliente(mcliente);
                                            Log.d(TAG, "Cliente Guardado en El servidor = "+ mcliente.getNamecliente());
                                            return;
                                        }
                                    }
                                }
                            }catch (Exception e){

                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseLogin> call, Throwable t) {

                            //ShowMessageResult("Error al guardar el pedido");
                        }
                    });
                }
            }else{
                Log.d(TAG, "No Hay Datos Para Exportar");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
