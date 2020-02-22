package com.dynasys.appdisoft.Login.DB;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ZonasEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ZonaListViewModel extends AndroidViewModel {

    private ZonaRepository mRepository;
    private LiveData<List<ZonasEntity>> users;
    private List<ZonasEntity> stockList;
    public ZonaListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new ZonaRepository(application);
    }



    public List<ZonasEntity> getZonaByRepartidor(int id) throws ExecutionException, InterruptedException {
        return mRepository.getZonaById(id);
    }

    public void insertZona(ZonasEntity user) {
        mRepository.insertZonas(user);
    }



    public void updateZona(ZonasEntity user) {
        mRepository.updateZona(user);
    }

    public void deleteZona(ZonasEntity user) {
        mRepository.deleteZona(user);
    }

    public void deleteAllZonas() {
        mRepository.deleteAllDetalles();
    }
}
