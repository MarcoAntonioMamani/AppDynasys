package com.dynasys.appdisoft.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.dynasys.appdisoft.ListarDeudas.Pagos.PagosMvp;
import com.dynasys.appdisoft.Login.DB.Entity.AlmacenEntity;
import com.dynasys.appdisoft.Login.DB.Entity.AlmacenEntity;
import com.dynasys.appdisoft.Pedidos.ShareMethods;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdapterDetalleFisico extends RecyclerView.Adapter<AdapterDetalleFisico.PedidosViewHolder> {

    private List<AlmacenEntity> listaDeudas;
    private List<ClienteEntity> listClientes;
    private Context context;
  //  private PagosMvp.View mview;
    public AdapterDetalleFisico(Context ctx, List<AlmacenEntity> s) {
        this.context = ctx;
        this.listaDeudas = s;
       // this.mview=view;

    }
    public AdapterDetalleFisico(Context ctx) {
        this.context = ctx;

    }

    @Override
    public int getItemCount() {
        return listaDeudas.size();
    }


    @Override
    public void onBindViewHolder(final AdapterDetalleFisico.PedidosViewHolder clientesViewHolder, final int i) {

       /* protected TextView tvProducto;
        protected TextView tvSaldo;
        protected EditText etFisico;
        protected TextView tvDiferencia;
        protected TextView tvBolivianos;*/


        clientesViewHolder.tvProducto.setText( ""+listaDeudas.get(i).getProducto());
        clientesViewHolder.tvSaldo.setText(ShareMethods.ObtenerDecimalToString(listaDeudas.get(i).getSaldo(),2));
        clientesViewHolder.etFisico.setText(ShareMethods.ObtenerDecimalToString(listaDeudas.get(i).getFisico(),2));
        clientesViewHolder.tvDiferencia.setText(ShareMethods.ObtenerDecimalToString(listaDeudas.get(i).getDiferencia(),2));
        clientesViewHolder.tvBolivianos.setText(ShareMethods.ObtenerDecimalToString(listaDeudas.get(i).getTotalbs(),2));
        final AlmacenEntity item=listaDeudas.get(i);


        clientesViewHolder.etFisico.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos =ObtenerPosicionElemento(item);
                if (pos>=0){
                  //  mview.ModifyPago(item,clientesViewHolder.chkDeuda.isChecked(),pos,clientesViewHolder.etMontoPagar,clientesViewHolder.chkDeuda,s.toString());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                int i=0;
            }
        });


    }

    public int ObtenerPosicionElemento( AlmacenEntity item){
        for (int i = 0; i < listaDeudas.size(); i++) {
            if (item.getProductoId()==listaDeudas.get(i).getProductoId()){
                return i;
            }
        }
        return -1;
    }


    @Override
    public AdapterDetalleFisico.PedidosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_fisico, viewGroup, false);

        return new AdapterDetalleFisico.PedidosViewHolder(itemView);
    }



    public static class PedidosViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvProducto;
        protected TextView tvSaldo;
        protected EditText etFisico;
        protected TextView tvDiferencia;
        protected TextView tvBolivianos;
        public PedidosViewHolder(View v) {
            super(v);

            tvProducto = (TextView) v.findViewById(R.id.view_detalle_fisico_producto);
            tvSaldo=(TextView) v.findViewById(R.id.view_detalle_fisico_saldo);
            etFisico = (EditText) v.findViewById(R.id.view_detalle_fisico_fisico);
            tvDiferencia = (TextView) v.findViewById(R.id.view_detalle_fisico_diferencia);
            tvBolivianos = (TextView) v.findViewById(R.id.view_detalle_fisico_Bs);

        }
    }

    public void setFilter(List<AlmacenEntity> ListaFiltrada){
        this.listaDeudas =new ArrayList<>();
        this.listaDeudas.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }
}
