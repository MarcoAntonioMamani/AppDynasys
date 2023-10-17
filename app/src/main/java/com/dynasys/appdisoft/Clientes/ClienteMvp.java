package com.dynasys.appdisoft.Clientes;

import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.ClienteEntity;

public interface ClienteMvp {
    interface View {
        void recyclerViewListClicked(android.view.View v, ClienteEntity cliente);
        void WhatsappClicked(android.view.View v, ClienteEntity Visita);
    }
    interface Presenter{
        void GuadarDatos();
    }
}
