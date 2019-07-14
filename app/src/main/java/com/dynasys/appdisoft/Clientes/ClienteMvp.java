package com.dynasys.appdisoft.Clientes;

import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

public interface ClienteMvp {
    interface View {
        void recyclerViewListClicked(android.view.View v, ClienteEntity cliente);

    }
    interface Presenter{
        void GuadarDatos();
    }
}
