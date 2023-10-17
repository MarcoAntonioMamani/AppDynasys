package com.dynasys.appdisoft.Visitas;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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


import com.dynasys.appdisoft.Adapter.AdapterVisitas;
import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.DB.Entity.VisitaEntity;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.VisitaListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.ShareUtil.LocationGeo;
import com.dynasys.appdisoft.Visitas.Create.CreateVisitaFragment;


import java.util.ArrayList;
import java.util.List;

import butterknife.Unbinder;


public class ListaVisitasFragment extends Fragment implements SearchView.OnQueryTextListener, VisitaMvp.View {


    private List<VisitaEntity> lisVisitas=new ArrayList<>();
    View view;
    Context context;
    RecyclerView recList;
    public AdapterVisitas adapterPerfil;
    private FloatingActionButton btnAddVisita;
    SearchView simpleSearchView;
    private VisitaListViewModel viewModel;
    private Unbinder unbinder;
    public static final String TAG = ListaVisitasFragment.class.getSimpleName();


    public ListaVisitasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Visitas");
        simpleSearchView.setQuery("", false);
        view.requestFocus();
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


        view = inflater.inflate(R.layout.fragment_lista_visitas, container, false);
        recList = (RecyclerView) view.findViewById(R.id.Visita_CardList);
        recList.setHasFixedSize(true);
        simpleSearchView = (SearchView) view.findViewById (R.id.simpleSearchViewVisita);
        btnAddVisita=(FloatingActionButton)view.findViewById(R.id.view_btnaddVisita) ;
        simpleSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        simpleSearchView.setIconifiedByDefault(false);

        _CargarCliente();
        // recList.requestFocus();
        _OnClickBtnAddCliente();


        return view;
    }

    public void _CargarCliente(){

        viewModel = ViewModelProviders.of(getActivity()).get(VisitaListViewModel.class);
        viewModel.getVisita().observe((LifecycleOwner) getActivity(), new Observer<List<VisitaEntity>>() {
            @Override
            public void onChanged(@Nullable List<VisitaEntity> notes) {
                try{
                    // lisClientes=FiltarByZona(notes)
                    lisVisitas=notes;
                    CargarRecycler(lisVisitas);
                }catch(Exception e){

                }



            }  });
        recList.requestFocus();
    }

    public void _OnClickBtnAddCliente(){
        btnAddVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new CreateVisitaFragment();
                MainActivity fca = (MainActivity) getActivity();
                fca.switchFragment(frag,"CREATE_VISITA");
            }
        });
    }


    public void CargarRecycler(List<VisitaEntity> listVisit){
        if (listVisit!=null){
            adapterPerfil = new AdapterVisitas(context,listVisit,this);
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
                    if (dy > 0 && btnAddVisita.getVisibility() == View.VISIBLE) {
                        btnAddVisita.hide();
                    } else if (dy < 0 && btnAddVisita.getVisibility() != View.VISIBLE) {
                        btnAddVisita.show();
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
            List<VisitaEntity> ListaFiltrada=filter(lisVisitas,newText);
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


    public List<VisitaEntity> filter (List<VisitaEntity> bares ,String texto){
        List<VisitaEntity>ListaFiltrada=new ArrayList<>();
        try{
            texto=texto.toLowerCase();
            for (VisitaEntity b:bares){
                String name=b.getNombreCliente().toLowerCase();
                if(name.contains(texto)){
                    ListaFiltrada.add(b);
                }
            }
        }catch (Exception e){

        }
        return ListaFiltrada;
    }


    @Override
    public void recyclerViewListClicked(View v,  VisitaEntity cliente) {
        if (cliente!=null){
            UtilShare.visita=cliente;
             /* MainActivity fca = ((MainActivity) getActivity());
              fca.startActivity(new Intent(getActivity(), MapClientActivity .class));
              fca.overridePendingTransition(R.transition.left_in, R.transition.left_out);*/
            int isUpdate=DataPreferences.getPrefInt("UpdateCliente",getContext());

            Fragment frag = new CreateVisitaFragment(1,cliente,isUpdate);
            MainActivity fca = (MainActivity) getActivity();
            fca.switchFragment(frag,"UpdateClientes");
        }
    }

    @Override
    public void WhatsappClicked(View v, VisitaEntity Visita) {
        if (!Visita.getTelefono().toString() .isEmpty()){
            String url="https://api.whatsapp.com/send?phone=591"+Visita.getTelefono()+"&text="+" ";
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
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