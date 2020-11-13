package com.dynasys.appdisoft.Pedidos.ModifyPedidos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dynasys.appdisoft.Adapter.ClientesAdapter;
import com.dynasys.appdisoft.Adapter.DetalleAdaptader;
import com.dynasys.appdisoft.Adapter.ProductAdapter;
import com.dynasys.appdisoft.Clientes.MapClientActivity;
import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.Constantes;
import com.dynasys.appdisoft.Login.Cloud.ApiManager;
import com.dynasys.appdisoft.Login.DB.DescuentosListViewModel;
import com.dynasys.appdisoft.Login.DB.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.DescuentosEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DB.StockListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.Pedidos.CreatePedidos.CreatePedidoFragment;
import com.dynasys.appdisoft.Pedidos.CreatePedidos.CreatePedidoMvp;
import com.dynasys.appdisoft.Pedidos.CreatePedidos.CreatePedidoPresenter;
import com.dynasys.appdisoft.Pedidos.ListPedidosFragment;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.ShareUtil.LocationGeo;
import com.dynasys.appdisoft.ShareUtil.ServiceSincronizacion;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.common.base.Preconditions;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyPedidoFragment extends Fragment  implements CreatePedidoMvp.View {

    View view;
    Context context;
    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private EditText acliente;
    private AutoCompleteTextView aProducto;
    private AlertDialog dialogs,dialogQuestion;
    private Button mbutton_update,mbutton_entrega,mbutton_viewcliente;

    private ImageButton ObFecha;
    private ClientesListViewModel viewModelCliente;
    private ProductosListViewModel viewModelProducto;
    private PedidoListViewModel viewModelPedido;
    private DetalleListViewModel viewModelDetalle;
    private DescuentosListViewModel viewModelDescuento;
    private  StockListViewModel viewModelStock;
    private List<DetalleEntity> mDetalleItem=new ArrayList<>();
    private CreatePedidoMvp.Presenter mCreatePedidoPresenter;
    private List<ClienteEntity> lisCliente;
    private List<ProductoEntity> lisProducto;
    RadioButton rEfectivo,rCredito;
    private ClienteEntity mCliente=null;
    private RecyclerView detalle_List;
    ClientesAdapter clientAdapter;
    ProductAdapter productoAdapter;
    DetalleAdaptader mDetalleAdapter;
    TextView name_total,etFecha,name_descuento,name_totalDescuento;
    EditText tvObservacion,tvTotalPago;
    Date mFecha;
    String Hora;
    Double mTotal=0.0;
    private PedidoEntity mPedido;
    private NestedScrollView mscroll;
    LottieAlertDialog alertDialog;
    LinearLayout linearViewCredito;
    EditText EtReclamo;
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("ENTREGA PEDIDO");
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
    public ModifyPedidoFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public ModifyPedidoFragment(PedidoEntity pedido,ClienteEntity mcliente,int tipo) {
        // Required empty public constructor
        this.mPedido=pedido;
        this.mCliente=mcliente;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_modify_pedido, container, false);
        acliente=view.findViewById(R.id.edit_view_cliente);
        aProducto=view.findViewById(R.id.edit_buscar_producto);
        detalle_List=view.findViewById(R.id.edit_view_RecPedidos);
        name_total=view.findViewById(R.id.edit_viewdata_MontoTotal);
        name_descuento=view.findViewById(R.id.edit_view_Descuento);
        name_totalDescuento=view.findViewById(R.id.edit_view_TotalDescuento);
        etFecha=view.findViewById(R.id.edit_viewdata_fecha);
        EtReclamo=view.findViewById(R.id.edit_update_Reclamo);
        ObFecha=(ImageButton)view.findViewById(R.id.edit_obtener_fecha);
        linearViewCredito=view.findViewById(R.id.modify_viewCredito);
        tvObservacion=(EditText)view.findViewById(R.id.edit_view_observacion) ;
        rEfectivo=(RadioButton)view.findViewById(R.id.edit_order_rbt_efectivo) ;
        rCredito=(RadioButton)view.findViewById(R.id.edit_order_rbt_credito);
        mbutton_update = (Button)view.findViewById(R.id.edit_viewdata_btnUpdatePedido);
        mbutton_entrega=(Button)view.findViewById(R.id.edit_viewdata_btnEntregar);
        mbutton_viewcliente=(Button)view.findViewById(R.id.edit_viewdata_btnVerCliente);
        mscroll=view.findViewById(R.id.edit_order_scroll);
        tvTotalPago=(EditText)view.findViewById(R.id.edit_view_totalpago);
        viewModelCliente = ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        viewModelProducto = ViewModelProviders.of(getActivity()).get(ProductosListViewModel.class);
        viewModelPedido = ViewModelProviders.of(getActivity()).get(PedidoListViewModel.class);
        viewModelDetalle = ViewModelProviders.of(getActivity()).get(DetalleListViewModel.class);
        viewModelDescuento=ViewModelProviders.of(getActivity()).get(DescuentosListViewModel.class);
        viewModelStock=ViewModelProviders.of(getActivity()).get(StockListViewModel.class);
        new CreatePedidoPresenter(this,getContext(),viewModelCliente,viewModelProducto,getActivity(),viewModelPedido,viewModelDetalle,viewModelStock);
        iniciarRecyclerView();
        acliente.setText(mCliente.getNamecliente());
        acliente.setEnabled(false);
        onclickObtenerFecha();
        onClickModificar();
        InterpretarDatos();
        onClickEtregar();
        onClickVerCliente();
        OnClickObtenerFecha();
        mFecha=Calendar.getInstance().getTime();
        LocationGeo.getInstance(context,getActivity());
        LocationGeo.iniciarGPS();


        return view;
    }
    public void InterpretarDatos(){
        int categoria =DataPreferences.getPrefInt("CategoriaRepartidor",getContext());
        if (categoria==3){
            mbutton_entrega.setVisibility(View.GONE);
        }
        if(mPedido.getTipocobro()==2){
            rCredito.setChecked(true);
            tvTotalPago.setText(ShareMethods.ObtenerDecimalToString(mPedido.getTotalcredito(),2));
        }
        EtReclamo.setText(mPedido.getReclamo());
        rCredito.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true){
                    tvTotalPago.setText(ShareMethods.ObtenerDecimalToString(_prObtenerTotal(),2));
                }else{
                    tvTotalPago.setText(ShareMethods.ObtenerDecimalToString(0,2));
                }
            }
        });
        mCreatePedidoPresenter.getDetailOrder(mPedido.getCodigogenerado());
        mCreatePedidoPresenter.CargarProducto(mCliente.getCccat());
        ////Para Visualiza la seccion de credito o contado
        int ViewCreditos=DataPreferences.getPrefInt("ViewCredito",getContext());
        if (ViewCreditos ==0){
            linearViewCredito.setVisibility(View.GONE);
        }else{
            linearViewCredito.setVisibility(View.VISIBLE);
        }
    }
