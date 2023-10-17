package com.dynasys.appdisoft.Visitas.Create.SincronizarData.Presentacion;

public interface SincronizarMvp {
    interface View {
        void MarcarDesmarcarTodos(boolean bandera);
        void setPresenter(SincronizarMvp.Presenter presenter);
        void ShowMessageResult(String message);
        void ShowSyncroMgs(String message);

    }
    interface Presenter{
        void GuadarDatos(boolean producto,boolean precio,boolean cliente,boolean pedidos,boolean chkZonas,int ZonaSelected);
    }
}
