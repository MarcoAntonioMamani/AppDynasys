package com.dynasys.appdisoft.Pedidos.carrito;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.dynasys.appdisoft.Login.DB.Entity.PedidoDetalle;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioCategoriaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.TipoNegocioEntity;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListProductsFragment extends Fragment implements ProductorMvp.View, SearchView.OnQueryTextListener{

    private ProductorMvp.Presenter mSincronizarPresenter;
    LottieAlertDialog alertDialog;
    int VentaDirectaOPedido =0;

    public CategoriaAdapter adapterCategoria;
    public ProductoCarritoAdapter adapterProductos;
    RecyclerView recListCategoria;
    RecyclerView recListProductos;
    Context context;

    SearchView simpleSearchView;
    List<ProductoEntity> listProductos;
    private ProductosListViewModel viewModelProducto;
    List<Categorias> ListCategorias;
    LinearLayout linearTotal;
    List<PedidoDetalle> listDetalle;
    TextView CantidadPedido;
    TextView TotalPedido;
    LinearLayout btnCarrito;
    PrecioCategoriaEntity precio;
    int ProveedorId;
    String NombreProveedor;
    public ListProductsFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public ListProductsFragment(PrecioCategoriaEntity precio, int proveedorId, String NombreProveedor, int VentaDirectaOPedido) {
        // Required empty public constructor
        this.precio=precio;
        this.ProveedorId=proveedorId;
        this.NombreProveedor=NombreProveedor;
        this.VentaDirectaOPedido=VentaDirectaOPedido;


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(NombreProveedor+"-"+precio.getNombre());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_products, container, false);
        recListCategoria=(RecyclerView) view.findViewById(R.id.Productos_CardCategoria);
        recListCategoria.setHasFixedSize(true);
        recListProductos=(RecyclerView) view.findViewById(R.id.Productos_CardOrder);
        recListProductos.setHasFixedSize(true);
        linearTotal=(LinearLayout)view.findViewById(R.id.linear_totalorder);
        viewModelProducto = ViewModelProviders.of(getActivity()).get(ProductosListViewModel.class);
        SetearListCategorias();
        CargarRecyclerCategoria(ListCategorias);
        cargarProducto();

        return view;
    }
    public void cargarProducto(){
        try {
        if (VentaDirectaOPedido==0){  //Quiere decir que es solo pedido normal y debe mostrar el stock general
            listProductos= viewModelProducto.getProductoByClienteVentaDirecta(precio.getId());
        }else{
            listProductos= viewModelProducto.getProductoByCliente(precio.getId());
        }
            limpiarCantidad();
            CargarRecycler(listProductos);
        } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }
    }
    public void limpiarCantidad(){
        for (int i = 0; i < listProductos.size(); i++) {
            listProductos.get(i).setCaja(0);
            listProductos.get(i).setTotalUnitario(0);
        }
    }
    public void CargarRecyclerCategoria(List<Categorias> listCliente){
        if (listCliente!=null){
          /* adapterCategoria = new CategoriaAdapter(context,listCliente,this,getActivity());
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), R.anim.layout_animation_fall_down);
            recListCategoria.setLayoutAnimation(controller);
            recListCategoria.setLayoutManager(llm);
            recListCategoria.setAdapter(adapterCategoria);*/

            // Cambia el LinearLayoutManager a GridLayoutManager
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.HORIZONTAL, false);

            recListCategoria.setLayoutManager(gridLayoutManager);
            adapterCategoria = new CategoriaAdapter(context, listCliente, this, getActivity());
            recListCategoria.setAdapter(adapterCategoria);

            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), R.anim.layout_animation_fall_down);
            recListCategoria.setLayoutAnimation(controller);



        }

    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void recyclerViewListClicked(View v, ProductoEntity empresa) {

    }

    @Override
    public void recyclerViewListClickedCategoria(View v, Categorias categoria, TextView tvCategoria) throws CloneNotSupportedException {

        CambiarEstado(categoria);
        List<ProductoEntity>  listaFiltrada=new ArrayList<>();

        if (categoria.getId()!=0){
            for (int i = 0; i < listProductos.size(); i++) {

                if (listProductos.get(i).getIdcategoria()==categoria.getId()){
                    listaFiltrada.add(listProductos.get(i).clone());
                }

            }
            adapterProductos.setFilter(listaFiltrada);
        }else{
            adapterProductos.setFilter(listProductos);
        }






/*  aqui haique filtrar producto
        List<ProductosEntity> list=new ArrayList<>();
        if (empresa.getId()==0){
            adapterPerfil.setFilter(mListEmpresas);
        }else{
            for (int i = 0; i < mListEmpresas.size(); i++) {
                if (mListEmpresas.get(i).getCategoriaId()==empresa.getId()){
                    list.add(mListEmpresas.get(i));
                }
            }
            adapterPerfil.setFilter(list);
        }*/
    }
    public void CargarRecycler(List<ProductoEntity> listCliente){
        if (listCliente!=null){
            adapterProductos = new ProductoCarritoAdapter(context,listCliente,this,getActivity());
            GridLayoutManager  llm = new GridLayoutManager(getActivity(),1);
            llm.setOrientation(GridLayoutManager .VERTICAL);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), R.anim.layout_animation_fall_down);
            recListProductos.setLayoutAnimation(controller);
            recListProductos.setLayoutManager(llm);
            recListProductos.setAdapter(adapterProductos);

            recListProductos.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0 && linearTotal.getVisibility() == View.VISIBLE) {
                        linearTotal.setVisibility(View.GONE);
                    } else if (dy < 0 && linearTotal.getVisibility() != View.VISIBLE) {
                        linearTotal.setVisibility(View.VISIBLE);

                    }
                }
            });
        }

    }

    @Override
    public void setPresenter(ProductorMvp.Presenter presenter) {

    }

    @Override
    public void ShowMessageResult(String message) {

    }

    @Override
    public void MostrarDatos(List<ProductoEntity> listEmpresa) {

    }

    @Override
    public void ShowSyncroMgs(String message) {

    }
    public void SetearListCategorias(){
        ListCategorias=new ArrayList<>();
        Gson gson=new Gson();
        Type type=new TypeToken<List<Categorias>>(){}.getType();
        String data=DataPreferences.getPref("categoriasProductos",getContext());
        ListCategorias=gson.fromJson(data,type);
        ListCategorias.add(0, new Categorias(0,"  Todos  ",1));

    }
    public void CambiarEstado(Categorias categoria){
        List<Categorias> list=new ArrayList<>();
        for (int i = 0; i < ListCategorias.size(); i++) {
            if (ListCategorias.get(i).getId()==categoria.getId()){
                Categorias ca= null;
                try {
                    ca = ListCategorias.get(i).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                ca.setEstado(1);
                list.add(ca);
            }else{
                Categorias ca= null;
                try {
                    ca = ListCategorias.get(i).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                ca.setEstado(0);
                list.add(ca);
            }


        }
        ListCategorias=list;

        adapterCategoria.setFilter(list);
        adapterCategoria.notifyDataSetChanged();

    }
}