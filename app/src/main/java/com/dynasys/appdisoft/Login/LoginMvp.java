package com.dynasys.appdisoft.Login;

public interface LoginMvp {
    interface View {
        void showEmailError();
        void showPasswordError();
        void LoginSuccesfull();
        void setPresenter(Presenter presenter);
        void ShowMessageResult(String message);
    }
    interface Presenter{
        void ValidarLogin(String codigo, String nroDocumento);
    }
}
