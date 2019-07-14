package com.dynasys.appdisoft.Login.Cloud;

import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.UserEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.strongloop.android.loopback.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IUsersApi {
    @POST("/api/repartidor/login")
    Call<ResponseLogin> LoginUser(@Body Bodylogin user);
    @POST("/api/repartidor/clients")
    Call<ResponseLogin> InsertUser(@Body ClienteEntity user);
    @GET("/api/repartidor/clientes")
    Call<List<ClienteEntity>> ObtenerClientes();
    @GET("/api/repartidor/precios")
    Call<List<PrecioEntity>> ObtenerPrecios();
    @GET("/api/repartidor/productos")
    Call<List<ProductoEntity>> ObtenerProductos();
}
