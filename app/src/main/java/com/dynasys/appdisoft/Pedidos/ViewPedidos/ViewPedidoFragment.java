package com.dynasys.appdisoft.Pedidos.ViewPedidos;


import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dynasys.appdisoft.Adapter.viewOrdersAdaptader;
import com.dynasys.appdisoft.Clientes.MapClientActivity;
import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Constantes;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.Pedidos.ListPedidosFragment;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClientesListViewModel;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPedidoFragment extends Fragment implements ViewPedidoMvp.View {

    View view;
    Context context;
    RecyclerView  detalle_List;;
    private List<ClienteEntity> lisClientes=new ArrayList<>();
    private List<DetalleEntity> listDetalle=new ArrayList<>();
    private ClientesListViewModel viewModelClientes;
    private DetalleListViewModel viewModelDetalles;
    private PedidoListViewModel viewModelPedidos;
    private ViewPedidoMvp.Presenter mPedidosPresenter;
    private AlertDialog dialogs,dialogQuestion;
    private viewOrdersAdaptader mviewOrderAdapter;
    private EditText tvCliente,tvObservacion,tvTotalPago;
    private TextView tvFecha,tvMontoTotal;
    private Button btnMapa,btnEntrega;
    RadioButton rEfectivo,rCredito;
    private PedidoEntity mPedido;
    ClienteEntity mCliente;
    LinearLayout LineaBtn,LinearViewCredito;
    EditText EtReclamo;
    int Tipo;
    public ViewPedidoFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public ViewPedidoFragment(PedidoEntity pedido,ClienteEntity mcliente,int tipo) {
        // Required empty public constructor
        this.mPedido=pedido;
        this.mCliente=mcliente;
        this.Tipo=tipo;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Entregar Pedido");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_view_pedido, container, false);
        detalle_List = (RecyclerView) view.findViewById(R.id.pedido_view_RecPedidos);
        detalle_List.setHasFixedSize(true);
        tvCliente=(EditText)view.findViewById(R.id.pedido_view_cliente); tvCliente.setEnabled(false);
        tvObservacion=(EditText)view.findViewById(R.id.pedido_view_observacion);tvObservacion.setEnabled(false);
        tvTotalPago=(EditText)view.findViewById(R.id.id_view_totalpago);
        rEfectivo=(RadioButton)view.findViewById(R.id.id_order_rbt_efectivo) ;
        rCredito=(RadioButton)view.findViewById(R.id.id_order_rbt_credito);
        tvFecha=(TextView)view.findViewById(R.id.pedido_viewdata_fecha);
        LineaBtn=(LinearLayout)view.findViewById(R.id.btnState);
        EtReclamo=(EditText)view.findViewById(R.id.edit_view_Reclamo);
        tvMontoTotal=(TextView)view.findViewById(R.id.pedido_viewdata_MontoTotal);
        LinearViewCredito =(LinearLayout)view.findViewById(R.id.view_CreditoContado);
        btnMapa=(Button)view.findViewById(R.id.pedido_viewdata_btnVerCliente);
        btnEntrega=(Button)view.findViewById(R.id.pedido_viewdata_btnEntregar);
        viewModelDetalles = ViewModelProviders.of(getActivity()).get(DetalleListViewModel.class);
        viewModelClientes = ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        viewModelPedidos=ViewModelProviders.of(getActivity()).get(PedidoListViewModel.class);
        new ViewPedidoPresenter(this,getContext(),viewModelDetalles,getActivity());
        iniciarRecyclerView();
        mPedidosPresenter.getDetailOrder(mPedido.getCodigogenerado());
        OnclickMapa();
        IniciarParametros();
        return view;
    }
public void IniciarParametros(){
    int categoria = DataPreferences.getPrefInt("CategoriaRepartidor",getContext());
    if (categoria==3){
        btnEntrega.setVisibility(View.GONE);
    }
    EtReclamo.setText(mPedido.getReclamo());
    OnclickEntrega();
    if(mPedido.getTipocobro()==2){
        rCredito.setChecked(true);
        tvTotalPago.setText(ShareMethods.ObtenerDecimalToString(mPedido.getTotalcredito(),2));
    }
    rCredito.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b==true){
                tvTotalPago.setText(ShareMethods.ObtenerDecimalToString(mPedido.getTotal(),2));
            }else{
                tvTotalPago.setText(ShareMethods.ObtenerDecimalToString(0,2));
            }
        }
    });
    ////Para Visualiza la seccion de credito o contado
    int ViewCreditos=DataPreferences.getPrefInt("ViewCredito",getContext());
    if (ViewCreditos ==0){
        LinearViewCredito.setVisibility(View.GONE);
    }else{
        LinearViewCredito.setVisibility(View.VISIBLE);
    }
}
    public void OnclickMapa(){
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCliente!=null){

                    if (mCliente.getLatitud()==0 || mCliente.getLongitud()==0){
                        ShowMessageResult("El Cliente seleccionado no tiene registrado una ubicación");
                    }else{
                        UtilShare.cliente=mCliente;
                        MainActivity fca = ((MainActivity) getActivity());
                        fca.startActivity(new Intent(getActivity(), MapClientActivity.class));
                        fca.overridePendingTransition(R.transition.left_in, R.transition.left_out);
                    }
                }else{
                    ShowMessageResult("El Cliente no Existe en la zona del repartidor");
                }
            }
        });
    }
    public void OnclickEntrega(){
        if (Tipo==3){
            LineaBtn.setVisibility(View.GONE);
        }
        btnEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                   PedidoEntity pedi= viewModelPedidos.getPedido(mPedido.getCodigogenerado());
                   if (pedi!=null){
                       if (rCredito.isChecked()==true){
                           pedi.setTipocobro(2);
                           pedi.setTotalcredito(Double.parseDouble(tvTotalPago.getText().toString()));
                       }else{
                           pedi.setTipocobro(1);
                       }
                       pedi.setOaest(3);
                       pedi.setEstado(2);
                       viewModelPedidos.updatePedido(pedi);
                   }
                    showSaveResultOption(0,"","");



            }
        });
    }
    public void showSaveResultOption(int codigo, String id, String mensaje) {



        switch (codigo){
            case 0:
                dialogs= showCustomDialog("Pedido Actualizado Correctamente"
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
    public void RetornarPrincipal(){
        MainActivity fca = ((MainActivity) getActivity());
        fca.removeAllFragments();
        Fragment frag = new ListPedidosFragment(1);
        //fca.switchFragment(frag,"LISTAR_PEDIDOS");
        fca.CambiarFragment(frag, Constantes.TAG_PEDIDOS);
    }
    public void ShowMessageResult(String message) {
        Snackbar snackbar= Snackbar.make(tvTotalPago, message, Snackbar.LENGTH_LONG);
        View snackbar_view=snackbar.getView();
        TextView snackbar_text=(TextView)snackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_iinfo,0);
        snackbar_text.setGravity(Gravity.CENTER);
        snackbar.show();
    }
public void CargarDatos(){
        tvMontoTotal.setText(ShareMethods.ObtenerDecimalToString(mPedido.getTotal(),2));
        tvObservacion.setText(mPedido.getOaobs());
        tvCliente.setText(mPedido.getCliente());
        tvFecha.setText(ShareMethods.ObtenerFecha02(mPedido.getOafdoc()));
}
    @Override
    public void setPresenter(ViewPedidoMvp.Presenter presenter) {
        mPedidosPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    public void showDataDetail(List<DetalleEntity> listDetalle) {
        this.listDetalle=listDetalle;
if (this.listDetalle.size()>0){
    mviewOrderAdapter.setFilter(this.listDetalle);
    CargarDatos();
}

    }

    @Override
    public void recyclerViewListClicked(ViewPedidoMvp.View v, int position) {

    }
    public void iniciarRecyclerView(){
        mviewOrderAdapter = new viewOrdersAdaptader(getContext(),listDetalle);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        detalle_List.setLayoutManager(llm);
        detalle_List.setAdapter(mviewOrderAdapter);
        detalle_List.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(detalle_List, false);

    }
}
