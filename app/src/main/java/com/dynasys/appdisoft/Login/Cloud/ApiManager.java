package com.dynasys.appdisoft.Login.Cloud;

import android.content.Context;
import android.widget.EditText;

import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.UserEntity;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static IUsersApi service;
    private static ApiManager apiManager;
private static Context mcontext;
    private ApiManager(Context mcontext) {
        String Url="";
        if (DataPreferences.getPref("servicio",mcontext)==null){
           Url="http://192.168.0.13:3050";
        }else{
            Url=DataPreferences.getPref("servicio",mcontext);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IUsersApi.class);
    }

    public static ApiManager getInstance(Context context) {
     //   if (apiManager == null) {
            apiManager = new ApiManager( context);
       // }
        mcontext=context;
        return apiManager;
    }

    public void LoginUser(Bodylogin user, Callback<ResponseLogin> callback) {
        Call<ResponseLogin> userCall = service.LoginUser(user);
        userCall.enqueue(callback);
    }
    public void InsertUser(ClienteEntity user, Callback<ResponseLogin> callback) {
        Call<ResponseLogin> userCall = service.InsertUser(user);
        userCall.enqueue(callback);
    }
    public void ObtenerClientes( Callback<List<ClienteEntity>> callback) {
        Call<List<ClienteEntity>> userCall = service.ObtenerClientes();
        userCall.enqueue(callback);
    }
    public void ObtenerPrecios( Callback<List<PrecioEntity>> callback) {
        Call<List<PrecioEntity>> userCall = service.ObtenerPrecios();
        userCall.enqueue(callback);
    }
    public void ObtenerProductos( Callback<List<ProductoEntity>> callback) {
        Call<List<ProductoEntity>> userCall = service.ObtenerProductos();
        userCall.enqueue(callback);
    }
    public void ObtenerPedidos( Callback<List<PedidoEntity>> callback,String idRepartidor) {
        Call<List<PedidoEntity>> userCall = service.ObtenerPedidos(idRepartidor);
        userCall.enqueue(callback);
    }
    public void ObtenerDetalles(Callback<List<DetalleEntity>> callback, String idRepartidor) {
        Call<List<DetalleEntity>> userCall = service.ObtenerDetalles(idRepartidor);
        userCall.enqueue(callback);
    }
}
