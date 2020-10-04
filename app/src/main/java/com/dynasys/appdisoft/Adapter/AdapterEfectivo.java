package com.dynasys.appdisoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.dynasys.appdisoft.ListarDeudas.DeudasMvp;

import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.RevisarEfectivo.Efectivo;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marco on 2016-08-01.
 */
public class AdapterEfectivo extends RecyclerView.Adapter<AdapterEfectivo.PedidosViewHolder> {
    private List<Efectivo> listaPedidos;
    private Context context;

private Activity act;
    public AdapterEfectivo(Context ctx, List<Efectivo> s, Activity act) {
        this.context = ctx;
        this.listaPedidos = s;

        this.act=act;
    }
    public AdapterEfectivo(Context ctx) {
        this.context = ctx;

    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }


    @Override
    public void onBindViewHolder(PedidosViewHolder clientesViewHolder, final int i) {
        clientesViewHolder.tvPedido.setText(""+listaPedidos.get(i).getIdPedido());
        clientesViewHolder.tvFecha.setText(ShareMethods.ObtenerFecha02(listaPedidos.get(i).getFecha()));
        clientesViewHolder.tvCliente.setText(listaPedidos.get(i).getCliente());

        clientesViewHolder.tvMonto.setText(ShareMethods.ObtenerDecimalToString(listaPedidos.get(i).getMonto(),2));

        if (listaPedidos.get(i).getTipo()==1){
            clientesViewHolder.tvTipo.setText("V");

            clientesViewHolder.tvTipo.setBackground(act.getResources().getDrawable(R.drawable.animation_riple_maps));
        }else{
            clientesViewHolder.tvTipo.setText("P");
            clientesViewHolder.tvTipo.setBackground(act.getResources().getDrawable(R.drawable.animation_riple_edit));
        }


    }

    @Override
    public PedidosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_efectivo, viewGroup, false);

        return new PedidosViewHolder(itemView);
    }



    public static class PedidosViewHolder extends RecyclerView.ViewHolder {


        protected TextView tvPedido;
        protected TextView tvFecha;

        protected TextView tvCliente;
        protected TextView tvMonto;
        protected TextView tvTipo;
        public PedidosViewHolder(View v) {
            super(v);

            tvPedido=(TextView) v.findViewById(R.id.view_detalle_efectivo_nropedido);
            tvFecha = (TextView) v.findViewById(R.id.view_detalle_efectivo_fecha);
            tvCliente = (TextView) v.findViewById(R.id.view_detalle_efectivo_cliente);
            tvMonto=(TextView)v.findViewById(R.id.view_detalle_efectivo_monto);
            tvTipo=(TextView)v.findViewById(R.id.view_detalle_efectivo_tipo);
        }
    }

    public void setFilter(List<Efectivo> ListaFiltrada){
        this.listaPedidos =new ArrayList<>();
        this.listaPedidos.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }
}