public void Saveoffline(){
    try {


        if (ShareMethods.IsServiceRunning(getContext(), ServiceSincronizacion.class)){
            UtilShare.mActivity=getActivity();
            Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
            //mContext.stopService(intent);
            ServiceSincronizacion.getInstance().onDestroy();
        }
        PedidoEntity pedi= viewModelPedido.getPedido(mPedido.getCodigogenerado());
        if (pedi!=null){
            if (mPedido.getEstado()==1){
                pedi.setEstado(2);
            }
            pedi.setReclamo(EtReclamo.getText().toString());
            pedi.setOaobs(tvObservacion.getText().toString());
            pedi.setOafdoc(mFecha);
            pedi.setTotal(ObtenerTotal());

            if (rCredito.isChecked()==true){
                pedi.setTipocobro(2);
                pedi.setTotalcredito(Double.parseDouble(tvTotalPago.getText().toString()));
            }else{
                pedi.setTipocobro(1);
                pedi.setTotalcredito(0.0);
            }
            viewModelPedido.updatePedido(pedi);
            List<DetalleEntity> list=viewModelDetalle.getDetalle(pedi.getCodigogenerado());
            for (int i = 0; i < mDetalleItem.size(); i++) {
                DetalleEntity NewValor=mDetalleItem.get(i);
                boolean bandera=false;
                int j=0;
                while (j < list.size() &&bandera==false) {
                    DetalleEntity detalle=list.get(j);
                    double cant=detalle.getObpcant();
                    if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==2){


                        detalle.setObpcant(NewValor.getObpcant());
                        detalle.setObupdate(NewValor.getObupdate());
                        detalle.setObptot(NewValor.getObptot());
                        detalle.setDescuento(NewValor.getDescuento());
                        detalle.setTotal(NewValor.getTotal());
                        StockEntity st=viewModelStock.getStock(detalle.getObcprod());
                        if (st!=null){
                            double dif=0 ;
                            if (NewValor.getObpcant()>cant){
                                dif=NewValor.getObpcant()-cant;
                                st.setCantidad(st.getCantidad()-dif);
                                viewModelStock.updateStock(st);
                            }
                            ///6 -10
                            if (NewValor.getObpcant()<cant){
                                dif=cant-NewValor.getObpcant();
                                st.setCantidad(st.getCantidad()+dif);
                                viewModelStock.updateStock(st);
                            }

                        }

                        viewModelDetalle.updateDetalle(detalle);
                        bandera=true;
                    }
                    if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==-1){
                        detalle.setObpcant(NewValor.getObpcant());
                        detalle.setObupdate(NewValor.getObupdate());
                        detalle.setObptot(NewValor.getObptot());
                        detalle.setDescuento(NewValor.getDescuento());
                        detalle.setTotal(NewValor.getTotal());
                        StockEntity st=viewModelStock.getStock(detalle.getObcprod());
                        if (st!=null){
                            double dif=0 ;
                            if (NewValor.getObpcant()>cant){
                                dif=NewValor.getObpcant()-cant;
                                st.setCantidad(st.getCantidad()-dif);
                                viewModelStock.updateStock(st);
                            }
                            ///6 -10
                            if (NewValor.getObpcant()<cant){
                                dif=cant-NewValor.getObpcant();
                                st.setCantidad(st.getCantidad()+dif);
                                viewModelStock.updateStock(st);
                            }

                        }
                        viewModelDetalle.updateDetalle(detalle);
                        bandera=true;
                    }
                    if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==-2){
                        StockEntity st=viewModelStock.getStock(detalle.getObcprod());
                        if (st!=null){
                            double dif=detalle.getObpcant() ;

                                st.setCantidad(st.getCantidad()+dif);
                            viewModelStock.updateStock(st);

                        }
                        viewModelDetalle.deleteDetalle(detalle);
                        bandera=true;
                    }
                    if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==0 &&detalle.getId()>0){
                        detalle.setObpcant(NewValor.getObpcant());
                        detalle.setObupdate(NewValor.getObupdate());
                        detalle.setObptot(NewValor.getObptot());
                        detalle.setObnumi(pedi.getCodigogenerado());
                        detalle.setDescuento(NewValor.getDescuento());
                        detalle.setTotal(NewValor.getTotal());
                        StockEntity st=viewModelStock.getStock(detalle.getObcprod());
                        if (st!=null){
                            double dif=0 ;
                            if (NewValor.getObpcant()>cant){
                                dif=NewValor.getObpcant()-cant;
                                st.setCantidad(st.getCantidad()-dif);
                                viewModelStock.updateStock(st);
                            }
                            ///6 -10
                            if (NewValor.getObpcant()<cant){
                                dif=cant-NewValor.getObpcant();
                                st.setCantidad(st.getCantidad()+dif);
                                viewModelStock.updateStock(st);
                            }

                        }
                        viewModelDetalle.updateDetalle(NewValor);
                        bandera=true;
                    }
                    j++;
                }


            }

            for (int i = 0; i < mDetalleItem.size(); i++) {
                DetalleEntity NewValor=mDetalleItem.get(i);
                boolean bandera=false;
                int j=0;
                while (j < list.size() &&bandera==false) {
                    DetalleEntity detalle=list.get(j);
                    if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==2){
                        bandera=true;
                    }
                    if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==-1){
                        bandera=true;
                    }
                    if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==-2){
                        bandera=true;
                    }
                    if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==0){
                        bandera=true;
                    }
                    j++;
                }
                if (bandera==false && NewValor.getObupdate() ==0){
                    NewValor.setObnumi(pedi.getCodigogenerado());
                    StockEntity st=viewModelStock.getStock(NewValor.getObcprod());

                            st.setCantidad(st.getCantidad()-NewValor.getObpcant());
                      viewModelStock.updateStock(st);

                    viewModelDetalle.insertDetalle(NewValor);
                }

            }
            viewModelPedido.updatePedido(pedi);

            if (!ShareMethods.IsServiceRunning(getContext(), ServiceSincronizacion.class)){
                UtilShare.mActivity=getActivity();
                Intent intent = new Intent(getActivity(),new ServiceSincronizacion(viewModelCliente,getActivity()).getClass());
                getContext().startService(intent);
            }
        }
        showSaveResultOption(0,"","");
    } catch (ExecutionException e) {
        if (!ShareMethods.IsServiceRunning(getContext(), ServiceSincronizacion.class)){
            UtilShare.mActivity=getActivity();
            Intent intent = new Intent(getActivity(),new ServiceSincronizacion(viewModelCliente,getActivity()).getClass());
            getContext().startService(intent);
        }
    } catch (InterruptedException e) {
        if (!ShareMethods.IsServiceRunning(getContext(), ServiceSincronizacion.class)){
            UtilShare.mActivity=getActivity();
            Intent intent = new Intent(getActivity(),new ServiceSincronizacion(viewModelCliente,getActivity()).getClass());
            getContext().startService(intent);
        }
    }
}

