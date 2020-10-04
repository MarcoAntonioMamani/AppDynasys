package com.dynasys.appdisoft.ListarDeudas.Pagos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.dynasys.appdisoft.Adapter.AdapterDetalleDeuda;
import com.dynasys.appdisoft.Adapter.viewOrdersAdaptader;
import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Constantes;
import com.dynasys.appdisoft.ListarDeudas.DeudasMvp;
import com.dynasys.appdisoft.ListarDeudas.ListDeudasFragment;
import com.dynasys.appdisoft.Login.DB.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaDetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;
import com.dynasys.appdisoft.Login.DB.ListViewModel.CobranzaDetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewModel.CobranzaListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewModel.DeudaListaViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.Pedidos.CreatePedidos.CreatePedidoFragment;
import com.dynasys.appdisoft.Pedidos.ListPedidosFragment;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.ShareUtil.LocationGeo;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.common.internal.Preconditions;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagosFragment extends Fragment implements PagosMvp.View{
TextView tvNombreCliente,tvDebe,tvLimite,tvDisponible;
TextView tvTotalDisponible,tvTotalPagar;
Button btnCancelar,BtnPagar;
    private AdapterDetalleDeuda mviewOrderAdapter;
    View view;
    Context context;
    RecyclerView detalle_List;
    LottieAlertDialog alertDialog;
    private DeudaListaViewModel viewModelDeuda;
    private CobranzaListViewModel viewModelCobranza;
    private CobranzaDetalleListViewModel viewModeloCobranzaDetalle;
    private PagosMvp.Presenter mDeudaPresenter;
    private AlertDialog dialogs,dialogQuestion;
    List<DeudaEntity> MListaDeuda;
    DeudaEntity mDeuda;
    private String M_Uii="";
    Boolean BanderaEvento=false;
    public PagosFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public PagosFragment(DeudaEntity mDeuda) {
        // Required empty public constructor
        this.mDeuda=mDeuda;
        UtilShare.IdCLiente=mDeuda.getClienteId();
    }
    @Override
    public void onResume() {

        super.onResume();
        getActivity().setTitle("Cobrar Deuda");
        context=getContext();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_pagos, container, false);
        detalle_List = (RecyclerView) view.findViewById(R.id.id_detalle_listDeudas);
        detalle_List.setHasFixedSize(true);
        tvNombreCliente=(TextView)view.findViewById(R.id.view_info_deuda_cliente);
        tvDebe=(TextView)view.findViewById(R.id.view_info_deuda_debe);
        tvLimite=(TextView)view.findViewById(R.id.view_info_deuda_limite);
        tvDisponible=(TextView)view.findViewById(R.id.view_info_deuda_disponible);
        tvTotalDisponible=(TextView)view.findViewById(R.id.view_info_deuda_MontoDisponible);
        tvTotalPagar=(TextView)view.findViewById(R.id.view_info_deuda_totalPagar);
        btnCancelar=(Button)view.findViewById(R.id.id_btn_deuda_cancelar);
        BtnPagar=(Button)view.findViewById(R.id.id_btn_deuda_guardar);
        viewModelDeuda= ViewModelProviders.of(getActivity()).get(DeudaListaViewModel.class);
        viewModelCobranza=ViewModelProviders.of(getActivity()).get(CobranzaListViewModel.class);
        viewModeloCobranzaDetalle=ViewModelProviders.of(getActivity()).get(CobranzaDetalleListViewModel.class);
        new PagosPresenter(this,getContext(),viewModelDeuda,getActivity(),viewModelCobranza,viewModeloCobranzaDetalle);

        mDeudaPresenter.CargarDeudas();
        IniciarParametros();
        context=getContext();
        OnClickButton();
        return view;
    }
    public void OnClickButton(){
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclickCancelar();
            }
        });
        BtnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MontoTotalCobrar()>0){
                    if (M_Uii.trim().equals("")){
                        showDialogs();
                        new ChecarNotificaciones().execute();
                    }else{
                        ShowMessageResult("El Cobro ya fue Realizado Localmente, por favor vuelva hacia atras");
                    }
                }else{
                    ShowMessageResult("Debe Ingresar Un Monto a Cobrar");
                }
            }
        });


    }

    public void showDialogs() {
        ShowDialogSincronizando();
        alertDialog.show();
    }

    private void ShowDialogSincronizando(){
      /*  progresdialog=new ProgressDialog(getContext());
        progresdialog.setCancelable(false);
        progresdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresdialog.setIndeterminate(false);
        Drawable drawable = new ProgressBar(getActivity()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent),
                PorterDuff.Mode.SRC_IN);
        progresdialog.setIndeterminateDrawable(drawable);
        progresdialog.setMessage("Guardando Pedido .....");*/
        try
        {

            alertDialog = new LottieAlertDialog.Builder(getContext(), DialogTypes.TYPE_LOADING).setTitle("COBRANZA")
                    .setDescription("Guardando Cobros ...")
                    .build();

            alertDialog.setCancelable(false);
        }catch (Error e){

            String d=e.getMessage();

        }
    }
