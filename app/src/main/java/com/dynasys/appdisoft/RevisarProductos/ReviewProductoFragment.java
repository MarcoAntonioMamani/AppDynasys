package com.dynasys.appdisoft.RevisarProductos;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.dynasys.appdisoft.Adapter.AdapterReviewProducto;
import com.dynasys.appdisoft.Login.DB.Entity.AlmacenEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.AlmacenListaViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.PedidoListViewModel;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.dynasys.appdisoft.Pedidos.ListPedidosFragment.parseDate;

public class ReviewProductoFragment extends Fragment   implements SearchView.OnQueryTextListener{
    private List<AlmacenEntity> lisAlmacen=new ArrayList<>();
    View view;
    Context context;
    RecyclerView recList;
    public AdapterReviewProducto adapterPerfil;
    TextView tvTotalIngreso,tvTotalVenta,tvTotalSaldo;
    SearchView simpleSearchView;
    TextView tvDesde,tvHasta;
    ImageButton btnDesde,btnHasta;
    Button btnCargar;
    public final Calendar c = Calendar.getInstance();
    private PedidoListViewModel viewModelPedido;
    private DetalleListViewModel viewModelPedidoDetalle;
    private AlmacenListaViewModel viewModelAlmacen;

    private List<PedidoEntity> listPedidos;
    List<DetalleEntity> listDetalle;

    private static final String CERO = "0";
    private static final String BARRA = "/";

    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    Button btnSalir;
    public ReviewProductoFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Revisar Producto");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_review_producto, container, false);

