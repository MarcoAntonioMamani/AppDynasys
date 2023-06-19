package com.dynasys.appdisoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dynasys.appdisoft.Login.DB.Entity.AlmacenEntity;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marco on 2016-08-01.
 */
public class AdapterReviewProducto extends RecyclerView.Adapter<AdapterReviewProducto.PedidosViewHolder> {
    private List<AlmacenEntity> listaPedidos;
    private Context context;

private Activity act;
    public AdapterReviewProducto(Context ctx, List<AlmacenEntity> s, Activity act) {
        this.context = ctx;
        this.listaPedidos = s;

        this.act=act;
    }
    public AdapterReviewProducto(Context ctx) {
        this.context = ctx;

    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }


    @Override
    public void onBindViewHolder(PedidosViewHolder clientesViewHolder, final int i) {
        clientesViewHolder.tvProducto.setText(""+listaPedidos.get(i).getProducto());
        clientesViewHolder.tvInicial.setText(ShareMethods.ObtenerDecimalToString(listaPedidos.get(i).getInicial(),2));
        clientesViewHolder.tvIngreso.setText(ShareMethods.ObtenerDecimalToString(listaPedidos.get(i).getIngreso(),2));

        clientesViewHolder.tvVenta.setText(ShareMethods.ObtenerDecimalToString(listaPedidos.get(i).getVenta(),2));

        clientesViewHolder.tvSaldo.setText(ShareMethods.ObtenerDecimalToString(listaPedidos.get(i).getSaldo(),2));


    }

    @Override
    public PedidosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_saldos_productos, viewGroup, false);

        return new PedidosViewHolder(itemView);
    }



    public static class PedidosViewHolder extends RecyclerView.ViewHolder {


        protected TextView tvProducto;
        protected TextView tvInicial;

        protected TextView tvIngreso;
        protected TextView tvVenta;
        protected TextView tvSaldo;
        public PedidosViewHolder(View v) {
            super(v);

            tvProducto=(TextView) v.findViewById(R.id.view_detalle_producto_name);
            tvInicial = (TextView) v.findViewById(R.id.view_detalle_producto_inicial);
            tvIngreso = (TextView) v.findViewById(R.id.view_detalle_producto_Ingreso);
            tvVenta=(TextView)v.findViewById(R.id.view_detalle_producto_venta);
            tvSaldo=(TextView)v.findViewById(R.id.view_detalle_producto_saldo);
        }
    }

    public void setFilter(List<AlmacenEntity> ListaFiltrada){
        this.listaPedidos =new ArrayList<>();
        this.listaPedidos.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }
}