public void VerficarStockDisponible(int tipo){
       // viewModelProducto.getMProductoByStock();
    for (int i = 0; i < mDetalleItem.size(); i++) {

        DetalleEntity detail=mDetalleItem.get(i);
        try {
            ProductoEntity p =viewModelProducto.getMProductoByStock(detail.getObcprod());
            if (p!=null){
                if (mDetalleItem .get(i).getObupdate()>=1){
                   DetalleEntity d1= ObtenerDetail(viewModelDetalle.getMAllDetalle(1),mDetalleItem.get(i));
                    if (d1!=null){
                        mDetalleItem.get(i).setStock(p.getStock()+d1.getObpcant());
                    }else{
                        mDetalleItem.get(i).setStock(p.getStock());
                    }

                }else{
                    mDetalleItem.get(i).setStock(p.getStock());
                }

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
            Saveoffline();
        }else{
            SaveOffLineEntregar();
        }

    }else{  //Existen productos sin stock
        Reconstruir();
        ShowMessageResult("Existen Productos que ya no Cuentan con el Stock ingresado");
    }


}

public DetalleEntity ObtenerDetail(List<DetalleEntity> list,DetalleEntity d){

    for (int i = 0; i < list.size(); i++) {
        if (list.get(i).getId()==d.getId()){
            return list.get(i);
        }
    }
return null;
}
    public void Verificaronline(){
        int idRepartidor=DataPreferences.getPrefInt("idrepartidor",context);

        ApiManager apiManager=ApiManager.getInstance(context);
        apiManager.ObtenerStock(new Callback<List<StockEntity>>() {
            @Override
            public void onResponse(Call<List<StockEntity>> call, Response<List<StockEntity>> response) {
                final List<StockEntity> responseUser = (List<StockEntity>) response.body();
                if (response.code() == 404) {
                    Saveoffline();
                    // mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {

                        viewModelStock.deleteAllStocks();
                        List<StockEntity> listStock = viewModelStock.getMStockAllAsync();

                        for (int i = 0; i < responseUser.size(); i++) {
                            StockEntity stock = responseUser.get(i);  //Obtenemos el registro del server
                            //viewModel.insertCliente(cliente);
                            StockEntity dbStock = null;
                            try {
                                dbStock = viewModelStock.getStock(stock.getCodigoProducto());
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (dbStock == null) {

                                if (stock.getCantidad()<0){
                                    stock.setCantidad(0);
                                    viewModelStock.insertStock(stock);
                                }else{
                                    viewModelStock.insertStock(stock);
                                }
                            } else {
                                for (int j = 0; j < listStock.size(); j++) {
                                    StockEntity dbStock02=listStock.get(j);

                                    if (stock.getCodigoProducto()==dbStock02.getCodigoProducto()&&stock.getCantidad()!=dbStock02.getCantidad()){
                                       if (stock.getCantidad()<0){
                                           dbStock02.setCantidad(0);
                                           viewModelStock.updateStock(dbStock02);
                                       }else{
                                           dbStock02.setCantidad(stock.getCantidad());
                                           viewModelStock.updateStock(dbStock02);
                                       }


                                    }

                                }
                            }


                        }
                        VerficarStockDisponible(1);




                } else {
                    Saveoffline();
                    // mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Productos");
                }
            }

            @Override
            public void onFailure(Call<List<StockEntity>> call, Throwable t) {
                Saveoffline();
            }
        },""+idRepartidor);
    }

    public void VerificarOnlineEntregar(){
        int idRepartidor=DataPreferences.getPrefInt("idrepartidor",context);

        ApiManager apiManager=ApiManager.getInstance(context);
        apiManager.ObtenerStock(new Callback<List<StockEntity>>() {
            @Override
            public void onResponse(Call<List<StockEntity>> call, Response<List<StockEntity>> response) {
                final List<StockEntity> responseUser = (List<StockEntity>) response.body();
                if (response.code() == 404) {
                    SaveOffLineEntregar();
                    // mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {

                        viewModelStock.deleteAllStocks();
                        List<StockEntity> listStock = viewModelStock.getMStockAllAsync();

                        for (int i = 0; i < responseUser.size(); i++) {
                            StockEntity stock = responseUser.get(i);  //Obtenemos el registro del server
                            //viewModel.insertCliente(cliente);
                            StockEntity dbStock = null;
                            try {
                                dbStock = viewModelStock.getStock(stock.getCodigoProducto());
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (dbStock == null) {
                                if (stock.getCantidad()<0){
                                    stock.setCantidad(0);
                                }
                                viewModelStock.insertStock(stock);
                            } else {
                                for (int j = 0; j < listStock.size(); j++) {
                                    StockEntity dbStock02=listStock.get(j);

                                    if (stock.getCodigoProducto()==dbStock02.getCodigoProducto()&&stock.getCantidad()!=dbStock02.getCantidad()){
                                        if (stock.getCantidad()<0){
                                            dbStock02.setCantidad(0);
                                        }else{{
                                            dbStock02.setCantidad(stock.getCantidad());
                                        }}

                                        viewModelStock.updateStock(dbStock02);
                                    }

                                }
                            }


                        }
                        VerficarStockDisponible(2);




                } else {
                    SaveOffLineEntregar();
                    // mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Productos");
                }
            }

            @Override
            public void onFailure(Call<List<StockEntity>> call, Throwable t) {
                Saveoffline();
            }
        },""+idRepartidor);
    }
    public void onClickModificar(){
        // private Button mbutton_update,mbutton_entrega,mbutton_viewcliente;

        mbutton_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rCredito.isChecked()==true){
                    if (tvTotalPago.getText().toString()==""){
                        ShowMessageResult("El Monto del Credito es mayor al Total de la Venta");
                        return;
                    }
                    if (ObtenerTotal()<Double.parseDouble(tvTotalPago.getText().toString())){
                        ShowMessageResult("El Monto del Credito es mayor al Total de la Venta");
                        return;
                    }
                }

                if (isOnline()){
                    Verificaronline();

                }else{
                    Saveoffline();
                }



            }
        });
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public void SaveOffLineEntregar(){
        if (ShareMethods.IsServiceRunning(getContext(), ServiceSincronizacion.class)){
            UtilShare.mActivity=getActivity();
            Intent intent = new Intent(getContext(),ServiceSincronizacion.getInstance().getClass());
            //mContext.stopService(intent);
            ServiceSincronizacion.getInstance().onDestroy();
        }

        try {



            PedidoEntity pedi= viewModelPedido.getPedido(mPedido.getCodigogenerado());
            if (pedi!=null){
                if (mPedido.getEstado()==1){
                    pedi.setEstado(2);
                }
                pedi.setReclamo(EtReclamo.getText().toString());
                pedi.setOaest(3);
                pedi.setOaobs(tvObservacion.getText().toString());
                pedi.setOafdoc(mFecha);
                pedi.setTotal(ObtenerTotal());
                if (rCredito.isChecked()==true){
                    pedi.setTipocobro(2);
                    pedi.setTotalcredito(Double.parseDouble(tvTotalPago.getText().toString()));
                }else{
                    pedi.setTipocobro(1);
                    pedi.setTotalcredito(0.0);
                }

                viewModelPedido.updatePedido(pedi);
                List<DetalleEntity> list=viewModelDetalle.getDetalle(pedi.getCodigogenerado());
                for (int i = 0; i < mDetalleItem.size(); i++) {
                    DetalleEntity NewValor=mDetalleItem.get(i);
                    boolean bandera=false;
                    int j=0;
                    while (j < list.size() &&bandera==false) {
                        DetalleEntity detalle=list.get(j);
                        double cant=detalle.getObpcant();
                        if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==2){
                            detalle.setObpcant(NewValor.getObpcant());
                            detalle.setObupdate(NewValor.getObupdate());
                            detalle.setObptot(NewValor.getObptot());
                            detalle.setDescuento(NewValor.getDescuento());
                            detalle.setTotal(NewValor.getTotal());
                            StockEntity st=viewModelStock.getStock(detalle.getObcprod());
                            if (st!=null){
                                double dif=0 ;
                                if (NewValor.getObpcant()>cant){
                                    dif=NewValor.getObpcant()-cant;
                                    st.setCantidad(st.getCantidad()-dif);
                                    viewModelStock.updateStock(st);
                                }
                                ///6 -10
                                if (NewValor.getObpcant()<cant){
                                    dif=cant-NewValor.getObpcant();
                                    st.setCantidad(st.getCantidad()+dif);
                                    viewModelStock.updateStock(st);
                                }

                            }
                            viewModelDetalle.updateDetalle(detalle);
                            bandera=true;
                        }
                        if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==-1){
                            detalle.setObpcant(NewValor.getObpcant());
                            detalle.setObupdate(NewValor.getObupdate());
                            detalle.setObptot(NewValor.getObptot());
                            detalle.setDescuento(NewValor.getDescuento());
                            detalle.setTotal(NewValor.getTotal());
                            StockEntity st=viewModelStock.getStock(detalle.getObcprod());
                            if (st!=null){
                                double dif=0 ;
                                if (NewValor.getObpcant()>cant){
                                    dif=NewValor.getObpcant()-cant;
                                    st.setCantidad(st.getCantidad()-dif);
                                    viewModelStock.updateStock(st);
                                }
                                ///6 -10
                                if (NewValor.getObpcant()<cant){
                                    dif=cant-NewValor.getObpcant();
                                    st.setCantidad(st.getCantidad()+dif);
                                    viewModelStock.updateStock(st);
                                }

                            }
                            viewModelDetalle.updateDetalle(detalle);
                            bandera=true;
                        }
                        if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==-2){
                            StockEntity st=viewModelStock.getStock(detalle.getObcprod());

                                    st.setCantidad(st.getCantidad()+detalle.getObpcant());
                                    viewModelStock.updateStock(st);

                            viewModelDetalle.deleteDetalle(detalle);
                            bandera=true;
                        }
                        if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==0){
                            detalle.setObpcant(NewValor.getObpcant());
                            detalle.setObupdate(NewValor.getObupdate());
                            detalle.setObptot(NewValor.getObptot());
                            detalle.setObnumi(pedi.getCodigogenerado());
                            detalle.setDescuento(NewValor.getDescuento());
                            detalle.setTotal(NewValor.getTotal());
                            StockEntity st=viewModelStock.getStock(detalle.getObcprod());
                            if (st!=null){
                                double dif=0 ;
                                if (NewValor.getObpcant()>cant){
                                    dif=NewValor.getObpcant()-cant;
                                    st.setCantidad(st.getCantidad()-dif);
                                    viewModelStock.updateStock(st);
                                }
                                ///6 -10
                                if (NewValor.getObpcant()<cant){
                                    dif=cant-NewValor.getObpcant();
                                    st.setCantidad(st.getCantidad()+dif);
                                    viewModelStock.updateStock(st);
                                }

                            }
                            viewModelDetalle.updateDetalle(NewValor);
                            bandera=true;
                        }
                        j++;
                    }


                }
                for (int i = 0; i < mDetalleItem.size(); i++) {
                    DetalleEntity NewValor=mDetalleItem.get(i);
                    boolean bandera=false;
                    int j=0;
                    while (j < list.size() &&bandera==false) {
                        DetalleEntity detalle=list.get(j);
                        if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==2){
                            bandera=true;
                        }
                        if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==-1){
                            bandera=true;
                        }
                        if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==-2){
                            bandera=true;
                        }
                        if (NewValor.getObcprod()==detalle.getObcprod() && NewValor.getObupdate()==0){
                            bandera=true;
                        }
                        j++;
                    }
                    if (bandera==false && NewValor.getObupdate() ==0){
                        NewValor.setObnumi(pedi.getCodigogenerado());

                        StockEntity st=viewModelStock.getStock(NewValor.getObcprod());
                        if (st!=null){

                                st.setCantidad(st.getCantidad()-NewValor.getObpcant());
                                viewModelStock.updateStock(st);


                        }
                        viewModelDetalle.insertDetalle(NewValor);
                    }

                }


                viewModelPedido.updatePedido(pedi);

                if (!ShareMethods.IsServiceRunning(getContext(), ServiceSincronizacion.class)){
                    UtilShare.mActivity=getActivity();
                    Intent intent = new Intent(getActivity(),new ServiceSincronizacion(viewModelCliente,getActivity()).getClass());
                    getContext().startService(intent);
                }
            }
            showSaveResultOption(0,"","");
        } catch (ExecutionException e) {
            if (!ShareMethods.IsServiceRunning(getContext(), ServiceSincronizacion.class)){
                UtilShare.mActivity=getActivity();
                Intent intent = new Intent(getActivity(),new ServiceSincronizacion(viewModelCliente,getActivity()).getClass());
                getContext().startService(intent);
            }
        } catch (InterruptedException e) {
            if (!ShareMethods.IsServiceRunning(getContext(), ServiceSincronizacion.class)){
                UtilShare.mActivity=getActivity();
                Intent intent = new Intent(getActivity(),new ServiceSincronizacion(viewModelCliente,getActivity()).getClass());
                getContext().startService(intent);
            }
        }
    }
    public void onClickEtregar(){
        mbutton_entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rCredito.isChecked()==true){
                    if (tvTotalPago.getText().toString()==""){
                        ShowMessageResult("El Monto del Credito es mayor al Total de la Venta");
                        return;
                    }
                    if (ObtenerTotal()<Double.parseDouble(tvTotalPago.getText().toString())){
                        ShowMessageResult("El Monto del Credito es mayor al Total de la Venta");
                        return;
                    }
                }

                if (isOnline()){
                    VerificarOnlineEntregar();

                }else{
                    SaveOffLineEntregar();
                }

                    }
                });

    }
    public void OnClickObtenerFecha(){
    ObFecha.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            obtenerFecha();
        }
    });

}
    public void onClickVerCliente(){
        mbutton_viewcliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCliente!=null){

                    if (mCliente.getLatitud()==0 || mCliente.getLongitud()==0){
                        ShowMessageResult("El Cliente seleccionado no tiene registrado una ubicación");
                    }else{
                        UtilShare.cliente=mCliente;
                        MainActivity fca = ((MainActivity) getActivity());
                        fca.startActivity(new Intent(getActivity(), MapClientActivity.class));
                        fca.overridePendingTransition(R.transition.left_in, R.transition.left_out);
                    }
                }else{
                    ShowMessageResult("El Cliente no Existe en la zona del repartidor");
                }
            }
        });
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
    public void MostrarClientes(List<ClienteEntity> clientes) {

    }

    @Override
    public void MostrarProductos(List<ProductoEntity> productos) {
        if (productos.size()>0){
            lisProducto = new ArrayList<>();
            lisProducto.addAll(productos);
            aProducto.setThreshold(1);
            productoAdapter = new ProductAdapter(getActivity(), R.layout.row_item, GetActualProducts());
            aProducto.setAdapter(productoAdapter);
            aProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if ((ProductoEntity) adapterView.getItemAtPosition(i)!=null){
                        ProductoEntity item = (ProductoEntity) adapterView.getItemAtPosition(i);
                        int CantidadSeleccionada=CantidadDeProductosAgregados();
                        int MaximaCantidadProductos=DataPreferences.getPrefInt("CantidadProducto",context);

                        if(CantidadSeleccionada<MaximaCantidadProductos){
                            int stock= DataPreferences.getPrefInt("stock",context);
                            if (stock>0){
                                if (item.getStock()>0){
                                    DetalleEntity ItemDetalle=ObtenerDetalle(item);
                                    if (ItemDetalle==null){
                                        DetalleEntity detalle=new DetalleEntity();
                                        detalle.setObnumi(mPedido.getCodigogenerado());
                                        detalle.setObcprod(item.getNumi());
                                        detalle.setCadesc(item.getProducto());
                                        detalle.setObpcant(1.0);
                                        detalle.setObpbase(item.getPrecio());
                                        detalle.setObptot(item.getPrecio());
                                        detalle.setTotal(item.getPrecio());
                                        detalle.setStock(item.getStock());
                                        detalle.setEstado(false);
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
                                        try {

                                            DetalleEntity  detalle = ItemDetalle.clone();
                                            mDetalleItem.remove(ItemDetalle);
                                            detalle.setObpcant(1.0);
                                            mDetalleItem.add(detalle);
                                            if (detalle.getObupdate()==-1){
                                                detalle.setObupdate(2);
                                            }else{
                                                detalle.setObupdate(0);
                                            }

                                            //mDetalleAdapter.setFilter(mDetalleItem);
                                            Reconstruir();
                                            calcularTotal();
                                            aProducto .setText("");
                                            aProducto.clearFocus();
                                            mscroll.fullScroll(View.FOCUS_DOWN);
                                            productoAdapter.setLista(GetActualProducts());
                                            productoAdapter.notifyDataSetChanged();

                                        } catch (CloneNotSupportedException e) {

                                        }

                                    }
                                }else{
                                    hideKeyboard();

                                    aProducto.setText("");
                                    ShowMessageResult("No Existe Stock para seleccionar el producto");
                                }
                            }else{
                                DetalleEntity ItemDetalle=ObtenerDetalle(item);
                                if (ItemDetalle==null){
                                    DetalleEntity detalle=new DetalleEntity();
                                    detalle.setObnumi(mPedido.getCodigogenerado());
                                    detalle.setObcprod(item.getNumi());
                                    detalle.setCadesc(item.getProducto());
                                    detalle.setObpcant(1.0);
                                    detalle.setStock(item.getStock());
                                    detalle.setObpbase(item.getPrecio());
                                    detalle.setObptot(item.getPrecio());
                                    detalle.setTotal(item.getPrecio());
                                    detalle.setEstado(false);
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
                                    try {

                                        DetalleEntity  detalle = ItemDetalle.clone();
                                        mDetalleItem.remove(ItemDetalle);
                                        detalle.setObpcant(1.0);
                                        mDetalleItem.add(detalle);
                                        if (detalle.getObupdate()==-1){
                                            detalle.setObupdate(2);
                                        }else{
                                            detalle.setObupdate(0);
                                        }

                                        //mDetalleAdapter.setFilter(mDetalleItem);
                                        Reconstruir();
                                        calcularTotal();
                                        aProducto .setText("");
                                        aProducto.clearFocus();
                                        mscroll.fullScroll(View.FOCUS_DOWN);
                                        productoAdapter.setLista(GetActualProducts());
                                        productoAdapter.notifyDataSetChanged();

                                    } catch (CloneNotSupportedException e) {

                                    }

                                }
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
    private  void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
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
    public DetalleEntity ObtenerDetalle(ProductoEntity producto){

        for (int i = 0; i < mDetalleItem.size(); i++) {
            DetalleEntity detalle=mDetalleItem.get(i);
            if(detalle.getObcprod()==producto.getNumi()){
                return detalle;
            }
        }
        return null;
    }

    @Override
    public void setPresenter(CreatePedidoMvp.Presenter presenter) {
        mCreatePedidoPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    public void ModifyItem(int pos, String value, DetalleEntity item, TextView tvsubtotal, EditText eCantidad,LinearLayout fondo) {
        double cantidad=0.0;
        if (isDouble(value)){
            cantidad=Double.parseDouble(value);
        }

        int stock= DataPreferences.getPrefInt("stock",context);
        if (stock>0){
            int posicion =obtenerPosicionItem(item);
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
                    detalle.setTotal(cantidad*detalle.getObpbase());
                    double total=cantidad*mDetalleItem.get(posicion).getObpbase();
                    tvsubtotal.setText(""+ ShareMethods.redondearDecimales(total,2));
                    if (mDetalleItem.get(posicion).getObupdate()>=1){
                        detalle.setObupdate(2);
                    }
                    // tvsubtotal.setText(""+String.format("%.2f", (cantidad*mDetalleItem.get(posicion).getObpbase())));
                    calcularTotal();
                }else{
                    fondo.setBackgroundColor(getActivity().getResources().getColor(R.color.marfil));
                    DetalleEntity detalle= mDetalleItem.get(posicion);
                    detalle.setObpcant(cantidad);
                    detalle.setObptot(cantidad*detalle.getObpbase());
                    detalle.setTotal(cantidad*detalle.getObpbase());
                    double total=cantidad*mDetalleItem.get(posicion).getObpbase();
                    tvsubtotal.setText(""+ ShareMethods.redondearDecimales(total,2));
                    if (mDetalleItem.get(posicion).getObupdate()>=1){
                        detalle.setObupdate(2);
                    }
                    //tvsubtotal.setText(""+String.format("%.2f", (cantidad*mDetalleItem.get(posicion).getObpbase())));
                    calcularTotal();
                }

            }
        }else{
            int posicion =obtenerPosicionItem(item);
            if (posicion>=0){
                DetalleEntity detalle= mDetalleItem.get(posicion);
                detalle.setObpcant(cantidad);
                detalle.setObptot(cantidad*detalle.getObpbase());
                detalle.setTotal(cantidad*detalle.getObpbase());
                tvsubtotal.setText(""+String.format("%.2f", (cantidad*mDetalleItem.get(posicion).getObpbase())));
                calcularTotal();
                if (mDetalleItem.get(posicion).getObupdate()>=1){
                    detalle.setObupdate(2);
                }
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
        DetalleEntity detalle= mDetalleItem.get(posicion);
        if (posicion>=0 && precio>0){

            detalle.setObpbase(precio);
            detalle.setObptot(precio*detalle.getObpcant());
            tvsubtotal.setText(""+String.format("%.2f", (precio*mDetalleItem.get(posicion).getObpcant())));
            calcularTotal();

        }else{

            detalle.setObpbase(precio);
            detalle.setObptot(0);
            tvsubtotal.setText(""+String.format("%.2f", (precio*mDetalleItem.get(posicion).getObpcant())));
            calcularTotal();
        }
        if (mDetalleItem.get(posicion).getObupdate()>=1){
            detalle.setObupdate(2);
        }
    }


    @Override
    public void DeleteAndModifyDetailOrder(DetalleEntity item, int pos) {
        if (item !=null){
            int posicionn=obtenerPosicionItem(item);
           // mDetalleItem.remove(item);
            int estado=mDetalleItem.get(posicionn).getObupdate();
                    if (estado>=1){
                        mDetalleItem.get(posicionn).setObupdate(-1);
                    }else{

                           // mDetalleItem.remove(item);
                            mDetalleItem.get(posicionn).setObupdate(-2);


                    }
            // mDetalleAdapter.setFilter(mDetalleItem);
            productoAdapter.setLista(GetActualProducts());
            productoAdapter.notifyDataSetChanged();
            calcularTotal();

        }
    }

    @Override
    public void ShowMessageResult(String message) {

        if (alertDialog!=null){
            if (alertDialog.isShowing()){
                alertDialog.dismiss();
            }
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
    public void RetornarPrincipal(){
        MainActivity fca = ((MainActivity) getActivity());
        fca.removeAllFragments();

        Fragment frag = new ListPedidosFragment(1);
        //fca.switchFragment(frag,"LISTAR_PEDIDOS");
        fca.CambiarFragment(frag, Constantes.TAG_PEDIDOS);
    }
    @Override
    public void showSaveResultOption(int codigo, String id, String mensaje) {
        switch (codigo){
            case 0:
                dialogs= showCustomDialog("Pedido Actualizado Correctamente"
                        ,true);
                dialogs.setCancelable(false);
                dialogs.show();
                break;
        }
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
                        RetornarPrincipal();
                        UtilShare.clienteMapa =mCliente;
                        Fragment frag = new CreatePedidoFragment(1);
                        MainActivity fca = (MainActivity) getActivity();
                        fca.switchFragment(frag,"CREATE_PEDIDOS");
                    }
                }).build();

        alertDialog.show();
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
    @Override
    public void showDataDetail(List<DetalleEntity> listDetalle) {
        this.mDetalleItem=listDetalle;
        if (this.mDetalleItem.size()>0){
            mDetalleAdapter.setFilter(ObtenerProductosDisponibles());
            CargarDatos();
        }
    }
    public void CargarDatos(){

        tvObservacion.setText(mPedido.getOaobs());
        acliente.setText(mPedido.getCliente());
        etFecha.setText(ShareMethods.ObtenerFecha02(mPedido.getOafdoc()));

        name_total.setText(ShareMethods.ObtenerDecimalToString(mPedido.getTotal(),2));
        mTotal=mPedido.getTotal();
        double descuentoTotal=0.0;
        for (int i = 0; i < mDetalleItem.size(); i++) {
            descuentoTotal+=(mDetalleItem.get(i).getDescuento());
        }
        name_descuento.setText(""+ ShareMethods.redondearDecimales(descuentoTotal,2)+" Bs");
        double TotalGeneral=0.0;
        for (int i = 0; i < mDetalleItem.size(); i++) {
            TotalGeneral+=(mDetalleItem.get(i).getTotal());
        }
        name_totalDescuento.setText(""+ ShareMethods.redondearDecimales(TotalGeneral,2)+" Bs");

    }
    public void _prModificarNumi(String Numi){
        for (int i = 0; i < mDetalleItem.size(); i++) {
            mDetalleItem.get(i).setObnumi(Numi);
        }
    }
    public double _prObtenerTotal(){
        double suma=0;
        for (int i = 0; i < mDetalleItem.size(); i++) {
            if (mDetalleItem.get(i).getObupdate()>=0){
                suma=suma+( mDetalleItem.get(i).getObpcant()*mDetalleItem.get(i).getObpbase());
            }

        }
        return suma;
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

    public double ObtenerTotal(){
        double descuento=0;

        double total=0.0;
        for (int i = 0; i < mDetalleItem.size(); i++) {
            if (mDetalleItem.get(i).getObupdate()>=0){
                total+=mDetalleItem.get(i).getTotal();
            }

        }
        total-=descuento;
        if (total<0.0){
            total=0.0;
        }
        return total;
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
        name_totalDescuento.setText(""+ ShareMethods.redondearDecimales(TotalGeneral,2)+" Bs");

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

                        if (cantf>=descuento.getCantidad1()&& cantf<=descuento.getCantidad2()&& FechaActual>=descuento.getFechaInicio().getTime()
                                && FechaActual<=descuento.getFechaFin().getTime() ){
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
    public void Reconstruir(){
        mDetalleAdapter=null;
        mDetalleAdapter = new DetalleAdaptader(context, ObtenerProductosDisponibles(),this,getActivity());
        detalle_List.setAdapter(mDetalleAdapter);
    }
    public List<DetalleEntity> ObtenerProductosDisponibles(){
        List<DetalleEntity> list=new ArrayList<>();
        for (int i = 0; i < mDetalleItem.size(); i++) {
            if (mDetalleItem.get(i).getObupdate()>=0){
                try {
                    list.add(mDetalleItem.get(i).clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
    public boolean ExistsItem(ProductoEntity product){
        int i=0;
        while(i<mDetalleItem.size()){
            DetalleEntity item=mDetalleItem.get(i);
            if (item.getObcprod()==product.getNumi() && mDetalleItem.get(i).getObupdate()>=0){
                return true;
            }
            i++;
        }

        return false;
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
}
