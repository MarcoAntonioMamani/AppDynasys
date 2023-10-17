package com.dynasys.appdisoft.Clientes.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.dynasys.appdisoft.Clientes.ClienteMvp;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClienteEntity;


import java.util.ArrayList;
import java.util.List;



/**
 * Created by Marco on 2016-08-01.
 */
public class AdapterClientes extends RecyclerView.Adapter<AdapterClientes.ClientesViewHolder> {
    private List<ClienteEntity> listaCliente;
    private Context context;
private ClienteMvp.View mview;
    public AdapterClientes(Context ctx, List<ClienteEntity> s,ClienteMvp.View view) {
        this.context = ctx;
        this.listaCliente = s;
        this.mview=view;
    }
    public AdapterClientes(Context ctx) {
        this.context = ctx;

    }

    @Override
    public int getItemCount() {
        return listaCliente.size();
    }


    @Override
    public void onBindViewHolder(ClientesViewHolder clientesViewHolder, final int i) {
        clientesViewHolder.TvAdapterNombre.setText((CharSequence) listaCliente.get(i).getNamecliente());
        clientesViewHolder.TvAdapterDireccion.setText((CharSequence) listaCliente.get(i).getDireccion());
        clientesViewHolder.TvAdapterTelefono.setText((CharSequence)listaCliente.get(i).getTelefono());
        clientesViewHolder.TvAdapterNombre.setTag(listaCliente.get(i));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(listaCliente.get(i).getNamecliente());

        clientesViewHolder.ivAdapterImg.setImageDrawable(TextDrawable.builder().buildRound(listaCliente.get(i).getNamecliente().substring(0, 1), color));
        clientesViewHolder.cardCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mview.recyclerViewListClicked(v, listaCliente.get(i));
            }
        });
        if (listaCliente.get(i).getEstado()==1){
            clientesViewHolder.TvAdapterNombre.setTextColor(Color.BLACK);
        }else{
            clientesViewHolder.TvAdapterNombre.setTextColor(Color.RED);
        }
        clientesViewHolder.btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mview.WhatsappClicked(view,listaCliente.get(i));
            }
        });

    }

    @Override
    public ClientesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_cliente, viewGroup, false);

        return new ClientesViewHolder(itemView);
    }



    public static class ClientesViewHolder extends RecyclerView.ViewHolder {

        protected ImageView ivAdapterImg;
        protected TextView TvAdapterNombre;
        protected TextView TvAdapterDireccion;
        protected TextView TvAdapterTelefono;
        protected CardView cardCliente;
        protected Button btnWhatsapp;
        public ClientesViewHolder(View v) {
            super(v);
            ivAdapterImg = (ImageView) v.findViewById(R.id.row_cliente_img);
            TvAdapterNombre = (TextView) v.findViewById(R.id.row_cliente_name);
            TvAdapterDireccion=(TextView) v.findViewById(R.id.row_cliente_direccion);
            TvAdapterTelefono = (TextView) v.findViewById(R.id.row_cliente_telefono);
            cardCliente=(CardView)v.findViewById(R.id.id_cardview_cliente);
            btnWhatsapp=(Button) v.findViewById(R.id.cliente_btnwhatsapp);


        }
    }

    public void setFilter(List<ClienteEntity> ListaFiltrada){
        this.listaCliente =new ArrayList<>();
        this.listaCliente.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }
}
