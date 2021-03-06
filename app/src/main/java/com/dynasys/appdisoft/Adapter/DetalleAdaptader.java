package com.dynasys.appdisoft.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Pedidos.CreatePedidos.CreatePedidoMvp;
import com.dynasys.appdisoft.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para comidas usadas en la sección "Categorías"
 */
public class DetalleAdaptader
        extends RecyclerView.Adapter<DetalleAdaptader.ViewHolder> {
ViewGroup viewgroup;
    private CreatePedidoMvp.View mView;
    private List<DetalleEntity> items;
    Context context;
    public DetalleAdaptader(Context ctx) {
        this.context = ctx;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombre;
        public EditText price;
        public TextView subtotal;
        public EditText cantidad;
        public ImageView img_delete;
          public ViewHolder(View v) {
            super(v);

            nombre = (TextView) v.findViewById(R.id.id_detalle_name);
              price = (EditText) v.findViewById(R.id.id_detalle_price);
              subtotal=(TextView)v.findViewById(R.id.id_detalle_subtotal);
              cantidad=(EditText)v.findViewById(R.id.id_detalle_cantidad);
              img_delete=(ImageView)v.findViewById(R.id.id_detalle_remove);
                     }
    }


    public DetalleAdaptader(List<DetalleEntity> items, Context s, ViewGroup v) {
        this.items = items;
        this.context=s;
        this.viewgroup=v;
    }
    public DetalleAdaptader(Context ctx, List<DetalleEntity> s, CreatePedidoMvp.View mView) {
        this.context = ctx;
        this.items = s;
        this.mView=mView;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_product, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final DetalleEntity item;
        final TextView tvsubtotal;
        final EditText tvCantidad;
        final EditText tvPrecio;
            item = items.get(i);
            viewHolder.nombre.setText(item.getCadesc());
            viewHolder.img_delete.setTag(item);
            viewHolder.price.setText(""+item.getObpbase());
            viewHolder.subtotal.setText(""+(item.getObpcant()*item.getObpbase()));
            viewHolder.cantidad.setText(""+item.getObpcant());
            viewHolder.cantidad.setTag(item);
             tvsubtotal=viewHolder.subtotal;
        tvCantidad=viewHolder.cantidad;
        tvPrecio=viewHolder.price;
            if (i== items.size()-1){
                viewHolder.cantidad.requestFocus();
            }
            viewHolder.cantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int pos =ObtenerPosicionElemento(item);
                    if (pos>=0){
                        mView.ModifyItem(pos,s.toString(),item,tvsubtotal,tvCantidad);
                    }


                }

                @Override
                public void afterTextChanged(Editable s) {
                 int i=0;
                }
            });

            viewHolder.price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int pos =ObtenerPosicionElemento(item);
                    if (pos>=0){
                        mView.ModifyItemPrecio(pos,charSequence.toString(),item,tvsubtotal,tvPrecio);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView img=(ImageView)view;
                    DetalleEntity obj =(DetalleEntity) img.getTag();
                    int pos=ObtenerPosicionElemento(obj);
                    if (pos>=0){
                        items.remove(pos);
                        mView.DeleteAndModifyDetailOrder(obj,pos);
                        notifyItemRemoved(pos);

                    }



                }
            });



    }
    public int ObtenerPosicionElemento( DetalleEntity item){
        for (int i = 0; i < items.size(); i++) {
            if (item.getObcprod()==items.get(i).getObcprod()){
                return i;
            }
        }
        return -1;
    }
public void setFilter(List<DetalleEntity> ListaFiltrada){
    this.items =new ArrayList<>();
    this.items.addAll(ListaFiltrada);
    notifyDataSetChanged();
}
}