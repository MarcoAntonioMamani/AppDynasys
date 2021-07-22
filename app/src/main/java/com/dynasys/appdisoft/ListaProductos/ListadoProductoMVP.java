package com.dynasys.appdisoft.ListaProductos;

import android.widget.EditText;
import android.widget.TextView;

import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Pedidos.CreatePedidos.CreatePedidoMvp;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

import java.util.List;

public interface ListadoProductoMVP {

    interface View {

        void StopDialog();


    }
}
