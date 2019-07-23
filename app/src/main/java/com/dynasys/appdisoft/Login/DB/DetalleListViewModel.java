package com.dynasys.appdisoft.Login.DB;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DetalleListViewModel extends AndroidViewModel {

    private DetalleRepository mRepository;
    private LiveData<List<DetalleEntity>> users;

    public DetalleListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new DetalleRepository(application);
    }

    public LiveData<List<DetalleEntity>> getDetalles() {
        if (users == null) {
            users = mRepository.getAllDetalle();
        }

        return users;
    }

    public List<DetalleEntity> getDetalle(String id) throws ExecutionException, InterruptedException {
        return mRepository.getDetalle(id);
    }
    public List<DetalleEntity> getMAllDetalle(int code) throws ExecutionException, InterruptedException {
        return mRepository.getMDetalleAll(code);
    }
    public List<DetalleEntity> getMAllDetalleState(int code) throws ExecutionException, InterruptedException {
        return mRepository.getMDetalleAllState(code);
    }
    public void insertDetalle(DetalleEntity user) {
        mRepository.insertDetalles(user);
    }



    public void updateDetalle(DetalleEntity user) {
        mRepository.updateDetalle(user);
    }
    public void updateListDetalle(DetalleEntity[] user) {
        mRepository.updateListDetalle(user);
    }
    public void deleteDetalle(DetalleEntity user) {
        mRepository.deleteDetalle(user);
    }
    public void deleteAllDetalles() {
        mRepository.deleteAllDetalles();
    }
}
