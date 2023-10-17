package com.dynasys.appdisoft.Pedidos.carrito;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.R;

import java.util.ArrayList;
import java.util.List;

public class ProductoCarritoAdapter extends RecyclerView.Adapter<ProductoCarritoAdapter.EmpresasViewHolder> {
private List<ProductoEntity> listaEmpresas;
private Context context;
private ProductorMvp.View mview;
private Activity activity;
public ProductoCarritoAdapter(Context ctx, List<ProductoEntity> s, ProductorMvp.View view, Activity act) {
        this.context = ctx;
        this.listaEmpresas = s;
        this.mview = view;
        this.activity=act;
        }

public ProductoCarritoAdapter(Context ctx) {
        this.context = ctx;

        }

@Override
public int getItemCount() {
        return listaEmpresas.size();
        }


@Override
public void onBindViewHolder(final EmpresasViewHolder clientesViewHolder, @SuppressLint("RecyclerView") final int i) {
        clientesViewHolder.tvNombre.setText(" "+(CharSequence) listaEmpresas.get(i).getProducto()+" ");
    clientesViewHolder.tvPrecio.setText("BS "+ String.format("%.2f",listaEmpresas.get(i).getPrecio())+" ");
    clientesViewHolder.tvInventario.setText("StockUni="+String.format("%.2f",listaEmpresas.get(i).getStock())+" \nStockCaja="+String.format("%.2f",listaEmpresas.get(i).getStock()/listaEmpresas.get(i).getConversion())+"\nConversion="+listaEmpresas.get(i).getConversion());

/*
if (listaEmpresas.get(i).getEstado()==0){
    clientesViewHolder.tvCategoria.setBackground(activity.getResources().getDrawable(R.drawable.animation_bottoncancel));
    clientesViewHolder.tvCategoria.setTextColor(activity.getResources().getColor(R.color.black));
}else{
    clientesViewHolder.tvCategoria.setBackground(activity.getResources().getDrawable(R.drawable.animation_riple_maps));
    clientesViewHolder.tvCategoria.setTextColor(activity.getResources().getColor(R.color.white));
}*/




        }

@Override
public EmpresasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.item_producto_cart, viewGroup, false);

        return new EmpresasViewHolder(itemView);
        }


public static class EmpresasViewHolder extends RecyclerView.ViewHolder {

    protected TextView tvNombre;
    protected TextView tvPrecio;
    protected CardView cardProducto;
    protected TextView tvInventario;
    public EditText cantidadUnitaria;
    public EditText cajas;
    public EmpresasViewHolder(View v) {
        super(v);
        tvNombre = (TextView) v.findViewById(R.id.id_nameProducto);
        tvPrecio = (TextView) v.findViewById(R.id.id_precioProducto);
        cantidadUnitaria = (EditText) v.findViewById(R.id.id_Cantidad_Carrito);
        cajas = (EditText) v.findViewById(R.id.id_Cantidad_Cajas);
       cardProducto = (CardView) v.findViewById(R.id.card_viewCarrito);
       tvInventario=(TextView) v.findViewById(R.id.id_InventarioProducto);
    }
}

    public void setFilter(List<ProductoEntity> ListaFiltrada) {
        this.listaEmpresas = new ArrayList<>();
        this.listaEmpresas.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }

}

