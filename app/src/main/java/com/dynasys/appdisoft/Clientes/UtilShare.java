package com.dynasys.appdisoft.Clientes;

import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioCategoriaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.VisitaEntity;
import com.dynasys.appdisoft.Mapas.ClientePedidos;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClienteEntity;

import java.util.ArrayList;
import java.util.List;

public class UtilShare {

    public static ClienteEntity cliente=null;
    public static VisitaEntity visita=null;
    public static List<ClienteEntity> ListClientes=new ArrayList<>();
    public static List<ClientePedidos> ListClientesPedidos=new ArrayList<>();
    public static FragmentActivity mActivity=null;
    public static ClienteEntity clienteMapa=null;
    public static int tipoAccion=0;
    public static TextView tvZona;
    public static  int IdCLiente;
    public static DeudaEntity deuda;

    public static ClienteEntity clienteSelected=null;
    public static PrecioCategoriaEntity PrecioCategoriaSelected=null;
    public static List<DetalleEntity> DetalleCarrito=null;
    public static List<ProductoEntity> listProductFiltrado =new ArrayList<>();
    public static List<ProductoEntity> listProductoCarrito =new ArrayList<>();
    public static ProductoEntity ProductoSelected=null;
}
