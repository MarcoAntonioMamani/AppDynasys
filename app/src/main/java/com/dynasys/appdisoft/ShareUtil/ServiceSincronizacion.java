package com.dynasys.appdisoft.ShareUtil;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.ListarDeudas.Pagos.CobranzaRequest;
import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Login.DB.Dao.StockDao;
import com.dynasys.appdisoft.Login.DB.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.AlmacenEntity;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaDetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoDetalle;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;
import com.dynasys.appdisoft.Login.DB.ListViewModel.AlmacenListaViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewModel.CobranzaDetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewModel.CobranzaListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewModel.DeudaListaViewModel;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DB.StockListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
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

import static android.app.Notification.VISIBILITY_PRIVATE;
import static android.app.Notification.VISIBILITY_PUBLIC;

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
    private StockListViewModel viewModelStock;
    private DetalleListViewModel viewModelDetalle;
    private ProductosListViewModel viewModelProducto;
    private CobranzaListViewModel viewModelCobranza;
    private DeudaListaViewModel viewModelDeuda;
    private CobranzaDetalleListViewModel viewModelCobranzaDetalle;
    private AlmacenListaViewModel viewModelAlmacen;
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
        if (mInstance==null){
            mInstance =new ServiceSincronizacion();
            return mInstance;
        }else{
            return mInstance;
        }

    }
    public static ServiceSincronizacion getInstance2() {

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
    viewModelStock= ViewModelProviders.of(UtilShare.mActivity).get(StockListViewModel.class);
    viewModelDetalle=ViewModelProviders.of(UtilShare.mActivity).get(DetalleListViewModel.class);
    viewModelProducto=ViewModelProviders.of(UtilShare.mActivity).get(ProductosListViewModel.class);
    viewModelCobranza =ViewModelProviders.of(UtilShare.mActivity).get(CobranzaListViewModel.class);
    viewModelCobranzaDetalle=ViewModelProviders.of(UtilShare.mActivity).get(CobranzaDetalleListViewModel.class);
    viewModelDeuda=ViewModelProviders.of(UtilShare.mActivity).get(DeudaListaViewModel.class);
    viewModelAlmacen=ViewModelProviders.of(UtilShare.mActivity).get(AlmacenListaViewModel.class);
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

    private void UpdateClientes(){
//        Looper.prepare();
        try {
            if (viewModelClientes==null){
                return;
            }
            List<ClienteEntity> listCliente = viewModelClientes.getMAllStateClienteUpdate(1);
            List<ClienteEntity> listClienteNuevos = viewModelClientes.getMAllStateCliente(1);
            if (listCliente==null){
                return;
            }
            if (listClienteNuevos==null){
                return;
            }
            if (listClienteNuevos.size()>0){
                return;
            }
            if (listCliente.size()>0){

                for (int i = 0; i < listCliente.size(); i++) {

                    final ClienteEntity cliente=listCliente.get(i);
                    ApiManager apiManager=ApiManager.getInstance(this);
                    apiManager.UpdateUser(cliente, new Callback<ResponseLogin>() {
                        @Override
                        public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                            ResponseLogin responseUser = response.body();
                            if (response.code()==404){

                                return;
                            }
                            try{
                                if (responseUser!=null){
                                    if (responseUser.getCode()==0){

                                        if (cliente!=null){

                                            cliente.setEstado(1);
                                            viewModelClientes.updateCliente(cliente);

                                            Log.d(TAG, "Cliente Guardado en El servidor = "+ cliente.getNamecliente());
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
                    int idRepartidor=DataPreferences.getPrefInt("idrepartidor",getApplicationContext());
                    ApiManager apiManager=ApiManager.getInstance(this);
                    apiManager.InsertUser(cliente,String.valueOf(idRepartidor).trim(), new Callback<ResponseLogin>() {
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
                                            mcliente.setEstado(1);
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
                            idRepartidor= DataPreferences.getPrefInt("idrepartidor",mContext);
                            _DecargarPedidos(""+idRepartidor);
                            _DecargarStocks(""+idRepartidor,0);
                            exportarClientes();
                            UpdateClientes();
                            exportarPedidos(""+idRepartidor);
                            exportarPedidosEstados();
                            _PostInsertarCobranza();
                            _DecargarAlmacen(""+idRepartidor);
                            _DecargarDeudas(""+idRepartidor);
                            _DecargarCobranza(""+idRepartidor);
                            _DecargarCobranzaDetalle(""+idRepartidor);

                        }catch (Exception e){
                            Log.d(TAG, "Error" + e.getMessage());
                            new ChecarNotificaciones().execute();
                        }

                    }
                    new ChecarNotificaciones().execute();
}
            }, 12*1000);//8
            super.onPostExecute(result);
        }
    }
    public void _DecargarPedidos(final String idRepartidor){
        int idZonas= DataPreferences.getPrefInt("Zonas",mContext);
        List<ClienteEntity> listCliente = null;
        try {
            listCliente = viewModelClientes.getMAllStateCliente(1);
            List<ClienteEntity>   listClienteUpdate = viewModelClientes.getMAllStateClienteUpdate(1);
        List<PedidoEntity> listPedidos=viewModelPedidos.getMAllPedidoState(1);
        List<DetalleEntity>listDetalle=viewModelDetalle.getMAllDetalleState(1);
        List<PedidoEntity> listPedidoModificados=viewModelPedidos.getMAllPedidoState02(2);
        if (listCliente==null){
            return;
        }
        if (listPedidos==null){
            return;
        }
            if (listClienteUpdate==null){
                return;
            }
            if (listPedidoModificados==null){
                return;
            }
        if (listDetalle==null){
            return;
        }
        Boolean IsLogeado=DataPreferences.getPrefLogin("isLogin",getApplicationContext());

        if (IsLogeado==false){
            onDestroy();
            return ;
        }

        if (listCliente.size()==0 &&listClienteUpdate.size()==0 && listPedidos.size()==0 && listPedidoModificados.size()==0&& listDetalle.size()==0) {

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
                                        pedido .setEstadoStock(1);
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
            }, idRepartidor,idZonas);

        }   } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void _DecargarDeudas(String idRepartidor){

        List<ClienteEntity> listCliente = null;
        List<CobranzaEntity> listCobranza=null;
        try {
            listCliente = viewModelClientes.getMAllStateCliente(1);
            List<ClienteEntity>   listClienteUpdate = viewModelClientes.getMAllStateClienteUpdate(1);
            List<PedidoEntity> listPedidos=viewModelPedidos.getMAllPedidoState(1);
            List<DetalleEntity>listDetalle=viewModelDetalle.getMAllDetalleState(1);
            List<PedidoEntity> listPedidoModificados=viewModelPedidos.getMAllPedidoState02(2);
            listCobranza=viewModelCobranza.getMCobranzaNoSincronizadas();
            if (listCliente==null){
                return;
            }
            if (listPedidos==null){
                return;
            }
            if (listClienteUpdate==null){
                return;
            }
            if (listPedidoModificados==null){
                return;
            }
            if (listDetalle==null){
                return;
            }
            Boolean IsLogeado=DataPreferences.getPrefLogin("isLogin",getApplicationContext());

            if (IsLogeado==false){
                onDestroy();
                return ;
            }

            if (listCliente.size()==0 &&listClienteUpdate.size()==0 && listPedidos.size()==0 && listPedidoModificados.size()==0 &&
            listDetalle.size()==0 && listCobranza.size()==0) {

                ApiManager apiManager = ApiManager.getInstance(mContext);
                apiManager.ObtenerDeudas(idRepartidor, new Callback<List<DeudaEntity>>() {
                    @Override
                    public void onResponse(Call<List<DeudaEntity>> call, Response<List<DeudaEntity>> response) {
                        final List<DeudaEntity> responseUser = (List<DeudaEntity>) response.body();
                        if (response.code()==404){

                            return;
                        }
                        if (response.isSuccessful() && responseUser != null) {
                            try {
                                List<DeudaEntity> listCliente = viewModelDeuda.getMDeudaAllAsync();
                                if (listCliente.size() <= 0) {
                                    for (int i = 0; i < responseUser.size(); i++) {
                                        DeudaEntity precio = responseUser.get(i);
                                        viewModelDeuda.insertDeuda(precio);
                                    }
                                    //cantidadPrecio+=responseUser.size();
                                    // mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Precios");
                                }else{



                                   // viewModelDeuda.deleteAllDeudas();
                                    List<DeudaEntity> listupdate=new ArrayList<>();
                                    Boolean BanderaCobranza=false;
                                    for (int i = 0; i < responseUser.size(); i++) {
                                        DeudaEntity precio = responseUser.get(i);

                                        //viewModel.insertCliente(cliente);
                                        DeudaEntity dbprecio=viewModelDeuda.getDeuda(precio.getPedidoId());
                                        if (dbprecio==null){
                                            viewModelDeuda.insertDeuda(precio);
                                        }else{
                                        if (dbprecio.getPendiente()!=precio.getPendiente()){
                                          BanderaCobranza=true;
                                        }

                                        }

                                    }
                                    if (BanderaCobranza==true){
                                        viewModelDeuda.deleteAllDeudas();
                                        for (int i = 0; i < responseUser.size(); i++) {
                                            DeudaEntity precio = responseUser.get(i);
                                            viewModelDeuda.insertDeuda(precio);
                                        }
                                    }


                                    //  mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Precios");
                                }


                            } catch (ExecutionException e) {
                                //e.printStackTrace();

                            } catch (InterruptedException e) {
                                //   e.printStackTrace();

                            }



                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<List<DeudaEntity>> call, Throwable t) {

                    }
                });

            }   } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void _DecargarCobranza(String idRepartidor){

        List<ClienteEntity> listCliente = null;
        List<CobranzaEntity> listCobranza=null;
        try {
            listCliente = viewModelClientes.getMAllStateCliente(1);
            List<ClienteEntity>   listClienteUpdate = viewModelClientes.getMAllStateClienteUpdate(1);
            List<PedidoEntity> listPedidos=viewModelPedidos.getMAllPedidoState(1);
            List<DetalleEntity>listDetalle=viewModelDetalle.getMAllDetalleState(1);
            List<PedidoEntity> listPedidoModificados=viewModelPedidos.getMAllPedidoState02(2);
            listCobranza=viewModelCobranza.getMCobranzaNoSincronizadas();
            if (listCliente==null){
                return;
            }
            if (listPedidos==null){
                return;
            }
            if (listClienteUpdate==null){
                return;
            }
            if (listPedidoModificados==null){
                return;
            }
            if (listDetalle==null){
                return;
            }
            Boolean IsLogeado=DataPreferences.getPrefLogin("isLogin",getApplicationContext());

            if (IsLogeado==false){
                onDestroy();
                return ;
            }

            if (listCliente.size()==0 &&listClienteUpdate.size()==0 && listPedidos.size()==0 && listPedidoModificados.size()==0 &&
                    listDetalle.size()==0 && listCobranza.size()==0) {

                ApiManager apiManager = ApiManager.getInstance(mContext);
                apiManager.ObtenerCobranza(idRepartidor, new Callback<List<CobranzaEntity>>() {
                    @Override
                    public void onResponse(Call<List<CobranzaEntity>> call, Response<List<CobranzaEntity>> response) {
                        final List<CobranzaEntity> responseUser = (List<CobranzaEntity>) response.body();
                        if (response.code()==404){

                            return;
                        }
                        if (response.isSuccessful() && responseUser != null) {
                            try {
                                List<CobranzaEntity> listCliente = viewModelCobranza.getMCobranzaAllAsync();
                                if (listCliente.size() <= 0) {
                                    for (int i = 0; i < responseUser.size(); i++) {
                                        CobranzaEntity precio = responseUser.get(i);
                                        viewModelCobranza.insertCobranza(precio);
                                    }
                                    //cantidadPrecio+=responseUser.size();
                                    // mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Precios");
                                }else{
                                    viewModelCobranza.deleteAllCobranzas();
                                    List<CobranzaEntity> listupdate=new ArrayList<>();
                                    for (int i = 0; i < responseUser.size(); i++) {
                                        CobranzaEntity precio = responseUser.get(i);
                                        //viewModel.insertCliente(cliente);
                                        CobranzaEntity dbprecio=viewModelCobranza.getCobranza(precio.getTenumi());
                                        if (dbprecio==null){
                                            viewModelCobranza.insertCobranza(precio);
                                        }else{

                                            listupdate.add(precio);
                                            //viewModelPrecios.updatePrecio(precio);
                                        }

                                    }

                                    //  mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Precios");
                                }


                            } catch (ExecutionException e) {
                                //e.printStackTrace();

                            } catch (InterruptedException e) {
                                //   e.printStackTrace();

                            }



                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<List<CobranzaEntity>> call, Throwable t) {

                    }
                });

            }   } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void _DecargarCobranzaDetalle(String idRepartidor){

        List<ClienteEntity> listCliente = null;
        List<CobranzaEntity> listCobranza=null;
        try {
            listCliente = viewModelClientes.getMAllStateCliente(1);
            List<ClienteEntity>   listClienteUpdate = viewModelClientes.getMAllStateClienteUpdate(1);
            List<PedidoEntity> listPedidos=viewModelPedidos.getMAllPedidoState(1);
            List<DetalleEntity>listDetalle=viewModelDetalle.getMAllDetalleState(1);
            List<PedidoEntity> listPedidoModificados=viewModelPedidos.getMAllPedidoState02(2);
            listCobranza=viewModelCobranza.getMCobranzaNoSincronizadas();
            if (listCliente==null){
                return;
            }
            if (listPedidos==null){
                return;
            }
            if (listClienteUpdate==null){
                return;
            }
            if (listPedidoModificados==null){
                return;
            }
            if (listDetalle==null){
                return;
            }
            Boolean IsLogeado=DataPreferences.getPrefLogin("isLogin",getApplicationContext());

            if (IsLogeado==false){
                onDestroy();
                return ;
            }

            if (listCliente.size()==0 &&listClienteUpdate.size()==0 && listPedidos.size()==0 && listPedidoModificados.size()==0 &&
                    listDetalle.size()==0 && listCobranza.size()==0) {

                ApiManager apiManager = ApiManager.getInstance(mContext);
                apiManager.ObtenerCobranzaDetalle(idRepartidor, new Callback<List<CobranzaDetalleEntity>>() {
                    @Override
                    public void onResponse(Call<List<CobranzaDetalleEntity>> call, Response<List<CobranzaDetalleEntity>> response) {
                        final List<CobranzaDetalleEntity> responseUser = (List<CobranzaDetalleEntity>) response.body();
                        if (response.code()==404){

                            return;
                        }
                        if (response.isSuccessful() && responseUser != null) {

                            List<CobranzaDetalleEntity> listCliente = viewModelCobranzaDetalle.getMCobranzaDetalleAllAsync();
                            if (listCliente.size() <= 0) {
                                for (int i = 0; i < responseUser.size(); i++) {
                                    CobranzaDetalleEntity precio = responseUser.get(i);
                                    viewModelCobranzaDetalle.insertCobranzaDetalle(precio);
                                }
                                //cantidadPrecio+=responseUser.size();
                                // mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Precios");
                            }else{
                                viewModelCobranzaDetalle.deleteAllCobranzaDetalles();

                                for (int i = 0; i < responseUser.size(); i++) {
                                    CobranzaDetalleEntity precio = responseUser.get(i);

                                    viewModelCobranzaDetalle.insertCobranzaDetalle(precio);


                                }

                                //  mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Precios");
                            }






                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<List<CobranzaDetalleEntity>> call, Throwable t) {

                    }
                });

            }   } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void _DecargarAlmacen(String idRepartidor){

        List<ClienteEntity> listCliente = null;
        List<CobranzaEntity> listCobranza=null;
        try {
            listCliente = viewModelClientes.getMAllStateCliente(1);
            List<ClienteEntity>   listClienteUpdate = viewModelClientes.getMAllStateClienteUpdate(1);
            List<PedidoEntity> listPedidos=viewModelPedidos.getMAllPedidoState(1);
            List<DetalleEntity>listDetalle=viewModelDetalle.getMAllDetalleState(1);
            List<PedidoEntity> listPedidoModificados=viewModelPedidos.getMAllPedidoState02(2);
            listCobranza=viewModelCobranza.getMCobranzaNoSincronizadas();
            if (listCliente==null){
                return;
            }
            if (listPedidos==null){
                return;
            }
            if (listClienteUpdate==null){
                return;
            }
            if (listPedidoModificados==null){
                return;
            }
            if (listDetalle==null){
                return;
            }
            Boolean IsLogeado=DataPreferences.getPrefLogin("isLogin",getApplicationContext());

            if (IsLogeado==false){
                onDestroy();
                return ;
            }

            if (listCliente.size()==0 &&listClienteUpdate.size()==0 && listPedidos.size()==0 && listPedidoModificados.size()==0 &&
                    listDetalle.size()==0 && listCobranza.size()==0) {

                ApiManager apiManager = ApiManager.getInstance(mContext);
                apiManager.ObtenerProductosAlmacen(idRepartidor, new Callback<List<AlmacenEntity>>() {
                    @Override
                    public void onResponse(Call<List<AlmacenEntity>> call, Response<List<AlmacenEntity>> response) {
                        final List<AlmacenEntity> responseUser = (List<AlmacenEntity>) response.body();
                        if (response.code()==404){

                            return;
                        }
                        if (response.isSuccessful() && responseUser != null) {
                            try {
                                List<AlmacenEntity> listCliente = viewModelAlmacen.getMAlmacenAllAsync();
                                if (listCliente.size() <= 0) {
                                    for (int i = 0; i < responseUser.size(); i++) {
                                        AlmacenEntity precio = responseUser.get(i);
                                        viewModelAlmacen.insertAlmacen(precio);
                                    }
                                    //cantidadPrecio+=responseUser.size();
                                    // mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Precios");
                                }else{
                                    viewModelAlmacen.deleteAllAlmacens();
                                    List<AlmacenEntity> listupdate=new ArrayList<>();
                                    for (int i = 0; i < responseUser.size(); i++) {
                                        AlmacenEntity precio = responseUser.get(i);
                                        //viewModel.insertCliente(cliente);
                                        AlmacenEntity dbprecio=viewModelAlmacen.getAlmacen(precio.getProductoId());
                                        if (dbprecio==null){
                                            viewModelAlmacen.insertAlmacen(precio);
                                        }else{

                                            listupdate.add(precio);
                                            //viewModelPrecios.updatePrecio(precio);
                                        }

                                    }

                                    //  mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Precios");
                                }


                            } catch (ExecutionException e) {
                                //e.printStackTrace();

                            } catch (InterruptedException e) {
                                //   e.printStackTrace();

                            }



                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<List<AlmacenEntity>> call, Throwable t) {

                    }
                });

            }   } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }
    }
    public void _PostInsertarCobranza(){

        List<ClienteEntity> listCliente = null;
        try {
            listCliente = viewModelClientes.getMAllStateCliente(1);
            List<ClienteEntity>   listClienteUpdate = viewModelClientes.getMAllStateClienteUpdate(1);
            List<PedidoEntity> listPedidos=viewModelPedidos.getMAllPedidoState(1);
            List<DetalleEntity>listDetalle=viewModelDetalle.getMAllDetalleState(1);
            List<PedidoEntity> listPedidoModificados=viewModelPedidos.getMAllPedidoState02(2);
            List<CobranzaEntity> listCobranza=viewModelCobranza.getMCobranzaNoSincronizadas();
            if (listCliente==null){
                return;
            }
            if (listPedidos==null){
                return;
            }
            if (listClienteUpdate==null){
                return;
            }
            if (listPedidoModificados==null){
                return;
            }
            if (listDetalle==null){
                return;
            }
            Boolean IsLogeado=DataPreferences.getPrefLogin("isLogin",getApplicationContext());

            if (IsLogeado==false){
                onDestroy();
                return ;
            }

            if (listCliente.size()==0 &&listClienteUpdate.size()==0 && listPedidos.size()==0 && listPedidoModificados.size()==0&& listDetalle.size()==0
            && listCobranza.size()>0) {

                CobranzaEntity cob=listCobranza.get(0);
                List<CobranzaDetalleEntity>detalle=viewModelCobranzaDetalle.getCobranzaDetalle(cob.getTenumi());
                final CobranzaRequest cobranza=new CobranzaRequest();
                cobranza.setTenumi(cob.getTenumi());
                cobranza.setIdPersonal(cob.getIdPersonal());
                cobranza.setEstado(cob.getEstado());
                cobranza.setFecha(cob.getFecha());
                cobranza.setObservacion(cob.getObservacion());
                cobranza.setId(cob.getId());
                cobranza.setListDetalle(detalle);
                ApiManager apiManager = ApiManager.getInstance(mContext);
                apiManager.InsertCobranza(cobranza, new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                        ResponseLogin responseUser = response.body();
                        if (response.code()==404 || response.code()==500){
                            return;
                        }
                        try{
                            if (responseUser!=null){
                                if (responseUser.getCode()==0){
                                    CobranzaEntity mPedido= viewModelCobranza.getCobranza(cobranza.getTenumi());
                                    List<DeudaEntity> listdeuda=viewModelDeuda.getMDeudaAllAsync();
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

                                        }

                                        if (listdeuda.size()>0){
                                            DeudaEntity deud=listdeuda.get(0);

                                            deud.setTotalAPagar(0);
                                            viewModelDeuda.updateDeuda(deud);

                                        }


                                        //showSaveResultOption(1,""+mcliente.getNumi(),"");
                                        return;
                                    }
                                }else{

                                    return;
                                }
                            }
                        }catch (Exception e){

                            return;
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {

                        return;
                        //ShowMessageResult("Error al guardar el pedido");
                    }
                });

            }   } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void _DecargarStocks(final String idRepartidor,final int Tipo){

        List<ClienteEntity> listCliente = null;
        try {
            listCliente = viewModelClientes.getMAllStateCliente(1);
            List<ClienteEntity>   listClienteUpdate = viewModelClientes.getMAllStateClienteUpdate(1);
            List<PedidoEntity> listPedidos=viewModelPedidos.getMAllPedidoState(1);
            List<DetalleEntity>listDetalle=viewModelDetalle.getMAllDetalleState(1);
            List<PedidoEntity> listPedidoModificados=viewModelPedidos.getMAllPedidoState02(2);
            if (listCliente==null){
                return;
            }
            if (listPedidos==null){
                return;
            }
            if (listClienteUpdate==null){
                return;
            }
            if (listPedidoModificados==null){
                return;
            }
            if (listDetalle==null){
                return;
            }
            Boolean IsLogeado=DataPreferences.getPrefLogin("isLogin",getApplicationContext());

            if (IsLogeado==false){
                onDestroy();
                return ;
            }

            if (listCliente.size()==0 &&listClienteUpdate.size()==0 ) {

                ApiManager apiManager = ApiManager.getInstance(mContext);
                apiManager.ObtenerStock(new Callback<List<StockEntity>>() {
                    @Override
                    public void onResponse(Call<List<StockEntity>> call, Response<List<StockEntity>> response) {
                        final List<StockEntity> responseUser = (List<StockEntity>) response.body();
                        if (response.code() == 404) {
                            // mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                            return;
                        }
                        if (response.isSuccessful() && responseUser != null) {
                            if (responseUser.size()>0){
                                viewModelStock.deleteAllStocks();
                            }
                                List<StockEntity> listStock = viewModelStock.getMStockAllAsync();
                             final List<StockEntity > listStockInsert = new ArrayList<>();
                                for (int i = 0; i < responseUser.size(); i++) {
                                    StockEntity stock = responseUser.get(i);  //Obtenemos el registro del server
                                    //viewModel.insertCliente(cliente);

                                    StockEntity dbStock = ObtenerProducto(listStock,stock.getCodigoProducto());

                                    if (dbStock == null) {
                                        if (stock.getCantidad()<0) {
                                            stock.setCantidad(0);
                                           // viewModelStock.insertStock(stock);
                                            listStockInsert.add(stock);
                                        }else{
                                            listStockInsert.add(stock);

                                           // viewModelStock.insertStock(stock);
                                        }

                                    } else {


                                            if (stock.getCodigoProducto()==dbStock.getCodigoProducto()&&stock.getCantidad()!=dbStock.getCantidad()){

                                                if (stock.getCantidad()<0) {
                                                    dbStock .setCantidad(0);
                                                    viewModelStock.updateStock(dbStock);
                                                }else{
                                                    dbStock .setCantidad(stock.getCantidad());
                                                    viewModelStock.updateStock(dbStock);
                                                }

                                            }

                                    }



                                }
                            viewModelStock.insertListStock(listStockInsert);


                                if (Tipo==1){  //Quiere decir que haique insertar Pedidos
                                    exportarPedidosStock();
                                }
                        } else {
                            // mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Productos");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StockEntity>> call, Throwable t) {
                    }
                }, idRepartidor);

            }   } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public StockEntity ObtenerProducto(List<StockEntity>  lista,int codProducto){

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigoProducto()==codProducto){
                return lista.get(i);
            }

        }
        return null;

    }
    public void Notificacion(String pedido,String Clie,String Total) {
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                UtilShare.mActivity.getBaseContext())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Pedido # "+pedido)
                .setContentText("Cliente: "+Clie +" Total: "+Total)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(VISIBILITY_PRIVATE);
        //Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        long[] vibrate = { 0, 100, 200, 300 };
        builder.setVibrate(vibrate);
        nManager.notify(Integer.parseInt(pedido), builder.build());


        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            //e.printStackTrace();
        }

/*
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setContentInfo("Info");

        notificationManager.notify(1, notificationBuilder.build());*/
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


    private void exportarPedidos(String IdRepartidor){
        _DecargarStocks(IdRepartidor,1);


    }
    private void exportarPedidosStock(){
//        Looper.prepare();
        try {
            if (viewModelClientes==null){
                return;
            }
            List<ClienteEntity> listCliente = viewModelClientes.getMAllStateCliente(1);
            List<ClienteEntity>   listClienteUpdate = viewModelClientes.getMAllStateClienteUpdate(1);
            List<PedidoEntity> listPedidos=viewModelPedidos.getMAllPedidoState(1);
            List<DetalleEntity>listDetalle=viewModelDetalle.getMAllDetalleState(1);
            List<PedidoEntity>listPedidoSinStock=viewModelPedidos.getMAllPedidoSinStock(1);
            if (listCliente==null){
                return;
            }
            if (listClienteUpdate==null){
                return;
            }
            if (listPedidos==null){
                return;
            }
            if (listDetalle==null){
                return;
            }
            if (listCliente.size()==0 && listClienteUpdate.size()==0 && listPedidos.size()>0){
                posicion = 0;
                final Boolean[] bandera = {false};

                bandera[0] =true;
                final PedidoEntity pedido=listPedidos.get(posicion);
                final String CodeGenerado=pedido.getCodigogenerado();
                List<DetalleEntity> Detalle= viewModelDetalle.getDetalle(CodeGenerado);
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
                ///Validamos Stock
                Boolean TieneStock=VerficarStockDisponible(Detalle);
                if (TieneStock==false){
                    pedido.setEstadoStock(2);
                    viewModelPedidos .updatePedido(pedido);
                    return;
                }




                ApiManager apiManager=ApiManager.getInstance(mContext);
                apiManager.InsertPedido(p, new Callback<ResponseLogin>() {
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


                                    }
                                }
                            }
                        }catch (Exception e){
                            // mPedidoView.showSaveResultOption(0,"","");

                            ;
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        //  mPedidoView.showSaveResultOption(0,"","");

                        //ShowMessageResult("Error al guardar el pedido");
                    }
                });



            }else{
                if (listCliente.size()==0 && listClienteUpdate.size()==0 && listPedidos.size()==0 && listPedidoSinStock .size()>0){


                    posicion = 0;
                    final Boolean[] bandera = {false};

                    bandera[0] =true;
                    final PedidoEntity pedido=listPedidoSinStock.get(posicion);
                    final String CodeGenerado=pedido.getCodigogenerado();
                    List<DetalleEntity> Detalle= viewModelDetalle.getDetalle(CodeGenerado);
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
                    ///Validamos Stock
                    Boolean TieneStock=VerficarStockDisponible(Detalle);
                    if (TieneStock==false){
                       // pedido.setEstadoStock(2);
                       // viewModelPedidos .updatePedido(pedido);
                        return;
                    }




                    ApiManager apiManager=ApiManager.getInstance(mContext);
                    apiManager.InsertPedido(p, new Callback<ResponseLogin>() {
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
                                            mPedido.setEstadoStock(1);
                                            mPedido.setCodigogenerado(responseUser.getToken());
                                            // viewModelPedidos.updatePedido(mPedido);
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


                                        }
                                    }
                                }
                            }catch (Exception e){
                                // mPedidoView.showSaveResultOption(0,"","");

                                ;
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseLogin> call, Throwable t) {
                            //  mPedidoView.showSaveResultOption(0,"","");

                            //ShowMessageResult("Error al guardar el pedido");
                        }
                    });








                }else{
                    Log.d(TAG, "No Hay Datos Para Exportar");
                }

            }
        } catch (ExecutionException e) {

            Log.d(TAG, "Error: "+e.getMessage());
        } catch (InterruptedException e) {

            Log.d(TAG, "Error: "+e.getMessage());
        }

    }

    public Boolean VerficarStockDisponible(List<DetalleEntity> mDetalleItem){
        // viewModelProducto.getMProductoByStock();
        for (int i = 0; i < mDetalleItem.size(); i++) {

            DetalleEntity detail=mDetalleItem.get(i);
            try {
                ProductoEntity p =viewModelProducto.getMProductoByStock(detail.getObcprod());
                if (p!=null){

                    mDetalleItem.get(i).setStock(p.getStock());


                }
            } catch (ExecutionException e) {
            } catch (InterruptedException e) {
            }
        }
        boolean b =true;

        for (int i = 0; i < mDetalleItem.size(); i++) {
            if (mDetalleItem.get(i).getObupdate()>=0){

                if (mDetalleItem.get(i).getObpcant() >mDetalleItem.get(i).getStock()){
                    return false;
                }
            }

        }
        return true;


    }
    private void exportarPedidosEstados(){
//        Looper.prepare();
        try {
            if (viewModelClientes==null){
                return;
            }
            List<ClienteEntity> listCliente = viewModelClientes.getMAllStateCliente(1);
            List<ClienteEntity>   listClienteUpdate = viewModelClientes.getMAllStateClienteUpdate(1);
            List<PedidoEntity> listPedidos=viewModelPedidos.getMAllPedidoState(1);
            List<PedidoEntity> listPedidosEstados=viewModelPedidos.getMAllPedidoState02(1);
            List<DetalleEntity>listDetalle=viewModelDetalle.getMAllDetalleState(1);
            if (listCliente==null){
                return;
            }
            if (listClienteUpdate==null){
                return;
            }
            if (listPedidos==null){
                return;
            }
            if (listDetalle==null){
                return;
            }
            if (listCliente.size()==0 && listClienteUpdate.size()==0 && listPedidos.size()==0&& listDetalle.size()>=0 &&listPedidosEstados.size()>0 ){
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
                                        mPedido.setEstadoUpdate(1);
                                        List<DetalleEntity> list=viewModelDetalle.getDetalle(CodeGenerado);
                                        UpdateDetalleServicio(responseUser.getToken(),list,mPedido,CodeGenerado);
                                        //showSaveResultOption(1,""+mcliente.getNumi(),"");

                                    //viewModelPedidos.updatePedido(mPedido);
                                    }
                                }
                            }
                        }catch (Exception e){
                            // mPedidoView.showSaveResultOption(0,"","");


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
