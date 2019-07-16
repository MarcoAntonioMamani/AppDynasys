package com.dynasys.appdisoft.Pedidos.ViewPedidos;


import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dynasys.appdisoft.Adapter.viewOrdersAdaptader;
import com.dynasys.appdisoft.Login.DB.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Pedidos.Presentacion.PedidosMvp;
import com.dynasys.appdisoft.Pedidos.Presentacion.PedidosPresenter;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
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
    private ViewPedidoMvp.Presenter mPedidosPresenter;
    private viewOrdersAdaptader mviewOrderAdapter;
    private EditText tvCliente,tvObservacion,tvTotalPago;
    private TextView tvFecha,tvMontoTotal;
    RadioButton rEfectivo,rCredito;
    private PedidoEntity mPedido;
    public ViewPedidoFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public ViewPedidoFragment(PedidoEntity pedido) {
        // Required empty public constructor
        this.mPedido=pedido;
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
        tvMontoTotal=(TextView)view.findViewById(R.id.pedido_viewdata_MontoTotal);
        viewModelDetalles = ViewModelProviders.of(getActivity()).get(DetalleListViewModel.class);
        viewModelClientes = ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        new ViewPedidoPresenter(this,getContext(),viewModelDetalles,getActivity());
        iniciarRecyclerView();
        mPedidosPresenter.getDetailOrder(mPedido.getCodigogenerado());

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
        return view;
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
