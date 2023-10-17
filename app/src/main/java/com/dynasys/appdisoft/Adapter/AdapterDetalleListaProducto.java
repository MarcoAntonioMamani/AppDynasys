package com.dynasys.appdisoft.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dynasys.appdisoft.ListaProductos.ListadoProductoMVP;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoViewEntity;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterDetalleListaProducto extends RecyclerView.Adapter<AdapterDetalleListaProducto.PedidosViewHolder> {

    private List<ProductoViewEntity> listaProductos;

    private Context context;

    private ListadoProductoMVP.View mView;
  //  private PagosMvp.View mview;
    public AdapterDetalleListaProducto(Context ctx, List<ProductoViewEntity> s, ListadoProductoMVP.View mView) {
        this.context = ctx;
        this.listaProductos = s;
        this.mView=mView;
       // this.mview=view;

    }
    public AdapterDetalleListaProducto(Context ctx) {
        this.context = ctx;

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }


    @Override
    public void onBindViewHolder(final AdapterDetalleListaProducto.PedidosViewHolder clientesViewHolder, final int i) {

       /* protected TextView tvProducto;
        protected TextView tvSaldo;
        protected EditText etFisico;
        protected TextView tvDiferencia;
        protected TextView tvBolivianos;*/


        clientesViewHolder.tvProducto.setText( ""+listaProductos.get(i).getNombreProducto());
        clientesViewHolder.tvCantidadInicial.setText(ShareMethods.ObtenerDecimalToString(listaProductos.get(i).getCantInicial(),2));
        clientesViewHolder.tvCantidadFinal.setText(ShareMethods.ObtenerDecimalToString(listaProductos.get(i).getCantFinal(),2));
        clientesViewHolder.tvPrecio.setText(ShareMethods.ObtenerDecimalToString(listaProductos.get(i).getPrecio(),2));
        clientesViewHolder.tvStock .setText(ShareMethods.ObtenerDecimalToString(listaProductos.get(i).getStock(),2));






    }

    public int ObtenerPosicionElemento( ProductoViewEntity item){
        for (int i = 0; i < listaProductos.size(); i++) {
            if (item.getProductoId()==listaProductos.get(i).getProductoId()){
                return i;
            }
        }
        return -1;
    }


    @Override
    public AdapterDetalleListaProducto.PedidosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_list_producto, viewGroup, false);

        return new AdapterDetalleListaProducto.PedidosViewHolder(itemView);
    }



    public static class PedidosViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvProducto;
        protected TextView tvCantidadInicial;
        protected TextView tvCantidadFinal;
        protected TextView tvPrecio;
        protected TextView tvStock;
        public PedidosViewHolder(View v) {
            super(v);

            tvProducto = (TextView) v.findViewById(R.id.view_lista_producto_name);
            tvCantidadInicial=(TextView) v.findViewById(R.id.view_listado_producto_cantidadinicial);
            tvCantidadFinal = (TextView) v.findViewById(R.id.view_listado_producto_cantidadfinal);
            tvPrecio = (TextView) v.findViewById(R.id.view_listado_producto_precio);
            tvStock = (TextView) v.findViewById(R.id.view_listado_producto_stock);

        }
    }

    public void setFilter(List<ProductoViewEntity> ListaFiltrada){
        this.listaProductos =new ArrayList<>();
        this.listaProductos.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }
}
