package com.dynasys.appdisoft.RevisarEfectivo;

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
import android.widget.SearchView;
import android.widget.TextView;

import com.dynasys.appdisoft.Adapter.AdapterEfectivo;
import com.dynasys.appdisoft.Login.DB.ListViewmodel.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
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


public class EfectivoFragment extends Fragment implements SearchView.OnQueryTextListener {



    private List<Efectivo> lisEfectivo=new ArrayList<>();
    View view;
    Context context;
    RecyclerView recList;
    public AdapterEfectivo adapterPerfil;
    TextView tvTotalContado,tvTotalCredito;
    SearchView simpleSearchView;
    TextView tvDesde,tvHasta;

    Button btnCargar;
    public final Calendar c = Calendar.getInstance();
    private PedidoListViewModel viewModelPedido;
    private DetalleListViewModel viewModelPedidoDetalle;


    private List<PedidoEntity> listPedidos;
    List<DetalleEntity> listDetalle;


    private static final String CERO = "0";
    private static final String BARRA = "/";

    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    Button btnSalir;
    public EfectivoFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Revisar Efectivo");
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
        view= inflater.inflate(R.layout.fragment_efectivo, container, false);
        recList = (RecyclerView) view.findViewById(R.id.id_detalle_listefectivo);
        recList.setHasFixedSize(true);
        simpleSearchView = (SearchView) view.findViewById (R.id.simpleSearchViewEfectivo);
        simpleSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        simpleSearchView.setIconifiedByDefault(false);
        btnSalir=view.findViewById(R.id.id_btn_efectivo_cancelar);
        tvTotalContado=view.findViewById(R.id.view_info_efectivo_contado);
        tvTotalCredito =view.findViewById(R.id.view_info_efectivo_credito );

        tvTotalContado.setText(ShareMethods.ObtenerDecimalToString(0,2));
        tvTotalCredito.setText(ShareMethods.ObtenerDecimalToString(0,2));

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
     /*   btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lisEfectivo=Filtrar();
                CargarRecycler(lisEfectivo);
            }
        });*/
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

        listPedidos=viewModelPedido.getMAllPedido(1);
        listDetalle=viewModelPedidoDetalle.getMAllDetalle(1);

        lisEfectivo=Filtrar();
        CargarRecycler(lisEfectivo);
    }

    public List<Efectivo> Filtrar(){
        List<Efectivo> listNewEfectivo=new ArrayList<>();


       /* Long fechaDesde = getFechaFromStringtoLong(tvDesde.getText().toString(), "dd/MM/yyyy");
        Long fechaHasta = getFechaHastaFromStringtoLong(tvHasta.getText().toString(), "dd/MM/yyyy");
*/
        double Contado=0;
        double Credito=0;


        for (int i = 0; i < listPedidos.size(); i++) {
            PedidoEntity ped=listPedidos.get(i);
            //  if (ped.getOafdoc().getTime()>=fechaDesde && ped.getOafdoc().getTime()<=fechaHasta){
            Efectivo ef=new Efectivo();
            ef.setCliente(ped.getCliente());
            ef.setFecha(ped.getOafdoc());
            ef.setIdCliente(ped.getOaccli());
            ef.setIdPedido(ped.getOanumi());

            ef.setObservacion(ped.getOaobs());
            ef.setTipo(1);


            if (ped.getTipocobro() !=2 && ped.getOaest()==3){
                Contado+=ped.getTotal();
                ef.setMontoContado(ped.getTotal());
                listNewEfectivo.add(ef);
            }else{
                if (ped.getTipocobro() ==2 && ped.getOaest()==3){
                    Credito+=ped.getTotal();
                    ef.setMontoCredito(ped.getTotal());
                    listNewEfectivo.add(ef);
                }

            }


            //   }
        }


        tvTotalContado .setText(ShareMethods.ObtenerDecimalToString(Contado,2));
        tvTotalCredito.setText(ShareMethods.ObtenerDecimalToString(Credito,2));





        return listNewEfectivo;
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
    public void CargarRecycler(List<Efectivo> listEfectivo){
        if (listEfectivo!=null){
            adapterPerfil = new AdapterEfectivo(context,listEfectivo,getActivity());
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
            List<Efectivo> ListaFiltrada=filter(lisEfectivo,newText);
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

    public List<Efectivo> filter (List<Efectivo> bares ,String texto){
        List<Efectivo>ListaFiltrada=new ArrayList<>();
        try{
            texto=texto.toLowerCase();
            for (Efectivo b:bares){
                String name=b.getCliente().toLowerCase();
                if(name.contains(texto)){
                    ListaFiltrada.add(b);
                }
            }
        }catch (Exception e){

        }
        return ListaFiltrada;
    }

}