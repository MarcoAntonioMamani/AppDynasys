package com.dynasys.appdisoft.Login;

import java.util.Date;

public class FechaCaducidad {

    Date Fecha;

    public FechaCaducidad(Date fecha) {
        Fecha = fecha;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }
}
