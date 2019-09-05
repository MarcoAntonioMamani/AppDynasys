package com.dynasys.appdisoft.Clientes.CreateCliente;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.Cloud.ResponseLogin;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.ShareUtil.LocationGeo;
import com.dynasys.appdisoft.ShareUtil.ServiceSincronizacion;
import com.dynasys.appdisoft.ShareUtil.ServicesLocation;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

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
    private  ClientesListViewModel viewModel;
    private PedidoListViewModel viewModelPedidos;
    private ProgressDialog progresdialog;
    private Context mContext;
    ClienteEntity mCliente;
    private int tipo=0; //// TIpo=0 = Nuevo Cliente ------------------  Tipo = 1 Modificacion Cliente
    public CreateClienteFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public CreateClienteFragment(int tipo, ClienteEntity cliente) {
        // Required empty public constructor
        this.tipo=tipo;
        this.mCliente=cliente;
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
        tilNombre = (TextInputLayout) view.findViewById(R.id.til_nombre);
        tilTelefono = (TextInputLayout) view.findViewById(R.id.til_telefono);
        tilDireccion = (TextInputLayout) view.findViewById(R.id.til_direccion);
        tilNit = (TextInputLayout) view.findViewById(R.id.til_nit);
        viewModel = ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        viewModelPedidos=ViewModelProviders.of(getActivity()).get(PedidoListViewModel.class);

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
        ShowDialogSincronizando();
        LocationGeo.getInstance(mContext,getActivity());
       LocationGeo.iniciarGPS();
        _prCargarDatos();
        return view;
    }

    public void _prCargarDatos(){
        if (tipo!=0 && mCliente!=null){
            tilNombre.getEditText().setText(mCliente.getNamecliente());
            tilNit.getEditText().setText(mCliente.getNit());
            tilDireccion.getEditText().setText(mCliente.getDireccion());
            tilTelefono.getEditText().setText(mCliente.getTelefono());
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
                   if (validarDatos()){
                       progresdialog.show();
                       if (tipo==0){
                           ClienteEntity cliente=new ClienteEntity();
                           int codigoRepartidor=  DataPreferences.getPrefInt("idrepartidor",getContext());
                           //cliente.setCodigogenerado();
                           DateFormat df = new SimpleDateFormat("dMMyyyy,HH:mm:ss");
                           String code = df.format(Calendar.getInstance().getTime());
                           code=""+codigoRepartidor+","+code;
                           cliente.setCodigogenerado(code);
                           cliente.setNumi(0);
                           cliente.setFechaingreso(Calendar.getInstance().getTime());
                           cliente.setDireccion(tilDireccion.getEditText().getText().toString());
                           cliente.setNamecliente(tilNombre.getEditText().getText().toString());
                           cliente.setNit(tilNit.getEditText().getText().toString());
                           cliente.setTelefono(tilTelefono.getEditText().getText().toString());
                           cliente.setLatitud(mapa.getCameraPosition().target.latitude);
                           cliente.setLongitud(mapa.getCameraPosition().target.longitude);
                           int idzona=DataPreferences.getPrefInt("zona",getContext());
                           cliente.setCccat(1);
                           cliente.setCczona(idzona);
                           cliente.setEstado(0);
                           GuardarCliente(cliente);
                       }else{
                           mCliente.setDireccion(tilDireccion.getEditText().getText().toString());
                           mCliente.setNamecliente(tilNombre.getEditText().getText().toString());
                           mCliente.setNit(tilNit.getEditText().getText().toString());
                           mCliente.setTelefono(tilTelefono.getEditText().getText().toString());
                           mCliente.setLatitud(mapa.getCameraPosition().target.latitude);
                           mCliente.setLongitud(mapa.getCameraPosition().target.longitude);
                           mCliente.setEstado(2);
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
                               if (progresdialog.isShowing()){
                                   progresdialog.dismiss();
                               }
                           } catch (InterruptedException e) {
                               //e.printStackTrace();
                               showSaveResultOption(2,""+mCliente.getNumi(),"");
                               if (progresdialog.isShowing()){
                                   progresdialog.dismiss();
                               }
                           }


                           showSaveResultOption(2,""+mCliente.getNumi(),"");
                           if (progresdialog.isShowing()){
                               progresdialog.dismiss();
                           }
                       }

                   }
            }
        });
    }
    public void showSaveResultOption(int codigo, String id, String mensaje) {

        if (progresdialog.isShowing()){
            progresdialog.dismiss();
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
            ApiManager apiManager=ApiManager.getInstance(getContext());
            apiManager.InsertUser(cliente, new Callback<ResponseLogin>() {
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
                                viewModel.updateCliente(mcliente);
                                if (progresdialog.isShowing()){
                                    progresdialog.dismiss();
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
                        if (progresdialog.isShowing()){
                            progresdialog.dismiss();
                        }
                    }
                    if (progresdialog.isShowing()){
                        progresdialog.dismiss();
                    }

                    if (!ShareMethods.IsServiceRunning(getContext(),ServiceSincronizacion.class)){
                        UtilShare.mActivity=getActivity();
                        Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
                        getContext().startService(intent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    if (progresdialog.isShowing()){
                        progresdialog.dismiss();
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
            if (progresdialog.isShowing()){
                progresdialog.dismiss();
            }
            if (!ShareMethods.IsServiceRunning(getContext(),ServiceSincronizacion.class)){
                UtilShare.mActivity=getActivity();
                Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
                getContext().startService(intent);
            }
            ShowMessageResult("El Cliente no ha podido ser guardado: "+ e.getMessage());
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
        boolean d = esNitValido(nit);

        if (a && b && c && d) {
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
        MainActivity fca = ((MainActivity) getActivity());
        fca.returnToMain();
    }
    private void ShowDialogSincronizando(){
        progresdialog=new ProgressDialog(getContext());
        progresdialog.setCancelable(false);
        progresdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresdialog.setIndeterminate(false);
        Drawable drawable = new ProgressBar(getActivity()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent),
                PorterDuff.Mode.SRC_IN);
        progresdialog.setIndeterminateDrawable(drawable);
        progresdialog.setMessage("Guardando Cliente .....");

    }
    public void ShowMessageResult(String message) {
        Snackbar snackbar= Snackbar.make(btnGuardar, message, Snackbar.LENGTH_LONG);
        View snackbar_view=snackbar.getView();
        TextView snackbar_text=(TextView)snackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_iinfo,0);
        snackbar_text.setGravity(Gravity.CENTER);
        snackbar.show();
    }
}
