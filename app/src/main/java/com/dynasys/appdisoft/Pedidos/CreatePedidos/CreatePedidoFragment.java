package com.dynasys.appdisoft.Pedidos.CreatePedidos;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dynasys.appdisoft.Adapter.ClientesAdapter;
import com.dynasys.appdisoft.Adapter.DetalleAdaptader;
import com.dynasys.appdisoft.Adapter.ProductAdapter;
import com.dynasys.appdisoft.Clientes.CreateCliente.CreateClienteFragment;
import com.dynasys.appdisoft.Clientes.ListClientesFragment;
import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Constantes;
import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Login.DB.DescuentosListViewModel;
import com.dynasys.appdisoft.Login.DB.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.DescuentosEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DB.PreciosListViewModel;
import com.dynasys.appdisoft.Login.DB.StockListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Login.LoginActivity;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.Pedidos.ListPedidosFragment;
import com.dynasys.appdisoft.Pedidos.Presentacion.PedidosMvp;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.ShareUtil.LocationGeo;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.common.base.Preconditions;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.jetbrains.annotations.NotNull;

import java.security.Guard;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePedidoFragment extends Fragment implements CreatePedidoMvp.View {
    View view;
    Context context;
    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private AutoCompleteTextView acliente;
    private AutoCompleteTextView aProducto;
    private AlertDialog dialogs,dialogQuestion;
    private Button mbutton_guardar,mbutton_cancelar;
    private ImageButton ObFecha;
    private ClientesListViewModel viewModelCliente;
    private ProductosListViewModel viewModelProducto;
    private PedidoListViewModel viewModelPedido;
    private DescuentosListViewModel viewModelDescuento;
    private StockListViewModel viewModelStock;
    private DetalleListViewModel viewModelDetalle;
    private List<DetalleEntity> mDetalleItem=new ArrayList<>();
    private CreatePedidoMvp.Presenter mCreatePedidoPresenter;
    private List<ClienteEntity> lisCliente;
    private List<ProductoEntity> lisProducto;
    private ClienteEntity mCliente=null;
    private RecyclerView detalle_List;
    ClientesAdapter clientAdapter;
    ProductAdapter productoAdapter;
    private String M_Uii="";
    Boolean Grabado=false;
DetalleAdaptader mDetalleAdapter;
TextView name_total,etFecha,name_descuento,name_descuentoTotal;
EditText tvObservacion;
Date mFecha;
String Hora;
Double mTotal=0.0;
   int tipoActividad=0;
EditText EtReclamo;
    LottieAlertDialog alertDialog;
private PedidoEntity mPedido;
    private NestedScrollView mscroll;
    public CreatePedidoFragment() {
        // Required empty public constructor
    }

    public CreatePedidoFragment(int tipo) {
        // Required empty public constructor
        this.tipoActividad=tipo;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("CREAR PEDIDO");
        context=getContext();
    }
    public void iniciarRecyclerView(){
        mDetalleAdapter = new DetalleAdaptader(context, mDetalleItem,this,getActivity());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        detalle_List.setLayoutManager(llm);
        detalle_List.setAdapter(mDetalleAdapter);
        detalle_List.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(detalle_List, false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_pedido, container, false);
        acliente=view.findViewById(R.id.pedido_buscar_cliente);
        aProducto=view.findViewById(R.id.pedido_buscar_producto);
        detalle_List=view.findViewById(R.id.id_detalle_listPedido);
        name_total=view.findViewById(R.id.pedido_view_Total);
        name_descuento=view.findViewById(R.id.pedido_view_Descuento);
        name_descuentoTotal=view.findViewById(R.id.pedido_view_TotalDescuento);
        EtReclamo=view.findViewById(R.id.edit_Reclamo);
        etFecha=view.findViewById(R.id.et_mostrar_fecha_picker);
        ObFecha=(ImageButton)view.findViewById(R.id.ib_obtener_fecha);
        tvObservacion=(EditText)view.findViewById(R.id.pedido_view_observacion) ;
        mbutton_guardar = (Button)view.findViewById(R.id.id_btn_guardarPedido);
        mbutton_cancelar=(Button)view.findViewById(R.id.id_btn_cancelarPedido);
        mscroll=view.findViewById(R.id.id_order_scroll);
        viewModelCliente = ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        viewModelProducto = ViewModelProviders.of(getActivity()).get(ProductosListViewModel.class);
        viewModelPedido = ViewModelProviders.of(getActivity()).get(PedidoListViewModel.class);
        viewModelDetalle = ViewModelProviders.of(getActivity()).get(DetalleListViewModel.class);
        viewModelDescuento=ViewModelProviders.of(getActivity()).get(DescuentosListViewModel.class);
        viewModelStock=ViewModelProviders.of(getActivity()).get(StockListViewModel.class);
        new CreatePedidoPresenter(this,getContext(),viewModelCliente,viewModelProducto,getActivity(),viewModelPedido,viewModelDetalle,viewModelStock);
        mCreatePedidoPresenter.CargarClientes();
        iniciarRecyclerView();
        onclickObtenerFecha();
        onclickGuardar();
        onclickCancelar();
        etFecha.setText(FormatearFecha(dia,mes+1,anio));
        mFecha=Calendar.getInstance().getTime();
        LocationGeo.getInstance(context,getActivity());
        LocationGeo.iniciarGPS();
        ShowDialogSincronizando();
        CargarDatosclienteMapa();
        return view;
    }
    public void CargarDatosclienteMapa(){
        if (tipoActividad==1){
            mCliente = UtilShare .clienteMapa;
            acliente.setText(mCliente.getNamecliente());
            mCreatePedidoPresenter.CargarProducto(mCliente.getCccat());
        }
    }
    public void onclickGuardar(){
        mbutton_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GuardarMovi();
                GuardarPedido();
            }
        });
    }
    public void showDialogs() {
        ShowDialogSincronizando();
        alertDialog.show();
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
    public void SaveOnline(){
        M_Uii= UUID.randomUUID().toString();
        Calendar c2 = Calendar.getInstance();
        final int hora = c2.get(Calendar.HOUR);
        final int minuto = c2.get(Calendar.MINUTE);
        final int Segundo = c2.get(Calendar.SECOND);
        mPedido=new PedidoEntity();
        mPedido.setOafdoc(mFecha);
        mPedido.setOahora(""+hora+":"+minuto);
        if (mCliente.getNumi()==0) {
            mPedido.setOaccli("" + mCliente.getCodigogenerado());
        }else{
            mPedido.setOaccli(""+mCliente.getNumi());
        }

        mPedido.setCliente(mCliente.getNamecliente());
        int idRepartidor= DataPreferences.getPrefInt("idrepartidor",getContext());
        mPedido.setOarepa(idRepartidor);
        mPedido.setOaest(2);
        mPedido.setOaobs(tvObservacion.getText().toString());
        mPedido.setLatitud((LocationGeo.getLocationActual())==null? 0:LocationGeo.getLocationActual().getLatitude());
        mPedido.setLongitud((LocationGeo.getLocationActual())==null? 0:LocationGeo.getLocationActual().getLongitude());
        mPedido.setTotal(_prObtenerTotal());
        mPedido.setTipocobro(1);
        mPedido.setTotalcredito(0.0);
        mPedido.setEstado(0);
        mPedido.setReclamo(EtReclamo.getText().toString());
        int codigoRepartidor=  DataPreferences.getPrefInt("idrepartidor",getContext());
        //cliente.setCodigogenerado();
        DateFormat df = new SimpleDateFormat("dMMyyyy,HH:mm:ss");
        String code = df.format(Calendar.getInstance().getTime());
        code=""+codigoRepartidor+","+code+"V2.6";
        mPedido.setCodigogenerado(code);
        mPedido.setOanumi(code);
        _prModificarNumi(code);

        List<StockEntity> st=viewModelStock.getMStockAllAsync();
        mCreatePedidoPresenter.GuardarDatos(mDetalleItem,mPedido);

    }
    public void Verificaronline(){
        int idRepartidor=DataPreferences.getPrefInt("idrepartidor",context);

        ApiManager apiManager=ApiManager.getInstance(context);
        apiManager.ObtenerStock(new Callback<List<StockEntity>>() {
            @Override
            public void onResponse(Call<List<StockEntity>> call, Response<List<StockEntity>> response) {
                final List<StockEntity> responseUser = (List<StockEntity>) response.body();
                if (response.code() == 404) {
                    SaveOnline();
                    // mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {
                        viewModelStock.deleteAllStocks();

                    final List<StockEntity > listStockInsert = new ArrayList<>();
                    for (int i = 0; i < responseUser.size(); i++) {
                        StockEntity stock = responseUser.get(i);  //Obtenemos el registro del server
                        //viewModel.insertCliente(cliente);

                                listStockInsert.add(stock);

                                // viewModelStock.insertStock(stock);


                        }
                     viewModelStock.insertListStock(listStockInsert);
                    VerficarStockDisponible(1);
                   /* new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase db = AppDatabase.getDatabase(UtilShare.mActivity.getApplicationContext());
                            db.stockDao().insertList(listStockInsert);
                            VerficarStockDisponible(1);
                        }
                    });*/
                   /* new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase db = AppDatabase.getDatabase(UtilShare.mActivity.getApplicationContext());
                            db.stockDao().insertList(listStockInsert);
                            VerficarStockDisponible(1);
                            // viewModelStock.insertListStock(listStockInsert);
                        }
                    }).start();*/








                } else {
                    SaveOnline();
                    // mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Productos");
                }
            }

            @Override
            public void onFailure(Call<List<StockEntity>> call, Throwable t) {
                SaveOnline();
            }
        },""+idRepartidor);
    }


    public void VerficarStockDisponible(int tipo){
        // viewModelProducto.getMProductoByStock();
        for (int i = 0; i < mDetalleItem.size(); i++) {

            DetalleEntity detail=mDetalleItem.get(i);
            try {
                ProductoEntity p =viewModelProducto.getMProductoByStock(detail.getObcprod());
                if (p!=null){

                        mDetalleItem.get(i).setStock(p.getStock());


                }
            } catch (ExecutionException e) {
            } catch (InterruptedException e) {
            }
        }
        boolean b =true;

        for (int i = 0; i < mDetalleItem.size(); i++) {
            if (mDetalleItem.get(i).getObupdate()>=0){

                if (mDetalleItem.get(i).getObpcant() >mDetalleItem.get(i).getStock()){
                    b=false;
                }
            }

        }
        if (b==true){
            if (tipo==1){
                SaveOnline();
            }

        }else{  //Existen productos sin stock
            Reconstruir();
            ShowMessageResult("Existen Productos que ya no Cuentan con el Stock ingresado");
        }


    }

    public void GuardarPedido(){
        if (mDetalleItem.size()>0){
            if (Grabado ==false){
                if (M_Uii.trim().equals("")){
                    showDialogs();
                    new ChecarNotificaciones().execute();
                }else{
                    ShowMessageResult("El pedido ya ha sido guardado localmente, por favor vuelva hacia atras");
                }

            }else{
                ShowMessageResult("El pedido ya ha sido guardado localmente, por favor vuelva hacia atras");
            }






        }else{
            ShowMessageResult("No Existen Productos Seleccionados");
        }
    }
    public void _prModificarNumi(String Numi){
        for (int i = 0; i < mDetalleItem.size(); i++) {
            mDetalleItem.get(i).setObnumi(Numi);
        }
    }
    public double _prObtenerTotal(){
        double suma=0;
        for (int i = 0; i < mDetalleItem.size(); i++) {
           suma=suma+( mDetalleItem.get(i).getTotal());
        }
        return suma;
    }

    public void onclickCancelar(){
        mbutton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogQuestion=showDialogQuestion("Sus Datos Se Perderan Si Sale De Esta Pantalla \n Esta Seguro de Salir?",true);
                dialogQuestion.show();
            }
        });
    }
    public AlertDialog showDialogQuestion(String Contenido, Boolean flag) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_question, null);

        builder.setView(v);
        TextView Content =(TextView)v.findViewById(R.id.dialog_question_content) ;
        Button aceptar = (Button) v.findViewById(R.id.dialog_btn_ok);
        Button cancelar = (Button) v.findViewById(R.id.dialog_btn_cancel);
        Content.setText(Contenido);
        aceptar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Crear Cuenta...
                        //
                        dialogQuestion.dismiss();
                        RetornarPrincipal();
                        //getFragmentManager().popBackStack();
                        //  finish();
                    }
                }
        );
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogQuestion.dismiss();
            }
        });


        return builder.create();
    }
    public void RetornarPrincipal(){

            MainActivity fca = ((MainActivity) getActivity());
            fca.removeAllFragments();
            Fragment frag = new  ListPedidosFragment(1);
            //fca.switchFragment(frag,"LISTAR_PEDIDOS");
            fca.CambiarFragment(frag, Constantes.TAG_PEDIDOS);




    }
    @Override
    public void MostrarClientes(List<ClienteEntity> clientes) {
            if (clientes.size()>0){
                lisCliente = new ArrayList<>();
                lisCliente.addAll(clientes);
                acliente.setThreshold(1);
                clientAdapter = new ClientesAdapter(getActivity(), R.layout.row_customer, lisCliente);
                acliente.setAdapter(clientAdapter);
                acliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if ((ClienteEntity) adapterView.getItemAtPosition(i)!=null){
                            mCliente = (ClienteEntity) adapterView.getItemAtPosition(i);
                            mCreatePedidoPresenter.CargarProducto(mCliente.getCccat());
                        }


                    }
                });
            }
    }

    @Override
    public void MostrarProductos(List<ProductoEntity> productos) {
        if (productos.size()>0){
            lisProducto = new ArrayList<>();
            lisProducto.addAll(productos);
            aProducto.setThreshold(1);
            productoAdapter = new ProductAdapter(getActivity(), R.layout.row_item, productos);
            aProducto.setAdapter(productoAdapter);
            aProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if ((ProductoEntity) adapterView.getItemAtPosition(i)!=null){
                        ProductoEntity item = (ProductoEntity) adapterView.getItemAtPosition(i);
                       /* this.obnumi = obnumi;
                        this.obcprod = obcprod;
                        this.cadesc = cadesc;
                        this.obpcant = obpcant;
                        this.obpbase = obpbase;
                        this.obptot = obptot;
                        this.estado = estado;*/
                       int CantidadSeleccionada=CantidadDeProductosAgregados();
                       int MaximaCantidadProductos=DataPreferences.getPrefInt("CantidadProducto",context);
                       if(CantidadSeleccionada<MaximaCantidadProductos){
                           int stock= DataPreferences.getPrefInt("stock",context);


                           if (stock>0){
                               if (item.getStock()>0){
                                   DetalleEntity detalle=new DetalleEntity();
                                   detalle.setObnumi("-1");
                                   detalle.setObcprod(item.getNumi());
                                   detalle.setCadesc(item.getProducto());
                                   detalle.setObpcant(1.0);
                                   detalle.setObpbase(item.getPrecio());
                                   detalle.setObptot(item.getPrecio());
                                   detalle.setEstado(false);
                                   detalle.setStock(item.getStock());
                                   detalle.setFamilia(item.getFamilia());
                                   detalle.setObupdate(0);
                                   mDetalleItem.add(detalle);


                                   //mDetalleAdapter.setFilter(mDetalleItem);
                                   Reconstruir();
                                   calcularTotal();
                                   aProducto .setText("");
                                   aProducto.clearFocus();
                                   mscroll.fullScroll(View.FOCUS_DOWN);
                                   productoAdapter.setLista(GetActualProducts());
                                   productoAdapter.notifyDataSetChanged();
                               }else{
                                   hideKeyboard();

                                   aProducto.setText("");
                                   ShowMessageResult("No Existe Stock para seleccionar el producto");
                               }
                           }else{
                               DetalleEntity detalle=new DetalleEntity();
                               detalle.setObnumi("-1");
                               detalle.setObcprod(item.getNumi());
                               detalle.setCadesc(item.getProducto());
                               detalle.setObpcant(1.0);
                               detalle.setObpbase(item.getPrecio());
                               detalle.setObptot(item.getPrecio());
                               detalle.setEstado(false);
                               detalle.setStock(0);
                               detalle.setObupdate(0);
                               detalle.setFamilia(item.getFamilia());
                               mDetalleItem.add(detalle);


                               //mDetalleAdapter.setFilter(mDetalleItem);
                               Reconstruir();
                               calcularTotal();
                               aProducto .setText("");
                               aProducto.clearFocus();
                               mscroll.fullScroll(View.FOCUS_DOWN);
                               productoAdapter.setLista(GetActualProducts());
                               productoAdapter.notifyDataSetChanged();
                           }
                       }else{
                           hideKeyboard();

                           aProducto.setText("");
                           ShowMessageResult("La cantidad Maxima de Productos es "+MaximaCantidadProductos+" Por favor Realize otro Pedido");
                       }




                    }


                }
            });
        }
    }

    public int CantidadDeProductosAgregados(){
        int suma =0;
        for (int i = 0; i < mDetalleItem.size(); i++) {
            if(mDetalleItem.get(i).getObupdate() >=0)
                suma+=1;
        }
        return suma;
    }
    private  void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }
    public void Reconstruir(){
        mDetalleAdapter=null;
        mDetalleAdapter = new DetalleAdaptader(context, mDetalleItem,this,getActivity());
        detalle_List.setAdapter(mDetalleAdapter);
    }
    @Override
    public void setPresenter(CreatePedidoMvp.Presenter presenter) {
        mCreatePedidoPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    public void ModifyItem(int pos, String value, DetalleEntity item, TextView tvsubtotal, EditText eCantidad, LinearLayout fondo) {
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
                    DetalleEntity detalle= mDetalleItem.get(posicion);
                    detalle.setObpcant(cantidad);
                    detalle.setObptot(cantidad*detalle.getObpbase());

                    double total=cantidad*mDetalleItem.get(posicion).getObpbase();
                    tvsubtotal.setText(""+ ShareMethods.redondearDecimales(total,2));

                   // tvsubtotal.setText(""+String.format("%.2f", (cantidad*mDetalleItem.get(posicion).getObpbase())));
                    calcularTotal();
                }else{
                    fondo.setBackgroundColor(getActivity().getResources().getColor(R.color.marfil));
                    DetalleEntity detalle= mDetalleItem.get(posicion);
                    detalle.setObpcant(cantidad);
                    detalle.setObptot(cantidad*detalle.getObpbase());
                    double total=cantidad*mDetalleItem.get(posicion).getObpbase();
                    tvsubtotal.setText(""+ ShareMethods.redondearDecimales(total,2));
                    //tvsubtotal.setText(""+String.format("%.2f", (cantidad*mDetalleItem.get(posicion).getObpbase())));
                    calcularTotal();
                }

            }
        }else{
            if (posicion>=0){
                DetalleEntity detalle= mDetalleItem.get(posicion);
                detalle.setObpcant(cantidad);
                detalle.setObptot(cantidad*detalle.getObpbase());
                double total=cantidad*mDetalleItem.get(posicion).getObpbase();
                tvsubtotal.setText(""+ ShareMethods.redondearDecimales(total,2));

               // tvsubtotal.setText(""+String.format("%.2f", (cantidad*mDetalleItem.get(posicion).getObpbase())));
                calcularTotal();
            }
        }

    }

    @Override
    public void ModifyItemPrecio(int pos, String value, DetalleEntity item, TextView tvsubtotal, EditText ePrecio) {
        double precio=0.0;
        if (isDouble(value)){
            precio=Double.parseDouble(value);
        }
        int posicion =obtenerPosicionItem(item);
        int stock= DataPreferences.getPrefInt("stock",context);

            if (posicion>=0 && precio>0){
                DetalleEntity detalle= mDetalleItem.get(posicion);
                detalle.setObpbase(precio);
                detalle.setObptot(precio*detalle.getObpcant());
               // tvsubtotal.setText(""+String.format("%.2f", (precio*mDetalleItem.get(posicion).getObpcant())));
                double total=precio*mDetalleItem.get(posicion).getObpcant();
                tvsubtotal.setText(""+ ShareMethods.redondearDecimales(total,2));
                calcularTotal();

            }else{
                DetalleEntity detalle= mDetalleItem.get(posicion);
                detalle.setObpbase(precio);
                detalle.setObptot(0);
                double total=precio*mDetalleItem.get(posicion).getObpcant();
                tvsubtotal.setText(""+ ShareMethods.redondearDecimales(total,2));
              //  tvsubtotal.setText(""+String.format("%.2f", (precio*mDetalleItem.get(posicion).getObpcant())));
                calcularTotal();
            }

    }

    @Override
    public void DeleteAndModifyDetailOrder(DetalleEntity item, int pos) {
        if (item !=null){
            mDetalleItem.remove(item);
            // mDetalleAdapter.setFilter(mDetalleItem);
            productoAdapter.setLista(GetActualProducts());
            productoAdapter.notifyDataSetChanged();
            calcularTotal();

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
    public int obtenerPosicionItem(DetalleEntity product){
        for (int i = 0; i <mDetalleItem.size() ; i++) {
            DetalleEntity item=mDetalleItem.get(i);
            if (item.getObcprod()==product.getObcprod()){
                return i;
            }

        }
        return -1;

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
        name_total.setText(""+ ShareMethods.redondearDecimales(total,2)+" Bs");
        mTotal=total;
        CalcularDescuentos();

        double descuentoTotal=0.0;
        for (int i = 0; i < mDetalleItem.size(); i++) {
            descuentoTotal+=(mDetalleItem.get(i).getDescuento());
        }
        name_descuento.setText(""+ ShareMethods.redondearDecimales(descuentoTotal,2)+" Bs");
        double TotalGeneral=0.0;
        for (int i = 0; i < mDetalleItem.size(); i++) {
            TotalGeneral+=(mDetalleItem.get(i).getTotal());
        }
        name_descuentoTotal.setText(""+ ShareMethods.redondearDecimales(TotalGeneral,2)+" Bs");


    }

    public void CalcularDescuentos(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateWithoutTime = null;
        try {
            dateWithoutTime = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {

        }

// Method 2
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        dateWithoutTime = cal.getTime();


        Long FechaActual=dateWithoutTime.getTime();

        double cant,preciod,total2,descuentoTotal=0;


        for (int i = 0; i < mDetalleItem.size(); i++) {
            total2=0;
            DetalleEntity detalle=mDetalleItem.get(i);
            int codigoProducto=detalle.getObcprod();
            double total=detalle.getObptot();
            try {
                List<DescuentosEntity> list=viewModelDescuento.getDescuentosByProducto(codigoProducto);
                cant=detalle.getObpcant();
                if (detalle.getFamilia()==1){  //Aqui buscamos los que no tienen familia que es el id =1

                    for (DescuentosEntity descuento:list ) {

                       Date fechaInicio= descuento.getFechaInicio();
                       Date fechaFin=descuento.getFechaFin();

                       fechaFin.setSeconds(0);
                       fechaFin.setMinutes(0);
                       fechaFin.setHours(0);
                       fechaInicio.setSeconds(0);
                       fechaInicio.setMinutes(0);
                       fechaInicio.setHours(0);

                        if (cant>=descuento.getCantidad1()&& cant<=descuento.getCantidad2()&& FechaActual>=fechaInicio.getTime()
                                && FechaActual<=fechaFin.getTime() ){
                        preciod=descuento.getPrecio();
                        total2=cant*preciod;
                        }
                    }
                    if (total2>0){
                        descuentoTotal=total-total2;
                    }else{
                        descuentoTotal=0;
                    }
                    mDetalleItem.get(i).setDescuento(descuentoTotal);
                    mDetalleItem.get(i).setTotal(total-descuentoTotal);

                    descuentoTotal=0;
                    total2=0;
                }else{
                    //Cálculo de descuentos por familia
                    int familia = detalle.getFamilia();
                    double cantnormal =detalle.getObpcant();
                    double cantf =0;
                    for (int j = 0; j < mDetalleItem.size(); j++) {
                        if(familia==mDetalleItem.get(j).getFamilia()){
                            cantf+=mDetalleItem.get(j).getObpcant();
                        }
                    }

                    for (DescuentosEntity descuento:list ) {
                        Date fechaInicio= descuento.getFechaInicio();
                        Date fechaFin=descuento.getFechaFin();

                        fechaFin.setSeconds(0);
                        fechaFin.setMinutes(0);
                        fechaFin.setHours(0);
                        fechaInicio.setSeconds(0);
                        fechaInicio.setMinutes(0);
                        fechaInicio.setHours(0);
                        if (cantf>=descuento.getCantidad1()&& cantf<=descuento.getCantidad2()&& FechaActual>=fechaInicio.getTime()
                                && FechaActual<=fechaFin.getTime() ){
                            preciod=descuento.getPrecio();
                            total2=cantnormal*preciod;
                        }
                    }
                    if (total2>0){
                        descuentoTotal=total-total2;
                    }else{
                        descuentoTotal=0;
                    }
                    mDetalleItem.get(i).setDescuento(descuentoTotal);
                    mDetalleItem.get(i).setTotal(total-descuentoTotal);

                    descuentoTotal=0;
                    total2=0;



                }



            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        
    }
    public List<ProductoEntity> GetActualProducts()  {
        List <ProductoEntity> lista=new ArrayList<>();
        for (int i = 0; i < lisProducto.size(); i++) {
            try{
                ProductoEntity item=lisProducto.get(i).clone();
                if (!ExistsItem(item)){
                    lista.add(item);
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }


        }
        return lista;
    }
    public boolean ExistsItem(ProductoEntity product){
        int i=0;
        while(i<mDetalleItem.size()){
            DetalleEntity item=mDetalleItem.get(i);
            if (item.getObcprod()==product.getNumi()){
                return true;
            }
            i++;
        }

        return false;
    }
    public String FormatearFecha (int dayOfMonth,int mesActual,int year){
        //Formateo el día obtenido: antepone el 0 si son menores de 10
        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
        //Formateo el mes obtenido: antepone el 0 si son menores de 10
        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
        return diaFormateado + BARRA + mesFormateado + BARRA + year;
    }
    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                etFecha.setText(FormatearFecha(dayOfMonth ,mesActual ,year));
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                mFecha=calendar.getTime();
            }

        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }
    public void onclickObtenerFecha(){
        ObFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ib_obtener_fecha:
                        obtenerFecha();
                        break;
                }
            }
        });
    }

    @Override
    public void ShowMessageResult(String message) {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        alertDialog=new LottieAlertDialog.Builder(getContext(),DialogTypes.TYPE_WARNING)
                .setTitle("Advertencia")
                .setDescription(message)
                .setPositiveText("Aceptar")
                .setPositiveButtonColor(Color.parseColor("#008ebe"))
                .setPositiveTextColor(Color.parseColor("#ffffff"))
                .setPositiveListener(new ClickListener() {
                    @Override
                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();
                    }
                }).build();
        alertDialog.show();
    }
    public void PreguntarNuevoPedido(String message) {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        alertDialog=new LottieAlertDialog.Builder(getContext(),DialogTypes.TYPE_WARNING)
                .setTitle("Advertencia")
                .setDescription(message)
                .setPositiveText("Aceptar")
                .setNegativeText("Cancelar")
                .setNegativeTextColor(Color.parseColor("#ffffff"))
                .setPositiveButtonColor(Color.parseColor("#008ebe"))
                .setPositiveTextColor(Color.parseColor("#ffffff"))
                .setNegativeListener(new ClickListener() {
                    @Override
                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();
                        RetornarPrincipal();
                    }
                })
                .setPositiveListener(new ClickListener() {
                    @Override
                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();
                        MainActivity fca = ((MainActivity) getActivity());
                        fca.removeAllFragments();
                        UtilShare.clienteMapa =mCliente;
                        Fragment frag = new CreatePedidoFragment(1);
                        fca.switchFragment(frag,"CREATE_PEDIDOS");
                    }
                }).build();

        alertDialog.show();
    }
    @Override
    public void showSaveResultOption(int codigo, String id, String mensaje) {

        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        M_Uii="";
        switch (codigo){
            case 0:
                Grabado=true;
                dialogs= showCustomDialog("El Pedido ha sido guardado localmente con exito. Pero no pudo ser guardado en el servidor por problemas de red"
                        ,true);
                dialogs.setCancelable(false);
                dialogs.show();
                break;
            case 1:
                Grabado=true;
                dialogs= showCustomDialog("El Pedido Nro:"+id+" ha sido guardado localmente y en el servidor" +
                        " con exito.",true);
                dialogs.setCancelable(false);
                dialogs.show();
                break;
            case 2:
                dialogs= showCustomDialog("El PedidoEntity #"+id+" ha sido guardado localmente con exito. Existen problemas" +
                        " en la exportacion:\n" + mensaje,true);
                dialogs.setCancelable(false);
                dialogs.show();
                break;
            case 3:
                dialogs= showCustomDialog("Existe un problema al guardar el pedido localmente:\n"  + mensaje,false);
                dialogs.setCancelable(false);
                dialogs.show();
                break;
        }
    }

    @Override
    public void showDataDetail(List<DetalleEntity> listDetalle) {

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
                        int MaximaCantidadProductos=DataPreferences.getPrefInt("CantidadProducto",context);
                        if(CantidadDeProductosAgregados()==MaximaCantidadProductos){

                            PreguntarNuevoPedido("Desea Crea un Nuevo Pedido Para El cliente "+ mCliente.getNamecliente() );
                        }else{
                            RetornarPrincipal();
                        }



                        //  finish();
                    }
                }
        );


        return builder.create();
    }
    private void ShowDialogSincronizando(){
      /*  progresdialog=new ProgressDialog(getContext());
        progresdialog.setCancelable(false);
        progresdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresdialog.setIndeterminate(false);
        Drawable drawable = new ProgressBar(getActivity()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent),
                PorterDuff.Mode.SRC_IN);
        progresdialog.setIndeterminateDrawable(drawable);
        progresdialog.setMessage("Guardando Pedido .....");*/
        try
        {

            alertDialog = new LottieAlertDialog.Builder(getContext(), DialogTypes.TYPE_LOADING).setTitle("Pedidos")
                    .setDescription("Guardando Pedido ...")
                    .build();

            alertDialog.setCancelable(false);
        }catch (Error e){

            String d=e.getMessage();

        }
    }
    private class ChecarNotificaciones extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            if (isOnline()){
                Verificaronline();
            }else{

                Verificaronline();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            //NUESTRO CODIGO
            new Handler().postDelayed(new Runnable() {
                public void run() {

                }
            }, 1 * 2000);
            super.onPostExecute(result);
        }
    }
}
