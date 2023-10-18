package com.dynasys.appdisoft.Pedidos.carrito;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
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

import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoDetalle;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioCategoriaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
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

import okhttp3.internal.Util;

public class ListProductsFragment extends Fragment implements ProductorMvp.View, SearchView.OnQueryTextListener{

    private ProductorMvp.Presenter mSincronizarPresenter;
    LottieAlertDialog alertDialog;
    int VentaDirectaOPedido =0;
    Boolean BanderaCantidad=false;
    int idCategoriaSelected=-1;
    Boolean BanderaCaja=false;
    SearchView simpleSearchView;
    public CategoriaAdapter adapterCategoria;
    public ProductoCarritoAdapter adapterProductos;
    RecyclerView recListCategoria;
    RecyclerView recListProductos;
    private NestedScrollView mscroll;
    Context context;
    Double mTotal=0.0;
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
        listProductos= UtilShare.listProductoCarrito;
        mDetalleItem=UtilShare.DetalleCarrito;
        filtrarListProductos();
    }

    public void filtrarListProductos(){

        List listFilter=new ArrayList();
        for (int i = 0; i < listProductos.size(); i++) {
            ProductoEntity pro=listProductos.get(i);
            DetalleEntity de =ExisteItem(pro.getNumi());
            if (de!=null){
                pro.setTotalUnitario(1);
            }else{
                pro.setTotalUnitario(0);
            }
            listFilter.add(pro);
        }


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
        //recListProductos.setHasFixedSize(true);
        linearTotal=(LinearLayout)view.findViewById(R.id.linear_totalorder);
        idCategoriaSelected=0;
        viewModelProducto = ViewModelProviders.of(getActivity()).get(ProductosListViewModel.class);
        cargarProducto();
        SetearListCategorias();
        CargarRecyclerCategoria(ListCategorias);
        calcularTotal();
        simpleSearchView = (SearchView) view.findViewById (R.id.simpleSearchCart);
        simpleSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        simpleSearchView.setIconifiedByDefault(false);
        return view;
    }
    public void cargarProducto(){

        if (listProductos.size()>0){
            List listP=new ArrayList();
            for (int i = 0; i < listProductos.size(); i++) {
                if (listProductos.get(i).getIdProveedor()==ProveedorId){
                    listP.add(listProductos.get(i).clone());
                }
            }
            listProductos=listP;
            limpiarCantidad();
            CargarRecycler(listProductos);
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
        try{
            List<ProductoEntity>  listaFiltrada=new ArrayList<>();

            if (idCategoriaSelected!=0){
                for (int i = 0; i < listProductos.size(); i++) {
                    if (listProductos.get(i).getIdcategoria()==idCategoriaSelected&&listProductos.get(i).getIdProveedor()==ProveedorId){
                        listaFiltrada.add(listProductos.get(i).clone());
                    }
                }
                adapterProductos.setFilter(listaFiltrada);

            }else{
                adapterProductos.setFilter(listProductos);
            }

        }catch (Exception e){

        }
        return false;
    }
    public List<ProductoEntity> filter (List<ProductoEntity> bares ,String texto){
        List<ProductoEntity>ListaFiltrada=new ArrayList<>();
        try{
            texto=texto.toLowerCase();
            for (ProductoEntity b:bares){
                String name=b.get().toLowerCase();
                if(name.contains(texto)){
                    ListaFiltrada.add(b);
                }
            }
        }catch (Exception e){

        }
        return ListaFiltrada;
    }

    @Override
    public void recyclerViewListClicked(View v, ProductoEntity empresa) {

    }

    @Override
    public void recyclerViewListClickedCategoria(View v, Categorias categoria, TextView tvCategoria) throws CloneNotSupportedException {
        idCategoriaSelected=categoria.id;
        CambiarEstado(categoria);
        List<ProductoEntity>  listaFiltrada=new ArrayList<>();

        if (categoria.getId()!=0){
            for (int i = 0; i < listProductos.size(); i++) {
                if (listProductos.get(i).getIdcategoria()==categoria.getId()&&listProductos.get(i).getIdProveedor()==ProveedorId){
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
    }

    @Override
    public void recyclerViewListClickedProducto(View v, ProductoEntity producto) {

        UtilShare.ProductoSelected=producto;
        getActivity().onBackPressed();
    }

    public void CargarRecycler(List<ProductoEntity> listCliente){
        if (listCliente!=null){
            adapterProductos = new ProductoCarritoAdapter(context,listCliente,this,getActivity());

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recListProductos.setLayoutManager(llm);
            recListProductos.setAdapter(adapterProductos);
           ViewCompat.setNestedScrollingEnabled(recListProductos, false);


          /*  GridLayoutManager  llm = new GridLayoutManager(getActivity(),1);
            llm.setOrientation(GridLayoutManager .VERTICAL);

            recListProductos.setLayoutManager(llm);
            recListProductos.setAdapter(adapterProductos);
*/
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
                if (ProductoId==1){
                    return item;
                }
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

                double cantidad=0.0;
                if (isDouble(value)) {

                    double ParteEnteraCaja = (double) Double.parseDouble(value);
                    double CantUnitCaja = (double) (ParteEnteraCaja * item.getConversion());
                    cantidad = CantUnitCaja;
                }

                if (Double.parseDouble(value)>0){
                    detalle.setObpcant(Double.parseDouble(value));

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

            }

            actualizarCantidad();
            //ModificarDetalleVentaUnitario(detalle,value,eCantidad,eCatidadCajas);
        }

        BanderaCantidad=false;
    }




    @Override
    public void ModifyItemCajaCart( String value, ProductoEntity item, EditText eCantidad,EditText eCatidadCajas) {
        if (BanderaCantidad==false){
            BanderaCaja=true;
            DetalleEntity detalle=ExisteItem(item.getNumi());
            if (detalle==null){
                double cantidad=0.0;
                if (isDouble(value)){
                    cantidad=Double.parseDouble(value);
                }

                if (cantidad>0){
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
                if (listProductos.get(i).getNumi()==1){
                    listProductos.get(i).setCaja(0);
                    listProductos.get(i).setTotalUnitario(0);
                }
                listProductos.get(i).setCaja(0);
                listProductos.get(i).setTotalUnitario(0);

            }else{
                if (detalle.getObpcant()==0 ){
                    if (listProductos.get(i).getNumi()==1){
                        listProductos.get(i).setCaja(0);
                        listProductos.get(i).setTotalUnitario(0);
                    }
                    listProductos.get(i).setCaja(0);
                    listProductos.get(i).setTotalUnitario(0);
                }
                if (detalle.getObpcant()>0 ){

                    if (listProductos.get(i).getNumi()==1){
                        listProductos.get(i).setCaja(detalle.getCajas());
                        listProductos.get(i).setTotalUnitario(detalle.getObpcant());
                    }
                    listProductos.get(i).setCaja(detalle.getCajas());
                    listProductos.get(i).setTotalUnitario(detalle.getObpcant());

                   // adapterProductos.notifyDataSetChanged();
                }
            }

        }
        UtilShare.listProductFiltrado.clear();
        for (int i = 0; i < listProductos.size(); i++) {
            UtilShare.listProductFiltrado.add(listProductos.get(i).clone());
        }

        List listaFiltrada=new ArrayList();
        for (int i = 0; i < listProductos.size(); i++) {

            if (idCategoriaSelected==0){
                if (listProductos.get(i).getIdProveedor()==ProveedorId){
                    listaFiltrada.add(listProductos.get(i).clone());
                }  else{
                    if (idCategoriaSelected==listProductos.get(i).getIdcategoria() &&listProductos.get(i).getIdProveedor()==ProveedorId){
                        listaFiltrada.add(listProductos.get(i).clone());
                    }
                }
            }
            UtilShare.listProductFiltrado =listaFiltrada;
            adapterProductos.setFilter(UtilShare.listProductFiltrado);
          //  adapterProductos.notifyDataSetChanged();
        }

        //adapterProductos.setFilter(UtilShare.listProductCart);
        //adapterProductos.notifyDataSetChanged();
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