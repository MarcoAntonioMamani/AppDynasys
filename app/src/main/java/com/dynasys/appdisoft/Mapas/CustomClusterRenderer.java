package com.dynasys.appdisoft.Mapas;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRenderer  extends DefaultClusterRenderer<StringClusterItem> {
    private final Context mContext;

    public CustomClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<StringClusterItem> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
    }

    @Override protected void onBeforeClusterItemRendered(StringClusterItem item,
                                                         MarkerOptions markerOptions) {
        BitmapDescriptor  markerDescriptor = BitmapDescriptorFactory. defaultMarker (BitmapDescriptorFactory. HUE_ORANGE );

        markerOptions.icon (markerDescriptor) .snippet (item.title);
    }
}
