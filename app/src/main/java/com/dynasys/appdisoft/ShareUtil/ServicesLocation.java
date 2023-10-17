package com.dynasys.appdisoft.ShareUtil;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.BodyLocation;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesLocation  extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


        private static final String TAG = "Tracking";
        GoogleApiClient mLocationClient;
        LocationRequest mLocationRequest = new LocationRequest();
        public static ServicesLocation mInstance;

        public static final String ACTION_LOCATION_BROADCAST = "Tracking" + "LocationBroadcast";
        public static final String EXTRA_LATITUDE = "extra_latitude";
        public static final String EXTRA_LONGITUDE = "extra_longitude";
    @Override
    public void onCreate() {
        mInstance = this;

    }
    public static ServicesLocation getInstance() {
        return mInstance;
    }

    @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            mLocationClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mLocationRequest.setInterval(60 * 1000);
            mLocationRequest.setFastestInterval(60 * 1000);


            int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
            //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


            mLocationRequest.setPriority(priority);
            mLocationClient.connect();

            //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
            return START_STICKY;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        /*
         * LOCATION CALLBACKS
         */
        @Override
        public void onConnected(Bundle dataBundle) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                Log.d(TAG, "== Error On onConnected() Permission not granted");
                //Permission not granted by user so cancel the further execution.

                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

            Log.d(TAG, "Connected to Google API");
        }

        /*
         * Called by Location Services if the connection to the
         * location client drops because of an error.
         */
        @Override
        public void onConnectionSuspended(int i) {
            Log.d(TAG, "Connection suspended");
        }

        //to get the location change
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "Location changed");

            Log.d(TAG,"lat:"+ location.getLatitude()+"   long:"+location.getLongitude());
            int codigoRepartidor=  DataPreferences.getPrefInt("idrepartidor",this);
            if (location != null) {
                ApiManager apiManager=ApiManager.getInstance(this);
                BodyLocation blocation=new BodyLocation();
                blocation.setLdchof(codigoRepartidor);
                blocation.setLdfec(Calendar.getInstance().getTime());
                blocation.setLdhora(""+Calendar.getInstance().HOUR+":"+Calendar.getInstance().MINUTE);
                blocation.setLblat(location.getLatitude());
                blocation.setLblongi(location.getLongitude());
                apiManager.InsertTracking(blocation, new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                        ResponseLogin responseUser = response.body();
                        if (response.code()==404){

                            return;
                        }
                        try{
                            if (responseUser!=null){
                                Log.d(TAG, "respuesta: "+responseUser.getMessage()+Calendar.getInstance().HOUR+":"+Calendar.getInstance().MINUTE);
                            }
                        }catch (Exception e){

                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        Log.d(TAG, "Error al enviar al servicio.");
                    }
                });
            }
        }

        private void sendMessageToUI(String lat, String lng) {

            Log.d(TAG, "Sending info...");

            Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
            intent.putExtra(EXTRA_LATITUDE, lat);
            intent.putExtra(EXTRA_LONGITUDE, lng);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.d(TAG, "Failed to connect to Google API");

        }


}
