package com.dynasys.appdisoft.Mapas;

import android.Manifest;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.config.GoogleDirectionConfiguration;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.dynasys.appdisoft.Clientes.CustomInfoWindowAdapter;
import com.dynasys.appdisoft.Clientes.TipoMapa;
import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Constantes;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PointEntity;
import com.dynasys.appdisoft.Login.DB.Entity.VisitaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ZonasEntity;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.PointListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.VisitaListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.ZonaListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;



import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback , DirectionCallback {
    GoogleMap mapa;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<ClienteEntity> lisClientes = new ArrayList<>();

    private List<VisitaEntity> listVisitados=new ArrayList<>();
    private List<ClientePedidos> listClientePedidos=new ArrayList<>();
    private List<ClienteEntity> lisClientesBackup = new ArrayList<>();
    private ZonaListViewModel viewModelZonas;
    private PointListViewModel viewmodelPoint;
    private ClientesListViewModel viewModelCliente;
    private VisitaListViewModel viewModelVisita;
    private List<PedidoEntity> listPedidos=new ArrayList<>();
    private PedidoListViewModel viewModelPedido;
    private String serverKey = "AIzaSyDRwwdFpU2VpMvsfw6igVL61iZj7eP5NJQ";
    private ClusterManager<StringClusterItem> mClusterManager;
    private Spinner listaTipos;
    private Button btnCargar;
    private Location Dlocation=null;
    private ProgressDialog progresdialog;

    private TextView tvlb01,tvlb02,tvlb03;

    LinearLayout linearMapaPedidos;
    private int tipoAccion=0;
    private Handler switchHandler = new Handler();
    public boolean BanderGps =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        listaTipos =findViewById(R.id.id_lista_Tipos);
        btnCargar=findViewById(R.id.btnFindPathall);
        linearMapaPedidos=findViewById(R.id.mapa_pedidos);
        tvlb01=findViewById(R.id.lb01);
        tvlb02=findViewById(R.id.lb2);
        tvlb03=findViewById(R.id.lb03);
        viewModelCliente = ViewModelProviders.of(this).get(ClientesListViewModel.class);
        viewModelPedido = ViewModelProviders.of(this).get(PedidoListViewModel.class);
        viewModelZonas=ViewModelProviders.of(this).get(ZonaListViewModel.class);
        viewmodelPoint=ViewModelProviders.of(this).get(PointListViewModel.class);
        viewModelVisita=ViewModelProviders.of(this).get(VisitaListViewModel.class);
        _CargarTipos();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_todos);
        mapFragment.getMapAsync(this);
        listaTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos>=0 ){
                    long code=id;

                    CargarClientes(pos);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // CargarClientes(listaTipos.getSelectedItemPosition());
                if(_prCheckstatus()){
                    requestDirection();
                }else{
                    createSimpleDialog().show();


                }

            }
        });
        _prGpsObtener();
        ValidarButtonVisible();
    }
    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Usuario:")
                .setMessage("Su Gps esta desactivado \n Desea Activarlo? .....")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity (intent);
                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        return builder.create();
    }
