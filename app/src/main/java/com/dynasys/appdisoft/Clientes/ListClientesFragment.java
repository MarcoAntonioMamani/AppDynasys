package com.dynasys.appdisoft.Clientes;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dynasys.appdisoft.Clientes.Adapter.AdapterClientes;
import com.dynasys.appdisoft.Clientes.CreateCliente.CreateClienteFragment;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.ShareUtil.LocationGeo;
import com.dynasys.appdisoft.ShareUtil.ServiceSincronizacion;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Unbinder;


public class ListClientesFragment extends Fragment
        implements SearchView.OnQueryTextListener,ClienteMvp.View {

private List<ClienteEntity> lisClientes=new ArrayList<>();
    View view;
    Context context;
    RecyclerView recList;
    public AdapterClientes adapterPerfil;
    private FloatingActionButton btnAddCliente;
    SearchView simpleSearchView;
    private  ClientesListViewModel viewModel;
    private Unbinder unbinder;
    public static final String TAG = ListClientesFragment.class.getSimpleName();
    public ListClientesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Clientes");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        LocationGeo.getInstance(getContext(),getActivity());
        LocationGeo.PedirPermisoApp();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_clientes, container, false);
        recList = (RecyclerView) view.findViewById(R.id.Client_CardList);
        recList.setHasFixedSize(true);
        simpleSearchView = (SearchView) view.findViewById (R.id.simpleSearchView);
        btnAddCliente=(FloatingActionButton)view.findViewById(R.id.view_btnadd) ;
        simpleSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        simpleSearchView.setIconifiedByDefault(false);

        _CargarCliente();
       // recList.requestFocus();
        _OnClickBtnAddCliente();
        if (!ShareMethods.IsServiceRunning(getContext(), ServiceSincronizacion.class)){
            UtilShare.mActivity=getActivity();
            Intent intent = new Intent(getContext(),new ServiceSincronizacion(viewModel,getActivity()).getClass());
            getContext().startService(intent);
        }
        return view;
    }
public void _OnClickBtnAddCliente(){
    btnAddCliente.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment frag = new CreateClienteFragment();
            MainActivity fca = (MainActivity) getActivity();
            fca.switchFragment(frag,"CREATE_CLIENTE");
        }
    });
}
    public void _CargarCliente(){

        viewModel = ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        viewModel.getClientes().observe((LifecycleOwner) getActivity(), new Observer<List<ClienteEntity>>() {
            @Override
            public void onChanged(@Nullable List<ClienteEntity> notes) {
                try{
                    lisClientes=FiltarByZona(notes);

                    Collections.sort(lisClientes);
                    CargarRecycler(lisClientes);
                }catch(Exception e){

                }



            }  });
        recList.requestFocus();
    }

    public List<ClienteEntity> FiltarByZona(List<ClienteEntity> list){

        int idzona= DataPreferences.getPrefInt("zona",getContext());
        List<ClienteEntity> listClie=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ClienteEntity cliente=list.get(i);
            if (cliente.getCczona() ==idzona){
                listClie.add(cliente);
            }
        }
        return listClie;
    }
    public void CargarRecycler(List<ClienteEntity> listCliente){
        if (listCliente!=null){
            adapterPerfil = new AdapterClientes(context,lisClientes,this);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), R.anim.layout_animation_fall_down);
            recList.setLayoutAnimation(controller);
            recList.setLayoutManager(llm);
            recList.setAdapter(adapterPerfil);

            recList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0 && btnAddCliente.getVisibility() == View.VISIBLE) {
                        btnAddCliente.hide();
                    } else if (dy < 0 && btnAddCliente.getVisibility() != View.VISIBLE) {
                        btnAddCliente.show();
                    }
                }
            });
        }

    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try{
            List<ClienteEntity> ListaFiltrada=filter(lisClientes,newText);
            adapterPerfil.setFilter(ListaFiltrada);

        }catch (Exception e){

        }
        return false;
    }
    public  void Refresh(){
        if(recList!=null){
            recList.scrollToPosition(0);
        }
    }


    public List<ClienteEntity> filter (List<ClienteEntity> bares ,String texto){
        List<ClienteEntity>ListaFiltrada=new ArrayList<>();
        try{
            texto=texto.toLowerCase();
            for (ClienteEntity b:bares){
                String name=b.getNamecliente().toLowerCase();
                if(name.contains(texto)){
                    ListaFiltrada.add(b);
                }
            }
        }catch (Exception e){

        }
        return ListaFiltrada;
    }


    @Override
    public void recyclerViewListClicked(View v,  ClienteEntity cliente) {
        if (cliente!=null){

          if (cliente.getLatitud()==0 || cliente.getLongitud()==0){
              ShowMessageResult("El usuario seleccionado no tiene registrado una ubicación");
            }else{
              UtilShare.cliente=cliente;
              MainActivity fca = ((MainActivity) getActivity());
              fca.startActivity(new Intent(getActivity(), MapClientActivity .class));
              fca.overridePendingTransition(R.transition.left_in, R.transition.left_out);
          }
        }
    }
    public void ShowMessageResult(String message) {
        Snackbar snackbar= Snackbar.make(recList, message, Snackbar.LENGTH_LONG);
        View snackbar_view=snackbar.getView();
        TextView snackbar_text=(TextView)snackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_iinfo,0);
        snackbar_text.setGravity(Gravity.CENTER);
        snackbar.show();
    }
}
