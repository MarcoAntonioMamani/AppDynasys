package com.dynasys.appdisoft.Mapas;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dynasys.appdisoft.Clientes.UtilShare;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


/**
 * Created by usuario on 23/11/2017.
 */

public class CustomInfoWindowAdapterMapa implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener{
    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;
    private ClienteEntity mclient;
    ImageView img ;
    RelativeLayout panel;
    public CustomInfoWindowAdapterMapa(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getInfoContents(final Marker m) {
        //Carga layout personalizado.
        String posicion =m.getSnippet();
        View v = inflater.inflate(R.layout.info_windows_layout, null);
        mclient=UtilShare.ListClientes.get(Integer.parseInt(posicion));
        panel=(RelativeLayout)v.findViewById(R.id.info_cliente);
        img=(ImageView) v.findViewById(R.id.id_img_client);

        panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 int i=0;
            }
        });
        m.setTag(mclient);
       /* Glide.with(v.getContext())
                .load(url)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.2f)
                .override(200, 105)//Target.SIZE_ORIGINAL
                .placeholder(R.drawable.fondoigle)
                .into(img);*/

        ((TextView)v.findViewById(R.id.view_cliente_nombre)).setText(mclient.getNamecliente());
        ((TextView)v.findViewById(R.id.view_cliente_direccion)).setText(mclient.getDireccion());
        ((TextView)v.findViewById(R.id.view_cliente_telefono)).setText("Telf: "+mclient.getTelefono());
        return v;
    }

    @Override
    public View getInfoWindow(Marker m) {

        return null;
    }


    @Override
    public void onInfoWindowClick(Marker marker){
        Object obj=marker;
    }

}
