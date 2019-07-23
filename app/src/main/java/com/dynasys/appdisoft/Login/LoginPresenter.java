package com.dynasys.appdisoft.Login;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.Bodylogin;
import com.dynasys.appdisoft.Login.Cloud.MainApplication;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.google.common.base.Preconditions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements LoginMvp.Presenter {
    private final LoginMvp.View mLoginView;
    private final Context mContext;
    public LoginPresenter(LoginMvp.View loginView,Context context){
        mLoginView = Preconditions.checkNotNull(loginView);
        mLoginView.setPresenter(this);
        this.mContext=context;
    }
    @Override
    public void ValidarLogin(String codigo, String nroDocumento) {
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
        mLoginView.showDialogs();

        Bodylogin blogin=new Bodylogin(codigo,nroDocumento);
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
                                mLoginView.LoginSuccesfull();
                            }

                        } else {
                            mLoginView.ShowMessageResult(responseUser.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
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
