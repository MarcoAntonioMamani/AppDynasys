package com.dynasys.appdisoft.ListarDeudas.Pagos;

import android.widget.CheckBox;
import android.widget.EditText;

import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;

import java.util.List;

public interface PagosMvp {
    interface View {

        void MostrarListadoPagos(List<DeudaEntity> clientes);
        void setPresenter(Presenter presenter);
        void OnClickCheck(DeudaEntity deuda, boolean Valor, int Posicion, EditText tvMontoPagar);
        void ModifyPago(DeudaEntity deuda, boolean Valor, int Posicion, EditText tvMontoPagar,CheckBox chkPago,String Value);
    }
    interface Presenter{
        void CargarDeudas();
    }
}
