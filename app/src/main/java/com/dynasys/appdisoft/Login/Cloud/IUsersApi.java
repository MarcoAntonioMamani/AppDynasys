package com.dynasys.appdisoft.Login.Cloud;

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
    @GET("/api/repartidor/clientes")
    Call<List<ClienteEntity>> ObtenerClientes();
}
