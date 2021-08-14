package com.dynasys.appdisoft.Login;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.ApiManagerExcel;
import com.dynasys.appdisoft.Login.Cloud.Bodylogin;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DB.Entity.ZonasEntity;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.ZonaListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements LoginMvp.Presenter {
    private final LoginMvp.View mLoginView;
    private final Context mContext;
    private final ZonaListViewModel viewModelZonas;
String SPREAD_SHEET_ID = "1jBtXZdoxIYJlEAnJ8YbQ3NbUmPrBFqgtSbmMHMIQMck";
    String  sheetInJsonURL = "https://script.google.com/macros/s/AKfycbw8cnwZw4TimA5H0s7qulTNvPGGHa1XUvClsAaISCcdHzd-qXryZLHBnGo1fSydbtTelQ/exec?spreadsheetId=1EmBwkEo1hAXuJ_IjdM6Af-fCqe8cTXgwlRR6Ny3LiAI&sheet=";
    public LoginPresenter(LoginMvp.View loginView,Context context,ZonaListViewModel zonamodel){
        mLoginView = Preconditions.checkNotNull(loginView);
        viewModelZonas=zonamodel;
        mLoginView.setPresenter(this);
        this.mContext=context;
    }


    @Override
    public void ValidarLogin(final String codigo, final String nroDocumento) {
        if ( codigo.trim().isEmpty()){
       mLoginView.showEmailError();
       return;
        }
        if ( nroDocumento.trim().isEmpty()){
       mLoginView.showPasswordError();
       return;
        }
      if (! isOnline()){
          mLoginView.ShowMessageResult("Sin Conexión. Por favor conectarse a una red");
          return;
      }

        //ApiManagerExcel.apiManager=null;
       // ApiManagerExcel apiManager=ApiManagerExcel.getInstance(mContext);

      //  String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=39.476245,-0.349448&sensor=true";

       /* String content="AKfycbw8cnwZw4TimA5H0s7qulTNvPGGHa1XUvClsAaISCcdHzd-qXryZLHBnGo1fSydbtTelQ/exec?spreadsheetId=1EmBwkEo1hAXuJ_IjdM6Af-fCqe8cTXgwlRR6Ny3LiAI&sheet=";
        apiManager.ObtenerFechaCaducidad(content,new Callback<List<FechaCaducidad>>() {
            @Override
            public void onResponse(Call<List<FechaCaducidad>> call, Response<List<FechaCaducidad>> response) {
                List<FechaCaducidad> responseUser = response.body();
                if (response.code()==404){
                    Login(codigo,nroDocumento);
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {

                } else {
                    if (responseUser!=null ){

                       /* if (Calendar.getInstance().getTime().before(responseUser.get(0).getFecha())){
                            mLoginView.ShowMessageResult("La Fecha de Uso De esta Aplicacion Ha Caducado. Contactese con su Proveedor de App");
                        }else{
                            Login(codigo,nroDocumento);
                            return;
                        }*/
/*
                    }else{
                        Login(codigo,nroDocumento);

                        return;
                    }

                }
            }

            @Override
            public void onFailure(Call<List<FechaCaducidad>> call, Throwable t) {
                Login(codigo,nroDocumento);
            }
        });*/

        Login(codigo,nroDocumento);


    }

    public void Login(String codigo, String nroDocumento){
        Bodylogin blogin=new Bodylogin( codigo,  nroDocumento);
        ApiManager.apiManager=null;
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.LoginUser(blogin, new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin responseUser = response.body();
                if (response.code()==404){
                    mLoginView.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {

                    if (responseUser.getCode()==4){
                        mLoginView.ShowMessageResult(responseUser.getMessage());
                    }else{
                        DataPreferences.putPrefLogin("isLogin",true,mContext);
                        DataPreferences.putPref("repartidor",responseUser.getToken(),mContext);
                        DataPreferences.putPrefInteger("idrepartidor",responseUser.getId(),mContext);
                        DataPreferences.putPrefInteger("zona",responseUser.getZona(),mContext);
                        DataPreferences.putPrefInteger("EditarPedidos",responseUser.getPedido(),mContext);
                        DataPreferences.putPrefInteger("ViewRuta",responseUser.getMapa(),mContext);
                        DataPreferences.putPrefInteger("UpdateCliente",responseUser.getUpdate_cliente(),mContext);
                        DataPreferences.putPrefInteger("CategoriaRepartidor",responseUser.getCategoria(),mContext);
                        DataPreferences.putPrefInteger("stock",responseUser.getStock(),mContext);
                        DataPreferences.putPrefInteger("ViewCredito",responseUser.getView_credito() ,mContext);
                        DataPreferences.putPrefInteger("CantidadProducto",responseUser.getCantidad_producto(),mContext);
                        DataPreferences.putPrefInteger("ValidarZona",responseUser.getValidarZona(),mContext);

                        _DescargarZonas(""+responseUser.getId());
                        mLoginView.LoginSuccesfull();
                    }

                } else {
                    if (responseUser!=null){
                        mLoginView.ShowMessageResult(responseUser.getMessage());
                    }else{
                        mLoginView.ShowMessageResult("Hubo un Error en la respuesta del web service null");
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                mLoginView.ShowMessageResult("No es posible conectarse con el servicio.");
            }
        });


    }
    public void _DescargarZonas(String idRepartidor){
        DataPreferences.putPrefInteger("Zonas",-1,mContext);
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.ObtenerZonas( idRepartidor.trim(),new Callback<List<ZonasEntity>>() {
            @Override
            public void onResponse(Call<List<ZonasEntity>> call, Response<List<ZonasEntity>> response) {
                final List<ZonasEntity> responseUser = (List<ZonasEntity>) response.body();
                if (response.code()==404){
                    mLoginView.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {

                    for (int i = 0; i < responseUser.size(); i++) {
                        ZonasEntity zona=responseUser.get(i);
                        viewModelZonas.insertZona(zona);
                    }


                } else {
                    mLoginView.ShowMessageResult("No se pudo Obtener Datos del Servidor para Clientes");
                }
            }

            @Override
            public void onFailure(Call<List<ZonasEntity>> call, Throwable t) {
                mLoginView.ShowMessageResult("No es posible conectarse con el servicio.");
            }
        });
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
