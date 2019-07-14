package com.dynasys.appdisoft.SincronizarData.Presentacion;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;

import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.Bodylogin;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.PreciosListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.common.internal.Preconditions;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SincronizarPresenter implements SincronizarMvp.Presenter {
    private final SincronizarMvp.View mSincronizarview;
    private final Context mContext;
    private final ClientesListViewModel viewModel;
    private final PreciosListViewModel viewModelPrecios;
    private final ProductosListViewModel viewModelProductos;
    private final Activity activity;
     int cantidadCliente = 0;
    int cantidadProducto=0;
    int cantidadPrecio=0;
    String Mensaje="";
    int CantidadPenticiones=0;
    int Contador=0;


    public SincronizarPresenter(SincronizarMvp.View sincronizarView, Context context, ClientesListViewModel viewModel, Activity activity, PreciosListViewModel
                                viewModelPrecios, ProductosListViewModel viewModelProductos){
        mSincronizarview = Preconditions.checkNotNull(sincronizarView);
        mSincronizarview.setPresenter(this);
        this.viewModel=viewModel;
        this.mContext=context;
        this.activity=activity;
        this.viewModelPrecios=viewModelPrecios;
        this.viewModelProductos=viewModelProductos;
         cantidadCliente = 0;
       cantidadProducto=0;
         cantidadPrecio=0;
         CantidadPenticiones=0;
        Contador=0;
    }
    @Override
    public void GuadarDatos(boolean producto,boolean precio,boolean cliente) {
        CantidadPenticiones=0;
        Contador=0;
        Mensaje="";
CantidadPenticiones=(producto==true? 1:0)+(precio==true? 1:0)+(cliente==true? 1:0);
        String Mensaje="";
        if (cliente==true ){
            _DescargarClientes();
        }
        if ( precio==true){
            _DecargarPrecios();
        }
        if (producto&& true){
            _DecargarProductos();
        }
    }

    public void _DecargarPrecios(){
        ApiManager apiManager=ApiManager.getInstance();
        apiManager.ObtenerPrecios( new Callback<List<PrecioEntity>>() {
            @Override
            public void onResponse(Call<List<PrecioEntity>> call, Response<List<PrecioEntity>> response) {
                final List<PrecioEntity> responseUser = (List<PrecioEntity>) response.body();
                if (response.code()==404){
                    mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {
                    try {
                        List<PrecioEntity> listCliente = viewModelPrecios.getMAllPrecio(1);
                        if (listCliente.size() <= 0) {
                            for (int i = 0; i < responseUser.size(); i++) {
                                PrecioEntity precio = responseUser.get(i);
                                viewModelPrecios.insertPrecio(precio);
                            }
                           cantidadPrecio+=responseUser.size();
                           // mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Precios");
                        }else{
                            viewModelPrecios.deleteAllPrecios();
                            for (int i = 0; i < responseUser.size(); i++) {
                                PrecioEntity precio = responseUser.get(i);
                                viewModelPrecios.insertPrecio(precio);
                            }
                          //  mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Precios");
                        }

                        Contador+=1;
                        if (Contador==CantidadPenticiones){
                            Mensaje+=" "+responseUser.size()+" Precios";
                            mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + getMensaje());
                        }else{
                            Mensaje+=" "+responseUser.size()+" Precios , ";
                        }
                    } catch (ExecutionException e) {
                        //e.printStackTrace();
                        mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Precios : "+e.getMessage());
                    } catch (InterruptedException e) {
                        //   e.printStackTrace();
                        mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Precios: "+e.getMessage());
                    }



                } else {
                    mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Precios");
                }
            }

            @Override
            public void onFailure(Call<List<PrecioEntity>> call, Throwable t) {
                mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio.");
            }
        });
    }
    public void _DescargarClientes(){
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
                    try {
                        List<ClienteEntity> listCliente = viewModel.getMAllCliente(1);
                        if (listCliente.size() <= 0) {
                            for (int i = 0; i < responseUser.size(); i++) {
                                ClienteEntity cliente = responseUser.get(i);
                                viewModel.insertCliente(cliente);
                            }
                            cantidadCliente=responseUser.size();
                            //mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Clientes");
                        }else{
                            for (int i = 0; i < responseUser.size(); i++) {
                                ClienteEntity cliente = responseUser.get(i);
                                //viewModel.insertCliente(cliente);
                               ClienteEntity dbcliente=viewModel.getClienteNumi(cliente.getNumi());
                               if (dbcliente==null){
                                   viewModel.insertCliente(cliente);
                               }else{
                                   viewModel.updateCliente(cliente);
                               }

                            }
                            cantidadCliente=responseUser.size();

                           // mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Clientes");
                        }
                        Contador+=1;
                        if (Contador==CantidadPenticiones){
                            Mensaje+=" "+responseUser.size()+" Clientes";
                            mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + getMensaje());
                        }else{
                            Mensaje+=" "+responseUser.size()+" Clientes , ";
                        }
                    } catch (ExecutionException e) {
                        //e.printStackTrace();
                        mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Clientes: "+e.getMessage());
                    } catch (InterruptedException e) {
                        //   e.printStackTrace();
                        mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Clientes : "+e.getMessage());
                    }



                } else {
                    mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Clientes");
                }
            }

            @Override
            public void onFailure(Call<List<ClienteEntity>> call, Throwable t) {
                mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio.");
            }
        });
    }
    public void _DecargarProductos(){
        ApiManager apiManager=ApiManager.getInstance();
        apiManager.ObtenerProductos( new Callback<List<ProductoEntity>>() {
            @Override
            public void onResponse(Call<List<ProductoEntity>> call, Response<List<ProductoEntity>> response) {
                final List<ProductoEntity> responseUser = (List<ProductoEntity>) response.body();
                if (response.code()==404){
                    mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {
                    try {
                        List<ProductoEntity> listCliente = viewModelProductos.getMAllProducto(1);
                        if (listCliente.size() <= 0) {
                            for (int i = 0; i < responseUser.size(); i++) {
                                ProductoEntity producto = responseUser.get(i);
                                viewModelProductos.insertProducto(producto);
                            }
                            cantidadProducto+=responseUser.size();
                            // mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Precios");
                        }else{
                            viewModelProductos.deleteAllProductos();
                            for (int i = 0; i < responseUser.size(); i++) {
                                ProductoEntity producto = responseUser.get(i);
                                viewModelProductos.insertProducto(producto);
                            }
                            cantidadProducto+=responseUser.size();
                            //  mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + responseUser.size() + " Precios");
                        }

                        Contador+=1;
                        if (Contador==CantidadPenticiones){
                            Mensaje+=" "+responseUser.size()+" Productos";
                            mSincronizarview.ShowSyncroMgs("Se ha Registrado/Actualizado " + getMensaje());
                        }else{
                            Mensaje+=" "+responseUser.size()+" Productos , ";
                        }
                    } catch (ExecutionException e) {
                        //e.printStackTrace();
                        mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Productos : "+e.getMessage());
                    } catch (InterruptedException e) {
                        //   e.printStackTrace();
                        mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Productos: "+e.getMessage());
                    }



                } else {
                    mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Productos");
                }
            }

            @Override
            public void onFailure(Call<List<ProductoEntity>> call, Throwable t) {
                mSincronizarview.ShowMessageResult("No es posible conectarse con el web services.");
            }
        });
    }


    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public int getCantidadPenticiones() {
        return CantidadPenticiones;
    }

    public void setCantidadPenticiones(int cantidadPenticiones) {
        CantidadPenticiones = cantidadPenticiones;
    }
}
