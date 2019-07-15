package com.dynasys.appdisoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

import java.util.ArrayList;
import java.util.List;

public class ClientesAdapter extends ArrayAdapter<ClienteEntity> {
    private Context context;
    private int resourceId;
    private List<ClienteEntity> items, tempItems, suggestions;

    public ClientesAdapter(@NonNull Context context, int resourceId, List<ClienteEntity> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            ClienteEntity client = getItem(position);
            TextView cliente_Name = (TextView) view.findViewById(R.id.row_cliente_name);
            ImageView clientImageView = view.findViewById(R.id.row_cliente_img);
            TextView client_direccion=(TextView) view.findViewById(R.id.row_cliente_direccion);
            TextView client_telefono=(TextView) view.findViewById(R.id.row_cliente_telefono);

            //imageView.setImageResource(client.getImageUrl());
            cliente_Name.setText(client.getNamecliente());
            client_direccion .setText(client.getDireccion());
            client_telefono.setText(client.getTelefono());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    @Nullable
    @Override
    public ClienteEntity getItem(int position) {
        return items.get(position);
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return clientFilter ;
    }
    private Filter clientFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            ClienteEntity client = (ClienteEntity) resultValue;
            return client.getNamecliente();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                int cantidad=10;
                int contador=0;
                for (ClienteEntity client: tempItems) {
                    if (client.getNamecliente().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        suggestions.add(client);
                        contador+=1;
                        if (contador==cantidad){
                            FilterResults filterResults = new FilterResults();
                            filterResults.values = suggestions;
                            filterResults.count = suggestions.size();
                            return filterResults;
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<ClienteEntity> tempValues = (ArrayList<ClienteEntity>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (ClienteEntity clientObj : tempValues) {
                    add(clientObj);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };

}
