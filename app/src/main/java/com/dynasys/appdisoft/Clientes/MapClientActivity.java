package com.dynasys.appdisoft.Clientes;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dynasys.appdisoft.Clientes.Mapas.DirectionFinder;
import com.dynasys.appdisoft.Clientes.Mapas.DirectionFinderListener;
import com.dynasys.appdisoft.Clientes.Mapas.Route;
import com.dynasys.appdisoft.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

public class MapClientActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mapa;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Location Dlocation=null;
    private ProgressDialog progresdialog;
    public boolean BanderGps =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapFragment.getMapAsync(this);
        progresdialog=new ProgressDialog(this);
        progresdialog.setCancelable(false);
        progresdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresdialog.setIndeterminate(false);
        Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent),
                PorterDuff.Mode.SRC_IN);
        progresdialog.setIndeterminateDrawable(drawable);
        progresdialog.setMessage("Obteniendo Ubicacion...........");

        btnFindPath = (Button) findViewById(R.id.btnFindPath);


        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(_prCheckstatus()){
                    sendRequest();
                }else{
                    createSimpleDialog().show();


                }

            }
        });

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        _prGpsObtener();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            _prGpsObtener();
        }
    }
    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Espere por favor.",
                "Cargando ruta..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 12));

            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

          /*  MarkerOptions marker = new MarkerOptions().position(route.startLocation);

// Changing marker icon
            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.markera));

            mapa.addMarker(marker);*/


            mapa.clear();

            originMarkers.add(mapa.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient02))
                    .title(route.startAddress)
                    .position(route.startLocation)));

            destinationMarkers.add(mapa.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient01))
                    .title("1")
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).color(Color.rgb(255,160,0)).width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mapa.addPolyline(polylineOptions));
        }
        mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng sydney ;
                /*if (DataOrder.cliente.getLatitud()!=null && DataOrder.cliente.getLongitud()!=null){
                    sydney = new LatLng(Double.parseDouble(""+DataOrder.cliente.getLatitud()),Double.parseDouble(""+DataOrder.cliente.getLongitud()));
                }else{*/
                    sydney = new LatLng(UtilShare.cliente.getLatitud(), UtilShare.cliente.getLongitud());
                //}

                if(marker.getTitle().equals("1")){
                    return false;
                }else{
                    return true;
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        LatLng sydney ;
        /*if (DataOrder.cliente.getLatitud()!=null && DataOrder.cliente.getLongitud()!=null){
            sydney = new LatLng(Double.parseDouble(""+DataOrder.cliente.getLatitud()),Double.parseDouble(""+DataOrder.cliente.getLongitud()));
        }else{*/
            sydney = new LatLng(UtilShare.cliente.getLatitud(),UtilShare.cliente.getLongitud());
        //}


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
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
// create marker
        MarkerOptions marker = new MarkerOptions().position(sydney);

// Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient01));
        mapa.addMarker(marker);
        mapa.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(this)));
       /* mapa.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ClickItemImageIglesia();
            }
        });*/
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
*/  Dlocation=location;
                    if(BanderGps==true){
                        try
                        {
                            progresdialog.dismiss();
                            BanderGps=false;
                            sendRequest();
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
    private void sendRequest() {
        try {

            if(Dlocation==null){
                BanderGps=true;
                progresdialog.show();
                // Toast.makeText(this,"Ubicacion no capturada.. \n Por favor Vuelva a intentarlo",Toast.LENGTH_LONG).show();
            }else{
                /*if (DataOrder.cliente.getLatitud()!=null && DataOrder.cliente.getLongitud()!=null){
                    new DirectionFinder(this,""+Dlocation.getLatitude() +" , "+Dlocation.getLongitude(), ""+DataOrder.cliente.getLatitud()+","+DataOrder.cliente.getLongitud()).execute();
                }else{*/
                    new DirectionFinder(this,""+Dlocation.getLatitude() +" , "+Dlocation.getLongitude(), UtilShare.cliente.getLatitud().toString()+","+UtilShare.cliente.getLongitud()).execute();
                //}



            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


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
}