void ValidarButtonVisible(){
        try {
            int ViewMapa= DataPreferences.getPrefInt("ViewRuta",this);
            if (ViewMapa==0){
                btnCargar.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }

}
    public void _CargarTipos(){
        List<TipoMapa> list=new ArrayList<>();
        list.add(new TipoMapa(1,"Ver Clientes"));
        list.add(new TipoMapa(2,"Ver Solo Pedidos Pendientes"));
        list.add(new TipoMapa(3,"Ver Solo Pedidos Entregados"));
        list.add(new TipoMapa(4,"Ver Todos Los Pedidos"));
        list.add(new TipoMapa(5,"Ver Visitados"));

        ArrayAdapter<TipoMapa> adapter =new ArrayAdapter<TipoMapa>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listaTipos.setAdapter(adapter);

    }

    public void CargarClientes(int tipo){
        List<ClientePedidos> listClientePed=new ArrayList<>();
        if (tipo==0){
            linearMapaPedidos.setVisibility(View.GONE);
            try {
                tipoAccion=0;
                //lisClientes=FiltarByZona(viewModelCliente.getMAllCliente(0));
                lisClientes=viewModelCliente.getMAllCliente(0);

                listClientePed.clear();

                for (int i = 0; i < lisClientes.size(); i++) {
                    listClientePed.add(new ClientePedidos(lisClientes.get(i),1,1));
                }
                UtilShare.ListClientesPedidos=listClientePed;
                listClientePedidos=listClientePed;
                dibujarClientes();
            } catch (ExecutionException e) {

            } catch (InterruptedException e) {

            }
        }
        if (tipo==1){
            linearMapaPedidos.setVisibility(View.GONE);
            try {
                tipoAccion=1;
                lisClientesBackup=viewModelCliente.getMAllCliente(0);
                lisClientes=new ArrayList<>();
                listPedidos=viewModelPedido.getMAllPedido(0);

                listClientePed.clear();
                for (int i = 0; i < listPedidos.size(); i++) {
                    PedidoEntity pedi=listPedidos.get(i);
                    if (pedi.getOaest()!=3){
                        ClienteEntity cliente=obtenerCliente(pedi);
                        lisClientes.add(cliente);

                        listClientePed.add(new ClientePedidos(cliente,pedi.getOaest(),pedi.getOaap()));
                    }
                }
                UtilShare.ListClientes=lisClientes;
                UtilShare.ListClientesPedidos=listClientePed;
                listClientePedidos=listClientePed;
                dibujarClientes();


            } catch (ExecutionException e) {

            } catch (InterruptedException e) {

            }
        }

        if (tipo==2){
            linearMapaPedidos.setVisibility(View.GONE);
            try {
                tipoAccion=2;
                lisClientesBackup=viewModelCliente.getMAllCliente(0);
                lisClientes=new ArrayList<>();
                listPedidos=viewModelPedido.getMAllPedido(0);
                listClientePed.clear();
                for (int i = 0; i < listPedidos.size(); i++) {
                    PedidoEntity pedi=listPedidos.get(i);
                    if (pedi.getOaest()==3){
                        ClienteEntity cliente=obtenerCliente(pedi);
                        listClientePed.add(new ClientePedidos(cliente,pedi.getOaest(),pedi.getOaap()));

                        lisClientes.add(cliente);
                    }
                }
                UtilShare.ListClientes=lisClientes;
                listClientePedidos=listClientePed;
                UtilShare.ListClientesPedidos=listClientePed;
                dibujarClientes();
            } catch (ExecutionException e) {

            } catch (InterruptedException e) {

            }
        }
        if (tipo==3){
            linearMapaPedidos.setVisibility(View.VISIBLE);
            tvlb01.setText("Cliente Con Pedidos Entregados");
            tvlb02.setText("Cliente Con Pedidos No Entregados");
            tvlb03.setText("Cliente Rechazado");
            try {
                tipoAccion=3;
                lisClientesBackup=viewModelCliente.getMAllCliente(0);
                lisClientes=new ArrayList<>();
                listPedidos=viewModelPedido.getMAllPedido(0);
                listClientePed.clear();
                for (int i = 0; i < listPedidos.size(); i++) {
                    PedidoEntity pedi=listPedidos.get(i);

                        ClienteEntity cliente=obtenerCliente(pedi);
                        listClientePed.add(new ClientePedidos(cliente,pedi.getOaest(),pedi.getOaap()));

                        lisClientes.add(cliente);

                }
                UtilShare.ListClientes=lisClientes;
                UtilShare.ListClientesPedidos=listClientePed;
                listClientePedidos=listClientePed;
                dibujarClientesEstadoPedido();
            } catch (ExecutionException e) {

            } catch (InterruptedException e) {

            }
        }
        if (tipo==4){
            linearMapaPedidos.setVisibility(View.VISIBLE);
            tvlb01.setText("Cliente No Visitados");
            tvlb02.setText("Cliente Visitado Sin Pedido");
            tvlb03.setText("Cliente Visitado Con Pedido");
            try {
                tipoAccion=3;
                lisClientesBackup=viewModelCliente.getMAllCliente(0);
                listVisitados=viewModelVisita.getVisitaAllAsync(1);

                lisClientes=new ArrayList<>();
                listPedidos=viewModelPedido.getMAllPedido(0);
                listClientePed.clear();

                for (int i = 0; i < lisClientesBackup.size(); i++) {
                    if (!obtenerClienteVisita(lisClientesBackup.get(i))&& !obtenerClientePedido(lisClientesBackup.get(i))){
                        listClientePed.add(new ClientePedidos(lisClientesBackup.get(i),0,1));//0= No visitados y sin pedidos

                    }

                    lisClientes.add(lisClientesBackup.get(i));
                }

                for (int i = 0; i < listVisitados.size(); i++) {
                    VisitaEntity pedi=listVisitados.get(i);

                    ClienteEntity cliente=obtenerClienteVisita(pedi);

                    if (!obtenerClientePedido(cliente)&&obtenerClienteVisita(cliente) ){
                        listClientePed.add(new ClientePedidos(cliente,1,1));  //Visitado sin pedidos
                    }
                    if (obtenerClientePedido(cliente)&&obtenerClienteVisita(cliente) ){
                        listClientePed.add(new ClientePedidos(cliente,2,1));  // Visitados con pedidos
                    }


                }
                UtilShare.ListClientes=lisClientes;
                UtilShare.ListClientesPedidos=listClientePed;
                listClientePedidos=listClientePed;
                dibujarClientesEstadoVisitas();
            } catch (ExecutionException e) {

            } catch (InterruptedException e) {

            }
        }
    }

    public ClienteEntity obtenerCliente(PedidoEntity pedido){
        for (int i = 0; i < lisClientesBackup.size(); i++) {
            ClienteEntity client=lisClientesBackup.get(i);
            if (pedido.getOaccli().trim().equals((""+client.getNumi()).trim())){
                return client;
            }
        }
        return null;
    }

    public ClienteEntity obtenerClienteVisita(VisitaEntity pedido){
        for (int i = 0; i < lisClientesBackup.size(); i++) {
            ClienteEntity client=lisClientesBackup.get(i);
            if (pedido.getClienteId().trim().equals((""+client.getNumi()).trim())){
                return client;
            }
        }
        return null;
    }

    public Boolean obtenerClienteVisita(ClienteEntity pedido){
        for (int i = 0; i < listVisitados .size(); i++) {
            VisitaEntity client=listVisitados.get(i);
            if (pedido.getCodigogenerado().trim().equals((""+client.getClienteId()).trim())){
                return true;
            }
        }
        return false;
    }

    public Boolean obtenerClientePedido(ClienteEntity pedido){
        for (int i = 0; i < listPedidos .size(); i++) {
            PedidoEntity client=listPedidos.get(i);
            if (pedido.getCodigogenerado().trim().equals((""+client.getOaccli()).trim())&&client.getOaap()==1){
                return true;
            }
        }
        return false;
    }
    public void dibujarClientes(){
        if (mapa==null){
            return;
        }
        mapa.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapa.setMyLocationEnabled(true);


        mClusterManager = new ClusterManager<>(this, mapa);
       // mapa.setOnCameraChangeListener( mClusterManager);
        final CameraPosition[] mPreviousCameraPosition = {null};

        mapa.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition position = mapa.getCameraPosition();
                if(mPreviousCameraPosition[0] == null || mPreviousCameraPosition[0].zoom != position.zoom) {
                    mPreviousCameraPosition[0] = mapa.getCameraPosition();
                    mClusterManager.cluster();
                }
            }
        });
        for (int i = 0; i < listClientePedidos.size(); i++) {

            ClienteEntity mov=listClientePedidos.get(i).getCliente();
            if (listClientePedidos.get(i).getAnulado()==1){///Anulado = 1  quiere decir que esta activo
                if (mov!=null){
                    if (mov.getLongitud()!=null){
                        if (mov.getLongitud()!=0){
               /* originMarkers.add(mapa.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient02 ))
                        .title(""+i)
                        .position(new LatLng(mov.getLatitud(),mov.getLongitud()))));*/
                            mClusterManager.addItem(new StringClusterItem("" + (i), new LatLng(mov.getLatitud(),mov.getLongitud())));

                        }
                    }
                }
            }



        }
        mClusterManager.cluster();
        if (lisClientes.size()>0){
            //mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lisClientes.get(0).getLatitud(),lisClientes.get(0).getLongitud()), 15));
          LatLng ubicacion=ObtenerUbicacion();
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
        }

        final CustomClusterRenderer renderer = new CustomClusterRenderer(this, mapa, mClusterManager);
        mClusterManager.setRenderer(renderer);
        mClusterManager.getMarkerCollection()
                .setOnInfoWindowAdapter(new CustomInfoWindowAdapterMapa(LayoutInflater.from(this)));

        mapa.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mapa.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker arg0) {

                ClienteEntity obj=(ClienteEntity) arg0.getTag();
                UtilShare.clienteMapa=obj;
                UtilShare.tipoAccion=tipoAccion;
                DataPreferences.putPrefInteger("Accion",1,getApplicationContext());
                DataPreferences.putPref("idCliente",obj.getCodigogenerado() ,getApplicationContext());
                if (tipoAccion==0){
                    DataPreferences.putPrefInteger("Accion",1,getApplicationContext());
                    DataPreferences.putPref("idCliente",obj.getCodigogenerado() ,getApplicationContext());
                           /* Fragment frag = new CreatePedidoFragment(1);
                            switchFragment(frag,"CREATE_PEDIDOS");*/
                  onBackPressed();
                }
                if (tipoAccion==1){
                    DataPreferences.putPrefInteger("Accion",2,getApplicationContext());
                    DataPreferences.putPref("idCliente",obj.getCodigogenerado() ,getApplicationContext());
                           /* Fragment frag = new CreatePedidoFragment(1);
                            switchFragment(frag,"CREATE_PEDIDOS");*/
                    onBackPressed();
                }
            }

        } );

    }

   public void dibujarClientesEstadoPedido(){
        if (mapa==null){
            return;
        }
        mapa.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapa.setMyLocationEnabled(true);


        mClusterManager = new ClusterManager<>(this, mapa);
        // mapa.setOnCameraChangeListener( mClusterManager);
        final CameraPosition[] mPreviousCameraPosition = {null};


        for (int i = 0; i < listClientePedidos.size(); i++) {

            ClienteEntity mov=listClientePedidos.get(i).getCliente();
            if (mov!=null){
                if (mov.getLongitud()!=null){
                    if (mov.getLongitud()!=0){
               /* originMarkers.add(mapa.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient02 ))
                        .title(""+i)
                        .position(new LatLng(mov.getLatitud(),mov.getLongitud()))));*/

                        /*
                        mClusterManager.addItem(new StringClusterItem("" + (i), new LatLng(mov.getLatitud(),mov.getLongitud())));
*/

                        MarkerOptions marker = new MarkerOptions().position(new LatLng(mov.getLatitud(),mov.getLongitud()));
                        if (listClientePedidos.get(i).getAnulado()==2){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient01));
                        }else{
                            if (listClientePedidos.get(i).getEstadoPedido() ==2){
                                marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient03));
                            }else{
                                marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient02));
                            }

                        }
