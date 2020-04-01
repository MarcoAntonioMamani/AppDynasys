package com.dynasys.appdisoft.SincronizarData;


import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.DB.DescuentosListViewModel;
import com.dynasys.appdisoft.Login.DB.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DB.PreciosListViewModel;
import com.dynasys.appdisoft.Login.DB.StockListViewModel;
import com.dynasys.appdisoft.Login.LoginActivity;
import com.dynasys.appdisoft.Login.LoginMvp;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.ShareUtil.LocationGeo;
import com.dynasys.appdisoft.ShareUtil.ServiceSincronizacion;
import com.dynasys.appdisoft.ShareUtil.ServicesLocation;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.dynasys.appdisoft.SincronizarData.Presentacion.SincronizarMvp;
import com.dynasys.appdisoft.SincronizarData.Presentacion.SincronizarPresenter;
import com.google.common.base.Preconditions;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SincronizarFragment extends Fragment implements SincronizarMvp.View {
    CheckBox checkTodo;
    CheckBox checkCliente;
    CheckBox checkProducto,checkPedidos;
    CheckBox checkPrecio;
    Button btnSincronizar;
    private SincronizarMvp.Presenter mSincronizarPresenter;
    private ClientesListViewModel viewModel;
    private PreciosListViewModel viewModelPrecio;
    private ProductosListViewModel viewModelProducto;
    private PedidoListViewModel viewModelPedidos;
    private DetalleListViewModel viewModelDetalle;
    private StockListViewModel viewModelStock;
    private DescuentosListViewModel viewModelDescuento;
    LottieAlertDialog alertDialog;
    public SincronizarFragment() {
        // Required empty public constructor
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocationGeo.getInstance(getContext(),getActivity());
        LocationGeo.PedirPermisoApp();

    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Sincronizar");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sincronizar, container, false);
        checkTodo=rootView.findViewById(R.id.view_sinc_all);
        checkCliente=rootView.findViewById(R.id.view_sinc_cliente);
        checkProducto=rootView.findViewById(R.id.view_sinc_producto);
        checkPrecio=rootView.findViewById(R.id.view_sinc_precio);
        btnSincronizar=rootView.findViewById(R.id.id_sync_btn_sync);
        checkPedidos=rootView.findViewById(R.id.view_sinc_pedidos);
        viewModel = ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        viewModelPrecio = ViewModelProviders.of(getActivity()).get(PreciosListViewModel.class);
        viewModelProducto = ViewModelProviders.of(getActivity()).get(ProductosListViewModel.class);
        viewModelPedidos = ViewModelProviders.of(getActivity()).get(PedidoListViewModel.class);
        viewModelDetalle = ViewModelProviders.of(getActivity()).get(DetalleListViewModel.class);
        viewModelStock=ViewModelProviders.of(getActivity()).get(StockListViewModel.class);
        viewModelDescuento=ViewModelProviders.of(getActivity()).get(DescuentosListViewModel.class);
      /*  NoteEntity note = new NoteEntity(inputNote.getText().toString());
        viewModel.insertNote(note);*/
        new SincronizarPresenter(this,getContext(),viewModel,getActivity(),viewModelPrecio,viewModelProducto,viewModelPedidos,viewModelDetalle,viewModelStock,viewModelDescuento);
checkTodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            MarcarDesmarcarTodos(b);
        }
        });
        OnclickButton();
        ShowDialogSincronizando();
        if (ServicesLocation.getInstance()==null){
            Intent intent = new Intent(getContext(), ServicesLocation.class);
            getContext().startService(intent);
        }

        if (ServiceSincronizacion.getInstance()==null){
        UtilShare.mActivity=getActivity();
        Intent intent = new Intent(getContext(),new ServiceSincronizacion(viewModel,getActivity()).getClass());
        getContext().startService(intent);
    }
        return rootView;
    }

    public void showDialogs() {
        ShowDialogSincronizando();
        alertDialog.show();
    }

    public void OnclickButton(){
        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (ValidarcheckSeleccionado()){
                  if (! isOnline()){
                      ShowMessageResult("Sin Conexión. Por favor conectarse a una red");
                      return;
                  }else{
                      showDialogs();
                      new ChecarNotificaciones().execute();

                  }

              }else{
                  ShowMessageResult("Error: No Existen Item seleccionado");
              }
            }
        });
}
public boolean ValidarcheckSeleccionado(){
        return checkProducto.isChecked() ||checkPrecio.isChecked()||checkCliente.isChecked()||checkPedidos.isChecked();
}

    @Override
    public void MarcarDesmarcarTodos(boolean bandera) {
      checkCliente.setChecked(bandera);
      checkPrecio.setChecked(bandera);
      checkProducto.setChecked(bandera);
      checkPedidos.setChecked(bandera);
    }

    @Override
    public void setPresenter(SincronizarMvp.Presenter presenter) {
        mSincronizarPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    public void ShowMessageResult(String message) {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        alertDialog=new LottieAlertDialog.Builder(getContext(),DialogTypes.TYPE_WARNING)
                .setTitle("Advertencia")
                .setDescription(message)
                .setPositiveText("Aceptar")
                .setPositiveButtonColor(Color.parseColor("#008ebe"))
                .setPositiveTextColor(Color.parseColor("#ffffff"))
                .setPositiveListener(new ClickListener() {
                    @Override
                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();
                    }
                }).build();
        alertDialog.show();
    }

    @Override
    public void ShowSyncroMgs(String message) {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        Snackbar snackbar= Snackbar.make(checkPrecio, message, Snackbar.LENGTH_LONG);
        View snackbar_view=snackbar.getView();
        TextView snackbar_text=(TextView)snackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_checked,0);
        snackbar_text.setGravity(Gravity.CENTER);
        snackbar.show();

    }
    private void ShowDialogSincronizando(){
       /* progresdialog=new ProgressDialog(getContext());
        progresdialog.setCancelable(false);
        progresdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresdialog.setIndeterminate(false);
        Drawable drawable = new ProgressBar(getActivity()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent),
                PorterDuff.Mode.SRC_IN);
        progresdialog.setIndeterminateDrawable(drawable);
        progresdialog.setMessage("Obteniendo Datos .....");*/

        try
        {

            alertDialog = new LottieAlertDialog.Builder(getContext(), DialogTypes.TYPE_LOADING).setTitle("Sincronizacion")
                    .setDescription("Obteniendo Datos .....")
                    .build();

            alertDialog.setCancelable(false);
        }catch (Error e){

            String d=e.getMessage();

        }

    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private class ChecarNotificaciones extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            //NUESTRO CODIGO
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mSincronizarPresenter.GuadarDatos(checkProducto.isChecked() ,checkPrecio.isChecked(),checkCliente.isChecked(),checkPedidos.isChecked());
                }
            }, 1 * 2000);
            super.onPostExecute(result);
        }
    }
}
