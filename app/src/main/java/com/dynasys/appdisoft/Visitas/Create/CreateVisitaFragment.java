package com.dynasys.appdisoft.Visitas.Create;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.dynasys.appdisoft.Adapter.ClientesAdapter;
import com.dynasys.appdisoft.Clientes.CreateCliente.CreateClienteFragment;
import com.dynasys.appdisoft.Clientes.CreateCliente.MySupportMapFragment;
import com.dynasys.appdisoft.Clientes.CustomInfoWindowAdapter;
import com.dynasys.appdisoft.Clientes.ListClientesFragment;
import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Constantes;
import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PointEntity;
import com.dynasys.appdisoft.Login.DB.Entity.VisitaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ZonasEntity;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.ShareUtil.LocationGeo;
import com.dynasys.appdisoft.ShareUtil.ServiceSincronizacion;

import com.dynasys.appdisoft.Login.DB.ListViewmodel.*;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;

import com.dynasys.appdisoft.Visitas.ListaVisitasFragment;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.jetbrains.annotations.NotNull;

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


public class CreateVisitaFragment extends Fragment  implements OnMapReadyCallback {


    GoogleMap mapa;
    View view;
    MySupportMapFragment mSupportMapFragment;
    NestedScrollView scrollView;
    Button btngps,btnAtras,btnGuardar;
    private AlertDialog dialogs,dialogQuestion;
    Location location;
    //////Variables de los campos
    private TextInputLayout tilDetalle;
    private AutoCompleteTextView acliente;
    private ClientesListViewModel viewModelCliente;
    private VisitaListViewModel viewModelVisita;
    private ZonaListViewModel viewModelZonas;
    private PointListViewModel viewmodelPoint;

    private Context mContext;
    VisitaEntity mVisita;