// Changing marker icon




                        Marker mm= mapa.addMarker(marker);

                        mm.setTag(mov);

                    }
                }
            }


        }

        mapa.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(this)));
        mapa.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ClienteEntity obj=(ClienteEntity) marker.getTag();
                UtilShare.clienteMapa=obj;
                UtilShare.tipoAccion=tipoAccion;
                DataPreferences.putPrefInteger("Accion",1,getApplicationContext());
                DataPreferences.putPref("idCliente",obj.getCodigogenerado() ,getApplicationContext());
                if (tipoAccion==0){
                    DataPreferences.putPrefInteger("Accion",1,getApplicationContext());
                    DataPreferences.putPref("idCliente",obj.getCodigogenerado() ,getApplicationContext());
                           /* Fragment frag = new CreatePedidoFragment(1);
                            switchFragment(frag,"CREATE_PEDIDOS");*/

                    onBackPressed();
                }
                if (tipoAccion==1){
                    DataPreferences.putPrefInteger("Accion",2,getApplicationContext());
                    DataPreferences.putPref("idCliente",obj.getCodigogenerado() ,getApplicationContext());
                           /* Fragment frag = new CreatePedidoFragment(1);
                            switchFragment(frag,"CREATE_PEDIDOS");*/
                    onBackPressed();
                }
            }

        });
      //  mClusterManager.cluster();
        if (lisClientes.size()>0){
            //mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lisClientes.get(0).getLatitud(),lisClientes.get(0).getLongitud()), 15));
            LatLng ubicacion=ObtenerUbicacion();
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
        }
       Dibujarzonas();

    }

    public void dibujarClientesEstadoVisitas(){



        if (mapa==null){
            return;
        }
        mapa.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapa.setMyLocationEnabled(true);


        mClusterManager = new ClusterManager<>(this, mapa);
        // mapa.setOnCameraChangeListener( mClusterManager);
        final CameraPosition[] mPreviousCameraPosition = {null};




        for (int i = 0; i < listClientePedidos.size(); i++) {

            ClienteEntity mov=listClientePedidos.get(i).getCliente();
            if (mov!=null){
                if (mov.getLongitud()!=null){
                    if (mov.getLongitud()!=0){
               /* originMarkers.add(mapa.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient02 ))
                        .title(""+i)
                        .position(new LatLng(mov.getLatitud(),mov.getLongitud()))));*/

                        /*
                        mClusterManager.addItem(new StringClusterItem("" + (i), new LatLng(mov.getLatitud(),mov.getLongitud())));
*/

                        MarkerOptions marker = new MarkerOptions().position(new LatLng(mov.getLatitud(),mov.getLongitud()));

// Changing marker icon
                        if (listClientePedidos.get(i).getEstadoPedido()==0){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient02));
                        }
                        if (listClientePedidos.get(i).getEstadoPedido() ==1){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient03));
                        }
                        if (listClientePedidos.get(i).getEstadoPedido()==2){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient01));
                        }




                        Marker mm= mapa.addMarker(marker);

                        mm.setTag(mov);

                    }
                }
            }


        }

        mapa.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(this)));
        mapa.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ClienteEntity obj=(ClienteEntity) marker.getTag();
                UtilShare.clienteMapa=obj;
                UtilShare.tipoAccion=tipoAccion;
                DataPreferences.putPrefInteger("Accion",1,getApplicationContext());
                DataPreferences.putPref("idCliente",obj.getCodigogenerado() ,getApplicationContext());
                if (tipoAccion==0){
                    DataPreferences.putPrefInteger("Accion",1,getApplicationContext());
                    DataPreferences.putPref("idCliente",obj.getCodigogenerado() ,getApplicationContext());
                           /* Fragment frag = new CreatePedidoFragment(1);
                            switchFragment(frag,"CREATE_PEDIDOS");*/

                    onBackPressed();
                }
                if (tipoAccion==1){
                    DataPreferences.putPrefInteger("Accion",2,getApplicationContext());
                    DataPreferences.putPref("idCliente",obj.getCodigogenerado() ,getApplicationContext());
                           /* Fragment frag = new CreatePedidoFragment(1);
                            switchFragment(frag,"CREATE_PEDIDOS");*/
                    onBackPressed();
                }
            }

        });
        //  mClusterManager.cluster();
        if (lisClientes.size()>0){
            //mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lisClientes.get(0).getLatitud(),lisClientes.get(0).getLongitud()), 15));
            LatLng ubicacion=ObtenerUbicacion();
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
        }


    }

    public void Dibujarzonas(){
        int idRepartidor=DataPreferences.getPrefInt("idrepartidor",this);
        try {
            List<ZonasEntity> lisZona=viewModelZonas.getZonaByRepartidor(idRepartidor);

            for (int i = 0; i < lisZona.size(); i++) {

                List<PointEntity> lisPoint=viewmodelPoint.getPoint(lisZona.get(i).getLanumi());
                if (lisPoint.size()>0){
                     List<LatLng> latLngList = new ArrayList<>();
                    for (int j = 0; j < lisPoint.size(); j++) {

                        latLngList.add(new LatLng(lisPoint.get(i).getLatitud(),lisPoint.get(i).getLongitud()));

                    }
                    latLngList.add(new LatLng(lisPoint.get(0).getLatitud(),lisPoint.get(0).getLongitud()));
                    Polygon polygon = null;
                    if (polygon != null ) polygon.remove(); // remove the previously drawn polygon

                  /* Polygon polygon1 = mapa.addPolygon(new PolygonOptions()
                            .add(new LatLng(lisPoint.get(0).getLatitud(), lisPoint.get(0).getLongitud()),
                                    new LatLng(lisPoint.get(1).getLatitud(), lisPoint.get(1).getLongitud()),
                                    new LatLng(lisPoint.get(2).getLatitud(), lisPoint.get(2).getLongitud()),
                                    new LatLng(lisPoint.get(3).getLatitud(), lisPoint.get(3).getLongitud()),
                                    new LatLng(lisPoint.get(4).getLatitud(), lisPoint.get(4).getLongitud()),
                                    new LatLng(lisPoint.get(5).getLatitud(), lisPoint.get(5).getLongitud()),
                                    new LatLng(lisPoint.get(6).getLatitud(), lisPoint.get(6).getLongitud()),
                                    new LatLng(lisPoint.get(7).getLatitud(), lisPoint.get(7).getLongitud()),
                                    new LatLng(lisPoint.get(8).getLatitud(), lisPoint.get(8).getLongitud()))
                            .strokeColor(Color.RED)
                            .fillColor(Color.BLUE));*/


                    PolygonOptions polygonOptions = new PolygonOptions();

                    for (int j = 0; j < lisPoint.size(); j++) {
                        polygonOptions.add(new LatLng(lisPoint.get(j).getLatitud(),lisPoint.get(j).getLongitud()));

                    }


                   polygonOptions.strokeColor(Color.RED);
                    //polygonOptions.fillColor(0x7F00FF00);
                    polygonOptions.fillColor(Color.argb(30, 50, 0, 255));
                    polygonOptions.strokeWidth(4);
                    Polygon polygon1 = mapa.addPolygon(polygonOptions);




                 /* Polyline polyline = mapa.addPolyline(new PolylineOptions()
                            .addAll(latLngList)
                            .width(10)
                            .color(Color.BLUE)
                            .geodesic(true));*/
                    CameraPosition cameraPosition;
                   // cameraPosition = new CameraPosition.Builder().target(new LatLng(-27.457,153.040)).zoom(15).build();



                   cameraPosition = new CameraPosition.Builder().target(new LatLng(latLngList.get(0).latitude,latLngList.get(0).longitude)).zoom(15).build();
                    mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
            }



        } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }

    }
    public void switchFragment(final Fragment frag, final String tag){
        switchHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    if (tag!= Constantes.TAG_CLIENTES  && tag!=Constantes.TAG_PEDIDOS ){
                        ft.setCustomAnimations(R.transition.left_in,R.transition.left_out);
                    }
                    ft.addToBackStack(tag)
                            .replace(R.id.fragment, frag,tag)
                            .commit();
                }catch(Exception e){}
            }
        }, 100);
    }
