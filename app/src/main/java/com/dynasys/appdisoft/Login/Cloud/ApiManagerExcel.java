package com.dynasys.appdisoft.Login.Cloud;

import android.content.Context;

import com.dynasys.appdisoft.Login.DB.Entity.DescuentosEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoDetalle;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PointEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoViewEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;
import com.dynasys.appdisoft.Login.DB.Entity.VisitaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ZonasEntity;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Login.FechaCaducidad;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManagerExcel {

    private static IUsersApi service;
    public static ApiManagerExcel apiManager;
private static Context mcontext;
    private ApiManagerExcel(Context context) {
        String Url="";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

           Url="https://script.google.com/";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url)
                .addConverterFactory(GsonConverterFactory.create(gson))

                .build();

        service = retrofit.create(IUsersApi.class);
    }

    public static ApiManagerExcel getInstance(Context context) {
       if (apiManager == null) {
            apiManager = new ApiManagerExcel( context);
        }
        mcontext=context;
        return apiManager;
    }
    public void ObtenerFechaCaducidad( String content ,Callback<List<FechaCaducidad>> callback) {
        Call<List<FechaCaducidad>> userCall = service.ObtenerFechaCaducidad(content);
        userCall.enqueue(callback);
    }


}
