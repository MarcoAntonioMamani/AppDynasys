package com.dynasys.appdisoft.SincronizarData.Presentacion;

import com.dynasys.appdisoft.Login.LoginMvp;

public interface SincronizarMvp {
    interface View {
        void MarcarDesmarcarTodos(boolean bandera);
        void setPresenter(SincronizarMvp.Presenter presenter);
        void ShowMessageResult(String message);
        void ShowSyncroMgs(String message);

    }
    interface Presenter{
        void GuadarDatos();
    }
}