public LatLng ObtenerUbicacion(){

    for (int i = 0; i < lisClientes.size(); i++) {
        ClienteEntity cliente=lisClientes.get(i);
        if (cliente!=null){
            if (cliente.getLatitud()!=0){
                return new LatLng(cliente.getLatitud(),cliente.getLongitud());
            }
        }

    }
    return new LatLng(-17.3910882,-66.1895404);
}
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

      /*  mapa.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(this)));*/
        /*mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int posicion=Integer.parseInt(marker.getTitle());
                UtilShare.cliente=lisClientes.get(posicion);
                return false;
            }
        });*/

    }
    public List<LatLng> ConvertirListLatng(List<ClienteEntity> list){
        List<LatLng> ListPoint=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ClienteEntity cliente=list.get(i);
            if (cliente.getLatitud()!=0 && ! ExistePoint(ListPoint,new LatLng(cliente.getLatitud(),cliente.getLongitud()))){
                ListPoint.add(new LatLng(cliente.getLatitud(),cliente.getLongitud()));
            }
        }
        return ListPoint;
    }

    public boolean ExistePoint(List<LatLng> lista,LatLng punto){
        for (int i = 0; i < lista.size(); i++) {
            LatLng data=lista.get(i);
            if (data.latitude==punto.latitude&& data.longitude==punto.longitude){
                return true;
            }
        }
        return false;
    }
    public boolean _prCheckstatus(){
        boolean GpsStatus=false;

        LocationManager locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(GpsStatus == true)
        {

            Toast.makeText(this, "Activado GPS", Toast.LENGTH_SHORT);
            return true;
        }else {

            Toast.makeText(this, "DesActivado GPS", Toast.LENGTH_SHORT);
            return false;
        }}
    public void requestDirection() {


        List<LatLng> listFinal=new ArrayList<>();
        //////

        if(Dlocation==null){
            BanderGps=true;
            progresdialog.show();
            // Toast.makeText(this,"Ubicacion no capturada.. \n Por favor Vuelva a intentarlo",Toast.LENGTH_LONG).show();
        }else{
            Location puntoA=new Location("loca001");
            puntoA.setLatitude(Dlocation.getLatitude());
            puntoA.setLongitude(Dlocation.getLongitude());

            List<LatLng> ListaPuntos=ConvertirListLatng(lisClientes);
            LatLng puntoInicial=new LatLng(Dlocation.getLatitude(),Dlocation.getLongitude());
            originMarkers.add(mapa.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient01 ))
                    .title("Punto Inicial")
                    .position(new LatLng(puntoInicial.latitude,puntoInicial.longitude))));
            while (listFinal.size()<ListaPuntos.size()){
                Location puntoMenor=new Location("Test");
                float distanciamenor=Float.MAX_VALUE;
                for (int j = 0; j < ListaPuntos.size(); j++) {
                    Location puntoB =new Location("loca002");
                    puntoB.setLatitude(ListaPuntos.get(j).latitude);
                    puntoB.setLongitude(ListaPuntos.get(j).longitude);
                    if (!verificar(listFinal,puntoB)&& ListaPuntos.get(j).latitude!=0){
                        float distancia=puntoA.distanceTo(puntoB);
                        if (distanciamenor>=distancia){
                            distanciamenor=distancia;
                            puntoMenor=puntoB;
                        }
                    }

                }

                listFinal.add(new LatLng(puntoMenor.getLatitude(),puntoMenor.getLongitude()));
                puntoA=puntoMenor;


            }
            LatLng FinalPoint=listFinal.get(listFinal.size()-1);
            listFinal.remove(listFinal.size()-1);
            Snackbar.make(btnCargar, "Obteniendo Dirección...", Snackbar.LENGTH_SHORT).show();
            GoogleDirectionConfiguration.getInstance().setLogEnabled(true);
            GoogleDirection.withServerKey(serverKey)
                    .from(puntoInicial)
                    .and(listFinal)
                    .to(FinalPoint)
                    .transportMode(TransportMode.DRIVING)
                    .execute(this);
        }

    }
    public boolean verificar(List<LatLng> listPuntos, Location a){

        for (int i = 0; i < listPuntos.size(); i++) {
            if (a.getLatitude()==listPuntos.get(i).latitude && a.getLongitude()==listPuntos.get(i).longitude){
                return true;
            }
        }
        return false;
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        Snackbar.make(btnCargar, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            int legCount = route.getLegList().size();
            for (int index = 0; index < legCount; index++) {
                Leg leg = route.getLegList().get(index);
               /* mapa.addMarker(new MarkerOptions().position(leg.getStartLocation().getCoordination()));
                if (index == legCount - 1) {
                    mapa.addMarker(new MarkerOptions().position(leg.getEndLocation().getCoordination()));
                }*/
                List<Step> stepList = leg.getStepList();
                ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, R.color.navy, 3, Color.BLUE);
                for (PolylineOptions polylineOption : polylineOptionList) {
                    mapa.addPolyline(polylineOption);
                }
            }
            setCameraWithCoordinationBounds(route);
           // btnCargar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(btnCargar, t.getMessage(), Snackbar.LENGTH_SHORT).show();
    }
    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mapa.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    public void _prGpsObtener() {
        try {
            LocationManager locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                Log.i("location", String.valueOf(loc.getLongitude()));
                Log.i("location", String.valueOf(loc.getLatitude()));
                Dlocation=loc;
            }
            LocationListener locListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                   /* Log.i("location", String.valueOf(location.getLongitude()));
                    Log.i("location", String.valueOf(location.getLatitude()));
*/                    Dlocation=location;
                    if(BanderGps==true){
                        try
                        {
                            progresdialog.dismiss();
                            BanderGps=false;
                            requestDirection();
                        }catch (Exception e){

                        }


                    }

                }

                public void onProviderDisabled(String provider) {
                    // Log.i("info", "Provider OFF");
                }

                public void onProviderEnabled(String provider) {
                    //   Log.i("info", "Provider ON");
                    _prObtenerUbicacion();

                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                   /* Log.i("LocAndroid", "Provider Status: " + status);
                    Log.i("info", "Provider Status: " + status);*/
                }
            };
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, locListener);
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, locListener);
        }
        catch(Exception e) {
            Log.e("ERROR", "Error: " + e);
        }
        finally {
            Log.i("INFO", "Salimos de onCreate");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            _prGpsObtener();
        }
    }
    public void _prObtenerUbicacion(){
        LocationManager locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc != null) {
            Dlocation=loc;
        }

    }
}
