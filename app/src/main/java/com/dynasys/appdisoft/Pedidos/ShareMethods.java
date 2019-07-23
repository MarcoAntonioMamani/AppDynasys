package com.dynasys.appdisoft.Pedidos;

import android.app.ActivityManager;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareMethods {

    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)* Math.pow(10, numeroDecimales);
        resultado= Math.round(resultado);
        resultado=(resultado/ Math.pow(10, numeroDecimales))+parteEntera;
        return resultado;
    }

    public static String ObtenerFecha(Date fecha) {
        String dia = new SimpleDateFormat("dd").format(fecha);
        String ano = new SimpleDateFormat("yyyy").format(fecha);
        String monthName = new SimpleDateFormat("MMMM").format(fecha);
        return dia+" "+monthName+" "+ano;
    }
    public static String ObtenerFecha02(Date fecha) {
        String dia = new SimpleDateFormat("dd").format(fecha);
        String ano = new SimpleDateFormat("yyyy").format(fecha);
        String monthName = new SimpleDateFormat("MM").format(fecha);
        return dia+"/"+monthName+"/"+ano;
    }
    public static String ObtenerDecimalToString(double number, int cantdecimales){
        return String.format("%."+cantdecimales+"f", (number));
    }
    public static String GetTimeToString(Date fecha){
        String hora=(fecha.getHours() < 10)? "0" + String.valueOf(fecha.getHours()): String.valueOf(fecha.getHours());
        String minutos=((fecha.getMinutes() < 10)? "0" + String.valueOf(fecha.getMinutes()): String.valueOf(fecha.getMinutes()));
        String Segundos=(((fecha.getSeconds()) < 10)? "0" + String.valueOf(fecha.getSeconds()): String.valueOf(fecha.getSeconds()));
        return hora+":"+minutos+":"+Segundos;
    }
    public static final boolean IsServiceRunning(Context ctx, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