        recList = (RecyclerView) view.findViewById(R.id.id_detalle_listreview_producto);
        recList.setHasFixedSize(true);
        simpleSearchView = (SearchView) view.findViewById (R.id.simpleSearchViewReviewProduct);
        simpleSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        simpleSearchView.setIconifiedByDefault(false);
        btnSalir=view.findViewById(R.id.id_btn_producto_cancelar);
        tvTotalIngreso=view.findViewById(R.id.view_info_producto_ingreso);
        tvTotalVenta=view.findViewById(R.id.view_info_productos_ventas);
        tvTotalSaldo=view.findViewById(R.id.view_info_producto_saldo);
        tvTotalIngreso.setText(ShareMethods.ObtenerDecimalToString(0,2));
        tvTotalVenta.setText(ShareMethods.ObtenerDecimalToString(0,2));
        tvTotalSaldo.setText(ShareMethods.ObtenerDecimalToString(0,2));
        iniciarParametroFecha();
        onclickObtenerFechaDesde();
        onclickObtenerFechaHasta();
        OnClickCargar();


        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetornarPrincipal();
            }
        });
        try {
            cargarDatos();
        } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }

        return view;
    }

    public void RetornarPrincipal(){

        MainActivity fca = ((MainActivity) getActivity());
        fca.returnToMain();





    }
    public void OnClickCargar(){
        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Filtrar();
                    CargarRecycler(lisAlmacen);
                } catch (ExecutionException e) {

                } catch (InterruptedException e) {

                }

            }
        });
    }
    public void onclickObtenerFechaDesde(){
        btnDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtenerFecha(1);

            }
        });
    }
    public void onclickObtenerFechaHasta(){
        btnHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtenerFecha(2);

            }
        });
    }

    private void obtenerFecha(final int tipo){  ///1 = desde    2=hasta
        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                if (tipo==1){
                    tvDesde.setText(FormatearFecha(dayOfMonth ,mesActual ,year));
                }else{
                    tvHasta.setText(FormatearFecha(dayOfMonth ,mesActual ,year));
                }

            }

        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }
    public void iniciarParametroFecha(){
        tvDesde=(TextView)view.findViewById(R.id.id_tvfecha_producto_desde);
        tvHasta=(TextView)view.findViewById(R.id.id_tvfecha_producto_hasta);
        tvDesde.setEnabled(false);
        tvHasta.setEnabled(false);
        btnDesde=(ImageButton) view.findViewById(R.id.ib_btn_producto_desde);
        btnHasta=(ImageButton)view.findViewById(R.id.id_btn_producto_hasta);
        btnCargar=(Button)view.findViewById(R.id.id_btn_producto_buscar) ;
        tvDesde.setText(FormatearFecha(dia,mes+1,anio));
        tvHasta.setText(FormatearFecha(dia,mes+1,anio));


    }
    public String FormatearFecha (int dayOfMonth,int mesActual,int year){
        //Formateo el día obtenido: antepone el 0 si son menores de 10
        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
        //Formateo el mes obtenido: antepone el 0 si son menores de 10
        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
        return diaFormateado + BARRA + mesFormateado + BARRA + year;
    }
    public void cargarDatos() throws ExecutionException, InterruptedException {
        viewModelPedido = ViewModelProviders.of(getActivity()).get(PedidoListViewModel.class);
        viewModelPedidoDetalle= ViewModelProviders.of(getActivity()).get(DetalleListViewModel.class);
        viewModelAlmacen=ViewModelProviders.of(getActivity()).get(AlmacenListaViewModel.class);
        lisAlmacen=viewModelAlmacen.getMAlmacenAllAsync();
        listPedidos=viewModelPedido.getMAllPedido(1);
        listDetalle=viewModelPedidoDetalle.getMAllDetalle(1);

        Filtrar();
        CargarRecycler(lisAlmacen);
    }

    public void Filtrar() throws ExecutionException, InterruptedException {



        /*Long fechaDesde = getFechaFromStringtoLong(tvDesde.getText().toString(), "dd/MM/yyyy");
        Long fechaHasta = getFechaHastaFromStringtoLong(tvHasta.getText().toString(), "dd/MM/yyyy");
*/
        double Ventas=0;
        double Ingreso=0;
        double Saldo=0;

        for (int i = 0; i < lisAlmacen.size(); i++) {
            AlmacenEntity prod=lisAlmacen.get(i);

            double cantidad=0.0;
            for (int j = 0; j < listPedidos.size(); j++) {
                PedidoEntity ped=listPedidos.get(j);
                if (ped.getOaest()==3){
                    // if (ped.getOafdoc().getTime()>=fechaDesde && ped.getOafdoc().getTime()<=fechaHasta){
                    List<DetalleEntity> listDet=viewModelPedidoDetalle.getDetalle(ped.getOanumi());
                    for (int k = 0; k < listDet.size(); k++) {
                        DetalleEntity detalle=listDet.get(k);
                        if (detalle.getObcprod()==prod.getProductoId() && ped.getEstado()>=0){
                            cantidad+=detalle.getObpcant();
                        }
                    }

                    //}
                }


            }
            lisAlmacen.get(i).setVenta(cantidad);
            lisAlmacen.get(i).setSaldo((lisAlmacen.get(i).getIngreso()+lisAlmacen.get(i).getInicial())-cantidad);
        }


        for (int i = 0; i < lisAlmacen.size(); i++) {
            Ingreso+=lisAlmacen.get(i).getIngreso();
            Ventas+=lisAlmacen.get(i).getVenta();
            Saldo+=lisAlmacen.get(i).getSaldo();
        }

        tvTotalIngreso.setText(ShareMethods.ObtenerDecimalToString(Ingreso,2));
        tvTotalVenta.setText(ShareMethods.ObtenerDecimalToString(Ventas,2));
        tvTotalSaldo.setText(ShareMethods.ObtenerDecimalToString(Saldo,2));



    }
    public static Long getFechaHastaFromStringtoLong(String date, String pattern) {
        Long lngFecha =0L;
        Date df =parseDate(date,pattern);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 59);
        cal.set(Calendar.YEAR, df.getYear()+1900);
        cal.set(Calendar.MONTH, df.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, df.getDate());
        lngFecha = cal.getTimeInMillis();
        return lngFecha;
    }
    public static Long getFechaFromStringtoLong(String date, String pattern) {
        Long lngFecha =0L;
        Date df =parseDate(date,pattern);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.YEAR, df.getYear()+1900);
        cal.set(Calendar.MONTH, df.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, df.getDate());
        lngFecha = cal.getTimeInMillis();
        return lngFecha;
    }
    public void CargarRecycler(List<AlmacenEntity> listEfectivo){
        if (listEfectivo!=null){
            adapterPerfil = new AdapterReviewProducto(context,lisAlmacen,getActivity());
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
            List<AlmacenEntity> ListaFiltrada=filter(lisAlmacen,newText);
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

    public List<AlmacenEntity> filter (List<AlmacenEntity> bares , String texto){
        List<AlmacenEntity>ListaFiltrada=new ArrayList<>();
        try{
            texto=texto.toLowerCase();
            for (AlmacenEntity b:bares){
                String name=b.getProducto().toLowerCase();
                if(name.contains(texto)){
                    ListaFiltrada.add(b);
                }
            }
        }catch (Exception e){

        }
        return ListaFiltrada;
    }
}