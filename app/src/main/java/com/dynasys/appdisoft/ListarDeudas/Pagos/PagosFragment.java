package com.dynasys.appdisoft.ListarDeudas.Pagos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.ListViewModel.DeudaListaViewModel;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.Pedidos.ListPedidosFragment;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.android.gms.common.internal.Preconditions;

import java.util.ArrayList;
import java.util.List;

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

    private DeudaListaViewModel viewModelDeuda;
    private PagosMvp.Presenter mDeudaPresenter;
    private AlertDialog dialogs,dialogQuestion;
    List<DeudaEntity> MListaDeuda;
    DeudaEntity mDeuda;
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

        new PagosPresenter(this,getContext(),viewModelDeuda,getActivity());

        mDeudaPresenter.CargarDeudas();
        IniciarParametros();
        context=getContext();
        return view;
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
}
