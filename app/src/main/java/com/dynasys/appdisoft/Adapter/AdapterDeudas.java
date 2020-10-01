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
import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Pedidos.Presentacion.PedidosMvp;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marco on 2016-08-01.
 */
public class AdapterDeudas extends RecyclerView.Adapter<AdapterDeudas.PedidosViewHolder> {
    private List<DeudaEntity> listaPedidos;
    private List<ClienteEntity> listClientes;
    private Context context;
private DeudasMvp.View mview;
private Activity act;
    public AdapterDeudas(Context ctx, List<DeudaEntity> s, DeudasMvp.View view, List<ClienteEntity> listcliente, Activity act) {
        this.context = ctx;
        this.listaPedidos = s;
        this.mview=view;
        this.listClientes=listcliente;
        this.act=act;
    }
    public AdapterDeudas(Context ctx) {
        this.context = ctx;

    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }


    @Override
    public void onBindViewHolder(PedidosViewHolder clientesViewHolder, final int i) {
        clientesViewHolder.TvNombreCliente.setText((CharSequence) listaPedidos.get(i).getCliente());
        clientesViewHolder.TvDireccionCliente.setText((CharSequence) listaPedidos.get(i).getDireccion());
        clientesViewHolder.TvTotal.setText((CharSequence) ShareMethods.ObtenerDecimalToString( listaPedidos.get(i).getPendiente(),2)+" Bs");
        ColorGenerator generator = ColorGenerator.MATERIAL;
        clientesViewHolder.tvTelefono.setText((CharSequence) "Telf: "+listaPedidos.get(i).getTelefono());
        clientesViewHolder.CardViewPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mview.recyclerViewListClicked(v, listaPedidos.get(i));
            }
        });

        if (listaPedidos.get(i).getEstado()==1 ){
            clientesViewHolder.TvNombreCliente.setBackground(act.getResources().getDrawable(R.drawable.line_drawable));

        }
        if (listaPedidos.get(i).getEstado()!=1 ){
            clientesViewHolder.TvNombreCliente.setBackground(act.getResources().getDrawable(R.drawable.animation_bottoncancelrojo));
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
                inflate(R.layout.item_deuda, viewGroup, false);

        return new PedidosViewHolder(itemView);
    }



    public static class PedidosViewHolder extends RecyclerView.ViewHolder {


        protected TextView TvNombreCliente;
        protected TextView TvDireccionCliente;

        protected TextView TvTotal;
        protected TextView tvTelefono;
        protected CardView CardViewPedidos;
        public PedidosViewHolder(View v) {
            super(v);

            TvNombreCliente=(TextView) v.findViewById(R.id.view_deuda_cliente);
            TvDireccionCliente = (TextView) v.findViewById(R.id.view_deuda_direccion);
            TvTotal = (TextView) v.findViewById(R.id.view_deuda_total);
            tvTelefono=(TextView)v.findViewById(R.id.view_deuda_Telefono);
            CardViewPedidos=(CardView)v.findViewById(R.id.view_deuda_card);
        }
    }

    public void setFilter(List<DeudaEntity> ListaFiltrada){
        this.listaPedidos =new ArrayList<>();
        this.listaPedidos.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }
}
