package com.dynasys.appdisoft.Pedidos;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.SearchView;

import com.dynasys.appdisoft.Adapter.AdapterPedidos;
import com.dynasys.appdisoft.Clientes.Adapter.AdapterClientes;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DB.PreciosListViewModel;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.Pedidos.CreatePedidos.CreatePedidoFragment;
import com.dynasys.appdisoft.Pedidos.Presentacion.PedidosMvp;
import com.dynasys.appdisoft.Pedidos.Presentacion.PedidosPresenter;
import com.dynasys.appdisoft.Pedidos.ViewPedidos.ViewPedidoFragment;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListPedidosFragment extends Fragment implements PedidosMvp.View {

    private List<ClienteEntity> lisClientes=new ArrayList<>();
    private List<PedidoEntity> listPedidos=new ArrayList<>();
    View view;
    Context context;

    RecyclerView recList;
    public AdapterPedidos adapterPerfil;
    private ClientesListViewModel viewModelClientes;
    private PedidoListViewModel viewModelPedidos;
    private PedidosMvp.Presenter mPedidosPresenter;
    private FloatingActionButton btnAddPedido;
    public ListPedidosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Pedidos Pendientes");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_pedidos, container, false);
        recList = (RecyclerView) view.findViewById(R.id.Pedidos_CardList);
        btnAddPedido=(FloatingActionButton)view.findViewById(R.id.view_btnaddPedidos) ;
        recList.setHasFixedSize(true);
        viewModelPedidos = ViewModelProviders.of(getActivity()).get(PedidoListViewModel.class);
        viewModelClientes = ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        new PedidosPresenter(this,getContext(),viewModelPedidos,getActivity());
        cargarClientes();
        _OnClickBtnAddPedidos();
        return view;
    }
    public void _OnClickBtnAddPedidos(){
        btnAddPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new CreatePedidoFragment();
                MainActivity fca = (MainActivity) getActivity();
                fca.switchFragment(frag,"CREATE_PEDIDOS");
            }
        });
    }
public void cargarClientes(){
    try {
        lisClientes = viewModelClientes.getMAllCliente(1);
        if (lisClientes.size()>0){
            mPedidosPresenter.CargarPedidos();
        }
    } catch (ExecutionException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
    @Override
    public void recyclerViewListClicked(View v, PedidoEntity pedido) {
        if (pedido!=null){
            Fragment frag = new ViewPedidoFragment(pedido);
            MainActivity fca = (MainActivity) getActivity();
            fca.switchFragment(frag,"VIEW_PEDIDOS");
        }

    }

    @Override
    public void MostrarPedidos(List<PedidoEntity> clientes) {
        listPedidos=clientes;
        CargarRecycler(listPedidos);
    }

    @Override
    public void setPresenter(PedidosMvp.Presenter presenter) {
        mPedidosPresenter = Preconditions.checkNotNull(presenter);
    }



    public void CargarRecycler(List<PedidoEntity> listPedidos){
        if (listPedidos!=null){
            adapterPerfil = new AdapterPedidos(getContext(),listPedidos,this,lisClientes);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), R.anim.layout_animation_fall_right);
            recList.setLayoutAnimation(controller);
            recList.setLayoutManager(llm);
            recList.setAdapter(adapterPerfil);


        }

    }
}
