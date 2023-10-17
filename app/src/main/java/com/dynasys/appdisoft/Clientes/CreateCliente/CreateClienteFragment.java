package com.dynasys.appdisoft.Clientes.CreateCliente;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.dynasys.appdisoft.Clientes.ListClientesFragment;
import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Constantes;
import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PointEntity;
import com.dynasys.appdisoft.Login.DB.Entity.TipoNegocioEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ZonasEntity;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.PointListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.ZonaListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.ShareUtil.LocationGeo;
import com.dynasys.appdisoft.ShareUtil.ServiceSincronizacion;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateClienteFragment extends Fragment implements OnMapReadyCallback{

    GoogleMap mapa;
    View view;
    MySupportMapFragment mSupportMapFragment;
    NestedScrollView scrollView;
    Button btngps,btnAtras,btnGuardar;
    private AlertDialog dialogs,dialogQuestion;
   Location location;
   //////Variables de los campos
   private TextInputLayout tilNombre;
    private TextInputLayout tilTelefono;
    private TextInputLayout tilDireccion;
    private TextInputLayout tilNit;
    private TextInputLayout tilRazonSocial;
    private  ClientesListViewModel viewModel;
    private ZonaListViewModel viewModelZona;
    private PedidoListViewModel viewModelPedidos;
    private ZonasEntity zonaSelected=null;
    private TipoNegocioEntity negocioSelected=null;
    private List<ZonasEntity> listZonas;
    private List<TipoNegocioEntity> listNegocios;
    private Context mContext;
    private PointListViewModel viewmodelPoint;
    ClienteEntity mCliente;
    private Spinner listaSpinnerZona,listSpinnerTipoCliente;
    private String M_Uii="";
    private int tipo=0; //// TIpo=0 = Nuevo Cliente ------------------  Tipo = 1 Modificacion Cliente
    private int  isUpdate=0;
    LottieAlertDialog alertDialog;
    public CreateClienteFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public CreateClienteFragment(int tipo, ClienteEntity cliente,int isUpdate) {
        // Required empty public constructor
        this.tipo=tipo;
        this.mCliente=cliente;
        this.isUpdate=isUpdate;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (tipo==0){
            getActivity().setTitle("Crear Cliente");
        }else{
            getActivity().setTitle("Modificar Cliente");
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getContext();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_cliente, container, false);
       scrollView=view.findViewById(R.id.id_scroll);
       btngps=(Button)view.findViewById(R.id.id_btn_ObtenerGps);
        btnAtras=(Button)view.findViewById(R.id.id_btn_cancelar);
        btnGuardar=(Button)view.findViewById(R.id.id_btn_guardar);
        listaSpinnerZona=(Spinner)view.findViewById(R.id.id_zona);
        listSpinnerTipoCliente=(Spinner)view.findViewById(R.id.id_TipoNegocio);
        tilNombre = (TextInputLayout) view.findViewById(R.id.til_nombre);
        tilTelefono = (TextInputLayout) view.findViewById(R.id.til_telefono);
        tilDireccion = (TextInputLayout) view.findViewById(R.id.til_direccion);
        viewmodelPoint=ViewModelProviders.of(this).get(PointListViewModel.class);
        tilRazonSocial=(TextInputLayout)view.findViewById(R.id.til_razonSocial);
        tilNit = (TextInputLayout) view.findViewById(R.id.til_nit);
        viewModel = ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        viewModelPedidos=ViewModelProviders.of(getActivity()).get(PedidoListViewModel.class);
        viewModelZona =ViewModelProviders.of(getActivity()).get(ZonaListViewModel.class);
        mSupportMapFragment = (MySupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mSupportMapFragment != null)
            mSupportMapFragment.setListener(new MySupportMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
            });
          //  SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mSupportMapFragment.getMapAsync(this);

        OnClickGps();
        onClickAtras();
        onClickGrabar();

        LocationGeo.getInstance(mContext,getActivity());
        LocationGeo.iniciarGPS();


        _prCargarDatos();
        M_Uii="";
        if (tipo!=0 && isUpdate==0){
            btngps.setVisibility(View.GONE);
            btnGuardar.setVisibility(View.GONE);
            tilNombre.getEditText().setEnabled(false);
            tilDireccion.getEditText().setEnabled(false);
            tilNit.getEditText().setEnabled(false);
            tilTelefono.getEditText().setEnabled(false);
        }

        listaSpinnerZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos>=0 && listZonas.size()>0){
                    zonaSelected = listZonas.get(pos);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        listSpinnerTipoCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos>=0 && listNegocios.size()>0){
                    negocioSelected = listNegocios.get(pos);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }

    public void _prCargarDatos(){



        int idZonas= DataPreferences.getPrefInt("Zonas",mContext);
        if (idZonas==-1){
            listaSpinnerZona.setVisibility(View.VISIBLE);
        }else{
            listaSpinnerZona.setVisibility(View.GONE);
        }

        int idRepartidor=DataPreferences.getPrefInt("idrepartidor",mContext);
        try {
            //// Tipo Negocio
            Gson gson=new Gson();
            Type type=new TypeToken<List<TipoNegocioEntity>>(){}.getType();
            String test=DataPreferences.getPref("tipoNegocios",getContext());
            listNegocios=gson.fromJson(DataPreferences.getPref("tipoNegocios",getContext()),type);

            ArrayAdapter<TipoNegocioEntity> adapterTipoNegocio =new ArrayAdapter<TipoNegocioEntity>(getContext(), android.R.layout.simple_spinner_item, listNegocios);
            adapterTipoNegocio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listSpinnerTipoCliente.setAdapter(adapterTipoNegocio);


            //////Zonas
            listZonas=viewModelZona.getZonaByRepartidor(idRepartidor);
            ArrayAdapter<ZonasEntity> adapter =new ArrayAdapter<ZonasEntity>(getContext(), android.R.layout.simple_spinner_item, listZonas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listaSpinnerZona.setAdapter(adapter);

            if (tipo!=0 && mCliente!=null){
                tilNombre.getEditText().setText(mCliente.getNamecliente());
                tilNit.getEditText().setText(mCliente.getNit());
                tilDireccion.getEditText().setText(mCliente.getDireccion());
                tilTelefono.getEditText().setText(mCliente.getTelefono());
                tilRazonSocial.getEditText().setText(mCliente.getRazon_social());
                listaSpinnerZona.setSelection(ObtenerPosicionListaZona(mCliente.getCczona()));
                listSpinnerTipoCliente.setSelection(ObtenerPosicionListaNegocio(mCliente.getTipoNegocio()));
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
    public int ObtenerPosicionListaNegocio(int idNegocio){

        for (int i = 0; i < listNegocios.size(); i++) {
            if (listNegocios.get(i).getId()==idNegocio){
                return i;
            }
        }
        return -1;
    }
public void onClickAtras(){
    btnAtras.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialogQuestion=showDialogQuestion("Si sale de esta pantalla se perderan todos los datos. \n Esta Seguro de Salir?",true);
            dialogQuestion.show();
        }
    });
}
    public List<LatLng> ObtenerListaPuntos(int IdZona ){

        List<PointEntity> lisPoint= null;
        final List<LatLng> latLngList = new ArrayList<>();
        try {
            lisPoint = viewmodelPoint.getPoint(IdZona);
            if (lisPoint.size()>0) {

                for (int j = 0; j < lisPoint.size(); j++) {

                    latLngList.add(new LatLng(lisPoint.get(j).getLatitud(), lisPoint.get(j).getLongitud()));

                }
            }

        } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }
        return latLngList;
    }
    public boolean ValidarClienteEnzona(){
        int idRepartidor=DataPreferences.getPrefInt("idrepartidor",getActivity());
        List<ZonasEntity> lisZona= null;
        Boolean bandera=false;
        int Zona;
        int idZonas= DataPreferences.getPrefInt("Zonas",mContext);
        if (idZonas==-1){
            Zona=zonaSelected.getLanumi();
        }else{
            Zona=idZonas;
        }

                List<LatLng> lista=ObtenerListaPuntos(Zona);

                LatLng punto=new LatLng(LocationGeo.getLocationActual().getLatitude(),LocationGeo.getLocationActual().getLongitude());

                if (lista.size()>0){
                    if (LocationGeo.Encontrado(lista,punto)){
                        return true;
                    }

                }




        return false;

    }
    public void onClickGrabar(){
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (M_Uii.trim().equals("")){


                    if (validarDatos()){


                        int ValidarZona=DataPreferences.getPrefInt("ValidarZona",getActivity());
                        if (tipo==0 && ValidarZona==1){
                            int idZonas= DataPreferences.getPrefInt("Zonas",mContext);
                            if (idZonas!=-1){
                                if (ValidarClienteEnzona()){
                                    showDialogs();
                                    new ChecarNotificaciones().execute();
                                    return;
                                }else{
                                    ShowMessageResult("No puede Crear el Cliente por que no se Encuentra dentro de su Zona asignada");
                                    return;
                                }
                            }else{
                                if (zonaSelected!=null){
                                    if (ValidarClienteEnzona()){
                                        showDialogs();
                                        new ChecarNotificaciones().execute();
                                        return;
                                    }else{
                                        ShowMessageResult("No puede Crear el Cliente por que no se Encuentra dentro de su Zona asignada");
                                        return;
                                    }
                                }else{
                                    ShowMessageResult("Seleccione una Zona");
                                    return;
                                }
                            }




                        }else{
                            showDialogs();
                            new ChecarNotificaciones().execute();
                            return;
                        }


                    }else{
                        M_Uii="";
                    }
                }else{
                    ShowMessageResult("El cliente ya ha sido guardado localmente, por favor vuelva hacia atras");
                    return;
                }




            }
        });
    }

    public void showDialogs() {
        ShowDialogSincronizando();
        alertDialog.show();
    }
    public void showSaveResultOption(int codigo, String id, String mensaje) {

        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }

        switch (codigo){
            case 0:
                dialogs= showCustomDialog("El cliente ha sido guardado localmente con exito. Pero no pudo ser guardado en el servidor por problemas de red"
                        ,true);
                dialogs.setCancelable(false);
                dialogs.show();
                break;
            case 1:
                dialogs= showCustomDialog("El Cliente id:"+id+" ha sido guardado localmente y en el servidor" +
                        " con exito.",true);
                dialogs.setCancelable(false);
                dialogs.show();
                break;
            case 2:
                dialogs= showCustomDialog("El cliente ha sido Modificado localmente con exito."
                        ,true);
                dialogs.setCancelable(false);
                dialogs.show();
                break;

        }
    }

    public AlertDialog showCustomDialog(String Contenido, Boolean flag) {

        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_succes, null);

        builder.setView(v);
        TextView Content = (TextView) v.findViewById(R.id.dialog_content);
        Button aceptar = (Button) v.findViewById(R.id.dialog_ok);

        Content.setText(Contenido);
        aceptar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogs.dismiss();
                        RetornarPrincipal();

                        //  finish();
                    }
                }
        );


        return builder.create();
    }
        public void GuardarCliente( ClienteEntity cliente){

        try {
             final String CodeGenerado=cliente.getCodigogenerado();
            viewModel.insertCliente(cliente);


            if (ShareMethods.IsServiceRunning(getContext(),ServiceSincronizacion.class)){
                UtilShare.mActivity=getActivity();
                Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
               // getContext().stopService(intent);
                ServiceSincronizacion.getInstance().onDestroy();
            }
            int idRepartidor=DataPreferences.getPrefInt("idrepartidor",mContext);
            ApiManager apiManager=ApiManager.getInstance(getContext());
            apiManager.InsertUser(cliente,String.valueOf(idRepartidor).trim(), new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    ResponseLogin responseUser = response.body();
                    if (response.code()==404){
                        showSaveResultOption(0,"","");


                        if (!ShareMethods.IsServiceRunning(getContext(),ServiceSincronizacion.class)){
                            UtilShare.mActivity=getActivity();
                            Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
                            getContext().startService(intent);
                        }
                        return;
                    }
                    try{
                        if (responseUser!=null){
                            if (responseUser.getCode()==0){
                            ClienteEntity mcliente=   viewModel.getClientebycode(CodeGenerado);
                            if (mcliente!=null){
                                mcliente.setNumi(Integer.parseInt(responseUser.getToken()));
                                mcliente.setEstado(1);
                                mcliente.setCodigogenerado(responseUser.getToken());
                                viewModel.updateCliente(mcliente);
                                if (alertDialog.isShowing()){
                                    alertDialog.dismiss();
                                }


                                if (!ShareMethods.IsServiceRunning(getContext(),ServiceSincronizacion.class)){
                                    UtilShare.mActivity=getActivity();
                                    Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
                                    getContext().startService(intent);
                                }
                                showSaveResultOption(1,""+mcliente.getNumi(),"");
                                return;
                            }
                            }else{

                                ShowMessageResult( responseUser.getMessage());

                                if (!ShareMethods.IsServiceRunning(getContext(),ServiceSincronizacion.class)){
                                    UtilShare.mActivity=getActivity();
                                    Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
                                    getContext().startService(intent);
                                }
                            }
                        }
                    }catch (Exception e){

                        if (!ShareMethods.IsServiceRunning(getContext(),ServiceSincronizacion.class)){
                            UtilShare.mActivity=getActivity();
                            Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
                            getContext().startService(intent);
                        }
                        if (alertDialog.isShowing()){
                            alertDialog.dismiss();
                        }
                    }
                    if (alertDialog.isShowing()){
                        alertDialog.dismiss();
                    }

                    if (!ShareMethods.IsServiceRunning(getContext(),ServiceSincronizacion.class)){
                        UtilShare.mActivity=getActivity();
                        Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
                        getContext().startService(intent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    if (alertDialog.isShowing()){
                        alertDialog.dismiss();
                    }

                    if (!ShareMethods.IsServiceRunning(getContext(),ServiceSincronizacion.class)){
                        UtilShare.mActivity=getActivity();
                        Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
                        getContext().startService(intent);
                    }
                    showSaveResultOption(0,"","");
                    //ShowMessageResult("Error al guardar el pedido");
                }
            });


        }catch (Exception e){
            if (alertDialog.isShowing()){
                alertDialog.dismiss();
            }
            if (!ShareMethods.IsServiceRunning(getContext(),ServiceSincronizacion.class)){
                UtilShare.mActivity=getActivity();
                Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
                getContext().startService(intent);
            }
            showSaveResultOption(0,"","");
        }

}
public void OnClickGps(){
        btngps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LocationGeo.getLocationActual()==null){
                    LocationGeo.iniciarGPS();
                }else{
                  location=  LocationGeo.getLocationActual();
                    LatLng sydney=new LatLng(location.getLatitude(),location.getLongitude());
                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

                }
            }
        });
}

    private boolean esNombreValido(String nombre) {
       // Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
       // if (!patron.matcher(nombre).matches() || nombre.length() > 30) {
        if (nombre.length()<2){
            tilNombre.setError("Nombre inválido");
            return false;
        } else {
            tilNombre.setError(null);
        }

        return true;
    }
    private boolean esRazonSocialValido(String nombre) {
        // Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        // if (!patron.matcher(nombre).matches() || nombre.length() > 30) {
        if (nombre.length()<2){
            tilRazonSocial.setError("Razon Social inválido");
            return false;
        } else {
            tilRazonSocial.setError(null);
        }

        return true;
    }
    private boolean esDireccionValido(String nombre) {

        if (tilDireccion.getEditText().getText().length() <= 2) {
            tilDireccion.setError("Dirección inválido");
            return false;
        } else {
            tilDireccion.setError(null);
        }

        return true;
    }
    private boolean esNitValido(String nombre) {

        if (tilNit.getEditText().getText().length() <= 0) {
            tilNit.setError("Nit inválido");
            return false;
        } else {
            tilNit.setError(null);
        }

        return true;
    }
    private boolean esTelefonoValido(String telefono) {
       // if (!Patterns.PHONE.matcher(telefono).matches()) {
         if (telefono.length()==0){
            tilTelefono.setError("Teléfono inválido");
            return false;
        } else {
            tilTelefono.setError(null);
        }

        return true;
    }
    private boolean validarDatos() {
        String nombre = tilNombre.getEditText().getText().toString();
        String telefono = tilTelefono.getEditText().getText().toString();
        String direccion = tilDireccion.getEditText().getText().toString();
        String nit = tilNit.getEditText().getText().toString();

        boolean a = esNombreValido(nombre);
        boolean b = esTelefonoValido(telefono);
        boolean c = esDireccionValido(direccion);
      /*  boolean d = esNitValido(nit);
        boolean e=esRazonSocialValido(tilRazonSocial.getEditText().getText().toString());*/

        if (a && b && c ) {
            return true;
        }
return false;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        LatLng sydney ;
        /*if (DataOrder.cliente.getLatitud()!=null && DataOrder.cliente.getLongitud()!=null){
            sydney = new LatLng(Double.parseDouble(""+DataOrder.cliente.getLatitud()),Double.parseDouble(""+DataOrder.cliente.getLongitud()));
        }else{*/
        location=new Location("location");
        location.setLatitude(-17.7833935);
        location.setLongitude(-63.1822832);
        if (tipo==0){
            sydney = new LatLng(location.getLatitude(),location.getLongitude());
        }else{
            if (mCliente.getLatitud()!=0){
                sydney = new LatLng(mCliente.getLatitud(),mCliente.getLongitud());
            }else{
                sydney = new LatLng(location.getLatitude(),location.getLongitude());
            }

        }

        //}


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapa.setMyLocationEnabled(false);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
// create marker
        MarkerOptions marker = new MarkerOptions().position(sydney);


    }


    public AlertDialog showDialogQuestion(String Contenido, Boolean flag) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_question, null);

        builder.setView(v);
        TextView Content =(TextView)v.findViewById(R.id.dialog_question_content) ;
        Button aceptar = (Button) v.findViewById(R.id.dialog_btn_ok);
        Button cancelar = (Button) v.findViewById(R.id.dialog_btn_cancel);
        Content.setText(Contenido);
        aceptar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Crear Cuenta...
                        //
                        dialogQuestion.dismiss();
                        RetornarPrincipal();

                        //  finish();
                    }
                }
        );
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogQuestion.dismiss();
            }
        });


        return builder.create();
    }
    private void RetornarPrincipal(){
        try{
            MainActivity fca = ((MainActivity) getActivity());
            fca.removeAllFragments();

            Fragment frag = new ListClientesFragment();
            //fca.switchFragment(frag,"LISTAR_CLIENTE");
            fca.CambiarFragment(frag, Constantes.TAG_CLIENTES);
        }catch(Exception e){

        }

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
        progresdialog.setMessage("Guardando Cliente .....");*/

        try
        {

            alertDialog = new LottieAlertDialog.Builder(getContext(), DialogTypes.TYPE_LOADING).setTitle("Cliente")
                    .setDescription("Guardando Cliente .....")
                    .build();

            alertDialog.setCancelable(false);
        }catch (Error e){

            String d=e.getMessage();

        }

    }
    public void ShowMessageResult(String message) {

if (alertDialog!=null){
    if (alertDialog.isShowing()){
        alertDialog.dismiss();
    }
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

    private class ChecarNotificaciones extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            //NUESTRO CODIGO
            new Handler().postDelayed(new Runnable() {
                public void run()

                {
                    M_Uii= UUID.randomUUID().toString();

                    if (tipo==0){
                        ClienteEntity cliente=new ClienteEntity();
                        int codigoRepartidor=  DataPreferences.getPrefInt("idrepartidor",getContext());
                        //cliente.setCodigogenerado();
                        DateFormat df = new SimpleDateFormat("dMMyyyy,HH:mm:ss");
                        String code = df.format(Calendar.getInstance().getTime());
                        code=""+codigoRepartidor+","+code;
                        cliente.setCodigogenerado(code+"P2.1");
                        cliente.setNumi(0);
                        cliente.setFechaingreso(Calendar.getInstance().getTime());
                        cliente.setDireccion(tilDireccion.getEditText().getText().toString());
                        cliente.setNamecliente(tilNombre.getEditText().getText().toString());
                        cliente.setNit(tilNit.getEditText().getText().toString());
                        cliente.setTelefono(tilTelefono.getEditText().getText().toString());
                        cliente.setRazon_social(tilRazonSocial.getEditText().getText().toString());
                        cliente.setLatitud(mapa.getCameraPosition().target.latitude);
                        cliente.setLongitud(mapa.getCameraPosition().target.longitude);
                        cliente.setTipoNegocio(negocioSelected.getId());

                        cliente.setCccat(2);
                        int idZonas= DataPreferences.getPrefInt("Zonas",mContext);
                        if (idZonas==-1){
                            cliente.setCczona(zonaSelected.getLanumi());
                        }else{
                            cliente.setCczona(idZonas);
                        }


                        cliente.setEstado(0);
                        GuardarCliente(cliente);
                    }else{
                        mCliente.setDireccion(tilDireccion.getEditText().getText().toString());
                        mCliente.setNamecliente(tilNombre.getEditText().getText().toString());
                        mCliente.setNit(tilNit.getEditText().getText().toString());
                        mCliente.setTelefono(tilTelefono.getEditText().getText().toString());
                        mCliente.setRazon_social(tilRazonSocial.getEditText().getText().toString());
                        mCliente.setLatitud(mapa.getCameraPosition().target.latitude);
                        mCliente.setLongitud(mapa.getCameraPosition().target.longitude);
                        mCliente.setEstado(2);
                        mCliente.setTipoNegocio(negocioSelected.getId());
                        int idZonas= DataPreferences.getPrefInt("Zonas",mContext);
                        if (idZonas==-1){
                            mCliente.setCczona(zonaSelected.getLanumi());
                        }else{
                            mCliente.setCczona(idZonas);
                        }

                        viewModel.updateCliente(mCliente);
                        try {
                            List<PedidoEntity> listPedido=viewModelPedidos.getPedidobyCliente(mCliente.getCodigogenerado());
                            for (int i = 0; i < listPedido.size(); i++) {
                                PedidoEntity pedido=listPedido.get(i);
                                pedido.setCliente(mCliente.getNamecliente());
                                viewModelPedidos.updatePedido(pedido);
                            }

                        } catch (ExecutionException e) {
                            // e.printStackTrace();
                            showSaveResultOption(2,""+mCliente.getNumi(),"");
                            if (alertDialog.isShowing()){
                                alertDialog.dismiss();
                            }
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                            showSaveResultOption(2,""+mCliente.getNumi(),"");
                            if (alertDialog.isShowing()){
                                alertDialog.dismiss();
                            }
                        }


                        showSaveResultOption(2,""+mCliente.getNumi(),"");
                        if (alertDialog.isShowing()){
                            alertDialog.dismiss();
                        }
                    }

                }
            }, 1 * 2000);
            super.onPostExecute(result);
        }
    }
}
