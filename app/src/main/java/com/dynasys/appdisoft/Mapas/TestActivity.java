package com.dynasys.appdisoft.Mapas;

import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.config.GoogleDirectionConfiguration;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.dynasys.appdisoft.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, DirectionCallback {
    private Button btnRequestDirection;
    private GoogleMap googleMap;
    private String serverKey = "AIzaSyDRwwdFpU2VpMvsfw6igVL61iZj7eP5NJQ";
    private LatLng park = new LatLng(-17.8558763, -63.2023661);
    private LatLng shopping = new LatLng(-17.8563868, -63.1960196);  /////Mercado PAraiso
    private LatLng dinner = new LatLng(-17.8509314, -63.1924776);  //// Surtidor Palmasola
    private LatLng gallery = new LatLng(-17.8478742, -63.2061929);  ////La glorieta
    private List<LatLng> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        btnRequestDirection = findViewById(R.id.btn_request_direction);
        btnRequestDirection.setOnClickListener(this);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_request_direction) {
            requestDirection();
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
    public void requestDirection() {
       // list.add(park);
        list.add(gallery);
        list.add(shopping);
        list.add(dinner);
        List<LatLng> listFinal=new ArrayList<>();
        listFinal.add(park);
        //////
        Location puntoA=new Location("loca001");
        puntoA.setLatitude(park.latitude);
        puntoA.setLongitude(park.longitude);
        while (listFinal.size()<=list.size()){
           Location puntoMenor=new Location("Test");
           float distanciamenor=Float.MAX_VALUE;
            for (int j = 0; j < list.size(); j++) {
                 Location puntoB =new Location("loca002");
                 puntoB.setLatitude(list.get(j).latitude);
                 puntoB.setLongitude(list.get(j).longitude);
                 if (!verificar(listFinal,puntoB)){
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
        Snackbar.make(btnRequestDirection, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        GoogleDirectionConfiguration.getInstance().setLogEnabled(true);
        GoogleDirection.withServerKey(serverKey)
                .from(park)
                .and(listFinal)
                .to(FinalPoint)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            int legCount = route.getLegList().size();
            for (int index = 0; index < legCount; index++) {
                Leg leg = route.getLegList().get(index);
                googleMap.addMarker(new MarkerOptions().position(leg.getStartLocation().getCoordination()));
                if (index == legCount - 1) {
                    googleMap.addMarker(new MarkerOptions().position(leg.getEndLocation().getCoordination()));
                }
                List<Step> stepList = leg.getStepList();
                ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.RED, 3, Color.BLUE);
                for (PolylineOptions polylineOption : polylineOptionList) {
                    googleMap.addPolyline(polylineOption);
                }
            }
            setCameraWithCoordinationBounds(route);
            btnRequestDirection.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(btnRequestDirection, t.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }
}
