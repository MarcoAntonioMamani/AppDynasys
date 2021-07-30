package com.dynasys.appdisoft.ShareUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;


import com.dynasys.appdisoft.MainActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class LocationGeo {

    private static LocationGeo INSTANCE;
    private static Context mContext;
    private static LocationManager locationManager;
    private static List<String> providersActivos;
    private static LocationListener locationListener;
    private static long minTimeMillis = 0;
    private static long minDistanceMeters = 0;
private static Activity activity;
    private static Location locationActual;


    private LocationGeo(Context mContext,Activity activity) {
        this.mContext = mContext;
        this.activity=activity;
    }

    public static LocationGeo getInstance(Context mContext, Activity act) {
        if (INSTANCE == null) {
            INSTANCE = new LocationGeo(mContext,activity);
            activity=act;
        }
        return INSTANCE;
    }
    public static Boolean Encontrado(List<LatLng> points, LatLng punto) {
        int counter = 0;
        int i;
        Double xinters;
        LatLng P1 = new LatLng(0, 0);
        LatLng P2 = new LatLng(0, 0);
        int n = points.size();
        P1 = points.get(0);
        int restod;
        for (i = 1; (i <= n); i++) {
            restod = (i % n);
            P2 = points.get(restod);
            if ((punto.longitude > Math.min(P1.longitude, P2.longitude))) {
                if ((punto.longitude <= Math.max(P1.longitude, P2.longitude))) {
                    if ((punto.latitude <= Math.max(P1.latitude, P2.latitude))) {
                        if ((P1.longitude != P2.longitude)) {
                            xinters = (((punto.longitude - P1.longitude)
                                    * ((P2.latitude - P1.latitude)
                                    / (P2.longitude - P1.longitude)))
                                    + P1.latitude);
                            if (((P1.latitude == P2.latitude)
                                    || (punto.latitude <= xinters))) {
                                counter = (counter + 1);
                            }

                        }

                    }

                }

            }

            P1 = P2;
        }

        if (((counter % 2)
                == 0)) {
            //MsgBox("Fuera");
            return false;
        }
        else {
            return true;
            //MsgBox("Dentro");
        }

    }

    public static void cerrarGPS() {
        if (providersActivos == null || providersActivos.size() <= 0) {
            return;
        }
        for (String provider : providersActivos) {
            locationManager.removeUpdates(locationListener);
        }
    }
public static void PedirPermisoApp(){
    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
    }
    locationManager = (LocationManager) activity
            .getSystemService(Context.LOCATION_SERVICE);
    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 120);
        return;
    }
    Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    if (loc != null) {
        locationActual = loc;
    }
}
    public static void iniciarGPS() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        locationManager = (LocationManager) activity
                .getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 120);
            return;
        }
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc != null) {
            locationActual = loc;
        }
        if (!estaActivoGPS()) {
            mostrarOpcionesPGS(mContext);
        }
        providersActivos = locationManager.getProviders(true);
        locationListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    try {
                        locationActual = location;
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            }
        };

            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            try{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeMillis,
                        minDistanceMeters, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTimeMillis, minDistanceMeters,  locationListener);
            }catch (Exception e){

            }



    }

    public static Boolean estaActivoGPS() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public static void mostrarOpcionesPGS(Context context) {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                createDialogGps(context).show();
            }catch (Exception e){

            }

        }
    }
    static AlertDialog createDialogGps(final Context context) {
        //AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Usuario:")
                .setMessage("Su Gps esta desactivado \n Desea Activarlo? .....")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent I = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(I);
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
    public static Location getLocationActual() {
            return locationActual;

    }
}
