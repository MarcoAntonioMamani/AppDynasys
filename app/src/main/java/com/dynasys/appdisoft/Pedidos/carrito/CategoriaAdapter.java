package com.dynasys.appdisoft.Pedidos.carrito;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.dynasys.appdisoft.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.EmpresasViewHolder> {
private List<Categorias> listaEmpresas;
private Context context;
private ProductorMvp.View mview;
private Activity activity;
public CategoriaAdapter(Context ctx, List<Categorias> s, ProductorMvp.View view, Activity act) {
        this.context = ctx;
        this.listaEmpresas = s;
        this.mview = view;
        this.activity=act;
        }

public CategoriaAdapter(Context ctx) {
        this.context = ctx;

        }

@Override
public int getItemCount() {
        return listaEmpresas.size();
        }


@Override
public void onBindViewHolder(final EmpresasViewHolder clientesViewHolder, @SuppressLint("RecyclerView") final int i) {
        clientesViewHolder.tvCategoria.setText(" "+(CharSequence) listaEmpresas.get(i).getNombreCategoria()+" ");

if (listaEmpresas.get(i).getEstado()==0){
    clientesViewHolder.tvCategoria.setBackground(activity.getResources().getDrawable(R.drawable.animation_bottoncancel));
    clientesViewHolder.tvCategoria.setTextColor(activity.getResources().getColor(R.color.black));
}else{
    clientesViewHolder.tvCategoria.setBackground(activity.getResources().getDrawable(R.drawable.animation_riple_maps));
    clientesViewHolder.tvCategoria.setTextColor(activity.getResources().getColor(R.color.white));
}

        clientesViewHolder.cardCategoria.setOnClickListener(new View.OnClickListener() {
@Override
    public void onClick(View v) {
    try {
        mview.recyclerViewListClickedCategoria(v, listaEmpresas.get(i),clientesViewHolder.tvCategoria);
    } catch (CloneNotSupportedException e) {

    }
}
        });


        }

@Override
public EmpresasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.card_categoria, viewGroup, false);

        return new EmpresasViewHolder(itemView);
        }


public static class EmpresasViewHolder extends RecyclerView.ViewHolder {

    protected TextView tvCategoria;

    protected CardView cardCategoria;

    public EmpresasViewHolder(View v) {
        super(v);

        tvCategoria = (TextView) v.findViewById(R.id.name_categoria);
       cardCategoria = (CardView) v.findViewById(R.id.card_Categorias);
    }
}

    public void setFilter(List<Categorias> ListaFiltrada) {
        this.listaEmpresas = new ArrayList<>();
        this.listaEmpresas.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }

}