public double MontoTotalCobrar(){
        double Cobranza=0;

    for (int i = 0; i < MListaDeuda.size(); i++) {
        Cobranza+=MListaDeuda.get(i).getTotalAPagar();
    }
    return Cobranza;
}

    public void onclickCancelar(){
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogQuestion=showDialogQuestion("Sus Datos Se Perderan Si Sale De Esta Pantalla \n Esta Seguro de Salir?",true);
                dialogQuestion.show();
            }
        });
    }
    public android.support.v7.app.AlertDialog showDialogQuestion(String Contenido, Boolean flag) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);

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
                        //getFragmentManager().popBackStack();
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
    public void IniciarParametros(){
   tvNombreCliente.setText(mDeuda.getCliente());
    tvDebe.setText(ShareMethods.ObtenerDecimalToString(mDeuda.getPendiente(),2));
    tvLimite .setText(ShareMethods.ObtenerDecimalToString(mDeuda.getLimiteCliente(),2));
    tvDisponible.setText(ShareMethods.ObtenerDecimalToString(mDeuda.getLimiteCliente()-mDeuda.getPendiente(),2));
    tvTotalDisponible.setText(ShareMethods.ObtenerDecimalToString(mDeuda.getLimiteCliente()-mDeuda.getPendiente(),2));
    }
    public void iniciarRecyclerView(){
        mviewOrderAdapter = new AdapterDetalleDeuda(getContext(),MListaDeuda,this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        detalle_List.setLayoutManager(llm);
        detalle_List.setAdapter(mviewOrderAdapter);
        detalle_List.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(detalle_List, false);

    }
    public void RetornarPrincipal(){
        MainActivity fca = ((MainActivity) getActivity());
        fca.removeAllFragments();
        Fragment frag = new ListDeudasFragment();
        //fca.switchFragment(frag,"LISTAR_PEDIDOS");
        fca.CambiarFragment(frag, Constantes.TAG_PEDIDOS);
    }
    public void ShowMessageResult(String message) {
        Snackbar snackbar= Snackbar.make(tvNombreCliente, message, Snackbar.LENGTH_LONG);
        View snackbar_view=snackbar.getView();
        TextView snackbar_text=(TextView)snackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_iinfo,0);
        snackbar_text.setGravity(Gravity.CENTER);
        snackbar.show();
    }
    @Override
    public void MostrarListadoPagos(List<DeudaEntity> clientes) {
        MListaDeuda=clientes;
        iniciarRecyclerView();
    }

    @Override
    public void setPresenter(PagosMvp.Presenter presenter) {
        mDeudaPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    public void OnClickCheck(DeudaEntity deuda, boolean Valor, int Posicion, EditText tvMontoPagar) {


        BanderaEvento=true;
        if (Valor==true){
            MListaDeuda.get(Posicion).setTotalAPagar(MListaDeuda.get(Posicion).getPendiente());
            tvMontoPagar.setText(ShareMethods.ObtenerDecimalToString(MListaDeuda.get(Posicion).getPendiente(),2));
        }else{
            MListaDeuda.get(Posicion).setTotalAPagar(0);
            tvMontoPagar.setText(ShareMethods.ObtenerDecimalToString(0,2));
        }
        CalcularTotales();
   BanderaEvento=false;
    }

    @Override
    public void ModifyPago(DeudaEntity deuda, boolean Valor, int Posicion, EditText tvMontoPagar, CheckBox chkPago,String Value) {
        if (BanderaEvento==true){
            return;
        }

        double monto=0.0;
        if (isDouble(Value)){
            monto=Double.parseDouble(Value);
        }
        if (monto>0){


            if (Valor==false){
                chkPago.setChecked(true);
            }
            if (monto>MListaDeuda.get(Posicion).getPendiente()){
                hideKeyboard();
                ShowMessageResult("El monto a Pagar Maximo es  = "+ShareMethods.ObtenerDecimalToString(MListaDeuda.get(Posicion).getPendiente(),2));
                MListaDeuda.get(Posicion).setTotalAPagar(MListaDeuda.get(Posicion).getPendiente());
                tvMontoPagar.setText(ShareMethods.ObtenerDecimalToString(MListaDeuda.get(Posicion).getPendiente(),2));

            }else{
                MListaDeuda.get(Posicion).setTotalAPagar(monto);
            }




        }else{
           chkPago.setChecked(false);
            MListaDeuda.get(Posicion).setTotalAPagar(0);
            //tvMontoPagar.setText(ShareMethods.ObtenerDecimalToString(0,2));
        }

        CalcularTotales();
    }

    @Override
    public void showSaveResultOption(int codigo, String id, String mensaje) {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        M_Uii = "";
        switch (codigo) {
            case 0:

                dialogs = showCustomDialog("La Cobranza ha sido guardado localmente con exito. Pero no pudo ser guardado en el servidor por problemas de red"
                        , true);
                dialogs.setCancelable(false);
                dialogs.show();
                break;
            case 1:

                dialogs = showCustomDialog("La cobranza Nro:" + id + " ha sido guardado localmente y en el servidor" +
                        " con exito.", true);
                dialogs.setCancelable(false);
                dialogs.show();
                break;
            case 2:
                dialogs = showCustomDialog("La Cobranza #" + id + " ha sido guardado localmente con exito. Existen problemas" +
                        " en la exportacion:\n" + mensaje, true);
                dialogs.setCancelable(false);
                dialogs.show();
                break;
            case 3:
                dialogs = showCustomDialog("Existe un problema al guardar la cobranza localmente:\n" + mensaje, false);
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
    private  void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }
    private static boolean isDouble(String cadena){
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
    public void CalcularTotales(){

        Double Total=0.0;
        Double Pendiente=0.0;

        for (int i = 0; i < MListaDeuda.size(); i++) {
            Total+=MListaDeuda.get(i).getTotalAPagar();

        }
        Pendiente=mDeuda.getLimiteCliente()-( mDeuda.getPendiente()-Total);

        tvTotalPagar .setText(ShareMethods.ObtenerDecimalToString(Total,2));
        tvTotalDisponible.setText(ShareMethods.ObtenerDecimalToString(Pendiente,2));

    }
    public void SaveOnline(){
        M_Uii= UUID.randomUUID().toString();
        Calendar c2 = Calendar.getInstance();
        final int hora = c2.get(Calendar.HOUR);
        final int minuto = c2.get(Calendar.MINUTE);
        final int Segundo = c2.get(Calendar.SECOND);



        int codigoRepartidor=  DataPreferences.getPrefInt("idrepartidor",getContext());
        //cliente.setCodigogenerado();
        DateFormat df = new SimpleDateFormat("dMMyyyy,HH:mm:ss");

        String code = df.format(Calendar.getInstance().getTime());
        code=""+codigoRepartidor+","+code+"V2.5";

        CobranzaRequest cobranza=new CobranzaRequest();

        cobranza.setEstado(0);
        cobranza.setFecha(Calendar.getInstance().getTime());
        cobranza.setId(0);
        cobranza.setObservacion(code);
        cobranza.setTenumi(code);
        cobranza.setIdPersonal(codigoRepartidor);
        cobranza.setListDetalle(ObtenerDetalle(code));
        mDeudaPresenter.GuardarCobranza(cobranza,MListaDeuda);



    }
  public List<CobranzaDetalleEntity> ObtenerDetalle(String NumiCobranza){
        List<CobranzaDetalleEntity> list=new ArrayList<>();
      for (int i = 0; i < MListaDeuda.size(); i++) {
          if (MListaDeuda.get(i).getTotalAPagar()>0){
              CobranzaDetalleEntity detalle=new CobranzaDetalleEntity();
              detalle.setCobranzaId(NumiCobranza);
              detalle.setEstado(0);
              detalle.setFechaPago(Calendar.getInstance().getTime());
              detalle.setPedidoId(MListaDeuda.get(i).getPedidoId());
              detalle.setTdnumi(0);//0 por que identity
              detalle.setCliente(MListaDeuda.get(i).getCliente());
              detalle.setMontoAPagar(MListaDeuda.get(i).getTotalAPagar());
              list.add(detalle);
          }
      }
return list;

  }

    private class ChecarNotificaciones extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            SaveOnline();

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            //NUESTRO CODIGO
            new Handler().postDelayed(new Runnable() {
                public void run() {

                }
            }, 1 * 2000);
            super.onPostExecute(result);
        }
    }
}