    ClienteEntity mcliente=null;
    private String M_Uii="";
    private int tipo=0; //// TIpo=0 = Nuevo Cliente ------------------  Tipo = 1 Modificacion Cliente
    private int  isUpdate=0;
    LottieAlertDialog alertDialog;
    ClientesAdapter clientAdapter;
    public CreateVisitaFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public CreateVisitaFragment(int tipo, VisitaEntity cliente,int isUpdate) {
        // Required empty public constructor
        this.tipo=tipo;
        this.mVisita=cliente;
        this.isUpdate=isUpdate;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (tipo==0){
            getActivity().setTitle("Crear Visita");
        }else{
            getActivity().setTitle("Modificar Visita");
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
        view = inflater.inflate(R.layout.fragment_create_visita, container, false);
        scrollView=view.findViewById(R.id.id_scrollVisita);
        btngps=(Button)view.findViewById(R.id.id_btnVisita_ObtenerGps);
        btnAtras=(Button)view.findViewById(R.id.id_btnVisita_cancelar);
        btnGuardar=(Button)view.findViewById(R.id.id_btnVisita_guardar);
        acliente=view.findViewById(R.id.visita_buscar_cliente);
        viewModelZonas=ViewModelProviders.of(this).get(ZonaListViewModel.class);
        viewmodelPoint=ViewModelProviders.of(this).get(PointListViewModel.class);

        tilDetalle=(TextInputLayout)view.findViewById(R.id.tilvisita_detalle);

        viewModelCliente = ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        viewModelVisita = ViewModelProviders.of(getActivity()).get(VisitaListViewModel.class);


        mSupportMapFragment = (MySupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_visita);

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
            tilDetalle.getEditText().setEnabled(false);

        }

        if (LocationGeo.estaActivoGPS() && tipo==0){
            btngps.performClick();
        }
        CargarClientes();
        return view;
    }
    public void CargarClientes() {
        try {
            //List<ClienteEntity> listCliente=FiltarByZona(viewModelClientes.getMAllCliente(1));
            List<ClienteEntity> listCliente=viewModelCliente.getMAllCliente(1);
            if (listCliente.size()>0){
                acliente.setThreshold(1);
                clientAdapter = new ClientesAdapter(getActivity(), R.layout.row_customer, listCliente);
                acliente.setAdapter(clientAdapter);
                acliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if ((ClienteEntity) adapterView.getItemAtPosition(i)!=null){
                            mcliente = (ClienteEntity) adapterView.getItemAtPosition(i);

                        }


                    }
                });
            }
        } catch (ExecutionException e) {
            // e.printStackTrace();
        } catch (InterruptedException e) {
            //  e.printStackTrace();
        }

    }

    public void _prCargarDatos(){


            if (tipo!=0 && mVisita!=null){
                tilDetalle.getEditText().setText(mVisita.getDescripcion());
               acliente .setText(mVisita.getNombreCliente());
                acliente.setEnabled(false);
                Location  location02=new Location("location");
                location02.setLatitude(mVisita.getLatitud());
                location02.setLongitude(mVisita.getLongitud());

                Dibujar(location02);
            }

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
    public void onClickGrabar(){
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (M_Uii.trim().equals("")){


                    if (validarDatos()){

                        if (!LocationGeo.estaActivoGPS()){
                            ShowMessageResult("Encienda su Gps para poder Grabar la Visita");
                        }else{

                            if (LocationGeo.estaActivoGPS()){
                                btngps.performClick();
                            }
                            if (LocationGeo.getLocationActual()==null){
                                ShowMessageResult("No hay Datos GPS Haga Clic en Obteber Gps");
                            }else{
                         //Validando Zonas
                                if (ValidarClienteEnzona()) {
                                    if (mcliente==null && tipo==0){
                                        ShowMessageResult("Seleccione un Cliente");
                                    }else{
                                        showDialogs();
                                        new ChecarNotificaciones().execute();
                                    }
                                }else{
                                    ShowMessageResult("El Cliente Visitado no Se Encuentra dentro de la zona");
                                }



                            }

                        }

                    }else{
                        M_Uii="";
                    }
                }else{
                    ShowMessageResult("La Visita ya ha sido guardado localmente, por favor vuelva hacia atras");
                }




            }
        });
    }

    public boolean ValidarClienteEnzona(){
        int idRepartidor=DataPreferences.getPrefInt("idrepartidor",getActivity());
        List<ZonasEntity> lisZona= null;
        Boolean bandera=false;
        try {
            lisZona = viewModelZonas.getZonaByRepartidor(idRepartidor);
            for (int i = 0; i < lisZona.size(); i++) {

                List<LatLng> lista=ObtenerListaPuntos(lisZona.get(i).getLanumi());

                LatLng punto=new LatLng(LocationGeo.getLocationActual().getLatitude(),LocationGeo.getLocationActual().getLongitude());

                if (LocationGeo.Encontrado(lista,punto)){
                    return true;
                }

            }


        } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }

       return false;

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
                dialogs= showCustomDialog("La Visita ha sido guardado localmente con exito. Pero no pudo ser guardado en el servidor por problemas de red"
                        ,true);
                dialogs.setCancelable(false);
                dialogs.show();
                break;
            case 1:
                dialogs= showCustomDialog("La Visita id:"+id+" ha sido guardado localmente y en el servidor" +
                        " con exito.",true);
                dialogs.setCancelable(false);
                dialogs.show();
                break;
            case 2:
                dialogs= showCustomDialog("La Visita ha sido Modificado localmente con exito."
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
    public void GuardarCliente( VisitaEntity cliente){

        try {
            final String CodeGenerado=cliente.getIdSincronizacion();
            //viewModelCliente.insertCliente(cliente);
            viewModelVisita.insertVisita(cliente);

            if (ShareMethods.IsServiceRunning(getContext(), ServiceSincronizacion.class)){
                UtilShare.mActivity=getActivity();
                Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
                // getContext().stopService(intent);
                ServiceSincronizacion.getInstance().onDestroy();
            }
            ApiManager apiManager=ApiManager.getInstance(getContext());
            apiManager.InsertVisita(cliente, new Callback<ResponseLogin>() {
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
                                VisitaEntity mVisita=   viewModelVisita.getVisitabycode(CodeGenerado);
                                if (mVisita!=null){
                                    mVisita.setId(Integer.parseInt(responseUser.getToken()));

                                    mVisita.setIdSincronizacion(responseUser.getToken());
                                    mVisita.setSincronizado(1);
                                    viewModelVisita.updateVisita(mVisita);
                                    if (alertDialog.isShowing()){
                                        alertDialog.dismiss();
                                    }


                                    if (!ShareMethods.IsServiceRunning(getContext(),ServiceSincronizacion.class)){
                                        UtilShare.mActivity=getActivity();
                                        Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
                                        getContext().startService(intent);
                                    }
                                    showSaveResultOption(1,""+mVisita.getId(),"");
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
                    Dibujar(location);
                   // LatLng sydney=new LatLng(location.getLatitude(),location.getLongitude());
                   // mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

                }
            }
        });
    }

    public void Dibujar(Location mlocation){
        if (mapa==null){
            return;
        }
        mapa.clear();

        mapa.setMyLocationEnabled(true);







                if (mlocation.getLongitude()!=0){
                    if (mlocation.getLongitude()!=0){

                        MarkerOptions marker = new MarkerOptions().position(new LatLng(mlocation.getLatitude(),mlocation.getLongitude()));


                                marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapclient01));





                        Marker mm= mapa.addMarker(marker);

                        mm.setTag(mcliente);
                        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mlocation.getLatitude(),mlocation.getLongitude()), 15));
                    }

            }





    }


    private boolean esDetalleValido(String nombre) {
        // Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        // if (!patron.matcher(nombre).matches() || nombre.length() > 30) {
        if (nombre.length()<2){
            tilDetalle.setError("Detalle inválido");
            return false;
        } else {
            tilDetalle.setError(null);
        }

        return true;
    }




    private boolean validarDatos() {
        String nombre = tilDetalle.getEditText().getText().toString();


        boolean a = esDetalleValido(nombre);


        if (a ) {
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
            if (mVisita.getLatitud()!=0){
                sydney = new LatLng(mVisita.getLatitud(),mVisita.getLongitud());
                Location  location02=new Location("location");
                location02.setLatitude(mVisita.getLatitud());
                location02.setLongitude(mVisita.getLongitud());
                Dibujar(location02);
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

        if (tipo!=0){
            mapa.getUiSettings().setScrollGesturesEnabled(false);
        }
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

            Fragment frag = new ListaVisitasFragment();
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

            alertDialog = new LottieAlertDialog.Builder(getContext(), DialogTypes.TYPE_LOADING).setTitle("Visita")
                    .setDescription("Guardando Visita .....")
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
                        VisitaEntity cliente=new VisitaEntity();
                        int codigoRepartidor=  DataPreferences.getPrefInt("idrepartidor",getContext());
                        //cliente.setCodigogenerado();
                        DateFormat df = new SimpleDateFormat("dMMyyyy,HH:mm:ss");
                        String code = df.format(Calendar.getInstance().getTime());
                        Calendar c2 = Calendar.getInstance();
                        final int hora = c2.get(Calendar.HOUR);
                        final int minuto = c2.get(Calendar.MINUTE);
                        code=""+codigoRepartidor+","+code;
                        cliente.setIdSincronizacion(code+"VA1.1");
                        cliente.setId(0);
                        cliente.setFecha(Calendar.getInstance().getTime());
                        cliente.setHora(""+hora+":"+minuto);

                        cliente.setDescripcion(tilDetalle.getEditText().getText().toString());
                        cliente.setNombreCliente(mcliente.getNamecliente());

                        cliente.setDireccion(mcliente.getDireccion());
                        cliente.setTelefono(mcliente.getTelefono());


                        cliente.setRepartidorId(codigoRepartidor);
                        cliente.setPedidoId("");
                        cliente.setClienteId(mcliente.getCodigogenerado());
                        cliente.setEstado(1);//solo para saber estado de la visita
                        cliente.setSincronizado(0);
                        cliente.setLatitud((LocationGeo.getLocationActual())==null? 0:LocationGeo.getLocationActual().getLatitude());
                        cliente.setLongitud((LocationGeo.getLocationActual())==null? 0:LocationGeo.getLocationActual().getLongitude());


                        GuardarCliente(cliente);
                    }else{
                        mVisita.setDescripcion(tilDetalle.getEditText().getText().toString());

                        mVisita.setSincronizado(2);  //Si es 2 haique actualzar
                        viewModelVisita.updateVisita(mVisita);



                        showSaveResultOption(2,""+mVisita.getId(),"");
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