package com.dynasys.appdisoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.dynasys.appdisoft.ListarDeudas.Pagos.PagosMvp;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Pedidos.Presentacion.PedidosMvp;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.pdfjet.Line;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marco on 2016-08-01.
 */
public class AdapterDetalleDeuda extends RecyclerView.Adapter<AdapterDetalleDeuda.PedidosViewHolder> {
    private List<DeudaEntity> listaDeudas;
    private List<ClienteEntity> listClientes;
    private Context context;
private PagosMvp.View mview;
private Activity activity;
    public AdapterDetalleDeuda(Context ctx, List<DeudaEntity> s, PagosMvp.View view, Activity act) {
        this.context = ctx;
        this.listaDeudas = s;
        this.mview=view;
        this.activity=act;

    }
    public AdapterDetalleDeuda(Context ctx) {
        this.context = ctx;

    }

    @Override
    public int getItemCount() {
        return listaDeudas.size();
    }


    @Override
    public void onBindViewHolder(final PedidosViewHolder clientesViewHolder, final int i) {
        clientesViewHolder.TvNroPedido.setText( ""+listaDeudas.get(i).getPedidoId());
        clientesViewHolder.tvFecha.setText(ShareMethods.ObtenerFecha02(listaDeudas.get(i).getFechaPedido()));
        clientesViewHolder.tvMonto.setText(ShareMethods.ObtenerDecimalToString(listaDeudas.get(i).getPendiente(),2));
        clientesViewHolder.txtFactura.setText("Factura = "+listaDeudas.get(i).getFactura());
        final DeudaEntity item=listaDeudas.get(i);
        if (listaDeudas.get(i).getTotalAPagar()>0){
            clientesViewHolder.chkDeuda.setChecked(true);
        }else{
            clientesViewHolder.chkDeuda.setChecked(false);
        }

        if (listaDeudas.get(i).getMora()>0){
            clientesViewHolder.txtMora.setText("Mora = "+listaDeudas.get(i).getMora()+" (Dias)");
        }else{
            clientesViewHolder.txtMora.setText("Mora = "+0+" (Dias)");
        }
        if(listaDeudas.get(i).isEstadoCredito()==false){
            clientesViewHolder.lnMora.setBackground(activity.getResources().getDrawable(R.drawable.animation_bottoncancelrojo ));

        }else{
            clientesViewHolder.lnMora.setBackground(activity.getResources().getDrawable(R.drawable.animation_riple_button));
        }
        clientesViewHolder.etMontoPagar.setText(ShareMethods.ObtenerDecimalToString(listaDeudas.get(i).getTotalAPagar(),2));
        clientesViewHolder.etMontoPagar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               int pos =ObtenerPosicionElemento(item);
                if (pos>=0){
                    mview.ModifyPago(item,clientesViewHolder.chkDeuda.isChecked(),pos,clientesViewHolder.etMontoPagar,clientesViewHolder.chkDeuda,s.toString());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                int i=0;
            }
        });
        clientesViewHolder.chkDeuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos =ObtenerPosicionElemento(item);
                mview.OnClickCheck(item,clientesViewHolder.chkDeuda.isChecked(),pos,clientesViewHolder.etMontoPagar);
            }
        });

    }

    public int ObtenerPosicionElemento( DeudaEntity item){
        for (int i = 0; i < listaDeudas.size(); i++) {
            if (item.getPedidoId()==listaDeudas.get(i).getPedidoId()){
                return i;
            }
        }
        return -1;
    }
public String ObtenerDireccionCliente(String numi){
    for (int i = 0; i < listClientes.size(); i++) {
        ClienteEntity cliente=listClientes.get(i);
        if (cliente.getCodigogenerado().trim().equals(numi.trim())){
            return cliente.getDireccion();
        }
    }
    return "S/D";
}
    public String ObtenerTelefonoCliente(String numi){
        for (int i = 0; i < listClientes.size(); i++) {
            ClienteEntity cliente=listClientes.get(i);
            if (cliente.getCodigogenerado().trim().equals(numi.trim())){
                return cliente.getTelefono();
            }
        }
        return "S/N";
    }
    @Override
    public PedidosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_detalle_deuda, viewGroup, false);

        return new PedidosViewHolder(itemView);
    }



    public static class PedidosViewHolder extends RecyclerView.ViewHolder {

        protected TextView TvNroPedido;
        protected TextView tvFecha;
        protected TextView tvMonto;
        protected CheckBox chkDeuda;
        protected EditText etMontoPagar;
        protected LinearLayout lnMora;
        protected TextView txtMora;
        protected  TextView txtFactura;
        public PedidosViewHolder(View v) {
            super(v);

            TvNroPedido = (TextView) v.findViewById(R.id.view_detalle_deuda_nropedido);
            tvFecha=(TextView) v.findViewById(R.id.view_detalle_deuda_fecha);
            tvMonto = (TextView) v.findViewById(R.id.view_detalle_deuda_monto);
            chkDeuda = (CheckBox) v.findViewById(R.id.deuda_detalle_chk);
            etMontoPagar = (EditText) v.findViewById(R.id.view_detalle_deuda_montopagar);
            lnMora=(LinearLayout) v.findViewById(R.id.banckgroundMora);
            txtMora=(TextView)v.findViewById(R.id.tvMora);
            txtFactura =(TextView)v.findViewById(R.id.tvFactura);
        }
    }

    public void setFilter(List<DeudaEntity> ListaFiltrada){
        this.listaDeudas =new ArrayList<>();
        this.listaDeudas.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }
}
