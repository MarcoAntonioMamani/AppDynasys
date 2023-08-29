package com.dynasys.appdisoft.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Pedidos.CreatePedidos.CreatePedidoMvp;
import com.dynasys.appdisoft.R;

import org.w3c.dom.Text;

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

    int HabilitadoPrecio;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombre;
        public EditText price;
        public TextView subtotal;
        public EditText cantidad;
        public EditText descuento;
        public EditText caja;
        public TextView img_delete;
        public ViewHolder(View v) {
            super(v);

            nombre = (TextView) v.findViewById(R.id.id_detalle_name);
            price = (EditText) v.findViewById(R.id.id_detalle_price);
            subtotal=(TextView)v.findViewById(R.id.id_detalle_subtotal);
            cantidad=(EditText)v.findViewById(R.id.id_detalle_cantidad);
            caja=(EditText)v.findViewById(R.id.id_detalle_Caja);
            img_delete=(TextView)v.findViewById(R.id.id_detalle_remove);
            descuento =(EditText)v.findViewById(R.id.id_detalle_Descuento);

        }
    }


    public DetalleAdaptader(List<DetalleEntity> items, Context s, ViewGroup v) {
        this.items = items;
        this.context=s;
        this.viewgroup=v;

    }
    public DetalleAdaptader(Context ctx, List<DetalleEntity> s, CreatePedidoMvp.View mView,int HabilitadoPrecio ) {
        this.context = ctx;
        this.items = s;
        this.mView=mView;
        this.HabilitadoPrecio=HabilitadoPrecio;

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
        final EditText tvCantidadCajas;
        final EditText tvPrecio;
        final EditText tvDescuento;
        item = items.get(i);
        viewHolder.nombre.setText(item.getCadesc());
        viewHolder.img_delete.setTag(item);

        viewHolder.img_delete.setText("Conv. = "+item.getConversion()+"  Eliminar"+" stock="+item.getStock());
        viewHolder.price.setText(""+(String.format("%.2f",item.getObpbase())));
        viewHolder.subtotal.setText(""+(String.format("%.2f",item.getTotal())));
        viewHolder.cantidad.setText(""+item.getObpcant());
        viewHolder.caja.setText(""+String.format("%.2f",item.getCajas()));
        viewHolder.descuento.setText(""+String.format("%.2f",item.getDescuento()));
        viewHolder.cantidad.setTag(item);
        tvsubtotal=viewHolder.subtotal;
        tvCantidad=viewHolder.cantidad;
        tvPrecio=viewHolder.price;
        tvCantidadCajas=viewHolder.caja;
        tvDescuento=viewHolder.descuento;
        if (i== items.size()-1){
            viewHolder.cantidad.requestFocus();
        }
        viewHolder.caja.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos =ObtenerPosicionElemento(item);
                if (pos>=0){
                    viewHolder.caja.removeTextChangedListener(this);
                    mView.ModifyItemCaja(pos,s.toString(),item,tvsubtotal,tvCantidad,tvCantidadCajas);
                    viewHolder.caja.addTextChangedListener(this);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                int i=0;
            }
        });

        viewHolder.cantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos =ObtenerPosicionElemento(item);
                if (pos>=0){
                    viewHolder.cantidad.removeTextChangedListener(this);
                    mView.ModifyItem(pos,s.toString(),item,tvsubtotal,tvCantidad,tvCantidadCajas);
                    viewHolder.cantidad.addTextChangedListener(this);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                int i=0;
            }
        });

if (HabilitadoPrecio==1){
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

}else{
    viewHolder.price.setKeyListener(null);
    viewHolder.price.setEnabled(false);
}

        viewHolder.descuento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int pos =ObtenerPosicionElemento(item);
                if (pos>=0){
                    mView.ModifyItemDescuento(pos,charSequence.toString(),item,tvsubtotal,tvDescuento);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView img=(TextView)view;
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