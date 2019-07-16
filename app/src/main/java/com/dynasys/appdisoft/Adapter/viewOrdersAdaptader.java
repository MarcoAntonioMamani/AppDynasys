package com.dynasys.appdisoft.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para comidas usadas en la sección "Categorías"
 */
public class viewOrdersAdaptader
        extends RecyclerView.Adapter<viewOrdersAdaptader.ViewHolder> {
    ViewGroup viewgroup;
    private List<DetalleEntity> items;
    Context context;

    public viewOrdersAdaptader(Context ctx) {
        this.context = ctx;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView nameProduct;
        public TextView cantidad;
        public TextView price;
        public TextView subTotal;
          public ViewHolder(View v) {
              super(v);

              nameProduct = (TextView) v.findViewById(R.id.order_datail_productname);
              cantidad = (TextView) v.findViewById(R.id.order_detail_product_count);
              price = (TextView) v.findViewById(R.id.order_detail_product_price);
              subTotal = (TextView) v.findViewById(R.id.order_detail_product_subtotal);
          }


    }


    public viewOrdersAdaptader(List<DetalleEntity> items, Context s, ViewGroup v) {
        this.items = items;
        this.context=s;
        this.viewgroup=v;
    }
    public viewOrdersAdaptader(Context ctx, List<DetalleEntity> s) {
        this.context = ctx;
        this.items = s;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_details_product, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final DetalleEntity item;
        item = items.get(i);
     viewHolder.nameProduct.setText(""+item.getCadesc().toString());
     viewHolder.cantidad.setText(ShareMethods.ObtenerDecimalToString(item.getObpcant(),2));
     viewHolder.price.setText(ShareMethods.ObtenerDecimalToString(item.getObpbase(),2));
     viewHolder.subTotal.setText(ShareMethods.ObtenerDecimalToString(item.getObptot(),2));
    }

public void setFilter(List<DetalleEntity> ListaFiltrada){
    this.items =new ArrayList<>();
    this.items.addAll(ListaFiltrada);
    notifyDataSetChanged();
}

}