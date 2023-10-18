package com.dynasys.appdisoft.Pedidos.carrito;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dynasys.appdisoft.Clientes.UtilShare;
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
public void onBindViewHolder(final EmpresasViewHolder clientesViewHolder, final int i) {
    final EditText tvCantidad;
    final EditText tvCantidadCajas;
    final ProductoEntity item;

    item = listaEmpresas.get(i);
    clientesViewHolder.tvNombre.setText(" "+(CharSequence) item.getProducto()+" ");
    clientesViewHolder.tvPrecio.setText("BS "+ String.format("%.2f",item.getPrecio())+" ");
    clientesViewHolder.tvInventario.setText("StockUni="+String.format("%.2f",item.getStock())+" \nStockCaja="+String.format("%.2f",item.getStock()/listaEmpresas.get(i).getConversion())+"\nConversion="+item.getConversion());

    clientesViewHolder.btnAgregar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mview.recyclerViewListClickedProducto(view,item);
        }
    });
    if (listaEmpresas.get(i).getStock()>0){
        clientesViewHolder.tvNombre.setTextColor(activity.getResources().getColor(R.color.secondary_text));
        clientesViewHolder.tvInventario.setTextColor(activity.getResources().getColor(R.color.state_paid));
        clientesViewHolder.btnAgregar.setVisibility(View.VISIBLE);

        if (listaEmpresas.get(i).getTotalUnitario()>0){
            clientesViewHolder.btnAgregar.setVisibility(View.GONE);
            clientesViewHolder.cardProducto.setBackgroundColor(activity.getResources().getColor(R.color.azulclaro));
        }else{
            clientesViewHolder.btnAgregar.setVisibility(View.VISIBLE);
            clientesViewHolder.cardProducto.setBackgroundColor(activity.getResources().getColor(R.color.white));
        }

    }else{
      //  clientesViewHolder.cardProducto.setBackgroundColor(activity.getResources().getColor(R.color.state_canceledclaro));
        clientesViewHolder.tvNombre.setTextColor(activity.getResources().getColor(R.color.AccentFondo));
        clientesViewHolder.tvInventario.setTextColor(activity.getResources().getColor(R.color.AccentFondo));
        clientesViewHolder.btnAgregar.setVisibility(View.GONE);
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
    public Button btnAgregar;
    public EmpresasViewHolder(View v) {
        super(v);
        tvNombre = (TextView) v.findViewById(R.id.id_nameProducto);
        tvPrecio = (TextView) v.findViewById(R.id.id_precioProducto);
        btnAgregar = (Button) v.findViewById(R.id.btnAgregarCarrito);
       cardProducto = (LinearLayout) v.findViewById(R.id.linear_productobackground);
       tvInventario=(TextView) v.findViewById(R.id.id_InventarioProducto);
    }
}

    public void setFilter(List<ProductoEntity> ListaFiltrada) {
        this.listaEmpresas = new ArrayList<>();
        this.listaEmpresas.addAll(ListaFiltrada);
      notifyDataSetChanged();
    }
    public int ObtenerPosicionElemento( ProductoEntity item){
        for (int i = 0; i < UtilShare.listProductFiltrado.size(); i++) {
            if (item.getNumi()==UtilShare.listProductFiltrado.get(i).getNumi()){
                return i;
            }
        }
        return -1;
    }

    public Double getCantidadUnitario(int ProductoId){

        for (int i = 0; i < UtilShare.listProductFiltrado.size(); i++) {
            if(UtilShare.listProductFiltrado.get(i).getNumi()==ProductoId){
                return UtilShare.listProductFiltrado.get(i).getTotalUnitario();
            }
        }
        return 0.0;
    }
    public Double getCantidadCaja(int ProductoId){

        for (int i = 0; i < UtilShare.listProductFiltrado.size(); i++) {
            if(UtilShare.listProductFiltrado.get(i).getNumi()==ProductoId){
                return UtilShare.listProductFiltrado.get(i).getCaja();
            }
        }
        return 0.0;
    }

}

