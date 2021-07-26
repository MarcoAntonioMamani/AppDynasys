package com.dynasys.appdisoft.Adapter;

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
import com.dynasys.appdisoft.Login.DB.Entity.VisitaEntity;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.Visitas.VisitaMvp;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marco on 2016-08-01.
 */
public class AdapterVisitas extends RecyclerView.Adapter<AdapterVisitas.VisitasViewHolder> {
    private List<VisitaEntity> listaVisita;
    private Context context;
private VisitaMvp.View mview;
    public AdapterVisitas(Context ctx, List<VisitaEntity> s, VisitaMvp.View view) {
        this.context = ctx;
        this.listaVisita = s;
        this.mview=view;
    }
    public AdapterVisitas(Context ctx) {
        this.context = ctx;

    }

    @Override
    public int getItemCount() {
        return listaVisita.size();
    }


    @Override
    public void onBindViewHolder(VisitasViewHolder VisitasViewHolder, final int i) {

        if (listaVisita.get(i).getSincronizado()==1){
            VisitasViewHolder.TvAdapterNombre.setText("Visita # "+listaVisita.get(i).getIdSincronizacion()+"  "+listaVisita.get(i).getNombreCliente());
        }else{
            VisitasViewHolder.TvAdapterNombre.setText("Visita # "+listaVisita.get(i).getId()+"  "+listaVisita.get(i).getNombreCliente());

        }
      VisitasViewHolder.TvAdapterDireccion.setText((CharSequence) listaVisita.get(i).getDireccion());

        VisitasViewHolder.TvAdapterFecha.setText((CharSequence) ShareMethods.ObtenerFecha(listaVisita.get(i).getFecha())+"  "+listaVisita.get(i).getHora());


        VisitasViewHolder.TvAdapterNombre.setTag(listaVisita.get(i));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(listaVisita.get(i).getNombreCliente());

        VisitasViewHolder.ivAdapterImg.setImageDrawable(TextDrawable.builder().buildRound(listaVisita.get(i).getNombreCliente().substring(0, 1), color));
        VisitasViewHolder.cardVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mview.recyclerViewListClicked(v, listaVisita.get(i));
            }
        });
        if (listaVisita.get(i).getSincronizado()==1){
            VisitasViewHolder.TvAdapterNombre.setTextColor(Color.WHITE);
        }else{
            VisitasViewHolder.TvAdapterNombre.setTextColor(Color.RED);
        }

        VisitasViewHolder.btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mview.WhatsappClicked(view,listaVisita.get(i));
            }
        });

    }

    @Override
    public VisitasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_visita, viewGroup, false);

        return new VisitasViewHolder(itemView);
    }



    public static class VisitasViewHolder extends RecyclerView.ViewHolder {

        protected ImageView ivAdapterImg;
        protected TextView TvAdapterNombre;
        protected TextView TvAdapterDireccion;
        protected TextView TvAdapterFecha;
        protected CardView cardVisita;
        protected Button btnWhatsapp;
        public VisitasViewHolder(View v) {
            super(v);
            ivAdapterImg = (ImageView) v.findViewById(R.id.row_visita_img);
            TvAdapterNombre = (TextView) v.findViewById(R.id.row_visita_name);
            TvAdapterDireccion=(TextView) v.findViewById(R.id.row_visita_direccion);
            TvAdapterFecha = (TextView) v.findViewById(R.id.row_visita_telefono);
            cardVisita=(CardView)v.findViewById(R.id.id_cardview_visita);
            btnWhatsapp=(Button)v.findViewById(R.id.visita_btnwhatsapp);
        }
    }

    public void setFilter(List<VisitaEntity> ListaFiltrada){
        this.listaVisita =new ArrayList<>();
        this.listaVisita.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }
}
