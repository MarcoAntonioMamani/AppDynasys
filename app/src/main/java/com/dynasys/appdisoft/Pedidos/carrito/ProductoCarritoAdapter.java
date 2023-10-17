package com.dynasys.appdisoft.Pedidos.carrito;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
public void onBindViewHolder(final EmpresasViewHolder clientesViewHolder, int i) {
    final EditText tvCantidad;
    final EditText tvCantidadCajas;
        clientesViewHolder.tvNombre.setText(" "+(CharSequence) listaEmpresas.get(i).getProducto()+" ");
    clientesViewHolder.tvPrecio.setText("BS "+ String.format("%.2f",listaEmpresas.get(i).getPrecio())+" ");
    clientesViewHolder.tvInventario.setText("StockUni="+String.format("%.2f",listaEmpresas.get(i).getStock())+" \nStockCaja="+String.format("%.2f",listaEmpresas.get(i).getStock()/listaEmpresas.get(i).getConversion())+"\nConversion="+listaEmpresas.get(i).getConversion());
final ProductoEntity item;
    item = listaEmpresas.get(i);
    tvCantidad=clientesViewHolder.cantidadUnitaria;
    tvCantidadCajas=clientesViewHolder.cajas;
    if (listaEmpresas.get(i).getStock()>0){

        clientesViewHolder.tvNombre.setTextColor(activity.getResources().getColor(R.color.secondary_text));
        clientesViewHolder.tvInventario.setTextColor(activity.getResources().getColor(R.color.state_paid));
        clientesViewHolder.cantidadUnitaria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clientesViewHolder.cantidadUnitaria.removeTextChangedListener(this);
                mview.ModifyItemCart(s.toString(),item,tvCantidad,tvCantidadCajas);
                clientesViewHolder.cantidadUnitaria.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clientesViewHolder.cajas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clientesViewHolder.cajas.removeTextChangedListener(this);
                mview.ModifyItemCajaCart(s.toString(),item,tvCantidad,tvCantidadCajas);
                clientesViewHolder.cajas.addTextChangedListener(this);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clientesViewHolder.cantidadUnitaria.setVisibility(View.VISIBLE);
        clientesViewHolder.cajas.setVisibility(View.VISIBLE);
    }else{
      //  clientesViewHolder.cardProducto.setBackgroundColor(activity.getResources().getColor(R.color.state_canceledclaro));
        clientesViewHolder.tvNombre.setTextColor(activity.getResources().getColor(R.color.AccentFondo));
        clientesViewHolder.tvInventario.setTextColor(activity.getResources().getColor(R.color.AccentFondo));
       //clientesViewHolder.cantidadUnitaria.setKeyListener(null);
       // clientesViewHolder.cantidadUnitaria.setEnabled(false);
        //clientesViewHolder.cajas.setKeyListener(null);
       // clientesViewHolder.cajas.setEnabled(false);
        clientesViewHolder.cantidadUnitaria.setVisibility(View.GONE);
        clientesViewHolder.cajas.setVisibility(View.GONE);
    }
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
    protected LinearLayout cardProducto;
    protected TextView tvInventario;
    public EditText cantidadUnitaria;
    public EditText cajas;
    public EmpresasViewHolder(View v) {
        super(v);
        tvNombre = (TextView) v.findViewById(R.id.id_nameProducto);
        tvPrecio = (TextView) v.findViewById(R.id.id_precioProducto);
        cantidadUnitaria = (EditText) v.findViewById(R.id.id_Cantidad_Carrito);
        cajas = (EditText) v.findViewById(R.id.id_Cantidad_Cajas);
       cardProducto = (LinearLayout) v.findViewById(R.id.linear_productobackground);
       tvInventario=(TextView) v.findViewById(R.id.id_InventarioProducto);
    }
}

    public void setFilter(List<ProductoEntity> ListaFiltrada) {
        this.listaEmpresas = new ArrayList<>();
        this.listaEmpresas.addAll(ListaFiltrada);
        //notifyDataSetChanged();
    }

}

