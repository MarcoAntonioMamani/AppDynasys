package com.dynasys.appdisoft.Login.Cloud;

import com.dynasys.appdisoft.ListarDeudas.Pagos.CobranzaRequest;
import com.dynasys.appdisoft.Login.DB.Entity.AlmacenEntity;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaDetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DescuentosEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoDetalle;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PointEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoViewEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;
import com.dynasys.appdisoft.Login.DB.Entity.VisitaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ZonasEntity;
import com.dynasys.appdisoft.Login.FechaCaducidad;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClienteEntity;

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

    @GET("macros/s/{idrepartidor}")
    Call<List<FechaCaducidad>> ObtenerFechaCaducidad(@Path("idrepartidor") String idRepartidor);

    @POST("/api/repartidor/clients/{idrepartidor}")
    Call<ResponseLogin> InsertUser(@Body ClienteEntity user,@Path("idrepartidor") String idRepartidor);

    @POST("/api/repartidor/visita")
    Call<ResponseLogin> InsertVisita(@Body VisitaEntity user);

    @PUT("/api/repartidor/clients/{idrepartidor}")
    Call<ResponseLogin> UpdateUser(@Body ClienteEntity user,@Path("idrepartidor") String idRepartidor);

    @POST("/api/repartidor/cobranza")
    Call<ResponseLogin> InsertCobranza(@Body CobranzaRequest user);

    @PUT("/api/repartidor/visita")
    Call<ResponseLogin> UpdateVisita(@Body VisitaEntity user);

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

    @GET("/api/repartidor/listado/{idrepartidor}")
    Call<List<ProductoViewEntity>> ObtenerListadoProductos(@Path("idrepartidor") String idRepartidor);

    @GET("/api/repartidor/visitas/{idrepartidor}")
    Call<List<VisitaEntity>> ObtenerListadoVisitas(@Path("idrepartidor") String idRepartidor);

    @GET("/api/repartidor/points/{idrepartidor}")
    Call<List<PointEntity>> ObtenerListadoPoints(@Path("idrepartidor") String idRepartidor);


    @GET("/api/repartidor/detalles/{idrepartidor}")
    Call<List<DetalleEntity>> ObtenerDetalles(@Path("idrepartidor") String idRepartidor);

    @GET("/api/stock/{idrepartidor}")
    Call<List<StockEntity>> ObtenerStocks(@Path("idrepartidor") String idRepartidor);



    @POST("/api/repartidor/pedidoDetalleCF")
    Call<ResponseLogin> InsertPedido(@Body PedidoDetalle user);

    @PUT("/api/repartidor/pedido")
    Call<ResponseLogin> UpdatePedido(@Body PedidoEntity user);

    @PUT("/api/repartidor/pedidoV2")
    Call<ResponseLogin> UpdatePedidoIncluirAnular(@Body PedidoEntity user);

    @POST("/api/repartidor/detalle/{oanumi}")
    Call<ResponseLogin> InsertDetalle(@Body List<DetalleEntity> listDetalle,@Path("oanumi") String oanumi);

    @PUT("/api/repartidor/detalle/{oanumi}/repartidor/{oarepa}")
    Call<ResponseLogin> UpdateDetalle(@Body List<DetalleEntity> listDetalle,@Path("oanumi") String oanumi,@Path("oarepa") String oarepa);

    @POST("/api/repartidor/tracking")
    Call<ResponseLogin> InsertTracking(@Body BodyLocation user);

    @GET("/api/repartidor/deudas/{idrepartidor}")
    Call<List<DeudaEntity>> ObtenerDeudas(@Path("idrepartidor") String idRepartidor);
    @GET("/api/repartidor/Cobranza/{idrepartidor}")
    Call<List<CobranzaEntity>> ObtenerCobranza(@Path("idrepartidor") String idRepartidor);
    @GET("/api/repartidor/almacen/{idrepartidor}")
    Call<List<AlmacenEntity>> ObtenerProductoAlmacen(@Path("idrepartidor") String idRepartidor);
    @GET("/api/repartidor/CobranzaDetalle/{idrepartidor}")

    Call<List<CobranzaDetalleEntity>> ObtenerCobranzaDetalle(@Path("idrepartidor") String idRepartidor);
}
