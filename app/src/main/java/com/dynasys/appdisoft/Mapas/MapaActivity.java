package com.dynasys.appdisoft.Mapas;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.config.GoogleDirectionConfiguration;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.dynasys.appdisoft.Clientes.TipoMapa;
import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback , DirectionCallback {
    GoogleMap mapa;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<ClienteEntity> lisClientes = new ArrayList<>();
    private List<ClienteEntity> lisClientesBackup = new ArrayList<>();
    private ClientesListViewModel viewModelCliente;
    private List<PedidoEntity> listPedidos=new ArrayList<>();
    private PedidoListViewModel viewModelPedido;
    private String serverKey = "AIzaSyDRwwdFpU2VpMvsfw6igVL61iZj7eP5NJQ";
    private ClusterManager<StringClusterItem> mClusterManager;
    private Spinner listaTipos;
    private Button btnCargar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        listaTipos =findViewById(R.id.id_lista_Tipos);
        btnCargar=findViewById(R.id.btnFindPathall);
        viewModelCliente = ViewModelProviders.of(this).get(ClientesListViewModel.class);
        viewModelPedido = ViewModelProviders.of(this).get(PedidoListViewModel.class);
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
                requestDirection();
            }
        });

    }

    public void _CargarTipos(){
        List<TipoMapa> list=new ArrayList<>();
        list.add(new TipoMapa(1,"Ver Clientes"));
        list.add(new TipoMapa(2,"Ver Pedidos Pendientes"));
        list.add(new TipoMapa(3,"Ver Pedidos Entregados"));

        ArrayAdapter<TipoMapa> adapter =new ArrayAdapter<TipoMapa>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listaTipos.setAdapter(adapter);

    }
    public List<ClienteEntity> FiltarByZona(List<ClienteEntity> list){

        int idzona= DataPreferences.getPrefInt("zona",this);
        List<ClienteEntity> listClie=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ClienteEntity cliente=list.get(i);
            if (cliente.getCczona() ==idzona){
                listClie.add(cliente);
            }
        }
        return listClie;
    }
    public void CargarClientes(int tipo){

        if (tipo==0){
            try {
                lisClientes=FiltarByZona(viewModelCliente.getMAllCliente(0));
                UtilShare.ListClientes=lisClientes;
                dibujarClientes();
            } catch (ExecutionException e) {

            } catch (InterruptedException e) {

            }
        }
        if (tipo==1){
            try {
                lisClientesBackup=viewModelCliente.getMAllCliente(0);
                lisClientes=new ArrayList<>();
                listPedidos=viewModelPedido.getMAllPedido(0);
                for (int i = 0; i < listPedidos.size(); i++) {
                    PedidoEntity pedi=listPedidos.get(i);
                    if (pedi.getOaest()!=3){
                        ClienteEntity cliente=obtenerCliente(pedi);
                        lisClientes.add(cliente);
                    }
                }
                UtilShare.ListClientes=lisClientes;
                dibujarClientes();


            } catch (ExecutionException e) {

            } catch (InterruptedException e) {

            }
        }

        if (tipo==2){
            try {
                lisClientesBackup=viewModelCliente.getMAllCliente(0);
                lisClientes=new ArrayList<>();
                listPedidos=viewModelPedido.getMAllPedido(0);
                for (int i = 0; i < listPedidos.size(); i++) {
                    PedidoEntity pedi=listPedidos.get(i);
                    if (pedi.getOaest()==3){
                        ClienteEntity cliente=obtenerCliente(pedi);
                        lisClientes.add(cliente);
                    }
                }
                UtilShare.ListClientes=lisClientes;
                dibujarClientes();
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
        for (int i = 0; i < lisClientes.size(); i++) {

            ClienteEntity mov=lisClientes.get(i);
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
    public void requestDirection() {


        List<LatLng> listFinal=new ArrayList<>();
        //////
        Location puntoA=new Location("loca001");
        puntoA.setLatitude(-16.50149190546621);
        puntoA.setLongitude(-68.20147946476936);

        List<LatLng> ListaPuntos=ConvertirListLatng(lisClientes);
        LatLng puntoInicial=new LatLng(-16.50149190546621,-68.20147946476936);
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
}
