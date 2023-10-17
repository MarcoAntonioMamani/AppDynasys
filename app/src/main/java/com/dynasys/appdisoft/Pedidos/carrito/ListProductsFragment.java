package com.dynasys.appdisoft.Pedidos.carrito;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoDetalle;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioCategoriaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.TipoNegocioEntity;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
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
    Boolean BanderaCantidad=false;
    Boolean BanderaCaja=false;
    public CategoriaAdapter adapterCategoria;
    public ProductoCarritoAdapter adapterProductos;
    RecyclerView recListCategoria;
    RecyclerView recListProductos;
    Context context;
    Double mTotal=0.0;
    SearchView simpleSearchView;
    List<ProductoEntity> listProductos;
    private List<DetalleEntity> mDetalleItem=new ArrayList<>();
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
        context=getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_products, container, false);
        recListCategoria=(RecyclerView) view.findViewById(R.id.Productos_CardCategoria);
        recListCategoria.setHasFixedSize(true);
        CantidadPedido=(TextView)view.findViewById(R.id.cantidad_order);
        TotalPedido=(TextView)view.findViewById(R.id.total_order);
        recListProductos=(RecyclerView) view.findViewById(R.id.Productos_CardOrder);
        recListProductos.setHasFixedSize(true);
        linearTotal=(LinearLayout)view.findViewById(R.id.linear_totalorder);
        viewModelProducto = ViewModelProviders.of(getActivity()).get(ProductosListViewModel.class);
        cargarProducto();
        SetearListCategorias();
        CargarRecyclerCategoria(ListCategorias);


        return view;
    }
    public void cargarProducto(){
        try {
        if (VentaDirectaOPedido==0){  //Quiere decir que es solo pedido normal y debe mostrar el stock general
            listProductos= viewModelProducto.getProductoByClienteVentaDirecta(precio.getId());
        }else{
            listProductos= viewModelProducto.getProductoByCliente(precio.getId());
        }
        if (listProductos.size()>0){
            List listP=new ArrayList();
            for (int i = 0; i < listProductos.size(); i++) {
                if (listProductos.get(i).getIdProveedor()==ProveedorId){
                    listP.add(listProductos.get(i));
                }
            }
            listProductos=listP;
            limpiarCantidad();
            CargarRecycler(listProductos);
        }

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
            int cantidadFilas=0;
            if (ListCategorias.size()>6){
                cantidadFilas=3;
            }else{
                cantidadFilas=2;
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), cantidadFilas, LinearLayoutManager.HORIZONTAL, false);

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
            CargarRecycler(listaFiltrada);
            //adapterProductos.setFilter(listaFiltrada);

        }else{
            CargarRecycler(listProductos);
           // adapterProductos.setFilter(listProductos);

        }
        hideKeyboard();





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

    public DetalleEntity ExisteItem(int ProductoId){

        for (int i = 0; i <mDetalleItem.size() ; i++) {
            DetalleEntity item=mDetalleItem.get(i);
            if (item.getObcprod()==ProductoId){
                return item;
            }

        }
        return null;

    }
    @Override
    public void ModifyItemCart( String value, ProductoEntity item, EditText eCantidad,EditText eCatidadCajas) {
        BanderaCantidad=true;
        if (BanderaCaja==false){  //Pregunto para saber si se esta modificando desde el componente de Caja
            DetalleEntity detalle=ExisteItem(item.getNumi());
            if (detalle==null){
                 detalle=new DetalleEntity();
                detalle.setObnumi("-1");
                detalle.setObcprod(item.getNumi());
                detalle.setCadesc(item.getProducto());
                detalle.setObpcant(0.0);
                detalle.setObpbase(item.getPrecio());
                detalle.setObptot(item.getPrecio());
                detalle.setTotal(item.getPrecio());
                detalle.setEstado(false);
                detalle.setStock(item.getStock());
                detalle.setFamilia(item.getFamilia());
                double CantCajaValue =0;
                if (item.getConversion()==1.0){

                    CantCajaValue=1;
                }else{
                    double Conversion=item.getConversion();
                    double CantCaja= (double) (1/Conversion);


                    CantCajaValue=CantCaja;
                }


                detalle.setCajas(CantCajaValue);
                detalle.setConversion(item.getConversion());
                mDetalleItem.add(detalle);
            }
            ModificarDetalleVentaUnitario(detalle,value,eCantidad,eCatidadCajas);
        }

        BanderaCantidad=false;
    }




    @Override
    public void ModifyItemCajaCart( String value, ProductoEntity item, EditText eCantidad,EditText eCatidadCajas) {
        if (BanderaCantidad==false){
            BanderaCaja=true;
            DetalleEntity detalle=ExisteItem(item.getNumi());
            if (detalle==null){
                detalle=new DetalleEntity();
                detalle.setObnumi("-1");
                detalle.setObcprod(item.getNumi());
                detalle.setCadesc(item.getProducto());
                detalle.setObpcant(0.0);
                detalle.setObpbase(item.getPrecio());
                detalle.setObptot(item.getPrecio());
                detalle.setTotal(item.getPrecio());
                detalle.setEstado(false);
                detalle.setStock(item.getStock());
                detalle.setFamilia(item.getFamilia());
                double CantCajaValue =0;
                if (item.getConversion()==1.0){

                    CantCajaValue=1;
                }else{
                    double Conversion=item.getConversion();
                    double CantCaja= (double) (1/Conversion);


                    CantCajaValue=CantCaja;
                }


                detalle.setCajas(CantCajaValue);
                detalle.setConversion(item.getConversion());
                mDetalleItem.add(detalle);
            }

            ModificarDetalleVentaCaja(detalle,value,eCantidad,eCatidadCajas);
            BanderaCaja=false;
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


    public void ModificarDetalleVentaUnitario(DetalleEntity item, String value, EditText eCantidad, EditText eCatidadCajas){
        double cantidad=0.0;
        if (isDouble(value)){
            cantidad=Double.parseDouble(value);
        }
        int posicion =obtenerPosicionItem(item);
        int stock= DataPreferences.getPrefInt("stock",context);
        if (stock>0){
            if (posicion>=0){
                if(cantidad> item.getStock()){
                    hideKeyboard();
                    // getActivity().onBackPressed();
                    ShowMessageResult("La cantidad es Superior al Stock Disponible = "+item.getStock());
                    cantidad=1;
                    eCantidad.setText("1");

                    ////////////Caja Logica//////////////////////
                    double CantCajaValue =0;
                    if (item.getConversion()==1.0){
                        eCatidadCajas.setText(""+String.format("%.2f",cantidad));
                        CantCajaValue=cantidad;
                    }else{
                        double Conversion=item.getConversion();
                        double CantCaja= (double) (cantidad/Conversion);
                        eCatidadCajas.setText(""+String.format("%.2f",CantCaja));
                        CantCajaValue=CantCaja;
                    }
                    ////////////Caja Logica////////////////////////////////
                    DetalleEntity detalle= mDetalleItem.get(posicion);
                    detalle.setObpcant(cantidad);
                    detalle.setCajas(CantCajaValue);
                    detalle.setObptot(cantidad*detalle.getObpbase());

                    calcularTotal();
                }else{
                    ////////////Caja Logica//////////////////////
                    double CantCajaValue =0;
                    if (item.getConversion()==1.0){
                        eCatidadCajas.setText(""+String.format("%.2f",cantidad));
                        CantCajaValue=cantidad;
                    }else{
                        double Conversion=item.getConversion();
                        double CantCaja= (double) (cantidad/Conversion);
                        eCatidadCajas.setText(""+String.format("%.2f",CantCaja));
                        CantCajaValue=CantCaja;
                    }
                    ////////////Caja Logica//////////////////////
                    DetalleEntity detalle= mDetalleItem.get(posicion);
                    detalle.setObpcant(cantidad);
                    detalle.setCajas(CantCajaValue);
                    detalle.setObptot(cantidad*detalle.getObpbase());
                    calcularTotal();
                }

            }
        }else{
            if (posicion>=0){

                ////////////Caja Logica//////////////////////
                double CantCajaValue =0;
                if (item.getConversion()==1.0){
                    eCatidadCajas.setText(""+String.format("%.2f",cantidad));
                    CantCajaValue=cantidad;
                }else{
                    double Conversion=item.getConversion();
                    double CantCaja= (double) (cantidad/Conversion);
                    eCatidadCajas.setText(String.format("%.2f",CantCaja));
                    CantCajaValue=CantCaja;
                }
                ////////////Caja Logica//////////////////////
                DetalleEntity detalle= mDetalleItem.get(posicion);
                detalle.setObpcant(cantidad);
                detalle.setCajas(CantCajaValue);
                detalle.setObptot(cantidad*detalle.getObpbase());
                calcularTotal();
            }
        }
    }
    public void ModificarDetalleVentaCaja( DetalleEntity item, String value, EditText eCantidad, EditText eCatidadCajas){
        double cantidad=0.0;
        if (isDouble(value)){

            double ParteEnteraCaja=(double)Double.parseDouble(value);
            double CantUnitCaja=(double)(ParteEnteraCaja*item.getConversion());
            cantidad=CantUnitCaja;
        }else{
            value="0.00";
        }
        int posicion =obtenerPosicionItem(item);
        int stock= DataPreferences.getPrefInt("stock",context);
        if (stock>0){
            if (posicion>=0){
                if(cantidad> item.getStock()){
                    hideKeyboard();
                    // getActivity().onBackPressed();
                    ShowMessageResult("La cantidad es Superior al Stock Disponible = "+item.getStock());
                    cantidad=1;
                    eCantidad.setText("1");

                    ////////////Caja Logica//////////////////////
                    double CantCajaValue =0;
                    if (item.getConversion()==1.0){

                        CantCajaValue=cantidad;
                    }else{
                        double Conversion=item.getConversion();
                        double CantCaja= (double) (cantidad/Conversion);


                        eCatidadCajas.setText(String.format("%.2f",CantCaja));
                        CantCajaValue=CantCaja;
                    }

                    ////////////Caja Logica////////////////////////////////
                    DetalleEntity detalle= mDetalleItem.get(posicion);
                    detalle.setObpcant(cantidad);
                    detalle.setCajas(CantCajaValue);
                    detalle.setObptot(cantidad*detalle.getObpbase());

                    calcularTotal();
                }else{
                    eCantidad.setText(""+String.format("%.2f", (cantidad)));
                    DetalleEntity detalle= mDetalleItem.get(posicion);
                    detalle.setObpcant(cantidad);
                    detalle.setCajas(Double.parseDouble(value));
                    detalle.setObptot(cantidad*detalle.getObpbase());

                    calcularTotal();
                }

            }
        }else{
            if (posicion>=0){

                eCantidad.setText(""+String.format("%.2f", (cantidad)));
                ////////////Caja Logica//////////////////////
                DetalleEntity detalle= mDetalleItem.get(posicion);
                detalle.setObpcant(cantidad);
                detalle.setCajas(Double.parseDouble(value));
                detalle.setObptot(cantidad*detalle.getObpbase());

                calcularTotal();
            }
        }
    }

    public void calcularTotal(){
        double descuento=0;

        double total=0.0;
        for (int i = 0; i < mDetalleItem.size(); i++) {
            total+=(mDetalleItem.get(i).getObpcant()*mDetalleItem.get(i).getObpbase());
        }
        total-=descuento;
        if (total<0.0){
            total=0.0;
        }

        mTotal=total;
        // CalcularDescuentos();

        double descuentoTotal=0.0;
        for (int i = 0; i < mDetalleItem.size(); i++) {
            descuentoTotal+=(mDetalleItem.get(i).getDescuento());
        }

        double TotalGeneral=0.0;
        for (int i = 0; i < mDetalleItem.size(); i++) {
            DetalleEntity detalle=mDetalleItem.get(i);
            detalle.setTotal(detalle.getObptot()-detalle.getDescuento());
        }


        for (int i = 0; i < mDetalleItem.size(); i++) {
            TotalGeneral+=(mDetalleItem.get(i).getTotal());
        }
        actualizarCantidad();
        CantidadPedido.setText(mDetalleItem.size()+" Items");
        TotalPedido.setText(""+ ShareMethods.redondearDecimales(TotalGeneral,2)+" Bs");

    }

    public void actualizarCantidad(){

        for (int i = 0; i < listProductos.size(); i++) {
            ProductoEntity pro=listProductos.get(i);
            DetalleEntity detalle=ExisteItem(pro.getNumi());
            if (detalle==null ){
                listProductos.get(i).setCaja(0);
                listProductos.get(i).setTotalUnitario(0);

            }else{
                if (detalle.getObpcant()==0 ){
                    listProductos.get(i).setCaja(0);
                    listProductos.get(i).setTotalUnitario(0);
                }
                if (detalle.getObpcant()>0 ){
                    listProductos.get(i).setCaja(detalle.getCajas());
                    listProductos.get(i).setTotalUnitario(detalle.getObpcant());
                    adapterProductos.setFilter(listProductos);
                    //adapterProductos.notifyDataSetChanged();
                }
            }

        }


    }

    public int obtenerPosicionItem(DetalleEntity product){
        for (int i = 0; i <mDetalleItem.size() ; i++) {
            DetalleEntity item=mDetalleItem.get(i);
            if (item.getObcprod()==product.getObcprod()){
                return i;
            }

        }
        return -1;

    }
    private  void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
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
        List listCa=new ArrayList();
        for (int i = 0; i < ListCategorias.size(); i++) {
            if (ExisteCategoria(ListCategorias.get(i).getId(),ProveedorId)){
                listCa.add(ListCategorias.get(i));
            }
        }
        ListCategorias=listCa;
        ListCategorias.add(0, new Categorias(0,"  Todos  ",1));



    }

    public boolean ExisteCategoria(int idCategoria,int proveedorId ){

        for (int i = 0; i < listProductos.size(); i++) {
            if (listProductos.get(i).getIdcategoria() ==idCategoria && listProductos.get(i).getIdProveedor()==proveedorId){
                return true;
            }
        }
        return false;
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