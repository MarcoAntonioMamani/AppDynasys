package com.dynasys.appdisoft.Login.Cloud;

import com.dynasys.appdisoft.Login.DB.Entity.CategoriaPrecioEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DescuentosEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoDetalle;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;
import com.dynasys.appdisoft.Login.DB.Entity.UserEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ZonasEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.strongloop.android.loopback.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IUsersApi {
    @POST("/api/repartidor/login")
    Call<ResponseLogin> LoginUser(@Body Bodylogin user);

    @POST("/api/repartidor/clients/{idrepartidor}")
    Call<ResponseLogin> InsertUser(@Body ClienteEntity user,@Path("idrepartidor") String idRepartidor);

    @PUT("/api/repartidor/clients")
    Call<ResponseLogin> UpdateUser(@Body ClienteEntity user);

    @GET("/api/repartidor/clientes/{idrepartidor}/{idZona}")
    Call<List<ClienteEntity>> ObtenerClientes(@Path("idrepartidor") String idRepartidor,@Path("idZona") int idZona);

    @GET("/api/repartidor/zonas/{idrepartidor}")
    Call<List<ZonasEntity>> ObtenerZonas(@Path("idrepartidor") String idRepartidor);

    @GET("/api/repartidor/precios")
    Call<List<PrecioEntity>> ObtenerPrecios();

    @GET("/api/repartidor/productos")
    Call<List<ProductoEntity>> ObtenerProductos();

    @GET("/api/repartidor/descuentos")
    Call<List<DescuentosEntity>> ObtenerDescuentos();

    @GET("/api/repartidor/pedidos/{idrepartidor}/{idZona}")
    Call<List<PedidoEntity>> ObtenerPedidos(@Path("idrepartidor") String idRepartidor,@Path("idZona") int idZona);
    @GET("/api/repartidor/detalles/{idrepartidor}")
    Call<List<DetalleEntity>> ObtenerDetalles(@Path("idrepartidor") String idRepartidor);

    @GET("/api/stock/{idrepartidor}")
    Call<List<StockEntity>> ObtenerStocks(@Path("idrepartidor") String idRepartidor);

    @GET("/api/precios/categoria")
    Call<List<CategoriaPrecioEntity>> ObtenerPrecioCategoria();


    @POST("/api/repartidor/pedidoDetalleCF")
    Call<ResponseLogin> InsertPedido(@Body PedidoDetalle user);

    @PUT("/api/repartidor/pedido")
    Call<ResponseLogin> UpdatePedido(@Body PedidoEntity user);

    @POST("/api/repartidor/detalle/{oanumi}")
    Call<ResponseLogin> InsertDetalle(@Body List<DetalleEntity> listDetalle,@Path("oanumi") String oanumi);

    @PUT("/api/repartidor/detalle/{oanumi}")
    Call<ResponseLogin> UpdateDetalle(@Body List<DetalleEntity> listDetalle,@Path("oanumi") String oanumi);

    @POST("/api/repartidor/tracking")
    Call<ResponseLogin> InsertTracking(@Body BodyLocation user);
}
