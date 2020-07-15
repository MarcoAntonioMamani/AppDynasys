package com.dynasys.appdisoft.Clientes;

import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

import java.util.ArrayList;
import java.util.List;

public class UtilShare {

    public static ClienteEntity cliente=null;
    public static List<ClienteEntity> ListClientes=new ArrayList<>();
    public static FragmentActivity mActivity=null;
    public static ClienteEntity clienteMapa=null;
    public static int tipoAccion=0;
    public static TextView tvZona;

}
