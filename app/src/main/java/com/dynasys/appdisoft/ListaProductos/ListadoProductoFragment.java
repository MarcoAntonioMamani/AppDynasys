package com.dynasys.appdisoft.ListaProductos;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.dynasys.appdisoft.Adapter.AdapterDetalleListaProducto;
import com.dynasys.appdisoft.Clientes.CreateCliente.CreateClienteFragment;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoViewEntity;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.ProductoViewListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class ListadoProductoFragment extends Fragment implements SearchView.OnQueryTextListener,ListadoProductoMVP.View  {

    private List<ProductoViewEntity> listProductos=new ArrayList<>();
    View view;
    Context context;
    RecyclerView recList;
    LottieAlertDialog alertDialog;
    public AdapterDetalleListaProducto adapterPerfil;

    SearchView simpleSearchView;

    private ProductoViewListViewModel viewModelProducto;


    public ListadoProductoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        context=getContext();
        getActivity().setTitle("Lista Producto");
        simpleSearchView.setQuery("", false);
        view.requestFocus();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context=getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_listado_producto, container, false);
        recList = (RecyclerView) view.findViewById(R.id.id_detalle_lista_productos);
        recList.setHasFixedSize(true);
        simpleSearchView = (SearchView) view.findViewById (R.id.simpleSearchViewListaProducto);
        viewModelProducto = ViewModelProviders.of(getActivity()).get(ProductoViewListViewModel.class);
        simpleSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        simpleSearchView.setIconifiedByDefault(false);
        context=getContext();
        try {

            cargarDatos();
        } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }





        return view;

    }

    private  void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }
    public void showDialogs() {
        ShowDialogSincronizando();
        alertDialog.show();
    }

    private void ShowDialogSincronizando(){


        try
        {

            alertDialog = new LottieAlertDialog.Builder(getContext(), DialogTypes.TYPE_LOADING).setTitle("Listado Productos")
                    .setDescription("Cargando datos .....")
                    .build();

            alertDialog.setCancelable(false);
        }catch (Error e){

            String d=e.getMessage();

        }

    }

    public void cargarDatos() throws ExecutionException, InterruptedException {

        showDialogs();
        new ChecarNotificaciones().execute();




       /* viewModelProducto.getProductoView().observe((LifecycleOwner) getActivity(), new Observer<List<ProductoViewEntity>>() {
            @Override
            public void onChanged(@Nullable List<ProductoViewEntity> notes) {
                try{
                    // lisClientes=FiltarByZona(notes)
                    listProductos=notes;
                    Collections.sort(listProductos);
                    CargarRecycler(listProductos);
                }catch(Exception e){

                }



            }  });*/
     //   CargarRecycler(listProductos);
    }

    public void CargarRecycler(List<ProductoViewEntity> listEfectivo){


        if (listEfectivo!=null){

            List<ProductoViewEntity> lli=new ArrayList<>();
            adapterPerfil = new AdapterDetalleListaProducto(context,lli,this);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            /*final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), R.anim.layout_animation_fall_down);
            recList.setLayoutAnimation(controller);*/
            recList.setLayoutManager(llm);
            recList.setAdapter(adapterPerfil);
            //StopDialog();
            adapterPerfil.setFilter(listEfectivo);

            StopDialog();
        }


    }

    @Override
    public void StopDialog() {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        simpleSearchView.setQuery("", false);
        view.requestFocus();
        hideKeyboard();
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
                public void run() {
                    listProductos=viewModelProducto.getMProductoViewAllAsync();

                    Collections.sort(listProductos);
                    CargarRecycler(listProductos);



                }
            }, 1 * 2000);
            super.onPostExecute(result);
        }
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try{
            List<ProductoViewEntity> ListaFiltrada=filter(listProductos,newText);
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

    public List<ProductoViewEntity> filter (List<ProductoViewEntity> bares ,String texto){
        List<ProductoViewEntity>ListaFiltrada=new ArrayList<>();
        try{
            texto=texto.toLowerCase();
            for (ProductoViewEntity b:bares){
                String name=b.getNombreProducto().toLowerCase();
                if(name.contains(texto)){
                    ListaFiltrada.add(b);
                }
            }
        }catch (Exception e){

        }
        return ListaFiltrada;
    }


}