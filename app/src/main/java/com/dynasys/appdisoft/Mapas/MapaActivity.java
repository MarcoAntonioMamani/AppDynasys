package com.dynasys.appdisoft.Mapas;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.dynasys.appdisoft.Clientes.CustomInfoWindowAdapter;
import com.dynasys.appdisoft.Clientes.TipoMapa;
import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mapa;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<ClienteEntity> lisClientes = new ArrayList<>();
    private List<ClienteEntity> lisClientesBackup = new ArrayList<>();
    private ClientesListViewModel viewModelCliente;
    private List<PedidoEntity> listPedidos=new ArrayList<>();
    private PedidoListViewModel viewModelPedido;
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
               CargarClientes(listaTipos.getSelectedItemPosition());
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
    public void CargarClientes(int tipo){

        if (tipo==0){
            try {
                lisClientes=viewModelCliente.getMAllCliente(0);
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
        mapa.setOnCameraChangeListener(mClusterManager);

        for (int i = 0; i < lisClientes.size(); i++) {

            ClienteEntity mov=lisClientes.get(i);
            if (mov.getLongitud()!=0){
               /* originMarkers.add(mapa.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient02 ))
                        .title(""+i)
                        .position(new LatLng(mov.getLatitud(),mov.getLongitud()))));*/
                mClusterManager.addItem(new StringClusterItem("" + (i), new LatLng(mov.getLatitud(),mov.getLongitud())));

            }
        }
        mClusterManager.cluster();
        if (lisClientes.size()>0){
            //mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lisClientes.get(0).getLatitud(),lisClientes.get(0).getLongitud()), 15));
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lisClientes.get(0).getLatitud(),lisClientes.get(0).getLongitud()), 15));
        }

        final CustomClusterRenderer renderer = new CustomClusterRenderer(this, mapa, mClusterManager);
        mClusterManager.setRenderer(renderer);
        mClusterManager.getMarkerCollection()
                .setOnInfoWindowAdapter(new CustomInfoWindowAdapterMapa(LayoutInflater.from(this)));

        mapa.setInfoWindowAdapter(mClusterManager.getMarkerManager());

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
