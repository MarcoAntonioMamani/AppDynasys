package com.dynasys.appdisoft.ListarDeudas;

import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;

import java.util.List;

public interface DeudasMvp {
    interface View {
        void recyclerViewListClicked(android.view.View v, DeudaEntity pedido);
        void MostrarDeudas(List<DeudaEntity> clientes);
        void setPresenter(Presenter presenter);

    }
    interface Presenter{
        void CargarDeudas();
    }
}
