package com.dynasys.appdisoft.Visitas.Create.SincronizarData;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.AlmacenListaViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.CobranzaDetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.CobranzaListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.DescuentosListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.ZonasEntity;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.DeudaListaViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.PointListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.PreciosListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.ProductoViewListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.StockListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.VisitaListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.ZonaListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.ShareUtil.LocationGeo;
import com.dynasys.appdisoft.ShareUtil.ServiceSincronizacion;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClientesListViewModel;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.Presentacion.SincronizarMvp;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.Presentacion.SincronizarPresenter;
import com.google.common.base.Preconditions;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class SincronizarFragment extends Fragment implements SincronizarMvp.View {
    CheckBox checkTodo;
    CheckBox checkCliente;
    CheckBox checkProducto,checkPedidos;
    CheckBox checkPrecio;
    CheckBox checkTodasZonas;
    Button btnSincronizar;
    Spinner SpinnerZona;
    private SincronizarMvp.Presenter mSincronizarPresenter;
    private ClientesListViewModel viewModel;
    private PreciosListViewModel viewModelPrecio;
    private ProductosListViewModel viewModelProducto;
    private PedidoListViewModel viewModelPedidos;
    private DetalleListViewModel viewModelDetalle;
    private StockListViewModel viewModelStock;
    private DescuentosListViewModel viewModelDescuento;
    private ProductoViewListViewModel viewModelListadoProducto;
    private VisitaListViewModel viewModelVisita;
    private PointListViewModel viewModelPoint;
    private AlmacenListaViewModel viewModelAlmacen;
    private DeudaListaViewModel viewModelDeuda;
    private CobranzaListViewModel viewModelCobranza;
    private CobranzaDetalleListViewModel viewModelCobranzaDetalle;
    LottieAlertDialog alertDialog;
    List<ZonasEntity> listZonas;
    ZonasEntity mZonaSelected;
    private ZonaListViewModel viewModelZona;
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
        checkTodasZonas=rootView.findViewById(R.id.view_sinc_zonas );
        btnSincronizar=rootView.findViewById(R.id.id_sync_btn_sync);
        checkPedidos=rootView.findViewById(R.id.view_sinc_pedidos);
        SpinnerZona=rootView.findViewById(R.id.id_spinner_zona);
        viewModel = ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        viewModelPrecio = ViewModelProviders.of(getActivity()).get(PreciosListViewModel.class);
        viewModelProducto = ViewModelProviders.of(getActivity()).get(ProductosListViewModel.class);
        viewModelPedidos = ViewModelProviders.of(getActivity()).get(PedidoListViewModel.class);
        viewModelDetalle = ViewModelProviders.of(getActivity()).get(DetalleListViewModel.class);
        viewModelStock=ViewModelProviders.of(getActivity()).get(StockListViewModel.class);
        viewModelDescuento=ViewModelProviders.of(getActivity()).get(DescuentosListViewModel.class);
        viewModelZona =ViewModelProviders.of(getActivity()).get(ZonaListViewModel.class);
        viewModelListadoProducto=ViewModelProviders.of(getActivity()).get(ProductoViewListViewModel.class);
        viewModelVisita=ViewModelProviders.of(getActivity()).get(VisitaListViewModel.class);
        viewModelPoint=ViewModelProviders.of(getActivity()).get(PointListViewModel.class);
        viewModelDeuda=ViewModelProviders.of(getActivity()).get(DeudaListaViewModel.class);
        viewModelCobranza=ViewModelProviders.of(getActivity()).get(CobranzaListViewModel.class);
        viewModelCobranzaDetalle=ViewModelProviders.of(getActivity()).get(CobranzaDetalleListViewModel.class);
        viewModelAlmacen=ViewModelProviders.of(getActivity()).get(AlmacenListaViewModel.class);
      /*  NoteEntity note = new NoteEntity(inputNote.getText().toString());
        viewModel.insertNote(note);*/
        new SincronizarPresenter(this,getContext(),viewModel,getActivity()
                ,viewModelPrecio,viewModelProducto,viewModelPedidos,viewModelDetalle,viewModelStock,viewModelDescuento,
                viewModelListadoProducto,viewModelVisita,viewModelPoint,viewModelDeuda,viewModelCobranza,viewModelCobranzaDetalle,viewModelAlmacen);
checkTodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            MarcarDesmarcarTodos(b);
        }
        });
        CargarZonas();

        checkTodasZonas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true){
                    SpinnerZona.setVisibility(View.GONE);
                }else{
                    SpinnerZona.setVisibility(View.VISIBLE);
                }
            }
        });
        OnclickButton();
        ShowDialogSincronizando();
       /* if (ServicesLocation.getInstance()==null){
            Intent intent = new Intent(getContext(), ServicesLocation.class);
            getContext().startService(intent);
        }*/

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
public void CargarZonas(){
    SpinnerZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (pos>=0 && listZonas.size()>0){
                mZonaSelected = listZonas.get(pos);
            }
        }
        public void onNothingSelected(AdapterView<?> parent) {
        }
    });
    int idRepartidor= DataPreferences.getPrefInt("idrepartidor",getContext());
    int idZonas= DataPreferences.getPrefInt("Zonas",getContext());
    try {
        listZonas=viewModelZona.getZonaByRepartidor(idRepartidor);
        ArrayAdapter<ZonasEntity> adapter =new ArrayAdapter<ZonasEntity>(getContext(), android.R.layout.simple_spinner_item, listZonas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerZona.setAdapter(adapter);
        if (idZonas!=-1){
            SpinnerZona.setSelection(ObtenerPosicionListaZona(idZonas));
            checkTodasZonas .setChecked(false);
            SpinnerZona.setVisibility(View.VISIBLE);
        }else{
            checkTodasZonas .setChecked(true);
            SpinnerZona.setVisibility(View.GONE);
        }


    } catch (ExecutionException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }


}
    public int ObtenerPosicionListaZona(int idZona){

        for (int i = 0; i < listZonas.size(); i++) {
            if (listZonas.get(i).getLanumi()==idZona){
                return i;
            }
        }
        return -1;
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
                    if (mZonaSelected==null){
                        UtilShare .tvZona.setText("Todas Las Zonas");
                        mSincronizarPresenter.GuadarDatos(checkProducto.isChecked() ,checkPrecio.isChecked(),
                                checkCliente.isChecked(),checkPedidos.isChecked(),checkTodasZonas .isChecked(),-1);
                    }else{
                        UtilShare .tvZona.setText(mZonaSelected.getZona());
                        mSincronizarPresenter.GuadarDatos(checkProducto.isChecked() ,checkPrecio.isChecked(),
                                checkCliente.isChecked(),checkPedidos.isChecked(),checkTodasZonas .isChecked(),mZonaSelected.getLanumi());
                    }


                }
            }, 1 * 2000);
            super.onPostExecute(result);
        }
    }
}
