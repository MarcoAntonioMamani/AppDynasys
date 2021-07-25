package com.dynasys.appdisoft.Visitas;


import com.dynasys.appdisoft.Login.DB.Entity.VisitaEntity;

public interface VisitaMvp {
    interface View {
        void recyclerViewListClicked(android.view.View v, VisitaEntity Visita);
        void WhatsappClicked(android.view.View v, VisitaEntity Visita);

    }
    interface Presenter{
        void GuadarDatos();
    }
}
