package com.dynasys.appdisoft.ShareUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DB.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.common.base.Stopwatch;

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
    private PedidoListViewModel viewModelPedidos;
    private DetalleListViewModel viewModelDetalle;
    public static ServiceSincronizacion mInstance;
    public Runnable runnable = null;
    FragmentActivity activity;
    int idRepartidor;
    public int posicion;
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
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = getApplicationContext();
      //  iRepositoryMovimiento = RepositoryMovimiento.getInstance(mContext,
        mInstance = this;
        this.mContext = getApplicationContext();
         idRepartidor= DataPreferences.getPrefInt("idrepartidor",mContext);
if (UtilShare.mActivity!=null){

    viewModelClientes = ViewModelProviders.of(UtilShare.mActivity).get(ClientesListViewModel.class);
    viewModelPedidos= ViewModelProviders.of(UtilShare.mActivity).get(PedidoListViewModel.class);
    viewModelDetalle=ViewModelProviders.of(UtilShare.mActivity).get(DetalleListViewModel.class);
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
       /* final Handler handler = new Handler();
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
        handler.postDelayed(runnable,1 * 1000);*/
        new ChecarNotificaciones().execute();
        return START_STICKY;
    }

    private void exportarClientes(){
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
                                        List<PedidoEntity> listPedido=viewModelPedidos.getPedidobyCliente(CodeGenerado);
                                        if (mcliente!=null){
                                            mcliente.setNumi(Integer.parseInt(responseUser.getToken()));
                                            mcliente.setCodigogenerado(responseUser.getToken());
                                            mcliente.setEstado(true);
                                            viewModelClientes.updateCliente(mcliente);
                                            for (int j = 0; j < listPedido.size(); j++) {
                                                PedidoEntity pedido=listPedido.get(j);
                                                pedido.setOaccli(responseUser.getToken());
                                                viewModelPedidos.updatePedido(pedido);
                                            }

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
            Log.d(TAG, "Error: "+e.getMessage());
        } catch (InterruptedException e) {
            Log.d(TAG, "Error: "+e.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class ChecarNotificaciones extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            return "";
        }
        @Override
        protected void onPostExecute(String result) {
            //NUESTRO CODIGO
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (isOnline()) {
                        try{
                            _DecargarPedidos(""+idRepartidor);
                            exportarClientes();
                            exportarPedidos();
                            exportarPedidosEstados();
                        }catch (Exception e){
                            Log.d(TAG, "Error" + e.getMessage());
                            new ChecarNotificaciones().execute();
                        }

                    }
                    new ChecarNotificaciones().execute();
}
            }, 9*1000);
            super.onPostExecute(result);
        }
    }
    public void _DecargarPedidos(final String idRepartidor){

        List<ClienteEntity> listCliente = null;
        try {
            listCliente = viewModelClientes.getMAllStateCliente(1);
        List<PedidoEntity> listPedidos=viewModelPedidos.getMAllPedidoState(1);
        List<DetalleEntity>listDetalle=viewModelDetalle.getMAllDetalleState(1);
        if (listCliente==null){
            return;
        }
        if (listPedidos==null){
            return;
        }
        if (listDetalle==null){
            return;
        }
        if (listCliente.size()==0 && listPedidos.size()==0 && listDetalle.size()==0) {

            ApiManager apiManager = ApiManager.getInstance(mContext);
            apiManager.ObtenerPedidos(new Callback<List<PedidoEntity>>() {
                @Override
                public void onResponse(Call<List<PedidoEntity>> call, Response<List<PedidoEntity>> response) {
                    final List<PedidoEntity> responseUser = (List<PedidoEntity>) response.body();
                    if (response.code() == 404) {
                        // mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                        return;
                    }
                    if (response.isSuccessful() && responseUser != null) {
                        try {
                            List<PedidoEntity> listCliente = viewModelPedidos.getMAllPedido(1);

                          /*  viewModelProductos.deleteAllProductos();
                            for (int i = 0; i < responseUser.size(); i++) {
                                ProductoEntity producto = responseUser.get(i);
                                viewModelProductos.insertProducto(producto);
                            }*/
                            for (int i = 0; i < responseUser.size(); i++) {
                                PedidoEntity pedido = responseUser.get(i);
                                //viewModel.insertCliente(cliente);
                                PedidoEntity dbproducto = viewModelPedidos.getPedido(pedido.getOanumi());
                                if (dbproducto == null) {
                                    if (pedido.getOaest() == 1) {
                                        pedido.setOaest(2);
                                        pedido.setEstado(2);
                                        viewModelPedidos.insertPedido(pedido);

                                        Notificacion("" + pedido.getOanumi(), pedido.getCliente(), "" + pedido.getTotal());
                                    } else {
                                        if (pedido.getOaest() == 2) {
                                            viewModelPedidos.insertPedido(pedido);
                                        }

                                    }


                                } else {
                                    if (pedido.getOaest() != dbproducto.getOaest()) {
                                        dbproducto.setOaest(pedido.getOaest());
                                        viewModelPedidos.updatePedido(dbproducto);
                                    }
                                }


                            }

                            _DecargarDetalles(idRepartidor);
                        } catch (ExecutionException e) {
                            //e.printStackTrace();
                            //  mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Productos : "+e.getMessage());
                        } catch (InterruptedException e) {
                            //   e.printStackTrace();
                            //  mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Productos: "+e.getMessage());
                        }


                    } else {
                        // mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Productos");
                    }
                }

                @Override
                public void onFailure(Call<List<PedidoEntity>> call, Throwable t) {
                    // mSincronizarview.ShowMessageResult("No es posible conectarse con el web services.");
                }
            }, idRepartidor);

        }   } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Notificacion(String pedido,String Clie,String Total){
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                UtilShare.mActivity.getBaseContext())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Pedido # "+pedido)
                .setContentText("Cliente: "+Clie +" Total: "+Total)
                .setWhen(System.currentTimeMillis());
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setSound(alarmSound);
        long[] vibrate = { 0, 100, 200, 300 };
        builder.setVibrate(vibrate);
        nManager.notify(Integer.parseInt(pedido), builder.build());
    }
    public void _DecargarDetalles(String idRepartidor){
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.ObtenerDetalles( new Callback<List<DetalleEntity>>() {
            @Override
            public void onResponse(Call<List<DetalleEntity>> call, Response<List<DetalleEntity>> response) {
                final List<DetalleEntity> responseUser = (List<DetalleEntity>) response.body();
                if (response.code()==404){
                  //  mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {
                    try {
                        List<DetalleEntity> listDetalle = viewModelDetalle.getMAllDetalle(1);

                          /*  viewModelProductos.deleteAllProductos();
                            for (int i = 0; i < responseUser.size(); i++) {
                                ProductoEntity producto = responseUser.get(i);
                                viewModelProductos.insertProducto(producto);
                            }*/
                            for (int i = 0; i < responseUser.size(); i++) {
                                DetalleEntity detalle = responseUser.get(i);

                                if (!existeDetalle(listDetalle,detalle)){
                                    viewModelDetalle.insertDetalle(detalle);
                                }else{
                                    //viewModelDetalle.updateDetalle(detalle);
                                }

                            }



                    } catch (ExecutionException e) {
                        //e.printStackTrace();
                       // mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Detalles : "+e.getMessage());
                    } catch (InterruptedException e) {
                        //   e.printStackTrace();
                       // mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Detalles: "+e.getMessage());
                    }



                } else {
                   // mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Detalles");
                }
            }

            @Override
            public void onFailure(Call<List<DetalleEntity>> call, Throwable t) {
                //mSincronizarview.ShowMessageResult("No es posible conectarse con el web services.");
            }
        },idRepartidor);
    }
    private void exportarPedidos(){
//        Looper.prepare();
        try {
            if (viewModelClientes==null){
                return;
            }
            List<ClienteEntity> listCliente = viewModelClientes.getMAllStateCliente(1);
            List<PedidoEntity> listPedidos=viewModelPedidos.getMAllPedidoState(1);
            List<DetalleEntity>listDetalle=viewModelDetalle.getMAllDetalleState(1);
            if (listCliente==null){
                return;
            }
            if (listPedidos==null){
                return;
            }
            if (listDetalle==null){
                return;
            }
            if (listCliente.size()==0 && listPedidos.size()>0){
                posicion = 0;
                final Boolean[] bandera = {false};

                       bandera[0] =true;
                       final PedidoEntity pedido=listPedidos.get(posicion);
                       final String CodeGenerado=pedido.getCodigogenerado();
                       ApiManager apiManager=ApiManager.getInstance(mContext);
                       apiManager.InsertPedido(pedido, new Callback<ResponseLogin>() {
                           @Override
                           public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                               ResponseLogin responseUser = response.body();
                               if (response.code()==404){
                                   // mPedidoView.showSaveResultOption(0,"","");
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
                                               // viewModelPedidos.updatePedido(mPedido);
                                               List<DetalleEntity> list=viewModelDetalle.getDetalle(CodeGenerado);
                                               InsertarDetalleServicio(responseUser.getToken(),list,mPedido,CodeGenerado);
                                               //showSaveResultOption(1,""+mcliente.getNumi(),"");
                                              bandera[0] =false;

                                           }
                                       }
                                   }
                               }catch (Exception e){
                                   // mPedidoView.showSaveResultOption(0,"","");
                                   bandera[0] =false;
                               ;
                               }

                           }

                           @Override
                           public void onFailure(Call<ResponseLogin> call, Throwable t) {
                               //  mPedidoView.showSaveResultOption(0,"","");
                               bandera[0] =false;

                               //ShowMessageResult("Error al guardar el pedido");
                           }
                       });



            }else{
                Log.d(TAG, "No Hay Datos Para Exportar");
            }
        } catch (ExecutionException e) {
            Log.d(TAG, "Error: "+e.getMessage());
        } catch (InterruptedException e) {
            Log.d(TAG, "Error: "+e.getMessage());
        }

    }

    private void exportarPedidosEstados(){
//        Looper.prepare();
        try {
            if (viewModelClientes==null){
                return;
            }
            List<ClienteEntity> listCliente = viewModelClientes.getMAllStateCliente(1);
            List<PedidoEntity> listPedidos=viewModelPedidos.getMAllPedidoState(1);
            List<PedidoEntity> listPedidosEstados=viewModelPedidos.getMAllPedidoState02(1);
            List<DetalleEntity>listDetalle=viewModelDetalle.getMAllDetalleState(1);
            if (listCliente==null){
                return;
            }
            if (listPedidos==null){
                return;
            }
            if (listDetalle==null){
                return;
            }
            if (listCliente.size()==0 && listPedidos.size()==0&& listDetalle.size()>=0 &&listPedidosEstados.size()>0){
                posicion = 0;
                final Boolean[] bandera = {false};

                bandera[0] =true;
                final PedidoEntity pedido=listPedidosEstados.get(posicion);
                final String CodeGenerado=pedido.getCodigogenerado();
                ApiManager apiManager=ApiManager.getInstance(mContext);
                apiManager.UpdatePedido(pedido, new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                        ResponseLogin responseUser = response.body();
                        if (response.code()==404){
                            // mPedidoView.showSaveResultOption(0,"","");
                            return;
                        }
                        try{
                            if (responseUser!=null){
                                if (responseUser.getCode()==0){
                                    PedidoEntity mPedido= viewModelPedidos.getPedido(pedido.getOanumi());
                                    if (mPedido!=null){
                                        mPedido.setEstado(1);
                                        List<DetalleEntity> list=viewModelDetalle.getDetalle(CodeGenerado);
                                        UpdateDetalleServicio(responseUser.getToken(),list,mPedido,CodeGenerado);
                                        //showSaveResultOption(1,""+mcliente.getNumi(),"");
                                        bandera[0] =false;
                                    //viewModelPedidos.updatePedido(mPedido);
                                    }
                                }
                            }
                        }catch (Exception e){
                            // mPedidoView.showSaveResultOption(0,"","");
                            bandera[0] =false;
                            ;
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        //  mPedidoView.showSaveResultOption(0,"","");
                        bandera[0] =false;

                        //ShowMessageResult("Error al guardar el pedido");
                    }
                });



            }else{
                Log.d(TAG, "No Hay Datos Para Exportar");
            }
        } catch (ExecutionException e) {
            Log.d(TAG, "Error: "+e.getMessage());
        } catch (InterruptedException e) {
            Log.d(TAG, "Error: "+e.getMessage());
        }

    }

    public void InsertarDetalleServicio(final String Oanumi, List<DetalleEntity> list, final PedidoEntity pedido, final String CodigoGenerado){
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.InsertDetalle(list,Oanumi, new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin responseUser = response.body();
                if (response.code()==404){
                  //  mPedidoView.showSaveResultOption(0,"","");
                    return;
                }
                try{
                    if (responseUser!=null){
                        if (responseUser.getCode()==1){
                            List<DetalleEntity> listDetalle= viewModelDetalle.getDetalle(CodigoGenerado);
                            if (listDetalle!=null){
                                for (int i = 0; i < listDetalle.size(); i++) {
                                    DetalleEntity item=listDetalle.get(i);
                                    item.setObnumi(Oanumi);
                                    item.setEstado(true);
                                    item.setObupdate(1);
                                    viewModelDetalle.updateDetalle(item);

                                }
                                viewModelPedidos.updatePedido(pedido);
                               // mPedidoView.showSaveResultOption(1,""+Oanumi,"");
                                return;
                            }
                        }
                    }
                }catch (Exception e){
                   // mPedidoView.showSaveResultOption(0,"","");
                    return;
                }

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                //mPedidoView.showSaveResultOption(0,"","");
                //ShowMessageResult("Error al guardar el pedido");
                return;
            }
        });
    }

    public void UpdateDetalleServicio(final String Oanumi, List<DetalleEntity> list, final PedidoEntity pedido, final String CodigoGenerado){
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.UpdateDetalle(list,Oanumi, new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin responseUser = response.body();
                if (response.code()==404){
                    //  mPedidoView.showSaveResultOption(0,"","");
                    return;
                }
                try{
                    if (responseUser!=null){
                        if (responseUser.getCode()==1){
                            List<DetalleEntity> listDetalle= viewModelDetalle.getDetalle(CodigoGenerado);
                            if (listDetalle!=null){
                                for (int i = 0; i < listDetalle.size(); i++) {

                                    DetalleEntity item=listDetalle.get(i);
                                    if (item.getObupdate()>=0){
                                        item.setObnumi(Oanumi);
                                        item.setEstado(true);
                                        item.setObupdate(1);
                                        viewModelDetalle.updateDetalle(item);
                                    }else{
                                        viewModelDetalle.deleteDetalle(item);
                                    }


                                }
                                viewModelPedidos.updatePedido(pedido);
                                // mPedidoView.showSaveResultOption(1,""+Oanumi,"");
                                return;
                            }
                        }
                    }
                }catch (Exception e){
                    // mPedidoView.showSaveResultOption(0,"","");
                    return;
                }

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                //mPedidoView.showSaveResultOption(0,"","");
                //ShowMessageResult("Error al guardar el pedido");
                return;
            }
        });
    }
    public boolean existeDetalle(List<DetalleEntity> listDetalle,DetalleEntity detail){
        for (int i = 0; i < listDetalle.size(); i++) {
            DetalleEntity detalle=listDetalle.get(i);
            if (detalle.getObnumi().toString().trim().equals(detail.getObnumi().toString().trim())&& detalle.getObcprod() ==detail.getObcprod()){
                return true;
            }
        }
        return false;
    }
}
