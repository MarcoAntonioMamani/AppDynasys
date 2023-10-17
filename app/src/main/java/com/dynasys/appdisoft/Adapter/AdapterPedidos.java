package com.dynasys.appdisoft.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Pedidos.Presentacion.PedidosMvp;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClienteEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marco on 2016-08-01.
 */
public class AdapterPedidos extends RecyclerView.Adapter<AdapterPedidos.PedidosViewHolder> {
    private List<PedidoEntity> listaPedidos;
    private List<ClienteEntity> listClientes;
    private Context context;
private PedidosMvp.View mview;
    public AdapterPedidos(Context ctx, List<PedidoEntity> s, PedidosMvp.View view,List<ClienteEntity> listcliente) {
        this.context = ctx;
        this.listaPedidos = s;
        this.mview=view;
        this.listClientes=listcliente;
    }
    public AdapterPedidos(Context ctx) {
        this.context = ctx;

    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }


    @Override
    public void onBindViewHolder(PedidosViewHolder clientesViewHolder, final int i) {
        clientesViewHolder.TvNroPedido.setText((CharSequence) ("Pedido # "+listaPedidos.get(i).getOanumi()));
        clientesViewHolder.TvNombreCliente.setText((CharSequence) listaPedidos.get(i).getCliente());
        clientesViewHolder.TvDireccionCliente.setText((CharSequence) ObtenerDireccionCliente(listaPedidos.get(i).getOaccli()));
        clientesViewHolder.TvFecha.setText((CharSequence) ShareMethods.ObtenerFecha(listaPedidos.get(i).getOafdoc())+" \n         "+listaPedidos.get(i).getOahora());
        clientesViewHolder.TvTotal.setText((CharSequence) ShareMethods.ObtenerDecimalToString( listaPedidos.get(i).getTotal(),2)+" Bs");
        ColorGenerator generator = ColorGenerator.MATERIAL;
        clientesViewHolder.tvTelefono.setText((CharSequence) "Telf: "+ObtenerTelefonoCliente(listaPedidos.get(i).getOaccli()));
        clientesViewHolder.CardViewPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mview.recyclerViewListClicked(v, listaPedidos.get(i));
            }
        });


        if (listaPedidos.get(i).getOaap()==2){
            clientesViewHolder.TvNroPedido.setText((CharSequence) ("Pedido # "+listaPedidos.get(i).getOanumi()+"  Anulado"));
            clientesViewHolder.TvNroPedido.setBackground(context.getResources().getDrawable(R.drawable.animation_bottoncancelrojo));
        }else{
            if (listaPedidos.get(i).getEstado()==0 && listaPedidos.get(i).getEstadoStock() !=2){  // EstadoStock= 2 Quiere Decir que no se sincronizo por stock
                clientesViewHolder.TvNroPedido.setBackground(context.getResources().getDrawable(R.drawable.animation_bottoncancelrojo));

            }
            if (listaPedidos.get(i).getEstado()==0 && listaPedidos.get(i).getEstadoStock() ==2){  // EstadoStock= 2 Quiere Decir que no se sincronizo por stock
                //Aqui Entra si el pedido es nuevo pero ya no existe el stock en server
                clientesViewHolder.TvNroPedido.setBackground(context.getResources().getDrawable(R.drawable.animation_riple_maps ));
            }
            if (listaPedidos.get(i).getEstado()==1 && listaPedidos.get(i).getOaap()==1){
                clientesViewHolder.TvNroPedido.setBackground(context.getResources().getDrawable(R.drawable.animation_riple_button));
            }


            if (listaPedidos.get(i).getEstado()==2 && listaPedidos.get(i).getEstadoStock() !=2){
                clientesViewHolder.TvNroPedido.setBackground(context.getResources().getDrawable(R.drawable.animation_bottoncancelrojo));
            }
            if (listaPedidos.get(i).getEstado()==2 && listaPedidos.get(i).getEstadoStock() ==2){
                //Aqui entra si ha sido modificado, pero ya no existe stock en el servidor
                clientesViewHolder.TvNroPedido.setBackground(context.getResources().getDrawable(R.drawable.animation_riple_maps));
            }
        }


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
                inflate(R.layout.item_order, viewGroup, false);

        return new PedidosViewHolder(itemView);
    }



    public static class PedidosViewHolder extends RecyclerView.ViewHolder {

        protected TextView TvNroPedido;
        protected TextView TvNombreCliente;
        protected TextView TvDireccionCliente;
        protected TextView TvFecha;
        protected TextView TvTotal;
        protected TextView tvTelefono;
        protected CardView CardViewPedidos;
        public PedidosViewHolder(View v) {
            super(v);

            TvNroPedido = (TextView) v.findViewById(R.id.view_pedido_nro);
            TvNombreCliente=(TextView) v.findViewById(R.id.view_pedido_nombre);
            TvDireccionCliente = (TextView) v.findViewById(R.id.view_pedido_direccion);
            TvFecha = (TextView) v.findViewById(R.id.view_pedido_fecha);
            TvTotal = (TextView) v.findViewById(R.id.view_pedido_total);
            tvTelefono=(TextView)v.findViewById(R.id.view_pedido_Telefono);
            CardViewPedidos=(CardView)v.findViewById(R.id.view_pedidos_card);
        }
    }

    public void setFilter(List<PedidoEntity> ListaFiltrada){
        this.listaPedidos =new ArrayList<>();
        this.listaPedidos.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }
}
