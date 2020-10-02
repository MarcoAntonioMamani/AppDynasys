package com.dynasys.appdisoft.ListarDeudas;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.SearchView;
import android.widget.TextView;

import com.dynasys.appdisoft.Adapter.AdapterDeudas;
import com.dynasys.appdisoft.Clientes.Adapter.AdapterClientes;
import com.dynasys.appdisoft.Clientes.ClienteMvp;
import com.dynasys.appdisoft.Clientes.CreateCliente.CreateClienteFragment;
import com.dynasys.appdisoft.Clientes.ListClientesFragment;
import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.ListarDeudas.Pagos.PagosFragment;
import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.ListViewModel.DeudaListaViewModel;
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
import java.util.concurrent.ExecutionException;

import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDeudasFragment extends Fragment     implements SearchView.OnQueryTextListener, DeudasMvp.View {
    private List<DeudaEntity> listDeudas=new ArrayList<>();
    View view;
    Context context;
    RecyclerView recList;
    public AdapterDeudas adapterPerfil;
    SearchView simpleSearchView;
    List<ClienteEntity> listClientes=new ArrayList<>();
    private DeudaListaViewModel viewModel;
    private ClientesListViewModel viewModelCliente;
    private Unbinder unbinder;
    public static final String TAG = ListClientesFragment.class.getSimpleName();
    public ListDeudasFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Cobranzas");
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_deudas, container, false);
        recList = (RecyclerView) view.findViewById(R.id.Deuda_CardList);
        recList.setHasFixedSize(true);
        simpleSearchView = (SearchView) view.findViewById (R.id.simpleSearchViewDeuda);
        simpleSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        simpleSearchView.setIconifiedByDefault(false);

        _CargarDeudas();





        return view;
    }

    public void _CargarDeudas(){

        viewModel = ViewModelProviders.of(getActivity()).get(DeudaListaViewModel.class);
        viewModelCliente= ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        try {
            listClientes=viewModelCliente.getMAllCliente(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        viewModel.getDeuda().observe((LifecycleOwner) getActivity(), new Observer<List<DeudaEntity>>() {
            @Override
            public void onChanged(@Nullable List<DeudaEntity> notes) {
                try{
                    // lisClientes=FiltarByZona(notes)
                    notes=UnificarListadoDeudas(notes);
                    listDeudas=notes;
                    Collections.sort(listDeudas);
                    CargarRecycler(listDeudas);
                }catch(Exception e){

                }



            }  });
        recList.requestFocus();
    }

    public List<DeudaEntity> UnificarListadoDeudas(List<DeudaEntity> list){

        List<DeudaEntity> ListNew=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DeudaEntity deuda=list.get(i);
            if (!ExisteCliente(ListNew,deuda)){
                ListNew.add(deuda);

            }else{
                for (int j = 0; j < ListNew.size(); j++) {
                    if (ListNew.get(j).getClienteId()==deuda.getClienteId()){
                        ListNew.get(j).setPendiente(ListNew.get(j).getPendiente()+deuda.getPendiente());

                    }

                }
            }


        }
        return ListNew;



    }

    public Boolean ExisteCliente(List<DeudaEntity> list,DeudaEntity deuda){

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getClienteId()==deuda.getClienteId()){
                return true;
            }
        }

        return false;
    }

    public void CargarRecycler(List<DeudaEntity> listCliente){
        if (listCliente!=null){
            adapterPerfil = new AdapterDeudas(context,listDeudas,this,listClientes,getActivity());
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), R.anim.layout_animation_fall_down);
            recList.setLayoutAnimation(controller);
            recList.setLayoutManager(llm);
            recList.setAdapter(adapterPerfil);


        }

    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try{
            List<DeudaEntity> ListaFiltrada=filter(listDeudas,newText);
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


    public List<DeudaEntity> filter (List<DeudaEntity> bares ,String texto){
        List<DeudaEntity>ListaFiltrada=new ArrayList<>();
        try{
            texto=texto.toLowerCase();
            for (DeudaEntity b:bares){
                String name=b.getCliente().toLowerCase();
                if(name.contains(texto)){
                    ListaFiltrada.add(b);
                }
            }
        }catch (Exception e){

        }
        return ListaFiltrada;
    }



    public void ShowMessageResult(String message) {
        Snackbar snackbar= Snackbar.make(recList, message, Snackbar.LENGTH_LONG);
        View snackbar_view=snackbar.getView();
        TextView snackbar_text=(TextView)snackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_iinfo,0);
        snackbar_text.setGravity(Gravity.CENTER);
        snackbar.show();
    }

    @Override
    public void recyclerViewListClicked(View v, DeudaEntity pedido) {
        if (pedido!=null){
            UtilShare.deuda=pedido;

           Fragment frag = new PagosFragment(pedido);
            MainActivity fca = (MainActivity) getActivity();
            fca.switchFragment(frag,"UpdateClientes");
        }
    }

    @Override
    public void MostrarDeudas(List<DeudaEntity> clientes) {

    }

    @Override
    public void setPresenter(DeudasMvp.Presenter presenter) {

    }
}
