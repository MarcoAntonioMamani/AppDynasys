package com.dynasys.appdisoft.Pedidos.CreatePedidos;


import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dynasys.appdisoft.Adapter.ClientesAdapter;
import com.dynasys.appdisoft.Adapter.DetalleAdaptader;
import com.dynasys.appdisoft.Adapter.ProductAdapter;
import com.dynasys.appdisoft.Clientes.CreateCliente.CreateClienteFragment;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.PreciosListViewModel;
import com.dynasys.appdisoft.Login.ProductosListViewModel;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.Pedidos.Presentacion.PedidosMvp;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private List<DetalleEntity> mDetalleItem=new ArrayList<>();
    private CreatePedidoMvp.Presenter mCreatePedidoPresenter;
    private List<ClienteEntity> lisCliente;
    private List<ProductoEntity> lisProducto;
    private ClienteEntity mCliente=null;
    private RecyclerView detalle_List;
    ClientesAdapter clientAdapter;
    ProductAdapter productoAdapter;
DetalleAdaptader mDetalleAdapter;
TextView name_total,etFecha;
    private NestedScrollView mscroll;
    public CreatePedidoFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("CREAR PEDIDO");
        context=getContext();
    }
    public void iniciarRecyclerView(){
        mDetalleAdapter = new DetalleAdaptader(context, mDetalleItem,this);
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
        etFecha=view.findViewById(R.id.et_mostrar_fecha_picker);
        ObFecha=(ImageButton)view.findViewById(R.id.ib_obtener_fecha);
        mbutton_guardar = (Button)view.findViewById(R.id.id_btn_guardarPedido);
        mbutton_cancelar=(Button)view.findViewById(R.id.id_btn_cancelarPedido);
        mscroll=view.findViewById(R.id.id_order_scroll);
        viewModelCliente = ViewModelProviders.of(getActivity()).get(ClientesListViewModel.class);
        viewModelProducto = ViewModelProviders.of(getActivity()).get(ProductosListViewModel.class);
        new CreatePedidoPresenter(this,context,viewModelCliente,viewModelProducto,getActivity());
        mCreatePedidoPresenter.CargarClientes();
        iniciarRecyclerView();
        onclickObtenerFecha();
        onclickGuardar();
        onclickCancelar();
        etFecha.setText(FormatearFecha(dia,mes+1,anio));
        return view;
    }
    public void onclickGuardar(){
        mbutton_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GuardarMovimiento();
            }
        });
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
        fca.returnToMain();
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

                            DetalleEntity detalle=new DetalleEntity();
                            detalle.setEstado(false);
                            detalle.setObpcant(1.0);
                            detalle.setCadesc(item.getProducto());
                            detalle.setObcprod(item.getNumi());
                            detalle.setObpbase(item.getPrecio());
                            detalle.setObptot(item.getPrecio());
                            detalle.setObnumi("-1");

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


                }
            });
        }
    }
    public void Reconstruir(){
        mDetalleAdapter=null;
        mDetalleAdapter = new DetalleAdaptader(context, mDetalleItem,this);
        detalle_List.setAdapter(mDetalleAdapter);
    }
    @Override
    public void setPresenter(CreatePedidoMvp.Presenter presenter) {
        mCreatePedidoPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    public void ModifyItem(int pos, String value, DetalleEntity item, TextView tvsubtotal) {
        double cantidad=0.0;
        if (isDouble(value)){
            cantidad=Double.parseDouble(value);
        }
        int posicion =obtenerPosicionItem(item);
        if (posicion>=0){
            DetalleEntity detalle= mDetalleItem.get(posicion);
            detalle.setObpcant(cantidad);
            tvsubtotal.setText(""+String.format("%.2f", (cantidad*mDetalleItem.get(posicion).getObpbase())));
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
}
