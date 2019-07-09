package com.dynasys.appdisoft.Login.Cloud;

import android.app.Application;

public class MainApplication {
    public static ApiManager apiManager;


    public void MainApplication() {
        apiManager = ApiManager.getInstance();
    }
}
