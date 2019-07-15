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
import android.widget.TextView;

import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<ProductoEntity> {
    private Context context;
    private int resourceId;
    private List<ProductoEntity> items, tempItems, suggestions;

    public ProductAdapter(@NonNull Context context, int resourceId, List<ProductoEntity> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }
public void setLista(List<ProductoEntity> items){
       /* this.items=items;*/
        this.tempItems=items;
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
            ProductoEntity item = getItem(position);
            TextView product_name = (TextView) view.findViewById(R.id.product_name);
            TextView product_price=(TextView) view.findViewById(R.id.product_price);
            TextView producto_code=(TextView) view.findViewById(R.id.product_code);

            //imageView.setImageResource(client.getImageUrl());
            product_name.setText(item.getProducto());
            product_price .setText(""+item.getPrecio()+" Bs" );
            producto_code.setText(""+item.getCod());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    @Nullable
    @Override
    public ProductoEntity getItem(int position) {
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
        return itemFilter ;
    }
    private Filter itemFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            ProductoEntity item = (ProductoEntity) resultValue;
            return item.getProducto();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                int cantidad=3;
                int contador=0;
                for (ProductoEntity item: tempItems) {
                    String word=charSequence.toString().toUpperCase();
                    String nameProducto=item.getCod()+item.getProducto().toUpperCase();
                    if (nameProducto.trim().contains(word.trim())) {
                        suggestions.add(item);
                        contador+=1;
                        if (contador>=cantidad){
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
            ArrayList<ProductoEntity> tempValues = (ArrayList<ProductoEntity>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (ProductoEntity itemObj : tempValues) {
                    add(itemObj);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };

}
